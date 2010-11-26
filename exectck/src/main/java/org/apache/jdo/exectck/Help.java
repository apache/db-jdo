/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.jdo.exectck;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Goal that displays help text for the exectck Maven plugin.
 *
 * @goal help
 *
 * @phase integration-test
 *
 */
public class Help extends AbstractMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        
        StringBuffer msg = new StringBuffer();

        msg.append("\n\n*** jdo-exectck Maven plugin ***\n\n");
        msg.append("This plugin executes the JDO Technology Compatibility Kit (TCK)\n");
        msg.append("against the Reference Implementation (RI) or an implementation under test (IUT).\n\n");
        msg.append("- To display this help text, type \"mvn jdo-exectck:help\"\n");
        msg.append("- To run the entire TCK type \"mvn integration-test\" or \"mvn install\"\n");
        msg.append("- To run individual goals as \"mvn jdo-exectck:<goal>\"\n");
        msg.append("  or specify command line parameters to configure the test.\n");
        msg.append("\nGOALS\n");
        msg.append("* installSchema\n");
        msg.append("  Installs all of the database schemas required to execute tests\n");
        msg.append("  in the current test configurations.\n");
        msg.append("* enhance\n");
        msg.append("  Not yet implemented\n");
        msg.append("* runTCK\n");
        msg.append("  Not yet implemented\n");
        msg.append("\nPARAMETERS\n");
        msg.append("To set parameters from the command line, \n  use the -D option and the parameter name.\n");
        msg.append("  For example, \"mvn -Djdo.tck.identitytype=applicationidentity\"\n");
        msg.append("* jdo.tck.cfglist\n");
        msg.append("  List of configuration files, each describing a test configuration.\n");
        msg.append("   Default is all configurations.\n");
        msg.append("* jdo.tck.dblist\n");
        msg.append("   List of databases to run tests under.\n");
        msg.append("   Currently only derby is supported\n");
        msg.append("* jdo.tck.identitytype\n");
        msg.append("   List of identity types to be tested\n");
        msg.append("   Default value is \"applicationidentity datastoreidentity\"\n");

        System.out.println(msg.toString());
    }

}
