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

package org.apache.jdo.tck.util;

import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;

/**
 *  TCKFileAppender appends log events to a file.
 *  Overrides {@link FileAppender#setFile(java.lang.String, boolean, boolean, int)}
 *  changing the given file name.
 */
public class TCKFileAppender extends FileAppender {

    /**
     * @see FileAppender#FileAppender()
     */
    public TCKFileAppender() {}
    
    public TCKFileAppender(Layout layout, String filename, boolean append, 
            boolean bufferedIO, int bufferSize) throws IOException {
        super(layout, filename, append, bufferedIO, bufferSize);
    }
    
    /**
     * @see FileAppender#FileAppender(org.apache.log4j.Layout, java.lang.String, boolean, boolean, int)
     */
    public TCKFileAppender(Layout layout, String filename, boolean append) 
        throws IOException {
        super(layout, filename, append);
    }
    
    /**
     * @see FileAppender#FileAppender(org.apache.log4j.Layout, java.lang.String)
     */
    public TCKFileAppender(Layout layout, String filename) throws IOException {
        this(layout, filename, true);
    }

    /**
     * The given file name is changed calling method
     * {@link BatchTestRunner#changeFileName(String)}.
     * @see FileAppender#setFile(java.lang.String, boolean, boolean, int)
     */
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) 
        throws IOException {
        String changedFileName = BatchTestRunner.changeFileName(fileName);
        super.setFile(changedFileName, append, bufferedIO, bufferSize);
    }
}

