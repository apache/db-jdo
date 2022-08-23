/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jdo.tck.api.persistencemanager;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.ThreadExceptionHandler;
import org.apache.jdo.tck.util.RogueBarrier;

/**
 * <B>Title:</B> Thread Safe <br>
 * <B>Keywords:</B> multithreaded <br>
 * <B>Assertion ID:</B> A12.4-1. <br>
 * <B>Assertion Description: </B> It is a requirement for all JDO implementations to be thread-safe.
 * That is, the behavior of the implementation must be predictable in the presence of multiple
 * application threads. This assertion will generate multiple test cases to be evaluated.
 */
public class ThreadSafe extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.4-1 (ThreadSafe) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(ThreadSafe.class);
  }

  private final int threadCount = 10;
  private final ThreadExceptionHandler group = new ThreadExceptionHandler();
  private final RogueBarrier barrier = new RogueBarrier(threadCount);
  private int successCount = 0;
  private int exceptionCount = 0;

  /**
   * @throws Exception exception
   */
  public void testThreadSafe() throws Exception {
    if (debug) logger.debug("\nSTART testThreadSafe");

    // test thread-safety of PMF.getPersistenceManager():
    //   pmf.getPM(), pm.close()
    final PCPoint[] nullPC = new PCPoint[threadCount];
    runThreads(nullPC, "Concurrent PMF.getPersistenceManager()", threadCount);

    // test thread-safety of PMF.getPersistenceManager():
    //   pmf.getPM(), pm.makePersistent(private transient PC), pm.close()
    final PCPoint[] localPC = new PCPoint[threadCount];
    for (int i = 0; i < threadCount; i++) {
      localPC[i] = new PCPoint(1, i);
    }
    runThreads(
        localPC, "Concurrent PMF.getPersistenceManager()" + ".makePersistent()", threadCount);

    // test thread-safety of PM.makePersistent():
    //   pmf.getPM(), pm.makePersistent(shared transient PC), pm.close()
    final PCPoint[] sharedPC = new PCPoint[threadCount];
    final PCPoint p1 = new PCPoint(3, 3);
    for (int i = 0; i < threadCount; i++) {
      sharedPC[i] = p1;
    }
    runThreads(sharedPC, "Concurrent PM.makePersistent(" + "shared transient PC)", 1);
  }

  /**
   * @param pc objects
   * @param header header
   * @param toSucceed to succeed
   */
  public void runThreads(Object[] pc, String header, int toSucceed) {
    // start threads with their pc instance
    final Thread[] threads = new Thread[threadCount];
    for (int i = 0; i < threadCount; i++) {
      Thread t = new Thread(group, new PMThread(pc[i]));
      t.setName("ThreadSafeID-" + i);
      threads[i] = t;
      t.start();
    }

    // wait for all threads to finish
    for (int i = 0; i < threadCount; i++) {
      while (true) {
        try {
          threads[i].join();
          break;
        } catch (InterruptedException e) { // swallow
        }
      }
    }

    checkResults(header, toSucceed);
  }

  /**
   * @param header header
   * @param toSucceed to succeed
   */
  protected synchronized void checkResults(String header, int toSucceed) {
    // check unhandled exceptions
    final Set uncaught = group.getAllUncaughtExceptions();
    if ((uncaught != null) && !uncaught.isEmpty()) {
      StringBuffer report = new StringBuffer("Uncaught exceptions:\n");
      for (Iterator i = uncaught.iterator(); i.hasNext(); ) {
        Map.Entry next = (Map.Entry) i.next();
        Thread thread = (Thread) next.getKey();
        Throwable problem = (Throwable) next.getValue();
        report.append(header + ": Uncaught exception " + problem + " in thread " + thread + "\n");
      }
      fail(ASSERTION_FAILED, report.toString());
      group.clear();
    }

    // check success count
    if (successCount != toSucceed) {
      fail(
          ASSERTION_FAILED,
          header
              + ": Incorrect number of "
              + "\"succeeding\" threads; expected="
              + toSucceed
              + " found="
              + successCount);
    }
    successCount = 0;

    // check exception count
    final int toExcept = threadCount - toSucceed;
    if (exceptionCount != toExcept) {
      fail(
          ASSERTION_FAILED,
          header
              + ": Incorrect number of "
              + "\"failing\" threads; expected="
              + toExcept
              + " found="
              + exceptionCount);
    }
    exceptionCount = 0;
  }

  /** */
  protected synchronized void markSuccess() {
    logger.debug("[" + Thread.currentThread().getName() + "]: success");
    successCount++;
  }

  /** */
  protected synchronized void markExpectedException() {
    logger.debug("[" + Thread.currentThread().getName() + "]: expected exception");
    exceptionCount++;
  }

  /** */
  class PMThread implements Runnable {
    private final Object pc;

    /** */
    PMThread(Object pc) {
      this.pc = pc;
    }

    /** */
    public void run() {
      ThreadSafe.this.logger.debug("[" + Thread.currentThread().getName() + "]: running");
      final PersistenceManager pm = pmf.getPersistenceManager();
      try {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        if (pc != null) {
          pm.makePersistent(pc);
        }
        tx.commit();
        markSuccess();
      } catch (JDOUserException ex) {
        markExpectedException();
      } finally {
        barrier.await();
        cleanupPM(pm);
      }
    }
  }
}
