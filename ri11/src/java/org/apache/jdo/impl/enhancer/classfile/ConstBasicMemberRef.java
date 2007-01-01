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
 * The abstract base class used to represent the various type of
 * references to members (fields/methods) within the constant pool. 
 */
public abstract class ConstBasicMemberRef extends ConstBasic {
    /* The name of the class on which the member is defined */
    protected ConstClass theClassName;

    /* The index of the class on which the member is defined
     *   - used temporarily while reading from a class file */
    protected int theClassNameIndex;

    /* The name and type of the member */
    protected ConstNameAndType theNameAndType;

    /* The index of the name and type of the member
     *   - used temporarily while reading from a class file */
    protected int theNameAndTypeIndex;

    /* public accessors */

    /**
     * Return the name of the class defining the member 
     */
    public ConstClass className() {
        return theClassName;
    }

    /**
     * Return the name and type of the member 
     */
    public ConstNameAndType nameAndType() {
        return theNameAndType;
    }

    public String toString () {
        return "className(" + theClassName.toString() + ")" +
            " nameAndType(" + theNameAndType.toString() + ")";
    }

    /**
     * Compares this instance with another for structural equality.
     */
    //@olsen: added method
    public boolean isEqual(Stack msg, Object obj) {
        if (!(obj instanceof ConstBasicMemberRef)) {
            msg.push("obj/obj.getClass() = "
                     + (obj == null ? null : obj.getClass()));
            msg.push("this.getClass() = "
                     + this.getClass());
            return false;
        }
        ConstBasicMemberRef other = (ConstBasicMemberRef)obj;

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
        if (!this.theNameAndType.isEqual(msg, other.theNameAndType)) {
            msg.push(String.valueOf("theNameAndType = "
                                    + other.theNameAndType));
            msg.push(String.valueOf("theNameAndType = "
                                    + this.theNameAndType));
            return false;
        }
        return true;
    }

    /* package local methods */

    /**
     * Constructor for "from scratch" creation
     */
    ConstBasicMemberRef (ConstClass cname, ConstNameAndType NT) {
        theClassName = cname;
        theNameAndType = NT;
    }

    /**
     * Constructor for reading from a class file
     */
    ConstBasicMemberRef (int cnameIndex, int NT_index) {
        theClassNameIndex = cnameIndex;
        theNameAndTypeIndex = NT_index;
    }

    void formatData (DataOutputStream b) throws IOException {
        b.writeShort(theClassName.getIndex());
        b.writeShort(theNameAndType.getIndex());
    }
    void resolve (ConstantPool p) {
        theClassName = (ConstClass) p.constantAt(theClassNameIndex);
        theNameAndType = (ConstNameAndType) p.constantAt(theNameAndTypeIndex);
    }
}
