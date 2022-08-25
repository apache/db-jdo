/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

/*
 * RegisterClassEvent.java
 *
 */

package javax.jdo.spi;

import java.util.EventObject;

/**
 * A <code>RegisterClassEvent</code> event gets delivered whenever a persistence-capable
 * class registers itself with the <code>JDOImplHelper</code>.
 *
 * @version 1.0
 */
public class RegisterClassEvent extends EventObject
{
	private static final long serialVersionUID = 945917130591776973L;

	/** The class object of the registered persistence-capable class */
    protected Class pcClass;

    /** The names of managed fields of the persistence-capable class */
    protected String[] fieldNames;  

    /** The types of managed fields of the persistence-capable class */
    protected Class[] fieldTypes;

    /** The flags of managed fields of the persistence-capable class */
    protected byte[] fieldFlags;

    /** */
    protected Class persistenceCapableSuperclass; 

    /** 
     * Constructs a new <code>RegisterClassEvent</code>.
     * @param helper the <code>JDOImplHelper</code> instance
     * @param registeredClass the persistence-capable class
     * @param fieldNames the names of the managed fields
     * @param fieldTypes the types of the managed fields
     * @param fieldFlags the flags of the managed fields
     * @param persistenceCapableSuperclass the persistence-capable superclass
     **/
    public RegisterClassEvent(JDOImplHelper helper,
                              Class registeredClass, 
                              String[] fieldNames, 
                              Class[] fieldTypes,
                              byte[] fieldFlags,
                              Class persistenceCapableSuperclass)
    {
        super(helper);
        this.pcClass = registeredClass;
        this.fieldNames = fieldNames;
        this.fieldTypes = fieldTypes;
        this.fieldFlags = fieldFlags;
        this.persistenceCapableSuperclass = persistenceCapableSuperclass;
    }

    /**
     * Returns the class object of the registered persistence-capable class.
     * @return the persistence-capable class.
     */
    public Class getRegisteredClass()
    {
        return pcClass;
    }
    
    /**    
     * Returns the names of the managed field of the persistence-capable class.
     * @return the names of the managed fields
     */
    public String[] getFieldNames()
    {
        return fieldNames;
    }

    /**
     * Returns the types of the managed field of the persistence-capable class.
     * @return the types of the managed fields
     */
    public Class[] getFieldTypes()
    {
        return fieldTypes;
    }

    /**
     * Returns the flags of the managed field of the persistence-capable class.
     * @return the flags of the managed fields
     */
    public byte[] getFieldFlags()
    {
        return fieldFlags;
    }

    /**
     * Returns the class object of the persistence-capable superclass.
     * @return the persistence-capable superclass.
     */
    public Class getPersistenceCapableSuperclass()
    {
        return persistenceCapableSuperclass;
    }
}