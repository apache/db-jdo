/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Goal that runs the JDO TCK against the Reference Implementation (RI) or an implementation under
 * test (IUT).
 */
@Mojo(name = "runtck")
public class RunTCK extends AbstractTCKMojo {

  private static final String TCK_PARAM_ON_FAILURE_FAIL_FAST = "failFast"; // NOI18N
  private static final String TCK_PARAM_ON_FAILURE_FAIL_EVENTUALLY = "failGoal"; // NOI18N
  private static final String TCK_PARAM_ON_FAILURE_LOG_ONLY = "logOnly"; // NOI18N

  private static final String FS = File.separator;

  private static final String CLASSES_DIR_NAME = "classes"; // NOI18N

  private static final String TCK_LOG_FILE = "tck.txt"; // NOI18N

  private static final String JUNIT_LOG_FILE = "junit.txt"; // NOI18N

  /** To skip running of TCK, set to false. */
  @Parameter(property = "jdo.tck.doRunTCK", defaultValue = "true", required = true)
  private boolean doRunTCK;

  /** To run the RunTCK plugin goal in verbose mode. */
  @Parameter(property = "jdo.tck.runTCKVerbose", defaultValue = "false", required = true)
  private boolean runtckVerbose;

  /** Define handling of TCK failures. */
  @Parameter(property = "jdo.tck.onFailure", defaultValue = "failGoal", required = true)
  private String onFailure;

  /** Run the TCK in a debugger. */
  @Parameter(property = "jdo.tck.debugTCK", defaultValue = "false", required = true)
  private boolean debugTCK;

  /** Location of third party libraries such as JNDI. */
  @Parameter(
      property = "project.lib.ext.directory",
      defaultValue = "${basedir}/../lib/ext",
      required = true)
  private String extLibsDirectory;

  /** Name of file in src/conf containing pmf properties. */
  @Parameter(property = "jdo.tck.pmfproperties", defaultValue = "jdori-pmf.properties")
  private String pmfProperties;

  /**
   * Name of file in src/conf containing property jdo.tck.exclude, whose value is a list of files to
   * be excluded from testing.
   */
  @Parameter(property = "jdo.tck.excludefile", defaultValue = "exclude.list", required = true)
  private String exclude;

  /** Run the TCK tests in verbose mode. */
  @Parameter(property = "jdo.tck.verbose", defaultValue = "false")
  private String verbose;

  /** To retain test output for debugging, set to false. */
  @Parameter(property = "jdo.tck.cleanupaftertest", defaultValue = "true")
  private String cleanupaftertest;

  /** Properties to use in accessing database. */
  @Parameter(
      property = "database.runtck.sysproperties",
      defaultValue = "-Dderby.system.home=${basedir}/target/database/derby")
  private String dbproperties; // NOTE: only allows for one db

  /** Properties to use in accessing database. */
  @Parameter(
      property = "jdo.tck.signaturefile",
      defaultValue = "${basedir}/src/main/resources/conf/jdo-signatures.txt")
  private String signaturefile;

  /** JVM properties. */
  @Parameter(property = "jdo.tck.jvmproperties", defaultValue = "-Xmx512m")
  private String jvmproperties;

  /** User-supplied arguments for debug directives. */
  @Parameter(
      property = "jdo.tck.debug.jvmargs",
      defaultValue =
          "-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=${jdo.tck.debug.port}")
  private String debugDirectives;

  /** Location of tck log file. */
  @Parameter(
      property = "jdo.tck.logfile",
      defaultValue = "${project.build.directory}/" + TCK_LOG_FILE,
      required = true)
  protected String tckLogFile;

  /** Class used to run a batch of tests. */
  @Parameter(
      property = "jdo.tck.testrunner.class",
      defaultValue = "org.junit.platform.console.ConsoleLauncher",
      required = true)
  private String testRunnerClass;

  /**
   * Output mode for test run. Use one of: none, summary, flat, tree, verbose, testfeed. If 'none'
   * is selected, then only the summary and test failures are shown. Default: tree.
   */
  @Parameter(property = "jdo.tck.testrunner.details", defaultValue = "tree", required = true)
  private String testRunnerDetails;

  /**
   * Whether to display colors in the junit result log file (jdo.tck.testrunner.colors=enable) or
   * not (jdo.tck.testrunner.colors=disable).
   */
  @Parameter(property = "jdo.tck.testrunner.colors", defaultValue = "disable", required = true)
  private String testRunnerColors;

  /**
   * Whether to display colors in the junit result log file (jdo.tck.testrunner.colors=enable) or
   * not (jdo.tck.testrunner.colors=disable).
   */
  @Parameter(property = "jdo.tck.parallel.execution", defaultValue = "true", required = true)
  private boolean testParallelExecution;

  /** Whether the datastore support query canceling. */
  @Parameter(property = "jdo.tck.datastore.supportsQueryCancel", required = false)
  private String datastoreSupportsQueryCancel;

  /**
   * Helper method returning the trimmed value of the specified property.
   *
   * @param props the Properties object
   * @param key the key of the property to be returned
   * @return the trimmed property value or the empty string if the property is not defined. +
   */
  private String getTrimmedPropertyValue(Properties props, String key) {
    String value = props.getProperty(key);
    return value == null ? "" : value.trim();
  }

  private static String fileToString(String fileName) {
    try {
      byte[] encoded = Files.readAllBytes(Paths.get(fileName));
      return new String(encoded);
    } catch (IOException ex) {
      return "Problems reading " + fileName + ": " + ex.getMessage();
    }
  }

  /** */
  @Override
  public void execute() throws MojoExecutionException {
    if (!doRunTCK) {
      System.out.println("Skipping RunTCK goal!");
      return;
    }

    boolean alreadyRan = false;
    boolean runOnce = false;
    String cpString = null;

    List<String> propsString = initTCKRun();
    String excludeFile = confDirectory + FS + exclude;
    propsString.add(
        "-Djdo.tck.exclude="
            + getTrimmedPropertyValue(PropertyUtils.getProperties(excludeFile), "jdo.tck.exclude"));

    // Create configuration log directory
    String logDir = logsDirectory + FS + Utilities.now();
    String cfgDirName = logDir + FS + "configuration";
    File cfgDir = new File(cfgDirName);
    if (!cfgDir.exists() && !cfgDir.mkdirs()) {
      throw new MojoExecutionException("Failed to create directory " + cfgDirName);
    }
    propsString.add("-Djdo.tck.log.directory=" + logDir);

    copyConfigurationFiles();

    // Get ClassLoader URLs to build classpath below
    List<URL> urlList =
        new ArrayList<>(
            Arrays.asList(
                ((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs()));

    // Get contents of pmf properties file to build new file below
    String pmfPropsReadFileName = confDirectory + FS + pmfProperties;
    String defaultPropsContents = "";
    try {
      defaultPropsContents = Utilities.readFile(pmfPropsReadFileName);
    } catch (IOException ex) {
      Logger.getLogger(RunTCK.class.getName()).log(Level.SEVERE, null, ex);
    }

    // Reset logfile content (may not be empty if previous run crashed)
    resetFileContent(implLogFile);
    resetFileContent(tckLogFile);

    int failureCount = 0;
    for (String db : dbs) {
      System.setProperty("jdo.tck.database", db);
      alreadyRan = false;

      for (String idtype : idtypes) {
        List<String> idPropsString = new ArrayList<>();
        idPropsString.addAll(propsString);
        idPropsString.add("-Djdo.tck.identitytype=" + idtype);
        String enhancedDirName = buildDirectory + FS + "enhanced" + FS + impl + FS + idtype + FS;
        File enhancedDir = new File(enhancedDirName);
        if (!enhancedDir.exists()) {
          throw new MojoExecutionException(
              "Could not find enhanced directory "
                  + enhancedDirName
                  + ". Execute Enhance goal before RunTCK.");
        }

        // get classpath string: add new entries to URLS from loader
        cpString = getClasspathString(urlList, enhancedDir);

        for (String cfg : cfgs) {
          // Parse conf file and set properties String
          String confFileName = confDirectory + FS + cfg;
          if (!new File(confFileName).exists()) {
            // Conf file nor found => continue
            System.out.println("ERROR: Configuration file " + confFileName + " not found.");
            continue;
          }
          Properties props = PropertyUtils.getProperties(confDirectory + FS + cfg);
          String mapping = getTrimmedPropertyValue(props, "jdo.tck.mapping");
          if (mapping == null) {
            throw new MojoExecutionException("Could not find mapping value in conf file: " + cfg);
          }
          List<String> classesList = getTestClasses(props, cfg, excludeFile);
          if (classesList.isEmpty()) {
            System.out.println("Skipping configuration " + cfg + ": classes excluded");
            continue;
          }

          String runonceString = getTrimmedPropertyValue(props, "runOnce");
          runOnce = runonceString != null && "true".equalsIgnoreCase(runonceString);
          if (runOnce && alreadyRan) {
            continue;
          }

          // Add Mapping and schemaname to properties file
          writePMFPropsFile(idtype, mapping, defaultPropsContents);

          String logFilePrefix = getLogFilePrefix(logDir, idtype, cfg);
          List<String> cfgPropsString = getCfgProps(idPropsString, props, idtype, cfg, mapping);
          int resultValue =
              executeTestClass(
                  cpString, cfgPropsString, classesList, idtype, cfg, db, mapping, logFilePrefix);
          if (resultValue != 0) {
            failureCount++;
          }
          moveLogFiles(logFilePrefix);

          if (runOnce) {
            alreadyRan = true;
          }

          if (TCK_PARAM_ON_FAILURE_FAIL_FAST.equals(onFailure) && failureCount > 0) {
            break;
          }
        }
        if (TCK_PARAM_ON_FAILURE_FAIL_FAST.equals(onFailure) && failureCount > 0) {
          break;
        }
      }
      if (TCK_PARAM_ON_FAILURE_FAIL_FAST.equals(onFailure) && failureCount > 0) {
        break;
      }
    }
    finalizeTCKRun(logDir, cpString, cfgDirName, failureCount);
  }

  /**
   * Initializes the TCK run: get the list of configurations (.conf files); get the properties
   * required for test execution. This method is called once per TCK run.
   *
   * @return the properties required for test execution
   * @throws MojoExecutionException
   */
  private List<String> initTCKRun() throws MojoExecutionException {
    if (impl.equals("iut")) {
      pmfProperties = "iut-pmf.properties";
    }

    if (cfgs == null) {
      if (cfgList != null) {
        cfgs = new ArrayList<String>();
        PropertyUtils.string2Collection(cfgList, cfgs);
      } else {
        // Fallback to "src/conf/configurations.list"
        setCfgListFromFile();
        if (cfgList != null) {
          cfgs = new ArrayList<String>();
          PropertyUtils.string2Collection(cfgList, cfgs);
        }

        if (cfgList == null) {
          throw new MojoExecutionException(
              "Could not find configurations to run TCK. "
                  + "Set cfgList parameter on command line or cfgs in pom.xml");
        }
      }
    }

    PropertyUtils.string2Collection(dblist, dbs);
    PropertyUtils.string2Collection(identitytypes, idtypes);
    System.out.println(
        "*>TCK to be run for implementation '"
            + impl
            + "' on \n"
            + " configurations: "
            + cfgs.toString()
            + "\n"
            + " databases: "
            + dbs.toString()
            + "\n"
            + " identitytypes: "
            + identitytypes);

    // Properties required for test execution
    System.out.println("cleanupaftertest is " + cleanupaftertest);

    List<String> propsString = new ArrayList<>();
    propsString.add("-Dverbose=" + verbose);
    propsString.add("-Djdo.tck.cleanupaftertest=" + cleanupaftertest);
    propsString.add(
        "-DPMFProperties=" + buildDirectory + FS + CLASSES_DIR_NAME + FS + pmfProperties);
    propsString.add(
        "-DPMF2Properties=" + buildDirectory + FS + CLASSES_DIR_NAME + FS + pmfProperties);
    return propsString;
  }

  /** Copy/create configuration files for logging, jdoconfig and JPA. */
  private void copyConfigurationFiles() {
    try {
      copyLog4j2ConfigurationFile();
    } catch (IOException ex) {
      Logger.getLogger(RunTCK.class.getName()).log(Level.SEVERE, null, ex);
    }

    // Copy JDO config files to classes dir
    try {
      File fromFile = new File(confDirectory + FS + impl + "-jdoconfig.xml");
      File toFile =
          new File(buildDirectory + FS + CLASSES_DIR_NAME + FS + "META-INF" + FS + "jdoconfig.xml");
      FileUtils.copyFile(fromFile, toFile);
      fromFile = new File(confDirectory + FS + impl + "-persistence.xml");
      toFile =
          new File(
              buildDirectory + FS + CLASSES_DIR_NAME + FS + "META-INF" + FS + "persistence.xml");
      FileUtils.copyFile(fromFile, toFile);
    } catch (IOException ex) {
      Logger.getLogger(RunTCK.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Get classpath string: add new entries to URLS from loader
   *
   * @param urlList ClassLoader URLs
   * @param enhancedDir
   * @return
   */
  private String getClasspathString(List<URL> urlList, File enhancedDir) {
    String cpString;
    ArrayList<URL> cpList = new ArrayList<>();
    cpList.addAll(urlList);
    try {
      URL url1 = enhancedDir.toURI().toURL();
      URL url2 = new File(buildDirectory + FS + CLASSES_DIR_NAME + FS).toURI().toURL();
      if (runtckVerbose) {
        System.out.println("url2 is " + url2.toString());
      }
      cpList.add(url1);
      cpList.add(url2);
      String[] jars = {"jar"};
      Iterator<File> fi = FileUtils.iterateFiles(new File(extLibsDirectory), jars, true);
      while (fi.hasNext()) {
        cpList.add(fi.next().toURI().toURL());
      }
      for (String dependency : this.dependencyClasspath.split(File.pathSeparator)) {
        cpList.add(new File(dependency).toURI().toURL());
      }
    } catch (MalformedURLException ex) {
      ex.printStackTrace();
      Logger.getLogger(RunTCK.class.getName()).log(Level.SEVERE, null, ex);
    }
    cpString = Utilities.urls2ClasspathString(cpList);
    if (runtckVerbose) {
      System.out.println("\nClasspath is " + cpString);
    }
    return cpString;
  }

  /**
   * Create the jdo pmf properties file.
   *
   * @param idtype identity type
   * @param mapping the mapping index
   * @param defaultPropsContents default pmf properties
   */
  private void writePMFPropsFile(String idtype, String mapping, String defaultPropsContents) {
    StringBuilder propsFileData = new StringBuilder();
    propsFileData.append("\n### Properties below added by maven 2 goal RunTCK.jdori");
    propsFileData.append("\njavax.jdo.mapping.Schema=" + idtype + mapping);
    mapping = (mapping.equals("0")) ? "" : mapping;
    propsFileData.append("\njavax.jdo.option.Mapping=standard" + mapping);
    propsFileData.append("\n");
    String pmfPropsWriteFileName = buildDirectory + FS + CLASSES_DIR_NAME + FS + pmfProperties;
    try (BufferedWriter out = new BufferedWriter(new FileWriter(pmfPropsWriteFileName, false))) {
      out.write(defaultPropsContents + propsFileData.toString());
    } catch (IOException ex) {
      Logger.getLogger(RunTCK.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Returns the configuration properties as List
   *
   * @param idPropsString
   * @param props the Properties object including the properties defined in the conf file
   * @param idtype identity type
   * @param cfg name of the configuration
   * @param mapping the mapping index
   * @return configuration properties
   */
  private List<String> getCfgProps(
      List<String> idPropsString, Properties props, String idtype, String cfg, String mapping) {
    List<String> cfgPropsString = new ArrayList<>();
    cfgPropsString.addAll(idPropsString);
    cfgPropsString.add("-Djdo.tck.testdata=" + getTrimmedPropertyValue(props, "jdo.tck.testdata"));
    cfgPropsString.add(
        "-Djdo.tck.standarddata=" + getTrimmedPropertyValue(props, "jdo.tck.standarddata"));
    cfgPropsString.add(
        "-Djdo.tck.mapping.companyfactory="
            + getTrimmedPropertyValue(props, "jdo.tck.mapping.companyfactory"));
    cfgPropsString.add(
        "-Djdo.tck.requiredOptions=" + getTrimmedPropertyValue(props, "jdo.tck.requiredOptions"));
    cfgPropsString.add("-Djdo.tck.signaturefile=" + signaturefile);
    cfgPropsString.add("-Djdo.tck.schemaname=" + idtype + mapping);
    cfgPropsString.add("-Djdo.tck.cfg=" + cfg);
    return cfgPropsString;
  }

  /**
   * Returns a list of class names of TCK test classes. Classes mentioned in the excludeFile are not
   * part of the result.
   *
   * @param props
   * @param cfg name of the configuration
   * @param excludeFile
   * @return
   * @throws MojoExecutionException
   */
  private List<String> getTestClasses(Properties props, String cfg, String excludeFile)
      throws MojoExecutionException {
    String classes = getTrimmedPropertyValue(props, "jdo.tck.classes");
    if (classes == null || classes.isEmpty()) {
      throw new MojoExecutionException("Could not find jdo.tck.classes value in conf file: " + cfg);
    }
    String excludeList =
        getTrimmedPropertyValue(PropertyUtils.getProperties(excludeFile), "jdo.tck.exclude");
    classes = Utilities.removeSubstrs(classes, excludeList);
    List<String> classesList = new ArrayList();
    PropertyUtils.string2Collection(classes, classesList);
    // skip test classes in comments
    return classesList.stream().filter(n -> !n.startsWith("#")).collect(Collectors.toList());
  }

  /**
   * Returns the perfix of the log file name. It includes the path, followed by an indicator for the
   * identitytype followed by the name of the configuration.
   *
   * @param logDir
   * @param idtype identity type
   * @param cfg name of the configuration
   * @return
   */
  private String getLogFilePrefix(String logDir, String idtype, String cfg) {
    String idname = idtype.trim().equals("applicationidentity") ? "app" : "dsid";
    String configName = cfg.indexOf('.') > 0 ? cfg.substring(0, cfg.indexOf('.')) : cfg;
    return logDir + FS + idname + "-" + configName + "-";
  }

  /**
   * Creates the java command to run a TCK test class and executes the command.
   *
   * @param cpString classpath
   * @param cfgPropsString configuration properties
   * @param classesList
   * @param idtype identity type
   * @param cfg name of the configuration
   * @param db the database
   * @param mapping the mapping index
   * @param logFilePrefix
   * @return
   */
  private int executeTestClass(
      String cpString,
      List<String> cfgPropsString,
      List<String> classesList,
      String idtype,
      String cfg,
      String db,
      String mapping,
      String logFilePrefix) {
    // build command line string
    List<String> command = new ArrayList<>();
    command.add("java");
    command.add("-cp");
    command.add(cpString);
    command.addAll(cfgPropsString);
    command.add(getDatastoreSupportsQueryCancelOption());
    command.add(dbproperties);
    command.add(jvmproperties);
    if (debugTCK) {
      command.add(debugDirectives);
    }
    command.add(testRunnerClass);
    command.add("execute");
    command.add("--disable-banner");
    if (disableColors()) {
      command.add("--disable-ansi-colors");
    }
    command.add("--details=" + testRunnerDetails);
    command.add("--config");
    command.add("junit.jupiter.execution.parallel.enabled=" + testParallelExecution);
    // add Test classes
    for (String testClass : classesList) {
      // skip empty entries
      if (testClass != null && testClass.trim().length() > 0) {
        command.add("-c");
        command.add(testClass);
      }
    }

    if (debugTCK) {
      System.out.println("Using debug arguments: \n" + debugDirectives);
    }

    // invoke class runner
    System.out.print(
        "*> Running tests for "
            + cfg
            + " with "
            + idtype
            + " on '"
            + db
            + "'"
            + " mapping="
            + mapping
            + " ... ");

    String junitLogFilename = logFilePrefix + JUNIT_LOG_FILE;
    int resultValue = 0;
    try {
      resultValue = Utilities.invokeCommand(command, new File(buildDirectory), junitLogFilename);
      if (resultValue == 0) {
        System.out.println("success");
      } else {
        System.out.println("FAIL");
      }
      if (runtckVerbose) {
        System.out.println("\nCommand line is: \n" + command.toString());
        System.out.println("Test exit value is " + resultValue);
        System.out.println("Test result output:\n" + fileToString(junitLogFilename));
      }
    } catch (java.lang.RuntimeException re) {
      System.out.println("Exception on command " + command);
    }
    return resultValue;
  }

  /**
   * Moves the implementation log file and TCK log file to the current log directory
   *
   * @param logFilePrefix the prefix of the log file consisting of idtype and conf
   */
  private void moveLogFiles(String logFilePrefix) {
    // Move log to per-test location
    String testLogFilename = logFilePrefix + impl + ".txt";
    try {
      File logFile = new File(implLogFile);
      FileUtils.moveFile(logFile, new File(testLogFilename));
    } catch (Exception e) {
      System.out.println(">> Error moving implementation log file: " + e.getMessage());
    }
    String tckLogFilename = logFilePrefix + TCK_LOG_FILE;
    try {
      File logFile = new File(tckLogFile);
      FileUtils.moveFile(logFile, new File(tckLogFilename));
    } catch (Exception e) {
      System.out.println(">> Error moving tck log file: " + e.getMessage());
    }
  }

  /**
   * Finalizes the TCK run: delete log files, create the result summary file TCK-results.txt, create
   * system configuration description file, copy metadata from enhanced to configuration logs
   * directory. This method is called once per TCK run.
   *
   * @param logDir the path of the log directory
   * @param cpString classpath
   * @param cfgDirName configuration directory
   * @param failureCount number of TCK test failures
   * @throws MojoExecutionException
   */
  private void finalizeTCKRun(String logDir, String cpString, String cfgDirName, int failureCount)
      throws MojoExecutionException {
    // Remove log file
    try {
      FileUtils.forceDeleteOnExit(new File(implLogFile));
    } catch (Exception e) {
      System.out.println(">> Error deleting log file: " + e.getMessage());
    }
    try {
      FileUtils.forceDeleteOnExit(new File(TCK_LOG_FILE));
    } catch (Exception e) {
      System.out.println(">> Error deleting log file: " + e.getMessage());
    }

    // Output results
    String resultSummaryLogFile = logDir + FS + "ResultSummary.txt";
    List<String> command = new ArrayList<>();
    command.add("java");
    command.add("-cp");
    command.add(cpString);
    command.add("org.apache.jdo.tck.util.ResultSummary");
    command.add(logDir);
    Utilities.invokeCommand(command, new File(buildDirectory), resultSummaryLogFile);
    System.out.println(fileToString(resultSummaryLogFile));

    // Create system configuration description file
    command.set(3, "org.apache.jdo.tck.util.SystemCfgSummary");
    command.set(4, cfgDirName);
    command.add("system_config.txt");
    Utilities.invokeCommand(command, new File(buildDirectory), null);

    // Copy metadata from enhanced to configuration logs directory
    for (String idtype : idtypes) {
      String fromDirName = buildDirectory + FS + "enhanced" + FS + impl + FS + idtype + FS;
      String[] metadataExtensions = {"jdo", "jdoquery", "orm", "xml", "properties"};
      // iterator over list of abs name of metadata files in src
      Iterator<File> fi = FileUtils.iterateFiles(new File(fromDirName), metadataExtensions, true);
      while (fi.hasNext()) {
        File fromFile = fi.next();
        String fromFileName = fromFile.toString();
        int startIdx = -1;
        if ((startIdx = fromFileName.indexOf(idtype + FS)) > -1) {
          // fully specified name of file (idtype + package + filename)
          String pkgName = fromFileName.substring(startIdx);
          File toFile = new File(cfgDirName + FS + pkgName);
          try {
            FileUtils.copyFile(fromFile, toFile);
          } catch (IOException ex) {
            throw new MojoExecutionException(
                "Failed to copy files from "
                    + fromFileName
                    + " to "
                    + toFile.toString()
                    + ": "
                    + ex.getLocalizedMessage());
          }
        }
      }
    }

    if (TCK_PARAM_ON_FAILURE_FAIL_FAST.equals(onFailure) && failureCount > 0) {
      throw new MojoExecutionException("Aborted TCK test run after 1 failure.");
    }
    if (TCK_PARAM_ON_FAILURE_FAIL_EVENTUALLY.equals(onFailure) && failureCount > 0) {
      throw new MojoExecutionException("There were " + failureCount + " TCK test failures.");
    }
  }

  private boolean disableColors() {
    return !this.testRunnerColors.equalsIgnoreCase("enable");
  }

  private String getDatastoreSupportsQueryCancelOption() {
    boolean support = true;
    if (datastoreSupportsQueryCancel == null || datastoreSupportsQueryCancel.isEmpty()) {
      // property not set -> check database
      String db = System.getProperty("jdo.tck.database");
      if (db != null && db.equals("derby")) {
        support = false;
      }
    } else {
      support = datastoreSupportsQueryCancel.equalsIgnoreCase("true");
    }
    return "-Djdo.tck.datastore.supportsQueryCancel=" + support;
  }
}
