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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/** Goal that displays help text for the exectck Maven plugin. */
@Mojo(name = "help")
public class Help extends AbstractMojo {

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    StringBuilder msg = new StringBuilder();

    msg.append("\n\n*** jdo-exectck Maven plugin ***\n\n");
    msg.append("This plugin executes the JDO Technology Compatibility Kit (TCK)\n");
    msg.append(
        "against the Reference Implementation (RI) or an implementation under test (IUT).\n\n");
    msg.append("- To display this help text, type \"mvn jdo-exectck:help\"\n");
    msg.append("- To run the entire TCK type \"mvn integration-test\" or \"mvn install\"\n");
    msg.append("- To run individual goals type \"mvn jdo-exectck:<goal>\"\n");
    msg.append("  Specify command line parameters, described below, to configure the test.\n");
    msg.append("\nGOALS\n");
    msg.append("* installSchema\n");
    msg.append("  Installs all of the database schemas required to execute tests\n");
    msg.append("  in the current test configurations.\n");
    msg.append("* enhance\n");
    msg.append("  Enhances classes. Classes must first be compiled (mvn compile).\n");
    msg.append("* runtck\n");
    msg.append(
        "  Runs the JDO Technology Compatibility Kit. Schema must first be installed and classes enhanced.\n");
    msg.append("\nPARAMETERS\n");
    msg.append(
        "To set parameters from the command line, \n  use the -D option and the parameter name.\n");
    msg.append("  For example, \"mvn -Djdo.tck.identitytype=applicationidentity\"\n");
    msg.append("* jdo.tck.impl\n");
    msg.append("  jdori (reference implementation) or iut (mplementation under test\n");
    msg.append("* jdo.tck.cfglist\n");
    msg.append("  List of configuration files, each describing a test configuration.\n");
    msg.append("  Default is all configurations.\n");
    msg.append("* jdo.tck.dblist\n");
    msg.append("  List of databases to run tests under.\n");
    msg.append("  Currently only derby is supported\n");
    msg.append("* jdo.tck.identitytypes\n");
    msg.append("  List of identity types to be tested\n");
    msg.append("  Default value is \"applicationidentity datastoreidentity\"\n");
    msg.append("* jdo.tck.impl.logfile\n");
    msg.append("  Location of implementation log file. Default: ${user.dir}/datanucleus.txt\n");
    msg.append("* jdo.tck.doRunTCK\n");
    msg.append("  Setting this parameter to false will bypass running the TCK.\n");
    msg.append("* jdo.tck.doInstallSchema\n");
    msg.append("  Setting this parameter to false will bypass schema installation.\n");
    msg.append("* jdo.tck.doEnhance\n");
    msg.append("  Setting this parameter to false will bypass enhancement.\n");
    msg.append("* jdo.tck.runTCKVerbose\n");
    msg.append(
        "  Setting this parameter to true will display test progress and"
            + "error output while the TCK is running.\n");
    msg.append("* jdo.tck.verbose:\n");
    msg.append("  Setting this parameter to true will run the TCK tests in verbose mode.\n");
    msg.append("* jdo.tck.onFailure\n");
    msg.append(
        "  Specifies how test failures are treated.\n"
            + "    \"failFast\" will immediately abort the test run.\n"
            + "    \"failGoal\" (default) will execute the whole TCK (maven goal) before failing.\n"
            + "    \"logOnly\" will report failures to console and logs only but return 'SUCCESS' to the "
            + "Maven execution environment.\n");
    msg.append("* jdo.tck.debugTCK:\n");
    msg.append("  Setting this parameter to true will run the TCK in a debugger.\n");
    msg.append("* jdo.tck.debug.jvmargs:\n");
    msg.append("  User-supplied arguments for debug directives.\n");
    msg.append("* jdo.tck.pmfproperties:\n");
    msg.append("  Name of file in src/conf containing pmf properties.\n");
    msg.append("* jdo.tck.excludefile:\n");
    msg.append(
        "  Name of file in src/conf containing property jdo.tck.exclude, "
            + "whose value is a list of files to be excluded from testing.\n");
    msg.append("* jdo.tck.signaturefile:\n");
    msg.append(
        "  Name of the signatute file used to test the signatures of the JDO API classes.\n");
    msg.append("* jdo.tck.cleanupaftertest:\n");
    msg.append("  Setting this parameter to false withh retain test output for debugging.\n");
    msg.append("* jdo.tck.jvmproperties:\n");
    msg.append("  JVM properties.\n");
    msg.append("* jdo.tck.logfile:\n");
    msg.append("  Name of the tck log file.\n");
    msg.append("* jdo.tck.testrunner.class:\n");
    msg.append("  Class used to run a batch of tests.\n");
    msg.append("* jdo.tck.testrunner.details:\n");
    msg.append(
        "  Output mode for test run. Use one of: none, summary, flat, tree, verbose, testfeed.\n"
            + "  If none is selected, then only the summary and test failures are shown.\n");
    msg.append("* jdo.tck.testrunner.colors:\n");
    msg.append(
        "  Setting this parameter to enable displays colors in the junit result log file.\n");
    msg.append("* jdo.tck.parallel.execution:\n");
    msg.append(
        "  Setting this parameter to false will disable parallel exceution of tck test methods in parallel threads.\n");
    msg.append("* jdo.tck.parallel.config.dynamic.factor:\n");
    msg.append(
        "  Computes the desired parallelism based on the number of available processors/cores "
            + "multiplied by the factor configuration parameter. Default: junit default.\n");
    msg.append("* jdo.tck.datastore.supportsQueryCancel:\n");
    msg.append(
        "  Setting this parameter to true indicates whether the datastore supports query canceling.\n");
    msg.append("* project.lib.ext.directory:\n");
    msg.append("  Location of third party libraries such as JNDI.\n");
    msg.append("* database.runtck.sysproperties:\n");
    msg.append("  Properties to use in accessing database.\n");
    msg.append(
        "\n  To run the TCK on an implementation under test, \n"
            + "  edit the pom.xml in tck and add the iut dependencies to the iut profile\n"
            + "  and set jdo.tck.impl to iut:\n");
    msg.append("     mvn integration-test -D jdo.tck.impl=\"iut\"\n");
    msg.append("\n END EXECTCK HELP INFORMATION\n");

    System.out.println(msg.toString());
  }
}
