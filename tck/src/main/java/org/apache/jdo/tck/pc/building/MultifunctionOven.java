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
package org.apache.jdo.tck.pc.building;

import java.io.Serializable;

/** 
 * Representation of a multifunction oven in a fitted kitchen.
 */
public class MultifunctionOven extends Oven implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Whether this oven provides a microwave. */
    protected boolean microwave;

    /** Capabilities of this model */
    protected String capabilities;

    public MultifunctionOven(String make, String model) {
    	super(make, model);
    }

    public void setMicrowave(boolean micro) {
    	this.microwave = micro;
    }

    public boolean getMicrowave() {
    	return microwave;
    }

    public void setCapabilities(String caps) {
    	this.capabilities = caps;
    }

    public String getCapabilities() {
    	return capabilities;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (!(that instanceof MultifunctionOven)) {
            return false;
        }
        return super.equals((MultifunctionOven) that);
    }

    public boolean equals(MultifunctionOven that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        return super.equals(that);
    }
}
