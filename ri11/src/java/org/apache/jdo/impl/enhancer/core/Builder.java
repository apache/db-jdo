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

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;

import org.apache.jdo.impl.enhancer.classfile.AttributeVector;
import org.apache.jdo.impl.enhancer.classfile.ClassField;
import org.apache.jdo.impl.enhancer.classfile.ClassFile;
import org.apache.jdo.impl.enhancer.classfile.CodeAttribute;
import org.apache.jdo.impl.enhancer.classfile.ConstClass;
import org.apache.jdo.impl.enhancer.classfile.ConstFieldRef;
import org.apache.jdo.impl.enhancer.classfile.ConstNameAndType;
import org.apache.jdo.impl.enhancer.classfile.ConstUtf8;
import org.apache.jdo.impl.enhancer.classfile.ConstantPool;
import org.apache.jdo.impl.enhancer.classfile.Descriptor;
import org.apache.jdo.impl.enhancer.classfile.ExceptionRange;
import org.apache.jdo.impl.enhancer.classfile.ExceptionTable;
import org.apache.jdo.impl.enhancer.classfile.ExceptionsAttribute;
import org.apache.jdo.impl.enhancer.classfile.Insn;
import org.apache.jdo.impl.enhancer.classfile.InsnIInc;
import org.apache.jdo.impl.enhancer.classfile.InsnInterfaceInvoke;
import org.apache.jdo.impl.enhancer.classfile.InsnLookupSwitch;
import org.apache.jdo.impl.enhancer.classfile.InsnTableSwitch;
import org.apache.jdo.impl.enhancer.classfile.InsnTarget;
import org.apache.jdo.impl.enhancer.classfile.InsnUtils;
import org.apache.jdo.impl.enhancer.classfile.VMConstants;
import org.apache.jdo.impl.enhancer.util.InternalError;
import org.apache.jdo.impl.enhancer.util.Support;

/**
 * Helper object to create the generic JDO methods for a class.
 */
class Builder
    extends Support
    implements VMConstants, JDOConstants, EnhancerConstants
{
    /**
     * The augmentation controller for this class.
     */
    private final Augmenter augmenter;

    /**
     * The class analyzer for this class.
     */
    private final Analyzer analyzer;

    /**
     * The classfile to be annotated.
     */
    private final ClassFile classFile;

    /**
     * The class name in VM form.
     */
    private final String className;

    /**
     * The class name in user ('.' delimited) form.
     */
    private final String userClassName;

    /**
     * The classfile's constant pool.
     */
    private final ConstantPool pool;

    /**
     * Repository for the enhancement options.
     */
    private final Environment env;

    /**
     * The constant utf8 string for the CodeAttribute.
     */
    private ConstUtf8 codeAttributeUtf8;
    /**

     * The constant field ref for the jdoStateManager field.
     */
    private ConstFieldRef jdoStateManagerFieldRef;

    /**
     * The constant field ref for the jdoFlags field.
     */
    private ConstFieldRef jdoFlagsFieldRef;

    /**
     * The constant field ref for the jdoFieldNames field.
     */
    private ConstFieldRef jdoFieldNamesFieldRef;

    /**
     * The constant field ref for the jdoFieldTypes field.
     */
    private ConstFieldRef jdoFieldTypesFieldRef;

    /**
     * The constant field ref for the jdoFieldFlags field.
     */
    private ConstFieldRef jdoFieldFlagsFieldRef;

    /**
     * The constant field ref for the jdoPersistenceCapableSuperclass field.
     */
    private ConstFieldRef jdoPersistenceCapableSuperclassFieldRef;

    /**
     * The constant field refs for the annotated fields sorted by their
     * relative field index.
     */
    private ConstFieldRef[] annotatedFieldRefs;

    /**
     * The constant field refs for the key fields sorted by
     * ascending relative field index.
     */
    private ConstFieldRef[] keyFieldRefs;

    /**
     * The constant field refs on the key class for the key fields sorted by
     * ascending relative field index.
     */
    private ConstFieldRef[] keyClassKeyFieldRefs;

    /**
     * Constructor.
     */
    public Builder(Analyzer analyzer,
                   Augmenter augmenter,
                   Environment env)
    {
        affirm(analyzer != null);
        affirm(augmenter != null);
        affirm(env != null);

        this.analyzer = analyzer;
        this.augmenter = augmenter;
        this.classFile = analyzer.getClassFile();
        this.className = classFile.classNameString();
        this.userClassName = classFile.userClassName();
        this.pool = classFile.pool();
        this.env = env;

        affirm(classFile != null);
        affirm(className != null);
        affirm(userClassName != null);
        affirm(pool != null);
    }

    // ----------------------------------------------------------------------
    // Internal Helper Methods
    // ----------------------------------------------------------------------

    /**
     * Holder object for returning a size info from a code generation method.
     */
    static private class SizeHolder
    {
        int size;
    }

    /**
     * Returns the minimum of two numbers.
     */
    static private int min(int i, int j) {
        return (i < j ? i : j);
    }

    /**
     * Returns the maximum of two numbers.
     */
    static private int max(int i, int j) {
        return (i < j ? j : i);
    }

    /**
     * Count the size of the arguments to an invokevirtual method call.
     */
    static private int countMethodArgWords(String sig) 
    {
        // the 'this' pointer is to be accounted too
        return Descriptor.countMethodArgWords(sig) + 1;
    }    

    /**
     * Returns the utf8 string for the CodeAttribute.
     */
    private ConstUtf8 getCodeAttributeUtf8()
    {
        // create utf8 in constant pool if not done yet
        if (codeAttributeUtf8 == null) {
            codeAttributeUtf8 = pool.addUtf8(CodeAttribute.expectedAttrName);
        }
        return codeAttributeUtf8;
    }

    /**
     * Returns the constant field ref for the jdoStateManager field.
     */
    private ConstFieldRef getjdoStateManagerFieldRef()
    {
        //^olsen: javac uses the truelly declaring class
        final String pcRootName = analyzer.getPCRootClassName();
        affirm(pcRootName != null);

        // create field reference in constant pool if not done yet
        if (jdoStateManagerFieldRef == null) {
            jdoStateManagerFieldRef
                = pool.addFieldRef(pcRootName, //className,
                                   JDO_PC_jdoStateManager_Name,
                                   JDO_PC_jdoStateManager_Sig);
        }
        return jdoStateManagerFieldRef;
    }

    /**
     * Returns the constant field ref for the jdoFlags field.
     */
    private ConstFieldRef getjdoFlagsFieldRef()
    {
        //^olsen: javac uses the truelly declaring class
        final String pcRootName = analyzer.getPCRootClassName();
        affirm(pcRootName != null);

        // create field reference in constant pool if not done yet
        if (jdoFlagsFieldRef == null) {
            jdoFlagsFieldRef
                = pool.addFieldRef(pcRootName, //className,
                                   JDO_PC_jdoFlags_Name,
                                   JDO_PC_jdoFlags_Sig);
        }
        return jdoFlagsFieldRef;
    }

    /**
     * Returns the constant field ref for the jdoFieldNames field.
     */
    private ConstFieldRef getjdoFieldNamesFieldRef()
    {
        // create field reference in constant pool if not done yet
        if (jdoFieldNamesFieldRef == null) {
            jdoFieldNamesFieldRef
                = pool.addFieldRef(className,
                                   JDO_PC_jdoFieldNames_Name,
                                   JDO_PC_jdoFieldNames_Sig);
        }
        return jdoFieldNamesFieldRef;
    }

    /**
     * Returns the constant field ref for the jdoFieldTypes field.
     */
    private ConstFieldRef getjdoFieldTypesFieldRef()
    {
        // create field reference in constant pool if not done yet
        if (jdoFieldTypesFieldRef == null) {
            jdoFieldTypesFieldRef
                = pool.addFieldRef(className,
                                   JDO_PC_jdoFieldTypes_Name,
                                   JDO_PC_jdoFieldTypes_Sig);
        }
        return jdoFieldTypesFieldRef;
    }

    /**
     * Returns the constant field ref for the jdoFieldFlags field.
     */
    private ConstFieldRef getjdoFieldFlagsFieldRef()
    {
        // create field reference in constant pool if not done yet
        if (jdoFieldFlagsFieldRef == null) {
            jdoFieldFlagsFieldRef
                = pool.addFieldRef(className,
                                   JDO_PC_jdoFieldFlags_Name,
                                   JDO_PC_jdoFieldFlags_Sig);
        }
        return jdoFieldFlagsFieldRef;
    }

    /**
     * Returns the constant field ref for the jdoPersistenceCapableSuperclass field.
     */
    private ConstFieldRef getjdoPersistenceCapableSuperclassFieldRef()
    {
        // create field reference in constant pool if not done yet
        if (jdoPersistenceCapableSuperclassFieldRef == null) {
            jdoPersistenceCapableSuperclassFieldRef
                = pool.addFieldRef(className,
                                   JDO_PC_jdoPersistenceCapableSuperclass_Name,
                                   JDO_PC_jdoPersistenceCapableSuperclass_Sig);
        }
        return jdoPersistenceCapableSuperclassFieldRef;
    }

    /**
     * Returns the constant field refs for the annotated fields.
     */
    private ConstFieldRef[] getAnnotatedFieldRefs()
    {
        // create field references in constant pool if not done yet
        if (annotatedFieldRefs == null) {
            final int annotatedFieldCount = analyzer.getAnnotatedFieldCount();
            final String[] annotatedFieldNames
                = analyzer.getAnnotatedFieldNames();
            final String[] annotatedFieldSigs
                = analyzer.getAnnotatedFieldSigs();
            affirm(annotatedFieldNames.length == annotatedFieldCount);
            affirm(annotatedFieldSigs.length == annotatedFieldCount);
            
            // add field references to constant pool
            annotatedFieldRefs = new ConstFieldRef[annotatedFieldCount];
            for (int i = 0; i < annotatedFieldCount; i++) {
                final String name = annotatedFieldNames[i];
                final String sig = annotatedFieldSigs[i];
                annotatedFieldRefs[i] = pool.addFieldRef(className, name, sig);
                affirm(annotatedFieldRefs[i] != null);
            }
        }
        affirm(annotatedFieldRefs != null);
        return annotatedFieldRefs;
    }

    /**
     * Returns the constant field refs for the key fields.
     */
    private ConstFieldRef[] getKeyFieldRefs()
    {
        // get field references if not done yet
        if (keyFieldRefs == null) {
            final ConstFieldRef[] annotatedFieldRefs = getAnnotatedFieldRefs();
            final int keyFieldCount = analyzer.getKeyFieldCount();
            final int[] keyFieldIndexes = analyzer.getKeyFieldIndexes();
            affirm(keyFieldIndexes.length == keyFieldCount);
            
            // add field references
            keyFieldRefs = new ConstFieldRef[keyFieldCount];
            for (int i = 0; i < keyFieldCount; i++) {
                keyFieldRefs[i] = annotatedFieldRefs[keyFieldIndexes[i]];
                affirm(keyFieldRefs[i] != null);
            }
        }
        affirm(keyFieldRefs != null);
        return keyFieldRefs;
    }

    /**
     * Returns the constant field refs for the key fields of the key class.
     */
    private ConstFieldRef[] getKeyClassKeyFieldRefs()
    {
        // get field references if not done yet
        if (keyClassKeyFieldRefs == null) {
            final String keyClassName = analyzer.getKeyClassName();
            affirm(keyClassName != null);
            final int keyFieldCount = analyzer.getKeyFieldCount();
            final ConstFieldRef[] keyFieldRefs = getKeyFieldRefs();
            affirm(keyFieldRefs.length == keyFieldCount);
            
            // add field references
            keyClassKeyFieldRefs = new ConstFieldRef[keyFieldCount];
            for (int i = 0; i < keyFieldCount; i++) {
                final ConstNameAndType nt = keyFieldRefs[i].nameAndType();
                final String name = nt.name().asString();
                final String sig = nt.signature().asString();
                keyClassKeyFieldRefs[i]
                    = pool.addFieldRef(keyClassName, name, sig);
                affirm(keyClassKeyFieldRefs[i] != null);
            }
        }
        affirm(keyClassKeyFieldRefs != null);
        return keyClassKeyFieldRefs;
    }

    /**
     * Adds the code for throwing a IllegalArgumentException.
     */
    private Insn appendThrowJavaException(Insn insn,
                                          String exceptionName,
                                          String exceptionText)
    {
        affirm(insn != null);
        affirm(exceptionName != null);
        affirm(exceptionText != null);

        // throw exception
        final String exceptionCtorName
            = NameHelper.constructorName();
        final String exceptionCtorSig
            = NameHelper.constructorSig(JAVA_String_Sig);
        insn = insn.append(
            Insn.create(opc_new,
                        pool.addClass(exceptionName)));
        insn = insn.append(Insn.create(opc_dup));
        insn = insn.append(
            InsnUtils.stringConstant(
                exceptionText, pool));
        insn = insn.append(
            Insn.create(opc_invokespecial,
                        pool.addMethodRef(
                            exceptionName,
                            exceptionCtorName,
                            exceptionCtorSig)));
        insn = insn.append(Insn.create(opc_athrow));

        affirm(insn != null);
        return insn;
    }

    /**
     * Adds the code for handling if jdoStateManager field is null.
     */
    private Insn appendCheckStateManager(Insn insn,
                                         int argStart,
                                         String exceptionName,
                                         String exceptionText)
    {
        affirm(insn != null);
        affirm(exceptionName != null);
        affirm(exceptionText != null);
        
        // throw exception if sm == null
        final InsnTarget body = new InsnTarget();
        insn = insn.append(InsnUtils.aLoad(argStart, pool));
        insn = insn.append(
            Insn.create(
                opc_getfield,
                getjdoStateManagerFieldRef()));
        insn = insn.append(Insn.create(opc_ifnonnull, body));
        insn = appendThrowJavaException(insn, exceptionName, exceptionText);
        insn = insn.append(body);

        affirm(insn != null);
        return insn;
    }

    /**
     * Adds the code for handling if an argument is null.
     */
    private Insn appendCheckVarNonNull(Insn insn,
                                       int argStart,
                                       String exceptionName,
                                       String exceptionText)
    {
        affirm(insn != null);
        affirm(exceptionName != null);
        affirm(exceptionText != null);

        // throw exception if obj == null
        final InsnTarget body = new InsnTarget();
        insn = insn.append(InsnUtils.aLoad(argStart, pool));
        insn = insn.append(Insn.create(opc_ifnonnull, body));
        insn = appendThrowJavaException(insn, exceptionName, exceptionText);
        insn = insn.append(body);

        affirm(insn != null);
        return insn;
    }

    /**
     * Adds the code for handling if an argument is instance of a class.
     */
    private Insn appendCheckVarInstanceOf(Insn insn,
                                          int argStart,
                                          ConstClass constClass,
                                          String exceptionName,
                                          String exceptionText)
    {
        affirm(insn != null);
        affirm(constClass != null);
        affirm(exceptionName != null);
        affirm(exceptionText != null);

        // throw exception if obj not instance of class
        final InsnTarget body = new InsnTarget();
        insn = insn.append(InsnUtils.aLoad(argStart, pool));
        insn = insn.append(Insn.create(opc_instanceof, constClass));
        insn = insn.append(Insn.create(opc_ifne, body));
        insn = appendThrowJavaException(insn, exceptionName, exceptionText);
        insn = insn.append(body);

        affirm(insn != null);
        return insn;
    }

    // ----------------------------------------------------------------------

    /**
     * Builds an empty method (for debugging).
     *
     * public void XXX() {
     * }
     */
    public void addNullMethod(final String methodName,
                              final String methodSig,
                              final int accessFlags)
    {
        // assumed nonstatic call; otherwise subtract 'this' from maxStack
        affirm((accessFlags & ACCStatic) == 0);
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // end of method body
        insn = insn.append(Insn.create(opc_return));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                0, // maxStack
                                countMethodArgWords(methodSig), // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------
    // Generic Augmentation
    // ----------------------------------------------------------------------

    /**
     * Build the jdoSetStateManager method for the class.
     *
     * public final synchronized void jdoReplaceStateManager(javax.jdo.StateManager sm)
     * {
     *     final javax.jdo.StateManager s = this.jdoStateManager;
     *     if (s != null) {
     *         this.jdoStateManager = s.replacingStateManager(this, sm);
     *         return;
     *     }
     *     // throws exception if not authorized
     *     JDOImplHelper.checkAuthorizedStateManager(sm);
     *     this.jdoStateManager = sm;
     *     this.jdoFlags = LOAD_REQUIRED;
     * }
     */
    public void addJDOReplaceStateManager()
    {
        final String methodName = JDO_PC_jdoReplaceStateManager_Name;
        final String methodSig = JDO_PC_jdoReplaceStateManager_Sig;
        final int accessFlags = JDO_PC_jdoReplaceStateManager_Mods;
        final ExceptionsAttribute exceptAttr = null;

        //^olsen: exceptAttr != null ???

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // store the sm field into local var
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(
                opc_getfield,
                getjdoStateManagerFieldRef()));
        insn = insn.append(Insn.create(opc_astore_2));

        // test the sm field and call the sm if nonnull
        final InsnTarget check = new InsnTarget();
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_ifnull, check));

        // load 'this' on the stack
        insn = insn.append(Insn.create(opc_aload_0));

        // call the sm's method with 'this' and 'sm' arguments
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(
            new InsnInterfaceInvoke(
                pool.addInterfaceMethodRef(
                    JDO_StateManager_Path,
                    JDO_SM_replacingStateManager_Name,
                    JDO_SM_replacingStateManager_Sig),
                countMethodArgWords(JDO_SM_replacingStateManager_Sig)));

        // put result value to sm field and return
        insn = insn.append(
            Insn.create(opc_putfield,
                        getjdoStateManagerFieldRef()));
        insn = insn.append(Insn.create(opc_return));

        // invoke JDOImplHelper.checkAuthorizedStateManager with 'sm' argument
        insn = insn.append(check);
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(
            Insn.create(opc_invokestatic,
                        pool.addMethodRef(
                            JDO_JDOImplHelper_Path,
                            JDO_JDOImplHelper_checkAuthorizedStateManager_Name,
                            JDO_JDOImplHelper_checkAuthorizedStateManager_Sig)));

        // put argument value to jdoStateManager field
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(
            Insn.create(opc_putfield,
                        getjdoStateManagerFieldRef()));

        // reset flags to LOAD_REQUIRED
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_iconst_1));
        insn = insn.append(
            Insn.create(opc_putfield,
                        getjdoFlagsFieldRef()));

        // end of method body
        insn = insn.append(Insn.create(opc_return));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                4, // maxStack
                                3, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build the jdoReplaceFlags method for the class.
     *
     * public final void jdoReplaceFlags()
     * {
     *     final StateManager sm = this.jdoStateManager;
     *     if (sm != null) {
     *         this.jdoFlags = sm.replacingFlags(this);
     *     }
     * }
     */
    public void addJDOReplaceFlags()
    {
        final String methodName = JDO_PC_jdoReplaceFlags_Name;
        final String methodSig = JDO_PC_jdoReplaceFlags_Sig;
        final int accessFlags = JDO_PC_jdoReplaceFlags_Mods;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // store the sm field into local var
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(
                opc_getfield,
                getjdoStateManagerFieldRef()));
        insn = insn.append(Insn.create(opc_astore_1));

        // test the sm field and goto end if null
        final InsnTarget end = new InsnTarget();
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_ifnull, end));

        // load 'this' on the stack
        insn = insn.append(Insn.create(opc_aload_0));

        // call the sm's method with 'this' argument
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            new InsnInterfaceInvoke(
                pool.addInterfaceMethodRef(
                    JDO_StateManager_Path,
                    JDO_SM_replacingFlags_Name,
                    JDO_SM_replacingFlags_Sig),
                countMethodArgWords(JDO_SM_replacingFlags_Sig)));

        // put result value to flags field
        insn = insn.append(
            Insn.create(opc_putfield,
                        getjdoFlagsFieldRef()));

        // end of method body
        insn = insn.append(end);
        insn = insn.append(Insn.create(opc_return));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                3, // maxStack
                                2, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build the jdoMakeDirty method for the class.
     *
     * public final void jdoMakeDirty(java.lang.String fieldname)
     * {
     *     final javax.jdo.StateManager sm = this.jdoStateManager;
     *     if (sm != null) {
     *         sm.makeDirty(this, fieldname);
     *     }
     * }
     */
    public void addJDOMakeDirtyMethod()
    {
        final String methodName = JDO_PC_jdoMakeDirty_Name;
        final String methodSig = JDO_PC_jdoMakeDirty_Sig;
        final int accessFlags = JDO_PC_jdoMakeDirty_Mods;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // store the sm field into local var
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(
                opc_getfield,
                getjdoStateManagerFieldRef()));
        insn = insn.append(Insn.create(opc_astore_2));

        // test the sm field and goto end if null
        final InsnTarget end = new InsnTarget();
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_ifnull, end));

        // call the sm's method with 'this' and 'fieldname' arguments
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(
            new InsnInterfaceInvoke(
                pool.addInterfaceMethodRef(
                    JDO_StateManager_Path,
                    JDO_SM_makeDirty_Name,
                    JDO_SM_makeDirty_Sig),
                countMethodArgWords(JDO_SM_makeDirty_Sig)));

        // end of method body
        insn = insn.append(end);
        insn = insn.append(Insn.create(opc_return));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                3, // maxStack
                                3, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build the jdoPreSerialize method for the class.
     *
     * protected final void jdoPreSerialize()
     * {
     *     final javax.jdo.StateManager sm = this.jdoStateManager;
     *     if (sm != null) {
     *         sm.preSerialize(this);
     *     }
     * }
     */
    public void addJDOPreSerializeMethod()
    {
        final String methodName = JDO_PC_jdoPreSerialize_Name;
        final String methodSig = JDO_PC_jdoPreSerialize_Sig;
        final int accessFlags = JDO_PC_jdoPreSerialize_Mods;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // store the sm field into local var
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(
                opc_getfield,
                getjdoStateManagerFieldRef()));
        insn = insn.append(Insn.create(opc_astore_1));

        // test the sm field and goto end if null
        final InsnTarget end = new InsnTarget();
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_ifnull, end));

        // call the sm's method with 'this' argument
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            new InsnInterfaceInvoke(
                pool.addInterfaceMethodRef(
                    JDO_StateManager_Path,
                    JDO_SM_preSerialize_Name,
                    JDO_SM_preSerialize_Sig),
                countMethodArgWords(JDO_SM_preSerialize_Sig)));

        // end of method body
        insn = insn.append(end);
        insn = insn.append(Insn.create(opc_return));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                2, // maxStack
                                2, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build the writeObject method for the class.
     *
     * private void writeObject(java.io.ObjectOutputStream out) 
     *    throws java.io.IOException
     * {
     *    jdoPreSerialize();
     *    out.defaultWriteObject();
     * }
     */
    public void addWriteObjectMethod()
    {
        final String methodName = JAVA_Object_writeObject_Name;
        final String methodSig = JAVA_Object_writeObject_Sig;
        final int accessFlags = JAVA_Object_writeObject_Mods;
        final ExceptionsAttribute exceptAttr
            = new ExceptionsAttribute(
                pool.addUtf8(ExceptionsAttribute.expectedAttrName),
                pool.addClass("java/io/IOException"));
        
        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // call jdoPreSerialize 
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(opc_invokevirtual,
                        pool.addMethodRef(
                            className,
                            JDO_PC_jdoPreSerialize_Name,
                            JDO_PC_jdoPreSerialize_Sig)));

        // call out.defaultWriteObject();
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(
            Insn.create(opc_invokevirtual,
                        pool.addMethodRef(
                            JAVA_ObjectOutputStream_Path,
                            JAVA_ObjectOutputStream_defaultWriteObject_Name,
                            JDO_PC_jdoPreSerialize_Sig)));
        
        // end of method body
        insn = insn.append(Insn.create(opc_return));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                1, // maxStack
                                2, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
        
    }

    // ----------------------------------------------------------------------

    /**
     * Adds a call to jdoPreSerialize as first statement to the existing method.
     */
    public void addJDOPreSerializeCall(String methodName, String methodSig)
    {
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;
        
        // invoke jdoPreSerialize
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(opc_invokevirtual,
                        pool.addMethodRef(
                            className,
                            JDO_PC_jdoPreSerialize_Name,
                            JDO_PC_jdoPreSerialize_Sig)));

        // create code block to be added
        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                1, // maxStack
                                0, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());

        augmenter.prependMethod(methodName, methodSig, codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build an interrogative method for the class.
     */
    public void addJDOIsPersistentMethod()
    {
        addJDOInterrogativeMethod(JDO_PC_jdoIsPersistent_Name,
                                  JDO_PC_jdoIsPersistent_Sig,
                                  JDO_PC_jdoIsPersistent_Mods,
                                  JDO_SM_isPersistent_Name,
                                  JDO_SM_isPersistent_Sig);
    }
    
    /**
     * Build an interrogative method for the class.
     */
    public void addJDOIsTransactionalMethod()
    {
        addJDOInterrogativeMethod(JDO_PC_jdoIsTransactional_Name,
                                  JDO_PC_jdoIsTransactional_Sig,
                                  JDO_PC_jdoIsTransactional_Mods,
                                  JDO_SM_isTransactional_Name,
                                  JDO_SM_isTransactional_Sig);
    }
    
    /**
     * Build an interrogative method for the class.
     */
    public void addJDOIsNewMethod()
    {
        addJDOInterrogativeMethod(JDO_PC_jdoIsNew_Name,
                                  JDO_PC_jdoIsNew_Sig,
                                  JDO_PC_jdoIsNew_Mods,
                                  JDO_SM_isNew_Name,
                                  JDO_SM_isNew_Sig);
    }
    
    /**
     * Build an interrogative method for the class.
     */
    public void addJDOIsDeletedMethod()
    {
        addJDOInterrogativeMethod(JDO_PC_jdoIsDeleted_Name,
                                  JDO_PC_jdoIsDeleted_Sig,
                                  JDO_PC_jdoIsDeleted_Mods,
                                  JDO_SM_isDeleted_Name,
                                  JDO_SM_isDeleted_Sig);
    }
    
    /**
     * Build an interrogative method for the class.
     */
    public void addJDOIsDirtyMethod()
    {
        addJDOInterrogativeMethod(JDO_PC_jdoIsDirty_Name,
                                  JDO_PC_jdoIsDirty_Sig,
                                  JDO_PC_jdoIsDirty_Mods,
                                  JDO_SM_isDirty_Name,
                                  JDO_SM_isDirty_Sig);
    }
    
    /**
     * Build an interrogative method named methodName for the class.
     *
     * public boolean isXXX() {
     *     final StateManager sm = this.jdoStateManager;
     *     if (sm == null)
     *         return false;
     *     return sm.isXXXX(this);
     * }
     */
    private void addJDOInterrogativeMethod(final String methodName,
                                           final String methodSig,
                                           final int accessFlags,
                                           final String delegateName,
                                           final String delegateSig)
    {
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // store the sm field into local var
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(
                opc_getfield,
                getjdoStateManagerFieldRef()));
        insn = insn.append(Insn.create(opc_astore_1));

        // test the sm field and do the call if nonnull
        InsnTarget noncall = new InsnTarget();
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_ifnull, noncall));

        // call the sm's method with 'this' argument and return
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            new InsnInterfaceInvoke(
                pool.addInterfaceMethodRef(
                    JDO_StateManager_Path,
                    delegateName,
                    delegateSig),
                countMethodArgWords(delegateSig)));
        insn = insn.append(Insn.create(opc_ireturn));

        // return false
        insn = insn.append(noncall);
        insn = insn.append(Insn.create(opc_iconst_0));

        // end of method body
        insn = insn.append(Insn.create(opc_ireturn));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                2, // maxStack
                                2, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build an object query method for the class.
     */
    public void addJDOGetPersistenceManagerMethod()
    {
        addJDOObjectQueryMethod(JDO_PC_jdoGetPersistenceManager_Name,
                                JDO_PC_jdoGetPersistenceManager_Sig,
                                JDO_PC_jdoGetPersistenceManager_Mods,
                                JDO_SM_getPersistenceManager_Name,
                                JDO_SM_getPersistenceManager_Sig);
    }

    /**
     * Build an object query method for the class.
     */
    public void addJDOGetObjectIdMethod()
    {
        addJDOObjectQueryMethod(JDO_PC_jdoGetObjectId_Name,
                                JDO_PC_jdoGetObjectId_Sig,
                                JDO_PC_jdoGetObjectId_Mods,
                                JDO_SM_getObjectId_Name,
                                JDO_SM_getObjectId_Sig);
    }

    /**
     * Build an object query method for the class.
     */
    public void addJDOGetTransactionalObjectIdMethod()
    {
        addJDOObjectQueryMethod(JDO_PC_jdoGetTransactionalObjectId_Name,
                                JDO_PC_jdoGetTransactionalObjectId_Sig,
                                JDO_PC_jdoGetTransactionalObjectId_Mods,
                                JDO_SM_getTransactionalObjectId_Name,
                                JDO_SM_getTransactionalObjectId_Sig);
    }

    /**
     * Build an object query method for the class.
     *
     * public final XXX jdoGetYYY()
     * {
     *     final javax.jdo.StateManager sm = this.jdoStateManager;
     *     if (sm != null) {
     *         return sm.getYYY(this);
     *     }
     *     return null;
     * }
     */
    private void addJDOObjectQueryMethod(final String methodName,
                                         final String methodSig,
                                         final int accessFlags,
                                         final String delegateName,
                                         final String delegateSig)
    {
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // store the sm field into local var
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(
                opc_getfield,
                getjdoStateManagerFieldRef()));
        insn = insn.append(Insn.create(opc_astore_1));

        // test the sm field and do the call if nonnull
        InsnTarget noncall = new InsnTarget();
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_ifnull, noncall));

        // call the sm's method with 'this' argument and return
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            new InsnInterfaceInvoke(
                pool.addInterfaceMethodRef(
                    JDO_StateManager_Path,
                    delegateName,
                    delegateSig),
                countMethodArgWords(delegateSig)));
        insn = insn.append(Insn.create(opc_areturn));

        // return null
        insn = insn.append(noncall);
        insn = insn.append(Insn.create(opc_aconst_null));

        // end of method body
        insn = insn.append(Insn.create(opc_areturn));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                2, // maxStack
                                2, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build the jdoArrayArgumentIteration method for the class.
     */
    public void addJDOProvideFieldsMethod()
    {
        addJDOArrayArgumentIterationMethod(JDO_PC_jdoProvideFields_Name,
                                           JDO_PC_jdoProvideFields_Sig,
                                           JDO_PC_jdoProvideFields_Mods,
                                           JDO_PC_jdoProvideField_Name,
                                           JDO_PC_jdoProvideField_Sig);
    }

    /**
     * Build the jdoArrayArgumentIteration method for the class.
     */
    public void addJDOReplaceFieldsMethod()
    {
        addJDOArrayArgumentIterationMethod(JDO_PC_jdoReplaceFields_Name,
                                           JDO_PC_jdoReplaceFields_Sig,
                                           JDO_PC_jdoReplaceFields_Mods,
                                           JDO_PC_jdoReplaceField_Name,
                                           JDO_PC_jdoReplaceField_Sig);
    }

    /**
     * Build the jdoArrayArgumentIteration method for the class.
     *
     * public final void jdoXXXFields(int[] fieldnumbers)
     * {
     *     final int n = fieldnumbers.length;
     *     for (int i = 0; i < n; i++) {
     *         this.jdoXXXField(fieldnumbers[i]);
     *     }
     * }
     */
    public void addJDOArrayArgumentIterationMethod(final String methodName,
                                                   final String methodSig,
                                                   final int accessFlags,
                                                   final String delegateName,
                                                   final String delegateSig)
    {
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // check arg
        insn = appendCheckVarNonNull(insn, 1,
                                     JAVA_IllegalArgumentException_Path,
                                     "arg1");

        // store the array argument length into local var
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_arraylength));
        insn = insn.append(Insn.create(opc_istore_2));

        // init loop counter and goto loop check
        final InsnTarget loopcheck = new InsnTarget();
        insn = insn.append(Insn.create(opc_iconst_0));
        insn = insn.append(Insn.create(opc_istore_3));
        insn = insn.append(Insn.create(opc_goto, loopcheck));

        // loop body: call self-delegating method with array element
        final InsnTarget loopbody = new InsnTarget();
        insn = insn.append(loopbody);
        insn = insn.append(Insn.create(opc_aload_0));

        // select element from array argument at loop counter
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_iload_3));
        insn = insn.append(Insn.create(opc_iaload));

        // call self-delegating method
        insn = insn.append(
            Insn.create(opc_invokevirtual,
                        pool.addMethodRef(
                            className,
                            delegateName,
                            delegateSig)));

        // loop counter increment
        insn = insn.append(new InsnIInc(3, 1));

        // loop termination check
        insn = insn.append(loopcheck);
        insn = insn.append(Insn.create(opc_iload_3));
        insn = insn.append(Insn.create(opc_iload_2));
        insn = insn.append(Insn.create(opc_if_icmplt, loopbody));

        // end of method body
        insn = insn.append(Insn.create(opc_return));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                3, // maxStack
                                4, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build the sunjdoClassForName method for the class.
     *
     * public final Class sunjdoClassForName(java.lang.String classname)
     * {
     *     try {
     *         return Class.forName(classname);
     *     catch (ClassNotFoundException ex) {
     *         throw new NoClassDefFoundError(ex.getMessage());
     *     }
     * }
     */
    public void addSunJDOClassForNameMethod()
    {
        final String methodName = SUNJDO_PC_sunjdoClassForName_Name;
        final String methodSig = SUNJDO_PC_sunjdoClassForName_Sig;
        final int accessFlags = SUNJDO_PC_sunjdoClassForName_Mods;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // invoke the Class.forName(String) method with argument
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(opc_invokestatic,
                        pool.addMethodRef(
                            JAVA_Class_Path,
                            JAVA_Class_forName_Name,
                            JAVA_Class_forName_Sig)));

        // end of method body
        insn = insn.append(Insn.create(opc_areturn));

        // begin of exception handler
        final InsnTarget end = new InsnTarget();
        final InsnTarget beginHandler = end;
        insn = insn.append(beginHandler);

        // create NoClassDefFoundError with message from caught exception
        insn = insn.append(Insn.create(opc_astore_1));
        insn = insn.append(
            Insn.create(opc_new,
                        pool.addClass(JAVA_NoClassDefFoundError_Path)));
        insn = insn.append(Insn.create(opc_dup));
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(
            Insn.create(
                opc_invokevirtual,
                pool.addMethodRef(
                    JAVA_Throwable_Path,
                    JAVA_Throwable_getMessage_Name,
                    JAVA_Throwable_getMessage_Sig)));
        insn = insn.append(
            Insn.create(
                opc_invokespecial,
                pool.addMethodRef(
                    JAVA_NoClassDefFoundError_Path,
                    JAVA_NoClassDefFoundError_NoClassDefFoundError_Name,
                    JAVA_NoClassDefFoundError_NoClassDefFoundError_Sig)));

        // end of exception handler
        insn = insn.append(Insn.create(opc_athrow));

        // create exception table
        final ConstClass catchType
            = pool.addClass(JAVA_ClassNotFoundException_Path);
        final ExceptionRange exceptionRange
            = new ExceptionRange(begin, end, beginHandler, catchType);
        final ExceptionTable exceptionTable
            = new ExceptionTable();
        exceptionTable.addElement(exceptionRange);

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                3, // maxStack
                                3, // maxLocals
                                begin,
                                exceptionTable,
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------
    // Specific Augmentation
    // ----------------------------------------------------------------------
    
    /**
     * Build the jdoGetManagedFieldCount method for the class.
     *
     * protected static int jdoGetManagedFieldCount()
     * {
     *     return jdoInheritedFieldCount + X;
     * }
     */
    public void addJDOGetManagedFieldCountMethod()
    {
        final String methodName = JDO_PC_jdoGetManagedFieldCount_Name;
        final String methodSig = JDO_PC_jdoGetManagedFieldCount_Sig;
        final int accessFlags = JDO_PC_jdoGetManagedFieldCount_Mods;
        final ExceptionsAttribute exceptAttr = null;

        final int managedFieldCount = analyzer.getManagedFieldCount();
        affirm(managedFieldCount >= 0);

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // push total (absolute) number of managed fields
        final boolean isPCRoot = analyzer.isAugmentableAsRoot();
        if (isPCRoot) {
            insn = insn.append(InsnUtils.integerConstant(managedFieldCount, pool));
        }
        else {
            final ConstClass superConstClass = classFile.superName();
            affirm(superConstClass != null);
            final String superClassName = superConstClass.asString();
            affirm(superClassName != null);
            // call the superclass' jdoGetManagedFieldCount method
            insn = insn.append(
                Insn.create(opc_invokestatic,
                            pool.addMethodRef(
                                superClassName,
                                JDO_PC_jdoGetManagedFieldCount_Name,
                                JDO_PC_jdoGetManagedFieldCount_Sig)));
            insn = insn.append(InsnUtils.integerConstant(managedFieldCount, pool));
            insn = insn.append(Insn.create(opc_iadd));
        }

        // end of method body
        insn = insn.append(Insn.create(opc_ireturn));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                isPCRoot ? 1 : 2, // maxStack
                                0, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Adds the initialization code for the jdoInheritedFieldCount field.
     */
    private Insn initJdoInheritedFieldCount(Insn insn) 
    {
        affirm(insn != null);

        // invoke jdoGetManagedFieldCount if not PCRoot class
        final boolean isPCRoot = analyzer.isAugmentableAsRoot();
        if (isPCRoot) {
            insn = insn.append(Insn.create(opc_iconst_0));
        } else {
            final ConstClass superConstClass = classFile.superName();
            affirm(superConstClass != null);
            final String superClassName = superConstClass.asString();
            affirm(superClassName != null);
            insn = insn.append(
                Insn.create(opc_invokestatic,
                            pool.addMethodRef(
                                superClassName,
                                JDO_PC_jdoGetManagedFieldCount_Name,
                                JDO_PC_jdoGetManagedFieldCount_Sig)));
        }

        // store to field
        insn = insn.append(
            Insn.create(opc_putstatic,
                        pool.addFieldRef(
                            className,
                            JDO_PC_jdoInheritedFieldCount_Name,
                            JDO_PC_jdoInheritedFieldCount_Sig)));

        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the initialization code for the jdoFieldNames field.
     */
    private Insn initJdoFieldNames(Insn insn)
    {
        affirm(insn != null);

        final int managedFieldCount = analyzer.getManagedFieldCount();
        final String[] managedFieldNames = analyzer.getAnnotatedFieldNames();
        affirm(managedFieldNames.length >= managedFieldCount);
        
        // create array
        affirm(NameHelper.elementPathForSig(JDO_PC_jdoFieldNames_Sig)
               .equals(JAVA_String_Path));
        insn = insn.append(InsnUtils.integerConstant(managedFieldCount, pool));
        insn = insn.append(
            Insn.create(opc_anewarray,
                        pool.addClass(JAVA_String_Path)));

        // initialize elements
        for (int i = 0; i < managedFieldCount; i++) {
            insn = insn.append(Insn.create(opc_dup));
            insn = insn.append(InsnUtils.integerConstant(i, pool));
            final String name = managedFieldNames[i];
            affirm(name != null);
            insn = insn.append(
                InsnUtils.stringConstant(name, pool));
            insn = insn.append(Insn.create(opc_aastore));
        }

        // store to field
        insn = insn.append(
            Insn.create(opc_putstatic,
                        getjdoFieldNamesFieldRef()));

        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the initialization code for the jdoFieldTypes field.
     */
    private Insn initJdoFieldTypes(Insn insn) 
    {
        affirm(insn != null);

        final int managedFieldCount = analyzer.getManagedFieldCount();
        final String[] managedFieldSigs = analyzer.getAnnotatedFieldSigs();
        affirm(managedFieldSigs.length >= managedFieldCount);
        
        // create array
        affirm(NameHelper.elementPathForSig(JDO_PC_jdoFieldTypes_Sig)
               .equals(JAVA_Class_Path));
        insn = insn.append(InsnUtils.integerConstant(managedFieldCount, pool));
        insn = insn.append(
            Insn.create(opc_anewarray,
                        pool.addClass(JAVA_Class_Path)));

        // initialize elements
        for (int i = 0; i < managedFieldCount; i++) {
            insn = insn.append(Insn.create(opc_dup));
            insn = insn.append(InsnUtils.integerConstant(i, pool));
            final String sig = managedFieldSigs[i];
            affirm(sig != null && sig.length() > 0);

            // push the class object
            // If the field is of primitive type then access the
            // corresponding wrapper class' static 'TYPE' field;
            // otherwise call generated, static method sunjdoClassForName.
            switch (sig.charAt(0)) {
            case 'Z':
                // for primitive types, the wrapper's TYPE field is pushed
                insn = insn.append(
                    Insn.create(opc_getstatic,
                                pool.addFieldRef(
                                    JAVA_Boolean_Path,
                                    JAVA_Boolean_TYPE_Name,
                                    JAVA_Boolean_TYPE_Sig)));
                break;
            case 'C':
                // for primitive types, the wrapper's TYPE field is pushed
                insn = insn.append(
                    Insn.create(opc_getstatic,
                                pool.addFieldRef(
                                    JAVA_Character_Path,
                                    JAVA_Character_TYPE_Name,
                                    JAVA_Character_TYPE_Sig)));
                break;
            case 'B':
                // for primitive types, the wrapper's TYPE field is pushed
                insn = insn.append(
                    Insn.create(opc_getstatic,
                                pool.addFieldRef(
                                    JAVA_Byte_Path,
                                    JAVA_Byte_TYPE_Name,
                                    JAVA_Byte_TYPE_Sig)));
                break;
            case 'S':
                // for primitive types, the wrapper's TYPE field is pushed
                insn = insn.append(
                    Insn.create(opc_getstatic,
                                pool.addFieldRef(
                                    JAVA_Short_Path,
                                    JAVA_Short_TYPE_Name,
                                    JAVA_Short_TYPE_Sig)));
                break;
            case 'I':
                // for primitive types, the wrapper's TYPE field is pushed
                insn = insn.append(
                    Insn.create(opc_getstatic,
                                pool.addFieldRef(
                                    JAVA_Integer_Path,
                                    JAVA_Integer_TYPE_Name,
                                    JAVA_Integer_TYPE_Sig)));
                break;
            case 'J':
                // for primitive types, the wrapper's TYPE field is pushed
                insn = insn.append(
                    Insn.create(opc_getstatic,
                                pool.addFieldRef(
                                    JAVA_Long_Path,
                                    JAVA_Long_TYPE_Name,
                                    JAVA_Long_TYPE_Sig)));
                break;
            case 'F':
                // for primitive types, the wrapper's TYPE field is pushed
                insn = insn.append(
                    Insn.create(opc_getstatic,
                                pool.addFieldRef(
                                    JAVA_Float_Path,
                                    JAVA_Float_TYPE_Name,
                                    JAVA_Float_TYPE_Sig)));
                break;
            case 'D':
                // for primitive types, the wrapper's TYPE field is pushed
                insn = insn.append(
                    Insn.create(opc_getstatic,
                                pool.addFieldRef(
                                    JAVA_Double_Path,
                                    JAVA_Double_TYPE_Name,
                                    JAVA_Double_TYPE_Sig)));
                break;
            case 'L':
                // for object types, the signature is simply converted 
                // into a type name, e.g.:
                //     ldc #13 <String "java.lang.String">
                insn = insn.append(
                    InsnUtils.stringConstant(
                        NameHelper.typeForSig(sig), pool));

                // push class object using the generated helper method
                insn = insn.append(
                    Insn.create(opc_invokestatic,
                                pool.addMethodRef(
                                    className,
                                    SUNJDO_PC_sunjdoClassForName_Name,
                                    SUNJDO_PC_sunjdoClassForName_Sig)));
                break;
            case '[':
                // for array types, the element's signature is simply
                // converted into a type name, e.g.:
                //     ldc #10 <String "[I">
                //     ldc #15 <String "[Ljava.lang.String;">
                insn = insn.append(
                    InsnUtils.stringConstant(
                        NameHelper.typeForPath(sig), pool));

                // push class object using the generated helper method
                insn = insn.append(
                    Insn.create(opc_invokestatic,
                                pool.addMethodRef(
                                    className,
                                    SUNJDO_PC_sunjdoClassForName_Name,
                                    SUNJDO_PC_sunjdoClassForName_Sig)));
                break;
            default:
                affirm(false, "Illegal field type: " + sig);
            }

            insn = insn.append(Insn.create(opc_aastore));
        }

        // store to field
        insn = insn.append(
            Insn.create(opc_putstatic,
                        getjdoFieldTypesFieldRef()));

        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the initialization code for the jdoFieldFlags field.
     */
    private Insn initJdoFieldFlags(Insn insn)
    {
        affirm(insn != null);

        final int managedFieldCount = analyzer.getManagedFieldCount();
        final int[] managedFieldFlags = analyzer.getAnnotatedFieldFlags();
        affirm(managedFieldFlags.length >= managedFieldCount);
        
        // create array
        affirm(NameHelper.elementSigForSig(JDO_PC_jdoFieldFlags_Sig)
               .equals("B"));
        insn = insn.append(InsnUtils.integerConstant(managedFieldCount, pool));
        insn = insn.append(
            Insn.create(opc_newarray, T_BYTE));

        // initialize elements
        for (int i = 0; i < managedFieldCount; i++) {
            insn = insn.append(Insn.create(opc_dup));
            insn = insn.append(InsnUtils.integerConstant(i, pool));
            final int flags = managedFieldFlags[i];

            // ensure we're using [opc_iconst_x .. opc_bipush]
            affirm(-128 <= flags && flags < 128);
            insn = insn.append(InsnUtils.integerConstant(flags, pool));
            insn = insn.append(Insn.create(opc_bastore));
        }

        // store to field
        insn = insn.append(
            Insn.create(opc_putstatic,
                        getjdoFieldFlagsFieldRef()));

        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the initialization code for the jdoPersistenceCapableSuperclass
     * field.
     */
    private Insn initJdoPersistenceCapableSuperclass(Insn insn) 
    {
        affirm(insn != null);

        final String pcSuperName = analyzer.getPCSuperClassName();
        final String pcRootName = analyzer.getPCRootClassName();
        affirm(pcSuperName == null || pcRootName != null);
        //final ConstClass superConstClass = classFile.superName();
        //affirm(pcSuperName == null || superConstClass != null);
        //affirm(pcRootName == null || superConstClass != null);

        if (pcSuperName == null) {
            insn = insn.append(Insn.create(opc_aconst_null));
        } else {
            // the type name is used for loading, e.g.:
            //     ldc #13 <String "java.lang.String">
            insn = insn.append(
                InsnUtils.stringConstant(
                    NameHelper.typeForPath(pcSuperName), pool));
            
            //^olsen: decide on whether to use PCRoot class or superclass
            // push class object using the generated helper method
            insn = insn.append(
                Insn.create(opc_invokestatic,
                            pool.addMethodRef(
                                pcRootName, //superConstClass.asString(),
                                SUNJDO_PC_sunjdoClassForName_Name,
                                SUNJDO_PC_sunjdoClassForName_Sig)));
        }
        
        // store to field
        insn = insn.append(
            Insn.create(opc_putstatic,
                        getjdoPersistenceCapableSuperclassFieldRef()));
        
        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the code for the jdoPersistenceCapableSuperclass
     * field.
     */
    private Insn registerClass(Insn insn) 
    {
        affirm(insn != null);

        final String pcRootName = analyzer.getPCRootClassName();
        //final ConstClass superConstClass = classFile.superName();
        //affirm(pcRootName == null || superConstClass != null);

        // push the class object for this class
        // the type name is used for loading, e.g.:
        //     ldc #13 <String "java.lang.String">
        insn = insn.append(
            InsnUtils.stringConstant(
                NameHelper.typeForPath(className), pool));
        
        //^olsen: decide on whether to use PCRoot class or superclass
        // push class object using the generated helper method
        insn = insn.append(
            Insn.create(opc_invokestatic,
                        pool.addMethodRef(
                            pcRootName, //superConstClass.asString(),
                            SUNJDO_PC_sunjdoClassForName_Name,
                            SUNJDO_PC_sunjdoClassForName_Sig)));

        // push the jdoFieldNames field
        insn = insn.append(
            Insn.create(opc_getstatic,
                        getjdoFieldNamesFieldRef()));

        // push the jdoFieldTypes field
        insn = insn.append(
            Insn.create(opc_getstatic,
                        getjdoFieldTypesFieldRef()));

        // push the jdoFieldFlags field
        insn = insn.append(
            Insn.create(opc_getstatic,
                        getjdoFieldFlagsFieldRef()));

        // push the jdoPersistenceCapableSuperclass field
        insn = insn.append(
            Insn.create(opc_getstatic,
                        getjdoPersistenceCapableSuperclassFieldRef()));

        // push a newly created an instance of this class or null if
        // class is abstract
        if (classFile.isAbstract()) {
            insn = insn.append(Insn.create(opc_aconst_null));
        } else {
            final ConstClass thisConstClass = classFile.className();
            affirm(thisConstClass != null);
            insn = insn.append(Insn.create(opc_new, thisConstClass));
            insn = insn.append(Insn.create(opc_dup));
            insn = insn.append(
                Insn.create(opc_invokespecial,
                            pool.addMethodRef(
                                className,
                                NameHelper.constructorName(),
                                NameHelper.constructorSig())));
        }
        
        // invoke registerClass
        insn = insn.append(
            Insn.create(opc_invokestatic,
                        pool.addMethodRef(
                            JDO_JDOImplHelper_Path,
                            JDO_JDOImplHelper_registerClass_Name,
                            JDO_JDOImplHelper_registerClass_Sig)));
        
        affirm(insn != null);
        return insn;
    }
    
    /**
     * Build the static initialization code for the class.
     *
     * static
     * {
     *     jdoInheritedFieldCount = 0 | super.jdoGetManagedFieldCount();
     *     jdoFieldNames = new String[]{ ... };
     *     jdoFieldTypes = new Class[]{ ... };
     *     jdoFieldFlags = new byte[]{ ... };
     *     jdoPersistenceCapableSuperclass = ...;
     *     javax.jdo.JDOImplHelper.registerClass(
     *         XXX.class, 
     *         jdoFieldNames, 
     *         jdoFieldTypes, 
     *         jdoFieldFlags, 
     *         jdoPersistenceCapableSuperclass, 
     *         new XXX()
     *     );
     * }
     */
    public void addStaticInitialization()
    {
        final String methodName = JAVA_clinit_Name;
        final String methodSig = JAVA_clinit_Sig;
        final int accessFlags = JAVA_clinit_Mods;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // initialize jdo fields
        insn = initJdoInheritedFieldCount(insn);
        insn = initJdoFieldNames(insn);
        insn = initJdoFieldTypes(insn);
        insn = initJdoFieldFlags(insn);
        insn = initJdoPersistenceCapableSuperclass(insn);

        // invoke registerClass
        insn = registerClass(insn);

        // add or extend the static initializer
        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                7, // maxStack
                                0, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());

        if (analyzer.hasStaticInitializer()) {
            // not end of method body
            augmenter.prependMethod(methodName, methodSig, 
                                    codeAttr, exceptAttr);
        } else {
            // end of method body
            insn = insn.append(Insn.create(opc_return));
            augmenter.addMethod(methodName, methodSig, accessFlags,
                                codeAttr, exceptAttr);
        }
    }

    // ----------------------------------------------------------------------

    /**
     * Build the jdoNewInstance method for the class.
     *
     * public PersistenceCapable jdoNewInstance(StateManager sm)
     * {
     *     final XXX pc = new XXX();
     *     pc.jdoFlags = 1; // == LOAD_REQUIRED
     *     pc.jdoStateManager = sm;
     *     return pc;
     * }
     */
    public void addJDONewInstanceMethod()
    {
        final String methodName = JDO_PC_jdoNewInstance_Name;
        final String methodSig = JDO_PC_jdoNewInstance_Sig;
        final int accessFlags = JDO_PC_jdoNewInstance_Mods;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // push a newly created an instance of this class
        final ConstClass thisConstClass = classFile.className();
        affirm(thisConstClass != null);
        insn = insn.append(Insn.create(opc_new, thisConstClass));
        insn = insn.append(Insn.create(opc_dup));
        insn = insn.append(
            Insn.create(opc_invokespecial,
                        pool.addMethodRef(
                            className,
                            NameHelper.constructorName(),
                            NameHelper.constructorSig())));
        insn = insn.append(Insn.create(opc_astore_2));

        // init jdo flags and assign argument to sm
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_iconst_1));
        insn = insn.append(
            Insn.create(opc_putfield,
                        getjdoFlagsFieldRef()));
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(
            Insn.create(opc_putfield,
                        getjdoStateManagerFieldRef()));

        // end of method body
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_areturn));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                2, // maxStack
                                3, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    /**
     * Build the jdoNewInstance method for the class.
     *
     * public PersistenceCapable jdoNewInstance(StateManager sm, Object oid)
     * {
     *     final XXX pc = new XXX();
     *     pc.jdoCopyKeyFieldsFromObjectId(oid);
     *     pc.jdoFlags = 1; // == LOAD_REQUIRED
     *     pc.jdoStateManager = sm;
     *     return pc;
     * }
     */
    public void addJDONewInstanceOidMethod()
    {
        final String methodName = JDO_PC_jdoNewInstance_Object_Name;
        final String methodSig = JDO_PC_jdoNewInstance_Object_Sig;
        final int accessFlags = JDO_PC_jdoNewInstance_Object_Mods;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // push a newly created an instance of this class
        final ConstClass thisConstClass = classFile.className();
        affirm(thisConstClass != null);
        insn = insn.append(Insn.create(opc_new, thisConstClass));
        insn = insn.append(Insn.create(opc_dup));
        insn = insn.append(
            Insn.create(opc_invokespecial,
                        pool.addMethodRef(
                            className,
                            NameHelper.constructorName(),
                            NameHelper.constructorSig())));
        insn = insn.append(Insn.create(opc_astore_3));

        // class on instance pc.jdoCopyKeyFieldsFromObjectId(oid)
        //^olsen: javac uses the truelly declaring class
        final String pcKeyOwnerClassName = analyzer.getPCKeyOwnerClassName();
        affirm(pcKeyOwnerClassName != null);
        insn = insn.append(Insn.create(opc_aload_3));
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(
            Insn.create(opc_invokevirtual,
                        pool.addMethodRef(
                            pcKeyOwnerClassName,
                            JDO_PC_jdoCopyKeyFieldsFromObjectId_Name,
                            JDO_PC_jdoCopyKeyFieldsFromObjectId_Sig)));

        // init jdo flags and assign argument to sm
        insn = insn.append(Insn.create(opc_aload_3));
        insn = insn.append(Insn.create(opc_iconst_1));
        insn = insn.append(
            Insn.create(opc_putfield,
                        getjdoFlagsFieldRef()));
        insn = insn.append(Insn.create(opc_aload_3));
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(
            Insn.create(opc_putfield,
                        getjdoStateManagerFieldRef()));

        // end of method body
        insn = insn.append(Insn.create(opc_aload_3));
        insn = insn.append(Insn.create(opc_areturn));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                2, // maxStack
                                4, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Adds the code for the begin of the jdoProvideField and
     * jdoReplaceField methods.
     */
    private Insn appendBeginProvideReplaceField(Insn insn)
    {
        affirm(insn != null);

        // store the sm field into local var
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(opc_getfield,
                        getjdoStateManagerFieldRef()));
        insn = insn.append(Insn.create(opc_astore_2));

        // push (fieldnumber - jdoInheritedFieldCount)
        insn = insn.append(Insn.create(opc_iload_1));
        insn = insn.append(
            Insn.create(opc_getstatic,
                        pool.addFieldRef(
                            className,
                            JDO_PC_jdoInheritedFieldCount_Name,
                            JDO_PC_jdoInheritedFieldCount_Sig)));
        insn = insn.append(Insn.create(opc_isub));
        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the default-branch code for the jdoProvideField and
     * jdoReplaceField methods.
     */
    private Insn appendEndProvideReplaceField(Insn insn,
                                              String provideReplaceField_Name,
                                              String provideReplaceField_Sig)
    {
        affirm(insn != null);
        affirm(provideReplaceField_Name);
        affirm(provideReplaceField_Sig);

        // throw exception or delegate to PC superclass
        final boolean isPCRoot = analyzer.isAugmentableAsRoot();
        if (isPCRoot) {
            insn = appendThrowJavaException(insn,
                                            JAVA_IllegalArgumentException_Path,
                                            "arg1");
        } else {
            // call super.jdoProvideField(int)
            final ConstClass superConstClass = classFile.superName();
            affirm(superConstClass != null);
            final String superClassName = superConstClass.asString();
            affirm(superClassName != null);
            insn = insn.append(Insn.create(opc_aload_0));
            insn = insn.append(Insn.create(opc_iload_1));
            insn = insn.append(
                Insn.create(opc_invokespecial,
                            pool.addMethodRef(
                                superClassName,
                                provideReplaceField_Name,
                                provideReplaceField_Sig)));
            insn = insn.append(Insn.create(opc_return));
        }        

        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the code for one case-branch in the jdoProvideField method.
     */
    private Insn appendCaseBranchForProvideField(Insn insn,
                                                 String providedXXXField_Name,
                                                 String providedXXXField_Sig,
                                                 ConstFieldRef managedFieldRef)
    {
        affirm(insn != null);
        affirm(providedXXXField_Name != null);
        affirm(providedXXXField_Sig != null);
        affirm(managedFieldRef != null);

        // check sm
        insn = appendCheckVarNonNull(insn, 2,
                                     JAVA_IllegalStateException_Path,
                                     "arg0." + JDO_PC_jdoStateManager_Name);

        // push sm and args: this, fieldnumber, and field
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_iload_1));
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_getfield, managedFieldRef));

        // call providedXXXField
        insn = insn.append(
            new InsnInterfaceInvoke(
                pool.addInterfaceMethodRef(
                    JDO_StateManager_Path,
                    providedXXXField_Name,
                    providedXXXField_Sig),
                countMethodArgWords(providedXXXField_Sig)));

        // return
        insn = insn.append(Insn.create(opc_return));
        
        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the switch code for the jdoProvideField method.
     */
    private Insn appendSwitchForProvideField(Insn insn,
                                             SizeHolder sizeHolder)
    {
        affirm(insn != null);
        affirm(sizeHolder != null);

        // generate the switch-statement only if more than zero fields
        final int managedFieldCount = analyzer.getManagedFieldCount();
        //if (managedFieldCount == 0) {
        //    return insn;
        //}        

        // get types of and field references of the managed fields
        final String[] managedFieldSigs = analyzer.getAnnotatedFieldSigs();
        final ConstFieldRef[] managedFieldRefs = getAnnotatedFieldRefs();
        affirm(managedFieldSigs.length >= managedFieldCount);
        affirm(managedFieldRefs.length >= managedFieldCount);

        // generate the switch
        final int lowOp = 0;
        final InsnTarget defaultOp = new InsnTarget();
        final InsnTarget[] targetsOp = new InsnTarget[managedFieldCount];
        for (int i = 0; i < managedFieldCount; i++) {
            targetsOp[i] = new InsnTarget();
        }            

        // javac prefers lookup switches for 1-element tables
        if (managedFieldCount <= 1) {
            final int[] matchesOp
                = (managedFieldCount == 0 ? new int[]{} : new int[]{ lowOp });
            insn = insn.append(
                new InsnLookupSwitch(defaultOp, matchesOp, targetsOp));
        } else {
            insn = insn.append(
                new InsnTableSwitch(lowOp, defaultOp, targetsOp));
        }
        
        // generate the case-targets for the method calls
        for (int i = 0; i < managedFieldCount; i++) {
            // target for accessing field [i]
            insn = insn.append(targetsOp[i]);

            // get signature and constant field reference for field
            final String sig = managedFieldSigs[i];
            final ConstFieldRef ref = managedFieldRefs[i];
            affirm(sig != null && sig.length() > 0);
            affirm(ref != null);

            // compute stack demand
            sizeHolder.size = max(sizeHolder.size,
                                  Descriptor.countFieldWords(sig));

            // generate the case-branch for a field depending on its type
            switch (sig.charAt(0)) {
            case 'Z':
                insn = appendCaseBranchForProvideField(
                    insn,
                    JDO_SM_providedBooleanField_Name,
                    JDO_SM_providedBooleanField_Sig,
                    ref);
                break;
            case 'C':
                insn = appendCaseBranchForProvideField(
                    insn,
                    JDO_SM_providedCharField_Name,
                    JDO_SM_providedCharField_Sig,
                    ref);
                break;
            case 'B':
                insn = appendCaseBranchForProvideField(
                    insn,
                    JDO_SM_providedByteField_Name,
                    JDO_SM_providedByteField_Sig,
                    ref);
                break;
            case 'S':
                insn = appendCaseBranchForProvideField(
                    insn,
                    JDO_SM_providedShortField_Name,
                    JDO_SM_providedShortField_Sig,
                    ref);
                break;
            case 'I':
                insn = appendCaseBranchForProvideField(
                    insn,
                    JDO_SM_providedIntField_Name,
                    JDO_SM_providedIntField_Sig,
                    ref);
                break;
            case 'J':
                insn = appendCaseBranchForProvideField(
                    insn,
                    JDO_SM_providedLongField_Name,
                    JDO_SM_providedLongField_Sig,
                    ref);
                break;
            case 'F':
                insn = appendCaseBranchForProvideField(
                    insn,
                    JDO_SM_providedFloatField_Name,
                    JDO_SM_providedFloatField_Sig,
                    ref);
                break;
            case 'D':
                insn = appendCaseBranchForProvideField(
                    insn,
                    JDO_SM_providedDoubleField_Name,
                    JDO_SM_providedDoubleField_Sig,
                    ref);
                break;
            case 'L':
            case '[':
                if (sig.equals(JAVA_String_Sig)) {
                    insn = appendCaseBranchForProvideField(
                        insn,
                        JDO_SM_providedStringField_Name,
                        JDO_SM_providedStringField_Sig,
                        ref);
                } else {
                    insn = appendCaseBranchForProvideField(
                        insn,
                        JDO_SM_providedObjectField_Name,
                        JDO_SM_providedObjectField_Sig,
                        ref);
                }
                break;
            default:
                affirm(false, "Illegal field type: " + sig);
            }
        }
        
        // the default branch target comes next
        insn = insn.append(defaultOp);        

        affirm(insn != null);
        return insn;
    }

    /**
     * Build the jdoProvideField method for the class.
     *
     * public void jdoProvideField(int fieldnumber)
     * {
     *     final javax.jdo.StateManager sm = this.jdoStateManager;
     *     switch(fieldnumber - jdoInheritedFieldCount) {
     *     case 0:
     *         sm.providedXXXField(this, fieldnumber, this.yyy);
     *         return;
     *     case 1:
     *         ...
     *     default:
     *         <if (isPCRoot) {>
     *             throw new javax.jdo.JDOFatalInternalException();
     *         <} else {>
     *             super.jdoProvideField(fieldnumber);
     *         <}>
     *     }
     * }
     */
    public void addJDOProvideFieldMethod()
    {
        final String methodName = JDO_PC_jdoProvideField_Name;
        final String methodSig = JDO_PC_jdoProvideField_Sig;
        final int accessFlags = JDO_PC_jdoProvideField_Mods;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // generate the begin code
        insn = appendBeginProvideReplaceField(insn);
        
        // generate the switch code
        final SizeHolder sizeHolder = new SizeHolder();
        insn = appendSwitchForProvideField(insn, sizeHolder);
        
        // generate the default-branch code with throw/return
        insn = appendEndProvideReplaceField(insn,
                                            JDO_PC_jdoProvideField_Name,
                                            JDO_PC_jdoProvideField_Sig);

        // end of method body
        affirm(insn.opcode() == opc_athrow || insn.opcode() == opc_return);
        
        affirm(0 <= sizeHolder.size && sizeHolder.size <= 2);
        //System.out.println("sizeHolder.size = " + sizeHolder.size);
        final int maxStack = (sizeHolder.size == 0
                              ? 3 : (sizeHolder.size == 1 ? 4 : 5));
        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                maxStack, // maxStack
                                3, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    /**
     * Adds the code for one case-branch in the jdoReplaceField method.
     */
    private Insn appendCaseBranchForReplaceField(Insn insn,
                                                 String replacingXXXField_Name,
                                                 String replacingXXXField_Sig,
                                                 ConstFieldRef managedFieldRef,
                                                 String managedFieldSig)
    {
        affirm(insn != null);
        affirm(replacingXXXField_Name != null);
        affirm(replacingXXXField_Sig != null);
        affirm(managedFieldRef != null);
        affirm(managedFieldSig != null);

        // check sm
        insn = appendCheckVarNonNull(insn, 2,
                                     JAVA_IllegalStateException_Path,
                                     "arg0." + JDO_PC_jdoStateManager_Name);

        // push this, sm and args: this and fieldnumber
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_iload_1));

        // call replacingXXXField
        insn = insn.append(
            new InsnInterfaceInvoke(
                pool.addInterfaceMethodRef(
                    JDO_StateManager_Path,
                    replacingXXXField_Name,
                    replacingXXXField_Sig),
                countMethodArgWords(replacingXXXField_Sig)));

        // put to field with downcast for Object return types
        if (replacingXXXField_Name.equals(JDO_SM_replacingObjectField_Name)) {
            final String fieldType = NameHelper.pathForSig(managedFieldSig);
            insn = insn.append(
                Insn.create(opc_checkcast,
                            pool.addClass(fieldType)));
        }
        insn = insn.append(Insn.create(opc_putfield, managedFieldRef));

        // return
        insn = insn.append(Insn.create(opc_return));
        
        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the switch code for the jdoReplaceField method.
     */
    private Insn appendSwitchForReplaceField(Insn insn,
                                             SizeHolder sizeHolder)
    {
        affirm(insn != null);
        affirm(sizeHolder != null);

        // generate the switch-statement only if more than zero fields
        final int managedFieldCount = analyzer.getManagedFieldCount();
        //if (managedFieldCount == 0) {
        //    return insn;
        //}        

        // get types of and field references of the managed fields
        final String[] managedFieldSigs = analyzer.getAnnotatedFieldSigs();
        final ConstFieldRef[] managedFieldRefs = getAnnotatedFieldRefs();
        affirm(managedFieldSigs.length >= managedFieldCount);
        affirm(managedFieldRefs.length >= managedFieldCount);

        // generate the switch
        final int lowOp = 0;
        final InsnTarget defaultOp = new InsnTarget();
        final InsnTarget[] targetsOp = new InsnTarget[managedFieldCount];
        for (int i = 0; i < managedFieldCount; i++) {
            targetsOp[i] = new InsnTarget();
        }            

        // javac prefers lookup switches for 1-element tables
        if (managedFieldCount <= 1) {
            final int[] matchesOp
                = (managedFieldCount == 0 ? new int[]{} : new int[]{ lowOp });
            insn = insn.append(
                new InsnLookupSwitch(defaultOp, matchesOp, targetsOp));
        } else {
            insn = insn.append(
                new InsnTableSwitch(lowOp, defaultOp, targetsOp));
        }
        
        // generate the case-targets for the method calls
        for (int i = 0; i < managedFieldCount; i++) {
            // target for accessing field [i]
            insn = insn.append(targetsOp[i]);

            // get signature and constant field reference for field
            final String sig = managedFieldSigs[i];
            final ConstFieldRef ref = managedFieldRefs[i];
            affirm(sig != null && sig.length() > 0);
            affirm(ref != null);

            // compute stack demand
            sizeHolder.size = max(sizeHolder.size,
                                  Descriptor.countFieldWords(sig));

            // generate the case-branch for a field depending on its type
            switch (sig.charAt(0)) {
            case 'Z':
                insn = appendCaseBranchForReplaceField(
                    insn,
                    JDO_SM_replacingBooleanField_Name,
                    JDO_SM_replacingBooleanField_Sig,
                    ref, sig);
                break;
            case 'C':
                insn = appendCaseBranchForReplaceField(
                    insn,
                    JDO_SM_replacingCharField_Name,
                    JDO_SM_replacingCharField_Sig,
                    ref, sig);
                break;
            case 'B':
                insn = appendCaseBranchForReplaceField(
                    insn,
                    JDO_SM_replacingByteField_Name,
                    JDO_SM_replacingByteField_Sig,
                    ref, sig);
                break;
            case 'S':
                insn = appendCaseBranchForReplaceField(
                    insn,
                    JDO_SM_replacingShortField_Name,
                    JDO_SM_replacingShortField_Sig,
                    ref, sig);
                break;
            case 'I':
                insn = appendCaseBranchForReplaceField(
                    insn,
                    JDO_SM_replacingIntField_Name,
                    JDO_SM_replacingIntField_Sig,
                    ref, sig);
                break;
            case 'J':
                insn = appendCaseBranchForReplaceField(
                    insn,
                    JDO_SM_replacingLongField_Name,
                    JDO_SM_replacingLongField_Sig,
                    ref, sig);
                break;
            case 'F':
                insn = appendCaseBranchForReplaceField(
                    insn,
                    JDO_SM_replacingFloatField_Name,
                    JDO_SM_replacingFloatField_Sig,
                    ref, sig);
                break;
            case 'D':
                insn = appendCaseBranchForReplaceField(
                    insn,
                    JDO_SM_replacingDoubleField_Name,
                    JDO_SM_replacingDoubleField_Sig,
                    ref, sig);
                break;
            case 'L':
            case '[':
                if (sig.equals(JAVA_String_Sig)) {
                    insn = appendCaseBranchForReplaceField(
                        insn,
                        JDO_SM_replacingStringField_Name,
                        JDO_SM_replacingStringField_Sig,
                        ref, sig);
                } else {
                    insn = appendCaseBranchForReplaceField(
                        insn,
                        JDO_SM_replacingObjectField_Name,
                        JDO_SM_replacingObjectField_Sig,
                        ref, sig);
                }
                break;
            default:
                affirm(false, "Illegal field type: " + sig);
            }
        }
        
        // the default branch target comes next
        insn = insn.append(defaultOp);        

        affirm(insn != null);
        return insn;
    }

    /**
     * Build the jdoReplaceField method for the class.
     *
     * public void jdoReplaceField(int fieldnumber)
     * {
     *     final javax.jdo.StateManager sm = this.jdoStateManager;
     *     switch(fieldnumber - jdoInheritedFieldCount) {
     *     case 0:
     *         this.yyy = (XXX)sm.replacingXXXField(this, fieldnumber);
     *         return;
     *     case 1:
     *         ...
     *     default:
     *         <if (isPCRoot) {>
     *             throw new javax.jdo.JDOFatalInternalException();
     *         <} else {>
     *             super.jdoReplaceField(fieldnumber);
     *         <}>
     *     }
     * }
     */
    public void addJDOReplaceFieldMethod()
    {
        final String methodName = JDO_PC_jdoReplaceField_Name;
        final String methodSig = JDO_PC_jdoReplaceField_Sig;
        final int accessFlags = JDO_PC_jdoReplaceField_Mods;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // generate the begin code
        insn = appendBeginProvideReplaceField(insn);
        
        // generate the switch code
        final SizeHolder sizeHolder = new SizeHolder();
        insn = appendSwitchForReplaceField(insn, sizeHolder);
        
        // generate the default-branch code with throw/return
        insn = appendEndProvideReplaceField(insn,
                                            JDO_PC_jdoReplaceField_Name,
                                            JDO_PC_jdoReplaceField_Sig);

        // end of method body
        affirm(insn.opcode() == opc_athrow || insn.opcode() == opc_return);

        affirm(0 <= sizeHolder.size && sizeHolder.size <= 2);
        //System.out.println("sizeHolder.size = " + sizeHolder.size);
        final int maxStack = (sizeHolder.size == 0 ? 3 : 4);
        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                maxStack, // maxStack
                                3, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Adds the code for the begin of the jdoCopyField method.
     */
    private Insn appendBeginCopyField(Insn insn)
    {
        affirm(insn != null);

        // push (fieldnumber - jdoInheritedFieldCount)
        insn = insn.append(Insn.create(opc_iload_2));
        insn = insn.append(
            Insn.create(opc_getstatic,
                        pool.addFieldRef(
                            className,
                            JDO_PC_jdoInheritedFieldCount_Name,
                            JDO_PC_jdoInheritedFieldCount_Sig)));
        insn = insn.append(Insn.create(opc_isub));

        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the default-branch code for the jdoCopyField method.
     */
    private Insn appendEndCopyField(Insn insn)
    {
        affirm(insn != null);

        // throw exception or delegate to PC superclass
        final boolean isPCRoot = analyzer.isAugmentableAsRoot();
        if (isPCRoot) {
            insn = appendThrowJavaException(insn,
                                            JAVA_IllegalArgumentException_Path,
                                            "arg2");
        } else {
            // call super.jdoCopyField(XXX, int)

            //^olsen: javac uses the truelly declaring class
            //final ConstClass superConstClass = classFile.superName();
            //affirm(superConstClass != null);
            //final String superClassName = superConstClass.asString();
            //affirm(superClassName != null);

            // must use pcSuperClass (not the immediate superclass) in order
            // to match formal parameter of jdoCopyField
            final String superClassName = analyzer.getPCSuperClassName();
            affirm(superClassName != null);
            final String jdo_PC_jdoCopyField_Sig
                = JDONameHelper.getJDO_PC_jdoCopyField_Sig(superClassName);
            insn = insn.append(Insn.create(opc_aload_0));
            insn = insn.append(Insn.create(opc_aload_1));
            insn = insn.append(Insn.create(opc_iload_2));
            insn = insn.append(
                Insn.create(opc_invokespecial,
                            pool.addMethodRef(
                                superClassName,
                                JDO_PC_jdoCopyField_Name,
                                jdo_PC_jdoCopyField_Sig)));
            insn = insn.append(Insn.create(opc_return));
        }

        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the code for one case-branch in the jdoCopyField method.
     */
    private Insn appendCaseBranchForCopyField(Insn insn,
                                              ConstFieldRef managedFieldRef)
    {
        affirm(insn != null);
        affirm(managedFieldRef != null);

        // check arg
        insn = appendCheckVarNonNull(insn, 1,
                                     JAVA_IllegalArgumentException_Path,
                                     "arg1");

        // assign argument's field to this instance's field
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_getfield, managedFieldRef));
        insn = insn.append(Insn.create(opc_putfield, managedFieldRef));

        // return
        insn = insn.append(Insn.create(opc_return));
        
        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the switch code for the jdoCopyField method.
     */
    private Insn appendSwitchForCopyField(Insn insn)
    {
        affirm(insn != null);

        // generate the switch-statement only if more than zero fields
        final int managedFieldCount = analyzer.getManagedFieldCount();
        //if (managedFieldCount == 0) {
        //    return insn;
        //}        

        // get types of and field references of the managed fields
        final String[] managedFieldSigs = analyzer.getAnnotatedFieldSigs();
        final ConstFieldRef[] managedFieldRefs = getAnnotatedFieldRefs();
        affirm(managedFieldSigs.length >= managedFieldCount);
        affirm(managedFieldRefs.length >= managedFieldCount);

        // generate the switch
        final int lowOp = 0;
        final InsnTarget defaultOp = new InsnTarget();
        final InsnTarget[] targetsOp = new InsnTarget[managedFieldCount];
        for (int i = 0; i < managedFieldCount; i++) {
            targetsOp[i] = new InsnTarget();
        }            

        // javac prefers lookup switches for 1-element tables
        if (managedFieldCount <= 1) {
            final int[] matchesOp
                = (managedFieldCount == 0 ? new int[]{} : new int[]{ lowOp });
            insn = insn.append(
                new InsnLookupSwitch(defaultOp, matchesOp, targetsOp));
        } else {
            insn = insn.append(
                new InsnTableSwitch(lowOp, defaultOp, targetsOp));
        }
        
        // generate the case-targets for the method calls
        for (int i = 0; i < managedFieldCount; i++) {
            // target for accessing field [i]
            insn = insn.append(targetsOp[i]);

            // get signature and constant field reference for field
            final String sig = managedFieldSigs[i];
            final ConstFieldRef ref = managedFieldRefs[i];
            affirm(sig != null && sig.length() > 0);
            affirm(ref != null);

            // generate the case-branch for a field depending on its type
            switch (sig.charAt(0)) {
            case 'Z':
                insn = appendCaseBranchForCopyField(insn, ref);
                break;
            case 'C':
                insn = appendCaseBranchForCopyField(insn, ref);
                break;
            case 'B':
                insn = appendCaseBranchForCopyField(insn, ref);
                break;
            case 'S':
                insn = appendCaseBranchForCopyField(insn, ref);
                break;
            case 'I':
                insn = appendCaseBranchForCopyField(insn, ref);
                break;
            case 'J':
                insn = appendCaseBranchForCopyField(insn, ref);
                break;
            case 'F':
                insn = appendCaseBranchForCopyField(insn, ref);
                break;
            case 'D':
                insn = appendCaseBranchForCopyField(insn, ref);
                break;
            case 'L':
            case '[':
                insn = appendCaseBranchForCopyField(insn, ref);
                break;
            default:
                affirm(false, "Illegal field type: " + sig);
            }
        }
        
        // the default branch target comes next
        insn = insn.append(defaultOp);        

        affirm(insn != null);
        return insn;
    }

    /**
     * Build the jdoCopyField method for the class.
     *
     * protected final void jdoCopyField(XXX pc, int fieldnumber)
     * {
     *     switch(fieldnumber - jdoInheritedFieldCount) {
     *     case 0:
     *         this.yyy = pc.yyy;
     *         return;
     *     case 1:
     *         ...
     *     default:
     *         <if (isPCRoot) {>
     *             throw new javax.jdo.JDOFatalInternalException();
     *         <} else {>
     *             super.jdoCopyField(pc, fieldnumber);
     *         <}>
     *     }
     * }
     */
    public void addJDOCopyFieldMethod()
    {
        final String methodName = JDO_PC_jdoCopyField_Name;
        final String methodSig
            = JDONameHelper.getJDO_PC_jdoCopyField_Sig(className);
        final int accessFlags = JDO_PC_jdoCopyField_Mods;
        final ExceptionsAttribute exceptAttr = null;
        
        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // generate the begin code
        insn = appendBeginCopyField(insn);
        
        // generate the switch code
        insn = appendSwitchForCopyField(insn);
        
        // generate the default-branch code with throw/return
        insn = appendEndCopyField(insn);

        // end of method body
        affirm(insn.opcode() == opc_athrow || insn.opcode() == opc_return);
        
        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                3, // maxStack
                                3, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build the jdoArrayArgumentIteration method for the class.
     *
     * public void jdoCopyFields(java.lang.Object pc, int[] fieldnumbers)
     * {
     *     final XXX other = (XXX)pc;
     *     if (other.jdoStateManager != this.jdoStateManager
     *         || this.jdoStateManager == null) {
     *         throw new javax.jdo.JDOFatalInternalException();
     *     }
     *     final int n = fieldnumbers.length;
     *     for (int i = 0; i < n; i++) {
     *         this.jdoCopyField(other, fieldnumbers[i]);
     *     }
     * }
     */
    public void addJDOCopyFieldsMethod()
    {
        final String methodName = JDO_PC_jdoCopyFields_Name;
        final String methodSig = JDO_PC_jdoCopyFields_Sig;
        final int accessFlags = JDO_PC_jdoCopyFields_Mods;        
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // check sm
        insn = appendCheckStateManager(insn, 0,
                                       JAVA_IllegalStateException_Path,
                                       "arg0." + JDO_PC_jdoStateManager_Name);

        // check pc argument
        final ConstClass thisConstClass = classFile.className();
        affirm(thisConstClass != null);
        insn = appendCheckVarInstanceOf(insn, 1, thisConstClass,
                                        JAVA_IllegalArgumentException_Path,
                                        "arg1");

        // check fieldnumbers argument
        insn = appendCheckVarNonNull(insn, 2,
                                     JAVA_IllegalArgumentException_Path,
                                     "arg2");

        // downcast argument
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_checkcast, thisConstClass));
        insn = insn.append(Insn.create(opc_astore_3));

        // check this and argument's sm for equality
        final InsnTarget endcheck = new InsnTarget();
        insn = insn.append(Insn.create(opc_aload_3));
        insn = insn.append(
            Insn.create(
                opc_getfield,
                getjdoStateManagerFieldRef()));
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(
                opc_getfield,
                getjdoStateManagerFieldRef()));
        insn = insn.append(Insn.create(opc_if_acmpeq, endcheck));
        insn = appendThrowJavaException(insn,
                                        JAVA_IllegalArgumentException_Path,
                                        "arg1." + JDO_PC_jdoStateManager_Name);

        // store the array argument length into local var
        insn = insn.append(endcheck);
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_arraylength));
        insn = insn.append(Insn.create(opc_istore, 4));

        // init loop counter and goto loop check
        final InsnTarget loopcheck = new InsnTarget();
        insn = insn.append(Insn.create(opc_iconst_0));
        insn = insn.append(Insn.create(opc_istore, 5));
        insn = insn.append(Insn.create(opc_goto, loopcheck));

        // loop body: call self-delegating method with arguments
        final InsnTarget loopbody = new InsnTarget();
        insn = insn.append(loopbody);
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_aload_3));

        // select element from array argument at loop counter
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_iload, 5));
        insn = insn.append(Insn.create(opc_iaload));

        // call self-delegating method
        final String delegateName = JDO_PC_jdoCopyField_Name;
        final String delegateSig
            = JDONameHelper.getJDO_PC_jdoCopyField_Sig(className);
        insn = insn.append(
            Insn.create(opc_invokevirtual,
                        pool.addMethodRef(
                            className,
                            delegateName,
                            delegateSig)));

        // loop counter increment
        insn = insn.append(new InsnIInc(5, 1));

        // loop termination check
        insn = insn.append(loopcheck);
        insn = insn.append(Insn.create(opc_iload, 5));
        insn = insn.append(Insn.create(opc_iload, 4));
        insn = insn.append(Insn.create(opc_if_icmplt, loopbody));

        // end of method body
        insn = insn.append(Insn.create(opc_return));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                4, // maxStack
                                6, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build the jdoNewObjectIdInstance method for the class.
     *
     * public java.lang.Object jdoNewObjectIdInstance()
     * {
     *     return new XXX();
     * }
     */
    public void addJDONewObjectIdInstanceMethod()
    {
        final String methodName = JDO_PC_jdoNewObjectIdInstance_Name;
        final String methodSig = JDO_PC_jdoNewObjectIdInstance_Sig;
        final int accessFlags = JDO_PC_jdoNewObjectIdInstance_Mods;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // generate empty method in case of datastore identity
        final String keyClassName = analyzer.getKeyClassName();
        if (keyClassName == null){
            // end of method body
            insn = insn.append(Insn.create(opc_aconst_null));
            insn = insn.append(Insn.create(opc_areturn));

            final CodeAttribute codeAttr
                = new CodeAttribute(getCodeAttributeUtf8(),
                                    1, // maxStack
                                    1, // maxLocals
                                    begin,
                                    new ExceptionTable(),
                                    new AttributeVector());
            augmenter.addMethod(methodName, methodSig, accessFlags,
                                codeAttr, exceptAttr);
            return;
        }
        affirm(keyClassName != null);

        // push a newly created an instance of this class
        insn = insn.append(
            Insn.create(opc_new,
                        pool.addClass(keyClassName)));
        insn = insn.append(Insn.create(opc_dup));
        insn = insn.append(
            Insn.create(opc_invokespecial,
                        pool.addMethodRef(
                            keyClassName,
                            NameHelper.constructorName(),
                            NameHelper.constructorSig())));

        // end of method body
        insn = insn.append(Insn.create(opc_areturn));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                2, // maxStack
                                1, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    /**
     * Build the jdoNewObjectIdInstance method for the class.
     *
     * public java.lang.Object jdoNewObjectIdInstance(String str)
     * {
     *     return new XXX(str);
     * }
     */
    public void addJDONewObjectIdInstanceStringMethod()
    {
        final String methodName = JDO_PC_jdoNewObjectIdInstance_String_Name;
        final String methodSig = JDO_PC_jdoNewObjectIdInstance_String_Sig;
        final int accessFlags = JDO_PC_jdoNewObjectIdInstance_String_Mods;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // generate empty method in case of datastore identity
        final String keyClassName = analyzer.getKeyClassName();
        if (keyClassName == null){
            // end of method body
            insn = insn.append(Insn.create(opc_aconst_null));
            insn = insn.append(Insn.create(opc_areturn));

            final CodeAttribute codeAttr
                = new CodeAttribute(getCodeAttributeUtf8(),
                                    1, // maxStack
                                    2, // maxLocals
                                    begin,
                                    new ExceptionTable(),
                                    new AttributeVector());
            augmenter.addMethod(methodName, methodSig, accessFlags,
                                codeAttr, exceptAttr);
            return;
        }
        affirm(keyClassName != null);

        // push a newly created an instance of this class
        insn = insn.append(
            Insn.create(opc_new,
                        pool.addClass(keyClassName)));
        insn = insn.append(Insn.create(opc_dup));
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(
            Insn.create(opc_invokespecial,
                        pool.addMethodRef(
                            keyClassName,
                            NameHelper.constructorName(),
                            NameHelper.constructorSig(JAVA_String_Sig))));

        // end of method body
        insn = insn.append(Insn.create(opc_areturn));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                3, // maxStack
                                2, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build the jdoCopyKeyFieldsToObjectId method for the class.
     */
    public void addJDOCopyKeyFieldsToObjectIdMethod()
    {
        addJDOCopyKeyFieldsToFromObjectIdMethod(true);
    }
    
    /**
     * Build the jdoCopyKeyFieldsFromObjectId method for the class.
     */
    public void addJDOCopyKeyFieldsFromObjectIdMethod()
    {
        addJDOCopyKeyFieldsToFromObjectIdMethod(false);
    }
    
    /**
     * Build the jdoCopyKeyFieldsTo/FromObjectId method for the class.
     *
     * public void jdoCopyKeyFieldsTo/FromObjectId(Object oid)
     * {
     *     if (!(oid instanceof XXX)) {
     *         throw new IllegalArgumentException("arg0");
     *     }
     *     final XXX _oid = (XXX)oid;
     *     <if (superKeyClassname != null) {>
     *         super.jdoCopyKeyFieldsToObjectId(oid);
     *     <}>
     *     _oid.yyy = this.yyy;
     *     ...
     * }
     */
    private void addJDOCopyKeyFieldsToFromObjectIdMethod(boolean isToOid)
    {
        final String methodName;
        final String methodSig;
        final int accessFlags;
        if (isToOid) {
            methodName = JDO_PC_jdoCopyKeyFieldsToObjectId_Name;
            methodSig = JDO_PC_jdoCopyKeyFieldsToObjectId_Sig;
            accessFlags = JDO_PC_jdoCopyKeyFieldsToObjectId_Mods;        
        } else {
            methodName = JDO_PC_jdoCopyKeyFieldsFromObjectId_Name;
            methodSig = JDO_PC_jdoCopyKeyFieldsFromObjectId_Sig;
            accessFlags = JDO_PC_jdoCopyKeyFieldsFromObjectId_Mods;
        }
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // generate empty method in case of datastore identity
        final String keyClassName = analyzer.getKeyClassName();
        if (keyClassName == null){
            // end of method body
            insn = insn.append(Insn.create(opc_return));

            final CodeAttribute codeAttr
                = new CodeAttribute(getCodeAttributeUtf8(),
                                    0, // maxStack
                                    2, // maxLocals
                                    begin,
                                    new ExceptionTable(),
                                    new AttributeVector());
            augmenter.addMethod(methodName, methodSig, accessFlags,
                                codeAttr, exceptAttr);
            return;
        }
        affirm(keyClassName != null);

        // check oid argument
        final ConstClass keyConstClass = pool.addClass(keyClassName);
        affirm(keyConstClass != null);
        insn = appendCheckVarInstanceOf(insn, 1, keyConstClass,
                                        JAVA_IllegalArgumentException_Path,
                                        "arg1");

        // downcast argument
        insn = insn.append(Insn.create(opc_aload_1));
        insn = insn.append(Insn.create(opc_checkcast, keyConstClass));
        insn = insn.append(Insn.create(opc_astore_2));

        // check argument or delegate to superclass
        final boolean isPCRoot = analyzer.isAugmentableAsRoot();
        if (!isPCRoot) {
            // call super.jdoCopyKeyFieldsToObjectId(oid)

            //^olsen: javac uses the truelly declaring class
            //final ConstClass superConstClass = classFile.superName();
            //affirm(superConstClass != null);
            //final String superClassName = superConstClass.asString();
            //affirm(superClassName != null);

            final String superClassName
                = analyzer.getPCSuperKeyOwnerClassName();
            affirm(superClassName != null);
            insn = insn.append(Insn.create(opc_aload_0));
            insn = insn.append(Insn.create(opc_aload_2));
            insn = insn.append(
                Insn.create(opc_invokespecial,
                            pool.addMethodRef(
                                superClassName,
                                methodName,
                                methodSig)));
        }
        
        // get types of and field references of the key fields
        final int keyFieldCount = analyzer.getKeyFieldCount();
        final ConstFieldRef[] keyFieldRefs = getKeyFieldRefs();
        final ConstFieldRef[] keyClassKeyFieldRefs = getKeyClassKeyFieldRefs();
        affirm(keyFieldRefs.length == keyFieldCount);
        affirm(keyClassKeyFieldRefs.length == keyFieldCount);

        // generate the assignment statements
        int maxFieldSize = 0;
        for (int i = 0; i < keyFieldCount; i++) {
            // assign key field
            final ConstFieldRef thisClassKeyRef = keyFieldRefs[i];
            final ConstFieldRef keyClassKeyRef = keyClassKeyFieldRefs[i];
            affirm(thisClassKeyRef != null);
            affirm(keyClassKeyRef != null);
            if (isToOid) {
                insn = insn.append(Insn.create(opc_aload_2));
                insn = insn.append(Insn.create(opc_aload_0));
                insn = insn.append(Insn.create(opc_getfield, thisClassKeyRef));
                insn = insn.append(Insn.create(opc_putfield, keyClassKeyRef));
            } else {
                insn = insn.append(Insn.create(opc_aload_0));
                insn = insn.append(Insn.create(opc_aload_2));
                insn = insn.append(Insn.create(opc_getfield, keyClassKeyRef));
                insn = insn.append(Insn.create(opc_putfield, thisClassKeyRef));
            }

            // compute stack demand
            final String sig
                = thisClassKeyRef.nameAndType().signature().asString();
            affirm(sig != null && sig.length() > 0);
            maxFieldSize = max(maxFieldSize, Descriptor.countFieldWords(sig));
        }

        // end of method body
        insn = insn.append(Insn.create(opc_return));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                max(maxFieldSize + 1, 3), // maxStack
                                3, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    /**
     * Adds the code for one case-branch in the jdoCopyKeyFieldsToObjectId.
     */
    private Insn appendCopyKeyFieldToOid(Insn insn,
                                         String fetchXXXField_Name,
                                         String fetchXXXField_Sig,
                                         int keyFieldNo,
                                         ConstFieldRef keyFieldRef,
                                         String keyFieldSig)
    {
        affirm(insn != null);
        affirm(fetchXXXField_Name != null);
        affirm(fetchXXXField_Sig != null);
        affirm(keyFieldNo >= 0);
        affirm(keyFieldRef != null);
        affirm(keyFieldSig != null && keyFieldSig.length() > 0);

        // push oid, ofs
        insn = insn.append(Insn.create(opc_aload_3));
        insn = insn.append(Insn.create(opc_aload_1));

        // push absolute field index
        insn = insn.append(
            Insn.create(opc_getstatic,
                        pool.addFieldRef(
                            className,
                            JDO_PC_jdoInheritedFieldCount_Name,
                            JDO_PC_jdoInheritedFieldCount_Sig)));
        insn = insn.append(InsnUtils.integerConstant(keyFieldNo, pool));
        insn = insn.append(Insn.create(opc_iadd));

        // call fetchXXXField
        insn = insn.append(
            new InsnInterfaceInvoke(
                pool.addInterfaceMethodRef(
                    JDO_ObjectIdFieldSupplier_Path,
                    fetchXXXField_Name,
                    fetchXXXField_Sig),
                countMethodArgWords(fetchXXXField_Sig)));

        // downcast an Object value to actual field type if neccessary
        if (fetchXXXField_Name.equals(JDO_OIFS_fetchObjectField_Name)
            && !keyFieldSig.equals(JAVA_Object_Sig)) {
            final String fieldType = NameHelper.pathForSig(keyFieldSig);
            insn = insn.append(
                Insn.create(opc_checkcast,
                            pool.addClass(fieldType)));
        }

        // assign
        insn = insn.append(Insn.create(opc_putfield, keyFieldRef));
        
        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the field assignment code for the jdoCopyKeyFieldsToObjectId.
     */
    private Insn appendStatementsForCopyKeyFieldsToOid(Insn insn,
                                                       SizeHolder sizeHolder)
    {
        affirm(insn != null);
        affirm(sizeHolder != null);

        // get field references of the key fields
        final int keyFieldCount = analyzer.getKeyFieldCount();
        final ConstFieldRef[] keyFieldRefs = getKeyClassKeyFieldRefs();
        final int[] keyFieldIndexes = analyzer.getKeyFieldIndexes();
        affirm(keyFieldRefs.length == keyFieldCount);
        affirm(keyFieldIndexes.length == keyFieldCount);

        // generate the field access statements
        for (int i = 0; i < keyFieldCount; i++) {
            // get field no, constant field ref, and signature for field
            final int no = keyFieldIndexes[i];
            final ConstFieldRef ref = keyFieldRefs[i];
            affirm(ref != null);
            final String sig = ref.nameAndType().signature().asString();
            affirm(sig != null && sig.length() > 0);

            // compute stack demand
            sizeHolder.size = max(sizeHolder.size,
                                  Descriptor.countFieldWords(sig));

            // generate the field copying depending on its type
            switch (sig.charAt(0)) {
            case 'Z':
                insn = appendCopyKeyFieldToOid(
                    insn,
                    JDO_OIFS_fetchBooleanField_Name,
                    JDO_OIFS_fetchBooleanField_Sig,
                    no, ref, sig);
                break;
            case 'C':
                insn = appendCopyKeyFieldToOid(
                    insn,
                    JDO_OIFS_fetchCharField_Name,
                    JDO_OIFS_fetchCharField_Sig,
                    no, ref, sig);
                break;
            case 'B':
                insn = appendCopyKeyFieldToOid(
                    insn,
                    JDO_OIFS_fetchByteField_Name,
                    JDO_OIFS_fetchByteField_Sig,
                    no, ref, sig);
                break;
            case 'S':
                insn = appendCopyKeyFieldToOid(
                    insn,
                    JDO_OIFS_fetchShortField_Name,
                    JDO_OIFS_fetchShortField_Sig,
                    no, ref, sig);
                break;
            case 'I':
                insn = appendCopyKeyFieldToOid(
                    insn,
                    JDO_OIFS_fetchIntField_Name,
                    JDO_OIFS_fetchIntField_Sig,
                    no, ref, sig);
                break;
            case 'J':
                insn = appendCopyKeyFieldToOid(
                    insn,
                    JDO_OIFS_fetchLongField_Name,
                    JDO_OIFS_fetchLongField_Sig,
                    no, ref, sig);
                break;
            case 'F':
                insn = appendCopyKeyFieldToOid(
                    insn,
                    JDO_OIFS_fetchFloatField_Name,
                    JDO_OIFS_fetchFloatField_Sig,
                    no, ref, sig);
                break;
            case 'D':
                insn = appendCopyKeyFieldToOid(
                    insn,
                    JDO_OIFS_fetchDoubleField_Name,
                    JDO_OIFS_fetchDoubleField_Sig,
                    no, ref, sig);
                break;
            case 'L':
            case '[':
                if (sig.equals(JAVA_String_Sig)) {
                    insn = appendCopyKeyFieldToOid(
                        insn,
                        JDO_OIFS_fetchStringField_Name,
                        JDO_OIFS_fetchStringField_Sig,
                        no, ref, sig);
                } else {
                    insn = appendCopyKeyFieldToOid(
                        insn,
                        JDO_OIFS_fetchObjectField_Name,
                        JDO_OIFS_fetchObjectField_Sig,
                        no, ref, sig);
                }
                break;
            default:
                affirm(false, "Illegal field type: " + sig);
            }
        }

        affirm(insn != null);
        return insn;
    }
 
    /**
     * Adds the code for one case-branch in the jdoCopyKeyFieldsFromObjectId.
     */
    private Insn appendCopyKeyFieldFromOid(Insn insn,
                                           String storeXXXField_Name,
                                           String storeXXXField_Sig,
                                           int keyFieldNo,
                                           ConstFieldRef keyFieldRef)
    {
        affirm(insn != null);
        affirm(storeXXXField_Name != null);
        affirm(storeXXXField_Sig != null);
        affirm(keyFieldNo >= 0);
        affirm(keyFieldRef != null);

        // push ofc
        insn = insn.append(Insn.create(opc_aload_1));

        // push absolute field index
        insn = insn.append(
            Insn.create(opc_getstatic,
                        pool.addFieldRef(
                            className,
                            JDO_PC_jdoInheritedFieldCount_Name,
                            JDO_PC_jdoInheritedFieldCount_Sig)));
        insn = insn.append(InsnUtils.integerConstant(keyFieldNo, pool));
        insn = insn.append(Insn.create(opc_iadd));

        // push oid field
        insn = insn.append(Insn.create(opc_aload_3));
        insn = insn.append(Insn.create(opc_getfield, keyFieldRef));

        // call storeXXXField
        insn = insn.append(
            new InsnInterfaceInvoke(
                pool.addInterfaceMethodRef(
                    JDO_ObjectIdFieldConsumer_Path,
                    storeXXXField_Name,
                    storeXXXField_Sig),
                countMethodArgWords(storeXXXField_Sig)));

        affirm(insn != null);
        return insn;
    }
    
    /**
     * Adds the field assignment code for the jdoCopyKeyFieldsFromObjectId.
     */
    private Insn appendStatementsForCopyKeyFieldsFromOid(Insn insn,
                                                         SizeHolder sizeHolder)
    {
        affirm(insn != null);
        affirm(sizeHolder != null);

        // get field references of the key fields
        final int keyFieldCount = analyzer.getKeyFieldCount();
        final ConstFieldRef[] keyFieldRefs = getKeyClassKeyFieldRefs();
        final int[] keyFieldIndexes = analyzer.getKeyFieldIndexes();
        affirm(keyFieldRefs.length == keyFieldCount);
        affirm(keyFieldIndexes.length == keyFieldCount);

        // generate the field access statements
        for (int i = 0; i < keyFieldCount; i++) {
            // get field no, constant field ref, and signature for field
            final int no = keyFieldIndexes[i];
            final ConstFieldRef ref = keyFieldRefs[i];
            affirm(ref != null);
            final String sig = ref.nameAndType().signature().asString();
            affirm(sig != null && sig.length() > 0);

            // compute stack demand
            sizeHolder.size = max(sizeHolder.size,
                                  Descriptor.countFieldWords(sig));

            // generate the field copying depending on its type
            switch (sig.charAt(0)) {
            case 'Z':
                insn = appendCopyKeyFieldFromOid(
                    insn,
                    JDO_OIFC_storeBooleanField_Name,
                    JDO_OIFC_storeBooleanField_Sig,
                    no, ref);
                break;
            case 'C':
                insn = appendCopyKeyFieldFromOid(
                    insn,
                    JDO_OIFC_storeCharField_Name,
                    JDO_OIFC_storeCharField_Sig,
                    no, ref);
                break;
            case 'B':
                insn = appendCopyKeyFieldFromOid(
                    insn,
                    JDO_OIFC_storeByteField_Name,
                    JDO_OIFC_storeByteField_Sig,
                    no, ref);
                break;
            case 'S':
                insn = appendCopyKeyFieldFromOid(
                    insn,
                    JDO_OIFC_storeShortField_Name,
                    JDO_OIFC_storeShortField_Sig,
                    no, ref);
                break;
            case 'I':
                insn = appendCopyKeyFieldFromOid(
                    insn,
                    JDO_OIFC_storeIntField_Name,
                    JDO_OIFC_storeIntField_Sig,
                    no, ref);
                break;
            case 'J':
                insn = appendCopyKeyFieldFromOid(
                    insn,
                    JDO_OIFC_storeLongField_Name,
                    JDO_OIFC_storeLongField_Sig,
                    no, ref);
                break;
            case 'F':
                insn = appendCopyKeyFieldFromOid(
                    insn,
                    JDO_OIFC_storeFloatField_Name,
                    JDO_OIFC_storeFloatField_Sig,
                    no, ref);
                break;
            case 'D':
                insn = appendCopyKeyFieldFromOid(
                    insn,
                    JDO_OIFC_storeDoubleField_Name,
                    JDO_OIFC_storeDoubleField_Sig,
                    no, ref);
                break;
            case 'L':
            case '[':
                if (sig.equals(JAVA_String_Sig)) {
                    insn = appendCopyKeyFieldFromOid(
                        insn,
                        JDO_OIFC_storeStringField_Name,
                        JDO_OIFC_storeStringField_Sig,
                        no, ref);
                } else {
                    insn = appendCopyKeyFieldFromOid(
                        insn,
                        JDO_OIFC_storeObjectField_Name,
                        JDO_OIFC_storeObjectField_Sig,
                        no, ref);
                }
                break;
            default:
                affirm(false, "Illegal field type: " + sig);
            }
        }

        affirm(insn != null);
        return insn;
    }
 
   /**
    * Build the jdoCopyKeyFieldsToObjectId method for the class.
    */
    public void addJDOCopyKeyFieldsToObjectIdOIFSMethod()
    {
        addJDOCopyKeyFieldsToFromObjectIdOIFSMethod(true);
    }
    
   /**
    * Build the jdoCopyKeyFieldsFromObjectId method for the class.
    */
    public void addJDOCopyKeyFieldsFromObjectIdOIFCMethod()
    {
        addJDOCopyKeyFieldsToFromObjectIdOIFSMethod(false);
    }
    
    /**
     * Build the jdoCopyKeyFieldsTo/FromObjectId method for the class.
     *
     * public void jdoCopyKeyFieldsTo/FromObjectId(
     *     ObjectIdFieldSupplier/Consumer fm,
     *     Object oid)
     * {
     *     if (fm == null) {
     *         throw new IllegalArgumentException("arg0");
     *     }
     *     if (!(oid instanceof XXX)) {
     *         throw new IllegalArgumentException("arg1");
     *     }
     *     final XXX _oid = (XXX)oid;
     *     <if (superKeyClassname != null) {>
     *         super.jdoCopyKeyFieldsTo/FromObjectId(fm, _oid);
     *     <}>
     *     _oid.yyy = ofs.fetchIntField(jdoInheritedFieldCount + 0);
     *   / ofc.storeIntField(jdoInheritedFieldCount + 0, _oid.yyy);
     *     ...
     * }
     */
    private void addJDOCopyKeyFieldsToFromObjectIdOIFSMethod(boolean isToOid)
    {
        final String methodName;
        final String methodSig;
        final int accessFlags;
        if (isToOid) {
            methodName = JDO_PC_jdoCopyKeyFieldsToObjectId_OIFS_Name;
            methodSig = JDO_PC_jdoCopyKeyFieldsToObjectId_OIFS_Sig;
            accessFlags = JDO_PC_jdoCopyKeyFieldsToObjectId_OIFS_Mods;        
        } else {
            methodName = JDO_PC_jdoCopyKeyFieldsFromObjectId_OIFC_Name;
            methodSig = JDO_PC_jdoCopyKeyFieldsFromObjectId_OIFC_Sig;
            accessFlags = JDO_PC_jdoCopyKeyFieldsFromObjectId_OIFC_Mods;
        }
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // generate empty method in case of datastore identity
        final String keyClassName = analyzer.getKeyClassName();
        if (keyClassName == null){
            // end of method body
            insn = insn.append(Insn.create(opc_return));

            final CodeAttribute codeAttr
                = new CodeAttribute(getCodeAttributeUtf8(),
                                    0, // maxStack
                                    3, // maxLocals
                                    begin,
                                    new ExceptionTable(),
                                    new AttributeVector());
            augmenter.addMethod(methodName, methodSig, accessFlags,
                                codeAttr, exceptAttr);
            return;
        }
        affirm(keyClassName != null);

        // check fm argument
        insn = appendCheckVarNonNull(insn, 1,
                                     JAVA_IllegalArgumentException_Path,
                                     "arg1");
        
        // check oid argument
        final ConstClass keyConstClass = pool.addClass(keyClassName);
        affirm(keyConstClass != null);
        insn = appendCheckVarInstanceOf(insn, 2, keyConstClass,
                                        JAVA_IllegalArgumentException_Path,
                                        "arg2");

        // downcast argument
        insn = insn.append(Insn.create(opc_aload_2));
        insn = insn.append(Insn.create(opc_checkcast, keyConstClass));
        insn = insn.append(Insn.create(opc_astore_3));

        // call super.jdoCopyKeyFieldsToObjectId(oid)
        final boolean isPCRoot = analyzer.isAugmentableAsRoot();
        if (!isPCRoot) {
            // call super.jdoCopyKeyFieldsToObjectId(oid)

            //^olsen: javac uses the truelly declaring class
            //final ConstClass superConstClass = classFile.superName();
            //affirm(superConstClass != null);
            //final String superClassName = superConstClass.asString();
            //affirm(superClassName != null);

            final String superClassName
                = analyzer.getPCSuperKeyOwnerClassName();
            insn = insn.append(Insn.create(opc_aload_0));
            insn = insn.append(Insn.create(opc_aload_1));
            insn = insn.append(Insn.create(opc_aload_3));
            insn = insn.append(
                Insn.create(opc_invokespecial,
                            pool.addMethodRef(
                                superClassName,
                                methodName,
                                methodSig)));
        }
        
        // get types of and field references of the key fields
        final int keyFieldCount = analyzer.getKeyFieldCount();
        final ConstFieldRef[] keyFieldRefs = getKeyFieldRefs();
        final ConstFieldRef[] keyClassKeyFieldRefs = getKeyClassKeyFieldRefs();
        affirm(keyFieldRefs.length == keyFieldCount);
        affirm(keyClassKeyFieldRefs.length == keyFieldCount);

        // generate the case-targets for the method calls
        final SizeHolder sizeHolder = new SizeHolder();
        if (isToOid) {
            insn = appendStatementsForCopyKeyFieldsToOid(insn, sizeHolder);
        } else {
            insn = appendStatementsForCopyKeyFieldsFromOid(insn, sizeHolder);
        }

        // end of method body
        insn = insn.append(Insn.create(opc_return));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                max(sizeHolder.size
                                    + (isToOid ? 3 : 2), 3), // maxStack
                                4, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Append the code for returning the value from a direct read access.
     */
    private Insn appendDirectReadReturn(Insn insn,
                                        ConstFieldRef fieldRef)
    {
        affirm(insn != null);
        affirm(fieldRef != null);

        final String sig = fieldRef.nameAndType().signature().asString();
        affirm(sig != null && sig.length() > 0);

        // read field
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_getfield, fieldRef));
        switch (sig.charAt(0)) {
        case 'Z':
        case 'C':
        case 'B':
        case 'S':
        case 'I':
            insn = insn.append(Insn.create(opc_ireturn));
            break;
        case 'J':
            insn = insn.append(Insn.create(opc_lreturn));
            break;
        case 'F':
            insn = insn.append(Insn.create(opc_freturn));
            break;
        case 'D':
            insn = insn.append(Insn.create(opc_dreturn));
            break;
        case 'L':
        case '[':
            insn = insn.append(Insn.create(opc_areturn));
            break;
        default:
            affirm(false, "Illegal field type: " + sig);
        }

        affirm(insn != null);
        return insn;
    }

    /**
     * Build an accessor method for direct read access.
     *
     * static xxx final YYY jdoGetyyy(XXX instance)
     * {
     *     // augmentation: grant direct read access
     *     return instance.yyy;
     * }
     */
    public void addJDODirectReadAccessMethod(String methodName,
                                             String methodSig,
                                             int accessFlags,
                                             int fieldIndex)
    {
        affirm(methodName != null);      
        affirm(methodSig != null);      
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        final ConstFieldRef fieldRef = getAnnotatedFieldRefs()[fieldIndex];
        affirm(fieldRef != null);
        final String sig = fieldRef.nameAndType().signature().asString();
        affirm(sig != null && sig.length() > 0);
        final int fieldSize = ((sig.equals("J") || sig.equals("D")) ? 2 : 1);

        // return direct read
        insn = appendDirectReadReturn(insn, fieldRef);

        // end of method body

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                fieldSize, // maxStack
                                1, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    /**
     * Append the code for mediated read access.
     */
    public Insn appendMediatedReadAccess(Insn insn,
                                         int fieldIndex,
                                         ConstFieldRef fieldRef,
                                         int varStart)
    {
        affirm(insn != null);      
        affirm(fieldRef != null);

        final String sig = fieldRef.nameAndType().signature().asString();
        affirm(sig != null && sig.length() > 0);

        // store the sm field into local var
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(
                opc_getfield,
                getjdoStateManagerFieldRef()));
        insn = insn.append(InsnUtils.aStore(varStart, pool));

        // test the sm field and return field if null
        final InsnTarget callIsLoaded = new InsnTarget();
        insn = insn.append(InsnUtils.aLoad(varStart, pool));
        insn = insn.append(Insn.create(opc_ifnonnull, callIsLoaded));
        insn = appendDirectReadReturn(insn, fieldRef);

        // call sm for isLoaded
        insn = insn.append(callIsLoaded);
        insn = insn.append(InsnUtils.aLoad(varStart, pool));

        // push instance
        insn = insn.append(Insn.create(opc_aload_0));

        // push absolute field index
        insn = insn.append(
            Insn.create(opc_getstatic,
                        pool.addFieldRef(
                            className,
                            JDO_PC_jdoInheritedFieldCount_Name,
                            JDO_PC_jdoInheritedFieldCount_Sig)));
        insn = insn.append(InsnUtils.integerConstant(fieldIndex, pool));
        insn = insn.append(Insn.create(opc_iadd));

        // test result of isLoaded and return field if nonzero
        final InsnTarget mediate = new InsnTarget();
        insn = insn.append(
            new InsnInterfaceInvoke(
                pool.addInterfaceMethodRef(
                    JDO_StateManager_Path,
                    JDO_SM_isLoaded_Name,
                    JDO_SM_isLoaded_Sig),
                countMethodArgWords(JDO_SM_isLoaded_Sig)));
        insn = insn.append(Insn.create(opc_ifeq, mediate));
        insn = appendDirectReadReturn(insn, fieldRef);

        // call sm for mediation
        insn = insn.append(mediate);
        insn = insn.append(InsnUtils.aLoad(varStart, pool));

        // push instance
        insn = insn.append(Insn.create(opc_aload_0));

        // push absolute field index
        insn = insn.append(
            Insn.create(opc_getstatic,
                        pool.addFieldRef(
                            className,
                            JDO_PC_jdoInheritedFieldCount_Name,
                            JDO_PC_jdoInheritedFieldCount_Sig)));
        insn = insn.append(InsnUtils.integerConstant(fieldIndex, pool));
        insn = insn.append(Insn.create(opc_iadd));

        // push field
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_getfield, fieldRef));

        // call the sm's get field method
        switch (sig.charAt(0)) {
        case 'Z':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_getBooleanField_Name,
                        JDO_SM_getBooleanField_Sig),
                    countMethodArgWords(JDO_SM_getBooleanField_Sig)));
            insn = insn.append(Insn.create(opc_ireturn));
            break;
        case 'C':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_getCharField_Name,
                        JDO_SM_getCharField_Sig),
                    countMethodArgWords(JDO_SM_getCharField_Sig)));
            insn = insn.append(Insn.create(opc_ireturn));
            break;
        case 'B':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_getByteField_Name,
                        JDO_SM_getByteField_Sig),
                    countMethodArgWords(JDO_SM_getByteField_Sig)));
            insn = insn.append(Insn.create(opc_ireturn));
            break;
        case 'S':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_getShortField_Name,
                        JDO_SM_getShortField_Sig),
                    countMethodArgWords(JDO_SM_getShortField_Sig)));
            insn = insn.append(Insn.create(opc_ireturn));
            break;
        case 'I':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_getIntField_Name,
                        JDO_SM_getIntField_Sig),
                    countMethodArgWords(JDO_SM_getIntField_Sig)));
            insn = insn.append(Insn.create(opc_ireturn));
            break;
        case 'J':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_getLongField_Name,
                        JDO_SM_getLongField_Sig),
                    countMethodArgWords(JDO_SM_getLongField_Sig)));
            insn = insn.append(Insn.create(opc_lreturn));
            break;
        case 'F':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_getFloatField_Name,
                        JDO_SM_getFloatField_Sig),
                    countMethodArgWords(JDO_SM_getFloatField_Sig)));
            insn = insn.append(Insn.create(opc_freturn));
            break;
        case 'D':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_getDoubleField_Name,
                        JDO_SM_getDoubleField_Sig),
                    countMethodArgWords(JDO_SM_getDoubleField_Sig)));
            insn = insn.append(Insn.create(opc_dreturn));
            break;
        case 'L':
        case '[':
            if (sig.equals(JAVA_String_Sig)) {
                insn = insn.append(
                    new InsnInterfaceInvoke(
                        pool.addInterfaceMethodRef(
                            JDO_StateManager_Path,
                            JDO_SM_getStringField_Name,
                            JDO_SM_getStringField_Sig),
                        countMethodArgWords(JDO_SM_getStringField_Sig)));
                insn = insn.append(Insn.create(opc_areturn));
            } else {
                insn = insn.append(
                    new InsnInterfaceInvoke(
                        pool.addInterfaceMethodRef(
                            JDO_StateManager_Path,
                            JDO_SM_getObjectField_Name,
                            JDO_SM_getObjectField_Sig),
                        countMethodArgWords(JDO_SM_getObjectField_Sig)));
                if (!sig.equals(JAVA_Object_Sig)) {
                    final String fieldType = NameHelper.pathForSig(sig);
                    insn = insn.append(
                        Insn.create(opc_checkcast,
                                    pool.addClass(fieldType)));
                }
                insn = insn.append(Insn.create(opc_areturn));
            }
            break;
        default:
            affirm(false, "Illegal field type: " + sig);
        }

        affirm(insn != null);
        return insn;
    }

    /**
     * Build an accessor method for mediated read access.
     *
     * static xxx final YYY jdoGetyyy(XXX instance)
     * {
     *     // augmentation: mediate read access
     *     final javax.jdo.StateManager sm = instance.jdoStateManager;
     *     if (sm == null) {
     *         return instance.yyy;
     *     }
     *     if (sm.isLoaded(instance, instance.jdoInheritedFieldCount + y)) {
     *         return instance.yyy;
     *     }
     *     return (YYY)sm.getYYYField(instance,
     *                                instance.jdoInheritedFieldCount + x,
     *                                instance.yyy);
     * }
     */
    public void addJDOMediatedReadAccessMethod(String methodName,
                                               String methodSig,
                                               int accessFlags,
                                               int fieldIndex)
    {
        affirm(methodName != null);      
        affirm(methodSig != null);      
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // get field's sig and compute first non-parameter slot
        final ConstFieldRef fieldRef = getAnnotatedFieldRefs()[fieldIndex];
        affirm(fieldRef != null);
        final String sig = fieldRef.nameAndType().signature().asString();
        affirm(sig != null && sig.length() > 0);
        final int fieldSize = ((sig.equals("J") || sig.equals("D")) ? 2 : 1);
        final int varStart = 1;

        // mediate access
        insn = appendMediatedReadAccess(insn, fieldIndex, fieldRef, varStart);

        // end of method body

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                fieldSize + 3, // maxStack
                                2, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());

        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    /**
     * Build an accessor method for checked read access.
     *
     * static xxx final YYY jdoGetyyy(XXX instance)
     * {
     *     // augmentation: check read access
     *     if (instance.jdoFlags <= 0) {
     *         return instance.yyy;
     *     }
     *     final javax.jdo.StateManager sm = instance.jdoStateManager;
     *     if (sm == null) {
     *         return instance.yyy;
     *     }
     *     if (sm.isLoaded(instance, instance.jdoInheritedFieldCount + y)) {
     *         return instance.yyy;
     *     }
     *     return (YYY)instance.jdoStateManager
     *         .getYYYField(instance,
     *                      instance.jdoInheritedFieldCount + y,
     *                      instance.yyy);
     * }
     */
    public void addJDOCheckedReadAccessMethod(String methodName,
                                              String methodSig,
                                              int accessFlags,
                                              int fieldIndex)
    {
        affirm(methodName != null);      
        affirm(methodSig != null);      
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // get field's sig and compute first non-parameter slot
        final ConstFieldRef fieldRef = getAnnotatedFieldRefs()[fieldIndex];
        affirm(fieldRef != null);
        final String sig = fieldRef.nameAndType().signature().asString();
        affirm(sig != null && sig.length() > 0);
        final int fieldSize = ((sig.equals("J") || sig.equals("D")) ? 2 : 1);
        final int varStart = 1;

        // directly return field if flags are <= LOAD_REQUIRED
        final InsnTarget mediate = new InsnTarget();
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(opc_getfield,
                        getjdoFlagsFieldRef()));
        insn = insn.append(Insn.create(opc_ifgt, mediate));
        insn = appendDirectReadReturn(insn, fieldRef);

        // mediate access
        insn = insn.append(mediate);
        insn = appendMediatedReadAccess(insn, fieldIndex, fieldRef, varStart);

        // end of method body

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                fieldSize + 3, // maxStack
                                2, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    /**
     * Append the code for assigning the argument to the field and return.
     */
    private Insn appendDirectWriteReturn(Insn insn,
                                         ConstFieldRef fieldRef)
    {
        affirm(insn != null);
        affirm(fieldRef != null);

        final String sig = fieldRef.nameAndType().signature().asString();
        affirm(sig != null && sig.length() > 0);

        // write argument to field and return
        insn = insn.append(Insn.create(opc_aload_0));
        switch (sig.charAt(0)) {
        case 'Z':
        case 'C':
        case 'B':
        case 'S':
        case 'I':
            insn = insn.append(Insn.create(opc_iload_1));
            break;
        case 'J':
            insn = insn.append(Insn.create(opc_lload_1));
            break;
        case 'F':
            insn = insn.append(Insn.create(opc_fload_1));
            break;
        case 'D':
            insn = insn.append(Insn.create(opc_dload_1));
            break;
        case 'L':
        case '[':
            insn = insn.append(Insn.create(opc_aload_1));
            break;
        default:
            affirm(false, "Illegal field type: " + sig);
        }
        insn = insn.append(Insn.create(opc_putfield, fieldRef));
        insn = insn.append(Insn.create(opc_return));

        affirm(insn != null);
        return insn;
    }

    /**
     * Build a mutator method for direct write access.
     *
     * static xxx void jdoSetyyy(XXX instance, YYY yyy)
     * {
     *     // augmentation: grant direct write access
     *     instance.yyy = yyy;
     * }
     */
    public void addJDODirectWriteAccessMethod(String methodName,
                                              String methodSig,
                                              int accessFlags,
                                              int fieldIndex)
    {
        affirm(methodName != null);      
        affirm(methodSig != null);      
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        final ConstFieldRef fieldRef = getAnnotatedFieldRefs()[fieldIndex];
        affirm(fieldRef != null);

        // write argument to field and return
        insn = appendDirectWriteReturn(insn, fieldRef);

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                3, // maxStack: allow for long/double
                                3, // maxLocals: allow for long/double
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    /**
     * Append the code for mediated write access.
     */
    private Insn appendMediatedWriteAccess(Insn insn,
                                           int fieldIndex,
                                           ConstFieldRef fieldRef,
                                           int varStart)
    {
        affirm(insn != null);
        affirm(fieldRef != null);

        final String sig = fieldRef.nameAndType().signature().asString();
        affirm(sig != null && sig.length() > 0);

        // store the sm field into local var
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(
                opc_getfield,
                getjdoStateManagerFieldRef()));
        insn = insn.append(InsnUtils.aStore(varStart, pool));

        // test the sm field and assign field if null
        final InsnTarget mediate = new InsnTarget();
        insn = insn.append(InsnUtils.aLoad(varStart, pool));
        insn = insn.append(Insn.create(opc_ifnonnull, mediate));

        // write argument to field and return
        insn = appendDirectWriteReturn(insn, fieldRef);

        // call sm for mediation
        insn = insn.append(mediate);
        insn = insn.append(InsnUtils.aLoad(varStart, pool));

        // push instance
        insn = insn.append(Insn.create(opc_aload_0));

        // push absolute field index
        insn = insn.append(
            Insn.create(opc_getstatic,
                        pool.addFieldRef(
                            className,
                            JDO_PC_jdoInheritedFieldCount_Name,
                            JDO_PC_jdoInheritedFieldCount_Sig)));
        insn = insn.append(InsnUtils.integerConstant(fieldIndex, pool));
        insn = insn.append(Insn.create(opc_iadd));

        // push field
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(Insn.create(opc_getfield, fieldRef));

        // push passed argument value
        switch (sig.charAt(0)) {
        case 'Z':
        case 'C':
        case 'B':
        case 'S':
        case 'I':
            insn = insn.append(Insn.create(opc_iload_1));
            break;
        case 'J':
            insn = insn.append(Insn.create(opc_lload_1));
            break;
        case 'F':
            insn = insn.append(Insn.create(opc_fload_1));
            break;
        case 'D':
            insn = insn.append(Insn.create(opc_dload_1));
            break;
        case 'L':
        case '[':
            insn = insn.append(Insn.create(opc_aload_1));
            break;
        default:
            affirm(false, "Illegal field type: " + sig);
        }

        // call the sm's set field method
        switch (sig.charAt(0)) {
        case 'Z':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_setBooleanField_Name,
                        JDO_SM_setBooleanField_Sig),
                    countMethodArgWords(JDO_SM_setBooleanField_Sig)));
            break;
        case 'C':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_setCharField_Name,
                        JDO_SM_setCharField_Sig),
                    countMethodArgWords(JDO_SM_setCharField_Sig)));
            break;
        case 'B':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_setByteField_Name,
                        JDO_SM_setByteField_Sig),
                    countMethodArgWords(JDO_SM_setByteField_Sig)));
            break;
        case 'S':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_setShortField_Name,
                        JDO_SM_setShortField_Sig),
                    countMethodArgWords(JDO_SM_setShortField_Sig)));
            break;
        case 'I':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_setIntField_Name,
                        JDO_SM_setIntField_Sig),
                    countMethodArgWords(JDO_SM_setIntField_Sig)));
            break;
        case 'J':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_setLongField_Name,
                        JDO_SM_setLongField_Sig),
                    countMethodArgWords(JDO_SM_setLongField_Sig)));
            break;
        case 'F':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_setFloatField_Name,
                        JDO_SM_setFloatField_Sig),
                    countMethodArgWords(JDO_SM_setFloatField_Sig)));
            break;
        case 'D':
            insn = insn.append(
                new InsnInterfaceInvoke(
                    pool.addInterfaceMethodRef(
                        JDO_StateManager_Path,
                        JDO_SM_setDoubleField_Name,
                        JDO_SM_setDoubleField_Sig),
                    countMethodArgWords(JDO_SM_setDoubleField_Sig)));
            break;
        case 'L':
        case '[':
            if (sig.equals(JAVA_String_Sig)) {
                insn = insn.append(
                    new InsnInterfaceInvoke(
                        pool.addInterfaceMethodRef(
                            JDO_StateManager_Path,
                            JDO_SM_setStringField_Name,
                            JDO_SM_setStringField_Sig),
                        countMethodArgWords(JDO_SM_setStringField_Sig)));
            } else {
                insn = insn.append(
                    new InsnInterfaceInvoke(
                        pool.addInterfaceMethodRef(
                            JDO_StateManager_Path,
                            JDO_SM_setObjectField_Name,
                            JDO_SM_setObjectField_Sig),
                        countMethodArgWords(JDO_SM_setObjectField_Sig)));
            }
            break;
        default:
            affirm(false, "Illegal field type: " + sig);
        }

        insn = insn.append(Insn.create(opc_return));

        affirm(insn != null);
        return insn;
    }

    /**
     * Build a mutator method for mediated write access.
     *
     * static xxx void jdoSetyyy(XXX instance, YYY yyy)
     * {
     *     // augmentation: mediate write access
     *     final javax.jdo.StateManager sm = instance.jdoStateManager;
     *     if (sm == null) {
     *         instance.yyy = yyy;
     *         return;
     *     }
     *     sm.setYYYField(instance,
     *                    instance.jdoInheritedFieldCount + y,
     *                    instance.yyy,
     *                    yyy);
     * }
     */
    public void addJDOMediatedWriteAccessMethod(String methodName,
                                                String methodSig,
                                                int accessFlags,
                                                int fieldIndex)
    {
        affirm(methodName != null);      
        affirm(methodSig != null);      
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // get field's sig and compute first non-parameter slot
        final ConstFieldRef fieldRef = getAnnotatedFieldRefs()[fieldIndex];
        affirm(fieldRef != null);
        final String sig = fieldRef.nameAndType().signature().asString();
        affirm(sig != null && sig.length() > 0);
        final int fieldSize = ((sig.equals("J") || sig.equals("D")) ? 2 : 1);
        final int varStart = fieldSize + 1;

        // mediate access
        insn = appendMediatedWriteAccess(insn, fieldIndex, fieldRef, varStart);

        // end of method body

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                (2 * fieldSize) + 3, // maxStack
                                fieldSize + 2, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    /**
     * Build a mutator method for checked write access.
     *
     * static xxx void jdoSetyyy(XXX instance, YYY yyy)
     * {
     *     // augmentation: check write access
     *     if (instance.jdoFlags == 0) {
     *         instance.yyy = yyy;
     *         return;
     *     }
     *     instance.yyy = (YYY)instance.jdoStateManager
     *         .setYYYField(instance,
     *                      instance.jdoInheritedFieldCount + y,
     *                      instance.yyy, yyy);
     * }
     */
    public void addJDOCheckedWriteAccessMethod(String methodName,
                                               String methodSig,
                                               int accessFlags,
                                               int fieldIndex)
    {
        affirm(methodName != null);      
        affirm(methodSig != null);      
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // get field's sig and compute first non-parameter slot
        final ConstFieldRef fieldRef = getAnnotatedFieldRefs()[fieldIndex];
        affirm(fieldRef != null);
        final String sig = fieldRef.nameAndType().signature().asString();
        affirm(sig != null && sig.length() > 0);
        final int fieldSize = ((sig.equals("J") || sig.equals("D")) ? 2 : 1);
        final int varStart = fieldSize + 1;

        // directly write argument and retrurn if flags are != READ_WRITE_OK
        final InsnTarget mediate = new InsnTarget();
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(opc_getfield,
                        getjdoFlagsFieldRef()));
        insn = insn.append(Insn.create(opc_ifne, mediate));
        insn = appendDirectWriteReturn(insn, fieldRef);

        // mediate access
        insn = insn.append(mediate);
        insn = appendMediatedWriteAccess(insn, fieldIndex, fieldRef, varStart);

        // end of method body

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                (2 * fieldSize) + 3, // maxStack
                                fieldSize + 2, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    // ----------------------------------------------------------------------

    /**
     * Build the jdoClear method for the class.
     *
     * public void jdoClear() {
     *     ...
     * }
     */
    public void addJDOClearMethod()
    {
        final String methodName = "";
        final String methodSig = "()V";
        final int accessFlags = 0;
        final ExceptionsAttribute exceptAttr = null;

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        //@olsen: disabled code
        if (false) {
            // reset jdoFlags = LOAD_REQUIRED
            insn = insn.append(Insn.create(opc_aload_0));
            insn = insn.append(Insn.create(opc_iconst_1));
            insn = insn.append(
                Insn.create(opc_putfield,
                            pool.addFieldRef(className,
                                             JDO_PC_jdoFlags_Name,
                                             JDO_PC_jdoFlags_Sig)));
        }

        // iterate over all declared fields of the class
        final ClassField[] managedFields = null; //analyzer.annotatedFields();
        final int managedFieldCount = managedFields.length;
        for (int i = 0; i < managedFieldCount; i++) {
            final ClassField field = managedFields[i];
            final String fieldName = field.name().asString();
            final String fieldSig = field.signature().asString();

/*
            // ignore primary managed fields
            if (field.isManaged())
                continue;
*/

/*
            //@olsen: disconnect mutable SCOs before clear
            if (field.isMutableSCO()) {
                // fetch field
                insn = insn.append(Insn.create(opc_aload_0));
                insn = insn.append(
                    Insn.create(opc_getfield,
                                pool.addFieldRef(
                                    className,
                                    fieldName,
                                    fieldSig)));

                // test whether instanceof SCO base type
                // skip disconnecting if == 0
                final ConstClass cc
                    = pool.addClass(JDO_SecondClassObjectBase_Path);
                InsnTarget disconnect = new InsnTarget();
                InsnTarget afterDisconnect = new InsnTarget();
                insn = insn.append(
                    Insn.create(opc_dup));
                insn = insn.append(
                    Insn.create(opc_instanceof,
                                cc));
                insn = insn.append(
                    Insn.create(opc_ifne,
                                disconnect));

                // pop field and skip disconnecting
                insn = insn.append(
                    Insn.create(opc_pop));
                insn = insn.append(
                    Insn.create(opc_goto, afterDisconnect));

                // disconnect SCO field's object
                insn = insn.append(disconnect);

                // cast to SCO base type
                insn = insn.append(
                    Insn.create(opc_checkcast,
                                cc));

                // call method: void unsetOwner();
                final int requiredStack = 1;
                insn = insn.append(
                    new InsnInterfaceInvoke(
                        pool.addInterfaceMethodRef(
                            JDO_SecondClassObjectBase_Path,
                            "unsetOwner",
                            "()V"),
                        requiredStack));

                insn = insn.append(afterDisconnect);
            }
*/

            // get this
            insn = insn.append(Insn.create(opc_aload_0));

            // use the getMethodReturn type to decide how to clear field
            switch (fieldSig.charAt(0)) {
            case 'D':
                insn = insn.append(Insn.create(opc_dconst_0));
                break;
            case 'F':
                insn = insn.append(Insn.create(opc_fconst_0));
                break;
            case 'J':
                insn = insn.append(Insn.create(opc_lconst_0));
                break;
            case 'Z':
            case 'C':
            case 'B':
            case 'S':
            case 'I':
                insn = insn.append(Insn.create(opc_iconst_0));
                break;
            case 'L':
            case '[':
                insn = insn.append(Insn.create(opc_aconst_null));
                break;
            default:
                throw new InternalError("Illegal field type: " + fieldSig);
            }

            // put default value to field
            insn = insn.append(
                Insn.create(opc_putfield,
                            pool.addFieldRef(className,
                                             fieldName,
                                             fieldSig)));
        }

        // end of method body
        insn = insn.append(Insn.create(opc_return));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                3, // maxStack
                                1, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }

    /**
     * Build the clone method for the class.
     */
    public void addJDOClone()
    {
        final String methodName = "";
        final String methodSig = "()Ljava/lang/Object;";
        final int accessFlags = 0;
        final ExceptionsAttribute exceptAttr
            = new ExceptionsAttribute(
                pool.addUtf8(ExceptionsAttribute.expectedAttrName),
                pool.addClass("java/lang/CloneNotSupportedException"));

        // begin of method body
        final InsnTarget begin = new InsnTarget();
        Insn insn = begin;

        // THISCLASS newObject = (THISCLASS) super.clone();
        final ConstClass superConstClass = classFile.superName();
        final ConstClass thisConstClass = classFile.className();
        affirm(thisConstClass != null);
        insn = insn.append(Insn.create(opc_aload_0));
        insn = insn.append(
            Insn.create(opc_invokespecial,
                        pool.addMethodRef(superConstClass.asString(),
                                          methodName,
                                          methodSig)));
        insn = insn.append(Insn.create(opc_checkcast, thisConstClass));

        // newObject.jdoStateManager = null;
        if (false)
        {
            insn = insn.append(Insn.create(opc_dup));
            insn = insn.append(Insn.create(opc_aconst_null));
            insn = insn.append(
                Insn.create(opc_putfield,
                            pool.addFieldRef(className,
                                             JDO_PC_jdoStateManager_Name,
                                             JDO_PC_jdoStateManager_Sig)));
        }

        // newObject.jdoFlags = 0;
        if (false)
        {
            insn = insn.append(Insn.create(opc_dup));
            insn = insn.append(Insn.create(opc_iconst_0));
            insn = insn.append(
                Insn.create(opc_putfield,
                            pool.addFieldRef(className,
                                             JDO_PC_jdoFlags_Name,
                                             JDO_PC_jdoFlags_Sig)));
        }

        // return newObject;

        // end of method body
        insn = insn.append(Insn.create(opc_areturn));

        final CodeAttribute codeAttr
            = new CodeAttribute(getCodeAttributeUtf8(),
                                1, //3, //3, // maxStack
                                1, //2, // maxLocals
                                begin,
                                new ExceptionTable(),
                                new AttributeVector());
        augmenter.addMethod(methodName, methodSig, accessFlags,
                            codeAttr, exceptAttr);
    }
}
