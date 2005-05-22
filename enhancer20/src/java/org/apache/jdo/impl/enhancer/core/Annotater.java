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


package org.apache.jdo.impl.enhancer.core;

import java.util.Iterator;

import org.apache.jdo.impl.enhancer.classfile.ClassFile;
import org.apache.jdo.impl.enhancer.classfile.ClassMethod;
import org.apache.jdo.impl.enhancer.classfile.CodeAttribute;
import org.apache.jdo.impl.enhancer.classfile.ConstClass;
import org.apache.jdo.impl.enhancer.classfile.ConstFieldRef;
import org.apache.jdo.impl.enhancer.classfile.ConstMethodRef;
import org.apache.jdo.impl.enhancer.classfile.ConstNameAndType;
import org.apache.jdo.impl.enhancer.classfile.ConstantPool;
import org.apache.jdo.impl.enhancer.classfile.Descriptor;
import org.apache.jdo.impl.enhancer.classfile.Insn;
import org.apache.jdo.impl.enhancer.classfile.InsnConstOp;
import org.apache.jdo.impl.enhancer.classfile.VMConstants;
import org.apache.jdo.impl.enhancer.meta.EnhancerMetaData;
import org.apache.jdo.impl.enhancer.util.Support;





/**
 * Handles the augmentation actions for a method.
 */
class Annotater
    extends Support
    implements VMConstants
{
    /**
     * The classfile's enhancement controller.
     */
    private final Controller control;

    /**
     * The class analyzer for this class.
     */
    private final Analyzer analyzer;

    /**
     * The classfile to be enhanced.
     */
    private final ClassFile classFile;

    /**
     * The class name in user ('.' delimited) form.
     */
    private final String userClassName;

    /**
     * The classfile's constant pool.
     */
    private final ConstantPool pool;

    /**
     * Repository for the enhancer options.
     */
    private final Environment env;

    /**
     * Repository for JDO meta-data on classes.
     */
    private final EnhancerMetaData meta;

    /**
     * Constructor
     */
    public Annotater(Controller control,
                     Analyzer analyzer,
                     Environment env)
    {
        affirm(control != null);
        affirm(analyzer != null);
        affirm(env != null);

        this.control = control;
        this.analyzer = analyzer;
        this.env = env;
        this.meta = env.getEnhancerMetaData();
        this.classFile = control.getClassFile();
        this.userClassName = classFile.userClassName();
        this.pool = classFile.pool();

        affirm(classFile != null);
        affirm(userClassName != null);
        affirm(meta != null);
        affirm(pool != null);
    }

    /**
     * Performs necessary annotation actions on the class.
     */
    public void annotate()
    {
        affirm(analyzer.isAnnotateable() && !env.noAnnotate());
        env.message("annotating class " + userClassName);

        boolean annotated = false;
        for (final Iterator i = analyzer.getAnnotatableMethods().iterator();
             i.hasNext();) {
            final ClassMethod method = (ClassMethod)i.next();
            annotated |= annotated(method);
        }
        
        // notify controller if class changed
        if (annotated) {
            control.noteUpdate();
        }

//^olsen: reenable
/*
        //@olsen: do special annotation if detected super.clone()
        if ((annotate & SuperClone) != 0) {
            //final String superName = control.classFile().superName().asString();
            //annotateClone(method, superName);
        }
*/
    }

    /**
     * Annotate the class method.  For now, brute force rules.
     */
    private boolean annotated(ClassMethod method)
    {
        boolean annotated = false;
        final CodeAttribute codeAttr = method.codeAttribute();
        if (codeAttr == null) {
            return annotated;
        }

        env.message(
            "annotating: " + userClassName
            + "." + method.name().asString()
            + Descriptor.userMethodArgs(method.signature().asString()));
        
        // first instruction is a target
        final Insn firstInsn = codeAttr.theCode();
        affirm(firstInsn.opcode() == Insn.opc_target);
        Insn insn = firstInsn.next();
        while (insn != null) {
            switch(insn.opcode()) {
            case opc_getfield:
            case opc_putfield:
                final Insn newInsn = insnAnnotation(insn);
                if (insn != newInsn) {
                    annotated = true;
                }
                insn = newInsn;
                break;
            default:
            }

            insn = insn.next();
        }

        return annotated;
    }

    /**
     * Generate annotations for put/getfield instructions.
     */
    private Insn insnAnnotation(final Insn insn)
    {
        if (false) {
            System.out.println("MethodAnnotator.insnAnnotation(): ");
            insn.printInsn(System.out);
            System.out.println();
        }

        affirm(insn.opcode() == opc_getfield || insn.opcode() == opc_putfield);
        final boolean isGet = (insn.opcode() == opc_getfield);

        // get the instruction arguments
        final InsnConstOp fieldInsn = (InsnConstOp)insn;
        final ConstFieldRef fieldRef = (ConstFieldRef)fieldInsn.value();

        final ConstNameAndType fieldNameAndType = fieldRef.nameAndType();
        final String fieldName = fieldNameAndType.name().asString();
        final String fieldType = fieldNameAndType.signature().asString();

        final String qualifyingClassName = fieldRef.className().asString();
        // get the field's declaring class from the model
        final String declClassName =
            meta.getDeclaringClass(qualifyingClassName, fieldName);
        affirm(declClassName != null, "Cannot get declaring class of " 
               + qualifyingClassName + "." + fieldName);
        final ConstClass declClass = pool.addClass(declClassName);

        // check if field is known to be non-managed
        if (meta.isKnownNonManagedField(declClassName, fieldName, fieldType)) {
            return insn;
        }

        // never annotate a jdo field; such may occur in pre-enhanced clone()
        if (meta.isPersistenceCapableClass(declClassName)
            && (fieldName.equals(JDOConstants.JDO_PC_jdoStateManager_Name)
                || fieldName.equals(JDOConstants.JDO_PC_jdoFlags_Name))) {
            return insn;
        }

        if (false) {
            System.out.println("    " + (isGet ? "get" : "put") + "field "
                               + declClassName + "." + fieldName
                               + " : " + fieldType);
        }

        final String methodName;
        final String methodSig;
        if (isGet) {
            methodName = "jdoGet" + fieldName;
            methodSig = "(L" + declClassName + ";)" + fieldType;
        } else {
            methodName = "jdoSet" + fieldName;
            methodSig = "(L" + declClassName + ";" + fieldType + ")V";
        }

        if (false) {
            System.out.println("    "
                               + declClassName + "." + methodName
                               + " : " + methodSig);
        }

        // call the PC's static accessor/mutator
        final Insn frag = Insn.create(opc_invokestatic,
                                      pool.addMethodRef(declClassName,
                                                        methodName,
                                                        methodSig));

        //insn.prev().insert(Insn.create(opc_nop));
        // replace instruction
        final Insn prev = insn.prev();
        insn.remove();

        // replace current instruction with new fragment
        final Insn last = prev.insert(frag);
        return last;
    }

    private void annotateClone(ClassMethod method,
                               String superName)
    {
        //^olsen: extend for full support of inheritance on PC classes

        if (false) {
            final String methodName = method.name().asString();
            final String methodSig = method.signature().asString();
            System.out.println("annotateClone()");
            System.out.println("    methodName = " + methodName);
            System.out.println("    methodSig = " + methodSig);
            System.out.println("    superName = " + superName);
        }

        final CodeAttribute codeAttr = method.codeAttribute();
        for (Insn insn = codeAttr.theCode();
             insn != null;
             insn = insn.next()) {

            // Found the clone method.  See if it is the flavor of clone()
            // which does a super.clone() call, and if it is, add
            // field initializations for the jdoStateManager and jdoFlags
            // fields.
            if (insn.opcode() != opc_invokespecial)
                continue;

            final InsnConstOp invoke = (InsnConstOp)insn;
            final ConstMethodRef methodRef = (ConstMethodRef)invoke.value();
            final ConstNameAndType methodNT = methodRef.nameAndType();
            final String methodName = methodNT.name().asString();
            final String methodSig = methodNT.signature().asString();

            if (!(methodName.equals("clone")
                  && methodSig.equals("()Ljava/lang/Object;")))
                continue;

            if (false) {
                final ConstClass methodClass = methodRef.className();
                final String methodClassName = methodClass.asString();
                System.out.println("        found invocation of: "
                                   + methodClassName
                                   + "." + methodName + methodSig);
            }

            // check whether next instruction already is a downcast to a
            // class implementing PersistenceCapable
            final String thisClass = classFile.classNameString();
            final Insn checkCastInsn = insn.next();
            final boolean needCheckcast;
            if (checkCastInsn.opcode() != opc_checkcast) {
                needCheckcast = true;
            } else {
                ConstClass target =
                    (ConstClass) ((InsnConstOp)checkCastInsn).value();
                if (target.asString().equals(thisClass)) {
                    insn = checkCastInsn;
                    needCheckcast = false;
                } else {
                    needCheckcast = true;
                }
            }

            // clear jdo fields of clone
            {
                // duplicate downcasted reference
                final Insn newInsn = Insn.create(opc_dup);
                if (needCheckcast) {
                    newInsn.append(Insn.create(opc_checkcast,
                                               pool.addClass(thisClass)));
                }
                newInsn.append(Insn.create(opc_dup));

                // clear jdo fields
                newInsn.append(Insn.create(opc_aconst_null));
                newInsn.append(Insn.create(
                    opc_putfield,
                    pool.addFieldRef(
                        thisClass,
                        JDOConstants.JDO_PC_jdoStateManager_Name,
                        JDOConstants.JDO_PC_jdoStateManager_Sig)));
                newInsn.append(Insn.create(opc_iconst_0));
                newInsn.append(Insn.create(
                    opc_putfield,
                    pool.addFieldRef(
                        thisClass,
                        JDOConstants.JDO_PC_jdoFlags_Name,
                        JDOConstants.JDO_PC_jdoFlags_Sig)));

                // insert code
                insn.insert(newInsn);

                // increase stack
                final int annotationStack = 3;
                codeAttr.setStackUsed(codeAttr.stackUsed() + annotationStack);
            }
        }
    }
}
