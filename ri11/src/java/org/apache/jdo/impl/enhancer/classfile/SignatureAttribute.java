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
 * A SignatureAttribute is a fixed-length attribute in the attributes table
 * of ClassFile, ClassField, and ClassMethod structures.  A signature is a
 * string representing the generic type of a field or method, or generic
 * type information for a class declaration.
 */
public class SignatureAttribute extends ClassAttribute {
    /* The expected name of this attribute */
    public static final String expectedAttrName = "Signature";

    /* The signature constant */
    private ConstUtf8 signature;

    /* public accessors */

    public ConstUtf8 signature() {
        return signature;
    }

    /** 
     * Construct a signature attribute
     */
    public SignatureAttribute(ConstUtf8 attrName, ConstUtf8 sig) {
        super(attrName);
        signature = sig;
    }

    /**
     * Compares this instance with another for structural equality.
     */
    public boolean isEqual(Stack msg, Object obj) {
        if (!(obj instanceof SignatureAttribute)) {
            msg.push("obj/obj.getClass() = "
                     + (obj == null ? null : obj.getClass()));
            msg.push("this.getClass() = "
                     + this.getClass());
            return false;
        }
        SignatureAttribute other = (SignatureAttribute)obj;

        if (!super.isEqual(msg, other)) {
            return false;
        }

        if (!this.signature.isEqual(msg, other.signature)) {
            msg.push("signature = " + other.signature);
            msg.push("signature = " + this.signature);
            return false;
        }
        return true;
    }

    /* package local methods */

    static SignatureAttribute read(ConstUtf8 attrName,
                                   DataInputStream data, ConstantPool pool)
        throws IOException {
        int index = 0;
        index = data.readUnsignedShort();

        return new SignatureAttribute(attrName,
                                      (ConstUtf8)pool.constantAt(index));
    }

    void write(DataOutputStream out) throws IOException {
        out.writeShort(attrName().getIndex());
        out.writeInt(2);
        out.writeShort(signature.getIndex());
    }

    void print(PrintStream out, int indent) {
        ClassPrint.spaces(out, indent);
        out.println("Signature: " + signature.toString());
    }
}

