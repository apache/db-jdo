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

package org.apache.jdo.impl.model.jdo.xml;

import java.util.*;

import org.apache.jdo.impl.model.java.reflection.ReflectionJavaModelFactory;
import org.apache.jdo.impl.model.java.runtime.RuntimeJavaModel;
import org.apache.jdo.impl.model.jdo.caching.JDOModelFactoryImplCaching;
import org.apache.jdo.impl.model.jdo.util.PrintSupport;
import org.apache.jdo.model.ModelException;
import org.apache.jdo.model.ModelFatalException;
import org.apache.jdo.model.java.JavaModel;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOModel;
import org.apache.jdo.model.jdo.JDOModelFactory;

/**
 * This class allows to check whether there is JDO metadata for a class with 
 * a given class name. There must be a class file (enhanced or not enhanced)
 * available in the classpath of the caller.
 * <p>
 * Usage: XMLExists &lt;options&gt; &lt;arguments&gt;...
 * <br>
 * Options:
 * <br>
 * <code>&nbsp;&nbsp;-h&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</code>
 * print usage message
 * <br>
 * <code>&nbsp;&nbsp;-v&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</code>
 * print verbose messages and JDO metadata
 * <br>
 * <code>&nbsp;&nbsp;-q&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</code>
 * do not print any output; just set the exit code
 * <br>
 * <code>&nbsp;&nbsp;-xml&nbsp;&nbsp;&nbsp;&nbsp;</code>
 * expected to find JDO XML metadata for the specified class(es)
 * <br>
 * <code>&nbsp;&nbsp;-noxml&nbsp;&nbsp;</code>
 * expected to find no JDO XML metadata for the specified class(es)
 * <br>
 * Arguments:
 * <br>
 * <code>&nbsp;&nbsp;&lt;classname&gt;&nbsp;</code>
 * the fully qualified name of a class to be checked
 * <p>
 * The following sample call checks whether there is JDO XML metadata for the 
 * classes Emplyoe and Department from the com.xyz.hr package and dumps the 
 * JDOClass instances:
 * <br>
 * <code>&nbsp;&nbsp;java org.apache.jdo.impl.model.jdo.util.XMLExists -v 
 * com.xyz.hr.Employee com.xyz.hr.Department</code>
 * <p>
 * Method main will exit the JVM with an exit code 1 if the test fails for 
 * at least one class. Please note, the class dumps the JDOClass if the 
 * outputLevel is set to VERBOSE (e.g. by setting the -v option). This JDOClass 
 * info does NOT include runtime metadata, even if the class file is enhanced.
 * 
 * @author Michael Bouschen
 */
public class XMLExists
{
    /** 
     * Flag indicating whether the classes to be checked is expected to have 
     * JDO XML metadata or not.
     */
    private boolean xmlExpected;
    
    /** Print verbose messages. */
    public static final int VERBOSE = 1;

    /** Normal output. */
    public static final int NORMAL = 0;

    /** No messages, just set the exit code. */
    public static final int QUIET = -1;
       
    /** Output level.*/
    private int outputLevel = NORMAL;

    /** 
     * The main method checks all classes specified as argument.
     * It will exit the JVM with an exit code 1 if the test fails for 
     * at least one class. 
     * @param args arguments which are options followed by class names.
     */
    public static void main(String[] args)
    {
        final XMLExists test = new XMLExists();
        final List classNames = test.processArgs(args);
        if (!test.run(classNames))
            System.exit(1);
    }

    /** 
     * No arg constructor. The flags default to
     * <ul>
     * <li> <code>xmlExpected == true</code>
     * <li> <code>outputLevel == NORMAL</code>
     * </ul>
     */
    public XMLExists()
    {
        this(true, NORMAL);
    }

    /** Constructor taking checkXMLExists and outputLevel. */
    public XMLExists(boolean xmlExpected, int outputLevel)
    {
        this.xmlExpected = xmlExpected;
        this.outputLevel = outputLevel;
    }

    /**
     * This method checks all classes from the specified list of class names.
     * It uses the current classLoader to load the classes. The method returns 
     * <code>false</code> if there is at least one class that fails on checking.
     * @param classNames list of classes to be checked
     * @return <code>true</code> if all classes are ok;
     * <code>false</code> otherwise.
     */
    public boolean run(List classNames)
    {
        ClassLoader classLoader = getClass().getClassLoader();
        XMLExistsJDOModelFactory factory = new XMLExistsJDOModelFactory();
        JavaModel javaModel = factory.getJavaModel(classLoader);
        JDOModel jdoModel = javaModel.getJDOModel();
        boolean ok = true;
        for (Iterator i = classNames.iterator(); i.hasNext(); ) {
            final String className = (String)i.next();
            try {
                checkClass(className, classLoader, jdoModel, xmlExpected);
                if ((outputLevel == NORMAL) && xmlExpected)
                    System.out.println(
                        "Found XML metadata for class " + className); //NOI18N
                                       
                else if ((outputLevel == NORMAL) && !xmlExpected)
                    System.out.println(
                        "No MXL metadata for class " + className); //NOI18N
            }
            catch (Exception ex) {
                if (outputLevel > QUIET) {
                    System.err.println(ex);
                    ex.printStackTrace();
                }
                ok = false;
            }
        }
        return ok;
    }
    
    /** 
     * The method checks the class with the specified class name.
     * It first loads the class using the specified class loader.
     * If the flag xmlExpected is <code>true</code> it expects to get a non-null
     * JDOClass instance. If the flag xmlExpected is <code>false</code> it 
     * expects to get a null JDOClass instance. 
     * <p>
     * The method indicates any failure by throwing an exception. 
     * It dumps the JDOClass if the output level is VERBOSE.
     * @param className the name of the class to be checked
     * @param classLoader the class loader to be used to load the class
     * @param jdoModel JDOModel for type info
     * @param xmlExpected flag whether the class to be checked is expected to 
     * have JDO XML metadata or not.
     */
    public void checkClass(String className, 
                           ClassLoader classLoader,
                           JDOModel jdoModel, 
                           boolean xmlExpected)
        throws Exception
    {
        Class clazz = Class.forName(className, true, classLoader);
        if (outputLevel == VERBOSE)
            System.out.println("Found " + clazz); //NOI18N
        JDOClass jdoClass = jdoModel.getJDOClass(className);
        if (xmlExpected && (jdoClass == null)) {
            throw new Exception(
                "Missing JDO XML metadata for class " + //NOI18N
                className);
        }
        if (!xmlExpected && (jdoClass != null)) {
            throw new Exception(
                "Found JDO XML metadata for class " + className); //NOI18N
        }
        if ((outputLevel == VERBOSE) && (jdoClass != null)) {
            PrintSupport.printJDOClass(jdoClass);
        }
    }
    
    // ==== Comand line processing helper method =====
    
    /**
     * Helper method to do command line argument processing.
     * @param args the arguments passed to main.
     * @return the list of classes to be checked.
     */
    protected List processArgs(String[] args)
    { 
        List classNames = new ArrayList();
        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];
            if (arg.equals("-h")) { //NOI18N
                usage();
            }
            else if (arg.equals("-v")) { //NOI18N
                outputLevel = VERBOSE;
            }
            else if (arg.equals("-q")) { //NOI18N
                outputLevel = QUIET;
            }
            else if (arg.equals("-xml")) { //NOI18N
                xmlExpected = true;
            }
            else if (arg.equals("-noxml")) { //NOI18N
                xmlExpected = false;
            }
            else if (arg.length() > 0 && arg.charAt(0) == '-') {
                System.err.println("Unrecognized option:" + arg); //NOI18N
                usage();
            }
            else if (arg.length() == 0) {
                System.err.println("Ignoring empty command line argument."); //NOI18N
            }
            else {
                classNames.add(arg);
            }
        }
        return classNames;
    }
    
    /**
     * Print a usage message to System.err.
     */
    public void usage() 
    {
        System.err.println("Usage: main <options> <arguments>..."); //NOI18N
        System.err.println("Options:"); //NOI18N
        System.err.println("  -h      print usage message"); //NOI18N
        System.err.println("  -v      print verbose messages and JDO metadata"); //NOI18N
        System.err.println("  -q      do not print any messages; just set the exit code"); //NOI18N
        System.err.println("  -xml    expected to find JDO XML metadata for the specified class(es)"); //NOI18N
        System.err.println("  -noxml  expected to find no JDO XML metadata for the specified class(es)"); //NOI18N
        System.err.println("Arguments:"); //NOI18N
        System.err.println("  <classname>   the fully qualified name of a class to be checked"); //NOI18N
    }
    
    /**
     * JavaModelFactory implementation. 
     * We cannot use the RuntimeJavaModelFactory, because it registers
     * a JDOImplHelper event listener to populate enhancer generated
     * metadata into the model. Since XMLExists checks whether JDO
     * metadata is present from a .jdo we do not want to the enhancer
     * generated metadata.
     */
    private static class XMLExistsJDOModelFactory 
        extends ReflectionJavaModelFactory {

        /** */
        protected XMLExistsJDOModelFactory() {}

        /** */
        public JavaModel createJavaModel(Object key) throws ModelException {
            if ((key != null) && (!(key instanceof ClassLoader)))
                throw new ModelException("Invalid key " + key + 
                                         " expected ClassLoader");
            ClassLoader classLoader = (ClassLoader)key;
            JavaModel javaModel = new RuntimeJavaModel(classLoader, this);

            // set the JDOModel property in JavaModel
            setJDOModelInternal(javaModel);

            return javaModel;
        }
        
        /**
         * Sets the JDOModel instance for the specified JavaModel.
         * @param javaModel the JavaModel
         */
        protected void setJDOModelInternal(JavaModel javaModel) {
            JDOModelFactory factory = JDOModelFactoryImplCaching.getInstance();
            JDOModel jdoModel = factory.getJDOModel(javaModel);
            // update the JDOModel property of the JavaModel
            try {
                javaModel.setJDOModel(jdoModel);
            }
            catch (ModelException ex) {
                throw new ModelFatalException("Cannot set JDOModel", ex); //NOI18N
            }
        }
    }
    
}
