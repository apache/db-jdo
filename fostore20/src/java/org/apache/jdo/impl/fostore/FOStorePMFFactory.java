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

package org.apache.jdo.impl.fostore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import javax.naming.StringRefAddr;

/**
* Creates a FOStorePMF when read in via JNDI.
* <p>
* This class is <code>public</code> so that JNDI can create instances.
*
* @author Dave Bristor
*/
public class FOStorePMFFactory implements ObjectFactory {
    
    /**
     * Uses StringRefAddr's to store the information
     */
    public Object getObjectInstance(
        Object obj, Name name, Context ctx, Hashtable env) throws Exception {

        FOStorePMF rc = null;

        if (obj instanceof Reference) {
            Reference ref = (Reference)obj;
            if (ref.getClassName().equals(FOStorePMF.class.getName())) {
                Properties p = new Properties();
                for (Enumeration e = ref.getAll(); e.hasMoreElements();) {
                    StringRefAddr sra = (StringRefAddr)e.nextElement();
                    p.setProperty(sra.getType(), (String)sra.getContent());
                }

                rc = new FOStorePMF();
                rc.setFromProperties(p);
            }
        }
        return rc;
    }
}
