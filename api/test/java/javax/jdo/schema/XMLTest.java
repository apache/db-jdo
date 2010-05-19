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

package javax.jdo.schema;

import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;

import java.util.Arrays;
import java.util.List;

import javax.jdo.JDOFatalException;
import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;
import javax.jdo.util.XMLTestUtil;

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

    /** File prefix */
    protected static final String FILE_PREFIX = BASEDIR + "/test/schema/";

    /** */
    protected static final File JDO_XSD_FILE = 
        new File(BASEDIR + "/target/classes/javax/jdo/jdo_2_2.xsd");

    /** */
    protected static final File ORM_XSD_FILE = 
        new File(BASEDIR + "/target/classes/javax/jdo/orm_2_2.xsd");

    /** */
    protected static final File JDOQUERY_XSD_FILE = 
        new File(BASEDIR + "/target/classes/javax/jdo/jdoquery_2_2.xsd");

    /** .xsd files */
    protected static final File[] XSD_FILES = 
        new File[] {JDO_XSD_FILE, ORM_XSD_FILE, JDOQUERY_XSD_FILE};
    
    /** XSD metadata files. */
    protected static File[] positiveXSDJDO = getFiles("Positive", "-xsd.jdo");
    protected static File[] negativeXSDJDO = getFiles("Negative", "-xsd.jdo");
    protected static File[] positiveXSDORM = getFiles("Positive", "-xsd.orm");
    protected static File[] negativeXSDORM = getFiles("Negative", "-xsd.orm");
    protected static File[] positiveXSDJDOQUERY = getFiles("Positive", "-xsd.jdoquery");
    protected static File[] negativeXSDJDOQUERY = getFiles("Negative", "-xsd.jdoquery");
    
    /** DTD metadata files. */
    protected static File[] positiveDTDJDO = getFiles("Positive", "-dtd.jdo");
    protected static File[] negativeDTDJDO = getFiles("Negative", "-dtd.jdo");
    protected static File[] positiveDTDORM = getFiles("Positive", "-dtd.orm");
    protected static File[] negativeDTDORM = getFiles("Negative", "-dtd.orm");
    protected static File[] positiveDTDJDOQUERY = getFiles("Positive", "-dtd.jdoquery");
    protected static File[] negativeDTDJDOQUERY = getFiles("Negative", "-dtd.jdoquery");
    
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
    public void testXSD() {
        XMLTestUtil util = new XMLTestUtil();
        appendMessage(util.checkXMLNonValidating(XSD_FILES));
        failOnError();
    }

    /** Test XSD based .jdo, .orm and .jdoquery files. */
    public void testXSDBased() {
        XMLTestUtil util = new XMLTestUtil();
        appendMessage(util.checkXML(positiveXSDJDO, true));
        appendMessage(util.checkXML(negativeXSDJDO, false));
        appendMessage(util.checkXML(positiveXSDORM, true));
        appendMessage(util.checkXML(negativeXSDORM, false));
        appendMessage(util.checkXML(positiveXSDJDOQUERY, true));
        appendMessage(util.checkXML(negativeXSDJDOQUERY, false));
        failOnError();
    }

    /** Test DTD based .jdo, .orm and .jdoquery files. */
    public void testDTDBased() {
        XMLTestUtil util = new XMLTestUtil();
        appendMessage(util.checkXML(positiveDTDJDO, true));
        appendMessage(util.checkXML(negativeDTDJDO, false));
        appendMessage(util.checkXML(positiveDTDORM, true));
        appendMessage(util.checkXML(negativeDTDORM, false));
        appendMessage(util.checkXML(positiveDTDJDOQUERY, true));
        appendMessage(util.checkXML(negativeDTDJDOQUERY, false));
        failOnError();
    }

}

