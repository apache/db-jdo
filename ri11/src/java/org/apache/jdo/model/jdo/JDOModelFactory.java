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

package org.apache.jdo.model.jdo;

import org.apache.jdo.model.ModelException;
import org.apache.jdo.model.java.JavaModel;


/**
 * Factory for JDOModel instances. The factory provides a mechanism to cache 
 * JDOModel instances per user defined keys.
 * 
 * @author Michael Bouschen
 */
public interface JDOModelFactory 
{
    /**
     * Creates a new empty JDOModel instance.
     * @exception ModelException if impossible
     */
    public JDOModel createJDOModel(JavaModel javaModel)
        throws ModelException;
    
    /**
     * Returns the JDOModel instance for the specified JavaModel.
     * @param javaModel the javaModel used to cache the returned JDOModel instance
     */
    public JDOModel getJDOModel(JavaModel javaModel);
    
}
