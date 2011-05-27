package org.apache.jdo.exectck;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
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
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

/**
 * Goal that installs a database schema for testing a JDO implementation.
 *
 * @goal installSchema
 * 
 * @phase integration-test
 *
 */
public class InstallSchema
        extends AbstractMojo {

    /**
     * Location of TCK generated output.
     * @parameter expression="${jdo.tck.doInstallSchema}"
     *      default-value=true
     * @required
     */
    private boolean doInstallSchema;

    /**
     * Root of the TCK installation.
     * @parameter expression="${project.base.directory}"
     *      default-value="${basedir}"
     * @required
     */
//    private String baseDirectory;
    
    /**
     * Location of TCK generated output.
     * @parameter expression="${project.build.directory}"
     *      default-value="${basedir}/target"
     * @required
     */
    private String buildDirectory;
    
    /**
     * Location of the logs directory.
     * @parameter expression="${project.log.directory}"
     *      default-value="${project.build.directory}/logs"
     * @required
     */
    private File logsDirectory;
    
    /**
     * Location of the configuration directory.
     * @parameter expression="${project.conf.directory}"
     *      default-value="${basedir}/src/conf"
     * @required
     */
    private String confDirectory;
    
    /**
     * Location of the configuration directory.
     * @parameter expression="${project.sql.directory}"
     *      default-value="${basedir}/src/sql"
     * @required
     */
    private String sqlDirectory;

    /**
     * List of configuration files, each describing a test configuration.
     * Note: Collection can only be configured in pom.xml. Using multi-valued
     *       type because long String cannot be broken across lines in pom.xml.
     * @parameter
     * @required
     */
    private HashSet<String> cfgs;

    /**
     * List of configuration files, each describing a test configuration.
     * Allows command line override of configured cfgs value.
     * @parameter expression="${jdo.tck.cfglist}
     * @optional
     */
    private String cfgList;
    
    /**
     * List of databases to run tests under.
     * Currently only derby is supported.
     * @parameter expression="${jdo.tck.dblist}" default-value="derby"
     * @required
     */
    private String dblist;
    private HashSet<String> dbs;
    
    /**
     * List of identity types to be tested.
     * @parameter expression="${jdo.tck.identitytypes}"
     *      default-value="applicationidentity datastoreidentity"
     * @required
     */
    private String identitytypes;
    private HashSet<String> idtypes;
    
    /**
     * List of mappings required by the current configurationd
     */
    private HashSet<String> mappings;

    @Override
    public void execute()
            throws MojoExecutionException {

        if (!doInstallSchema) {
            System.out.println("Skipping InstallSchema goal!");
            return;
        }
        
        dbs = new HashSet();
        idtypes = new HashSet();
        mappings = new HashSet();

        if (cfgList != null) {
            cfgs = new HashSet();
            PropertyUtils.string2Set(cfgList, cfgs);
        }

        PropertyUtils.string2Set(dblist, dbs);
        PropertyUtils.string2Set(identitytypes, idtypes);
        PropertyUtils.mappingsSet(cfgs, confDirectory, mappings);
        System.out.println("*>Schemas to be installed for \n  configurations: "
                + cfgs.toString() + "\n  databases: " + dbs.toString()
                + "\n  identity types: " + identitytypes.toString());

//        System.setProperty("jdo.tck.basedir", baseDirectory);
        System.setProperty("java.security.manager", "default");
        System.setProperty("java.security.policy", confDirectory
                + File.separator + "security.policy");

        // Currently we support only derby. To support additional db's,
        //   configuration data must be parameterized.
        for (String db : dbs) {

            // Create directory for db logs
            String dbLogsDirName = logsDirectory + File.separator + "database";
            File dbLogsDir = new File(dbLogsDirName);
            if (!(dbLogsDir.exists()) && !(dbLogsDir.mkdirs())) {
                throw new MojoExecutionException("Failed to create directory "
                        + dbLogsDir);
            }

            // Create database directory
            String dbDirName = buildDirectory + File.separator + "database"
                    + File.separator + db;
            File dbDir = new File(dbDirName);
            if (!(dbDir.exists()) && !(dbDir.mkdirs())) {
                throw new MojoExecutionException("Failed to create directory "
                        + dbDir);
            }

            // Copy derby.properties to db dir
            File dbConf = new File(confDirectory + File.separator + db
                    + ".properties");
            File targetDir = new File(dbDirName);
            try {
                FileUtils.copyFileToDirectory(dbConf, targetDir, false);
            } catch (IOException ex) {
                throw new MojoExecutionException("Failed to copy file " + dbConf
                        + " to " + dbDir + ": " + ex.getLocalizedMessage());
            }

            // Create database
            for (String idtype : idtypes) {

                for (String mapping : mappings) {

                    System.setProperty("ij.outfile", logsDirectory + File.separator
                            + "database" + File.separator + db + "_" + idtype
                            + "_" + mapping + ".txt");

                    if (mapping.equals("0")) {
                        mapping = "";
                    }
                    System.out.println("*> Installing schema" + mapping
                                + ".sql for " + db + " " + idtype);

                    String[] args = {sqlDirectory + File.separator + db
                        + File.separator + idtype + File.separator
                        + "schema" + mapping + ".sql"};
                    System.setProperty("derby.system.home", dbDirName);

                    try {
                        org.apache.derby.tools.ij.main(args);
                    } catch (IOException ioex) {
                        throw new MojoExecutionException(
                                "*> Failed to execute ij: " +
                                ioex.getLocalizedMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("*> Classpath is ");
                        new Utilities().printClasspath();
                        System.out.println("*> derby.system.home is \n    "
                                + System.getProperty("derby.system.home"));
                        System.out.println("*> jdo.tck.basedir is \n    "
                                + System.getProperty("jdo.tck.basedir"));
                        System.out.println("*> ij.outfile is \n    "
                                + System.getProperty("ij.outfile"));
                        System.out.println("*> java.security.manager is \n    "
                                + System.getProperty("java.security.manager"));
                        System.out.println("*> java.security.policy is \n    "
                                + System.getProperty("java.security.policy"));
                    } finally {
                        System.out.println("*> Installation of schema" + mapping
                                + ".sql for " + db + " " + idtype
                                + " is complete. See diagnostic output in "
                                + dbLogsDir + ".");
                    }
                }
            }
        }
    }
}
