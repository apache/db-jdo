/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

/*
 * Optimizer.g
 *
 * Created on August 28, 2001
 */

header
{
    package org.apache.jdo.impl.jdoql.jdoqlc;
    
    import java.util.Collection;
    
    import java.math.BigDecimal;
    import java.math.BigInteger;

    import java.lang.reflect.Field;
    import java.security.AccessController;
    import java.security.PrivilegedAction;

    import org.apache.jdo.model.java.JavaField;
    import org.apache.jdo.model.java.JavaType;
    import org.apache.jdo.impl.model.java.PredefinedType;
    import org.apache.jdo.impl.model.java.WrapperClassType;

    import org.apache.jdo.pm.PersistenceManagerInternal;
    import org.apache.jdo.util.I18NHelper;
    import org.apache.jdo.impl.jdoql.tree.*;
    import org.apache.jdo.impl.jdoql.scope.ParameterTable;
}

/**
 * This class defines the optimizer pass of the JDOQL compiler.
 * It takes the typed AST as produced by the smenatic analysis and
 * converts it into a simpler but equivalent typed AST.
 * 
 * @author  Michael Bouschen
 */
class Optimizer extends TreeParser;

options
{
    importVocab = JDOQL;
    buildAST = true;
    defaultErrorHandler = false;
    ASTLabelType = "JDOQLAST"; //NOI18N
}

{
    /** The persistence manager of the query instance. */
    protected PersistenceManagerInternal pm;

    /** The error message support class. */
    protected ErrorMsg errorMsg;
    
    /** The query parameter table */
    protected ParameterTable paramtab;
    
    /** Flag indicating whether query parameers should be included. */
    protected boolean optimizeParameters;

    /** I18N support */
    protected final static I18NHelper msg = I18NHelper.getInstance(
        "org.apache.jdo.impl.jdoql.Bundle", Optimizer.class.getClassLoader()); //NOI18N
    
    /**
     *
     */
    public void init(PersistenceManagerInternal pm,
                     ParameterTable paramtab, ErrorMsg errorMsg)
    {
        this.pm = pm;
        this.errorMsg = errorMsg;
        this.paramtab = paramtab;
        this.optimizeParameters = (paramtab != null);
    }

    /**
     *
     */
    public void reportError(RecognitionException ex) {
        errorMsg.fatal(msg.msg("ERR_OptimizerError", ex)); //NOI18N
    }

    /**
     *
     */
    public void reportError(String s) {
        errorMsg.fatal(msg.msg("ERR_OptimizerError") + s); //NOI18N
    }

    /**
     * Converts the string argument into a single char.
     */
    protected static char parseChar(String text)
    {
        char first = text.charAt(0);
        if (first == '\\') {
            //found escape => check the next char
            char second = text.charAt(1);
            switch (second)
            {
            case 'n': return '\n';
            case 'r': return '\r';
            case 't': return '\t';
            case 'b': return '\b';
            case 'f': return '\f';
            case 'u': 
                // unicode spec
                return (char)Integer.parseInt(text.substring(2, text.length()), 16);
            case '0':            
            case '1':
            case '2':
            case '3':            
            case '4':
            case '5':            
            case '6':
            case '7': 
                // octal spec 
                return (char)Integer.parseInt(text.substring(1, text.length()), 8);
            default : return second;
            }
        }
        return first;
    }

    /**
     * Check an AND operation (BAND, AND) for constant operands 
     * that could be optimized.
     * @param op the AND operator
     * @param left the left operand
     * @param right the right operand
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkAnd(JDOQLAST op, JDOQLAST left, JDOQLAST right)
    {
        JDOQLAST ast = op;

        if (isBooleanValueAST(left)) {
            ast = handleValueAndExpr(op, ((ConstantExpr)left).getValue(), right);
        }
        else if (isBooleanValueAST(right)) {
            ast = handleValueAndExpr(op, ((ConstantExpr)right).getValue(), left);
        }
        return ast;
    }

    /**
     * Check an OR operation (BOR, OR) for constant operands 
     * that could be optimized.
     * @param op the OR operator
     * @param left the left operand
     * @param right the right operand
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkOr(JDOQLAST op, JDOQLAST left, JDOQLAST right)
    {
        JDOQLAST ast = op;

        if (isBooleanValueAST(left)) {
            ast = handleValueOrExpr(op, ((ConstantExpr)left).getValue(), right);
        }
        else if (isBooleanValueAST(right)) {
            ast = handleValueOrExpr(op, ((ConstantExpr)right).getValue(), left);
        }
        return ast;
    }

    /**
     * Check a equality operation (EQUAL, NOT_EQUAL) for constant operands
     * that could be optimized.
     * @param op the equality operator
     * @param left the left operand
     * @param right the right operand
     * @param negate true for not equal operation, false otherwise
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkEqualityOp(JDOQLAST op, JDOQLAST left, 
                                       JDOQLAST right, boolean negate)
    {
        JDOQLAST ast = op;
        
        // case <CONSTANT> <op> <CONSTANT> 
        if ((left.getType() == CONSTANT) && (right.getType() == CONSTANT)) {
            ast = handleValueEqValue(op, left, right, negate);
        }
        // case <boolean CONSTANT> <op> <expr>
        else if (isBooleanValueAST(left)) {
            ast = handleBooleanValueEqExpr(op, ((ConstantExpr)left).getValue(), 
                                           right, negate);
        }
        // case <expr> <op> <boolean CONSTANT>
        else if (isBooleanValueAST(right)) {
            ast = handleBooleanValueEqExpr(op, ((ConstantExpr)right).getValue(),
                                           left, negate);
        }
        return ast;
    }

    /**
     * Check a object equality operation (OBJECT_EQUAL, OBJECT_NOT_EQUAL) 
     * for constant operands that could be optimized.
     * @param op the object equality operator
     * @param left the left operand
     * @param right the right operand
     * @param negate true for not equal operation, false otherwise
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkObjectEqualityOp(JDOQLAST op, JDOQLAST left, 
                                             JDOQLAST right, boolean negate)
    {
        JDOQLAST ast = op;

        if ((left.getType() == CONSTANT) && (right.getType() == CONSTANT)) {
            ast = handleValueEqValue(op, left, right, negate);
        }
        return ast;
    }

    /**
     * Check a collection equality operation (COLLECTION_EQUAL, 
     * COLLECTION_NOT_EQUAL) for constant operands that could be optimized.
     * @param op the collection equality operator
     * @param left the left operand
     * @param right the right operand
     * @param negate true for not equal operation, false otherwise
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkCollectionEqualityOp(JDOQLAST op, JDOQLAST left, 
                                                 JDOQLAST right, boolean negate)
    {
        JDOQLAST ast = op;
        boolean isLeftConstant = (left.getType() == CONSTANT);
        boolean isRightConstant = (right.getType() == CONSTANT);
        
        if (isLeftConstant && isRightConstant) {
            ast = handleValueEqValue(op, left, right, negate);
        }
        return ast;
    }

    /**
     * Check a logical not operation (LNOT) for a constant operand 
     * that could be optimized.
     * @param op the logical not operator
     * @param arg the operand
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkLogicalNotOp(JDOQLAST op, JDOQLAST arg)
    {
        JDOQLAST ast = op;

        if (arg.getType() == CONSTANT) {
            // !value may be calculated at compile time.
            Object valueObj = ((ConstantExpr)arg).getValue();
            Boolean value = (valueObj instanceof Boolean) ? 
                            new Boolean(!((Boolean)valueObj).booleanValue()) :
                            Boolean.TRUE;
            ast = ConstantExpr.newConstant(value);
            ast.setType(CONSTANT);
            ast.setText(value.toString());
            ast.setTypeInfo(op.getTypeInfo());
        }
        return ast;
    }

    /**
     * Check a binary plus operation (PLUS) for constant operands
     * that could be optimized.
     * @param op the plus operator
     * @param left the left operand
     * @param right the right operand
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkBinaryPlusOp(JDOQLAST op, JDOQLAST left, 
                                         JDOQLAST right)
    {
        JDOQLAST ast = op;

        if ((left.getType() == CONSTANT) && (right.getType() == CONSTANT)) {
            Object leftValue = ((ConstantExpr)left).getValue();
            Object rightValue = ((ConstantExpr)right).getValue();
            Object value = null;
            if (leftValue == null)
                value = rightValue;
            else if (rightValue == null)
                value = leftValue;
            else {
                JavaType type = op.getTypeInfo();
                
                if (type.isWrapperClass())
                    type = ((WrapperClassType)type).getWrappedPrimitiveType();
                
                if (type.equals(PredefinedType.intType))
                    value = new Integer(((Number)leftValue).intValue() + 
                        ((Number)rightValue).intValue());
                else if (type.equals(PredefinedType.longType))
                    value = new Long(((Number)leftValue).longValue() + 
                        ((Number)rightValue).longValue());
                else if (type.equals(PredefinedType.floatType))
                    value = new Float(((Number)leftValue).floatValue() + 
                        ((Number)rightValue).floatValue());
                else if (type.equals(PredefinedType.doubleType))
                    value = new Double(((Number)leftValue).doubleValue() + 
                        ((Number)rightValue).doubleValue());
                else if (type.equals(PredefinedType.bigDecimalType))
                    value = getBigDecimalValue(leftValue).add(
                       getBigDecimalValue(rightValue));
                else if (type.equals(PredefinedType.bigIntegerType))
                    value = getBigIntegerValue(leftValue).add(
                        getBigIntegerValue(rightValue));
                else 
                    errorMsg.fatal(msg.msg("ERR_OptmizerInvalidType", //NOI18N
                        "checkBinaryPlusOp", type)); //NOI18N
            }
            ast = ConstantExpr.newConstant(value);
            ast.setType(CONSTANT);
            ast.setText(value.toString());
            ast.setTypeInfo(op.getTypeInfo());
        }
        return ast;
    }
    
    /**
     * Check a string concatenation operation (CONCAT) for constant operands
     * that could be optimized.
     * @param op the concat operator
     * @param left the left operand
     * @param right the right operand
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkConcatOp(JDOQLAST op, JDOQLAST left, JDOQLAST right)
    {
        JDOQLAST ast = op;

        if ((left.getType() == CONSTANT) && (right.getType() == CONSTANT)) {
            Object leftValue = ((ConstantExpr)left).getValue();
            Object rightValue = ((ConstantExpr)right).getValue();
            Object value = null;
            if (leftValue == null)
                value = rightValue;
            else if (rightValue == null)
                value = leftValue;
            else 
                value = leftValue.toString() + rightValue.toString();
            ast = ConstantExpr.newConstant(value);
            ast.setType(CONSTANT);
            ast.setText(value.toString());
            ast.setTypeInfo(op.getTypeInfo());
        }
        return ast;
    }

    /**
     * Check a binary minus operation (MINUS) for constant operands
     * that could be optimized.
     * @param op the minus operator
     * @param left the left operand
     * @param right the right operand
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkBinaryMinusOp(JDOQLAST op, JDOQLAST left, 
                                          JDOQLAST right)
    {
        JDOQLAST ast = op;

        if ((left.getType() == CONSTANT) && (right.getType() == CONSTANT)) {
            Object leftValue = ((ConstantExpr)left).getValue();
            Object rightValue = ((ConstantExpr)right).getValue();
            Object value = null;
            if (rightValue == null)
                value = leftValue;
            else {
                if (leftValue == null)
                    leftValue = new Integer(0);
                
                JavaType type = op.getTypeInfo();
                
                if (type.isWrapperClass())
                    type = ((WrapperClassType)type).getWrappedPrimitiveType();
                
                if (type.equals(PredefinedType.intType))
                    value = new Integer(((Number)leftValue).intValue() -
                        ((Number)rightValue).intValue());
                else if (type.equals(PredefinedType.longType))
                    value = new Long(((Number)leftValue).longValue() -
                        ((Number)rightValue).longValue());
                else if (type.equals(PredefinedType.floatType))
                    value = new Float(((Number)leftValue).floatValue() - 
                        ((Number)rightValue).floatValue());
                else if (type.equals(PredefinedType.doubleType))
                    value = new Double(((Number)leftValue).doubleValue() - 
                        ((Number)rightValue).doubleValue());
                else if (type.equals(PredefinedType.bigDecimalType))
                    value = getBigDecimalValue(leftValue).subtract(
                       getBigDecimalValue(rightValue));
                else if (type.equals(PredefinedType.bigIntegerType))
                    value = getBigIntegerValue(leftValue).subtract(
                        getBigIntegerValue(rightValue));
                else 
                    errorMsg.fatal(msg.msg("ERR_OptmizerInvalidType", //NOI18N
                        "checkBinaryMinusOp", type)); //NOI18N
            }
            ast = ConstantExpr.newConstant(value);
            ast.setType(CONSTANT);
            ast.setText(value.toString());
            ast.setTypeInfo(op.getTypeInfo());
        }
        return ast;
    }
    
    /**
     * Check a binary multiplication operation (STAR) for constant operands
     * that could be optimized.
     * @param op the multiplication operator
     * @param left the left operand
     * @param right the right operand
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkMultiplicationOp(JDOQLAST op, JDOQLAST left, 
                                             JDOQLAST right)
    {
        JDOQLAST ast = op;

        if ((left.getType() == CONSTANT) && (right.getType() == CONSTANT)) {
            Object leftValue = ((ConstantExpr)left).getValue();
            Object rightValue = ((ConstantExpr)right).getValue();
            Object value = null;
            if (leftValue == null)
                leftValue = new Integer(0);
            if (rightValue == null)
                rightValue = new Integer(0);
            JavaType type = op.getTypeInfo();
                
            if (type.isWrapperClass())
                type = ((WrapperClassType)type).getWrappedPrimitiveType();
                
            if (type.equals(PredefinedType.intType))
                value = new Integer(((Number)leftValue).intValue() *
                    ((Number)rightValue).intValue());
            else if (type.equals(PredefinedType.longType))
                value = new Long(((Number)leftValue).longValue() *
                    ((Number)rightValue).longValue());
            else if (type.equals(PredefinedType.floatType))
                value = new Float(((Number)leftValue).floatValue() * 
                    ((Number)rightValue).floatValue());
            else if (type.equals(PredefinedType.doubleType))
                value = new Double(((Number)leftValue).doubleValue() * 
                    ((Number)rightValue).doubleValue());
            else if (type.equals(PredefinedType.bigDecimalType))
                value = getBigDecimalValue(leftValue).multiply(
                    getBigDecimalValue(rightValue));
            else if (type.equals(PredefinedType.bigIntegerType))
                value = getBigIntegerValue(leftValue).multiply(
                    getBigIntegerValue(rightValue));
            else 
                errorMsg.fatal(msg.msg("ERR_OptmizerInvalidType", //NOI18N
                    "checkMultiplicationOp", type)); //NOI18N

            ast = ConstantExpr.newConstant(value);
            ast.setType(CONSTANT);
            ast.setText(value.toString());
            ast.setTypeInfo(op.getTypeInfo());
        }
        return ast;
    }
    
    /**
     * Check a binary division operation (DIV) for constant operands
     * that could be optimized.
     * @param op the division operator
     * @param left the left operand
     * @param right the right operand
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkDivisionOp(JDOQLAST op, JDOQLAST left, 
                                       JDOQLAST right)
    {
        JDOQLAST ast = op;

        if ((left.getType() == CONSTANT) && (right.getType() == CONSTANT)) {
            Object leftValue = ((ConstantExpr)left).getValue();
            Object rightValue = ((ConstantExpr)right).getValue();
            Object value = null;
            if (leftValue == null)
                leftValue = new Integer(0);
            if (rightValue == null)
                // division by zero!
                rightValue = new Integer(0);

            JavaType type = op.getTypeInfo();
            
            if (type.isWrapperClass())
                type = ((WrapperClassType)type).getWrappedPrimitiveType();
                
            if (type.equals(PredefinedType.intType))
                value = new Integer(((Number)leftValue).intValue() /
                    ((Number)rightValue).intValue());
            else if (type.equals(PredefinedType.longType))
                value = new Long(((Number)leftValue).longValue() /
                    ((Number)rightValue).longValue());
            else if (type.equals(PredefinedType.floatType))
                value = new Float(((Number)leftValue).floatValue() / 
                    ((Number)rightValue).floatValue());
            else if (type.equals(PredefinedType.doubleType))
                value = new Double(((Number)leftValue).doubleValue() / 
                    ((Number)rightValue).doubleValue());
            else if (type.equals(PredefinedType.bigDecimalType))
                value = getBigDecimalValue(leftValue).divide(
                   getBigDecimalValue(rightValue), BigDecimal.ROUND_HALF_EVEN);
            else if (type.equals(PredefinedType.bigIntegerType))
                value = getBigIntegerValue(leftValue).divide(
                    getBigIntegerValue(rightValue));
            else 
                errorMsg.fatal(msg.msg("ERR_OptmizerInvalidType", //NOI18N
                    "checkDivisionOp", type)); //NOI18N

            ast = ConstantExpr.newConstant(value);
            ast.setType(CONSTANT);
            ast.setText(value.toString());
            ast.setTypeInfo(op.getTypeInfo());
        }
        return ast;
    }
    
    /**
     * Check a unary minus operation (UNARY_MINUS) for a constant operand
     * that could be optimized.
     * @param op the unary minus operator
     * @param arg the operand
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST checkUnaryMinusOp(JDOQLAST op, JDOQLAST arg)
    {
        JDOQLAST ast = op;
        
        if (arg.getType() == CONSTANT) {
            Object value = ((ConstantExpr)arg).getValue();
            JavaType type = op.getTypeInfo();
            Object negate = null;
            
            if (type.equals(PredefinedType.intType))
                negate = new Integer(-((Number)value).intValue());
            else if (type.equals(PredefinedType.longType))
                negate = new Long(-((Number)value).longValue());
            else if (type.equals(PredefinedType.floatType))
                negate = new Float(-((Number)value).floatValue());
            else if (type.equals(PredefinedType.doubleType))
                negate = new Double(-((Number)value).doubleValue());
            else if (type.equals(PredefinedType.bigDecimalType))
                negate = getBigDecimalValue(value).negate();
            else if (type.equals(PredefinedType.bigIntegerType))
                negate = getBigIntegerValue(value).negate();
            else 
                errorMsg.fatal(msg.msg("ERR_OptmizerInvalidType", //NOI18N
                    "checkUnaryMinusOp", type)); //NOI18N
            
            ast = ConstantExpr.newConstant(negate);
            ast.setType(CONSTANT);
            ast.setText(negate.toString());
            ast.setTypeInfo(op.getTypeInfo());
        }
        return ast;
    }

    /**
     * Converts the specified value into a BigDecimal value. 
     * @param value value to be converted
     * @return BigDecimal representation
     */
    protected BigDecimal getBigDecimalValue(Object value)
    {
        BigDecimal ret = null;
        if (value instanceof BigDecimal)
            ret = (BigDecimal)value;
        else if (value instanceof BigInteger)
            ret = new BigDecimal((BigInteger)value);
        else if (value instanceof Double)
            ret = new BigDecimal(((Double)value).doubleValue());
        else if (value instanceof Float)
            ret = new BigDecimal(((Float)value).doubleValue());
        else if (value instanceof Number)
            ret = BigDecimal.valueOf(((Number)value).longValue());
        else
            errorMsg.fatal(msg.msg("ERR_OptmizerNumberExpected", //NOI18N
                "getBigDecimalValue", value)); //NOI18N
        return ret;
    }

    /**
     * Converts the specified value into a BigInteger value. 
     * @param value value to be converted
     * @return BigInteger representation
     */
    protected BigInteger getBigIntegerValue(Object value)
    {
        BigInteger ret = null;

        if (value instanceof BigInteger)
            ret = (BigInteger)value;
        else if (value instanceof Number)
            ret = BigInteger.valueOf(((Number)value).longValue());
        else
            errorMsg.fatal(msg.msg("ERR_OptmizerNumberExpected", //NOI18N
                "getBigIntegerValue", value)); //NOI18N
        return ret;
    }
    
    /**
     * This method is called in the case of an equality operation having two 
     * constant operands. It calculates the result of this constant operation 
     * and returns a JDOQLAST node representing a constant boolean value.
     * @param op the equality operator
     * @param left the left operand
     * @param right the right operand
     * @param negate true for not equal operation, false otherwise
     * @return optimized JDOQLAST 
     */
    protected JDOQLAST handleValueEqValue(JDOQLAST op, JDOQLAST left,
                                          JDOQLAST right, 
                                          boolean negate)
    {
        Object leftValue = ((ConstantExpr)left).getValue();
        Object rightValue = ((ConstantExpr)right).getValue();
        boolean booleanValue = false;
        
        if ((leftValue == null) && (rightValue == null)) {
            // both values are null -> true
            booleanValue = true;
        }
        else if ((leftValue != null) && (rightValue != null)) {
            // both values are not null -> use equals
            booleanValue = leftValue.equals(rightValue);
        }
        else
        {
            // one value is null, the other is not null -> false
            booleanValue = false;
        }
        if (negate) {
            booleanValue = !booleanValue;
        }
        
        Boolean value = booleanValue ? Boolean.TRUE : Boolean.FALSE;
        ConstantExpr constant = ConstantExpr.newConstant(value);
        constant.setType(CONSTANT);
        constant.setText(value.toString());
        constant.setTypeInfo(op.getTypeInfo());
        return op;
    }

    /**
     * This method is called in the case of an equality operation having 
     * a boolean constant operand and a non constant operand. 
     * It returns the non constant operand either as it is or inverted, 
     * depending on the equality operation.
     * @param op the equality operator
     * @param value the contant boolean value
     * @param expr the non constant operand
     * @param negate true for not equal operation, false otherwise
     * @return optimized JDOQLAST 
     */
    private JDOQLAST handleBooleanValueEqExpr(JDOQLAST op, Object value, 
                                              JDOQLAST expr, boolean negate)
    {
        JDOQLAST ast;
        boolean skip = (value instanceof Boolean) ? 
                       ((Boolean)value).booleanValue() : false;
        if (negate) skip = !skip;

        if (skip) {
            // expr == true -> expr
            // expr != false -> expr
            ast = expr;
        }
        else {
            // if expr is a equality op or a not op the invert operation 
            // may be "inlined":
            //   (expr1 == expr2) != true -> expr1 != expr2
            //   (expr1 != expr2) != true -> expr1 == expr2
            //   !expr != true -> expr
            //   !expr == false -> expr
            // Otherwise wrap the expr with a not op
            //   expr != true -> !expr
            //   expr == false -> !expr
            switch (expr.getType()) {
            case EQUAL:
                ast = new NotEqualsExpr();
                ast.initialize(NOT_EQUAL, "!=", PredefinedType.booleanType); //NOI18N
                ast.setFirstChild(expr.getFirstChild());
                ((BinaryExpr)ast).setCommonOperandType(
                    ((BinaryExpr)expr).getCommonOperandType());
                break;
            case NOT_EQUAL:
                ast = new EqualsExpr();
                ast.initialize(EQUAL, "==", PredefinedType.booleanType); //NOI18N
                ast.setFirstChild(expr.getFirstChild());
                ((BinaryExpr)ast).setCommonOperandType(
                    ((BinaryExpr)expr).getCommonOperandType());
                break;
            case LNOT:
                ast = (JDOQLAST)expr.getFirstChild();
                break;
            default:
                ast = new NotExpr();
                ast.initialize(LNOT, "!", PredefinedType.booleanType); //NOI18N
                ast.setFirstChild(expr);
            }
            expr.setNextSibling(null);
        }
        return ast;
    }

    /**
     * This method is called in the case of an AND operation having at least 
     * one constant operand. If the constant operand evaluates to true it 
     * returns the other operand. If it evaluates to false it returns an AST
     * representing the constant boolean value false.
     * @param op the AND operator
     * @param value the value of the contsnat operand
     * @param expr the other operand
     * @return optimized JDOQLAST 
     */
    private JDOQLAST handleValueAndExpr(JDOQLAST op, Object value, JDOQLAST expr)
    {
        JDOQLAST ast;

        if ((value instanceof Boolean) && ((Boolean)value).booleanValue()) {
            // true AND expr -> expr
            // expr AND true -> expr
            expr.setNextSibling(null);
            ast = expr;
        }
        else
        {
            // false AND expr -> false
            // expr AND false -> false
            ast = ConstantExpr.newConstant(Boolean.FALSE);
            ast.setType(CONSTANT);
            ast.setText("false"); //NOI18N
            ast.setTypeInfo(op.getTypeInfo());
        }
        return ast;
    }

    /**
     * This method is called in the case of an OR operation having at least 
     * one constant operand. If the constant operand evaluates to false it 
     * returns the other operand. If it evaluates to true it returns an AST
     * representing the constant boolean value true.
     * @param op the AND operator
     * @param value the value of the constant operand
     * @param expr the other operand
     * @return optimized JDOQLAST 
     */
    private JDOQLAST handleValueOrExpr(JDOQLAST op, Object value, JDOQLAST expr)
    {
        JDOQLAST ast;

        if ((value instanceof Boolean) && ((Boolean)value).booleanValue()) {
            // true OR expr -> true
            // expr OR true -> true
            ast = ConstantExpr.newConstant(Boolean.TRUE);
            ast.setType(CONSTANT);
            ast.setText("true"); //NOI18N
            ast.setTypeInfo(op.getTypeInfo());
        }
        else {
            // false OR expr -> expr
            // expr OR false -> expr
            expr.setNextSibling(null);
            ast = expr;
        }
        return ast;
    }

    /**
     * Returns true if the specified AST represents a constant boolean value.
     */
    protected boolean isBooleanValueAST(JDOQLAST ast)
    {
        return (ast.getType() == CONSTANT) && 
               (PredefinedType.booleanType.equals(ast.getTypeInfo()));
    }

    /**
     * Returns the specified string w/o a long type suffix 'l' or 'L' or
     * the specified string if there is no such suffix.
     */
    private String skipLongTypeSuffix(String txt)
    {
        int index = txt.length() - 1;
        char last = txt.charAt(index);
        return ((last == 'l') || (last == 'L')) ? txt.substring(0, index) : txt;
    }

    /**
     * Returns the value of the specified field of the specified object.
     * The method uses StateManager methods to access the field value, 
     * if it is a managed field of a persistent instance. 
     * Otherwise it uses reflection.
     * @param javaField the field representation
     * @param pm the pm used in case of a managed field of a persistent instance
     * @param object the instance
     * @return the field value
     */
    private Object getFieldValue(JavaField javaField, 
                                 PersistenceManagerInternal pm, 
                                 Object object)
    {
        int fieldNumber = 
            TypeSupport.getFieldNumber(javaField, pm, object);
        if (fieldNumber == -1) 
            return TypeSupport.getFieldValue(getAccessibleField(javaField), 
                                             object);
        else
            return TypeSupport.getFieldValue(fieldNumber, pm, object);
    }

    /**
     * Returns the value of the specified static field.
     * @param javaField the field representation
     * @return the field value
     */
    private Object getStaticFieldValue(JavaField javaField)
    {
        return TypeSupport.getFieldValue(getAccessibleField(javaField), null);
    } 

    /**
     * Method executing TypeSupport.getAccessibleField in a doPrivileged block.
     */
    private Field getAccessibleField(final JavaField javaField)
    {
        return (Field) AccessController.doPrivileged(
            new PrivilegedAction() {
                public Object run () {
                    return TypeSupport.getAccessibleField(javaField);
                }});
    }

}

// rules

query!
    :   #(  q:QUERY_TREE
            c:candidateClass
            p:parameters
            v:variables
            o:ordering
            f:filter
        )
        {
            Tree tree = new Tree((CandidateClassImpl)#c, (ParameterDecl)#p, 
                                 (VariableDecl)#v, (OrderingExpr)#o, (Expr)#f);
            tree.setType(QUERY_TREE);
            tree.setText("QUERY_TREE"); //NOI18N
            #query = tree;
        }
    ;

// ----------------------------------
// rules: candidate class
// ----------------------------------

candidateClass
{   
    errorMsg.setContext("setCandidates"); //NOI18N
}
    :   #(CANDIDATE_CLASS TYPE)
    ;

// ----------------------------------
// rules: parameter declaration
// ----------------------------------

parameters
{   
    errorMsg.setContext("declareParameters"); //NOI18N
}
    :   ( declareParameter )*
    ;

declareParameter
    :   #( PARAMETER_DECL TYPE ( IDENT )? )
    ;

// ----------------------------------
// rules: variable declaration
// ----------------------------------

variables 
{ 
    errorMsg.setContext("declareVariables"); //NOI18N
}
    :   ( declareVariable )*
    ;

declareVariable
    :   #( VARIABLE_DECL TYPE ( IDENT )? )
    ;

// ----------------------------------
// rules: ordering specification
// ----------------------------------

ordering 
{   
    errorMsg.setContext("setOrdering"); //NOI18N
}
    :   ( orderSpec )*
    ;

orderSpec
    :   #( ASCENDING expression )
    |   #( DESCENDING expression )
    ;

// ----------------------------------
// rules: filer expression
// ----------------------------------

filter
{   
    errorMsg.setContext("setFilter"); //NOI18N
}
    :   expression
    ;

expression 
    :   primary
    |   bitwiseExpr
    |   conditionalExpr
    |   relationalExpr
    |   binaryArithmeticExpr
    |   unaryArithmeticExpr
    |   complementExpr
    ;

bitwiseExpr
    :   #( op1:BAND left1:expression right1:expression )
        {
            #bitwiseExpr = checkAnd(#op1, #left1, #right1);
        }
    |   #( op2:BOR  left2:expression right2:expression )
        {
            #bitwiseExpr = checkOr(#op2, #left2, #right2);
        }
    ;

conditionalExpr
    :   #( op1:AND left1:expression right1:expression )
        {
            #conditionalExpr = checkAnd(#op1, #left1, #right1);
        }
    |   #( op2:OR  left2:expression right2:expression )
        {
            #conditionalExpr = checkOr(#op2, #left2, #right2);
        }
   ;

relationalExpr
    :   #( op1:EQUAL left1:expression right1:expression )
        {
            #relationalExpr = checkEqualityOp(#op1, #left1, #right1, false);
        }
    |   #( op2:NOT_EQUAL left2:expression right2:expression )
        {
            #relationalExpr = checkEqualityOp(#op2, #left2, #right2, true);
        }
    |   #( op3:OBJECT_EQUAL left3:expression right3:expression ) 
        {
            #relationalExpr = checkObjectEqualityOp(#op3, #left3, #right3, false);
        }
    |   #( op4:OBJECT_NOT_EQUAL left4:expression right4:expression )
        {
            #relationalExpr = checkObjectEqualityOp(#op4, #left4, #right4, true);
        }
    |   #( op5:COLLECTION_EQUAL left5:expression right5:expression )
        {
            #relationalExpr = checkCollectionEqualityOp(#op5, #left5, #right5, false);
        }
    |   #( op6:COLLECTION_NOT_EQUAL left6:expression right6:expression )
        {
            #relationalExpr = checkCollectionEqualityOp(#op6, #left6, #right6, true);
        }
    |   #( LT expression expression )
    |   #( GT expression expression )
    |   #( LE expression expression )
    |   #( GE expression expression )
    ;

binaryArithmeticExpr
    :   #( op1:PLUS   left1:expression right1:expression )
        {
            #binaryArithmeticExpr = checkBinaryPlusOp(#op1, #left1, #right1);
        }
    |   #( op2:CONCAT left2:expression right2:expression )
        {
            #binaryArithmeticExpr = checkConcatOp(#op2, #left2, #right2);
        }
    |   #( op3:MINUS  left3:expression right3:expression )
        {
            #binaryArithmeticExpr = checkBinaryMinusOp(#op3, #left3, #right3);
        }
    |   #( op4:STAR   left4:expression right4:expression )
        {
            #binaryArithmeticExpr = checkMultiplicationOp(#op4, #left4, #right4);
        }
    |   #( op5:DIV    left5:expression right5:expression )
        {
            #binaryArithmeticExpr = checkDivisionOp(#op5, #left5, #right5);
        }
    ;

unaryArithmeticExpr 
    :   #( UNARY_PLUS expression )
    |   ( #( UNARY_MINUS ( INT_LITERAL | LONG_LITERAL ) ) )=> 
        #( UNARY_MINUS l:integralLiteral[true] )
        { 
            #unaryArithmeticExpr = #l; 
        }
    |   #( op2:UNARY_MINUS arg2:expression )
        {
            #unaryArithmeticExpr = checkUnaryMinusOp(#op2, #arg2);
        }
    ;

complementExpr 
    :   #( op1:BNOT arg1:expression )
    |   #( op2:LNOT arg2:expression )
        {
            #complementExpr = checkLogicalNotOp(#op2, #arg2);
        }
    ;

primary 
    :   #( CAST TYPE expression )
    |   literal
    |   THIS
    |   parameterAccess
    |   variableAccess
    |   staticFieldAccess
    |   fieldAccess
    |   navigation
    |   contains
    |   startsWith
    |   endsWith
    |   isEmpty
    ;

literal
    :   b1:TRUE
        { 
            ConstantExpr constant = ConstantExpr.newConstant(Boolean.TRUE);
            constant.setType(CONSTANT);
            constant.setText("true"); //NOI18N
            constant.setTypeInfo(b1.getTypeInfo());
            #literal = constant;
        }
    |   b2:FALSE
        { 
            ConstantExpr constant = ConstantExpr.newConstant(Boolean.FALSE);
            constant.setType(CONSTANT);
            constant.setText("false"); //NOI18N
            constant.setTypeInfo(b2.getTypeInfo());
            #literal = constant;
        }
    |   b3:BOOLEAN_LITERAL
        {
            b3.setType(CONSTANT);
        }
    |   integralLiteral[false]
    |   f:FLOAT_LITERAL
        {  
            String txt = f.getText();
            Float value = null;
            try {
                value = new Float(txt);
            } 
            catch (NumberFormatException ex) {
                errorMsg.error(f.getLine(), f.getColumn(), 
                    msg.msg("EXC_InvalidLiteral", "float", txt)); //NOI18N
            }
            ConstantExpr constant = ConstantExpr.newConstant(value);
            constant.setType(CONSTANT);
            constant.setText(f.getText());
            constant.setTypeInfo(f.getTypeInfo());
            #literal = constant;
        }
    |   d:DOUBLE_LITERAL
        {  
            String txt = d.getText();
            Double value = null;
            try {
                value = new Double(txt);
            } 
            catch (NumberFormatException ex) {
                errorMsg.error(d.getLine(), d.getColumn(), 
                    msg.msg("EXC_InvalidLiteral", "double", txt)); //NOI18N
            }
            ConstantExpr constant = ConstantExpr.newConstant(value);
            constant.setType(CONSTANT);
            constant.setText(d.getText());
            constant.setTypeInfo(d.getTypeInfo());
            #literal = constant;
        }
    |   c:CHAR_LITERAL
        { 
            Character value = new Character(parseChar(c.getText())); 
            ConstantExpr constant = ConstantExpr.newConstant(value);
            constant.setType(CONSTANT);
            constant.setText(c.getText());
            constant.setTypeInfo(c.getTypeInfo());
            #literal = constant;
        }
    |   s:STRING_LITERAL
        { 
            ConstantExpr constant = ConstantExpr.newConstant(s.getText());
            constant.setType(CONSTANT);
            constant.setText(s.getText());
            constant.setTypeInfo(s.getTypeInfo());
            #literal = constant;
        }
    |   n:NULL
        { 
            ConstantExpr constant = ConstantExpr.newConstant(null);
            constant.setType(CONSTANT);
            constant.setText("null"); //NOI18N
            constant.setTypeInfo(n.getTypeInfo());
            #literal = constant;
        }
    |   by:BYTE_LITERAL
        {
            by.setType(CONSTANT);
        }
    |   sh:SHORT_LITERAL
        {
            sh.setType(CONSTANT);
        }
    |   con:CONSTANT
    ;

integralLiteral! [boolean negate]
    :   i:INT_LITERAL
        { 
            String txt = negate ? "-" + i.getText() : i.getText();
            Integer value = null;
            try {
                value = Integer.decode(txt);
            } 
            catch (NumberFormatException ex) {
                errorMsg.error(i.getLine(), i.getColumn(), 
                    msg.msg("EXC_InvalidLiteral", "int", txt)); //NOI18N
            }
            ConstantExpr constant = ConstantExpr.newConstant(value);
            constant.setType(CONSTANT);
            constant.setText(txt);
            constant.setTypeInfo(i.getTypeInfo());
            #integralLiteral = constant;
        }
    |   l:LONG_LITERAL
        {   
            String txt = negate ? "-" + l.getText() : l.getText();
            Long value = null;
            try {
                value = Long.decode(skipLongTypeSuffix(txt));
            } 
            catch (NumberFormatException ex) {
                errorMsg.error(l.getLine(), l.getColumn(), 
                    msg.msg("EXC_InvalidLiteral", "long", txt)); //NOI18N
            }
            ConstantExpr constant = ConstantExpr.newConstant(value);
            constant.setType(CONSTANT);
            constant.setText(txt);
            constant.setTypeInfo(l.getTypeInfo());
            #integralLiteral = constant;
        }
    ;

parameterAccess
    :   p:PARAMETER_ACCESS
        {
            if (optimizeParameters) {
                Object value = paramtab.getValue(#p.getText());
                ConstantExpr constant = ConstantExpr.newConstant(value);
                constant.setType(CONSTANT);
                constant.setText((value==null) ? "null" : value.toString()); //NOI18N
                constant.setTypeInfo(#p.getTypeInfo());
                #parameterAccess = constant;
            }
        }
    ;

variableAccess
    :   #( VARIABLE_ACCESS ( expression )? )
    ;

staticFieldAccess!
    :   #( s:STATIC_FIELD_ACCESS t:TYPE (i:IDENT)? )
        {
            // Calculate the value of the static field at compile time
            // and treat it as constant value.
            StaticFieldAccessExpr fieldAccess = (StaticFieldAccessExpr)#s;
            String fieldName = fieldAccess.getName();
            JavaField javaField = #t.getTypeInfo().getJavaField(fieldName);
            Object value = getStaticFieldValue(javaField);
            ConstantExpr constant = ConstantExpr.newConstant(value);
            constant.setType(CONSTANT);
            constant.setText((value==null) ? "null" : value.toString()); //NOI18N
            constant.setTypeInfo(#s.getTypeInfo());
            #staticFieldAccess = constant;
        }
    ;

fieldAccess
    :   #( f:FIELD_ACCESS o:expression ( i:IDENT )? )
        {
            if (#o.getType() == CONSTANT) {
                // If the object of the field access is a constant value, 
                // evaluate the field access at compile time and 
                // treat the expression as constant value.
                FieldAccessExpr fieldAccess = (FieldAccessExpr)#f;
                String fieldName = fieldAccess.getName();
                JavaField javaField = #o.getTypeInfo().getJavaField(fieldName);
                Object value = getFieldValue(
                    javaField, pm, ((ConstantExpr)#o).getValue());
                ConstantExpr constant = ConstantExpr.newConstant(value);
                constant.setType(CONSTANT);
                constant.setText((value==null) ? "null" : value.toString()); //NOI18N
                constant.setTypeInfo(#f.getTypeInfo());
                #fieldAccess = constant;
            }
        }
    ;

navigation
    :   #(  n:NAVIGATION o:expression ( i:IDENT )? )
        {
            if (#o.getType() == CONSTANT) {
                // If the object of the navigation is a constant value, 
                // evaluate the field access at compile time and 
                // treat the expression as constant value.
                FieldAccessExpr fieldAccess = (FieldAccessExpr)#n;
                String fieldName = fieldAccess.getName();
                JavaField javaField = #o.getTypeInfo().getJavaField(fieldName);
                Object value = getFieldValue(
                    javaField, pm, ((ConstantExpr)#o).getValue());
                ConstantExpr constant = ConstantExpr.newConstant(value);
                constant.setType(CONSTANT);
                constant.setText((value==null) ? "null" : value.toString()); //NOI18N
                constant.setTypeInfo(#n.getTypeInfo());
                #navigation = constant;
            }
        }
    ;

contains
    :   #( CONTAINS expression expression )
    ;

startsWith
    :   #( STARTS_WITH expression expression ) 
    ;

endsWith
    :   #( ENDS_WITH expression expression ) 
    ;

isEmpty
    :   #( op:IS_EMPTY e:expression)
        {
            if (#e.getType() == CONSTANT) {
                // If the expression that specifies the collection is a 
                // constant value, evaluate the isEmpty call at compile time 
                // and treat the expression as constant value.
                Object object = ((ConstantExpr)#e).getValue();
                Object value = null;
                if (object == null) {
                    value = new Boolean(false);
                }
                else if (object instanceof Collection) {
                    value = new Boolean(((Collection)object).isEmpty());
                }
                else {
                    errorMsg.fatal(msg.msg("ERR_OptmizerCollectionExpected", //NOI18N
                            "isEmpty", object)); //NOI18N
                }
                ConstantExpr constant = ConstantExpr.newConstant(value);
                constant.setType(CONSTANT);
                constant.setText((value==null) ? "null" : value.toString()); //NOI18N
                constant.setTypeInfo(#op.getTypeInfo());
                #isEmpty = constant;
            }
        }
    ;

