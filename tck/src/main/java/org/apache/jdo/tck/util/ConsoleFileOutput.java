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

package org.apache.jdo.tck.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Creates an output stream that delegates to
 * {@link System#out} and to a file output stream.
 * The file name of the file output stream is determined
 * by method {@link BatchTestRunner#getFileName()}.
 */
class ConsoleFileOutput extends OutputStream {

    private String fileName;
    private final PrintStream systemOut = System.out;
    private FileOutputStream fileOut;
    
    ConsoleFileOutput() {
        this.fileName = BatchTestRunner.getFileName();
        try {
            this.fileOut = new FileOutputStream(this.fileName);
        } catch (IOException e) {
            System.err.println("Cannot create log file "+this.fileName+". "+e);
        }
    }
    
    /* 
     * @see java.io.OutputStream#write(int)
     */
    public void write(int b) throws IOException {
        this.systemOut.write(b);
        this.fileOut.write(b);
    }
    
    /**
     * @see java.io.OutputStream#close()
     */
    @Override
    public void close()  throws IOException {
        this.fileOut.close();
        this.systemOut.close();
    }

    /**
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush()  throws IOException {
        this.systemOut.flush();
        this.fileOut.flush();
    }  
    
    String getFileName() {
        return new File(this.fileName).getName();
    }
    
    String getDirectory() {
        String result = new File(this.fileName).getParent();
        if (!result.endsWith(File.separator))
            result += File.separator;
        return result;
    }
}
