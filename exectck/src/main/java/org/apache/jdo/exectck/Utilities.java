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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.jdo.JDOException;

public class Utilities {

    Utilities() {
    }
    
    public static final String DATE_FORMAT_NOW = "yyyyMMdd-HHmmss";

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
     *   as a command line argument
     */
    public static String urls2ClasspathString(ArrayList<URL> urls) {
        StringBuffer cp = new StringBuffer();

        for (URL url: urls) {
            cp.append(url.getPath());
            cp.append(File.pathSeparator);
        }
        return cp.toString();
    }

    public static String removeSubstrs(String master, String exclude) {
        String [] deleteThese = exclude.split(" ");
        String filtered = master;
        for (String sub: deleteThese) {
            filtered = filtered.replaceAll(sub.trim(), "");
        }
        return filtered.trim();
    }

    public static void printClasspath(ClassLoader loader) {

        //Get the URLs
        URL[] urls = ((URLClassLoader) loader).getURLs();

        System.out.println(urls.length + " URL(s) for loader: ");
        for (int i = 0; i < urls.length; i++) {
            if (urls[i] != null) {
                System.out.println("    " + urls[i].getFile());
            }
        }
    }

    public void printClasspath() {

        //Get the System Classloader
        ClassLoader loader = ClassLoader.getSystemClassLoader();

        //Get the URLs
        URL[] urls = ((URLClassLoader) loader).getURLs();

        for (int i = 0; i < urls.length; i++) {
            System.out.println("    " + urls[i].getFile());
        }

        //Get the System Classloader
        loader = Thread.currentThread().getContextClassLoader();

        //Get the URLs
        urls = ((URLClassLoader) loader).getURLs();

        for (int i = 0; i < urls.length; i++) {
            System.out.println("    " + urls[i].getFile());
        }

    }

 static String readFile( String fileName ) throws IOException {
    BufferedReader reader = new BufferedReader( new FileReader (fileName));
    String line  = null;
    StringBuffer stringBuf = new StringBuffer();
    String ls = System.getProperty("line.separator");
    while( ( line = reader.readLine() ) != null ) {
        stringBuf.append( line );
        stringBuf.append( ls );
    }
    return stringBuf.toString();
 }

    InvocationResult invokeTest(List command) {
        InvocationResult result = new InvocationResult();
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            Map<String, String> env = builder.environment();
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

        InvocationResult invokeTest(List command, File directory) {
        InvocationResult result = new InvocationResult();
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(directory);
            Map<String, String> env = builder.environment();
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
