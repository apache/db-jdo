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

package org.apache.jdo.tck;

import java.security.AccessController;
import java.security.PrivilegedAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.xml.XmlBeanFactory;

/*
 * Abstract class for managed relationship tests
 */
public class AbstractReaderTest extends JDO_Test {

    /** The list of all objects in the bean collection. */
    protected List rootOids;

    /** The name of the root object in the bean collection. */
    protected static final String rootName = "root";

    /** The name of the file containing the bean collection (test data). */
    protected final String inputFilename = System.getProperty("jdo.tck.testdata");

    /** The map of String (bean name) to Object (bean). */
    protected Map oidMap = new HashMap();

    /** Get the named bean from the bean factory.
     * 
     * @param reader the xml bean reader
     * @param name the name of the bean
     * @return the named object
     */
    protected Object getBean(final XmlBeanFactory reader, final String name) {
        return AccessController.doPrivileged(
            new PrivilegedAction() {
                public Object run() {
                    return reader.getBean(name);
                }
            }
        );
    }

    /** Get the root object from the bean factory.
     * 
     * @param reader the xml bean reader
     * @return the List of objects
     */
    protected List getRootList(XmlBeanFactory reader) {
        return (List) getBean(reader, rootName);
    }

    /** Get the named object from the Map of objects.
     * 
     * @param name the bean name
     * @return the named object
     */
    protected Object getOidByName(String name) {
        return oidMap.get((Object)name);
    }

}
