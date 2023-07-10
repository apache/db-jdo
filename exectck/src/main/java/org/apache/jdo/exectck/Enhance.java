/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jdo.exectck;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jdo.JDOEnhancer;
import javax.jdo.JDOHelper;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/** Goal that enhances test classes for testing a JDO implementation. */
@Mojo(name = "enhance")
public class Enhance extends AbstractTCKMojo {

  private static final String APACHE_DIR_NAME = "apache"; // NOI18N
  private static final String JDO_DIR_NAME = "jdo"; // NOI18N
  private static final String ENHANCED_DIR_NAME = "enhanced"; // NOI18N
  private static final String ORG_DIR_NAME = "org"; // NOI18N
  private static final String TCK_DIR_NAME = "tck"; // NOI18N

  private static final String[] PC_PKG_DIRS = {
    ORG_DIR_NAME
        + File.separator
        + APACHE_DIR_NAME
        + File.separator
        + JDO_DIR_NAME
        + File.separator
        + TCK_DIR_NAME
        + File.separator
        + "api"
        + File.separator,
    ORG_DIR_NAME
        + File.separator
        + APACHE_DIR_NAME
        + File.separator
        + JDO_DIR_NAME
        + File.separator
        + TCK_DIR_NAME
        + File.separator
        + "pc"
        + File.separator,
    ORG_DIR_NAME
        + File.separator
        + APACHE_DIR_NAME
        + File.separator
        + JDO_DIR_NAME
        + File.separator
        + TCK_DIR_NAME
        + File.separator
        + "models"
        + File.separator
        + "inheritance"
        + File.separator
  };
  /** Location of TCK generated output. */
  @Parameter(property = "jdo.tck.doEnhance", defaultValue = "true", required = true)
  private boolean doEnhance;

  /** Root of the TCK source installation. */
  @Parameter(
      property = "project.src.directory",
      defaultValue = "${basedir}/src/main/resources",
      required = true)
  private String srcDirectory;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    if (!doEnhance) {
      System.out.println("Skipping Enhance goal!");
      return;
    }

    idtypes = new HashSet<String>();
    PropertyUtils.string2Set(identitytypes, idtypes);

    // Reset logfile content (may not be empty if previous run crashed)
    resetFileContent(implLogFile);

    // Create directory for enhancer logs
    String enhanceLogsDirName = logsDirectory + File.separator + ENHANCED_DIR_NAME;
    File enhancerLogsDir = new File(enhanceLogsDirName);
    if (!(enhancerLogsDir.exists()) && !(enhancerLogsDir.mkdirs())) {
      throw new MojoExecutionException("Failed to create directory " + enhancerLogsDir);
    }

    try {
      copyLog4j2ConfigurationFile();
    } catch (IOException ex) {
      Logger.getLogger(Enhance.class.getName()).log(Level.SEVERE, null, ex);
    }

    // Create directory for enhanced classes
    String enhancedDirName =
        buildDirectory
            + File.separator
            + ENHANCED_DIR_NAME
            + File.separator
            + impl
            + File.separator;
    File enhancedDir = new File(enhancedDirName);
    if (!(enhancedDir.exists()) && !(enhancedDir.mkdirs())) {
      throw new MojoExecutionException("Failed to create directory " + enhancedDir);
    }

    String[] metadataExtensions = {"jdo", "jdoquery", "orm", "xml", "properties"};
    String[] srcDirs = {"jdo", "orm", "testdata"};
    String classesDirName = buildDirectory + File.separator + "classes" + File.separator;
    //    ArrayList<String> classes = null;

    // Copy metadata from src to enhanced
    for (String idtype : idtypes) {
      ArrayList<String> classes = null;
      String enhancedIdDirName = enhancedDirName + idtype + File.separator;
      for (String srcDir : srcDirs) {
        copyMetadata(idtype, srcDir, metadataExtensions, enhancedDirName);

        // Copy pc and pa classes from target/classes to enhanced
        // TODO The original code resets "classes" in every loop
        // ArrayList<String> classes = copyPClasses(classesDirName, enhancedIdDirName);
        classes = copyPClasses(classesDirName, enhancedIdDirName);
        // ...
        // classes = new ArrayList<>(); // TODO Was this a bug????
        // ...
      }

      // Enhance classes

      // Build ClassLoader for finding enhancer
      ClassLoader enhancerLoader = buildClassLoader(classesDirName, enhancedIdDirName);

      // Context classloader for finding log4j2 configuration
      ClassLoader prevCl = Thread.currentThread().getContextClassLoader();
      insertLog4jClassLoader(classesDirName, enhancedIdDirName);

      System.out.println("ClassLoader ContextClassLoader:");
      Utilities.printClasspath(Thread.currentThread().getContextClassLoader());
      System.out.println("Get enhancer");
      JDOEnhancer enhancer = JDOHelper.getEnhancer(enhancerLoader);
      System.out.println("enhancer.setVerbose()");
      enhancer.setVerbose(true);
      System.out.println("enhancer.setClassLoader()");
      enhancer.setClassLoader(enhancerLoader);
      String[] classArr = classes.toArray(new String[0]);
      enhancer.addClasses(classArr);
      System.out.println("Enhancing classes for identity type " + idtype);
      // enhancer needs  org/apache/jdo/tck/util/DeepEquality
      enhancer.enhance();
      Thread.currentThread().setContextClassLoader(prevCl);

      // Move log to per-test location
      moveLogs(idtype);
    }
    System.out.println();
  }

  private void copyMetadata(
      String idType, String srcDir, String[] metadataExtensions, String enhancedDirName)
      throws MojoExecutionException, MojoFailureException {
    // Copy metadata from src to enhanced
    String srcDirName = srcDirectory + File.separator + srcDir;
    // iterator over list of abs name of metadata files in src
    Iterator<File> fi = FileUtils.iterateFiles(new File(srcDirName), metadataExtensions, true);

    while (fi.hasNext()) {
      String fromFileName = null;
      String toFileName = null;
      try {
        File fromFile = fi.next();
        fromFileName = fromFile.toString();
        int startIdx = -1;
        if ((startIdx = fromFileName.indexOf(idType + File.separator)) > -1) {
          // fully specified name of file (idtype + package + filename)
          String pkgName = fromFileName.substring(startIdx);
          File toFile = new File(enhancedDirName + File.separator + pkgName);
          toFileName = toFile.toString();
          FileUtils.copyFile(fromFile, toFile);
        } else if (srcDir.equals("testdata")) {
          startIdx = fromFileName.indexOf("org" + File.separator);
          String pkgName = fromFileName.substring(startIdx);
          File toFile =
              new File(enhancedDirName + File.separator + idType + File.separator + pkgName);
          toFileName = toFile.toString();
          FileUtils.copyFile(fromFile, toFile);
        } else {
          // idType not in pathname, do not copy
        }
      } catch (IOException ex) {
        throw new MojoExecutionException(
            "Failed to copy files from "
                + fromFileName
                + " to "
                + toFileName
                + ": "
                + ex.getLocalizedMessage());
      }
    }
  }

  private ArrayList<String> copyPClasses(String classesDirName, String enhancedIdDirName)
      throws MojoExecutionException, MojoFailureException {
    // Copy pc and pa classes from target/classes to enhanced
    String[] extensions = {"class"};
    ArrayList<String> classes = new ArrayList<>();
    for (String pcPkgName : PC_PKG_DIRS) {
      // iterator over list of abs name of class files in target/classes
      Iterator<File> fi =
          FileUtils.iterateFiles(new File(classesDirName + pcPkgName), extensions, true);
      while (fi.hasNext()) {
        String fromFileName = null;
        String toFileName = null;
        try {
          File fromFile = fi.next();
          fromFileName = fromFile.toString();
          // fully specified name of file (package + filename)
          int index = fromFileName.indexOf(pcPkgName);
          if (index == -1) {
            throw new MojoExecutionException(
                "Cannot get index of package path " + pcPkgName + " in file name" + fromFileName);
          }
          File toFile = new File(enhancedIdDirName + fromFileName.substring(index));
          toFileName = toFile.toString();
          FileUtils.copyFile(fromFile, toFile);
          classes.add(toFile.toString());
        } catch (IOException ex) {
          throw new MojoExecutionException(
              "Failed to copy files from "
                  + fromFileName
                  + " to "
                  + toFileName
                  + ": "
                  + ex.getLocalizedMessage());
        }
      }
    }
    return classes;
  }

  private ClassLoader buildClassLoader(String classesDirName, String enhancedIdDirName) {
    // Enhance classes

    // Build ClassLoader for finding enhancer
    URL[] classPathURLs1 = new URL[2];
    ArrayList<URL> cpList1 = new ArrayList<>();
    ClassLoader enhancerLoader = null;
    try {
      // Must add enhancedIdDirName first!!
      cpList1.add((new File(enhancedIdDirName)).toURI().toURL());
      cpList1.add((new File(classesDirName)).toURI().toURL());
      for (String dependency : this.dependencyClasspath.split(File.pathSeparator)) {
        cpList1.add(new File(dependency).toURI().toURL());
      }
      enhancerLoader =
          new URLClassLoader(cpList1.toArray(classPathURLs1), getClass().getClassLoader());
      System.out.println("ClassLoader enhancerLoader:");
      Utilities.printClasspath(enhancerLoader);
    } catch (MalformedURLException ex) {
      Logger.getLogger(Enhance.class.getName()).log(Level.SEVERE, null, ex);
    }
    return enhancerLoader;
  }

  private void insertLog4jClassLoader(String classesDirName, String enhancedIdDirName) {
    // Context classloader for finding log4j2 configuration
    ClassLoader prevCl = Thread.currentThread().getContextClassLoader();
    try {
      URL enhancedClassesUrl = (new File(enhancedIdDirName)).toURI().toURL();
      // Classes dir needed for org.apache.jdo.tck.util.TCKFileAppender
      URL classesUrl = (new File(classesDirName)).toURI().toURL();
      ClassLoader loggingPropsCl =
          URLClassLoader.newInstance(new URL[] {enhancedClassesUrl, classesUrl}, prevCl);
      Thread.currentThread().setContextClassLoader(loggingPropsCl);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void moveLogs(String idType) {
    // Move log to per-test location
    String idname = "dsid";
    if (idType.trim().equals("applicationidentity")) {
      idname = "app";
    }
    String testLogFilename =
        logsDirectory
            + File.separator
            + ENHANCED_DIR_NAME
            + File.separator
            + idname
            + "-"
            + impl
            + ".txt";
    System.out.println("testLogFilename is " + testLogFilename);
    try {
      File logFile = new File(implLogFile);
      File testLogFile = new File(testLogFilename);
      FileUtils.copyFile(logFile, testLogFile);
      // reset file content
      FileUtils.write(logFile, "", Charset.defaultCharset());
      FileUtils.forceDeleteOnExit(logFile);
    } catch (Exception e) {
      System.out.println(">> Error moving implementation log file: " + e.getMessage());
    }
  }
}
