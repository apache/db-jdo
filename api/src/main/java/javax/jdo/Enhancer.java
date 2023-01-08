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
package javax.jdo;

import static javax.jdo.Constants.ENHANCER_EXCEPTION;
import static javax.jdo.Constants.ENHANCER_NO_JDO_ENHANCER_FOUND;
import static javax.jdo.Constants.ENHANCER_USAGE_ERROR;
import static javax.jdo.Constants.PROPERTY_ENHANCER_VENDOR_NAME;
import static javax.jdo.Constants.PROPERTY_ENHANCER_VERSION_NUMBER;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import javax.jdo.spi.I18NHelper;

/**
 * Main class to invoke a JDO Enhancer. The enhancer is invoked with the following command line:
 *
 * <pre>
 * java -cp {classpath} javax.jdo.Enhancer {options} {directory, file, or resource names}
 * </pre>
 *
 * &lt;classpath&gt; must contain the jdo specification jar, the implementation jar and any
 * implementation dependencies, the statically-compiled classes, and the jdo metadata files loadable
 * as resources.
 *
 * <p>&lt;options&gt; include:
 *
 * <ul>
 *   <li>? : print usage to stderr and exit
 *   <li>-h : print usage to stderr and exit
 *   <li>-help : print usage to stderr and exit
 *   <li>-pu &lt;persistence-unit-name&gt; : the name of a persistence unit
 *   <li>-d &lt;target directory&gt; : write the enhanced classes to the specified directory
 *   <li>-checkonly : just check the classes for enhancement status
 *   <li>-v : verbose output
 *   <li>-r : recurse through directories to find all classes and metadata files to enhance
 *   <li>-cp &lt;enhancer class loader path&gt; : if not already included in the java class loader,
 *       this parameter must contain the statically-compiled classes, and the jdo metadata files
 *       loadable as resources
 * </ul>
 *
 * &lt;directory, file, or resource names&gt;
 *
 * <ul>
 *   <li>Directory names must not end in ".jdo", ".jar", or ".class"
 *   <li>Directories will be searched for files with suffixes ".jdo", ".jar", and ".class"
 *   <li>Directories will be searched recursively if the -r option is set
 * </ul>
 *
 * @since 3.0
 */
public class Enhancer {

  /** The Internationalization message helper. */
  private static final I18NHelper msg = I18NHelper.getInstance("javax.jdo.Bundle"); // NOI18N

  /** New Line */
  private static final char NL = '\n'; // NOI18N
  /** Jar file suffix */
  private static final String JAR_FILE_SUFFIX = ".jar"; // NOI18N
  /** JDO Metadata file suffix */
  private static final String JDO_FILE_SUFFIX = ".jdo"; // NOI18N
  /** Class file suffix */
  private static final String CLASS_FILE_SUFFIX = ".class"; // NOI18N

  private static final String MSG_ENHANCER_CLASS_PATH = "MSG_EnhancerClassPath"; // NOI18N
  private static final String MSG_ENHANCER_PROCESSING = "MSG_EnhancerProcessing"; // NOI18N
  private static final String MSG_ENHANCER_PROPERTY = "MSG_EnhancerProperty"; // NOI18N
  private static final String MSG_ENHANCER_USAGE = "MSG_EnhancerUsage"; // NOI18N

  /** Error indicator */
  private boolean error = false;
  /** If set, process parameters, print usage, and exit. */
  private boolean printAndExit = false;

  /** Persistence Units */
  private final List<String> persistenceUnitNames = new ArrayList<>();
  /** Target Directory Parameter */
  private String directoryName = null;
  /** ClassLoader for JDOEnhancer */
  private ClassLoader loader = null;
  /** Classpath (-cp) parameter */
  private String classPath = null;
  /** Check Only flag */
  private boolean checkOnly = false;
  /** Verbose flag */
  private boolean verbose = false;
  /** Recurse flag */
  private boolean recurse = false;
  /** Error messages should be empty unless there is an error */
  private final StringBuilder errorBuffer = new StringBuilder();
  /** Verbose messages are always collected but only output if verbose flag is set */
  private final StringBuilder verboseBuffer = new StringBuilder();
  /** File Names */
  private List<String> fileNames = new ArrayList<>();
  /** Class File Names */
  private final List<String> classFileNames = new ArrayList<>();
  /** JDO File Names */
  private final List<String> jdoFileNames = new ArrayList<>();
  /** Jar File Names */
  private final List<String> jarFileNames = new ArrayList<>();
  /** The number of classes validated by the JDOEnhancer */
  private int numberOfValidatedClasses = 0;
  /** The number of classes enhanced by the JDOEnhancer */
  private int numberOfEnhancedClasses = 0;

  /** The properties from the JDOEnhancer */
  private Properties properties;

  /**
   * Run the enhancer from the command line.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    Enhancer enhancerMain = new Enhancer();
    enhancerMain.run(args);
  }

  /**
   * Execute the enhancer.
   *
   * @param args the command line arguments
   */
  private void run(String[] args) {
    // processArgs will exit if errors or help
    processArgs(args);
    JDOEnhancer enhancer = null;
    try {
      enhancer = JDOHelper.getEnhancer();
    } catch (JDOException jdoex) {
      jdoex.printStackTrace(); // outputs to stderr
      exit(ENHANCER_NO_JDO_ENHANCER_FOUND);
    }

    try {
      // provide verbose property settings of the JDOEnhancer we just loaded
      properties = enhancer.getProperties();
      addVerboseMessage("MSG_EnhancerClass", enhancer.getClass().getName()); // NOI18N
      addVerboseMessage(
          MSG_ENHANCER_PROPERTY,
          PROPERTY_ENHANCER_VENDOR_NAME,
          properties.getProperty(PROPERTY_ENHANCER_VENDOR_NAME));
      addVerboseMessage(
          MSG_ENHANCER_PROPERTY,
          PROPERTY_ENHANCER_VERSION_NUMBER, // NOI18N
          properties.getProperty(PROPERTY_ENHANCER_VERSION_NUMBER));
      Set<Entry<Object, Object>> props = properties.entrySet();
      for (Entry<Object, Object> entry : props) {
        if (!(PROPERTY_ENHANCER_VENDOR_NAME.equals(entry.getKey())
            || PROPERTY_ENHANCER_VERSION_NUMBER.equals(entry.getKey()))) {
          addVerboseMessage(
              MSG_ENHANCER_PROPERTY,
              (String) entry.getKey(), // NOI18N
              (String) entry.getValue());
        }
      }
      enhancer.setVerbose(verbose);
      if (loader != null) {
        enhancer.setClassLoader(loader);
      }

      int numberOfClasses = classFileNames.size();
      if (numberOfClasses != 0) {
        enhancer.addClasses(classFileNames.toArray(new String[numberOfClasses]));
      }
      int numberOfFiles = jdoFileNames.size();
      if (numberOfFiles != 0) {
        enhancer.addFiles(jdoFileNames.toArray(new String[numberOfFiles]));
      }
      if (!jarFileNames.isEmpty()) {
        for (String jarFileName : jarFileNames) {
          enhancer.addJar(jarFileName);
        }
      }
      if (persistenceUnitNames != null) {
        for (String persistenceUnitName : persistenceUnitNames) {
          enhancer.addPersistenceUnit(persistenceUnitName);
        }
      }
      if (directoryName != null) {
        enhancer.setOutputDirectory(directoryName);
      }
      if (checkOnly) {
        numberOfValidatedClasses = enhancer.validate();
        addVerboseMessage("MSG_EnhancerValidatedClasses", numberOfValidatedClasses); // NOI18N
      } else {
        numberOfEnhancedClasses = enhancer.enhance();
        addVerboseMessage("MSG_EnhancerEnhancedClasses", numberOfEnhancedClasses); // NOI18N
      }
      exit(0); // good exit
    } catch (Exception ex) {
      ex.printStackTrace(); // outputs to stderr
      exit(ENHANCER_EXCEPTION); // error exit
    }
  }

  /**
   * Process the command line arguments and exit if there is a usage request or an error.
   *
   * @param args the command line arguments
   */
  private void processArgs(String[] args) {
    parseArgs(args);
    parseFiles(fileNames.toArray(new String[fileNames.size()]), true, recurse);
    loader = prepareClassLoader(classPath);
    if (error) {
      addErrorMessage(msg.msg(MSG_ENHANCER_USAGE)); // NOI18N
      exit(ENHANCER_USAGE_ERROR); // error exit
    }
    if (printAndExit) {
      addVerboseMessage(MSG_ENHANCER_USAGE); // NOI18N
      exit(0); // good exit
    }
  }

  /**
   * Parse the command line arguments. Put the results into fields.
   *
   * @param args the command line arguments
   */
  private void parseArgs(String[] args) {
    boolean doneWithOptions = false;
    fileNames = new ArrayList<>();
    for (int i = 0; i < args.length; ++i) {
      String arg = args[i];
      // if first argument is ? then simply print usage and return.
      if ("?".equals(arg)) {
        printAndExit = true;
        return;
      }
      if (!doneWithOptions) {
        if (arg.startsWith("-")) { // NOI18N
          String option = arg.substring(1);
          if ("help".equals(option)) { // NOI18N
            addVerboseMessage(MSG_ENHANCER_PROCESSING, "-help"); // NOI18N
            setPrintAndExit();
          } else if ("h".equals(option)) { // NOI18N
            addVerboseMessage(MSG_ENHANCER_PROCESSING, "-h"); // NOI18N
            setPrintAndExit();
          } else if ("v".equals(option)) { // NOI18N
            addVerboseMessage(MSG_ENHANCER_PROCESSING, "-v"); // NOI18N
            verbose = true;
          } else if ("verbose".equals(option)) { // NOI18N
            addVerboseMessage(MSG_ENHANCER_PROCESSING, "-verbose"); // NOI18N
            verbose = true;
          } else if ("pu".equals(option)) { // NOI18N
            if (hasNextArgument(MSG_ENHANCER_PROCESSING, "-pu", i, args.length)) { // NOI18N
              String puName = args[++i];
              addVerboseMessage("MSG_EnhancerPersistenceUnitName", puName); // NOI18N
              persistenceUnitNames.add(puName);
            } else {
              setError();
            }
          } else if ("cp".equals(option)) { // NOI18N
            if (hasNextArgument(MSG_ENHANCER_PROCESSING, "-cp", i, args.length)) { // NOI18N
              classPath = args[++i];
              addVerboseMessage(MSG_ENHANCER_CLASS_PATH, classPath); // NOI18N
            } else {
              setError();
            }
          } else if ("d".equals(option)) { // NOI18N
            if (hasNextArgument(MSG_ENHANCER_PROCESSING, "-d", i, args.length)) { // NOI18N
              directoryName = args[++i];
              addVerboseMessage("MSG_EnhancerOutputDirectory", directoryName); // NOI18N
            } else {
              setError();
            }
          } else if ("checkonly".equals(option)) { // NOI18N
            addVerboseMessage(MSG_ENHANCER_PROCESSING, "-checkonly"); // NOI18N
            checkOnly = true;
          } else if ("r".equals(option)) { // NOI18N
            addVerboseMessage(MSG_ENHANCER_PROCESSING, "-r"); // NOI18N
            recurse = true;
          } else {
            setError();
            addErrorMessage(msg.msg("ERR_EnhancerUnrecognizedOption", option)); // NOI18N
          }
        } else {
          doneWithOptions = true;
          fileNames.add(arg);
        }
      } else {
        fileNames.add(arg);
      }
    }
  }

  /**
   * Check whether there is another parameter (the argument for an option that requires an
   * argument).
   *
   * @param msgId the message id for an error message
   * @param where the parameter for the message
   * @param i the index into the parameter array
   * @param length the length of the parameter array
   * @return
   */
  private boolean hasNextArgument(String msgId, String where, int i, int length) {
    if (i + 1 >= length) {
      setError();
      addErrorMessage(msg.msg(msgId, where));
      addErrorMessage(msg.msg("ERR_EnhancerRequiredArgumentMissing")); // NOI18N
      return false;
    }
    return true;
  }

  /**
   * Files can be one of four types:
   *
   * <ol>
   *   <li>directory: the directory is examined for files of the following types
   *   <li>.class: this is a java class file
   *   <li>.jdo: this is a jdo metadata file
   *   <li>.jar: this is a jar file
   * </ol>
   *
   * If the recursion flag is set, directories contained in directories are examined, recursively.
   */
  private void parseFiles(String[] fileNames, boolean search, boolean recurse) {
    for (String fileName : fileNames) {
      if (fileName.endsWith(JAR_FILE_SUFFIX)) {
        // add to jar file names
        jarFileNames.add(fileName);
        addVerboseMessage("MSG_EnhancerJarFileName", fileName); // NOI18N
      } else if (fileName.endsWith(JDO_FILE_SUFFIX)) {
        // add to jdo file names
        jdoFileNames.add(fileName);
        addVerboseMessage("MSG_EnhancerJDOFileName", fileName); // NOI18N
      } else if (fileName.endsWith(CLASS_FILE_SUFFIX)) {
        // add to class file names
        classFileNames.add(fileName);
        addVerboseMessage("MSG_EnhancerClassFileName", fileName); // NOI18N
      } else {
        // assume a directory if no recognized suffix
        File directoryFile = new File(fileName);
        if (directoryFile.isDirectory() && search) {
          String directoryPath = directoryFile.getAbsolutePath();
          String[] files = directoryFile.list();
          String[] pathName = new String[1];
          if (files != null) {
            for (String file : files) {
              pathName[0] = directoryPath + '/' + file;
              parseFiles(pathName, recurse, recurse);
            }
          }
        }
      }
    }
  }

  /**
   * Prepare the class loader from the classPath specified
   *
   * @param classPath the classPath string from the "-cp classPath" option
   * @return the class loader
   */
  private ClassLoader prepareClassLoader(String classPath) {
    if (classPath == null) return null;
    ClassLoader result = null;
    // separate classPath using system class path separator
    String separator = System.getProperty("path.separator");
    String[] paths = classPath.split(separator);
    List<URL> urls = new ArrayList<>();
    for (String path : paths) {
      // for each path construct a URL from the File
      File file = new File(path);
      URI uri = file.toURI();
      try {
        URL url = uri.toURL();
        addVerboseMessage(MSG_ENHANCER_CLASS_PATH, url.toString());
        urls.add(url);
      } catch (MalformedURLException e) {
        setError();
        addErrorMessage(msg.msg("ERR_EnhancerBadClassPath", file));
      }
    }
    result = new URLClassLoader(urls.toArray(new URL[urls.size()]), null);
    return result;
  }

  /**
   * Add a message to stderr.
   *
   * @param message the internationalized message to add
   */
  private void addErrorMessage(String message) {
    errorBuffer.append(message);
    errorBuffer.append(NL);
  }

  /** Set the error flag. */
  private void setError() {
    error = true;
  }

  /** Set the print-and-exit flag. */
  private void setPrintAndExit() {
    printAndExit = true;
  }

  /**
   * Exit this process.
   *
   * @param exitValue the process exit value
   */
  private void exit(int exitValue) {
    System.out.print(verboseBuffer.toString());
    System.err.print(errorBuffer.toString());
    System.exit(exitValue);
  }

  /**
   * Add a message to the verbose message buffer.
   *
   * @param msgId the message id
   * @param where the parameter
   */
  private void addVerboseMessage(String msgId, String... where) {
    verboseBuffer.append(msg.msg(msgId, where));
    verboseBuffer.append(NL);
  }

  /**
   * Add a message to the verbose message buffer.
   *
   * @param msgId the message id
   * @param where the parameter
   */
  private void addVerboseMessage(String msgId, String where) {
    verboseBuffer.append(msg.msg(msgId, where));
    verboseBuffer.append(NL);
  }

  /**
   * Add a message to the verbose message buffer.
   *
   * @param msgId the message id
   */
  private void addVerboseMessage(String msgId) {
    verboseBuffer.append(msg.msg(msgId));
    verboseBuffer.append(NL);
  }

  /**
   * Add a message to the verbose message buffer.
   *
   * @param msgId the message id
   * @param where the parameter
   */
  private void addVerboseMessage(String msgId, int where) {
    addVerboseMessage(msgId, String.valueOf(where));
  }
}
