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

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.jdo.JDOEnhancer;
import javax.jdo.JDOHelper;

/**
 * Goal that enhances test classes for testing a JDO implementation.
 *
 * @goal enhance
 *
 * @phase integration-test
 */
public class Enhance extends AbstractTCKMojo {

    private static final String[] PC_PKG_DIRS = {
        "org" + File.separator + "apache" + File.separator + "jdo" + File.separator + "tck" + File.separator + "api" + File.separator,
        "org" + File.separator + "apache" + File.separator + "jdo" + File.separator + "tck" + File.separator + "pc" + File.separator,
        "org" + File.separator + "apache" + File.separator + "jdo" + File.separator + "tck" + File.separator + "models" + File.separator + "inheritance" + File.separator
    };
    /**
     * Location of TCK generated output.
     * @parameter property="jdo.tck.doEnhance"
     *      default-value="true"
     * @required
     */
    private boolean doEnhance;
    /**
     * Root of the TCK source installation.
     * @parameter property="project.src.directory"
     *      default-value="${basedir}/src/main/resources"
     * @required
     */
    private String srcDirectory;

    /**
     * List of identity types to be tested.
     * @parameter property="jdo.tck.identitytypes"
     *      default-value="applicationidentity datastoreidentity"
     * @required
     */
    private String identitytypes;

    private Collection<String> idtypes;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (!doEnhance) {
            System.out.println("Skipping Enhance goal!");
            return;
        }

        idtypes = new HashSet<String>();
        PropertyUtils.string2Set(identitytypes, idtypes);

        // Create directory for enhancer logs
        String enhanceLogsDirName = logsDirectory + File.separator + "enhanced";
        File enhancerLogsDir = new File(enhanceLogsDirName);
        if (!(enhancerLogsDir.exists()) && !(enhancerLogsDir.mkdirs())) {
            throw new MojoExecutionException("Failed to create directory "
                    + enhancerLogsDir);
        }

        try {
            copyLog4jPropertiesFile();
        } catch (IOException ex) {
            Logger.getLogger(Enhance.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Create directory for enhanced classes
        String enhancedDirName = buildDirectory + File.separator + "enhanced"
                + File.separator + impl + File.separator;
        File enhancedDir = new File(enhancedDirName);
        if (!(enhancedDir.exists()) && !(enhancedDir.mkdirs())) {
            throw new MojoExecutionException("Failed to create directory "
                    + enhancedDir);
        }

        String[] metadataExtensions = {"jdo", "jdoquery", "orm", "xml", "properties"};
        String[] srcDirs = {"jdo", "orm", "testdata"};
        File toFile = null;
        File fromFile = null;
        String fromFileName = null;
        String pkgName = null;
        int startIdx = -1;
        Iterator<File> fi = null;
        String[] classArray = new String[10];
        String enhancedIdDirName = null;
        String classesDirName = buildDirectory + File.separator
                        + "classes" + File.separator;
        ArrayList<String> classes = null;

        // Copy metadata from src to enhanced
        for (String idtype : idtypes) {
            for (String srcDir : srcDirs) {
                String srcDirName = srcDirectory + File.separator + srcDir;
                // iterator over list of abs name of metadata files in src
                fi = FileUtils.iterateFiles(
                        new File(srcDirName), metadataExtensions, true);

                while (fi.hasNext()) {
                    try {
                        fromFile = fi.next();
                        fromFileName = fromFile.toString();
                        if ((startIdx = fromFileName.indexOf(idtype + File.separator)) > -1) {
                            // fully specified name of file (idtype + package + filename)
                            pkgName = fromFileName.substring(startIdx);
                            toFile = new File(enhancedDirName + File.separator
                                    + pkgName);
                            FileUtils.copyFile(fromFile, toFile);
                        } else if (srcDir.equals("testdata")) {
                            startIdx = fromFileName.indexOf("org" + File.separator);
                            pkgName = fromFileName.substring(startIdx);
                            toFile = new File(enhancedDirName + File.separator
                                    + idtype + File.separator + pkgName);
                            FileUtils.copyFile(fromFile, toFile);
                        } else {
                            continue;  // idtype not in pathname, do not copy
                        }
                    } catch (IOException ex) {
                        throw new MojoExecutionException("Failed to copy files from "
                                + fromFileName + " to " + toFile.toString()
                                + ": " + ex.getLocalizedMessage());
                    }
                }

                // Copy pc and pa classes from target/classes to enhanced
                String[] extensions = {"class"};
                enhancedIdDirName = enhancedDirName + idtype + File.separator;
                classes = new ArrayList<String>();
                for (String pcPkgName : PC_PKG_DIRS) {
                    // iterator over list of abs name of class files in target/classes
                    fi = FileUtils.iterateFiles(
                            new File(classesDirName + pcPkgName), extensions, true);
                    while (fi.hasNext()) {
                        try {
                            fromFile = fi.next();
                            fromFileName = fromFile.toString();
                            // fully specified name of file (package + filename)
                            int index = fromFileName.indexOf(pcPkgName);
                            if (index == -1) {
                                throw new MojoExecutionException(
                                        "Cannot get index of package path " + pcPkgName
                                        + " in file name" + fromFileName);
                            }
                            toFile = new File(enhancedIdDirName + fromFileName.substring(index));
                            FileUtils.copyFile(fromFile, toFile);
                            classes.add(toFile.toString());
                        } catch (IOException ex) {
                            throw new MojoExecutionException("Failed to copy files from "
                                    + fromFileName + " to " + toFile.toString()
                                    + ": " + ex.getLocalizedMessage());
                        }
                    }
                }
            }

            // Enhance classes

            // Build ClassLoader for finding enhancer
            URL[] classPathURLs1 = new URL[2];
            ArrayList<URL> cpList1 = new ArrayList<URL>();
            ClassLoader enhancerLoader = null;
            try {
                // Must add enhancedIdDirName first!!
                cpList1.add((new File(enhancedIdDirName)).toURI().toURL());
                for (String dependency : this.dependencyClasspath.split(":")) {
                    cpList1.add(new File(dependency).toURI().toURL());
                }
                enhancerLoader = new URLClassLoader(cpList1.toArray(classPathURLs1),
                        getClass().getClassLoader());
                System.out.println("ClassLoader enhancerLoader:");
                Utilities.printClasspath(enhancerLoader);
            } catch (MalformedURLException ex) {
                Logger.getLogger(Enhance.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Context classloader for finding log4j.properties
            ClassLoader prevCl = Thread.currentThread().getContextClassLoader();
            try {
                URL enhancedClassesUrl = (new File(enhancedIdDirName)).toURI().toURL();
                // Classes dir needed for org.apache.jdo.tck.util.TCKFileAppender
                URL classesUrl = (new File(classesDirName)).toURI().toURL();
                ClassLoader loggingPropsCl =
                        URLClassLoader.newInstance(new URL[]{
                        enhancedClassesUrl, classesUrl}, prevCl);
                Thread.currentThread().setContextClassLoader(loggingPropsCl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("ClassLoader ContextClassLoader:");
            Utilities.printClasspath(Thread.currentThread().getContextClassLoader());
            System.out.println("Get enhancer");
            JDOEnhancer enhancer = JDOHelper.getEnhancer(enhancerLoader);
            System.out.println("enhancer.setVerbose()");
            enhancer.setVerbose(true);
            System.out.println("enhancer.setClassLoader()");
            enhancer.setClassLoader(enhancerLoader);
            String[] classArr = classes.toArray(classArray);
            enhancer.addClasses(classArr);
            System.out.println("Enhancing classes for identity type " + idtype);
            // enhancer needs  org/apache/jdo/tck/util/DeepEquality
            enhancer.enhance();
            Thread.currentThread().setContextClassLoader(prevCl);

            // Move log to per-test location
            String idname = "dsid";
            if (idtype.trim().equals("applicationidentity")) {
                idname = "app";
            }
            String testLogFilename = logsDirectory + File.separator +
                    "enhanced" + File.separator + idname +
                    "-" + impl + ".txt";
            System.out.println("testLogFilename is " + testLogFilename);
            try {
                File logFile = new File(implLogFile);
                File testLogFile = new File(testLogFilename);
                FileUtils.copyFile(logFile, testLogFile);
            } catch (Exception e) {
                System.out.println(">> Error copying implementation log file: " +
                    e.getMessage());
            }
        }
        System.out.println("");
    }
}
