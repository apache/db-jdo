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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.impl.model.jdo.JDOModelImplDynamic;
import org.apache.jdo.model.java.JavaType;

/**
 * A JDOModel instance bundles a number of JDOClass instances used by an 
 * application. It provides factory methods to create and retrieve JDOClass 
 * instances. A fully qualified class name must be unique within a JDOModel 
 * instance. The model supports multiple classes having the same fully qualified 
 * name by different JDOModel instances.
 * <p>
 * The caching JDOModel implementation caches any caclulated value to
 * avoid re-calculating it if it is requested again. It is intended to
 * be used in an environment where JDO metadata does NOT change
 * (e.g. at runtime).
 *
 * @author Michael Bouschen
 * @since 1.1
 * @version 1.1
 */
public class JDOModelImplCaching extends JDOModelImplDynamic {

    /** 
     * This is a mapping from ObjectId classes to its JDOClass instances.
     * Key is the type representation of the ObjectId class, value is the 
     * corresponding JDOClass instance. Note, in the case of inheritance
     * the top most persistence-capable class is stored.
     */
    private Map jdoClassesByObjectIdClasses = new HashMap();

    /** 
     * Set of fully qualified names of classes known to be 
     * non persistence-capable. 
     */
    private Set nonPCClasses = new HashSet();

    /** 
     * Constructor. 
     * JDOModel instances are created using the JDOModelFactory only.
     */
    protected JDOModelImplCaching(JavaModel javaModel) {
        super(javaModel);
    }

    /**
     * This method returns the JDOClass instance that defines the specified type
     * as its objectId class. In the case of an inheritance hierarchy it returns 
     * the top most persistence-capable class of the hierarchy (see 
     * {@link JDOClass#getPersistenceCapableSuperclass}).
     * @param objectIdClass the type representation of the ObjectId class
     * @return the JDOClass defining the specified class as ObjectId class
     */
    public JDOClass getJDOClassForObjectIdClass(JavaType objectIdClass)
    {
        // Note, method getJDOClassForObjectIdClass is not synchronized to
        // avoid a deadlock with PC class registration.
        if (objectIdClass == null)
            return null;

        synchronized (jdoClassesByObjectIdClasses) {
            // First check the cache
            JDOClass jdoClass = 
                (JDOClass)jdoClassesByObjectIdClasses.get(objectIdClass);
            if (jdoClass == null) {
                // not found in the cache => call super
                jdoClass = super.getJDOClassForObjectIdClass(objectIdClass);
                if (jdoClass != null) {
                    // found => update the cache
                    jdoClassesByObjectIdClasses.put(objectIdClass, jdoClass);
                }
            }
            
            return jdoClass;
        }
    }

    /** Returns a new instance of the JDOClass implementation class. */
    protected JDOClass newJDOClassInstance() {
        return new JDOClassImplCaching();
    }

    /**
     * Checks whether the type with the specified name does NOT denote a
     * persistence-capable class.
     * @param typeName name of the type to be checked
     * @return <code>true</code> if types is a name of a primitive type; 
     * <code>false</code> otherwise
     */
    protected boolean isKnownNonPC(String typeName) {
        return super.isKnownNonPC(typeName) || nonPCClasses.contains(typeName);
    }

    /** 
     * Hook called when a class is known to be non persistence
     * capable.
     * @param className the name of the non-pc class
     */
    protected void knownNonPC(String className) {
        nonPCClasses.add(className);
    }
}
