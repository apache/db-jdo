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

package org.apache.jdo.impl.model.jdo;

import java.util.Map;
import java.util.HashMap;

import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.jdo.JDOModel;
import org.apache.jdo.model.jdo.JDOModelFactory;

/**
 * Factory for dynamic JDOModel instances. The factory provides a
 * mechanism to cache JDOModel instances per JavaModel instances. 
 * <p>
 * TBD:
 * <ul>
 * <li> Check synchronization.
 * </ul>
 *
 * @author Michael Bouschen
 * @since 1.1
 * @version 1.1
 */
public class JDOModelFactoryImplDynamic implements JDOModelFactory {

    /**
     * Map of JDOModel instances, key is the JavaModel
     * {@link #getJDOModel(JavaModel javaModel)} 
     */
    private Map modelCache = new HashMap();

    /** The singleton JDOModelFactory instance. */    
    private static JDOModelFactory jdoModelFactory = 
        new JDOModelFactoryImplDynamic();

    /**
     * Creates new JDOModelFactory. This constructor should not be
     * called directly; instead, the singleton access method  
     * {@link #getInstance} should be used.
     */
    protected JDOModelFactoryImplDynamic() {}

    /** 
     * Get an instance of JDOModelFactory.
     * @return an instance of JDOModelFactory
     */    
    public static JDOModelFactory getInstance() {
        return jdoModelFactory;
    }
    
    /**
     * Creates a new empty JDOModel instance.
     */
    public JDOModel createJDOModel(JavaModel javaModel) {
        return new JDOModelImplDynamic(javaModel);
    }
    
    /**
     * Returns the JDOModel instance for the specified key.
     * @param javaModel the javaModel used to cache the returned JDOModel instance
     */
    public JDOModel getJDOModel(JavaModel javaModel) {
        synchronized (this.modelCache) {
            JDOModel jdoModel = (JDOModel)modelCache.get(javaModel);
            if (jdoModel == null) {
                // create new model and store it using the specified javaModel
                jdoModel = createJDOModel(javaModel);
                modelCache.put(javaModel, jdoModel);
            }
            return jdoModel;
        }
    }
    
}
