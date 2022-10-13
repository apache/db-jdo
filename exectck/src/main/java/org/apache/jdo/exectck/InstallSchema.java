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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/** Goal that installs a database schema for testing a JDO implementation. */
@Mojo(name = "installSchema")
public class InstallSchema extends AbstractTCKMojo {

  /** Location of TCK generated output. */
  @Parameter(property = "jdo.tck.doInstallSchema", defaultValue = "true", required = true)
  private boolean doInstallSchema;

  /** List of mappings required by the current configuration */
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
      } else {
        // Fallback to "src/conf/main/resources/configurations.list"
        setCfgListFromFile();
        if (cfgList != null) {
          cfgs = new HashSet<String>();
          PropertyUtils.string2Set(cfgList, cfgs);
        }

        if (cfgList == null) {
          throw new MojoExecutionException(
              "Could not find configurations to run TCK. "
                  + "Set cfgList parameter on command line or cfgs in pom.xml.");
        }
      }
    }

    PropertyUtils.string2Set(dblist, dbs);
    PropertyUtils.string2Set(identitytypes, idtypes);
    PropertyUtils.mappingsSet(cfgs, confDirectory, mappings);
    System.out.println(
        "*>Schemas to be installed for \n  configurations: "
            + cfgs.toString()
            + "\n  databases: "
            + dbs.toString()
            + "\n  identity types: "
            + identitytypes.toString());

    System.setProperty("java.security.manager", "default");
    System.setProperty("java.security.policy", confDirectory + File.separator + "security.policy");

    for (String db : dbs) {
      // Create directory for db logs
      String dbLogsDirName = logsDirectory + File.separator + "database";
      File dbLogsDir = new File(dbLogsDirName);
      if (!(dbLogsDir.exists()) && !(dbLogsDir.mkdirs())) {
        throw new MojoExecutionException("Failed to create directory " + dbLogsDir);
      }

      // Create database directory
      String dbDirName = buildDirectory + File.separator + "database" + File.separator + db;
      File dbDir = new File(dbDirName);
      if (!(dbDir.exists()) && !(dbDir.mkdirs())) {
        throw new MojoExecutionException("Failed to create directory " + dbDir);
      }

      // Copy db.properties to db dir
      File dbConf = new File(confDirectory + File.separator + db + ".properties");
      try {
        FileUtils.copyFileToDirectory(dbConf, dbDir, false);
      } catch (IOException ex) {
        throw new MojoExecutionException(
            "Failed to copy file " + dbConf + " to " + dbDir + ": " + ex.getLocalizedMessage());
      }

      if (mappings.contains("0")) {
        // If schema was explicitly specified as "0" change to "" since
        // the schema file in that case is "schema.sql". Note that this
        // removes a duplicate entry too
        mappings.remove("0");
        mappings.add("");
      }

      initializeDB(db, dbDirName);

      // Create database
      for (String idtype : idtypes) {
        for (String mapping : mappings) {
          System.out.print(
              "*> Installing schema" + mapping + ".sql for " + db + " " + idtype + " ... ");

          String outFileName =
              logsDirectory
                  + File.separator
                  + "database"
                  + File.separator
                  + db
                  + "_"
                  + idtype
                  + "_"
                  + mapping
                  + ".txt";
          String sqlFileName =
              sqlDirectory
                  + File.separator
                  + db
                  + File.separator
                  + idtype
                  + File.separator
                  + "schema"
                  + mapping
                  + ".sql";

          boolean success = true;
          try {
            loadSQLFileUsingJDBC(db, dbDirName, sqlFileName, outFileName);
          } catch (Exception ex) {
            success = false;
            System.out.println("FAILED!");
            ex.printStackTrace();
            System.out.println("*> Classpath is ");
            new Utilities().printClasspath();
            System.out.println(
                "*> jdo.tck.basedir is \n    " + System.getProperty("jdo.tck.basedir"));
            System.out.println(
                "*> java.security.manager is \n    " + System.getProperty("java.security.manager"));
            System.out.println(
                "*> java.security.policy is \n    " + System.getProperty("java.security.policy"));
            System.out.println("*> dbDirName is \n    " + dbDirName);
            System.out.println("*> outFileName is \n    " + outFileName);
          } finally {
            if (success) {
              System.out.println("done");
            }
          }
        }
      }

      finalizeDB(db, dbDirName);

      System.out.println("*> See diagnostic output in " + dbLogsDir + ".");
      System.out.println("");
    }
  }

  /**
   * @param db database name
   * @param dbDirName database home directory
   */
  protected void initializeDB(String db, String dbDirName) {
    if ("derby".equalsIgnoreCase(db)) {
      System.setProperty("derby.system.home", dbDirName);
    }
  }

  /**
   * @param db database name
   * @param dbDirName database home directory
   */
  protected void finalizeDB(String db, String dbDirName) {
    if ("derby".equalsIgnoreCase(db)) {
      try {
        DriverManager.getConnection("jdbc:derby:;shutdown=true");
      } catch (SQLException ex) {
        // ignore
      }
    }
  }

  /**
   * @param db database name
   * @param dbDirName database home directory
   * @param sqlFileName name of the sql file
   * @param outFileName name of the output file
   * @throws MojoExecutionException exception indicating error case
   */
  protected void loadSQLFileUsingIJ(
      String db, String dbDirName, String sqlFileName, String outFileName)
      throws MojoExecutionException {
    String[] args = {sqlFileName};
    try {
      System.setProperty("ij.outfile", outFileName);
      org.apache.derby.tools.ij.main(args);
    } catch (IOException ioex) {
      System.out.println("FAILED!");
      throw new MojoExecutionException("*> Failed to execute ij: " + ioex.getLocalizedMessage());
    }
  }

  /**
   * @param db database name
   * @param dbDirName database home directory
   * @param sqlFileName name of the sql file
   * @param outFileName name of the output file
   * @throws MojoExecutionException exception indicating error case
   */
  protected void loadSQLFileUsingJDBC(
      String db, String dbDirName, String sqlFileName, String outFileName)
      throws MojoExecutionException {
    try {
      SQLFileLoader loader = new SQLFileLoader(sqlFileName);
      String url = loader.getConnect();
      if (url == null || "".equals(url.trim())) {
        throw new MojoExecutionException(
            "*> connection url needs to be specified, see file " + sqlFileName);
      }
      String user = loader.getUser();
      if (user == null || "".equals(user.trim())) {
        throw new MojoExecutionException(
            "*> connection user needs to be specified, see file " + sqlFileName);
      }
      String passwd = loader.getPassword();
      if (passwd == null || "".equals(passwd.trim())) {
        throw new MojoExecutionException(
            "*> connection passwd needs to be specified, see file " + sqlFileName);
      }
      List<String> stmts = loader.getStatements();

      try (Connection conn = DriverManager.getConnection(url, user, passwd);
          Statement stmt = conn.createStatement();
          PrintWriter outfile = new PrintWriter(outFileName)) {
        outfile.println("url=" + url + " user=" + user + " password=" + passwd);
        for (String s : stmts) {
          try {
            outfile.print("loading: " + s);
            stmt.execute(s);
            outfile.println(" success");
          } catch (SQLException ex) {
            outfile.println(" failure " + ex);
          }
        }
      } catch (SQLException ex) {
        throw new MojoExecutionException(
            "*> Failed to initialize JDBC connection, "
                + "url="
                + url
                + " user="
                + user
                + " password="
                + passwd
                + ": "
                + ex.getLocalizedMessage());
      } catch (IOException ex) {
        throw new MojoExecutionException(
            "*> Failed to write to the outfile " + outFileName + ": " + ex.getLocalizedMessage());
      }
    } catch (IOException ex) {
      throw new MojoExecutionException(
          "*> Failed to load file " + sqlFileName + " " + ex.getLocalizedMessage());
    }
  }
}
