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

package org.apache.jdo.exectck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import javax.jdo.JDOException;

public class Utilities {

    Utilities() {
    }
    
    private static final String DATE_FORMAT_NOW = "yyyyMMdd-HHmmss";

    /*
     * Return the current date/time as a String.
     */
    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    /*
     * From an array of URLs, create a classpath String suitable for use
     * as a command line argument
     */
    public static String urls2ClasspathString(List<URL> urls) {
        StringBuilder cp = new StringBuilder();

        for (URL url : urls) {
            cp.append(url.getPath());
            cp.append(File.pathSeparator);
        }
        return cp.toString();
    }

    public static String removeSubstrs(String master, String exclude) {
        String[] deleteThese = exclude.split(" ");
        String filtered = master;
        for (String sub: deleteThese) {
            filtered = filtered.replaceAll(sub.trim(), "");
        }
        return filtered.trim();
    }

    public static void printClasspath(ClassLoader loader) {
        if (loader instanceof URLClassLoader) {
            // Get the URLs
            URL[] urls = ((URLClassLoader) loader).getURLs();
            System.out.println(urls.length + " URL(s) for loader: ");
            for (URL url : urls) {
                if (url != null) {
                    System.out.println("    " + url.getFile());
                }
            }
        }
    }

    public void printClasspath() {

        // Get the System Classloader
        printClasspath(ClassLoader.getSystemClassLoader());

        // Get the System Classloader
        printClasspath(Thread.currentThread().getContextClassLoader());

    }

    static String readFile( String fileName ) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader (fileName));
        String line  = null;
        StringBuffer stringBuf = new StringBuffer();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuf.append( line );
            stringBuf.append( ls );
        }
        return stringBuf.toString();
    }

    InvocationResult invokeTest(List<String> command) {
        return invokeTest(new ProcessBuilder(command));
    }

    InvocationResult invokeTest(List<String> command, File directory) {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(directory);
        return invokeTest(builder);
    }

    private InvocationResult invokeTest(ProcessBuilder builder) {
        InvocationResult result = new InvocationResult();
        try {
            Process proc = builder.start();
            InputStream stdout = proc.getInputStream();
            InputStream stderr = proc.getErrorStream();
            CharBuffer outBuffer = CharBuffer.allocate(1000000);
            CharBuffer errBuffer = CharBuffer.allocate(1000000);
            Thread outputThread = createReaderThread(stdout, outBuffer);
            Thread errorThread = createReaderThread(stderr, errBuffer);
            int exitValue = proc.waitFor();
            result.setExitValue(exitValue);
            errorThread.join(10000); // wait ten seconds to get stderr after process terminates
            outputThread.join(10000); // wait ten seconds to get stdout after process terminates
            result.setErrorString(errBuffer.toString());
            result.setOutputString(outBuffer.toString());
            // wait until the Enhancer command finishes
        } catch (InterruptedException ex) {
            throw new RuntimeException("InterruptedException", ex);
        } catch (IOException ex) {
            throw new RuntimeException("IOException", ex);
        } catch (JDOException jdoex) {
            jdoex.printStackTrace();
            Throwable[] throwables = jdoex.getNestedExceptions();
            System.out.println("Exception throwables of size: " + throwables.length);
            for (Throwable throwable: throwables) {
                throwable.printStackTrace();
            }
        }
        return result;
    }

    private Thread createReaderThread(final InputStream input, final CharBuffer output) {
        final Reader reader = new InputStreamReader(input);
        Thread thread = new Thread(
                new Runnable() {
                    public void run() {
                        int count = 0;
                        int outputBytesRead = 0;
                        try {
                            while (-1 != (outputBytesRead = reader.read(output))) {
                                count += outputBytesRead;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            output.flip();
                        }
                    }
                });
        thread.start();
        return thread;
    }


    class InvocationResult {
        private int exitValue;
        private String errorString;
        private String outputString;

        int getExitValue() {
            return exitValue;
        }

        private void setExitValue(int exitValue) {
            this.exitValue = exitValue;
        }

        private void setErrorString(String errorString) {
            this.errorString = errorString;
        }

        String getErrorString() {
            return errorString;
        }

        private void setOutputString(String outputString) {
            this.outputString = outputString;
        }

        String getOutputString() {
            return outputString;
        }

    }
}
