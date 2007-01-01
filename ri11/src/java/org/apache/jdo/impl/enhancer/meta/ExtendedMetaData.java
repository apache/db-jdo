/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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
 * Provides extended JDO meta information for byte-code enhancement.
 */
public interface ExtendedMetaData
       extends EnhancerMetaData
{
    /**
     *  Gets all known classnames.
     *
     *  @return  All known classnames.
     */
    String[] getKnownClasses()
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;    

    /**
     *  Gets the modifiers of a class. The return value is a constant of the
     *  <code>java.lang.reflect.Modifier</code> class.
     *
     *  @param  classname  The classname.
     *  @return  The modifiers.
     *  @see  java.lang.reflect.Modifier
     */
    int getClassModifiers(String classname)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     * Returns the name of the superclass of a class.
     * <P>
     * @param classPath the JVM-qualified name of the class 
     * @return the name of the superclass or null if there is none
     */
    String getSuperClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     *  Gets all known fieldnames of a class.
     *
     *  @param  classname  The classname.
     *  @return  All known fieldnames.
     */
    String[] getKnownFields(String classname)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     *  Gets the type of a field.
     *
     *  @param  classname  The classname.
     *  @param  fieldname  The fieldname.
     *  @return  The type of the field.
     */
    String getFieldType(String classname,
                        String fieldname)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     *  Gets the modifiers of a field. The return value is a constant of the
     *  <code>java.lang.reflect.Modifier</code> class.
     *
     *  @param  classname  The classname.
     *  @param  fieldname  The fieldname.
     *  @return  The modifiers.
     *  @see  java.lang.reflect.Modifier
     */
    int getFieldModifiers(String classname,
                          String fieldname)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    // convenience methods

    /**
     *  Gets the type of some fields.
     *
     *  @param  classname  The classname.
     *  @param  fieldnames  The fieldnames.
     *  @return  The type of the fields.
     */
    String[] getFieldType(String classname,
                          String[] fieldnames)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;

    /**
     *  Gets the modifiers of some fields.
     *
     *  @param  classname  The classname.
     *  @param  fieldnames  The fieldnames.
     *  @return  The modifiers.
     *  @see  java.lang.reflect.Modifier
     */
    int[] getFieldModifiers(String classname,
                            String[] fieldnames)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError;
}
