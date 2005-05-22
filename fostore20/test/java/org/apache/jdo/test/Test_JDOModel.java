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

package org.apache.jdo.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;

import javax.jdo.JDOFatalInternalException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.jdo.impl.model.java.runtime.RuntimeJavaModelFactory;
import org.apache.jdo.impl.model.jdo.util.PrintSupport;
import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.model.jdo.JDOIdentityType;
import org.apache.jdo.model.jdo.JDOModel;
import org.apache.jdo.model.jdo.NullValueTreatment;
import org.apache.jdo.model.jdo.PersistenceModifier;
import org.apache.jdo.pc.PCArrays;
import org.apache.jdo.pc.appid.PCRect;
import org.apache.jdo.pc.empdept.PCFullTimeEmployee;
import org.apache.jdo.pc.serializable.PCSub3;
import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This is a very simple test of the JDOModel.
 * 
 * @author Dave Bristor
 */
public class Test_JDOModel extends AbstractTest {
    
    /** RuntimeJavaModelFactory. */
    private RuntimeJavaModelFactory javaModelFactory;
    private JDOModel jdoModel;
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_JDOModel.class);
    }

    /** */
    protected void setUp() {
        javaModelFactory = (RuntimeJavaModelFactory) AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return RuntimeJavaModelFactory.getInstance();
                }});
        JavaModel javaModel =
            javaModelFactory.getJavaModel(this.getClass().getClassLoader());
        jdoModel = javaModel.getJDOModel();
    }
    
    /** */
    protected void tearDown() { }

    /** */
    public void testXML() throws Exception {
        DocumentBuilderFactory domParserFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder domParser = domParserFactory.newDocumentBuilder();
        domParser.setEntityResolver(new JDOEntityResolver());

        // package.jdo in org.apache.jdo.pc
        handleJDOFile(getResourceAsStream(PCArrays.class, "package.jdo"), domParser);
        // package.jdo in org.apache.jdo.pc.empdept
        handleJDOFile(getResourceAsStream(PCFullTimeEmployee.class, "package.jdo"), domParser);
        // package.jdo in org.apache.jdo.pc.serializable
        handleJDOFile(getResourceAsStream(PCSub3.class, "package.jdo"), domParser);
        // package.jdo in org.apache.jdo.pc.appid
        handleJDOFile(getResourceAsStream(PCRect.class, "package.jdo"), domParser);
        // Employee.jdo in org.apache.jdo.pc.xempdept
        handleJDOFile(getResourceAsStream(Employee.class, "Employee.jdo"), domParser);
    }

    /** 
     * Tests the JDOModel properties of selected pc classes to make sure
     * that the default calulation of the JDOModel is correct.
     */
    public void testDefaults() {
        verifyPCFullTimeEmployee();
        verifyPCSub3();
    }

    /** */
    protected void handleJDOFile(InputStream is, DocumentBuilder domParser)
        throws Exception {
        Document dom = domParser.parse(is);
        String packageName = getPackageName(dom);
        handleClassElements(dom.getElementsByTagName("class"), packageName);
    }

    /** */
    protected String getPackageName(Document dom) {
        NodeList packageElements = dom.getElementsByTagName("package");
        int length = packageElements.getLength();
        if (length != 1)
            fail("Multiple package nodes");
        Node packageElement = packageElements.item(0);
        String packageName =
            packageElement.getAttributes().getNamedItem("name").getNodeValue();
        if (debug) logger.debug("packageName: " + packageName);
        return packageName;
    }

    /** */
    protected void handleClassElements(NodeList classElements,
                                       String packageName) {
        for (int i = 0; i < classElements.getLength(); i++) {
            handleClassElement((Element)classElements.item(i), packageName);
        }
    }
    
    /** */
    protected void handleClassElement(Element classElement, String packageName) {
        String fullClassName = packageName + '.' + classElement.getAttribute("name");
        if (debug) logger.debug("class: " + fullClassName);
        JDOClass jdoClass = jdoModel.getJDOClass(fullClassName);
        assertNotNull("No JDOClass instance for class " + fullClassName, jdoClass);

        // handle identity type attribute
        if (classElement.hasAttribute("identity-type")) {
            String identityType = classElement.getAttribute("identity-type");
            if (debug) logger.debug("  identity-type: " + identityType);
            assertEquals("Wrong identity-type of class " + fullClassName, 
                         identityType, 
                         JDOIdentityType.toString(jdoClass.getIdentityType()));
        }

        // handle objectid-class attribute
        if (classElement.hasAttribute("objectid-class")) {
            String objectIdClass = classElement.getAttribute("objectid-class");
            if (debug) logger.debug("  objectid-class: " + objectIdClass);
            assertEquals("Wrong objectid-class of class " + fullClassName, 
                         objectIdClass, 
                         jdoClass.getDeclaredObjectIdClassName());
        }

        // handle requires-extent attribute
        if (classElement.hasAttribute("requires-extent")) {
            String requiresExtent = classElement.getAttribute("requires-extent");
            if (debug) logger.debug("  requires-extent: " + requiresExtent);
            assertEquals("Wrong requires-extent of class " + fullClassName, 
                         Boolean.valueOf(requiresExtent).booleanValue(), 
                         jdoClass.requiresExtent());
        }

        // handle persistence-capable-superclass attribute
        if (classElement.hasAttribute("persistence-capable-superclass")) {
            String pcSuperClass =
                classElement.getAttribute("persistence-capable-superclass");
            if (debug)
                logger.debug("  persistence-capable-superclass: " + pcSuperClass);
            // Note, this code assumes the persistence-capable-superclass
            // attribute is either a fully qualified class name or a class
            // in the same package as the pc class!
            assertEquals("Wrong persistence-capable-superclass of class " +
                         fullClassName, 
                         getQualifiedClassName(pcSuperClass, packageName), 
                         jdoClass.getPersistenceCapableSuperclassName());
        }
            
        // handle field subelements
        handleFieldElements(classElement.getElementsByTagName("field"), jdoClass);
    }

    /** */
    protected void handleFieldElements(NodeList fields, JDOClass jdoClass) {
        for (int i = 0; i < fields.getLength(); i++) {
            handleFieldElement((Element)fields.item(i), jdoClass);
        }
    }
    
    /** */
    protected void handleFieldElement(Element fieldElement, JDOClass jdoClass) {
        String fieldName = fieldElement.getAttribute("name");
        String className = jdoClass.getName();
        if (debug) logger.debug("  field: " + fieldName);
        JDOField jdoField = jdoClass.getField(fieldName);
        assertNotNull("No JDOField instance for field " + fieldName +
                      " of class " + className, jdoField);

        // handle persistence-modifier attribute
        if (fieldElement.hasAttribute("persistence-modifier")) {
            String persistenceModifier =
                fieldElement.getAttribute("persistence-modifier");
            if (debug)
                logger.debug("    persistence-modifier: " + persistenceModifier);
            assertEquals("Wrong persistence-modifier of field " + fieldName +
                         " of class " + className, 
                         persistenceModifier, 
                         PersistenceModifier.toString(jdoField.getPersistenceModifier()));
        }
        
        // handle primary-key attribute
        if (fieldElement.hasAttribute("primary-key")) {
            String primaryKey = fieldElement.getAttribute("primary-key");
            if (debug) logger.debug("    primary-key: " + primaryKey);
            assertEquals("Wrong primary-key of field " + fieldName + " of class " + className, 
                         Boolean.valueOf(primaryKey).booleanValue(), 
                         jdoField.isPrimaryKey());
        }

        // handle null-value attribute 
        if (fieldElement.hasAttribute("null-value")) {
            String nullValue = fieldElement.getAttribute("null-value");
            if (debug) logger.debug("    null-value: " + nullValue);
            assertEquals("Wrong null-value of field " + fieldName +
                         " of class " + className, 
                         nullValue, 
                         NullValueTreatment.toString(jdoField.getNullValueTreatment()));
        }

        // handle default-fetch-group attribute 
        if (fieldElement.hasAttribute("default-fetch-group")) {
            String dfg = fieldElement.getAttribute("default-fetch-group");
            if (debug) logger.debug("    default-fetch-group: " + dfg);
            assertEquals("Wrong default-fetch-group of field " + fieldName +
                         " of class " + className, 
                         Boolean.valueOf(dfg).booleanValue(), 
                         jdoField.isDefaultFetchGroup());
        }

        // handle embedded attribute 
        if (fieldElement.hasAttribute("embedded")) {
            String embedded = fieldElement.getAttribute("embedded");
            if (debug) logger.debug("    embedded: " + embedded);
            assertEquals("Wrong embedded of field " + fieldName +
                         " of class " + className, 
                         Boolean.valueOf(embedded).booleanValue(), 
                         jdoField.isEmbedded());
        }
        
    }

    /** 
     * Verify metadata for org.apache.jdo.pc.empdept.PCFullTimeEmployee.
     */ 
    protected void verifyPCFullTimeEmployee() {
        Class clazz = PCFullTimeEmployee.class;
        String className = clazz.getName();
        JDOClass jdoClass = javaModelFactory.getJavaType(clazz).getJDOClass();
        assertNotNull("No JDOClass instance for class " + className, jdoClass);
        assertEquals("Wrong identity type of class " + className, 
                     JDOIdentityType.DATASTORE, 
                     jdoClass.getIdentityType());
        assertEquals("Wrong pc super class of class " + className, 
                     "org.apache.jdo.pc.empdept.PCEmployee", 
                     jdoClass.getPersistenceCapableSuperclassName());
        assertEquals("Wrong number of declared managed fields of class " + className, 
                     1, 
                     jdoClass.getDeclaredManagedFieldCount());
        assertEquals("Wrong number of inherited managed fields of class " + className, 
                     10, 
                     jdoClass.getInheritedManagedFieldCount());
        assertEquals("Wrong list of managed fields of class " + className, 
                     "[birthdate, firstname, lastname, department, empid, employees, hiredate, insurance, manager, projects, salary]",
                     Arrays.asList(jdoClass.getManagedFields()).toString());
        assertEquals("Wrong list of default fetch group fields of class " + className, 
                     "[birthdate, firstname, lastname, empid, hiredate, salary]",
                     Arrays.asList(jdoClass.getDefaultFetchGroupFields()).toString());
        assertEquals("Wrong list of  managedFieldNumbers of class " + className, 
                     "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                     PrintSupport.asList(jdoClass.getManagedFieldNumbers()).toString());
        assertEquals("Wrong list of  persistentFieldNumbers of class " + className, 
                     "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                     PrintSupport.asList(jdoClass.getPersistentFieldNumbers()).toString());
        assertEquals("Wrong list of primaryKeyFieldNumbers of class " + className, 
                     "[]",
                     PrintSupport.asList(jdoClass.getPrimaryKeyFieldNumbers()).toString());
        assertEquals("Wrong list of persistentNonPKFieldNs of class " + className, 
                     "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                     PrintSupport.asList(jdoClass.getPersistentNonPrimaryKeyFieldNumbers()).toString());
        assertEquals("Wrong list of persistentRelshipFieldNumbers of class " + className, 
                     "[3, 5, 7, 8, 9]",
                     PrintSupport.asList(jdoClass.getPersistentRelationshipFieldNumbers()).toString());
        assertEquals("Wrong list of persistentSerializableFieldNumbers of class " + className, 
                     "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                     PrintSupport.asList(jdoClass.getPersistentSerializableFieldNumbers()).toString());

        JDOField[] jdoFields = jdoClass.getDeclaredFields();
        assertEquals("Wrong number of declared fields of class" + className,
                     1,
                     jdoFields.length);
        JDOField jdoField = jdoFields[0];
        String fieldName = jdoField.getName();
        assertEquals("Wrong name of field of class " + className,
                     "salary",
                     fieldName);
        assertEquals("Wrong type of field " + fieldName + " of class " + className,
                     "double",
                     jdoField.getType().toString());
        assertEquals("Wrong field number of field " + fieldName + " of class " + className,
                     10,
                     jdoField.getFieldNumber());
    }
    
    
    /** 
     * Verify metadata for org.apache.jdo.pc.serializable.PCSub3. 
     * It explicitly defines the dfg attribute.
     */
    protected void verifyPCSub3() {
        Class clazz = PCSub3.class;
        String className = clazz.getName();
        JDOClass jdoClass = javaModelFactory.getJavaType(clazz).getJDOClass();
        assertNotNull("No JDOClass instance for class " + className, jdoClass);
        assertEquals("Wrong identity type of class " + className, 
                     JDOIdentityType.DATASTORE, 
                     jdoClass.getIdentityType());
        assertEquals("Wrong pc super class of class " + className, 
                     "org.apache.jdo.pc.serializable.PCSuper", 
                     jdoClass.getPersistenceCapableSuperclassName());
        assertEquals("Wrong number of declared managed fields of class " + className, 
                     2, 
                     jdoClass.getDeclaredManagedFieldCount());
        assertEquals("Wrong number of inherited managed fields of class " + className, 
                     2, 
                     jdoClass.getInheritedManagedFieldCount());
        assertEquals("Wrong list of managed fields of class " + className, 
                     "[s01, t02, s03, t04]",
                     Arrays.asList(jdoClass.getManagedFields()).toString());
        assertEquals("Wrong list of  managedFieldNumbers of class " + className, 
                     "[0, 1, 2, 3]",
                     PrintSupport.asList(jdoClass.getManagedFieldNumbers()).toString());
        assertEquals("Wrong list of  persistentFieldNumbers of class " + className, 
                     "[0, 1, 2, 3]",
                     PrintSupport.asList(jdoClass.getPersistentFieldNumbers()).toString());
        assertEquals("Wrong list of primaryKeyFieldNumbers of class " + className, 
                     "[]",
                     PrintSupport.asList(jdoClass.getPrimaryKeyFieldNumbers()).toString());
        assertEquals("Wrong list of persistentNonPKFieldNs of class " + className, 
                     "[0, 1, 2, 3]",
                     PrintSupport.asList(jdoClass.getPersistentNonPrimaryKeyFieldNumbers()).toString());
        assertEquals("Wrong list of persistentRelshipFieldNumbers of class " + className, 
                     "[]",
                     PrintSupport.asList(jdoClass.getPersistentRelationshipFieldNumbers()).toString());
        assertEquals("Wrong list of persistentSerializableFieldNumbers of class " + className, 
                     "[0, 2]",
                     PrintSupport.asList(jdoClass.getPersistentSerializableFieldNumbers()).toString());

        JDOField[] jdoFields = jdoClass.getDeclaredFields();
        assertEquals("Wrong number of declared fields of class" + className,
                     2,
                     jdoFields.length);

        JDOField jdoField;
        String fieldName;

        jdoField = jdoFields[0];
        fieldName = jdoField.getName();
        assertEquals("Wrong name of field of class " + className,
                     "s03",
                     fieldName);
        assertEquals("Wrong type of field " + fieldName + " of class " + className,
                     "java.lang.String",
                     jdoField.getType().toString());
        assertEquals("Wrong field number of field " + fieldName + " of class " +
                     className,
                     2,
                     jdoField.getFieldNumber());
        
        jdoField = jdoFields[1];
        fieldName = jdoField.getName();
        assertEquals("Wrong name of field of class " + className,
                     "t04",
                     fieldName);
        assertEquals("Wrong type of field " + fieldName + " of class " + className,
                     "java.lang.String",
                     jdoField.getType().toString());
        assertEquals("Wrong field number of field " + fieldName + " of class " + className,
                     3,
                     jdoField.getFieldNumber());
    }

    /** */
    protected String getQualifiedClassName(String className, String packageName) {
        if (className.indexOf('.') == -1) {
            // not qualified
            return packageName + '.' + className;
        }
        return className;
    }

    /** */
    protected InputStream getResourceAsStream(final Class clazz,
                                              final String resourceName) {
        return (InputStream)AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return clazz.getResourceAsStream(resourceName);
                }});
    }
    
    /**
     * Implementation of EntityResolver interface to check the jdo.dtd location
     **/
    private static class JDOEntityResolver 
        implements EntityResolver 
    {
        private static final String RECOGNIZED_PUBLIC_ID = 
            "-//Sun Microsystems, Inc.//DTD Java Data Objects Metadata 1.0//EN";
        private static final String RECOGNIZED_SYSTEM_ID = 
            "file:/javax/jdo/jdo.dtd";

        public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException 
        {
            // check for recognized ids
            if (((publicId != null) && RECOGNIZED_PUBLIC_ID.equals(publicId)) ||
                ((publicId == null) && (systemId != null) && 
                 RECOGNIZED_SYSTEM_ID.equals(systemId))) {
                // Substitute the dtd with the one from javax.jdo.jdo.dtd,
                // but only if the publicId is equal to RECOGNIZED_PUBLIC_ID
                // or there is no publicID and the systemID is equal to
                // RECOGNIZED_SYSTEM_ID. 
                InputStream stream = (InputStream) AccessController.doPrivileged (
                    new PrivilegedAction () {
                        public Object run () {
                            return getClass().getClassLoader().
                                getResourceAsStream("javax/jdo/jdo.dtd"); //NOI18N
                        }});
                if (stream == null) {
                    throw new JDOFatalInternalException(
                        "Cannot load javax/jdo/jdo.dtd, because the file does not exist in the jdo.jar file, or the test case class is not granted permission to read this file.  The metadata .xml file contained PUBLIC=" + publicId + " SYSTEM=" + systemId + ".");
                }
                return new InputSource(new InputStreamReader(stream));
            }
            return null;
        }
    }
}

