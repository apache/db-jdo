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

package javax.jdo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileFilter;
import java.io.FilenameFilter;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.jdo.JDOFatalException;

import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * Tests schema files.
 * <p>
 */
public class XMLTestUtil {

    /** */
    protected static String BASEDIR = System.getProperty("basedir", ".");

    /** "http://www.w3.org/2001/XMLSchema" */
    protected static final String XSD_TYPE = 
        "http://www.w3.org/2001/XMLSchema";

    /** */
    protected static final String SCHEMA_LANGUAGE_PROP = 
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /** */
    protected static final String SCHEMA_LOCATION_PROP =
        "http://apache.org/xml/properties/schema/external-schemaLocation";
    
    /** jdo namespace */
    protected static final String JDO_XSD_NS = 
        "http://java.sun.com/xml/ns/jdo/jdo";

    /** orm namespace */
    protected static final String ORM_XSD_NS = 
        "http://java.sun.com/xml/ns/jdo/orm";

    /** jdoquery namespace */
    protected static final String JDOQUERY_XSD_NS = 
        "http://java.sun.com/xml/ns/jdo/jdoquery";

    /** jdo xsd file */
    protected static final File JDO_XSD_FILE = 
        new File(BASEDIR + "/target/classes/javax/jdo/jdo_2_2.xsd");

    /** orm xsd file */
    protected static final File ORM_XSD_FILE = 
        new File(BASEDIR + "/target/classes/javax/jdo/orm_2_2.xsd");

    /** jdoquery xsd file */
    protected static final File JDOQUERY_XSD_FILE = 
        new File(BASEDIR + "/target/classes/javax/jdo/jdoquery_2_2.xsd");

    /** Entity resolver */
    protected static final EntityResolver resolver = new JDOEntityResolver();

    /** Error handler */
    protected static final Handler handler = new Handler();

    /** Name of the metadata property, a comma separated list of JDO metadata
     * file or directories containing such files. */
    protected static String METADATA_PROP = "javax.jdo.metadata";

    /** Name of the recursive property, allowing recursive search of metadata
     * files. */
    protected static String RECURSIVE_PROP = "javax.jdo.recursive";
    
    /** Separator character for the metadata property. */
    protected static final String DELIM = ",;";

    /** Newline. */
    protected static final String NL = System.getProperty("line.separator");

    /** XSD builder for jdo namespace. */
    private final DocumentBuilder jdoXsdBuilder = 
        createBuilder(JDO_XSD_NS + " " + JDO_XSD_FILE.toURI().toString());
    
    /** XSD builder for orm namespace. */
    private final DocumentBuilder ormXsdBuilder = 
        createBuilder(ORM_XSD_NS + " " + ORM_XSD_FILE.toURI().toString());
    
    /** XSD builder for jdoquery namespace. */
    private final DocumentBuilder jdoqueryXsdBuilder = 
        createBuilder(JDOQUERY_XSD_NS + " " + JDOQUERY_XSD_FILE.toURI().toString());
    
    /** DTD builder. */
    private final DocumentBuilder dtdBuilder = createBuilder(true);
    
    /** Non validating builder. */
    private final DocumentBuilder nonValidatingBuilder = createBuilder(false);

    /** Create XSD builder. */
    private DocumentBuilder createBuilder(String location) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        factory.setAttribute(SCHEMA_LANGUAGE_PROP, XSD_TYPE);
        factory.setAttribute(SCHEMA_LOCATION_PROP, location);
        return getParser(factory);
    }

    /** Create builder. */
    private DocumentBuilder createBuilder(boolean validating) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(validating);
        factory.setNamespaceAware(true);
        return getParser(factory);
    }

    /** Returns a parser obtained from specified factroy. */
    private DocumentBuilder getParser(DocumentBuilderFactory factory) {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(resolver);
            builder.setErrorHandler(handler);
            return builder;
        } catch (ParserConfigurationException ex) {
            throw new JDOFatalException("Cannot create XML parser", ex);
        }
    }

    /** Parse the specified files. The valid parameter determines whether the
     * specified files are valid JDO metadata files. The method does not throw
     * an exception on an error, instead it instead it returns the error
     * message(s) as string. 
     */ 
    public String checkXML(File[] files, boolean valid) {
        StringBuffer messages = new StringBuffer();
        for (int i = 0; i < files.length; i++) {
            String msg = checkXML(files[i], valid);
            if (msg != null) {
                messages.append(msg);
            }
        }
        return (messages.length() == 0) ? null : messages.toString();
    }
    
    /** Parse the specified files using a non validating parser. The method
     * does not throw an exception on an error, instead it instead it returns
     * the error message(s) as string.
     */ 
    public String checkXMLNonValidating(File[] files) {
        StringBuffer messages = new StringBuffer();
        for (int i = 0; i < files.length; i++) {
            String msg = checkXML(nonValidatingBuilder, files[i], true);
            if (msg != null) {
                messages.append(msg);
            }
        }
        return (messages.length() == 0) ? null : messages.toString();
    }
    
     /** Parse the specified file. The method checks whether it is a XSD or
     * DTD base file and parses the file using a builder according to the file
     * name suffix. The valid parameter determines whether the specified files
     * are valid JDO metadata files. The method does not throw an exception on
     * an error, instead it returns the error message(s) as string. 
     */
    private String checkXML(File file, boolean valid) {
        String messages = null;
        String fileName = file.getName();
        try {
            if (isDTDBased(file)) {
                messages = checkXML(dtdBuilder, file, valid);
            } else if (fileName.endsWith(".jdo")) {
                messages = checkXML(jdoXsdBuilder, file, valid);
            } else if (fileName.endsWith(".orm")) {
                messages = checkXML(ormXsdBuilder, file, valid);
            } else if (fileName.endsWith(".jdoquery")) {
                messages = checkXML(jdoqueryXsdBuilder, file, valid);
            }
        } catch (SAXException ex) {
            messages = ex.getMessage();
        }
        return messages;
    }

    /** Parse the specified file using the specified builder. The valid
     * parameter determines whether the specified files are valid JDO metadata
     * files. The method does not throw an exception on an error, instead it
     * returns the error message(s) as string.
     */
    private String checkXML(DocumentBuilder builder, File file, boolean valid) {
        String messages = null;
        handler.init(file);
        try {
            builder.parse(file);
        } catch (SAXParseException ex) {
            handler.error(ex);
        } catch (Exception ex) {
            messages = "Fatal error processing " + file.getName() + ":  " + ex + NL;
        }
        if (messages == null) {
            messages = handler.getMessages();
        }
        if (!valid) {
            if (messages != null) {
                // expected error for negative test
                messages = null;
            } else {
                messages = file.getName() + " is not valid, " +
                    "but the parser did not catch the error.";
            } 
        }
        return messages;
    }

    /** Checks whether the specifeid file is DTD or XSD based. The method
     * throws a SAXException if the file has syntax errors. */
    private boolean isDTDBased(File file) throws SAXException {
        handler.init(file);
        try {
            Document document = nonValidatingBuilder.parse(file);
            return document.getDoctype() != null;
        } catch (SAXParseException ex) {
            handler.error(ex);
            throw new SAXException(handler.getMessages());
        } catch (Exception ex) {
            throw new SAXException(
                "Fatal error processing " + file.getName() + ":  " + ex);
        }
    }
    
    /** ErrorHandler implementation. */
    private static class Handler implements ErrorHandler {

        private File fileUnderTest;
        private String[] lines;
        private StringBuffer messages;

        public void error(SAXParseException ex) {
            append("Handler.error: ", ex);
        }
            
        public void fatalError(SAXParseException ex) {
            append("Handler.fatalError: ", ex);
        }
        
        public void warning(SAXParseException ex) {
            append("Handler.warning: ", ex);
        }
        
        public void init(File file) {
            this.fileUnderTest = file;
            this.messages = new StringBuffer();
            this.lines = null;
        }

        public String getMessages() {
            return (messages.length() == 0) ? null : messages.toString();
        }

        private void append(String prefix, SAXParseException ex) {
            int lineNumber = ex.getLineNumber();
            int columnNumber = ex.getColumnNumber();
            messages.append("------------------------").append(NL);
            messages.append(prefix).append(fileUnderTest.getName());
            messages.append(" [line=").append(lineNumber);
            messages.append(", col=").append(columnNumber).append("]: ");
            messages.append(ex.getMessage()).append(NL);
            messages.append(getErrorLocation(lineNumber, columnNumber));
        }

        private String[] getLines() {
            if (lines == null) {
                try {
                    BufferedReader bufferedReader =
                        new BufferedReader(new FileReader(fileUnderTest));
                    ArrayList tmp = new ArrayList();
                    while (bufferedReader.ready()) {
                        tmp.add(bufferedReader.readLine());
                    }
                    lines = (String[])tmp.toArray(new String[tmp.size()]);
                } catch (IOException ex) {
                    throw new JDOFatalException("getLines: caught IOException", ex);
                }
            }
            return lines;
        }
        
        /** Return the error location for the file under test.
         */
        private String getErrorLocation(int lineNumber, int columnNumber) {
            String[] lines = getLines();
            int length = lines.length;
            if (lineNumber > length) {
                return "Line number " + lineNumber +
                    " exceeds the number of lines in the file (" +
                    lines.length + ")";
            } else if (lineNumber < 1) {
                return "Line number " + lineNumber +
                    " does not allow retriving the error location.";
            }
            StringBuffer buf = new StringBuffer();
            if (lineNumber > 2) {
                buf.append(lines[lineNumber-3]);
                buf.append(NL);
                buf.append(lines[lineNumber-2]);
                buf.append(NL);
            }
            buf.append(lines[lineNumber-1]);
            buf.append(NL);
            for (int i = 1; i < columnNumber; ++i) {
                buf.append(' ');
            }
            buf.append("^\n");
            if (lineNumber + 1 < length) {
                buf.append(lines[lineNumber]);
                buf.append(NL);
                buf.append(lines[lineNumber+1]);
                buf.append(NL);
            }
            return buf.toString();
        }
    }

    /** Implementation of EntityResolver interface to check the jdo.dtd location
     **/
    private static class JDOEntityResolver 
        implements EntityResolver {

        private static final String RECOGNIZED_JDO_PUBLIC_ID = 
            "-//Sun Microsystems, Inc.//DTD Java Data Objects Metadata 2.2//EN";
        private static final String RECOGNIZED_JDO_SYSTEM_ID = 
            "file:/javax/jdo/jdo_2_2.dtd";
        private static final String RECOGNIZED_JDO_SYSTEM_ID2 = 
            "http://java.sun.com/dtd/jdo_2_2.dtd";
        private static final String RECOGNIZED_ORM_PUBLIC_ID = 
            "-//Sun Microsystems, Inc.//DTD Java Data Objects Mapping Metadata 2.2//EN";
        private static final String RECOGNIZED_ORM_SYSTEM_ID = 
            "file:/javax/jdo/orm_2_2.dtd";
        private static final String RECOGNIZED_ORM_SYSTEM_ID2 = 
            "http://java.sun.com/dtd/orm_2_2.dtd";
        private static final String RECOGNIZED_JDOQUERY_PUBLIC_ID = 
            "-//Sun Microsystems, Inc.//DTD Java Data Objects Query Metadata 2.2//EN";
        private static final String RECOGNIZED_JDOQUERY_SYSTEM_ID = 
            "file:/javax/jdo/jdoquery_2_2.dtd";
        private static final String RECOGNIZED_JDOQUERY_SYSTEM_ID2 = 
            "http://java.sun.com/dtd/jdoquery_2_2.dtd";
        private static final String JDO_DTD_FILENAME = 
            "javax/jdo/jdo_2_2.dtd";
        private static final String ORM_DTD_FILENAME = 
            "javax/jdo/orm_2_2.dtd";
        private static final String JDOQUERY_DTD_FILENAME = 
            "javax/jdo/jdoquery_2_2.dtd";

        static Map publicIds = new HashMap();
        static Map systemIds = new HashMap();
        static {
            publicIds.put(RECOGNIZED_JDO_PUBLIC_ID, JDO_DTD_FILENAME);
            publicIds.put(RECOGNIZED_ORM_PUBLIC_ID, ORM_DTD_FILENAME);
            publicIds.put(RECOGNIZED_JDOQUERY_PUBLIC_ID, JDOQUERY_DTD_FILENAME);
            systemIds.put(RECOGNIZED_JDO_SYSTEM_ID, JDO_DTD_FILENAME);
            systemIds.put(RECOGNIZED_ORM_SYSTEM_ID, ORM_DTD_FILENAME);
            systemIds.put(RECOGNIZED_JDOQUERY_SYSTEM_ID, JDOQUERY_DTD_FILENAME);
            systemIds.put(RECOGNIZED_JDO_SYSTEM_ID2, JDO_DTD_FILENAME);
            systemIds.put(RECOGNIZED_ORM_SYSTEM_ID2, ORM_DTD_FILENAME);
            systemIds.put(RECOGNIZED_JDOQUERY_SYSTEM_ID2, JDOQUERY_DTD_FILENAME);
        }
        public InputSource resolveEntity(String publicId, final String systemId)
            throws SAXException, IOException 
        {
            // check for recognized ids
            String filename = (String)publicIds.get(publicId);
            if (filename == null) {
                filename = (String)systemIds.get(systemId);
            }
            final String finalName = filename;
            if (finalName == null) {
                return null;
            } else {
                // Substitute the dtd with the one from javax.jdo.jdo.dtd,
                // but only if the publicId is equal to RECOGNIZED_PUBLIC_ID
                // or there is no publicID and the systemID is equal to
                // RECOGNIZED_SYSTEM_ID. 
                    InputStream stream = (InputStream) AccessController.doPrivileged (
                        new PrivilegedAction () {
                            public Object run () {
                            return getClass().getClassLoader().
                                getResourceAsStream(finalName);
                            }
                         }
                     );
                    if (stream == null) {
                        throw new JDOFatalException("Cannot load " + finalName + 
                            ", because the file does not exist in the jdo.jar file, " +
                            "or the JDOParser class is not granted permission to read this file.  " +
                            "The metadata .xml file contained PUBLIC=" + publicId +
                            " SYSTEM=" + systemId + ".");
                    }
                return new InputSource(new InputStreamReader(stream));
            }
        }
    }

    /** Helper class to find all test JDO metadata files. */
    public static class XMLFinder {

        private List metadataFiles = new ArrayList();
        private final boolean recursive;
        
        /** Constructor. */
        public XMLFinder(String[] fileNames, boolean recursive) {
            this.recursive = recursive;
            if (fileNames == null) return;
            for (int i = 0; i < fileNames.length; i++) {
                appendTestFiles(fileNames[i]);
            }
        }
        
        /** Returns array of files of matching file names. */
        private File[] getFiles(File dir, final String suffix) {
            FilenameFilter filter = new FilenameFilter() {
                    public boolean accept(File file, String name) {
                        return name.endsWith(suffix);
                    }
                };
            return dir.listFiles(filter);
        }

        /** */
        private File[] getDirectories(File dir) {
            FileFilter filter = new FileFilter() {
                    public boolean accept(File pathname) {
                        return pathname.isDirectory();
                    }
                };
            return dir.listFiles(filter);
        }

        /** */
        private void appendTestFiles(String fileName) {
            File file = new File(fileName);
            if (file.isDirectory()) {
                processDirectory(file);
            } else if (fileName.endsWith(".jdo") || 
                       fileName.endsWith(".orm") ||
                       fileName.endsWith(".jdoquery")) {
                metadataFiles.add(new File(fileName));
            }
        }

        /** Adds all files with suffix .jdo, .orm and .jdoquery to the list of
         * metadata files. Recursively process subdirectories if recursive
         * flag is set. */
        private void processDirectory(File dir) {
            metadataFiles.addAll(Arrays.asList(getFiles(dir, ".jdo")));
            metadataFiles.addAll(Arrays.asList(getFiles(dir, ".orm")));
            metadataFiles.addAll(Arrays.asList(getFiles(dir, ".jdoquery")));
            if (recursive) {
                File[] subdirs = getDirectories(dir);
                for (int i = 0; i < subdirs.length; i++) {
                    processDirectory(subdirs[i]);
                }
            }
        }

        /** Returns an array of test files with suffix .jdo, .orm or .jdoquery. */
        public File[] getMetadataFiles() {
            return (File[])metadataFiles.toArray(new File[metadataFiles.size()]);
        }

    }

    /** */
    private static String[] checkMetadataSystemProperty() {
        String[] ret = null;
        String metadata = System.getProperty(METADATA_PROP);
        if ((metadata != null) && (metadata.length() > 0)) {
            List entries = new ArrayList();
            StringTokenizer st = new StringTokenizer(metadata, DELIM);
            while (st.hasMoreTokens()) {
                entries.add(st.nextToken());
            }
            ret = (String[])entries.toArray(new String[entries.size()]);
        }
        return ret;
    }

    /**
     * Command line tool to test JDO metadata files. 
     * Usage: XMLTestUtil [-r] <file or directory>+
     */
    public static void main(String args[]) {
        String[] fromProp = checkMetadataSystemProperty();
        boolean recursive = Boolean.getBoolean(RECURSIVE_PROP);

        // handle command line args
        String[] fileNames = null;
        if ((args.length > 0) && ("-r".equals(args[0]))) {
            recursive = true;
            fileNames = new String[args.length - 1];
            System.arraycopy(args, 1, fileNames, 0, args.length - 1);
        } else {
            fileNames = args;
        }
        
        // check args
        if ((fileNames.length == 0) && (fromProp == null)) {
            System.err.println(
                "No commandline arguments and system property metadata not defined; " + 
                "nothing to be tested.\nUsage: XMLTestUtil [-r] <directories>\n" + 
                "\tAll .jdo, .orm, and .jdoquery files in the directory (recursively) will be tested.");
        } else if ((fileNames.length == 0) && (fromProp != null)) {
            // use metadata system property
            fileNames = fromProp;
        } else if ((fileNames.length != 0) && (fromProp != null)) {
            System.err.println(
                "Commandline arguments specified and system property metadata defined; " +
                "ignoring system property metadata.");
        }

        // run the test
        XMLTestUtil xmlTest = new XMLTestUtil();
        File[] files = new XMLFinder(fileNames, recursive).getMetadataFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            System.out.print("Checking " + file.getPath() + ": ");
            String messages = xmlTest.checkXML(file, true);
            messages = (messages == null) ?  "OK" : NL + messages;
            System.out.println(messages);
        }
    }
}

