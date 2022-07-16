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

package org.apache.jdo.tck.util.signature;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.HashSet;

import java.text.ParseException;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;

/**
 * Tests classes for correct signatures.
 *
 * @author Martin Zaun
 */
public class SignatureVerifier {
    /** The new-line character on this system. */
    protected static final String NL = System.getProperty("line.separator");

    /** All modifiers defined in java.lang.reflect.Modifier.
     * This field is used to filter out vm-specific modifiers 
     * such as found in Sun's vm to identify Enum and Annotation.
     */
    protected static final int ALL_MODIFIERS =
            Modifier.ABSTRACT |
            Modifier.FINAL |
            Modifier.INTERFACE |
            Modifier.NATIVE |
            Modifier.PRIVATE |
            Modifier.PROTECTED |
            Modifier.PUBLIC |
            Modifier.STATIC |
            Modifier.STRICT |
            Modifier.SYNCHRONIZED |
            Modifier.TRANSIENT |
            Modifier.VOLATILE;

    /** Pseudo modifier for annotation */
    protected static final int ANNOTATION = 0x2000;

    /** Pseudo modifier for enum */
    protected static final int ENUM = 0x4000;

    /** A writer for standard output. */
    protected final PrintWriter log;

    /** Flag to print error output only. */
    private final boolean quiet;

    /** Flag to also print verbose output. */
    private final boolean verbose;

    /** The parse to be used for parsing signature descriptor files. */
    protected final Parser parser = new Parser();

    /** The classloader to be used for loading types. */
    protected final ClassLoader classLoader;

    /** The currently tested Class. */
    protected Class<?> cls;
    
    /** All untested, declared members of the current class. */
    protected final Set<Member> members = new HashSet<>();

    /** Collects names of loadable classes. */
    private final Set<String> loading = new TreeSet<>();

    /** Collects names of unloadable classes. */
    private final Set<String> notLoading = new TreeSet<>();

    /** Counts tested features (class, constructor, fields, methods). */
    private int tested;

    /** Counts missing members (constructor, fields, methods). */
    private int missing;

    /** Counts non-matching features (class, constructor, fields, methods). */
    private int mismatch;

    /** Counts matching features (class, constructor, fields, methods). */
    private int matching;

    /** Counts public non-standard members (constructor, fields, methods). */
    private int nonStandard;

    /** Counts other reported problems (e.g., accessing field values). */
    private int otherProblems;

    /**
     * Constructs a test instance.
     * @param loader ClassLoader
     * @param log writer for logging
     * @param quiet quiet option
     * @param verbose verbose option
     */
    public SignatureVerifier(ClassLoader loader, PrintWriter log,
                             boolean quiet, boolean verbose) {
        classLoader = this.getClass().getClassLoader();
        this.log = log;
        this.quiet = quiet;
        this.verbose = (!quiet && verbose);
    }    

    /**
     * Constructs a test instance.
     * @param log writer for logging
     * @param quiet quiet option
     * @param verbose verbose option
     */
    public SignatureVerifier(PrintWriter log,
                             boolean quiet, boolean verbose) {
        this(SignatureVerifier.class.getClassLoader(), log, quiet, verbose);
    }

    // ----------------------------------------------------------------------
    // Local Logging Methods
    // ----------------------------------------------------------------------

    /**
     * Prints an error message.
     * @param msg error message
     */
    protected void logError(String msg) {
        log.println(msg);
    }

    /**
     * Prints an info message.
     * @param msg info message
     */
    protected void logInfo(String msg) {
        if (!quiet) {
            log.println(msg);
        }
    }

    /**
     * Prints a verbose message.
     * @param msg  verbose message
     */
    protected void logVerbose(String msg) {
        if (verbose) {
            log.println(msg);
        }
    }

    // ----------------------------------------------------------------------
    // Test Methods
    // ----------------------------------------------------------------------

    /**
     * Tests the signature of classes (in the specified classloader) against
     * a list of signature descriptor files; returns with a status code.
     * @param descrFileNames list of signature descriptor file names
     * @return zero if all tests have passed and no problems were detected
     * @throws IOException error reading file
     * @throws ParseException error during parsing
     */
    public int test(List<String> descrFileNames)
        throws IOException, ParseException {
        // check argument
        if (descrFileNames == null || descrFileNames.isEmpty()) {
            final String m = ("ERROR: No signature descriptor file to parse.");
            logError(m);
            return -1;
        }
        
        // clear statistics
        loading.clear();
        notLoading.clear();
        tested = 0;
        missing = 0;
        mismatch = 0;
        matching = 0;
        nonStandard = 0;
        otherProblems = 0;

        // process descriptor files
        parser.parse(descrFileNames);
        report();
        
        // return a positive value in case of any problems
        return (notLoading.size() + missing + mismatch + nonStandard
                + otherProblems);
    }

    /**
     * Reports the results of the last signature test run.
     */
    public void report() {
        logInfo("");
        logInfo("Signature Test Results");
        logInfo("======================");
        logInfo("");
        logInfo("    tested features:          " + tested);
        logInfo("");
        logInfo("Successes:");
        logInfo("    matching features:        " + matching);
        logInfo("    loadable classes:         " + loading.size());
        logInfo("");
        logInfo("Failures:");
        logInfo("    missing features:         " + missing);
        logInfo("    non-matching features:    " + mismatch);
        logInfo("    non-standard features:    " + nonStandard);
        logInfo("    unloadable classes:       " + notLoading.size());
        logInfo("    other problems:           " + otherProblems);
        logInfo("");
    }
    
    // ----------------------------------------------------------------------
    // Test Logic
    // ----------------------------------------------------------------------

    /**
     * Handles class loading problems.
     * @param t Throwable
     */
    protected void handleNotLoading(Throwable t) {
        notLoading.add(t.getMessage().replace('/', '.'));
        final String m = ("--- failed loading class;" + NL
                          + "    caught: " + t);
        logError(m);
    }

    /**
     * Handles missing members.
     * @param msg message
     * @param exp expected
     */
    protected void handleMissing(String msg, String exp) {
        missing++;
        final String m = ("--- " + msg + NL
                          + "    expected: " + exp + NL
                          + "    class:    " + Formatter.toString(cls));
        logError(m);
    }

    /**
     * Handles non-matching features.
     * @param msg message
     * @param exp expected
     * @param fnd feature
     */
    protected void handleMismatch(String msg, String exp, String fnd) {
        mismatch++;
        final String m = ("--- " + msg + NL
                          + "    expected: " + exp + NL
                          + "    found:    " + fnd + NL
                          + "    class:    " + Formatter.toString(cls));
        logError(m);
    }

    /**
     * Handles public non-standard features.
     * @param msg message
     * @param fnd feature
     */
    protected void handleNonStandard(String msg, String fnd) {
        nonStandard++;
        final String m = ("--- " + msg + NL
                          + "    found:    " + fnd + NL
                          + "    class:    " + Formatter.toString(cls));
        logError(m);
    }

    /**
     * Handles other problems.
     * @param msg message
     * @param exp expected
     */
    protected void handleProblem(String msg, String exp) {
        otherProblems++;
        final String m = ("--- " + msg + NL
                          + "    expected: " + exp + NL
                          + "    class:    " + Formatter.toString(cls));
        logError(m);
    }

    /**
     * Handles a perfect feature match.
     * @param msg message
     * @param fnd feature
     */
    protected void handleMatch(String msg, String fnd) {
        matching++;
        final String m = ("+++ " + msg + fnd);
        logVerbose(m);
    }

    /**
     * Returns the class objects for given (Java) user type names.
     * @param userTypeName user type names
     * @return class objects
     */
    protected Class<?>[] getClasses(String[] userTypeName) {
        final Class<?>[] cls = new Class[userTypeName.length];
        for (int i = userTypeName.length - 1; i >= 0; i--) {
            cls[i] = getClass(userTypeName[i]);
        }
        return cls;
    }
    
    /**
     * Returns the class object for a given (Java) user type name.
     * @param userTypeName user type name
     * @return class object
     */
    protected Class<?> getClass(String userTypeName) {
        // use helper for retrieving class objects for primitive types
        Class<?> cls = TypeHelper.primitiveClass(userTypeName);
        if (cls != null) {
            return cls;
        }
        
        // load class
        try {
            final String r = TypeHelper.reflectionTypeName(userTypeName);
            cls = Class.forName(r, false, classLoader);
            loading.add(userTypeName);
        } catch (LinkageError err) {
            handleNotLoading(err);
        } catch (ClassNotFoundException ex) {
            handleNotLoading(ex);
        }
        return cls;
    }

    /** Check an expected value expression (the value of a static field
     *  or the default value of an annotation method), comparing it to
     *  the actual value from the Field or Method object.
     *  Only supports primitive, enum, empty array of enum, empty array of
     *  annotation, and String.
     * @param value the String form of the value from the signature file
     * @param type the type as declared in the class
     * @param actual the actual value
     * @return the description of the expected value, or null if ok
     * @throws java.lang.NumberFormatException format exception
     */
    protected String checkValue(String value, String type, Object actual) 
            throws NumberFormatException {
        // note array type
        boolean isArray = false;
        if (type.endsWith("[]")) {
            isArray = true;
            type = type.substring(0, type.length() - 2);
            // remove { from beginning and } from end
            value = value.substring(1, value.length() - 1);
        }
        // first check primitive type
        final Object exp;
        Class<?> expClass;
        final boolean ok;
        if (type.equals("byte")) {
            if (isArray) {
                ok = actual.getClass().getComponentType().equals(byte.class);
            } else {
                ok = Byte.valueOf(value).equals(actual);
            }
        } else if (type.equals("boolean")) {
            if (isArray) {
                ok = actual.getClass().getComponentType().equals(boolean.class);
            } else {
                ok = Boolean.valueOf(value).equals(actual);
            }
        } else if (type.equals("short")) {
            if (isArray) {
                ok = actual.getClass().getComponentType().equals(short.class);
            } else {
                ok = Short.valueOf(value).equals(actual);
            }
        } else if (type.equals("int")) {
            if (isArray) {
                ok = actual.getClass().getComponentType().equals(int.class);
            } else {
                ok = Integer.valueOf(value).equals(actual);
            }
        } else if (type.equals("long")) {
            if (isArray) {
                ok = actual.getClass().getComponentType().equals(long.class);
            } else {
                ok = Long.valueOf(value).equals(actual);
            }
        } else if (type.equals("float")) {
            if (isArray) {
                ok = actual.getClass().getComponentType().equals(float.class);
            } else {
                ok = Float.valueOf(value).equals(actual);
            }
        } else if (type.equals("double")) {
            if (isArray) {
                ok = actual.getClass().getComponentType().equals(double.class);
            } else {
                ok = Double.valueOf(value).equals(actual);
            }
        } else if (type.equals("char")) {
            if (isArray) {
                ok = actual.getClass().getComponentType().equals(char.class);
            } else {
                ok = Character.valueOf(value.charAt(1)).equals(actual);
            }
        // next check Class
        } else if (type.equals("java.lang.Class")) {
            if (isArray) {
                ok = actual.getClass().getComponentType().equals(Class.class);
            } else {
                // strip ".class" from type name
                int offset = value.indexOf(".class");
                value = value.substring(0, offset);
                ok = getClass(value).equals(actual);
            }
        // next check String
        } else if (type.equals("java.lang.String")) {
            if (isArray) {
                ok = actual.getClass().getComponentType().equals(String.class);
            } else {
                // cut off '\"' chars at begin and end
                final String s;
                if (value.length() > 1) {
                    s = value.substring(1, value.length() - 1);
                } else {
                    s = "";
                }
                ok = s.equals(actual);
            }
        // now check non-java.lang annotations and enums
        } else {
            expClass = getClass(type);
            if (isArray) {
                // check if the actual component type is the right class
                // don't check the actual values because only empty arrays
                // are supported.
                ok = actual.getClass().getComponentType().equals(expClass);
            } else if (expClass == null) {
                System.out.println("WARNING : checkValue value=" + value + " type=" + type + " comes up with null class");
                ok = false;
            } else if (expClass.isAnnotation()) {
                // check whether the type isAssignableFrom the class of the actual value, 
                // if type is an annotation. The actual value is a dynamic proxy, 
                // so an equals comparison does not work. 
                ok = expClass.isAssignableFrom(actual.getClass());
            } else {
                // get the actual value which must be a static class.field
                Object expectedValue = null;
                try {
                    // now get actual value
                    // separate value name into class and field name
                    int lastDot = value.lastIndexOf(".");
                    String expectedClassName = value.substring(0, lastDot);
                    String expectedFieldName = 
                            value.substring(lastDot + 1);
                    // get Class object from class name
                    Class<?> expectedClass = getClass(expectedClassName);
                    if (expectedClass == null) 
                        throw new ClassNotFoundException();
                    // get Field object from Class and field name
                    Field expectedField = 
                            expectedClass.getField(expectedFieldName);
                    expectedValue = expectedField.get(null);
                } catch (NoSuchFieldException ex) {
                    handleNotLoading(ex);
                } catch (SecurityException ex) {
                    handleNotLoading(ex);
                } catch (IllegalArgumentException ex) {
                    handleNotLoading(ex);
                } catch (IllegalAccessException ex) {
                    handleNotLoading(ex);
                } catch (ClassNotFoundException ex) {
                    handleNotLoading(ex);
                }
                ok = expectedValue.equals(actual);
            }
        }
        // return message if not ok
        if (ok) return null;
        else return value;
    }

    /**
     * Validates a field against a prescribed signature.
     * @param mods modifier
     * @param type type
     * @param name name
     * @param value value
     */
    protected void checkField(int mods, String type, String name,
                              String value) {
        tested++;
        type = TypeHelper.qualifiedUserTypeName(type);

        // get field
        final Field field;
        try {
            field = cls.getDeclaredField(name);
        } catch (NoSuchFieldException ex) {
            handleMissing(
                "missing field: ",
                Formatter.toString(mods, type, name, value));
            return;
        } catch (LinkageError err) {
            handleNotLoading(err);
            return;
        }

        // check modifiers
        if (cls.isInterface()) {
            // fields interfaces are implicitly public, static, and final
            mods |= Modifier.PUBLIC;
            mods |= Modifier.STATIC;
            mods |= Modifier.FINAL;
        }
        if (mods != convertModifiers(field)) {
            handleMismatch(
                "field declaration: non-matching modifiers;",
                Formatter.toString(mods, type, name, null),
                Formatter.toString(field, null));
        }
            
        // check type
        if (!TypeHelper.isNameMatch(type, field.getType())) {
            handleMismatch(
                "field declaration: non-matching type;",
                Formatter.toString(mods, type, name, null),
                Formatter.toString(field, null));
        }

        // check field value if any
        Object actualValue = null;
        if (value != null) {
            // only support for public, static, and final fields
            final int m = (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL);
            if ((mods & m) == 0) {
                handleProblem("field declaration: ignoring field value "
                              + "definition in descriptor file;",
                              Formatter.toString(mods, type, name, value));
            } else {
                // compare field's expected with found value
                try {
                    actualValue = field.get(null);
                } catch (IllegalAccessException ex) {
                    handleProblem("field declaration: cannot access field "
                                  + "value, exception: " + ex + ";",
                                  Formatter.toString(mods, type, name, value));
                }
                String error = checkValue(value, type, actualValue);
                if (error != null) {
                    handleMismatch(
                        "field declaration: non-matching values;",
                        Formatter.toString(mods, type, name, error),
                        Formatter.toString(field, "\"" + actualValue + "\""));
                }
            }
        }
        
        // field OK
        members.remove(field);
        handleMatch("has field: ", Formatter.toString(field, actualValue));
    }

    /**
     * Validates a constructor against a prescribed signature.
     * @param mods modifier
     * @param params parameters
     * @param excepts excepts
     */
    protected void checkConstructor(int mods, String[] params,
                                    String[] excepts) {
        tested++;
        params = TypeHelper.qualifiedUserTypeNames(params);
        excepts = TypeHelper.qualifiedUserTypeNames(excepts);

        // get parameter classes
        final Class<?>[] prms = getClasses(params);
        if (prms == null) {
            return;
        }

        // get constructor
        final Constructor<?> ctor;
        try {
            ctor = cls.getDeclaredConstructor(prms);
        } catch (NoSuchMethodException ex) {
            String name = cls.getName();
            final int i = name.lastIndexOf('.');
            name = (i < 0 ? name : name.substring(i));
            handleMissing(
                "missing constructor: ",
                Formatter.toString(mods, null, name, params, excepts));
            return;
        } catch (LinkageError err) {
            handleNotLoading(err);
            return;
        }
        
        // check modifiers
        if (mods != ctor.getModifiers()) {
            handleMismatch(
                "constructor declaration: non-matching modifiers;",
                Formatter.toString(mods, null, cls.getName(), params, excepts),
                Formatter.toString(ctor));
        }

        // check exceptions
        if (!TypeHelper.isNameMatch(excepts, ctor.getExceptionTypes())) {
            handleMismatch(
                "method declaration: non-matching exceptions;",
                Formatter.toString(mods, null, cls.getName(), params, excepts),
                Formatter.toString(ctor));
        }

        // constructor OK
        members.remove(ctor);
        handleMatch("has constructor: ", Formatter.toString(ctor));
    }
    
    /**
     * Validates a method against a prescribed signature.
     * @param mods modifier
     * @param result result
     * @param name name
     * @param params parameters
     * @param excepts excepts
     * @param value value
     */
    protected void checkMethod(int mods, String result, String name,
                               String[] params, String[] excepts, 
                               String value) {
        tested++;
        params = TypeHelper.qualifiedUserTypeNames(params);
        excepts = TypeHelper.qualifiedUserTypeNames(excepts);
        result = TypeHelper.qualifiedUserTypeName(result);

        // get parameter classes
        final Class<?>[] prms = getClasses(params);
        if (prms == null) {
            return;
        }

        // get method
        final Method method;
        try {
            method = cls.getDeclaredMethod(name, prms);
        } catch (NoSuchMethodException ex) {
            handleMissing(
                "missing method: ",
                Formatter.toString(mods, result, name, params, excepts));
            return;
        } catch (LinkageError err) {
            handleNotLoading(err);
            return;
        }
        
        // check modifiers
        if (cls.isInterface()) {
            // methods in interfaces are implicitly public
            mods |= Modifier.PUBLIC;
        }
        Class<?> resultType = getClass(result);
        if (resultType == null) {
        	System.out.println("WARNING : checkMethod " + name + " result=" + result + " comes up with null resultType!");
        } else if (resultType.isAnnotation()) {
            // add ANNOTATION modifier if the result type is an annotation
            mods |= ANNOTATION;
        }
        if (mods != convertModifiers(method)) {
            handleMismatch(
                "method declaration: non-matching modifiers;",
                Formatter.toString(mods, result, name, params, excepts),
                Formatter.toString(method));
        }

        // check return type
        if (!TypeHelper.isNameMatch(result, method.getReturnType())) {
            handleMismatch(
                "method declaration: non-matching return type",
                Formatter.toString(mods, result, name, params, excepts),
                Formatter.toString(method));
        }

        // check exceptions
        if (!TypeHelper.isNameMatch(excepts, method.getExceptionTypes())) {
            handleMismatch(
                "method declaration: non-matching exceptions;",
                Formatter.toString(mods, result, name, params, excepts),
                Formatter.toString(method));
        }
        
        // check default value of an annotation element (method)
        if (value != null) {
            Object actualValue = null;
            try {
                actualValue = method.getDefaultValue();
            } catch (Exception ex) {
                handleProblem("method declaration: cannot access default "
                              + "value, exception: " + ex + ";",
                              Formatter.toString(mods, result, name, value));
            }
            // check value expected versus actual
            String wrong = checkValue(value, result, actualValue);

            if (wrong != null) {
                handleMismatch(
                    "method declaration: non-matching default value;",
                    Formatter.toString(mods, result, name + "() default ", wrong),
                    Formatter.toString(method));
            }
        }

        // method OK
        members.remove(method);
        handleMatch("has method: ", Formatter.toString(method));
    }

    /**
     * Validates a class declaration against a prescribed signature.
     * @param mods modifier
     * @param name name
     * @param ext ext
     * @param impl implementation
     */
    protected void checkClass(int mods, String name,
                              String[] ext, String[] impl) {
        logVerbose("");
        logVerbose("testing " + Formatter.toString(mods, name, ext, impl));
        tested++;
        ext = TypeHelper.qualifiedUserTypeNames(ext);
        impl = TypeHelper.qualifiedUserTypeNames(impl);

        // get and assign currently processed class
        cls = getClass(name);
        if (cls == null) {
            return; // can't load class
        }  

        // collect all declared members of current class
        members.clear();
        try {
            members.addAll(Arrays.asList(cls.getDeclaredFields()));
            members.addAll(Arrays.asList(cls.getDeclaredConstructors()));
            members.addAll(Arrays.asList(cls.getDeclaredMethods()));
        } catch (LinkageError err) {
            handleNotLoading(err);
        }
      
        // check modifiers
        final boolean isInterface = ((mods & Modifier.INTERFACE) != 0);
        if (isInterface) {
            mods |= Modifier.ABSTRACT;
        }
        if (mods != convertModifiers(cls)) {
            handleMismatch(
                "class declaration: non-matching modifiers;",
                Formatter.toString(mods, name, ext, impl),
                Formatter.toString(cls));
        }

        // check superclass and extended/implemented interfaces
        final Class<?> superclass = cls.getSuperclass();
        final Class<?>[] interfaces = cls.getInterfaces();
        if (isInterface) {
            //assert (impl.length == 0);
            if (!TypeHelper.isNameMatch(ext, interfaces)) {
                handleMismatch(
                    "interface declaration: non-matching interfaces;",
                    Formatter.toString(mods, name, ext, impl),
                    Formatter.toString(cls));
            }
        } else {
            //assert (ext.length <= 1);
            final String s = (ext.length == 0 ? "java.lang.Object" : ext[0]);
            if (!TypeHelper.isNameMatch(s, superclass)) {
                handleMismatch(
                    "class declaration: non-matching superclass;",
                    Formatter.toString(mods, name, ext, impl),
                    Formatter.toString(cls));
            }
            if (!TypeHelper.isNameMatch(impl, interfaces)) {
                handleMismatch(
                    "class declaration: non-matching interfaces;",
                    Formatter.toString(mods, name, ext, impl),
                    Formatter.toString(cls));
            }
        }

        handleMatch("has class: ", Formatter.toString(cls));
    }

    /** Runs checks on a class after its members have been validated. */
    protected void postCheckClass() {
        if (cls == null) {
            return; // nothing to do if class couldn't be loaded
        }

        // check for public non-standard members
        for (final Member m : members) {
            if ((m.getModifiers() & Modifier.PUBLIC) != 0) {
                handleNonStandard("non-standard, public member;",
                        Formatter.toString(m));
            }
        }
    }

    /** Return modifiers for the class, but handling enum and annotation
     * as if they had a special modifier.
     * @param cls the class
     * @return modifiers of the class with extra flags for enum and annotation
     */
    protected int convertModifiers(Class<?> cls) {
        int result = cls.getModifiers();
        // first remove extraneous stuff
        result &= ALL_MODIFIERS;
        // if enum, set pseudo enum flag
        if (cls.isEnum())
            result |= ENUM;
        // if annotation, set pseudo annotation flag
        if (cls.isAnnotation()) 
            result |= ANNOTATION;
        return result;
    }

    /** Return modifiers for the method, but handling enum and annotation
     * as if they had a special modifier.
     * @param method the method
     * @return modifiers of the class with extra flags for enum and annotation
     */
    protected int convertModifiers(Method method) {
        int result = method.getModifiers();
        // first remove extraneous stuff
        result &= ALL_MODIFIERS;
        // if enum return type, set pseudo enum flag
        if (method.getReturnType().isEnum())
            result |= ENUM;
        // if annotation, set pseudo annotation flag
        if (method.getReturnType().isAnnotation()) 
            result |= ANNOTATION;
        // if return type is an enum class, un-set FINAL modifier in all methods
        // because in Java 5, methods are generated as final; in Java 6, not
        if (method.getDeclaringClass().isEnum()) 
            result &= ~Modifier.FINAL;
        return result;
    }

    /** Return modifiers for the field, but handling enum
     * as if it had a special modifier.
     * @param field the field
     * @return modifiers of the class with extra flag for enum
     */
    protected int convertModifiers(Field field) {
        int result = field.getModifiers();
        // first remove extraneous stuff
        result &= ALL_MODIFIERS;
        // if enum, set pseudo enum flag
        if (field.isEnumConstant())
            result |= ENUM;
        return result;
    }

    // ----------------------------------------------------------------------
    // Parser for Signature Descriptor Files
    // ----------------------------------------------------------------------

    /**
     * For parsing of signature descriptor files.
     */
    protected class Parser {
        /** The current descriptor file being parsed. */
        private File descriptorFile;

        /** The line number reader for the current descriptor file. */
        private LineNumberReader ir;

        /** A look-ahead token to be read next. */
        private String nextToken;

        /**
         * Returns an error message reporting an unexpected end of file.
         * @return message
         */
        protected String msgUnexpectedEOF() {
            return ("unexpected end of file at line: " + ir.getLineNumber()
                    + ", file: " + descriptorFile.getPath());
        }

        /**
         * Returns an error message reporting an unextected token.
         * @param t token
         * @return error message
         */
        protected String msgUnexpectedToken(String t) {
            return ("unexpected token: '" + t + "'"
                    + " in line: " + ir.getLineNumber()
                    + ", file: " + descriptorFile.getPath());
        }

        /**
         * Retrieves the look-ahead token to be parsed next.
         * @return look ahead
         */
        protected String getLookAhead() {
            final String t = nextToken;
            nextToken = null;
            return t;
        }

        /**
         * Sets the look-ahead token to be parsed next.
         * @param t token
         */
        protected void setLookAhead(String t) {
            //assert (nextToken == null);
            nextToken = t;
        }

        /**
         * Skips any "white space" and /* style comments
         * and returns whether there are more
         * characters to be parsed.
         * @return true if there are more characters to be parsed
         * @throws IOException IO error
         */
        protected boolean skip() throws IOException {
            Character c;
            Character cnext;
            // Permitted states are white, commentSlashStar, commentSlashSlash
            String state = "white";
            final Character SLASH = '/';
            final Character STAR = '*';
            while (true) {
                c = peek();
                if (c == null)
                    return false;
                if (state.equals("commentSlashStar") && STAR.equals(c)) {
                    cnext = peek();
                    if (cnext == null)
                        return false;
                    if (SLASH.equals(cnext)) {
                        state = "white";
                    }
                    continue;
                }
                if (state.equals("white")) {
                    if (SLASH.equals(c)) {
                        cnext = peek();
                        if (cnext == null)
                            return false;
                        if (STAR.equals(cnext)) {
                            state = "commentSlashStar";
                            continue;
                        }
                        if (SLASH.equals(cnext)) {
                            state = "commentSlashSlash";
                            continue;
                        }
                    }
                    if (!Character.isWhitespace(c)) {
                        break;
                    }
                }
                if (state.equals("commentSlashSlash")
                    && (c == Character.LINE_SEPARATOR
                        || c == '\n'
                        || c == '\r')) {
                    state = "white";
                    continue;
                }
            }
            ir.reset();
            return true;
        }

        /**
         * Returns next char, or null if there is none
         * @return next char
         * @throws IOException IO error
         */
        protected Character peek() throws IOException {
            ir.mark(1);
            int i;
            if ((i = ir.read()) < 0) {
                return null;
            }            
            return Character.valueOf((char)i);
        }

        /**
         * Scans for an (unqualified) identifier.
         * @return <code>null</code> if the next token is not an identifier
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String scanIdentifier()
            throws IOException, ParseException {
            // parse stored token if any
            String t;
            if ((t = getLookAhead()) != null) {
                if (!Character.isJavaIdentifierStart(t.charAt(0))) {
                    setLookAhead(t); // not an identifier
                    return null;
                }
                return t;
            }

            // parse first char
            if (!skip()) {
                throw new ParseException(msgUnexpectedEOF(), 0);
            }
            ir.mark(1);
            char c = (char)ir.read();
            if (!Character.isJavaIdentifierStart(c)) {
                ir.reset(); // not start of an identifier
                return null;
            }
        
            // parse remaining chars
            final StringBuffer sb = new StringBuffer();
            do {
                sb.append(c); 
                int i;
                ir.mark(1);
                if ((i = ir.read()) < 0)
                    break;
                c = (char)i;
            } while (Character.isJavaIdentifierPart(c));
            ir.reset(); // not part of an identifier
            return sb.toString();
        }
    
        /**
         * Scans for a number literal.
         * @return <code>null</code> if the next token is not a number
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String scanNumberLiteral()
            throws IOException, ParseException {
            // parse stored token if any
            String t;
            if ((t = getLookAhead()) != null) {
                if (!(t.charAt(0) == '-' || Character.isDigit(t.charAt(0)))) {
                    setLookAhead(t); // not a number literal
                    return null;
                }
                return t;
            }

            // parse first char
            if (!skip()) {
                throw new ParseException(msgUnexpectedEOF(), 0);
            }
            ir.mark(1);
            char c = (char)ir.read();
            if (Character.isDigit(c)) {
            } else if (c == '-') {
                skip();
            } else {
                ir.reset(); // not start of a number
                return null;
            }
        
            // parse remaining chars
            final StringBuffer sb = new StringBuffer();
            do {
                sb.append(c); 
                int i;
                ir.mark(1);
                if ((i = ir.read()) < 0)
                    break;
                c = (char)i;
            } while (Character.isLetterOrDigit(c) || c == '-' || c == '.');
            ir.reset(); // not part of a number
            return sb.toString();
        }
    
        /**
         * Scans for a character literal.
         * @return <code>null</code> if the next token is not a character
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String scanCharacterLiteral()
            throws IOException, ParseException {
            // parse stored token if any
            String t;
            if ((t = getLookAhead()) != null) {
                if (t.charAt(0) != '\'') {
                    setLookAhead(t); // not a char literal
                    return null;
                }
                return t;
            }

            // parse first char
            if (!skip()) {
                throw new ParseException(msgUnexpectedEOF(), 0);
            }
            ir.mark(1);
            char c = (char)ir.read();
            if (c != '\'') {
                ir.reset(); // not start of a char literal
                return null;
            }
                    
            // parse remaining two chars
            final StringBuffer sb = new StringBuffer();
            for (int j = 0; j < 2; j++) {
                sb.append(c);
                int i;
                if ((i = ir.read()) < 0) {
                    throw new ParseException(msgUnexpectedEOF(), 0);
                }
                c = (char)i;
            }
            if (c != '\'') {
                throw new ParseException(msgUnexpectedToken(String.valueOf(c)),
                                         0);
            }
            sb.append(c); // keep '\'' part of a char literal
            return sb.toString();
        }
    
        /**
         * Scans for a string literal.
         * @return <code>null</code> if the next token is not a string
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String scanStringLiteral()
            throws IOException, ParseException {
            // parse stored token if any
            String t;
            if ((t = getLookAhead()) != null) {
                if (t.charAt(0) != '\"') {
                    setLookAhead(t); // not a string literal
                    return null;
                }
                return t;
            }

            // parse first char
            if (!skip()) {
                throw new ParseException(msgUnexpectedEOF(), 0);
            }
            ir.mark(1);
            char c = (char)ir.read();
            if (c != '\"') {
                ir.reset(); // not start of a string literal
                return null;
            }
        
            // parse remaining chars
            final StringBuffer sb = new StringBuffer();
            do {
                sb.append(c);
                int i;
                if ((i = ir.read()) < 0) {
                    throw new ParseException(msgUnexpectedEOF(), 0);
                }
                c = (char)i;
            } while (c != '\"'); // not supported: nested '\"' char sequences
            sb.append(c); // keep '\"' part of a string literal
            return sb.toString();
        }
    
        /**
         * Scans for an array literal. 
         * Limitation: only the empty array "{}" can be scanned.
         * @return <code>null</code> if the next token is not an array
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String scanArrayLiteral()
            throws IOException, ParseException {
            // parse stored token if any
            String t;
            if ((t = getLookAhead()) != null) {
                if (t.charAt(0) != '{' || t.charAt(1) != '}') {
                    setLookAhead(t); // not an array literal
                    return null;
                }
                return t;
            }

            // parse first char
            if (!skip()) {
                throw new ParseException(msgUnexpectedEOF(), 0);
            }
            ir.mark(1);
            char c = (char)ir.read();
            if (c != '{') {
                ir.reset(); // not start of an array literal
                return null;
            }
            c = (char)ir.read();
            if (c != '}') {
                ir.reset(); // not end of an array literal
                return null;
            }
        
            return "{}";
        }

        /**
         * Scans for an at-sign
         * @return <code>null</code> if the next token is not an at-sign
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String scanAtSign()
            throws IOException, ParseException {
            // parse stored token if any
            String t;
            if ((t = getLookAhead()) != null) {
                if (t.charAt(0) != '@') {
                    setLookAhead(t); // not an at-sign
                    return null;
                }
                return t;
            }

            // parse first char
            if (!skip()) {
                throw new ParseException(msgUnexpectedEOF(), 0);
            }
            ir.mark(1);
            char c = (char)ir.read();
            if (c != '@') {
                ir.reset(); // not an at-sign
                return null;
            }

            return "@";
        }
    
        /**
         * Returns the next token to be parsed.
         * @return never <code>null</code>
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String parseToken()
            throws IOException, ParseException {
            String t;
            if ((t = getLookAhead()) != null) {
            } else if ((t = scanIdentifier()) != null) {
            } else if ((t = scanNumberLiteral()) != null) {
            } else if ((t = scanStringLiteral()) != null) {
            } else if ((t = scanCharacterLiteral()) != null) {
            } else if ((t = scanAtSign()) != null) {
            } else {
                setLookAhead(t); // not an identifier, number, or string
                // next non-white char
                if (!skip()) {
                    throw new ParseException(msgUnexpectedEOF(), 0);
                }
                t = String.valueOf((char)ir.read());
            }
            //log.println("parseToken() : '" + t + "'");
            return t;
        }

        /**
         * Parses the next token and validates it against an expected one.
         * @param token the token
         * @return never <code>null</code>
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String demandToken(String token)
            throws IOException, ParseException {
            final String t = parseToken();
            if (!t.equals(token)) {
                throw new ParseException(msgUnexpectedToken(t), 0);
            }
            return t;
        }
    
        /**
         * Parses a literal.
         * @return <code>null</code> if the next token is not a literal
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String parseLiteral()
            throws IOException, ParseException {
            String t;
            if ((t = scanNumberLiteral()) != null) {
            } else if ((t = scanStringLiteral()) != null) {
            } else if ((t = scanCharacterLiteral()) != null) {
            } else if ((t = scanArrayLiteral()) != null) {
            }
            //log.println("parseLiteral() : '" + t + "'");
            return t;
        }

        /**
         * Parses the next token and validates that it is a literal.
         * @return never <code>null</code>
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String demandLiteral()
            throws IOException, ParseException {
            final String l = parseLiteral();
            if (l == null) {
                throw new ParseException(msgUnexpectedToken(parseToken()), 0);
            }
            return l;
        }
    
        /**
         * Parses the next token and validates that it is a constant,
         * which is either a literal or a static member.
         * @return the description of the field
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String demandConstant()
            throws IOException, ParseException {
            final String literal = parseLiteral();
            if (literal != null) {
                return literal;
            }
            final String field = demandIdentifier();
            if (field == null) {
                throw new ParseException(msgUnexpectedToken(parseToken()), 0);
            }
            return field;
        }

        /**
         * Parses an annotation which is an at-sign followed by a fully qualified interface name.
         * @return <code>null</code> if the next token is not an annotation, otherwise return 
         * the annotation type name without the at-sign.
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String parseAnnotation()
            throws IOException, ParseException {
            String t;
            // parse stored token if any
            if ((t = getLookAhead()) != null) {
                if (t.charAt(0) != '@') {
                    setLookAhead(t); // not a annotation
                    return null;
                }
            } else if ((t = scanAtSign()) != null) {
            } else {
                return null;
            }

            final String identifier = demandIdentifier();
            if (identifier == null) {
                throw new ParseException(msgUnexpectedToken(parseToken()), 0);
            }

            return identifier;
        }

        /**
         * Parses the default value specification of an annotation element and validates 
         * that it is an element value, which is either an annotation or a constant.
         * @return never <code>null</code>
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String demandElementValue()
            throws IOException, ParseException { 
            final String annotation = parseAnnotation();
            if (annotation != null) {
                return annotation;
            }
            final String constant = demandConstant();
            if (constant == null) {
                throw new ParseException(msgUnexpectedToken(parseToken()), 0);
            }
            return constant;
        }
    
        /**
         * Parses any available Java modifiers.
         * @return an int value with the parsed modifiers' bit set
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected int parseModifiers()
            throws IOException, ParseException {
            int m = 0;
            while (true) {
                // parse known modifiers
                final String t = parseToken();
                if      (t.equals("abstract")) m |= Modifier.ABSTRACT;
                else if (t.equals("annotation")) m |= 
                        (ANNOTATION + Modifier.ABSTRACT + Modifier.INTERFACE);
                else if (t.equals("enum")) m |= ENUM;
                else if (t.equals("final")) m |= Modifier.FINAL;
                else if (t.equals("interface")) m |= Modifier.INTERFACE;
                else if (t.equals("native")) m |= Modifier.NATIVE;
                else if (t.equals("private")) m |= Modifier.PRIVATE;
                else if (t.equals("protected")) m |= Modifier.PROTECTED;
                else if (t.equals("public")) m |= Modifier.PUBLIC;
                else if (t.equals("static")) m |= Modifier.STATIC;
                else if (t.equals("strictfp")) m |= Modifier.STRICT;
                else if (t.equals("synchronized")) m |= Modifier.SYNCHRONIZED;
                else if (t.equals("transient")) m |= Modifier.TRANSIENT;
                else if (t.equals("varargs")) m |= Modifier.TRANSIENT;
                else if (t.equals("volatile")) m |= Modifier.VOLATILE;
                else {
                    setLookAhead(t); // not a modifier
                    break;
                }
            }
            //log.println("parseModifiers() : '" + Modifier.toString(m) + "'");
            return m;
        }
    
        /**
         * Parses a (qualified) identifier.
         * @return <code>null</code> if the next token is not an identifier
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String parseIdentifier()
            throws IOException, ParseException {
            String t = scanIdentifier();
            if (t != null) {
                // parse dot-connected identifiers
                final StringBuffer id = new StringBuffer(t);
                String tt = parseToken();
                while (tt.equals(".")) {
                    id.append(".");
                    tt = parseIdentifier();
                    if (tt == null) {
                        throw new ParseException(msgUnexpectedToken(tt), 0);
                    }
                    id.append(tt);
                    tt = parseToken();
                }
                setLookAhead(tt); // not a dot token
                t = id.toString();
            }
            //log.println("parseIdentifier() : '" + t + "'");
            return t;
        }

        /**
         * Parses the next token(s) and validates that it is an identifier.
         * @return never <code>null</code>
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String demandIdentifier()
            throws IOException, ParseException {
            final String id = parseIdentifier();
            if (id == null) {
                throw new ParseException(msgUnexpectedToken(parseToken()), 0);
            }
            return id;
        }
    
        /**
         * Parses a comma-separated list of identifiers.
         * @return never <code>null</code>
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String[] demandIdentifierList()
            throws IOException, ParseException {
            final List<String> ids = new ArrayList<>();
            ids.add(demandIdentifier());
            String t;
            while ((t = parseToken()).equals(",")) {
                ids.add(demandIdentifier());
            }
            setLookAhead(t); // not an identifier
            return ids.toArray(new String[ids.size()]);
        }
    
        /**
         * Parses a type expression.
         * @return <code>null</code> if the next token is not a type
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String parseType()
            throws IOException, ParseException {
            String t = parseIdentifier();
            if (t != null) {
                // parse array dimensions
                final StringBuffer type = new StringBuffer(t);
                while ((t = parseToken()).equals("[")) {
                    demandToken("]");
                    type.append("[]");
                }
                setLookAhead(t); // not an open bracket token
                t = type.toString();
            }
            //log.println("parseType() : '" + t + "'");
            return t;
        }
    
        /**
         * Parses the next token and validates that it is a type expression.
         * @return never <code>null</code>
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String demandType()
            throws IOException, ParseException {
            final String id = parseType();
            if (id == null) {
                throw new ParseException(msgUnexpectedToken(parseToken()), 0);
            }
            return id;
        }
    
        /**
         * Parses a comma-separated parameter list.
         * @return never <code>null</code>
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String[] parseParameterList()
            throws IOException, ParseException {
            final List<String> types = new ArrayList<>();
            String t = parseType();
            if (t != null) {
                types.add(t);
                parseIdentifier(); // optional parameter name
                while ((t = parseToken()).equals(",")) {
                    types.add(demandType());
                    parseIdentifier(); // optional parameter name
                }
                setLookAhead(t); // not a comma token
            }
            return types.toArray(new String[types.size()]);
        }
    
        /**
         * Parses a class member declaration and provides the information
         * to a field, constructor, or method handler.
         * @return <code>null</code> if there's no member declaration
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        protected String parseMember()
            throws IOException, ParseException {
            // parse optional modifiers, type, and member name
            final int mods = parseModifiers();
            final String typeOrName = parseType();
            if (typeOrName == null) {
                if (mods != 0) {
                    throw new ParseException(msgUnexpectedEOF(), 0);
                }
                return null; // no member to parse
            }
            final String memberName = parseIdentifier(); // null if constructor
            
            // parse optional field value or parameter+exception list
            final String value;
            final String[] params;
            final String[] excepts;
            {
                final String tvp = parseToken();
                if (tvp.equals(";")) {
                    value = null;
                    params = null;
                    excepts = null;
                } else if (tvp.equals("=")) {
                    // parse field value
                    value = demandLiteral();
                    demandToken(";");
                    params = null;
                    excepts = null;
                } else if (tvp.equals("(")) {
                    // parse optional parameter and exception list
                    params = parseParameterList();
                    demandToken(")");
                    final String tt = parseToken();
                    if (tt.equals("throws")) {
                        excepts = demandIdentifierList();
                        demandToken(";");
                        value = null;
                    } else if (tt.equals(";")) {
                        excepts = new String[]{};
                        value = null;
                    } else if (tt.equals("default")) {
                        value = demandElementValue();
                        demandToken(";");
                        excepts = new String[]{};
                    } else {
                        throw new ParseException(msgUnexpectedToken(tt), 0);
                    }
                } else {
                    throw new ParseException(msgUnexpectedToken(tvp), 0);
                }
            }
        
            // verify field, constructor, or method
            String name = memberName;
            if (params == null) {
                checkField(mods, typeOrName, memberName, value);
            } else {
                if (memberName == null) {
                    name = typeOrName;
                    checkConstructor(mods, params, excepts);
                } else {
                    checkMethod(mods, typeOrName, memberName, params, excepts, value);
                }
            }

            //log.println("parseMember() : " + name);
            return name;
        }

        /**
         * Parses a class definition and provides the information
         * to a handler.
         * @return <code>null</code> if there's no class definition
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        @SuppressWarnings("empty-statement")
        protected String parseClass()
            throws IOException, ParseException {
            // parse optional modifiers, class token, and class name
            if (!skip()) {
                return null; // eof, no class to parse
            }
            final int mods = parseModifiers();
            final String tc = parseToken();
            if (!tc.equals("class")) { // token 'interface' parsed as modifier
                setLookAhead(tc);
            }
            final String name = demandIdentifier();
        
            // parse optional extends and implements clauses
            final String[] ext;
            final String[] impl;
            {
                String tei = parseToken();
                if (tei.equals("extends")) {
                    ext = demandIdentifierList();
                    tei = parseToken();
                } else {
                    ext = new String[]{};
                }
                if (((mods & Modifier.INTERFACE) == 0)
                    && tei.equals("implements")) {
                    impl = demandIdentifierList();
                    tei = parseToken();
                } else {
                    impl = new String[]{};
                }
                if (!tei.equals("{")) {
                    throw new ParseException(msgUnexpectedToken(tei), 0);
                }
            }
        
            // verify class header
            checkClass(mods, name, ext, impl);

            // process members; empty-statement intentional
            while (parseMember() != null);
            demandToken("}");

            // verify class
            postCheckClass();

            //log.println("parseClass() : " + name);
            return name;
        }

        /**
         * Parses a list of signature descriptor files and processes
         * the class definitions.
         * @param descrFileNames list of signature descriptor file names
         * @throws IOException IO error
         * @throws ParseException parsing error
         */
        @SuppressWarnings("empty-statement")
        public void parse(List<String> descrFileNames)
            throws IOException, ParseException {
            for (final String fileName : descrFileNames) {
                logInfo("");
                logInfo("parsing descriptor file: " + fileName);
                try {
                    descriptorFile = new File(fileName);
                    ir = new LineNumberReader(new FileReader(descriptorFile));
                    ir.setLineNumber(1);
                    setLookAhead(null);
                    // empty-statement intentional
                    while (parseClass() != null) ;
                } finally {
                    descriptorFile = null;
                    if (ir != null) {
                        ir.close();
                    }
                    setLookAhead(null);
                }
            }
        }
    }
    
    // ----------------------------------------------------------------------
    // Stand-Alone Command-Line Interface
    // ----------------------------------------------------------------------

    /** A writer for standard output. */
    protected static final PrintWriter out = new PrintWriter(System.out, true);

    /** Command line arguments */
    public static final List<String> descrFileNames = new ArrayList<>();

    /** Command line option 'quiet'.*/
    private static boolean optionQuiet;

    /** Command line option 'verbose'.*/
    private static boolean optionVerbose;

    /** Prints the CLI usage. */
    public static  void printUsage() {
        out.println();
        out.println("usage: SignatureVerifier [options] arguments");
        out.println("options:");
        out.println("  [-h|--help               print usage and exit]");
        out.println("  [-q|--quiet              only print error messages]");
        out.println("  [-v|--verbose            print verbose messages]");
        out.println("arguments:");
        out.println("  <signature descriptor file>...");
        out.println();
    }

    /**
     * Parses command line arguments.
     * @param args arguments
     * @return status code
     */
    public static int parseArgs(String[] args) {
        out.println("parse main() arguments");

        // parse this class' options and arguments
        for (String arg : args) {
            if (arg == null) {
                continue;
            }
            if (arg.equalsIgnoreCase("-h")
                    || arg.equalsIgnoreCase("--help")) {
                return -1;
            }
            if (arg.equalsIgnoreCase("-v")
                    || arg.equalsIgnoreCase("--verbose")) {
                optionVerbose = true;
                continue;
            }
            if (arg.equalsIgnoreCase("-q")
                    || arg.equalsIgnoreCase("--quiet")) {
                optionQuiet = true;
                continue;
            }
            if (arg.startsWith("-")) {
                out.println("Usage Error: unknown option " + arg);
                return -1;
            }
            // collect argument
            descrFileNames.add(arg);
        }

        // print args
        if (false) {
            out.println("descrFileNames = {");
            for (String descrFileName : descrFileNames) {
                out.println("                     " + descrFileName);
            }
            out.println("                 }");
            out.println("optionQuiet    = " + optionQuiet);
            out.println("optionVerbose  = " + optionVerbose);
        }

        // check args
        if (descrFileNames.isEmpty()) {
            out.println("Usage Error: Missing argument "
                        + "<signature descriptor file>...");
            return -1;
        }
        return 0;
    }

    /**
     * Runs the signature test and exits with a status code.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        out.println("run SignatureVerifier ...");
        if (parseArgs(args) != 0) {
            printUsage();
            out.println("abort.");
            System.exit(-1);
        }

        int status = 0;
        try {
            final SignatureVerifier s
                = new SignatureVerifier(out, optionQuiet, optionVerbose);
            status = s.test(descrFileNames);
        } catch (IOException ex) {
            out.println("ERROR: exception caught: " + ex);
            //ex.printStackTrace();
            out.println("abort.");
            System.exit(-2);
        } catch (ParseException ex) {
            out.println("ERROR: exception caught: " + ex);
            //ex.printStackTrace();
            out.println("abort.");
            System.exit(-3);
        }

        out.println();
        out.println("done.");
        System.exit(status);
    }
}
