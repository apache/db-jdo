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
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Abstract Mojo to be extended for the actual goals of this maven plugin.
 */
public abstract class AbstractTCKMojo extends AbstractMojo {

	/**
     * Location of TCK generated output.
     * @parameter property="project.build.directory"
     *      default-value="${basedir}/target"
     * @required
     */
    protected String buildDirectory;

    /**
     * Location of the logs directory.
     * @parameter property="project.log.directory"
     *      default-value="${project.build.directory}/logs"
     * @required
     */
    protected File logsDirectory;

    /**
     * Location of the configuration directory.
     * @parameter property="project.conf.directory"
     *      default-value="${basedir}/src/main/resources/conf"
     * @required
     */
    protected String confDirectory;

    /**
     * Location of the configuration directory.
     * @parameter property="project.sql.directory"
     *      default-value="${basedir}/src/main/resources/sql"
     * @required
     */
    protected String sqlDirectory;

    /**
     * List of configuration files, each describing a test configuration.
     * Note: Collection can only be configured in pom.xml. Using multi-valued
     *       type because long String cannot be broken across lines in pom.xml.
     * @parameter
     * @optional
     */
    protected Collection<String> cfgs;

    /**
     * List of configuration files, each describing a test configuration.
     * Allows command line override of configured cfgs value.
     * @parameter property="jdo.tck.cfglist"
     * @optional
     */
    protected String cfgList;

    /**
     * List of databases to run tests under.
     * Currently only derby is supported.
     * @parameter property="jdo.tck.dblist"
     *      default-value="derby"
     * @required
     */
    protected String dblist;

    /**
     * Implementation to be tested (jdori or iut).
     * Any value other than "jdori" will test an appropriately configured IUT
     * @parameter property="jdo.tck.impl"
     *      default-value="jdori"
     * @required
     */
    protected String impl;

    /**
     * Location of implementation log file.
     * @parameter property="jdo.tck.impl.logfile"
     *      default-value="${user.dir}/datanucleus.txt"
     * @required
     */
    protected String implLogFile;

    /**
     * Classpath including the dependencies using : as separator between entries.
     * @parameter property="jdo.tck.dependencyClasspath"
     *      default-value=""
     * @required
     */
    protected String dependencyClasspath;

    protected Collection<String> dbs = new HashSet<String>();

    /**
     * List of identity types to be tested.
     * @parameter property="jdo.tck.identitytypes"
     *      default-value="applicationidentity datastoreidentity"
     * @required
     */
    protected String identitytypes;

    protected Collection<String> idtypes = new HashSet<String>();

    /**
     * Convenience method to set the cfgList from the file
     * "src/main/resources/conf/configurations.list".
     * @throws MojoExecutionException If the file could not be found/opened
     */
    protected void setCfgListFromFile() throws MojoExecutionException {
        try {
            Properties defaultProps = new Properties();
            FileInputStream in = new FileInputStream(confDirectory + File.separator +
                "configurations.list");
            defaultProps.load(in);
            in.close();
            cfgList = defaultProps.getProperty("jdo.tck.cfglist");
        }
        catch (Exception e) {
            // Error finding configurations.list
            throw new MojoExecutionException("No configuration specified and could not " +
                "find 'src/main/resources/conf/configurations.list'");
		}
    }

    protected void copyLog4jPropertiesFile () throws IOException {
        File fromFile = new File(confDirectory + File.separator + impl + "-log4j.properties");
        File toFile = new File(buildDirectory + File.separator + "classes" +
                File.separator + "log4j.properties");
        FileUtils.copyFile(fromFile, toFile);
    }
}
