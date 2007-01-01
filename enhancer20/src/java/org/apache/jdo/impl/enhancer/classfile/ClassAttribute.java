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

import java.util.Stack;
import java.util.Vector;
import java.util.Hashtable;
import java.io.*;

/**
 * An abstract base class for the attributes within a class file
 */
public abstract class ClassAttribute implements VMConstants {

    /* The name of the attribute */
    private ConstUtf8 attributeName;

    /**
     * Returns the name of the attribute
     */
    public ConstUtf8 attrName() {
        return attributeName;
    }

    /**
     * Compares this instance with another for structural equality.
     */
    //@olsen: added method
    public boolean isEqual(Stack msg, Object obj) {
        if (!(obj instanceof ClassAttribute)) {
            msg.push("obj/obj.getClass() = "
                     + (obj == null ? null : obj.getClass()));
            msg.push("this.getClass() = "
                     + this.getClass());
            return false;
        }
        ClassAttribute other = (ClassAttribute)obj;

        if (!this.attributeName.isEqual(msg, other.attributeName)) {
            msg.push(String.valueOf("attributeName = "
                                    + other.attributeName));
            msg.push(String.valueOf("attributeName = "
                                    + this.attributeName));
            return false;
        }
        return true;
    }

    /**
     * Constructor
     */
    ClassAttribute(ConstUtf8 theAttrName) {
        attributeName = theAttrName;
    }

    /**
     * General attribute reader 
     */
    static ClassAttribute read(DataInputStream data, ConstantPool pool)
	throws IOException {

        ClassAttribute attr = null;
        int attrNameIndex = data.readUnsignedShort();
        ConstUtf8 attrName8 = (ConstUtf8) pool.constantAt(attrNameIndex);
        String attrName = attrName8.asString();
        int attrLength = data.readInt();

        if (attrName.equals(CodeAttribute.expectedAttrName)) {
            /* The old style code attribute reader uses more memory and
               cpu when the instructions don't need to be examined than the
               new deferred attribute reader.  We may at some point decide that
               we want to change the default based on the current situation
               but for now we will just use the deferred reader in all cases. */
            if (true) {
                attr = CodeAttribute.read(attrName8, attrLength, data, pool);
            } else {
                attr = CodeAttribute.read(attrName8, data, pool);
            }
        }
        else if (attrName.equals(SourceFileAttribute.expectedAttrName)) {
            attr = SourceFileAttribute.read(attrName8, data, pool);
        }
        else if (attrName.equals(ConstantValueAttribute.expectedAttrName)) {
            attr = ConstantValueAttribute.read(attrName8, data, pool);
        }
        else if (attrName.equals(ExceptionsAttribute.expectedAttrName)) {
            attr = ExceptionsAttribute.read(attrName8, data, pool);
        }
        else if (attrName.equals(AnnotatedClassAttribute.expectedAttrName)) {
            attr = AnnotatedClassAttribute.read(attrName8, data, pool);
        }
        else {
            /* Unrecognized method attribute */
            byte attrBytes[] = new byte[attrLength];
            data.readFully(attrBytes);
            attr = new GenericAttribute (attrName8, attrBytes);
        }

        return attr;
    }

    /*
     * CodeAttribute attribute reader
     */

    static ClassAttribute read(DataInputStream data, CodeEnv env)
	throws IOException {
        ClassAttribute attr = null;
        int attrNameIndex = data.readUnsignedShort();
        ConstUtf8 attrName8 = (ConstUtf8) env.pool().constantAt(attrNameIndex);
        String attrName = attrName8.asString();
        int attrLength = data.readInt();

        if (attrName.equals(LineNumberTableAttribute.expectedAttrName)) {
            attr = LineNumberTableAttribute.read(attrName8, data, env);
        }
        else if (attrName.equals(LocalVariableTableAttribute.expectedAttrName)) {
            attr = LocalVariableTableAttribute.read(attrName8, data, env);
        }
        else if (attrName.equals(AnnotatedMethodAttribute.expectedAttrName)) {
            attr = AnnotatedMethodAttribute.read(attrName8, data, env);
        }
        //@olsen: fix 4467428, added support for synthetic code attribute
        else if (attrName.equals(SyntheticAttribute.expectedAttrName)) {
            attr = SyntheticAttribute.read(attrName8, data, env.pool());
        }
        else {
            /* Unrecognized method attribute */
            byte attrBytes[] = new byte[attrLength];
            data.readFully(attrBytes);
            attr = new GenericAttribute (attrName8, attrBytes);
        }

        return attr;
    }

    /**
     * Write the attribute to the output stream
     */
    abstract void write(DataOutputStream out) throws IOException;

    /**
     * Print a description of the attribute to the print stream
     */
    abstract void print(PrintStream out, int indent);
}

