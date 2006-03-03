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

package org.apache.jdo.tck.api.persistencecapable;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.spi.PersistenceCapable;

import org.apache.jdo.tck.JDO_Test;

/**
 * This class is the super class for all persistence-capable tests. 
 * @author Michael Watzek
 */
abstract class AbstractPersistenceCapableTest extends JDO_Test {

    /**
     * Makes the given object persistent, 
     * adds it as tear down instance,
     * and returns its object id.
     * @param pc the transient instance to be made persistent 
     * @return the object id of the persistent instance
     */
    protected Object makePersistent(Object pc) {
        addTearDownClass(pc.getClass());
        PersistenceManager pm = getPM();
        pm.currentTransaction().begin();
        pm.makePersistent(pc);
        Object result = JDOHelper.getObjectId(pc);
        pm.currentTransaction().commit();
        return result;
    }
    
    protected boolean isTestToBePerformed() {
        String testName = getClass().getName();
        if (!isBinaryCompatibilitySupported()) {
            printUnsupportedOptionalFeatureNotTested(
                    testName, "javax.jdo.option.BinaryCompatibility");
        } else if (!isApplicationIdentitySupported()) {
            printUnsupportedOptionalFeatureNotTested(
                    testName, "javax.jdo.option.ApplicationIdentity");
        }
        return isBinaryCompatibilitySupported() &&
               isApplicationIdentitySupported() &&
               runsWithApplicationIdentity();
    }
    
    protected void assertPersistenceCapable(Object o) {
        if (!(o instanceof PersistenceCapable)) {
            throw new IllegalArgumentException
                ("Argument o must be instance of PersistenceCapable.");
        }
    }
}
