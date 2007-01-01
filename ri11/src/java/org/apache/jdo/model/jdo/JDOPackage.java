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

package org.apache.jdo.model.jdo;

import org.apache.jdo.model.ModelException;

/**
 * A JDOPackage instance represents the JDO package metadata.
 *
 * @author Michael Bouschen
 */
public interface JDOPackage 
    extends JDOElement
{
    /**
     * Returns the name of this JDOPackage.
     * @return the name
     */
    public String getName();

    /**
     * Sets the name of this JDOPackage.
     * @param name the name
     * @exception ModelException if impossible
     */
    public void setName(String name)
        throws ModelException;

    /**
     * Returns the declaring JDOModel of this JDOPackage.
     * @return the JDOModel that owns this JDOPackage.
     */
    public JDOModel getDeclaringModel();

    /**
     * Set the declaring JDOModel for this JDOPackage.
     * @param model the declaring JDOModel of this JDOPackage.
     */
    public void setDeclaringModel(JDOModel model);
}
