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

package org.apache.jdo.impl.enhancer.meta;


/**
 * Provides the JDO meta information neccessary for byte-code enhancement.
 * <p>
 * <b>Please note: This interface deals with fully qualified names in the
 * JVM notation, that is, with '/' as package separator character&nbsp;
 * (instead of '.').</b>
 */
public interface EnhancerMetaData
{
    /**
     * The JDO field flags.
     */
    int CHECK_READ    = 0x01;
    int MEDIATE_READ  = 0x02;
    int CHECK_WRITE   = 0x04;
    int MEDIATE_WRITE = 0x08;
    int SERIALIZABLE  = 0x10;

    // ----------------------------------------------------------------------
    // Class Metadata
    // ----------------------------------------------------------------------

    /**
     * Returns whether a class is not to be modified by the enhancer.
     * <P>
     * It is an error if an unenhanceable class is persistence-capable
     * (or persistence-aware).  The following holds:
     *   isKnownUnenhancableClass(classPath)
     *       ==> !isPersistenceCapableClass(classPath)
     * @param classPath the non-null JVM-qualified name of the class
     * @return true if this class is known to be unmodifiable; otherwise false
     * @see #isPersistenceCapableClass(String)
     */
    boolean isKnownUnenhancableClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns whether a class is persistence-capable.
     * <P>
     * If a persistence-capable class is also known to be unenhancable,
     * an exception is thrown.
     * The following holds:
     *   isPersistenceCapableClass(classPath)
     *       ==> !isKnownUnenhancableClass(classPath)
     * @param classPath the non-null JVM-qualified name of the class
     * @return true if this class is persistence-capable; otherwise false
     * @see #isKnownUnenhancableClass(String)
     */
    boolean isPersistenceCapableClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns whether a class implements java.io.Serializable.
     * @param classPath the non-null JVM-qualified name of the class
     * @return true if this class is serializable; otherwise false
     */
    boolean isSerializableClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns the name of the persistence-capable superclass of a class.
     * <P>
     * The following holds:
     *   (String s = getPersistenceCapableSuperClass(classPath)) != null
     *       ==> isPersistenceCapableClass(classPath)
     *           && !isPersistenceCapableRootClass(classPath)
     * @param classPath the non-null JVM-qualified name of the class
     * @return the name of the PC superclass or null if there is none
     * @see #isPersistenceCapableClass(String)
     * @see #getPersistenceCapableRootClass(String)
     */
    String getPersistenceCapableSuperClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns the name of the key class of a class.
     * <P>
     * The following holds:
     *   (String s = getKeyClass(classPath)) != null
     *       ==> !isPersistenceCapableClass(s)
     *           && isPersistenceCapableClass(classPath)
     * @param classPath the non-null JVM-qualified name of the class
     * @return the name of the key class or null if there is none
     * @see #isPersistenceCapableClass(String)
     */
    String getKeyClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns an array of field names of all declared persistent and
     * transactional fields of a class.
     * <P>
     * The position of the field names in the result array corresponds
     * to their unique field index as returned by getFieldNumber such that
     * these equations hold:
     * <P> getFieldNumber(getManagedFields(classPath)[i]) == i
     * <P> getManagedFields(classPath)[getFieldNumber(fieldName)] == fieldName
     * <P>
     * This method requires all fields having been declared by
     * declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @return an array of all declared persistent and transactional
     *         fields of a class
     * @see #getFieldNumber(String, String)
     * @see #declareField(String, String, String)
     */
    String[] getManagedFields(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    // ----------------------------------------------------------------------
    // Field Metadata
    // ----------------------------------------------------------------------

    /** 
     * Returns the JVM-qualified name of the specified field's declaring
     * class. The method first checks whether the class of the specified
     * classPath (the JVM-qualified name) declares such a field. If yes,
     * classPath is returned. Otherwise, it checks its superclasses. The
     * method returns <code>null</code> for an unkown field.
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldName the non-null name of the field
     * @return the JVM-qualified name of the declararing class of the
     * field, or <code>null</code> if there is no such field.
     */
    String getDeclaringClass(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError; 

    /**
     * Declares a field to the JDO model passing its type information.
     * <P>
     * By the new JDO model, it's a requirement to declare fields to
     * the model for their type information before any field information
     * based on persistence-modifiers can be retrieved.  This method
     * passes a field's type information to the underlying JDO model.
     * <P>
     * There's one important exception: The method isKnownNonManagedField()
     * may be called at any time.
     * <P>
     * The class must be persistence-capable, otherwise an exception
     * is thrown.
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldName the non-null name of the field
     * @param fieldSig the non-null JVM signature of the field
     * @see #isPersistenceCapableClass(String)
     */
    void declareField(String classPath, String fieldName, String fieldSig)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns whether a field of a class is known to be non-managed.
     * <P>
     * This method differs from isManagedField() in that a field may or
     * may not be managed if its not known as non-managed.
     * The following holds (not vice versa!):
     *   isKnownNonManagedField(classPath, fieldName, fieldSig)
     *       ==> !isManagedField(classPath, fieldName)
     * <P>
     * This method doesn't require the field having been declared by
     * declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldName the non-null name of the field
     * @param fieldSig the non-null type signature of the field
     * @return true if this field is known to be non-managed; otherwise false
     * @see #isManagedField(String, String)
     * @see #declareField(String, String, String)
     */
    boolean isKnownNonManagedField(String classPath,
                                   String fieldName,
                                   String fieldSig)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns whether a field of a class is transient transactional
     * or persistent.
     * <P>
     * A managed field must not be known as non-managed and must be either
     * transient transactional or persistent.  The following holds:
     *   isManagedField(classPath, fieldName)
     *       ==> !isKnownNonManagedField(classPath, fieldName, fieldSig)
     *           && (isPersistentField(classPath, fieldName)
     *               ^ isTransactionalField(classPath, fieldName))
     * <P>
     * This method requires the field having been declared by declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldName the non-null name of the field
     * @return true if this field is managed; otherwise false
     * @see #isKnownNonManagedField(String, String, String)
     * @see #isPersistentField(String, String)
     * @see #isTransactionalField(String, String)
     * @see #isPersistenceCapableClass(String)
     */
    boolean isManagedField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns whether a field of a class is transient
     * transactional.
     * <P>
     * A transient transactional field cannot be persistent.
     * The following holds:
     *   isTransactionalField(classPath, fieldName)
     *       ==> isManagedField(classPath, fieldName)
     *           && !isPersistentField(classPath, fieldName)
     * <P>
     * This method requires the field having been declared by declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldName the non-null name of the field
     * @return true if this field is transactional; otherwise false
     * @see #isManagedField(String, String)
     * @see #declareField(String, String, String)
     */
    boolean isTransactionalField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns whether a field of a class is persistent.
     * <P>
     * A persistent field cannot be transient transactional.
     * The following holds:
     *   isPersistentField(classPath, fieldName)
     *       ==> isManagedField(classPath, fieldName)
     *           && !isTransactionalField(classPath, fieldName)
     * <P>
     * This method requires the field having been declared by declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldName the non-null name of the field
     * @return true if this field is persistent; otherwise false
     * @see #isManagedField(String, String)
     * @see #declareField(String, String, String)
     */
    boolean isPersistentField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns whether a field of a class is key.
     * <P>
     * A key field must be persistent.
     * The following holds:
     *   isKeyField(classPath, fieldName)
     *       ==> isPersistentField(classPath, fieldName)
     *           && !isDefaultFetchGroupField(classPath, fieldName)
     * <P>
     * This method requires the field having been declared by declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldName the non-null name of the field
     * @return true if this field is key; otherwise false
     * @see #isPersistentField(String, String)
     * @see #declareField(String, String, String)
     */
    boolean isKeyField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns whether a field of a class is part of the
     * default fetch group.
     * <P>
     * A field in the default fetch group must be persistent.
     * The following holds:
     *   isDefaultFetchGroupField(classPath, fieldName)
     *       ==> isPersistentField(classPath, fieldName)
     *           && !isKeyField(classPath, fieldName)
     * <P>
     * This method requires the field having been declared by declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldName the non-null name of the field
     * @return true if this field is part of the
     *         default fetch group; otherwise false
     * @see #isPersistentField(String, String)
     * @see #declareField(String, String, String)
     */
    boolean isDefaultFetchGroupField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns the unique field index of a declared, managed field of a
     * class.
     * <P>
     * The following holds:
     *   int i = getFieldFlags(classPath, fieldName);
     *   i > 0  ==>  getManagedFields(classPath)[i] == fieldName
     * <P>
     * This method requires the field having been declared by declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldName the non-null name of the field
     * @return the non-negative, unique field index or -1 if the field
     *         is non-managed
     * @see #getManagedFields(String)
     * @see #declareField(String, String, String)
     */
    int getFieldNumber(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns the field flags for a declared field of a class.
     * <P>
     * The following holds for the field flags:
     *   int f = getFieldFlags(classPath, fieldName);
     *
     *   !isManagedField(classPath, fieldName)
     *       ==> (f & CHECK_READ == 0) && (f & MEDIATE_READ == 0) &&
     *           (f & CHECK_WRITE == 0) && (f & MEDIATE_WRITE == 0)
     *
     *   isTransactionalField(classPath, fieldName)
     *       ==> (f & CHECK_READ == 0) && (f & MEDIATE_READ == 0) &&
     *           (f & CHECK_WRITE != 0) && (f & MEDIATE_WRITE == 0)
     *
     *   isKeyField(classPath, fieldName)
     *       ==> (f & CHECK_READ == 0) && (f & MEDIATE_READ == 0) &&
     *           (f & CHECK_WRITE == 0) && (f & MEDIATE_WRITE != 0)
     *
     *   isDefaultFetchGroupField(classPath, fieldName)
     *       ==> (f & CHECK_READ != 0) && (f & MEDIATE_READ != 0) &&
     *           (f & CHECK_WRITE == 0) && (f & MEDIATE_WRITE == 0)
     *
     *   isPersistentField(classPath, fieldName)
     *   && isKeyField(classPath, fieldName)
     *   && isDefaultFetchGroupField(classPath, fieldName)
     *       ==> (f & CHECK_READ == 0) && (f & MEDIATE_READ == 0) &&
     *           (f & CHECK_WRITE != 0) && (f & MEDIATE_WRITE != 0)
     * <P>
     * This method requires the field having been declared by declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldName the non-null name of the field
     * @return the field flags for this field
     * @see #declareField(String, String, String)
     */
    int getFieldFlags(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    // ----------------------------------------------------------------------
    // Convenience Methods
    // ----------------------------------------------------------------------

    /**
     * Returns whether a class is persistence-capable root class.
     * <P>
     * The following holds:
     *   isPersistenceCapableRootClass(classPath)
     *     <==> isPersistenceCapableClass(classPath)
     *          && getPersistenceCapableSuperClass(classPath) == null
     * @param classPath the non-null JVM-qualified name of the class
     * @return true if this class is persistence-capable and does not
     *         derive from another persistence-capable class; otherwise false
     */
    boolean isPersistenceCapableRootClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns the name of the persistence-capable root class of a class.
     * <P>
     * The following holds:
     *   (String s = getPersistenceCapableRootClass(classPath)) != null
     *       ==> isPersistenceCapableClass(classPath)
     *           && getPersistenceCapableSuperClass(classPath) == null
     * @param classPath the non-null JVM-qualified name of the class
     * @return the name of the least-derived persistence-capable class that
     *         is equal to or a super class of the argument class; if the
     *         argument class is not persistence-capable, null is returned.
     */
    String getPersistenceCapableRootClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns the name of the key class of the next persistence-capable
     * superclass that defines one.
     * <P>
     * The following holds:
     *   (String s = getSuperKeyClass(classPath)) != null
     *       ==> !isPersistenceCapableClass(s)
     *           && isPersistenceCapableClass(classPath)
     *           && !isPersistenceCapableRootClass(classPath)
     * @param classPath the non-null JVM-qualified name of the class
     * @return the name of the key class or null if there is none
     * @see #getKeyClass(String)
     * @see #getPersistenceCapableSuperClass(String)
     */
    String getSuperKeyClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns an array of field names of all key fields of a class.
     * <P>
     * This method requires all fields having been declared by declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @return an array of all declared key fields of a class
     * @see #declareField(String, String, String)
     */
    String[] getKeyFields(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns the unique field index of some declared, managed fields of a
     * class.
     * <P>
     * This method requires all fields having been declared by declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldNames the non-null array of names of the declared fields
     * @return the non-negative, unique field indices
     * @see #declareField(String, String, String)
     */
    int[] getFieldNumber(String classPath, String[] fieldNames)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns the field flags for some declared, managed fields of a class.
     * <P>
     * This method requires all fields having been declared by declareField().
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldNames the non-null array of names of the declared fields
     * @return the field flags for the fields
     * @see #declareField(String, String, String)
     */
    int[] getFieldFlags(String classPath, String[] fieldNames)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;
}
