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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

import javax.jdo.JDOUserException;

import org.apache.jdo.util.I18NHelper;


/** Diagnostic code to identify a dump request to the store. Currently,
* there are the following options supported: 
* <ul>
* <li>DBINFO: Provide information about all classes in the store.</li>
* <li>CLASS_METADATA: Provide metadata information about a special class.</li>
* <li>CLASS_INSTANCES: List all instances of a special class.</li>
* </ul>
*
* @author Markus Fuchs
*/
class DumpOption {
    /** Value of this DumpOption. */
    private final int value;

    /** String name of this option. */
    private final String name;

    /** Map from name to DumpOption. */
    private static final HashMap options = new HashMap();

    /** Provide information about all classes in the store. */
    static final DumpOption DBINFO =
        new DumpOption(1, "dbInfo"); // NOI18N

    /** Provide metadata information about a particular class.*/
    static final DumpOption CLASS_METADATA =
        new DumpOption(2, "classMetadata"); // NOI18N

    /** List all instances of a particular class. */
    static final DumpOption CLASS_INSTANCES =
    new DumpOption(3, "classInstances"); // NOI18N

    /** List all subclasses of a particular class. */
    static final DumpOption CLASS_SUBCLASSES =
    new DumpOption(4, "classSubclasses"); // NOI18N

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /**
     * Create a DumpOption with the given value and name.
     */
    private DumpOption(int value, String name) {
        this.value = value;
        this.name = name;
        options.put(name, this);
    }

    /** Provide a DumpOption given a name. */
    static DumpOption forName(String name) {
        DumpOption rc = (DumpOption)options.get(name);
        if (null == rc) {
            throw new JDOUserException(
                msg.msg("EXC_BadValue", name)); // NOI18N
        }
        return rc;
    }

    //
    // I/O support
    // 

    /**
     * Write this DumpOption's value.
     */
    void write(DataOutput out) throws IOException {
        out.writeUTF(name);
    }

    /**
     * Return the instance of a DumpOption that corresponds to the value read
     * from the DataInput.
     * @throws IOException if there is an IOException reading the value.
     * <em>or</em> if the value read does not correspond to a DumpOption.
     */
    static DumpOption read(DataInput in) throws IOException {
        String optionName = in.readUTF();
        DumpOption rc = forName(optionName);
        if (null == rc) {
            throw new IOException(
                msg.msg("EXC_BadValue", optionName)); // NOI18N
        }
        return rc;
    }

    //
    // Override java.lang.Object methods
    //
    
    public String toString() {
        return name;
    }
    
    public boolean equals(Object o) {
        boolean rc = false;
        if ((null != o) && (o instanceof DumpOption)) {
            rc = (value == ((DumpOption)o).value);
        }
        return rc;
    }

    public int hashCode() {
        // Same as for java.lang.Integer
        return value;
    }
}
