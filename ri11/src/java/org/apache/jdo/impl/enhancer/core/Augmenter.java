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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import org.apache.jdo.impl.enhancer.classfile.AttributeVector;
import org.apache.jdo.impl.enhancer.classfile.ClassField;
import org.apache.jdo.impl.enhancer.classfile.ClassFile;
import org.apache.jdo.impl.enhancer.classfile.ClassMethod;
import org.apache.jdo.impl.enhancer.classfile.CodeAttribute;
import org.apache.jdo.impl.enhancer.classfile.ConstClass;
import org.apache.jdo.impl.enhancer.classfile.ConstantPool;
import org.apache.jdo.impl.enhancer.classfile.Descriptor;
import org.apache.jdo.impl.enhancer.classfile.ExceptionsAttribute;
import org.apache.jdo.impl.enhancer.classfile.Insn;
import org.apache.jdo.impl.enhancer.classfile.InsnTarget;
import org.apache.jdo.impl.enhancer.classfile.LineNumberTableAttribute;
import org.apache.jdo.impl.enhancer.classfile.SyntheticAttribute;
import org.apache.jdo.impl.enhancer.classfile.VMConstants;
import org.apache.jdo.impl.enhancer.meta.EnhancerMetaData;
import org.apache.jdo.impl.enhancer.util.Support;




/**
 * Handles the augmentation actions for a class.
 */
final class Augmenter
    extends Support
    implements JDOConstants
{
    //@olsen: fix for bug 4467428:
    // Debugging under jdk 1.3.1 shows the problem that any breakpoints
    // in PC classes are ignored if the added jdo methods do NOT have a
    // non-empty line number table attribute, no matter whether the
    // 'Synthetic' attribute is given or not.  However, this doesn't
    // seem to comply with the JVM Spec (2nd edition), which states
    // that the synthetic attribute _must_ be specified if no source
    // code information is available for the member:
    //
    //     4.7.6 The Synthetic Attribute
    //     ... A class member that does not appear in the source code must
    //     be marked using a Synthetic attribute. ...
    //
    //     4.7.8 The LineNumberTable Attribute
    //     The LineNumberTable attribute is an optional variable-length
    //     attribute in the attributes table of a Code (§4.7.3)
    //     attribute. It may be used by debuggers to determine which
    //     part of the Java virtual machine code array corresponds to a
    //     given line number in the original source file. ... Furthermore,
    //     multiple LineNumberTable attributes may together represent a
    //     given line of a source file; that is, LineNumberTable attributes
    //     need not be one-to-one with source lines.
    //
    // Unfortunately, if we do both, adding the synthetic attribute and
    // a (dummy) line number table on generated methods, jdk's 1.3.1 javap
    // fails to disassemble the classfile with an exception:
    //
    //     sun.tools.java.CompilerError: checkOverride() synthetic
    //
    // So, to workaround these problems and to allow for both, debugging
    // and disassembling with the jdk (1.3.1) tools, we pretend that the
    // generated jdo methods have source code equivalents by
    // - not adding the synthetic code attribute
    // - providing a dummy line number table code attribute
    static private final boolean addSyntheticAttr = false;
    static private final boolean addLineNumberTableAttr = true;

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
     * The method builder helper object.
     */
    private final Builder builder;

    // public accessors

    /**
     * Constructor
     */
    public Augmenter(Controller control,
                     Analyzer analyzer,
                     Environment env)
    {
        affirm(control != null);
        affirm(analyzer != null);
        affirm(env != null);

        this.control = control;
        this.analyzer = analyzer;
        this.env = env;
        this.classFile = control.getClassFile();
        this.className = classFile.classNameString();
        this.userClassName = classFile.userClassName();
        this.pool = classFile.pool();
        this.builder = new Builder(analyzer, this, env);

        affirm(classFile != null);
        affirm(className != null);
        affirm(userClassName != null);
        affirm(pool != null);
        affirm(builder != null);
    }

    // ----------------------------------------------------------------------

    //^olsen: check public access modifier
    
    /**
     * Adds the augmentation to the class.
     */
    public void augment()
    {
        affirm(analyzer.isAugmentable() && !env.noAugment());
        env.message("augmenting class " + userClassName);

        if (analyzer.isAugmentableAsRoot()) {
            augmentGenericJDOFields();
            augmentGenericJDOMethods();
        }
        augmentClassInterface(JDO_PersistenceCapable_Path);
        augmentSpecificJDOFields();
        augmentSpecificJDOMethods();
        augmentJDOAccessorMutatorMethods();
        augmentSerializableSupportMethods();
    }

    /**
     * Adds the specified interface to the implements clause of the class.
     */
    private void augmentClassInterface(String interfaceName)
    {
        env.message("adding: implements "
                    + ClassFile.userClassFromVMClass(interfaceName));

        final ConstClass iface = pool.addClass(interfaceName);
        classFile.addInterface(iface);

        // notify controller of class change
        control.noteUpdate();
    }

    /**
     * Adds the generic JDO fields to the class.
     */
    public void augmentGenericJDOFields()
    {
        //protected transient javax.jdo.StateManager jdoStateManager
        addField(
            JDO_PC_jdoStateManager_Name,
            JDO_PC_jdoStateManager_Sig,
            JDO_PC_jdoStateManager_Mods);        

        //protected transient byte jdoFlags
        addField(
            JDO_PC_jdoFlags_Name,
            JDO_PC_jdoFlags_Sig,
            JDO_PC_jdoFlags_Mods);
    }

    /**
     * Adds the specific JDO fields to the class.
     */
    public void augmentSpecificJDOFields()
    {
        //private static final int jdoInheritedFieldCount
        addField(
            JDO_PC_jdoInheritedFieldCount_Name,
            JDO_PC_jdoInheritedFieldCount_Sig,
            JDO_PC_jdoInheritedFieldCount_Mods);

        //private static final String[] jdoFieldNames
        addField(
            JDO_PC_jdoFieldNames_Name,
            JDO_PC_jdoFieldNames_Sig,
            JDO_PC_jdoFieldNames_Mods);

        //private static final Class[] jdoFieldTypes
        addField(
            JDO_PC_jdoFieldTypes_Name,
            JDO_PC_jdoFieldTypes_Sig,
            JDO_PC_jdoFieldTypes_Mods);

        //private static final byte[] jdoFieldFlags
        addField(
            JDO_PC_jdoFieldFlags_Name,
            JDO_PC_jdoFieldFlags_Sig,
            JDO_PC_jdoFieldFlags_Mods);

        //private static final Class jdoPersistenceCapableSuperclass
        addField(
            JDO_PC_jdoPersistenceCapableSuperclass_Name,
            JDO_PC_jdoPersistenceCapableSuperclass_Sig,
            JDO_PC_jdoPersistenceCapableSuperclass_Mods);
    }

    /**
     * Adds a field to the class.
     */
    private void addField(String fieldName,
                          String fieldSig,
                          int accessFlags)
    {
        affirm(fieldName != null);
        affirm(fieldSig != null);

        env.message("adding: "
                    + Descriptor.userFieldSig(fieldSig)
                    + " " + fieldName);

        //@olsen: fix 4467428, add synthetic attribute for generated fields
        final AttributeVector fieldAttrs = new AttributeVector();
        fieldAttrs.addElement(
            new SyntheticAttribute(
                pool.addUtf8(SyntheticAttribute.expectedAttrName)));

        // create and add the field
        final ClassField field
            = new ClassField(accessFlags,
                             pool.addUtf8(fieldName),
                             pool.addUtf8(fieldSig),
                             fieldAttrs);
        affirm(classFile.findField(fieldName) == null,
               "Attempt to add a repeated field.");
        classFile.addField(field);

        // notify controller of class change
        control.noteUpdate();
    }

    /**
     * Adds the generic JDO methods to the class.
     */
    public void augmentGenericJDOMethods()
    {
        builder.addJDOReplaceFlags();
        builder.addJDOIsPersistentMethod();
        builder.addJDOIsTransactionalMethod();
        builder.addJDOIsNewMethod();
        builder.addJDOIsDeletedMethod();
        builder.addJDOIsDirtyMethod();        
        builder.addJDOMakeDirtyMethod();
        builder.addJDOPreSerializeMethod();
        builder.addJDOGetPersistenceManagerMethod();
        builder.addJDOGetObjectIdMethod();
        builder.addJDOGetTransactionalObjectIdMethod();
        builder.addJDOReplaceStateManager();
        builder.addJDOProvideFieldsMethod();
        builder.addJDOReplaceFieldsMethod();

        builder.addSunJDOClassForNameMethod();

/*
        if (!hasCloneMethod) {
            classFile.addMethod(
                builder.makeJDOClone(
                    this,
                    JAVA_Object_clone_Name));
        }
*/
    }

    /**
     * Adds the specific JDO methods to the class.
     */
    public void augmentSpecificJDOMethods()
    {
        // class registration
        builder.addJDOGetManagedFieldCountMethod();
        builder.addStaticInitialization();

        // instantiation methods
        builder.addJDONewInstanceMethod();
        builder.addJDONewInstanceOidMethod();
        
        // field handling methods
        builder.addJDOProvideFieldMethod();
        builder.addJDOReplaceFieldMethod();
        builder.addJDOCopyFieldMethod();
        builder.addJDOCopyFieldsMethod();

        // key handling methods
        if (analyzer.isAugmentableAsRoot()
            || analyzer.getKeyClassName() != null) {
            builder.addJDONewObjectIdInstanceMethod();
            builder.addJDONewObjectIdInstanceStringMethod();
            builder.addJDOCopyKeyFieldsToObjectIdMethod();
            builder.addJDOCopyKeyFieldsFromObjectIdMethod();
            builder.addJDOCopyKeyFieldsToObjectIdOIFSMethod();
            builder.addJDOCopyKeyFieldsFromObjectIdOIFCMethod();
        }

/*
        builder.addNullMethod(JDO_PC_jdoProvideField_Name,
                              JDO_PC_jdoProvideField_Sig,
                              JDO_PC_jdoProvideField_Mods);
        builder.addNullMethod(JDO_PC_jdoReplaceField_Name,
                              JDO_PC_jdoReplaceField_Sig,
                              JDO_PC_jdoReplaceField_Mods);
*/
    }

    /**
     * Adds the JDO accessor+mutator method for a field.
     */
    public void augmentJDOAccessorMutatorMethod(String fieldName,
                                                String fieldSig,
                                                int fieldMods,
                                                int fieldFlags,
                                                int index)
    {
        affirm(fieldName != null);
        affirm(fieldSig != null);
        affirm((fieldMods & ACCStatic) == 0);
        affirm((fieldFlags & CHECK_READ) == 0
               | (fieldFlags & MEDIATE_READ) == 0);
        affirm((fieldFlags & CHECK_WRITE) == 0
               | (fieldFlags & MEDIATE_WRITE) == 0);

        // these combinations are not supported by JDO
        affirm((fieldFlags & CHECK_READ) == 0
               | (fieldFlags & MEDIATE_WRITE) == 0);
        affirm((fieldFlags & CHECK_WRITE) == 0
               | (fieldFlags & MEDIATE_READ) == 0);

        // add accessor
        final String aName
            = JDONameHelper.getJDO_PC_jdoAccessor_Name(fieldName);
        final String aSig
            = JDONameHelper.getJDO_PC_jdoAccessor_Sig(className, fieldSig);
        final int aMods
            = JDONameHelper.getJDO_PC_jdoAccessor_Mods(fieldMods);
        if ((fieldFlags & CHECK_READ) != 0) {
            builder.addJDOCheckedReadAccessMethod(aName, aSig, aMods, index);
        } else if ((fieldFlags & MEDIATE_READ) != 0) {
            builder.addJDOMediatedReadAccessMethod(aName, aSig, aMods, index);
        } else {
            builder.addJDODirectReadAccessMethod(aName, aSig, aMods, index);
        }

        // add mutator
        final String mName
            = JDONameHelper.getJDO_PC_jdoMutator_Name(fieldName);
        final String mSig
            = JDONameHelper.getJDO_PC_jdoMutator_Sig(className, fieldSig);
        final int mMods
            = JDONameHelper.getJDO_PC_jdoMutator_Mods(fieldMods);
        if ((fieldFlags & CHECK_WRITE) != 0) {
            builder.addJDOCheckedWriteAccessMethod(mName, mSig, mMods, index);
        } else if ((fieldFlags & MEDIATE_WRITE) != 0) {
            builder.addJDOMediatedWriteAccessMethod(mName, mSig, mMods, index);
        } else {
            builder.addJDODirectWriteAccessMethod(mName, mSig, mMods, index);
        }
    }
    
    /**
     * Adds the JDO accessor+mutator methods to the class.
     */
    public void augmentJDOAccessorMutatorMethods()
    {
        final int annotatedFieldCount = analyzer.getAnnotatedFieldCount();
        final String[] annotatedFieldNames = analyzer.getAnnotatedFieldNames();
        final String[] annotatedFieldSigs = analyzer.getAnnotatedFieldSigs();
        final int[] annotatedFieldMods = analyzer.getAnnotatedFieldMods();
        final int[] annotatedFieldFlags = analyzer.getAnnotatedFieldFlags();
        affirm(annotatedFieldNames.length == annotatedFieldCount);
        affirm(annotatedFieldSigs.length == annotatedFieldCount);
        affirm(annotatedFieldMods.length == annotatedFieldCount);
        affirm(annotatedFieldFlags.length == annotatedFieldCount);

        for (int i = 0; i < annotatedFieldCount; i++) {
            augmentJDOAccessorMutatorMethod(annotatedFieldNames[i],
                                            annotatedFieldSigs[i],
                                            annotatedFieldMods[i],
                                            annotatedFieldFlags[i], i);
        }
    }

    /**
     *
     */
    public void augmentSerializableSupportMethods()
    {
        final EnhancerMetaData meta = env.getEnhancerMetaData();
        final String pcSuperClassName = analyzer.getPCSuperClassName();
        
        // Add serializable support, if 
        // - this class implements Serializable and
        // - the pc superclass (if available) does NOT implement Serializable
        if (meta.isSerializableClass(className) &&
            (pcSuperClassName == null || 
             !meta.isSerializableClass(pcSuperClassName))) {
            // add writeObject if this class does not provide method writeObject and 
            // does not provide method writeReplace
            if (!analyzer.hasWriteObjectMethod() && 
                !analyzer.hasWriteReplaceMethod()) {
                builder.addWriteObjectMethod();
            }
            else {
                if (analyzer.hasWriteObjectMethod()) {
                    // add call of jdoPreSerialize to writeObject
                    builder.addJDOPreSerializeCall(
                        JAVA_Object_writeObject_Name, 
                        JAVA_Object_writeObject_Sig);
                }
                if (analyzer.hasWriteReplaceMethod()) {
                    // add call of jdoPreSerialize to writeReplace
                    builder.addJDOPreSerializeCall(
                        JAVA_Object_writeReplace_Name, 
                        JAVA_Object_writeReplace_Sig);
                }
            }
        }
    }

    /**
     * Adds a method to the class.
     */
    void addMethod(String methodName,
                   String methodSig,
                   int accessFlags,
                   CodeAttribute codeAttr,
                   ExceptionsAttribute exceptAttr)
    {
        affirm(methodName != null);
        affirm(methodSig != null);
        affirm(codeAttr != null);

        env.message("adding: "
                    + Descriptor.userMethodResult(methodSig)
                    + " " + methodName
                    + Descriptor.userMethodArgs(methodSig));

        //@olsen: fix 4467428, add dummy, non-empty line number table
        if (addLineNumberTableAttr) {
            // get first instruction which always is an instruction target
            affirm(codeAttr.theCode().opcode() == Insn.opc_target);
            final InsnTarget begin = (InsnTarget)codeAttr.theCode();

            // get attributes of the code attribute
            final AttributeVector codeSpecificAttrs = codeAttr.attributes();
            affirm(codeSpecificAttrs != null);
            
            // add dummy line number attribute with first instruction
            codeSpecificAttrs.addElement(
                new LineNumberTableAttribute(
                    pool.addUtf8(LineNumberTableAttribute.expectedAttrName),
                    new short[]{ 0 }, new InsnTarget[]{ begin }));
        }

        // add the method's code and exception attributes
        final AttributeVector methodAttrs = new AttributeVector();
        methodAttrs.addElement(codeAttr);
        if (exceptAttr != null) {
            methodAttrs.addElement(exceptAttr);
        }
        
        //@olsen: fix 4467428, add synthetic attribute for generated methods
        if (addSyntheticAttr) {
            methodAttrs.addElement(
                new SyntheticAttribute(
                    pool.addUtf8(SyntheticAttribute.expectedAttrName)));
        }
        
        // create and add the method
        final ClassMethod method
            = new ClassMethod(accessFlags,
                              pool.addUtf8(methodName),
                              pool.addUtf8(methodSig),
                              methodAttrs);
        affirm(classFile.findMethod(methodName, methodSig) == null,
               "Attempt to add a repeated method.");
        classFile.addMethod(method);

        // notify controller of class change
        control.noteUpdate();
    }

    /**
     * Extends an exisiting method by prepending code.
     */
    void prependMethod(String methodName,
                       String methodSig,
                       CodeAttribute codeAttr,
                       ExceptionsAttribute exceptAttr)
    {
        affirm(methodName != null);
        affirm(methodSig != null);
        affirm(codeAttr != null);

        env.message("extending: "
                    + Descriptor.userMethodResult(methodSig)
                    + " " + methodName
                    + Descriptor.userMethodArgs(methodSig));

        // get method
        final ClassMethod method = classFile.findMethod(methodName, methodSig);
        affirm(method != null,
               "Attempt to add code to a non-existing method.");

        // check the found method
        affirm(!method.isAbstract(),
               "Attempt to add code to an abstract method.");
        affirm(!method.isNative(),
               "Attempt to add code to a native method.");
        final CodeAttribute foundCodeAttr = method.codeAttribute();
        affirm(foundCodeAttr != null);  // by JVM spec

        // prepend the new code to the current one
        final Insn firstInsn = codeAttr.theCode();
        affirm(firstInsn != null);
        final Insn foundFirstInsn = foundCodeAttr.theCode();
        affirm(foundFirstInsn != null);
        final Insn lastInsn = firstInsn.append(foundFirstInsn);
        affirm(lastInsn != null);
        foundCodeAttr.setTheCode(firstInsn);

        // ajust the method's stack and locals demand
        foundCodeAttr.setStackUsed(max(foundCodeAttr.stackUsed(),
                                       codeAttr.stackUsed()));
        foundCodeAttr.setLocalsUsed(max(foundCodeAttr.localsUsed(),
                                        codeAttr.localsUsed()));

        // add the exception attribute or its exceptions
        if (exceptAttr != null) {
            affirm((exceptAttr.getExceptions().size()
                    == new HashSet(exceptAttr.getExceptions()).size()),
                   "Exception attribute contains duplicate exceptions.");
            
            final ExceptionsAttribute foundExceptAttr
                = method.exceptionsAttribute();
            if (foundExceptAttr == null) {
                // add the exception attribute
                final AttributeVector methodAttrs = method.attributes();
                affirm(methodAttrs != null);
                methodAttrs.addElement(exceptAttr);
            } else {
                // add those exceptions not already present
                final List foundEx = foundExceptAttr.getExceptions();
                final List newEx = exceptAttr.getExceptions();
                newEx.removeAll(foundEx);
                foundEx.addAll(newEx);
            }
        }
        
        // notify controller of class change
        control.noteUpdate();
    }

    static private int max(int i, int j) 
    {
        return (i < j) ? j : i;
    }
}
