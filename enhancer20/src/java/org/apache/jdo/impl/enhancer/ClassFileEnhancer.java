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

package org.apache.jdo.impl.enhancer;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * A JDO enhancer, or byte-code enhancer, modifies the byte-codes of
 * Java class files to enable transparent loading and storing of the
 * fields of the persistent instances.
 *
 * @author Martin Zaun
 */
public interface ClassFileEnhancer
{
    /**
     * Enhances a given class according to the JDO meta-data. If the
     * input class has been enhanced or not - the output stream is
     * always written, either with the enhanced class or with the
     * non-enhanced class.
     *
     * @param in The byte-code of the class to be enhanced.
     * @param out The byte-code of the enhanced class.
     * @return  <code>true</code> if the class has been enhanced,
     *          <code>false</code> otherwise.
     */
   boolean enhanceClassFile(InputStream in,
                            OutputStream out)
        throws EnhancerUserException, EnhancerFatalError;


    /**
     * Enhances a given class according to the JDO meta-data. If the
     * input class has been enhanced or not - the output stream is
     * always written, either with the enhanced class or with the
     * non-enhanced class.
     * <p>
     * Furthermore, the enhancer has to set the classname of
     * the enhanced class to the output stream wrapper object (it's
     * possible to get the input stream without knowing the classname).
     *
     * @param in  The byte-code of the class to be enhanced.
     * @param out The byte-code of the enhanced class.
     * @return  <code>true</code> if the class has been enhanced,
     *          <code>false</code> otherwise.
     */
    boolean enhanceClassFile(InputStream in,
                             OutputStreamWrapper out)
            throws EnhancerUserException, EnhancerFatalError;
}
