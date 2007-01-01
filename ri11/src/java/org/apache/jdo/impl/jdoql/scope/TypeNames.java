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

/*
 * TypeNames.java
 *
 * Created on September 06, 2001
 */

package org.apache.jdo.impl.jdoql.scope;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.jdo.impl.jdoql.jdoqlc.TypeSupport;
import org.apache.jdo.model.java.JavaType;


/**
 * The table handling type names.
 * <p>
 * TBD:
 * <ul>
 * <li> check single type-import-on-demand on access
 * </ul> 
 * @author  Michael Bouschen
 */
public class TypeNames
{
    /**
     * The corresponding type table.
     **/
    protected TypeSupport typeSupport;

    /**
     * Map of single-type-imports. 
     * Key is the imported class name, value is the fully qualified class name.
     */
    protected Map imports = new HashMap();

    /**
     * Collection of type-imports-on-demand. 
     * The collection stores the imported package names.
     */
    protected Set importOnDemands = new HashSet();

    /**
     * The package of the class of the current compilation unit.
     */
    protected String currentPackage = null;

    /**
     * Creates a new TypeNames instance.
     */
    public TypeNames(TypeSupport typeSupport)
    {
        this.typeSupport = typeSupport;
    }

    /**
     * This method initializes the TypeNames table. It sets the currentPackage 
     * as the package name of the specified compilation unit. It also adds the 
     * package java.lang to the type-imports-on-demand.
     */
    public void init(String compilationUnit)
    {
        declareImport(compilationUnit);
        declareImportOnDemand("java.lang"); //NOI18N
        int index = compilationUnit.lastIndexOf('.');
        currentPackage = index > 0 ? compilationUnit.substring(0, index) : ""; //NOI18N
    }

    /**
     * Defines a single-type-import.
     * @param typeName the fully qualified name of the type to be imported.
     */
    public String declareImport(String typeName)
    {
        int index = typeName.lastIndexOf('.');
        String shortName = index>0 ? typeName.substring(index+1) : typeName;
        String old = (String)imports.get(shortName);
        if (old == null)
            imports.put(shortName, typeName);
        else if (old.equals(typeName))
            // same import twice => no problem
            old = null;
        return old;
    }

    /**
     * Defines a type-import-on-demand.
     * @param packageName the package name to be imported.
     */
    public void declareImportOnDemand(String packageName)
    {
        importOnDemands.add(packageName);
    }

    /**
     * Resolves a type name. If the specified type name is fully qualified 
     * the method checks the type table for the type representation. If the 
     * name is not fully qualified, the method first checks whether there is 
     * a single-type-import importing the specified name. If not it checks 
     * whether the current package defines this type. If not the method checks 
     * whether there is a single type-import-on-demand for the spceified name. 
     * @param name a type name
     * @return the type representation for the type name.
     */
    public JavaType resolve(String name)
    {
        // check fully qulified class name
        int index = name.indexOf('.');
        if (index > -1) {
            return typeSupport.checkType(name);
        }
        
        // check single-type-import
        String singleTypeImort = (String)imports.get(name);
        if (singleTypeImort != null)
            return typeSupport.checkType(singleTypeImort);
        
        // check current package
        JavaType type = typeSupport.checkType(currentPackage + '.' + name);
        if (type != null)
            return type;

        // check type-import-on-demand
        // TBD: check single type-import-on-demand
        for (Iterator i = importOnDemands.iterator(); i.hasNext();) {
            String next = (String)i.next();
            type = typeSupport.checkType(next + '.' + name);
            if (type != null)
                return type;
        }

        // not found
        return null;
    }

}
