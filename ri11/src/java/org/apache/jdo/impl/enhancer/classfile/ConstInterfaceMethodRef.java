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


package org.apache.jdo.impl.enhancer.classfile;

import java.io.*;

/**
 * Class representing a reference to an interface method of some class
 * in the constant pool of a class file.
 */
public class ConstInterfaceMethodRef extends ConstBasicMemberRef {
    /* The tag value associated with ConstDouble */
    public static final int MyTag = CONSTANTInterfaceMethodRef;

    /* public accessors */

    /**
     * The tag of this constant entry
     */
    public int tag () { return MyTag; }

    /**
     * A printable representation
     */
    public String toString () {
        return "CONSTANTInterfaceMethodRef(" + indexAsString() + "): " + 
            super.toString();
    }

    /* package local methods */

    ConstInterfaceMethodRef (ConstClass cname, ConstNameAndType NT) {
        super(cname, NT);
    }

    ConstInterfaceMethodRef (int cnameIndex, int NT_index) {
        super(cnameIndex, NT_index);
    }

    static ConstInterfaceMethodRef read (DataInputStream input) 
        throws IOException {
        int cname = input.readUnsignedShort();
        int NT = input.readUnsignedShort();
        return new ConstInterfaceMethodRef (cname, NT);
    }
}
