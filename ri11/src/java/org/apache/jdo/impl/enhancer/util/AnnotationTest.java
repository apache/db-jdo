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

package org.apache.jdo.impl.enhancer.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.List;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.IOException;
import java.io.DataInputStream;

import org.apache.jdo.impl.enhancer.EnhancerFatalError;
import org.apache.jdo.impl.enhancer.JdoMetaMain;
import org.apache.jdo.impl.enhancer.classfile.ClassFile;
import org.apache.jdo.impl.enhancer.classfile.ClassMethod;
import org.apache.jdo.impl.enhancer.classfile.CodeAttribute;
import org.apache.jdo.impl.enhancer.classfile.ConstClass;
import org.apache.jdo.impl.enhancer.classfile.ConstFieldRef;
import org.apache.jdo.impl.enhancer.classfile.ConstMethodRef;
import org.apache.jdo.impl.enhancer.classfile.ConstNameAndType;
import org.apache.jdo.impl.enhancer.classfile.Descriptor;
import org.apache.jdo.impl.enhancer.classfile.Insn;
import org.apache.jdo.impl.enhancer.classfile.InsnConstOp;
import org.apache.jdo.impl.enhancer.classfile.VMConstants;
import org.apache.jdo.impl.enhancer.meta.EnhancerMetaData;
import org.apache.jdo.impl.enhancer.meta.EnhancerMetaDataFatalError;
import org.apache.jdo.impl.enhancer.meta.EnhancerMetaDataUserException;





/**
 * Utility class for testing a class file for correct annotation.
 *
 * @author Martin Zaun
 */
public class AnnotationTest
    extends JdoMetaMain
{
    // return values of internal test methods
    static public final int AFFIRMATIVE = 1;
    static public final int NEGATIVE = 0;
    static public final int ERROR = -1;

    // ----------------------------------------------------------------------

    private boolean verbose;
    private String className;
    private String classFileName;
    private ClassFile classFile;

    public AnnotationTest(PrintWriter out,
                          PrintWriter err)
    {
        super(out, err);
    }

    private int checkGetPutField(PrintWriter out,
                                 Insn insn,
                                 boolean jdoMethod) 
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        // get the instruction arguments
        final InsnConstOp fieldInsn = (InsnConstOp)insn;
        final ConstFieldRef fieldRef = (ConstFieldRef)fieldInsn.value();
        final ConstClass declClass = fieldRef.className();
        final String declClassName = declClass.asString();
        final ConstNameAndType fieldNameAndType = fieldRef.nameAndType();
        final String fieldName = fieldNameAndType.name().asString();
        final String fieldType = fieldNameAndType.signature().asString();

        // check if field is known to be non-managed or not annotatable
        final int res;
        if (jdoMeta.isKnownNonManagedField(declClassName,
                                           fieldName, fieldType)) {
            if (false) { // verbose
                out.println("        --- unannotated field access: "
                            + declClassName + "." + fieldName);
            }
            res = NEGATIVE;
        } else if (jdoMethod) {
            if (false) { // verbose
                out.println("        --- unannotated field access: "
                            + declClassName + "." + fieldName);
            } 
            res = NEGATIVE;
        } else if (jdoMeta.isPersistenceCapableClass(declClassName)
                   && (fieldName.equals("jdoStateManager")
                       || fieldName.equals("jdoFlags"))) {
            if (false) { // verbose
                out.println("        --- unannotated field access: "
                            + declClassName + "." + fieldName);
            } 
            res = NEGATIVE;
        } else {
            out.println("        !!! ERROR: missing annotation of field access: "
                        + declClassName + "." + fieldName);
            res = ERROR;
        }
        return res;
    }
    
    private int checkInvokeStatic(PrintWriter out,
                                  Insn insn,
                                  boolean jdoMethod) 
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        // get the instruction arguments
        final InsnConstOp methodInsn = (InsnConstOp)insn;
        final ConstMethodRef methodRef = (ConstMethodRef)methodInsn.value();
        final ConstClass declClass = methodRef.className();
        final String declClassName = declClass.asString();
        final ConstNameAndType methodNameAndType = methodRef.nameAndType();
        final String methodName = methodNameAndType.name().asString();
        final String methodType = methodNameAndType.signature().asString();

        if (!methodName.startsWith("jdoSet")
            && (!methodName.startsWith("jdoGet")
                || methodName.equals("jdoGetManagedFieldCount"))) {
            return NEGATIVE;
        }
        final String fieldName = methodName.substring(6);

        final int res;
        final String fieldType;
        if (methodName.startsWith("jdoGet")) {
            fieldType = Descriptor.extractResultSig(methodType);
        } else {
            final String argSig = Descriptor.extractArgSig(methodType);
            final int idx = Descriptor.nextSigElement(argSig, 0);
            fieldType = argSig.substring(idx);
        }
        affirm(fieldType != null);
        
        // check if field is known to be non-managed or non-annotable
        if (jdoMeta.isKnownNonManagedField(declClassName,
                                           fieldName, fieldType)) {
            out.println("        !!! ERROR: annotated access to non-managed field: "
                        + declClassName + "." + fieldName);
            res = ERROR;
        } else if (jdoMethod) {
            out.println("        !!! ERROR: annotated field access in JDO method: "
                        + declClassName + "." + fieldName);
            res = ERROR;
        } else {
            if (verbose) {
                out.println("        +++ annotated field access: "
                            + declClassName + "." + fieldName);
            }
            res = AFFIRMATIVE;
        }

        return res;
    }
    
    private int hasAnnotation(PrintWriter out,
                              ClassMethod method,
                              String methodName) 
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        final CodeAttribute codeAttr = method.codeAttribute();

        // return if method is abstract or native
        if (codeAttr == null)
            return NEGATIVE;

        int res = NEGATIVE;
        // don't annotate readObject(ObjectInputStream) or any jdo* methods
        // except for jdoPreStore() and jdoPreDelete().
        final boolean jdoMethod
            = ((methodName.startsWith("jdo") 
                && !(methodName.equals("jdoPreStore()")
                     || methodName.equals("jdoPreDelete()")))
               || methodName.equals("readObject(java.io.ObjectInputStream)"));

        // first instruction is a target
        final Insn firstInsn = codeAttr.theCode();
        Insn insn = firstInsn.next();
        while (insn != null) {
            switch(insn.opcode()) {
            case VMConstants.opc_getfield:
            case VMConstants.opc_putfield: {
                final int r = checkGetPutField(out, insn, jdoMethod);
                if (r < NEGATIVE) {
                    res = ERROR;
                }
                break;
            }
            case VMConstants.opc_invokestatic: {
                final int r = checkInvokeStatic(out, insn, jdoMethod);
                if (r < NEGATIVE) {
                    res = ERROR;
                } else if (r > NEGATIVE) {
                    if (res == NEGATIVE) {
                        res = AFFIRMATIVE;
                    }
                }
                break;
            }
            default:
            }

            insn = insn.next();
        }

        return res;
    }

    private int testAnnotation(PrintWriter out)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        affirm(ERROR < NEGATIVE && NEGATIVE < AFFIRMATIVE);
        affirm(classFile);

        int res = NEGATIVE;
        
        Enumeration e = classFile.methods().elements();
        while (e.hasMoreElements()) {
            final ClassMethod method = (ClassMethod)e.nextElement();
            final String methodSig = method.signature().asString();
            final String methodArgs = Descriptor.userMethodArgs(methodSig);
            final String methodName = method.name().asString() + methodArgs;
            
            // check class-specific enhancement
            final StringWriter s = new StringWriter();
            int r = hasAnnotation(new PrintWriter(s), method, methodName);
            if (r < NEGATIVE) {
                out.println("    !!! ERROR: incorrect annotation in: "
                            + methodName);
                out.println(s.toString());
                res = ERROR;
            } else if (r == NEGATIVE) {
                if (verbose) {
                    out.println("    --- not annotated: "
                                + methodName);
                    out.println(s.toString());
                }
            } else {
                affirm(r > NEGATIVE);
                if (verbose) {
                    out.println("    +++ has correct annotation: "
                                + methodName);
                    out.println(s.toString());
                }
                if (res == NEGATIVE) {
                    res = AFFIRMATIVE;
                }
            }
        }
        
        return res;
    }

    private int parseClass(PrintWriter out)
    {
        DataInputStream dis = null;
        try {
            affirm(className == null ^ classFileName == null);
            if (className != null) {
                dis = new DataInputStream(openClassInputStream(className));
            } else {
                dis = new DataInputStream(openFileInputStream(classFileName));
            }
            final boolean allowJDK12ClassFiles = true;
            classFile = new ClassFile(dis, allowJDK12ClassFiles);

            // check user class name from ClassFile
            final String userClassName
                = classFile.className().asString().replace('/', '.');
            //^olsen: better throw user exception or error
            affirm(className == null || className.equals(userClassName));
            out.println("    +++ parsed classfile");
        } catch (ClassFormatError ex) {
            out.println("    !!! ERROR: format error when parsing class: "
                        + className);
            out.println("        error: " + err);
            return ERROR;
        } catch (IOException ex) {
            out.println("    !!! ERROR: exception while reading class: "
                        + className);
            out.println("        exception: " + ex);
            return ERROR;
        } finally {
            closeInputStream(dis);
        }

        affirm(classFile);
        return AFFIRMATIVE;
    }

    private int test(PrintWriter out,
                     String className,
                     String classFileName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        this.className = className;
        this.classFileName = classFileName;
        affirm(className == null ^ classFileName == null);
        final String name = (className != null ? className : classFileName);

        if (verbose) {
            out.println("-------------------------------------------------------------------------------");
            out.println();
            out.println("Test class for correct annotation: "
                        + name + " ...");
        }
        
        // check parsing class
        StringWriter s = new StringWriter();
        if (parseClass(new PrintWriter(s)) <= NEGATIVE) {
            out.println();
            out.println("!!! ERROR: failed parsing class: " + name);
            out.println(s.toString());
            return ERROR;
        }

        if (verbose) {
            out.println();
            out.println("+++ parsed class: " + name);
            out.println(s.toString());
        }
        
        // check annotation
        s = new StringWriter();
        final int r = testAnnotation(new PrintWriter(s));
        if (r < NEGATIVE) {
            out.println();
            out.println("!!! ERROR: incorrect annotation: " + name);
            out.println(s.toString());
            return ERROR;
        }
        
        if (r == NEGATIVE) {
            out.println();
            out.println("--- class not annotated: " + name);
        } else {
            out.println();
            out.println("+++ class annotated: " + name);
        }
        if (verbose) {
            out.println(s.toString());
        }

        return r;
    }

    protected int test(PrintWriter out,
                       boolean verbose,
                       List classNames,
                       List classFileNames)
    {
        affirm(classNames);
        this.verbose = verbose;

        out.println();
        out.println("AnnotationTest: Testing Classes for JDO Persistence-Capability Enhancement");

        int nofFailed = 0;
        final int all = classNames.size() + classFileNames.size();
        for (int i = 0; i < classNames.size(); i++) {
            if (test(out, (String)classNames.get(i), null) < NEGATIVE) {
                nofFailed++;
            }
        }
        for (int i = 0; i < classFileNames.size(); i++) {
            if (test(out, null, (String)classFileNames.get(i)) < NEGATIVE) {
                nofFailed++;
            }
        }
        final int nofPassed = all - nofFailed;

        out.println();
        out.println("AnnotationTest: Summary:  TESTED: " + all
                    + "  PASSED: " + nofPassed
                    + "  FAILED: " + nofFailed);
        return nofFailed;
    }
    
    // ----------------------------------------------------------------------

    /**
     * Run the annotation test.
     */
    protected int process()
    {
        //^olsen: to be extended for zip/jar file arguments
        return test(out, options.verbose.value,
                    options.classNames, options.classFileNames);
    }

    static public void main(String[] args)
    {
        final PrintWriter out = new PrintWriter(System.out, true);
        out.println("--> AnnotationTest.main()");
        final AnnotationTest main = new AnnotationTest(out, out);
        int res = main.run(args);
        out.println("<-- AnnotationTest.main(): exit = " + res);
        System.exit(res);
    }
}
