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

/**
 * This interface provides constants denoting the identity type
 * of a persistence-capable class.
 *
 * @author Michael Bouschen
 */
public class JDOIdentityType 
{
    /** Constant representing an unspecified jdo identity */
    public static final int UNSPECIFIED = 0;

    /** Constant representing jdo identity managed by the database. */
    public static final int DATASTORE = 1;

    /** Constant representing jdo identity managed by the application. */
    public static final int APPLICATION = 2;

    /** Constant representing unmanaged jdo identity. */
    public static final int NONDURABLE = 4;
    
    /**
     * Returns a string representation of the specified identity type constant.
     * @param jdoIdentityType the JDO identity type, one of 
     * {@link #APPLICATION}, {@link #DATASTORE}, or {@link #NONDURABLE}
     * @return the string representation of the JDOIdentityType constant
     */
    public static String toString(int jdoIdentityType) {
        switch ( jdoIdentityType) {
        case DATASTORE :
            return "datastore"; //NOI18N
        case APPLICATION :
            return "application"; //NOI18N
        case NONDURABLE:
            return "nondurable"; //NOI18N
        default:
            return "UNSPECIFIED"; //NOI18N
        }
    }
    
    /**
     * Returns the JDOIdentityType constant, one of {@link #APPLICATION}, 
     * {@link #DATASTORE}, or {@link #NONDURABLE} for the specified string.
     * @param jdoIdentityType the string representation of the 
     * JDO identity type
     * @return the JDO identity type
     **/
    public static int toJDOIdentityType(String jdoIdentityType)
    {
        if ((jdoIdentityType == null) || (jdoIdentityType.length() == 0))
            return UNSPECIFIED;
 
        if ("datastore".equals(jdoIdentityType)) //NOI18N
            return DATASTORE;
        else if ("application".equals(jdoIdentityType)) //NOI18N
            return APPLICATION;
        else if ("nondurable".equals(jdoIdentityType)) //NOI18N
            return NONDURABLE;
        else
            return UNSPECIFIED;
    }
}
