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
 * Represents the source file attribute in a class file
 */
public class SourceFileAttribute extends ClassAttribute {
    /* The expected attribute name */
    public static final String expectedAttrName = "SourceFile";

    /* The source file name */
    private ConstUtf8 sourceFileName;

    /* public accessors */

    /**
     * Returns the source file name
     * The file name should not include directories
     */
    public ConstUtf8 fileName() {
        return sourceFileName;
    }

    /**
     * Sets the source file name
     */
    public void setFileName(ConstUtf8 name) {
        sourceFileName = name;
    }

    /**
     * Constructor for a source file attribute
     */
    public SourceFileAttribute(ConstUtf8 attrName, ConstUtf8 sourceName) {
        super(attrName);
        sourceFileName = sourceName;
    }

    /* package local methods */
    static SourceFileAttribute read(ConstUtf8 attrName,
                                    DataInputStream data, ConstantPool pool)
        throws IOException {
        int index = 0;
        index = data.readUnsignedShort();

        return new SourceFileAttribute(attrName,
                                       (ConstUtf8) pool.constantAt(index));
    }

    void write(DataOutputStream out) throws IOException {
        out.writeShort(attrName().getIndex());
        out.writeInt(2);
        out.writeShort(sourceFileName.getIndex());
    }

    void print(PrintStream out, int indent) {
        ClassPrint.spaces(out, indent);
        out.println("SourceFile: " + sourceFileName.asString());
    }
}
