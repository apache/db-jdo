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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

/** Abstract Mojo to be extended for the actual goals of this maven plugin. */
public abstract class AbstractTCKMojo extends AbstractMojo {

  /** Filename of log4j2 configuration file. */
  protected final String LOG4J2_CONFIGURATION = "log4j2.xml";

  /** Location of TCK generated output. */
  @Parameter(
      property = "project.build.directory",
      defaultValue = "${basedir}/target",
      required = true)
  protected String buildDirectory;

  /** Location of the logs directory. */
  @Parameter(
      property = "project.log.directory",
      defaultValue = "${project.build.directory}/logs",
      required = true)
  protected File logsDirectory;

  /** Location of the configuration directory. */
  @Parameter(
      property = "project.conf.directory",
      defaultValue = "${basedir}/src/main/resources/conf",
      required = true)
  protected String confDirectory;

  /** Location of the configuration directory. */
  @Parameter(
      property = "project.sql.directory",
      defaultValue = "${basedir}/src/main/resources/sql",
      required = true)
  protected String sqlDirectory;

  /**
   * List of configuration files, each describing a test configuration. Note: Collection can only be
   * configured in pom.xml. Using multi-valued type because long String cannot be broken across
   * lines in pom.xml.
   */
  @Parameter protected Collection<String> cfgs;

  /**
   * List of configuration files, each describing a test configuration. Allows command line override
   * of configured cfgs value.
   */
  @Parameter(property = "jdo.tck.cfglist")
  protected String cfgList;

  /** List of databases to run tests under. Currently only derby is supported. */
  @Parameter(property = "jdo.tck.dblist", defaultValue = "derby", required = true)
  protected String dblist;

  /**
   * Implementation to be tested (jdori or iut). Any value other than "jdori" will test an
   * appropriately configured IUT
   */
  @Parameter(property = "jdo.tck.impl", defaultValue = "jdori", required = true)
  protected String impl;

  /** Location of implementation log file. */
  @Parameter(
      property = "jdo.tck.impl.logfile",
      defaultValue = "${user.dir}/datanucleus.txt",
      required = true)
  protected String implLogFile;

  /** Classpath including the dependencies using : as separator between entries. */
  @Parameter(property = "jdo.tck.dependencyClasspath", defaultValue = "", required = true)
  protected String dependencyClasspath;

  protected Collection<String> dbs = new HashSet<String>();

  /** List of identity types to be tested. */
  @Parameter(
      property = "jdo.tck.identitytypes",
      defaultValue = "applicationidentity datastoreidentity",
      required = true)
  protected String identitytypes;

  protected Collection<String> idtypes = new HashSet<String>();

  /**
   * Convenience method to set the cfgList from the file
   * "src/main/resources/conf/configurations.list".
   *
   * @throws MojoExecutionException If the file could not be found/opened
   */
  protected void setCfgListFromFile() throws MojoExecutionException {
    try {
      Properties defaultProps = new Properties();
      FileInputStream in =
          new FileInputStream(confDirectory + File.separator + "configurations.list");
      defaultProps.load(in);
      in.close();
      cfgList = defaultProps.getProperty("jdo.tck.cfglist");
    } catch (Exception e) {
      // Error finding configurations.list
      throw new MojoExecutionException(
          "No configuration specified and could not "
              + "find 'src/main/resources/conf/configurations.list'");
    }
  }

  protected void copyLog4j2ConfigurationFile() throws IOException {
    File fromFile = new File(confDirectory + File.separator + impl + "-" + LOG4J2_CONFIGURATION);
    File toFile =
        new File(
            buildDirectory + File.separator + "classes" + File.separator + LOG4J2_CONFIGURATION);
    FileUtils.copyFile(fromFile, toFile);
  }

  protected void resetFileContent(String fileName) throws MojoExecutionException {
    File file = new File(fileName);
    // reset file content
    try {
      FileUtils.write(file, "", Charset.defaultCharset());
    } catch (IOException e) {
      throw new MojoExecutionException("Failed to reset file content: " + file.toURI());
    }
  }
}
