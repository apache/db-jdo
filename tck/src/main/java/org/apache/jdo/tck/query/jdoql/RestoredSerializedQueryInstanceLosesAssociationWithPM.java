/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.query.jdoql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Restored Serialized Query Instance Loses Association With PM <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.3-8. <br>
 * <B>Assertion Description: </B> If a serialized instance is restored, it loses its association
 * with its former <code>PersistenceManager</code>.
 */
public class RestoredSerializedQueryInstanceLosesAssociationWithPM extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.3-8 (RestoredSerializedQueryInstanceLosesAssociationWithPM) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(RestoredSerializedQueryInstanceLosesAssociationWithPM.class);
  }

  /**
   * @throws Exception exception
   */
  public void test() throws Exception {
    pm = getPM();

    checkRestoredQueryInstance(pm);

    pm.close();
    pm = null;
  }

  /** */
  @SuppressWarnings("unchecked")
  void checkRestoredQueryInstance(PersistenceManager pm)
      throws IOException, ClassNotFoundException {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<Project> query = pm.newQuery(Project.class);
      query.setCandidates(pm.getExtent(Project.class, false));
      query.declareVariables(
          "org.apache.jdo.tck.pc.company.Person a; org.apache.jdo.tck.pc.company.Person b");
      query.setFilter(
          "reviewers.contains(a) && a.firstname==\"brazil\" || reviewers.contains(b) && b.firstname==\"brazil\"");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(query);
      ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
      query = (Query<Project>) ois.readObject();

      try {
        Object results = query.execute();
        fail(
            ASSERTION_FAILED,
            "A deserialized query instance should not execute successfully without associating that instance to a persistence manager");
      } catch (JDOException e) {
        if (debug) logger.debug("Caught expected " + e);
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
