/*
 * Copyright 2006 The Apache Software Foundation.
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

package javax.jdo.schema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FilenameFilter;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.JDOFatalException;
import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;

import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * Tests schema files.
 * <p>
 */
public class XMLTest extends AbstractTest {

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
    protected static final String JDOQL_XSD_NS = 
        "http://java.sun.com/xml/ns/jdo/jdoquery";

    /** */
    protected static final File JDO_XSD_FILE = 
        new File(BASEDIR + "/target/classes/javax/jdo/jdo.xsd");

    /** */
    protected static final File ORM_XSD_FILE = 
        new File(BASEDIR + "/target/classes/javax/jdo/orm.xsd");

    /** */
    protected static final File JDOQL_XSD_FILE = 
        new File(BASEDIR + "/target/classes/javax/jdo/jdoquery.xsd");

    /** File prefix */
    protected static final String FILE_PREFIX = BASEDIR + "/test/schema/";

    /** Entity resolver */
    protected static final EntityResolver resolver = new JDOEntityResolver();

    /** Error handler */
    protected static final Handler handler = new Handler();

    /** .xsd files */
    protected static final File[] XSD_FILES = {
        JDO_XSD_FILE, ORM_XSD_FILE, JDOQL_XSD_FILE
    };
    
    /** XSD metadata files. */
    protected static File[] positiveXSDJDO = getFiles("Positive", "-xsd.jdo");
    protected static File[] negativeXSDJDO = getFiles("Negative", "-xsd.jdo");
    protected static File[] positiveXSDORM = getFiles("Positive", "-xsd.orm");
    protected static File[] negativeXSDORM = getFiles("Negative", "-xsd.orm");
    protected static File[] positiveXSDJDOQL = getFiles("Positive", "-xsd.jdoquery");
    protected static File[] negativeXSDJDOQL = getFiles("Negative", "-xsd.jdoquery");

    /** DTD metadata files. */
    protected static File[] positiveDTDJDO = getFiles("Positive", "-dtd.jdo");
    protected static File[] negativeDTDJDO = getFiles("Negative", "-dtd.jdo");
    protected static File[] positiveDTDORM = getFiles("Positive", "-dtd.orm");
    protected static File[] negativeDTDORM = getFiles("Negative", "-dtd.orm");
    protected static File[] positiveDTDJDOQL = getFiles("Positive", "-dtd.jdoquery");
    protected static File[] negativeDTDJDOQL = getFiles("Negative", "-dtd.jdoquery");

    /** Returns array of files of matching file names. */
    protected static File[] getFiles(final String prefix, final String suffix) {
        FilenameFilter filter = new FilenameFilter () {
            public boolean accept(File file, String name) {
                return (name.startsWith(prefix) && name.endsWith(suffix));
            }
        };
        File dir = new File(FILE_PREFIX);
        return dir.listFiles(filter);
    }

    /** */
    public static void main(String args[]) {
        BatchTestRunner.run(XMLTest.class);
    }

    /** Test XSD files jdo.xsd, orm.xsd, and jdoquery.xsd. */
    public void testXSD() throws SAXException, IOException {
        DocumentBuilder builder = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        builder = getParser(factory);
        checkXML(builder, XSD_FILES, true);
        String messages = retrieveMessages();
        if (messages != null) {
            fail(messages);
        }        
    }

    /** Test XSD based .jdo, .orm and .jdoquery files. */
    public void testXSDBased() {
        // create XSD parser
        DocumentBuilder builder = null;
        builder = createBuilder(JDO_XSD_NS + " " + JDO_XSD_FILE.toURI().toString());
        checkXML(builder, positiveXSDJDO, true);
        checkXML(builder, negativeXSDJDO, false);
        builder = createBuilder(ORM_XSD_NS + " " + ORM_XSD_FILE.toURI().toString());
        checkXML(builder, positiveXSDORM, true);
        checkXML(builder, negativeXSDORM, false);
        builder = createBuilder(JDOQL_XSD_NS + " " + JDOQL_XSD_FILE.toURI().toString());
        checkXML(builder, positiveXSDJDOQL, true);
        checkXML(builder, negativeXSDJDOQL, false);
        String messages = retrieveMessages();
        if (messages != null) {
            fail(messages);
        }        
    }

    /** Test DTD based .jdo, .orm and .jdoquery files. */
    public void testDTDBased() {
        // create DTD parser 
        DocumentBuilder builder = createBuilder();
        checkXML(builder, positiveDTDJDO, true);
        checkXML(builder, negativeDTDJDO, false);
        checkXML(builder, positiveDTDORM, true);
        checkXML(builder, negativeDTDORM, false);
        checkXML(builder, positiveDTDJDOQL, true);
        checkXML(builder, negativeDTDJDOQL, false);
        String messages = retrieveMessages();
        if (messages != null) {
            fail(messages);
        }        
    }

    /** Create XSD builder.
     */
    private DocumentBuilder createBuilder(String location) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        factory.setAttribute(SCHEMA_LANGUAGE_PROP, XSD_TYPE);
        factory.setAttribute(SCHEMA_LOCATION_PROP, location);
        return getParser(factory);
    }

    /** Create DTD builder.
     */
    private DocumentBuilder createBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
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

    /** Parse the specified files using the specified builder. The valid
     * parameter determines whether the specified files are valid JDO metadata
     * files. The method does not throw an exception on an error, instead it
     * appends any error message to the global message handler.
     */
    private void checkXML(DocumentBuilder builder, File[] files, boolean valid) {
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            handler.init(file);
            try {
                builder.parse(file);
            } catch (SAXParseException ex) {
                handler.error(ex);
            } catch (Exception ex) {
                throw new JDOFatalException("Fatal error processing " +
                        file.getName(), ex);
            }
            String messages = handler.getMessages();
            if (valid && (messages != null)) {
                appendMessage(messages);
            } else if (!valid && (messages == null)) {
                appendMessage(file.getName() + " is not valid, " +
                              "but the parser did not catch the error.");
            }
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
            "-//Sun Microsystems, Inc.//DTD Java Data Objects Metadata 2.0//EN";
        private static final String RECOGNIZED_JDO_SYSTEM_ID = 
            "file:/javax/jdo/jdo.dtd";
        private static final String RECOGNIZED_JDO_SYSTEM_ID2 = 
            "http://java.sun.com/dtd/jdo_2_0.dtd";
        private static final String RECOGNIZED_ORM_PUBLIC_ID = 
            "-//Sun Microsystems, Inc.//DTD Java Data Objects Mapping Metadata 2.0//EN";
        private static final String RECOGNIZED_ORM_SYSTEM_ID = 
            "file:/javax/jdo/orm.dtd";
        private static final String RECOGNIZED_ORM_SYSTEM_ID2 = 
            "http://java.sun.com/dtd/orm_2_0.dtd";
        private static final String RECOGNIZED_JDOQUERY_PUBLIC_ID = 
            "-//Sun Microsystems, Inc.//DTD Java Data Objects Query Metadata 2.0//EN";
        private static final String RECOGNIZED_JDOQUERY_SYSTEM_ID = 
            "file:/javax/jdo/jdoquery.dtd";
        private static final String RECOGNIZED_JDOQUERY_SYSTEM_ID2 = 
            "http://java.sun.com/dtd/jdoquery_2_0.dtd";
        private static final String JDO_DTD_FILENAME = 
            "javax/jdo/jdo.dtd";
        private static final String ORM_DTD_FILENAME = 
            "javax/jdo/orm.dtd";
        private static final String JDOQUERY_DTD_FILENAME = 
            "javax/jdo/jdoquery.dtd";

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
                        // TDB: error handling + I18N
                        throw new JDOFatalException("Cannot load " +
                            finalName + 
                            ", because the file does not exist in the jdo.jar file, " +
                            "or the JDOParser class is not granted permission to read this file.  " +
                            "The metadata .xml file contained PUBLIC=" + publicId +
                            " SYSTEM=" + systemId + ".");
                    }
                return new InputSource(new InputStreamReader(stream));
            }
        }
    }
}

