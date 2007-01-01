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

package org.apache.jdo.impl.enhancer;

import java.io.PrintWriter;


/**
 * Set of options used by the JDO enhancer and its test programs.
 *
 * @author Martin Zaun
 */
public class JdoMetaOptions
    extends ClassArgOptions
{
    /**
     * The jdo path option.
     */
    public final StringOption jdoPath
        = createStringOption("jdopath", "j",
                             "<path>     : path for lookup of jdo files");

    /**
     * The jdo properties option.
     */
    public final StringOption jdoPropertiesFile
        = createStringOption("properties", null,
                             "<file>  : use property file for JDO metadata");

    /**
     * Creates an instance.
     */
    public JdoMetaOptions(PrintWriter out,
                          PrintWriter err) 
    {
        super(out, err);
    }

    // ----------------------------------------------------------------------

    /**
     * Print a usage message to System.err.
     */
    public void printUsageHeader()
    {
        printlnErr("Usage: <options>.. <arguments>..");
        printlnErr(indent
                   + "JDO metadata options:");
        printlnErr(indent
                   + "  --properties <file> [-j <path>] use property file for JDO metadata");
        printlnErr(indent
                   + "  -j <path>                       lookup .jdo files in the specified path");
        printlnErr(indent
                   + "Source option and arguments:");
        printlnErr(indent
                   + "  -s <path>   <classname>..");
        printlnErr(indent
                   + "              <classfile>..");
        printlnErr(indent
                   + "              <archivefile>..");
    }

    /**
     * Check options and arguments.
     */
    public int check()
    {
        int res;
        if ((res = super.check()) != OK) {
            return res;
        }
        
        // check jdopath option
        if (jdoPropertiesFile.value == null && 
            jdoPath.value == null && archiveFileNames.isEmpty()) {
            printUsageError("No JDO metadata option: specify either properties file or jdo-path for lookup of jdo files");
            return USAGE_ERROR;
        }

        return OK;
    }

    // ----------------------------------------------------------------------

    /**
     * Tests the class.
     */
    static public void main(String[] args)
    {
        final PrintWriter out = new PrintWriter(System.out, true);
        out.println("--> JdoMetaOptions.main()");
        final JdoMetaOptions options = new JdoMetaOptions(out, out);
        out.println("    options.process() ...");
        int res = options.process(args);
        out.println("    return value: " + res);
        out.println("<-- JdoMetaOptions.main()");
    }
}
