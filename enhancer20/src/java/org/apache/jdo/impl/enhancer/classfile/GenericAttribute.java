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
 * GenericAttribute represents a class attribute in a class file which
 * is not recognized as any supported attribute type.  These attributes
 * are maintained, and are not modified in any way.
 */

public class GenericAttribute extends ClassAttribute {

    /* The bytes of the attribute following the name */
    byte attributeBytes[];

    /* public accessors */

    /**
     * constructor
     */
    public GenericAttribute(ConstUtf8 attrName, byte attrBytes[]) {
        super(attrName);
        attributeBytes = attrBytes;
    }

    /**
     * Compares this instance with another for structural equality.
     */
    //@olsen: added method
    public boolean isEqual(Stack msg, Object obj) {
        if (!(obj instanceof GenericAttribute)) {
            msg.push("obj/obj.getClass() = "
                     + (obj == null ? null : obj.getClass()));
            msg.push("this.getClass() = "
                     + this.getClass());
            return false;
        }
        GenericAttribute other = (GenericAttribute)obj;

        if (!super.isEqual(msg, other)) {
            return false;
        }

        if (this.attributeBytes.length != other.attributeBytes.length) {
            msg.push("attributeBytes.length "
                     + String.valueOf(other.attributeBytes.length));
            msg.push("attributeBytes.length "
                     + String.valueOf(this.attributeBytes.length));
            return false;
        }

        for (int i = 0; i < attributeBytes.length; i++) {
            byte b1 = this.attributeBytes[i];
            byte b2 = other.attributeBytes[i];
            if (b1 != b2) {
                msg.push("attributeBytes[" + i + "] = "
                         + String.valueOf(b2));
                msg.push("attributeBytes[" + i + "] = "
                         + String.valueOf(b1));
                return false;
            }
        }
        return true;
    }

    void write(DataOutputStream out) throws IOException {
        out.writeShort(attrName().getIndex());
        out.writeInt(attributeBytes.length);
        out.write(attributeBytes, 0, attributeBytes.length);
    }

    void print(PrintStream out, int indent) {
        ClassPrint.spaces(out, indent);
        out.println("Generic Attribute(" + attrName().asString() + "): " +
                    Integer.toString(attributeBytes.length) +
                    " in length");
        for (int i=0; i<attributeBytes.length; i++) {
            if ((i % 16) == 0) {
                if (i != 0) 
                    out.println();
                out.print(i + " :");
            }
            out.print(" " + Integer.toString((attributeBytes[i] & 0xff), 16));
        }
        out.println();
    }
}

