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


package org.apache.jdo.impl.enhancer.meta.prop;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.FileInputStream;

import java.util.Iterator;
import java.util.Properties;

import org.apache.jdo.impl.enhancer.meta.EnhancerMetaDataFatalError;
import org.apache.jdo.impl.enhancer.meta.EnhancerMetaDataUserException;
import org.apache.jdo.impl.enhancer.meta.ExtendedMetaData;
import org.apache.jdo.impl.enhancer.meta.util.EnhancerMetaDataBaseModel;




/**
 * Provides the JDO meta information based on properties.
 */
public class EnhancerMetaDataPropertyImpl
    extends EnhancerMetaDataBaseModel
    implements ExtendedMetaData
{
    /**
     * The model instance.
     */
    final private MetaDataProperties model;
    
    /**
     * Creates an instance.
     */
    public EnhancerMetaDataPropertyImpl(PrintWriter out,
                                        boolean verbose,
                                        Properties properties)
       throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        super(out, verbose);
        affirm(properties != null);
        model = new MetaDataProperties(properties);
        initModel();
        affirm(model != null);
        printMessage(getI18N("enhancer.metadata.using_properties",
                             "<unnamed>"));
    }

    /**
     *  Creates an instance.
     */
    public EnhancerMetaDataPropertyImpl(PrintWriter out,
                                        boolean verbose,
                                        String fileName)
       throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        super(out, verbose);
        affirm(fileName != null);

        InputStream stream = null;
        try {
            stream = new FileInputStream(fileName);
            final Properties properties = new Properties();
            properties.load(stream);
            model = new MetaDataProperties(properties);
            initModel();
        } catch (IOException ex) {
            final String msg
                = getI18N("enhancer.metadata.io_error", ex.getMessage());
            throw new EnhancerMetaDataFatalError(msg, ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    final String msg
                        = getI18N("enhancer.metadata.io_error",
                                  ex.getMessage());
                    throw new EnhancerMetaDataFatalError(msg, ex);
                }
            }
        }
        affirm(model != null);
        printMessage(getI18N("enhancer.metadata.using_properties", fileName));
    }

    // ----------------------------------------------------------------------
    
    /**
     * Initializes the model.
     */
    private void initModel()
    {
        // we'd like to have all classes (and fields) parsed and
        // cached in order to early report errors with the properties
        final String[] classNames = model.getKnownClassNames();
        affirm(classNames != null);
        for (int i = classNames.length - 1; i >= 0; i--) {
            final JDOClass clazz = getJDOClass(classNames[i]);
            affirm(clazz != null);
        }
    }

    /** 
     * Returns the JVM-qualified name of the specified field's declaring
     * class. The method first checks whether the class of the specified
     * classPath (the JVM-qualified name) declares such a field. If yes,
     * classPath is returned. Otherwise, it checks its superclasses. The
     * method returns <code>null</code> for an unkown field.
     * @param classPath the non-null JVM-qualified name of the class
     * @param fieldName the non-null name of the field
     * @return the JVM-qualified name of the declararing class of the
     * field, or <code>null</code> if there is no such field.
     */
    public String getDeclaringClass(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        String declaringClass = null;
        JDOField field = getJDOField(classPath, fieldName);
        if (field != null) {
            // this class declares the filed => return classPath
            declaringClass = classPath;
        } else {
            String superclass = getSuperClass(classPath);
            if (superclass != null) {
                declaringClass = getDeclaringClass(superclass, fieldName);
            }
        }
        return declaringClass;
    }

    /**
     * Declares a field to the JDO model passing its type information.
     */
    public void declareField(String classPath,
                             String fieldName,
                             String signature)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        affirm(isPersistenceCapableClass(classPath));
        // nothing to be done: the properties-based model doesn't
        // support default calculation of persistence modifiers
    }
    
    /**
     * Returns whether a class is known to be persistence-capable.
     */
    public boolean isPersistenceCapableClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        final JDOClass clazz = getJDOClass(classPath);
        return (clazz != null ? clazz.isPersistent() : false);
    }

    /**
     * Returns whether a class implements java.io.Serializable.
     * @param classPath the non-null JVM-qualified name of the class
     * @return true if this class is serializable; otherwise false
     */
    public boolean isSerializableClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        final JDOClass clazz = getJDOClass(classPath);
        return (clazz != null ? clazz.isSerializable() : false);
    }
    
    /**
     * Returns the name of the persistence-capable root class of a class.
     */
//@olsen: use the inherited method
/*
    public String getPersistenceCapableRootClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        String pcRootClass = null;
        for (String clazz = classPath;
             clazz != null;
             clazz = getSuperClass(clazz))  {
            if (isPersistenceCapableClass(clazz)) {
                pcRootClass = clazz;
            }
        }
        return pcRootClass;
    }
*/

    /**
     * Returns the name of the persistence-capable superclass of a class.
     */
    public String getPersistenceCapableSuperClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        for (String clazz = getSuperClass(classPath);
             clazz != null;
             clazz = getSuperClass(clazz))  {
            if (isPersistenceCapableClass(clazz)) {
                return clazz;
            }
        }
        return null;
    }

    /**
     *  Returns the superclass of a class.
     */
    public final String getSuperClass(String classname)
    {
        final JDOClass clazz = getJDOClass(classname);
        return (clazz != null ? clazz.getSuperClassName() : null);
    }

    /**
     * Returns the name of the key class of a persistence-capable class.
     */
    public String getKeyClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        final JDOClass clazz = getJDOClass(classPath);
        return (clazz != null ? clazz.getOidClassName() : null);
    }

    /**
     * Returns whether a field of a class is known to be non-managed.
     */
    public boolean isKnownNonManagedField(String classPath,
                                          String fieldName,
                                          String fieldSig)
    {
        final JDOClass clazz = getJDOClass(classPath);
        if (clazz == null) {
            return true;
        }
        final JDOField field = getJDOField(clazz, fieldName);
        return (field != null ? field.isKnownTransient() : false);
    }    

    /**
     * Returns whether a field of a class is transient transactional
     * or persistent.
     */
    public boolean isManagedField(String classPath, String fieldName)
    {
        final JDOField field = getJDOField(classPath, fieldName);
        return (field != null
                ? (field.isPersistent() | field.isTransactional()) : false);
    }

    /**
     * Returns whether a field of a class is known to be persistent.
     */
    public boolean isPersistentField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        final JDOField field = getJDOField(classPath, fieldName);
        return (field != null ? field.isPersistent() : false);
    }

    /**
     * Returns whether a field of a class is known to be transactional.
     */
    public boolean isTransactionalField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        final JDOField field = getJDOField(classPath, fieldName);
        return (field != null ? field.isTransactional() : false);
    }

    /**
     * Returns whether a field of a class is known to be Primary Key.
     */
    public boolean isKeyField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        final JDOField field = getJDOField(classPath, fieldName);
        return (field != null ? field.isKey() : false);
    }

    /**
     * Returns whether a field of a class is known to be part of the
     * Default Fetch Group.
     */
    public boolean isDefaultFetchGroupField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        final JDOField field = getJDOField(classPath, fieldName);
        return (field != null ? field.isInDefaultFetchGroup() : false);
    }

    /**
     * Returns the unique field index of a declared, persistent field of a
     * class.
     */
    public int getFieldNumber(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        final JDOClass clazz = getJDOClass(classPath);
        return (clazz != null ? clazz.getIndexOfField(fieldName) : -1);
    }

    /**
     * Returns an array of field names of all declared, persistent fields
     * of a class.
     */
    public String[] getManagedFields(String classname)
    {
        final JDOClass clazz = getJDOClass(classname);
        return (clazz != null ? clazz.getManagedFieldNames() : new String[]{});
    }

    /**
     *  Not member of EnhancerMetaData Interface.
     */
    public final String[] getKnownClasses()
    {
        return model.getKnownClassNames();
    }

    /**
     *  Gets all known fields of a class.
     */
    public final String[] getKnownFields(String classname)
    {
        final JDOClass clazz = getJDOClass(classname);
        return (clazz != null ? clazz.getFieldNames() : new String[]{});
    }


    /**
     *  Gets the access modifier of a class.
     */
    public final int getClassModifiers(String classname)
    {
        final JDOClass clazz = getJDOClass(classname);
        return (clazz != null ? clazz.getModifiers() : 0);
    }

    /**
     *  Gets the access modifier of a field.
     */
    public final int getFieldModifiers(String classname,
                                       String fieldname)
    {
        final JDOField field = getJDOField(classname, fieldname);
        return (field != null ? field.getModifiers() : 0);
    }

    public final String getFieldType(String classname,
                                     String fieldname)
    {
        final JDOField field = getJDOField(classname, fieldname);
        return (field != null ? field.getType() : null);
    }

    public final String[] getFieldType(String classname,
                                        String[] fieldnames)
    {
        final int n = (fieldnames != null ? fieldnames.length : 0);
        final String[] types = new String[n];
        for (int i = 0; i < n; i++) {
            types[i] = getFieldType(classname, fieldnames[i]);
        }
        return types;
    }

    public final int[] getFieldModifiers(String classname,
                                          String[] fieldnames)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        final int n = (fieldnames != null ? fieldnames.length : 0);
        final int[] mods = new int[n];
        for (int i = 0; i < n; i++) {
            mods[i] = getFieldModifiers(classname, fieldnames[i]);
        }
        return mods;
    }

    private final JDOClass getJDOClass(String classname)
        throws EnhancerMetaDataUserException
    {
        return model.getJDOClass(classname);
    }

    private final JDOField getJDOField(JDOClass clazz,
                                       String fieldname)
    {
        return (clazz != null ? clazz.getField(fieldname) : null);
    }

    private final JDOField getJDOField(String classname,
                                       String fieldname)
    {
        final JDOClass clazz = getJDOClass(classname);
        return getJDOField(clazz, fieldname);
    }

    // ----------------------------------------------------------------------
    
    public static void main(String[] argv)
    {
        final PrintWriter out = new PrintWriter(System.out, true);
        
        if (argv.length != 1) {
            System.err.println("No property file specified.");
            return;
        }

        final Properties p = new Properties();
        try {
            java.io.InputStream in =
                new java.io.FileInputStream(new java.io.File(argv[0]));
            p.load(in);
            in.close();
            out.println("PROPERTIES: " + p);
            out.println("############");
            MetaDataProperties props = new MetaDataProperties(p);
        } catch (Throwable ex) {
            ex.printStackTrace(System.err);
        }

        final EnhancerMetaDataPropertyImpl jdo
            = new EnhancerMetaDataPropertyImpl(out, true, p);
        final String[] classes = jdo.getKnownClasses();
        for (int k = 0; k < classes.length; k++) {
            final String clazz = classes[k];
            out.println("CLAZZ: " + clazz);
            out.println("\tpersistent: "
                        + jdo.isPersistenceCapableClass(clazz));
            out.println("\tpersistent root: "
                        + jdo.isPersistenceCapableRootClass(clazz));
            out.println("\tpersistent root class: "
                        + jdo.getPersistenceCapableRootClass(clazz));
            out.println("\tpersistent super class: "
                        + jdo.getPersistenceCapableSuperClass(clazz));
            out.println("\tkey class: "
                        + jdo.getKeyClass(clazz));

            final String[] fields = jdo.getKnownFields(clazz);
            for (int j = 0; j < fields.length; j++) {
                final String field = fields[j];
                out.println("FIELD: " + field);
                out.println("\tpersistent field: "
                            + jdo.isPersistentField(clazz, field));
                out.println("\tpk field: "
                            + jdo.isKeyField(clazz, field));
                out.println("\tdfg field: "
                            + jdo.isDefaultFetchGroupField(clazz, field));
                out.println("\tnumber: "
                            + jdo.getFieldNumber(clazz, field));

                final String[] names = jdo.getManagedFields(clazz);
                final int n = (fields != null ? names.length : 0);
                out.println("managed fields: number: " + n);
                for (int i = 0; i < n; i++) {
                    final String name = names[i];
                    out.println(i + ": " + name +
                                " number: "
                                + jdo.getFieldNumber(clazz, name) +
                                " pk: "
                                + jdo.isKeyField(clazz, name) +
                                " dfg: "
                                + jdo.isDefaultFetchGroupField(clazz, name));
                }
            }
        }
    }
}
