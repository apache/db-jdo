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
import java.util.Stack;

/**
 * Class representing an integer constant in the constant pool of a class file
 */
public class ConstInteger extends ConstValue {
    /* The tag value associated with ConstInteger */
    public static final int MyTag = CONSTANTInteger;

    /* The value */
    private int intValue;

    /* public accessors */

    /**
     * The tag of this constant entry
     */
    public int tag() { return MyTag; }

    /**
     * return the value associated with the entry
     */
    public int value() {
        return intValue;
    }

    /**
     * Return the descriptor string for the constant type.
     */
    public String descriptor() {
        return "I";
    }

    /**
     * A printable representation
     */
    public String toString() {
        return "CONSTANTInteger(" + indexAsString() + "): " + 
            "intValue(" + Integer.toString(intValue) + ")";
    }

    /**
     * Compares this instance with another for structural equality.
     */
    //@olsen: added method
    public boolean isEqual(Stack msg, Object obj) {
        if (!(obj instanceof ConstInteger)) {
            msg.push("obj/obj.getClass() = "
                     + (obj == null ? null : obj.getClass()));
            msg.push("this.getClass() = "
                     + this.getClass());
            return false;
        }
        ConstInteger other = (ConstInteger)obj;

        if (!super.isEqual(msg, other)) {
            return false;
        }

        if (this.intValue != other.intValue) {
            msg.push(String.valueOf("intValue = "
                                    + other.intValue));
            msg.push(String.valueOf("intValue = "
                                    + this.intValue));
            return false;
        }
        return true;
    }

    /* package local methods */

    ConstInteger(int i) {
        intValue = i;
    }

    void formatData(DataOutputStream b) throws IOException {
        b.writeInt(intValue);
    }

    static ConstInteger read(DataInputStream input) throws IOException {
        return new ConstInteger(input.readInt());
    }

    void resolve(ConstantPool p) { }
}
