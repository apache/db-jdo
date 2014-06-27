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

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Goal that installs a database schema for testing a JDO implementation.
 *
 * @goal installSchema
 * 
 * @phase integration-test
 */
public class InstallSchema extends AbstractTCKMojo {

    /**
     * Location of TCK generated output.
     * @parameter property="jdo.tck.doInstallSchema"
     *      default-value="true"
     * @required
     */
    private boolean doInstallSchema;

    /**
     * List of mappings required by the current configuration
     */
    protected Collection<String> mappings = new HashSet<String>();

    @Override
    public void execute() throws MojoExecutionException {

        if (!doInstallSchema) {
            System.out.println("Skipping InstallSchema goal!");
            return;
        }

        if (cfgs == null) {
            if (cfgList != null) {
                    System.out.println("cfgList is " + cfgList);
                    cfgs = new HashSet<String>();
                    PropertyUtils.string2Set(cfgList, cfgs);
            }
            else {
                    // Fallback to "src/conf/configurations.list"
                    setCfgListFromFile();
                if (cfgList != null) {
                    cfgs = new HashSet<String>();
                    PropertyUtils.string2Set(cfgList, cfgs);
                }

                if (cfgList == null) {
                    throw new MojoExecutionException(
                        "Could not find configurations to run TCK. " +
                        "Set cfgList parameter on command line or cfgs in pom.xml.");
                }
            }
        }

        PropertyUtils.string2Set(dblist, dbs);
        PropertyUtils.string2Set(identitytypes, idtypes);
        PropertyUtils.mappingsSet(cfgs, confDirectory, mappings);
        System.out.println("*>Schemas to be installed for \n  configurations: "
                + cfgs.toString() + "\n  databases: " + dbs.toString()
                + "\n  identity types: " + identitytypes.toString());

        System.setProperty("java.security.manager", "default");
        System.setProperty("java.security.policy", confDirectory
                + File.separator + "security.policy");

        for (String db : dbs) {
            if ("derby".equalsIgnoreCase(db)) {
                // Currently we support only derby. To support additional db's, configuration 
                // data must be parameterized.

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

                if (mappings.contains("0")) {
                    // If schema was explicitly specified as "0" change to "" since
                    // the schema file in that case is "schema.sql". Note that this
                    // removes a duplicate entry too
                    mappings.remove("0");
                    mappings.add("");
                }

                // Create database
                for (String idtype : idtypes) {
                    for (String mapping : mappings) {
                        System.setProperty("ij.outfile", logsDirectory + File.separator
                                + "database" + File.separator + db + "_" + idtype
                                + "_" + mapping + ".txt");

                        System.out.print("*> Installing schema" + mapping
                                + ".sql for " + db + " " + idtype + " ... ");

                        String[] args = {sqlDirectory + File.separator + db
                                + File.separator + idtype + File.separator
                                + "schema" + mapping + ".sql"};
                        System.setProperty("derby.system.home", dbDirName);

                        boolean success = true;
                        try {
                            org.apache.derby.tools.ij.main(args);
                        } catch (IOException ioex) {
                            success = false;
                            System.out.println("FAILED!");
                            throw new MojoExecutionException(
                                    "*> Failed to execute ij: " +
                                    ioex.getLocalizedMessage());
                        } catch (Exception ex) {
                            success = false;
                            System.out.println("FAILED!");
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
                            if (success) {
                                System.out.println("done");
                            }
                        }
                    }
                }
                System.out.println("*> See diagnostic output in " + dbLogsDir + ".");
                System.out.println("");
            }
        }
    }
}
