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

package org.apache.jdo.impl.model.jdo;

import java.util.*;

import org.apache.jdo.model.jdo.JDOModel;
import org.apache.jdo.model.jdo.JDOPackage;

/**
 * A JDOPackage instance represents the JDO package metadata.
 *
 * @author Michael Bouschen
 */
public class JDOPackageImpl 
    extends JDOElementImpl
    implements JDOPackage
{
    /** The package name. */
    private String name;

    /** Relationship JDOModel<->JDOPackage. Initialized during creation.*/
    private JDOModel declaringModel;

    /**
     * Returns the name of this JDOPackage.
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this JDOPackage.
     * @param name the name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the declaring JDOModel of this JDOPackage.
     * @return the JDOModel that owns this JDOPackage.
     */
    public JDOModel getDeclaringModel()
    {
        return declaringModel;
    }

    /**
     * Set the declaring JDOModel for this JDOPackage.
     * @param model the declaring JDOModel of this JDOPackage.
     */
    public void setDeclaringModel(JDOModel model)
    {
        this.declaringModel = model;
    }
}
