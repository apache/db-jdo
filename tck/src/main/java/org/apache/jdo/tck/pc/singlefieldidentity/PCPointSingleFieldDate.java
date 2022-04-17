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
 
package org.apache.jdo.tck.pc.singlefieldidentity;

import java.util.Date;

import javax.jdo.identity.ObjectIdentity;
import javax.jdo.identity.SingleFieldIdentity;

/**
 * The PC class for testing <code>ObjectIdentity</code>.
 * @author Michael Watzek
 */
public class PCPointSingleFieldDate extends AbstractPCPointSingleField {
    
    /**
     * Returns a unique value, used for primary key field initialization.
     * @return a unique value
     */
    private static long newId() {
        synchronized (PCPointSingleFieldDate.class) {
            return counter++;
        }
    }

    /**
     * The primary key field.
     */
    private Date id = new Date(newId());

    public int x;
    public Integer y;

    /**
     * This constructor is used by test cases checking assertion A7.12-38:<br>
     * For classes using single field identity method 
     * <code>PersistenceCapable.newObjectIdInstance()</code> must be called 
     * on a persistent instance with its primary key field initialized, 
     * or a JDOFatalInternalException is thrown.
     */
    public PCPointSingleFieldDate() {
        this.id = null;
    }

    /**
     * This constructor is used by test cases checking assertion A7.12-39:<br>
     * The instance returned is initialized with the value of the primary key 
     * field of the instance on which the method is called.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public PCPointSingleFieldDate(int x, int y) {
        this.x = x;
        this.y = Integer.valueOf(y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getY() {
        return y;
    }
    
    public String name() {
        return " x: " + getX() + ", y: " + getY().intValue();
    }

    /**
     * Returns <code>true</code> if the given the key of the given 
     * <code>SingleFieldIdentity</code> instance equals the key in the subclass
     * of this class.
     * @param singleFieldIdentity the single field identity to check.
     * @return returns <code>true</code> if the given the key of the given 
     * <code>SingleFieldIdentity</code> instance equals the key in the subclass
     * of this class.
     */
    public boolean equalsPKField(SingleFieldIdentity singleFieldIdentity) {
        return this.id.equals(((ObjectIdentity)singleFieldIdentity).getKey());
    }
    
    public String toString() {
        return super.toString() + this.id;
    }
}
