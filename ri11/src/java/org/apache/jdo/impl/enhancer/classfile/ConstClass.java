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


package org.apache.jdo.impl.enhancer.classfile;

import java.io.*;
import java.util.Stack;

/**
 * Class representing a class reference in the constant pool
 */
//@olsen: JDK1.5: extend ConstValue to allow for class refs as constants
public class ConstClass extends ConstValue {
    /* The tag associated with ConstClass entries */
    public static final int MyTag = CONSTANTClass;

    /* The name of the class being referred to */
    private ConstUtf8 theClassName;

    /* The index of name of the class being referred to
     *  - used while reading from a class file */
    private int theClassNameIndex;

    /* public accessors */

    /**
     * Return the tag for this constant
     */
    public int tag() { return MyTag; }

    /**
     * Return the class name
     */
    public ConstUtf8 className() {
        return theClassName;
    }

    /**
     * Return the descriptor string for the constant type.
     */
    //@olsen: added method
    public String descriptor() {
        String cname = asString();
        return (cname.startsWith("[") ? cname : "L" + cname + ";");
    }

    /**
     * Return the class name in simple string form
     */
    public String asString() {
        return theClassName.asString();
    }

    /**
     * A printable representation 
     */
    public String toString() {
        return "CONSTANTClass(" + indexAsString() + "): " + 
            "className(" + theClassName.toString() + ")";
    }

    /**
     * Change the class reference (not to be done lightly)
     */
    public void changeClass(ConstUtf8 newName) {
        theClassName = newName;
        theClassNameIndex = newName.getIndex();
    }

    /**
     * Construct a ConstClass
     */
    public ConstClass(ConstUtf8 cname) {
        theClassName = cname;
    }

    /**
     * Compares this instance with another for structural equality.
     */
    //@olsen: JDK1.5: added method
    public boolean isEqual(Stack msg, Object obj) {
        if (!(obj instanceof ConstClass)) {
            msg.push("obj/obj.getClass() = "
                     + (obj == null ? null : obj.getClass()));
            msg.push("this.getClass() = "
                     + this.getClass());
            return false;
        }
        ConstClass other = (ConstClass)obj;

        if (!super.isEqual(msg, other)) {
            return false;
        }

        if (!this.theClassName.isEqual(msg, other.theClassName)) {
            msg.push(String.valueOf("theClassName = "
                                    + other.theClassName));
            msg.push(String.valueOf("theClassName = "
                                    + this.theClassName));
            return false;
        }
        return true;
    }

    /* package local methods */

    ConstClass(int cname) {
        theClassNameIndex = cname;
    }

    void formatData(DataOutputStream b) throws IOException {
        b.writeShort(theClassName.getIndex());
    }

    static ConstClass read(DataInputStream input) throws IOException {
        return new ConstClass(input.readUnsignedShort());
    }

    void resolve(ConstantPool p) {
        theClassName = (ConstUtf8)p.constantAt(theClassNameIndex);
    }
}
