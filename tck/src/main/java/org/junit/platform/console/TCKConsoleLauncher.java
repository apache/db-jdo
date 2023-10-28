package org.junit.platform.console;

import java.io.PrintWriter;
import java.util.Optional;
import org.apache.jdo.tck.util.ResultSummary;
import org.junit.platform.console.options.CommandLineOptionsParser;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class TCKConsoleLauncher extends ConsoleLauncher {

  private static final String IDTYPE_PROPERTY = "jdo.tck.identitytype";

  private static final String CONFIG_PROPERTY = "jdo.tck.cfg";

  private static final String LOGDIR_PROPERTY = "jdo.tck.log.directory";

  public TCKConsoleLauncher(
      CommandLineOptionsParser commandLineOptionsParser, PrintWriter out, PrintWriter err) {
    super(commandLineOptionsParser, out, err);
  }

  public static void main(String... args) {
    ConsoleLauncherExecutionResult result = execute(System.out, System.err, args);
    Optional<TestExecutionSummary> summary = result.getTestExecutionSummary();
    ResultSummary.save(
        System.getProperty(LOGDIR_PROPERTY, "."),
        System.getProperty(IDTYPE_PROPERTY, "Missing identitytype"),
        System.getProperty(CONFIG_PROPERTY, "Missing configuration name"),
        summary);
    int exitCode = result.getExitCode();
    System.exit(exitCode);
  }
}
