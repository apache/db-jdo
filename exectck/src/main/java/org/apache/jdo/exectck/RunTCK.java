/*
 * Copyright 2006-2012 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jdo.exectck;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.jdo.exectck.Utilities.InvocationResult;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal that runs the JDO TCK against the Reference Implementation (RI)
 * or an implementation under test (IUT).
 *
 * @goal runtck
 *
 * @phase integration-test
 */
public class RunTCK extends AbstractTCKMojo {

    /**
     * To skip running of TCK, set to false.
     * @parameter property="jdo.tck.doRunTCK"
     *      default-value=true
     * @required
     */
    private boolean doRunTCK;

    /**
     * To run the RunTCK plugin goal in verbose mode.
     * @parameter property="jdo.tck.runTCKVerbose"
     *      default-value=false
     * @required
     */
    private boolean runtckVerbose;

    /**
     * Run the TCK in a debugger.
     * @parameter property="jdo.tck.debugTCK"
     *      default-value=false
     * @required
     */
    private boolean debugTCK;

    /**
     * Implementation to be tested (jdori or iut).
     * Any value other than "jdori" will test an appropriately configured IUT
     * @parameter property="jdo.tck.impl"
     *      default-value="jdori"
     * @required
     */
    private String impl;

    /**
     * Location of third party libraries such as JNDI.
     * @parameter property="project.lib.ext.directory"
     *      default-value="${basedir}/../lib/ext"
     * @required
     */
    private String extLibsDirectory;

    /**
     * Location of implementation log file.
     * @parameter property="jdo.tck.impl.logfile"
     *      default-value="${user.dir}/datanucleus.txt"
     * @required
     */
    private String implLogFile;

    /**
     * Location of jar files for implementation under test.
     * @parameter property="project.lib.iut.directory"
     *      default-value="${basedir}/../lib/iut"
     * @required
     */
    private String iutLibsDirectory;
    /**
     * Location of jar files for implementation under test.
     * @parameter property="project.lib.iut.directory"
     *      default-value="${basedir}/../lib/jdori"
     * @required
     */
    private String jdoriLibsDirectory;
    /**
     * Name of file in src/conf containing pmf properties.
     * @parameter property="jdo.tck.pmfproperties"
     *      default-value="jdori-pmf.properties"
     * @optional
     */
    private String pmfProperties;

    /**
     * Name of file in src/conf containing property jdo.tck.exclude,
     *   whose value is a list of files to be excluded from testing.
     * @parameter property="jdo.tck.excludefile"
     *      default-value="exclude.list"
     * @required
     */
    private String exclude;

    /**
     * Run the TCK tests in verbose mode.
     * @parameter property="jdo.tck.verbose"
     *      default-value="false"
     * @optional
     */
    private String verbose;
    /**
     * To retain test output for debugging, set to false.
     * @parameter property="jdo.tck.cleanupaftertest"
     *      default-value="true"
     * @optional
     */
    private String cleanupaftertest;
    /**
     * Properties to use in accessing database.
     * @parameter property="database.runtck.sysproperties"
     *      default-value="-Dderby.system.home=${basedir}/target/database/derby"
     * @optional
     */
    private String dbproperties;    // NOTE: only allows for one db
    /**
     * Properties to use in accessing database.
     * @parameter property="jdo.tck.signaturefile"
     *      default-value="${basedir}/src/conf/jdo-3_1-signatures.txt"
     * @optional
     */
    private String signaturefile;
    /**
     * JVM properties.
     * @parameter property="jdo.tck.jvmproperties"
     *      default-value="-Xmx512m"
     * @optional
     */
    private String jvmproperties;

    /**
     * User-supplied arguments for debug directives.
     * @parameter property="jdo.tck.debug.jvmargs"
     *      default-value="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=${jdo.tck.debug.port}"
     * @optional
     */
    private String debugDirectives;

    /**
     * Class used to run a batch of tests.
     * @parameter property="jdo.tck.testrunnerclass"
     *      default-value="org.apache.jdo.tck.util.BatchTestRunner"
     * @required
     */
    private String testRunnerClass;

    /**
     * Class used to output test result and configuration information.
     * @parameter property="jdo.tck.resultprinterclass"
     *      default-value="org.apache.jdo.tck.util.BatchResultPrinter"
     * @required
     */
    private String resultPrinterClass;

    /**
     * Helper method returning the trimmed value of the specified property.
     * @param props the Properties object
     * @param ke the key of the property to be returned
     * @return the trimmed property value or the empty string if the property is not defined. +     */
    private String getTrimmedPropertyValue (Properties props, String key) {
        String value = props.getProperty(key);
        return value == null ? "" : value.trim();
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (!doRunTCK) {
            System.out.println("Skipping RunTCK goal!");
            return;
        }

        Properties props = null;
        boolean alreadyran = false;
        String runonce = "false";
        List<String> propsString = new ArrayList<String>();
        List<String> command;
        String cpString = null;
        InvocationResult result;
        File fromFile = null;
        File toFile = null;

        if (impl.equals("iut")) {
            pmfProperties="iut-pmf.properties";
        }

        if (cfgs == null) {
            if (cfgList != null) {
                cfgs = new ArrayList<String>();
                PropertyUtils.string2List(cfgList, (List<String>)cfgs);
            } else {
            	// Fallback to "src/conf/configurations.list"
            	setCfgListFromFile();
                if (cfgList != null) {
                    cfgs = new ArrayList<String>();
                    PropertyUtils.string2List(cfgList, (List<String>)cfgs);
                }

                if (cfgList == null) {
                    throw new MojoExecutionException(
                        "Could not find configurations to run TCK. " +
                        "Set cfgList parameter on command line or cfgs in pom.xml");
            	}
            }
        }

        PropertyUtils.string2Set(dblist, dbs);
        PropertyUtils.string2Set(identitytypes, idtypes);
        System.out.println("*>TCK to be run for implementation '" + impl +
                "' on \n" +
                " configurations: " + cfgs.toString() + "\n" +
                " databases: " + dbs.toString() + "\n" +
                " identitytypes: " + identitytypes.toString());

        // Properties required for test execution
        System.out.println("cleanupaftertest is " + cleanupaftertest);
        propsString.add("-DResultPrinterClass=" + resultPrinterClass);
        propsString.add("-Dverbose=" + verbose);
        propsString.add("-Djdo.tck.cleanupaftertest=" + cleanupaftertest);
        propsString.add("-DPMFProperties=" + buildDirectory + File.separator
                + "classes" + File.separator + pmfProperties);
        propsString.add("-DPMF2Properties=" + buildDirectory + File.separator
                + "classes" + File.separator + pmfProperties);
        String excludeFile = confDirectory + File.separator + exclude;
        propsString.add("-Djdo.tck.exclude="
                + getTrimmedPropertyValue(PropertyUtils.getProperties(excludeFile), "jdo.tck.exclude"));

        // Create configuration log directory
        String timestamp = Utilities.now();
        String thisLogDir = logsDirectory + File.separator + timestamp
                + File.separator;
        String cfgDirName = thisLogDir + "configuration";
        File cfgDir = new File(cfgDirName);
        if (!(cfgDir.exists()) && !(cfgDir.mkdirs())) {
            throw new MojoExecutionException("Failed to create directory "
                    + cfgDirName);
        }
        propsString.add("-Djdo.tck.log.directory=" + thisLogDir);

        // Copy JDO config files to classes dir
        try {
            fromFile = new File(confDirectory + File.separator + impl + "-jdoconfig.xml");
            toFile = new File(buildDirectory + File.separator + "classes" + 
                    File.separator + "META-INF" + File.separator + "jdoconfig.xml");
            FileUtils.copyFile(fromFile, toFile);
            fromFile = new File(confDirectory + File.separator + impl + "-persistence.xml");
            toFile = new File(buildDirectory + File.separator + "classes" +
                    File.separator + "META-INF" + File.separator + "persistence.xml");
            FileUtils.copyFile(fromFile, toFile);
        } catch (IOException ex) {
            Logger.getLogger(RunTCK.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Get ClassLoader URLs to build classpath below
        URL[] cpURLs = ((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs();
        ArrayList<URL> urlList = new ArrayList<URL>(Arrays.asList(cpURLs));

        // Get contents of pmf properties file to build new file below
        String pmfPropsReadFileName = confDirectory + File.separator + pmfProperties;
        String defaultPropsContents = "";
        try {
            defaultPropsContents = Utilities.readFile(pmfPropsReadFileName);
        } catch (IOException ex) {
            Logger.getLogger(RunTCK.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (String db : dbs) {
            System.setProperty("jdo.tck.database", db);
            alreadyran = false;

            for (String idtype : idtypes) {
                List<String> idPropsString = new ArrayList<String>();
                idPropsString.addAll(propsString);
                idPropsString.add("-Djdo.tck.identitytype=" + idtype);
                String enhancedDirName = buildDirectory + File.separator + "enhanced"
                        + File.separator + impl + File.separator + idtype + File.separator;
                File enhancedDir = new File(enhancedDirName);
                if (!(enhancedDir.exists())) {
                    throw new MojoExecutionException("Could not find enhanced directory "
                            + enhancedDirName + ". Execute Enhance goal before RunTCK.");
                }

                // Set classpath string: add new entries to URLS from loader
                ArrayList<URL> cpList = new ArrayList<URL>();
                cpList.addAll(urlList);
                try {
                    URL url1 = enhancedDir.toURI().toURL();
                    URL url2 = new File(buildDirectory + File.separator
                            + "classes" + File.separator).toURI().toURL();
                    if (runtckVerbose) {
                        System.out.println("url2 is " + url2.toString());
                    }
                    cpList.add(url1);
                    cpList.add(url2);
                    String[] jars = {"jar"};
                    Iterator<File> fi = FileUtils.iterateFiles(
                            new File(extLibsDirectory), jars, true);
                    while (fi.hasNext()) {
                        cpList.add(fi.next().toURI().toURL());
                    }
                    if (impl.equals("iut")) {
                        fi = FileUtils.iterateFiles(
                            new File(iutLibsDirectory), jars, true);
                        while (fi.hasNext()) {
                            cpList.add(fi.next().toURI().toURL());
                        }
                    } else {
                        fi = FileUtils.iterateFiles(
                            new File(jdoriLibsDirectory), jars, true);
                        while (fi.hasNext()) {
                            cpList.add(fi.next().toURI().toURL());
                        }
                    }
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(RunTCK.class.getName()).log(Level.SEVERE, null, ex);
                }
                cpString = Utilities.urls2ClasspathString(cpList);
                if (runtckVerbose) {
                    System.out.println("\nClasspath is " + cpString);
                }

                for (String cfg : cfgs) {
                    List<String> cfgPropsString = new ArrayList<String>();
                    cfgPropsString.addAll(idPropsString);
                    // Parse conf file and set properties String
                    props = PropertyUtils.getProperties(confDirectory
                            + File.separator + cfg);
                    cfgPropsString.add("-Djdo.tck.testdata="
                            + getTrimmedPropertyValue(props, "jdo.tck.testdata"));
                    cfgPropsString.add("-Djdo.tck.standarddata="
                            + getTrimmedPropertyValue(props, "jdo.tck.standarddata"));
                    cfgPropsString.add("-Djdo.tck.mapping.companyfactory="
                            + getTrimmedPropertyValue(props, "jdo.tck.mapping.companyfactory"));
//                    innerPropsString.append("-Djdo.tck.description=\"" +
//                            props.getProperty("jdo.tck.description") + "\"");
                    cfgPropsString.add("-Djdo.tck.requiredOptions="
                            + getTrimmedPropertyValue(props, "jdo.tck.requiredOptions"));
                    cfgPropsString.add("-Djdo.tck.signaturefile="
                            + signaturefile);
                    String mapping = getTrimmedPropertyValue(props, "jdo.tck.mapping");
                    if (mapping == null) {
                        throw new MojoExecutionException(
                                "Could not find mapping value in conf file: " + cfg);
                    }
                    String classes = getTrimmedPropertyValue(props, "jdo.tck.classes");
                    String excludeList = getTrimmedPropertyValue(
                            PropertyUtils.getProperties(excludeFile), "jdo.tck.exclude");
                    if (classes == null) {
                        throw new MojoExecutionException(
                                "Could not find classes value in conf file: " + cfg);
                    }
                    classes = Utilities.removeSubstrs(classes, excludeList);
                    if (classes.equals("")) {
                        System.out.println("Skipping configuration " + cfg +
                                ": classes excluded");
                        continue;
                    }
                    List<String> classesList = Arrays.asList(classes.split(" "));

                    cfgPropsString.add("-Djdo.tck.schemaname=" + idtype + mapping);
                    cfgPropsString.add("-Djdo.tck.cfg=" + cfg);

                    runonce = getTrimmedPropertyValue(props, "runOnce");
                    runonce = (runonce == null) ? "false" : runonce;

                    // Add Mapping and schemaname to properties file
                    StringBuffer propsFileData = new StringBuffer();
                    propsFileData.append("\n### Properties below added by maven 2 goal RunTCK.jdori");
                    propsFileData.append("\njavax.jdo.mapping.Schema=" + idtype + mapping);
                    mapping = (mapping.equals("0")) ? "" : mapping;
                    propsFileData.append("\njavax.jdo.option.Mapping=standard" + mapping);
                    propsFileData.append("\n");
                    String pmfPropsWriteFileName = buildDirectory + File.separator
                            + "classes" + File.separator + pmfProperties;
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(pmfPropsWriteFileName, false));
                        out.write(defaultPropsContents + propsFileData.toString());
                        out.close();
                    } catch (IOException ex) {
                        Logger.getLogger(RunTCK.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // build command line string
                    command = new ArrayList<String>();
                    command.add("java");
                    command.add("-cp");
                    command.add(cpString);
                    command.addAll(cfgPropsString);
                    command.add(dbproperties);
                    command.add(jvmproperties);
                    if (debugTCK) {
                        command.add(debugDirectives);
                    }
                    command.add(testRunnerClass);
                    command.addAll(classesList);

                    if (runonce.equals("true") && alreadyran) {
                        continue;
                    }

                    if (debugTCK) {
                        System.out.println("Using debug arguments: \n"
                                + debugDirectives);
                    }

                    // invoke class runner
                    System.out.print("*> Running tests for " + cfg +
                    		" with " + idtype +
                    		" on '" + db + "'" +
                            " mapping=" + mapping + " ... ");
                    try {
                        result = (new Utilities()).invokeTest(command);
                        if (result.getExitValue() == 0) {
                            System.out.println("success");
                        } else {
                            System.out.println("FAIL");
                        }
                        if (runtckVerbose) {
                            System.out.println("\nCommand line is: \n" + command.toString());
                            System.out.println("Test exit value is " + result.getExitValue());
                            System.out.println("Test result output:\n" + result.getOutputString());
                            System.out.println("Test result error:\n" + result.getErrorString());
                        }
                    } catch (java.lang.RuntimeException re) {
                        System.out.println("Exception on command " + command);
                    }

                    // Move log to per-test location
                    String idname = "dsid";
                    if (idtype.trim().equals("applicationidentity")) {
                        idname = "app";
                    }
                    String configName = cfg;
                    if (cfg.indexOf('.') > 0) {
                        configName = configName.substring(0, cfg.indexOf('.'));
                    }
                    String testLogFilename = thisLogDir
                            + idname + "-" + configName + "-" + impl + ".txt";
                    try {
                        File logFile = new File(implLogFile);
                        FileUtils.copyFile(logFile, new File(testLogFilename));
                    } catch (Exception e) {
                        System.out.println(">> Error copying implementation log file: "
                                + e.getMessage());
                    }

                    if (runonce.equals("true")) {
                        alreadyran = true;
                    }

                }
            }
        }
        // Remove log file
        try {
            FileUtils.forceDelete(new File(implLogFile));
        } catch (Exception e) {
            System.out.println(">> Error deleting log file: "
                    + e.getMessage());
        }

        // Output results
        command = new ArrayList<String>();
        command.add("java");
        command.add("-cp");
        command.add(cpString);
        command.add("org.apache.jdo.tck.util.ResultSummary");
        command.add(thisLogDir);
        result = (new Utilities()).invokeTest(command, new File(buildDirectory));

        // Create system configuration description file
        command.set(3, "org.apache.jdo.tck.util.SystemCfgSummary");
        command.set(4, cfgDirName);
        command.add("system_config.txt");
        result = (new Utilities()).invokeTest(command, new File(buildDirectory));

        // Copy metadata from enhanced to configuration logs directory
        for (String idtype : idtypes) {
            String fromDirName = buildDirectory + File.separator + "enhanced"
                    + File.separator + impl + File.separator + idtype + File.separator;
            String[] metadataExtensions = {"jdo", "jdoquery", "orm", "xml", "properties"};
            String fromFileName = null;
            String pkgName = null;
            int startIdx = -1;
            // iterator over list of abs name of metadata files in src
            Iterator<File> fi = FileUtils.iterateFiles(
                    new File(fromDirName), metadataExtensions, true);
            while (fi.hasNext()) {
                try {
                    fromFile = fi.next();
                    fromFileName = fromFile.toString();
//                    System.out.println("Copying " + fromFileName);
                    if ((startIdx = fromFileName.indexOf(idtype + File.separator)) > -1) {
                        // fully specified name of file (idtype + package + filename)
                        pkgName = fromFileName.substring(startIdx);
                        toFile = new File(cfgDirName + File.separator
                                + pkgName);
//                        System.out.println("Copy from source dir to " + toFile.toString());
                        FileUtils.copyFile(fromFile, toFile);
                    }
                } catch (IOException ex) {
                    throw new MojoExecutionException("Failed to copy files from "
                            + fromFileName + " to " + toFile.toString()
                            + ": " + ex.getLocalizedMessage());
                }
            }
        }
    }
}
