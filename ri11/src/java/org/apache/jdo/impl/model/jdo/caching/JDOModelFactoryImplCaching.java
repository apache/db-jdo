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

package org.apache.jdo.impl.model.jdo.caching;

import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.jdo.JDOModel;
import org.apache.jdo.model.jdo.JDOModelFactory;

import org.apache.jdo.impl.model.jdo.JDOModelFactoryImplDynamic;

/**
 * Factory for caching JDOModel instances.
 *
 * @author Michael Bouschen
 * @since 1.1
 * @version 1.1
 */
public class JDOModelFactoryImplCaching extends JDOModelFactoryImplDynamic {

    /** The singleton JDOModelFactory instance. */    
    private static JDOModelFactory jdoModelFactory = 
        new JDOModelFactoryImplCaching();

    /**
     * Creates new JDOModelFactoryImplCaching. This constructor
     * should not be called directly; instead, the singleton access
     * method  {@link #getInstance} should be used.
     */
    protected JDOModelFactoryImplCaching() {}

    /** 
     * Get an instance of JDOModelFactoryImpl.
     * @return an instance of JDOModelFactoryImpl
     */    
    public static JDOModelFactory getInstance() {
        return jdoModelFactory;
    }
    
    /**
     * Creates a new empty JDOModel instance.
     * The returned JDOModel instance uses the specified flag
     * <code>loadXMLMetadataDefault</code> to set the default value for the
     * flag <code>loadXMLMetadata</code> used by the JDOModel methods
     * createJDOClass and setJDOClass. 
     * @param loadXMLMetadataDefault the default setting for the flag
     * loadXMLMetadata.
     */
    public JDOModel createJDOModel(JavaModel javaModel,
                                   boolean loadXMLMetadataDefault) {
        return new JDOModelImplCaching(javaModel, loadXMLMetadataDefault);
    }

}
