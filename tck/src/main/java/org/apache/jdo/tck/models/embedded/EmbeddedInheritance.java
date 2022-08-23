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
package org.apache.jdo.tck.models.embedded;

import java.util.List;
import javax.jdo.Query;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.building.Kitchen;
import org.apache.jdo.tck.pc.building.MultifunctionOven;
import org.apache.jdo.tck.pc.building.Oven;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test EmbeddedInheritance <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> <br>
 * <B>Assertion Description: </B>
 */
public class EmbeddedInheritance extends JDO_Test {

  /** */
  protected void localSetUp() {
    addTearDownClass(Kitchen.class);
  }

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(EmbeddedInheritance.class);
  }

  /**
   * Test for basic persistence of object with embedded object which has inheritance and persisting
   * the base type of the embedded object.
   */
  public void testPersistBase() {
    Object id = null;
    try {
      getPM().currentTransaction().begin();
      Kitchen kitchen1 = new Kitchen(1);
      Oven oven = new Oven("Westinghouse", "Economy");
      kitchen1.setOven(oven);
      getPM().makePersistent(kitchen1);
      getPM().currentTransaction().commit();
      id = getPM().getObjectId(kitchen1);
    } catch (Exception e) {
      fail("Exception on persist : " + e.getMessage());
    } finally {
      if (getPM().currentTransaction().isActive()) {
        getPM().currentTransaction().rollback();
      }
    }

    getPM().currentTransaction().begin();
    Kitchen kitchen = (Kitchen) getPM().getObjectById(id);
    assertEquals("Id of object is incorrect", 1, kitchen.getId());
    Oven oven = kitchen.getOven();
    assertNotNull("Oven of Kitchen is null!", oven);
    assertEquals("Oven is of incorrect type", Oven.class.getName(), oven.getClass().getName());
    assertEquals("Oven make is incorrect", "Westinghouse", oven.getMake());
    assertEquals("Oven model is incorrect", "Economy", oven.getModel());
    getPM().currentTransaction().commit();
  }

  /**
   * Test for basic persistence of object with embedded object which has inheritance and persisting
   * the subclass type of the embedded object.
   */
  public void testPersistSubclass() {
    Object id = null;
    try {
      getPM().currentTransaction().begin();
      Kitchen kitchen = new Kitchen(1);
      MultifunctionOven oven = new MultifunctionOven("Westinghouse", "Economy");
      oven.setMicrowave(true);
      oven.setCapabilities("TIMER,CLOCK");
      kitchen.setOven(oven);
      getPM().makePersistent(kitchen);
      getPM().currentTransaction().commit();
      id = getPM().getObjectId(kitchen);
    } catch (Exception e) {
      fail("Exception on persist : " + e.getMessage());
    } finally {
      if (getPM().currentTransaction().isActive()) {
        getPM().currentTransaction().rollback();
      }
    }

    getPM().currentTransaction().begin();
    Kitchen kitchen = (Kitchen) getPM().getObjectById(id);
    assertEquals("Id of object is incorrect", 1, kitchen.getId());
    Oven oven = kitchen.getOven();
    assertNotNull("Oven of Kitchen is null!", oven);
    assertEquals(
        "Oven is of incorrect type", MultifunctionOven.class.getName(), oven.getClass().getName());
    MultifunctionOven multioven = (MultifunctionOven) oven;
    assertEquals("Oven make is incorrect", "Westinghouse", multioven.getMake());
    assertEquals("Oven model is incorrect", "Economy", multioven.getModel());
    assertEquals("Oven microwave setting is incorrect", true, multioven.getMicrowave());
    assertEquals("Oven capabilities is incorrect", "TIMER,CLOCK", multioven.getCapabilities());
    getPM().currentTransaction().commit();
  }

  /** Test for querying of fields of base type of an embedded object. */
  @SuppressWarnings("unchecked")
  public void testQueryBase() {
    Object id = null;
    try {
      getPM().currentTransaction().begin();
      Kitchen kitchen = new Kitchen(1);
      MultifunctionOven oven = new MultifunctionOven("Westinghouse", "Economy");
      oven.setMicrowave(true);
      oven.setCapabilities("TIMER,CLOCK");
      kitchen.setOven(oven);
      getPM().makePersistent(kitchen);
      getPM().currentTransaction().commit();
      id = getPM().getObjectId(kitchen);
    } catch (Exception e) {
      fail("Exception on persist : " + e.getMessage());
    } finally {
      if (getPM().currentTransaction().isActive()) {
        getPM().currentTransaction().rollback();
      }
    }

    getPM().currentTransaction().begin();

    Query q =
        getPM()
            .newQuery(
                "SELECT FROM "
                    + Kitchen.class.getName()
                    + " WHERE this.oven.make == 'Westinghouse'");
    List<Kitchen> kitchens = (List<Kitchen>) q.execute();
    assertNotNull("No results from query!", kitchens);
    assertEquals("Number of query results was incorrect", 1, kitchens.size());
    Kitchen kit = kitchens.iterator().next();
    assertEquals("Kitchen result is incorrect", id, getPM().getObjectId(kit));

    getPM().currentTransaction().commit();
  }

  /** Test for querying of fields of subclass type of an embedded object. */
  @SuppressWarnings("unchecked")
  public void testQuerySubclass() {
    Object id = null;
    try {
      getPM().currentTransaction().begin();
      Kitchen kitchen = new Kitchen(1);
      MultifunctionOven oven = new MultifunctionOven("Westinghouse", "Economy");
      oven.setMicrowave(true);
      oven.setCapabilities("TIMER,CLOCK");
      kitchen.setOven(oven);
      getPM().makePersistent(kitchen);
      getPM().currentTransaction().commit();
      id = getPM().getObjectId(kitchen);
    } catch (Exception e) {
      fail("Exception on persist : " + e.getMessage());
    } finally {
      if (getPM().currentTransaction().isActive()) {
        getPM().currentTransaction().rollback();
      }
    }

    getPM().currentTransaction().begin();

    Query q1 =
        getPM()
            .newQuery(
                "SELECT FROM "
                    + Kitchen.class.getName()
                    + " WHERE this.oven instanceof org.apache.jdo.tck.pc.building.MultifunctionOven");
    List<Kitchen> kitchens1 = (List<Kitchen>) q1.execute();
    assertNotNull("No results from query!", kitchens1);
    assertEquals("Number of query results was incorrect", 1, kitchens1.size());

    // Query using cast and a field of the subclass embedded class
    Query q2 =
        getPM()
            .newQuery(
                "SELECT FROM "
                    + Kitchen.class.getName()
                    + " WHERE ((org.apache.jdo.tck.pc.building.MultifunctionOven)this.oven).capabilities == 'TIMER,CLOCK'");
    List<Kitchen> kitchens2 = (List<Kitchen>) q2.execute();
    assertNotNull("No results from query!", kitchens2);
    assertEquals("Number of query results was incorrect", 1, kitchens2.size());
    Kitchen kit = kitchens2.iterator().next();
    assertEquals("Kitchen result is incorrect", id, getPM().getObjectId(kit));

    getPM().currentTransaction().commit();
  }
}
