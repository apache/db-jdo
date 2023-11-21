package org.apache.jdo.tck.util;

import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

public class TCKSummaryGeneratingListener extends SummaryGeneratingListener {

  private static final String IDTYPE_PROPERTY = "jdo.tck.identitytype";

  private static final String CONFIG_PROPERTY = "jdo.tck.cfg";

  private static final String LOGDIR_PROPERTY = "jdo.tck.log.directory";

  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    super.testPlanExecutionFinished(testPlan);
    ResultSummary.save(
        System.getProperty(LOGDIR_PROPERTY, "."),
        System.getProperty(IDTYPE_PROPERTY, "Missing identitytype"),
        System.getProperty(CONFIG_PROPERTY, "Missing configuration name"),
        getSummary());
  }
}
