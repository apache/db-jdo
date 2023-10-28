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
package org.apache.jdo.tck.models.embedded;

import java.util.List;
import javax.jdo.Query;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.building.Kitchen;
import org.apache.jdo.tck.pc.building.MultifunctionOven;
import org.apache.jdo.tck.pc.building.Oven;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
   * Test for basic persistence of object with embedded object which has inheritance and persisting
   * the base type of the embedded object.
   */
  @Test
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
    Assertions.assertEquals(1, kitchen.getId(), "Id of object is incorrect");
    Oven oven = kitchen.getOven();
    Assertions.assertNotNull(oven, "Oven of Kitchen is null!");
    Assertions.assertEquals(
        Oven.class.getName(), oven.getClass().getName(), "Oven is of incorrect type");
    Assertions.assertEquals("Westinghouse", oven.getMake(), "Oven make is incorrect");
    Assertions.assertEquals("Economy", oven.getModel(), "Oven model is incorrect");
    getPM().currentTransaction().commit();
  }

  /**
   * Test for basic persistence of object with embedded object which has inheritance and persisting
   * the subclass type of the embedded object.
   */
  @Test
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
    Assertions.assertEquals(1, kitchen.getId(), "Id of object is incorrect");
    Oven oven = kitchen.getOven();
    Assertions.assertNotNull(oven, "Oven of Kitchen is null!");
    Assertions.assertEquals(
        MultifunctionOven.class.getName(), oven.getClass().getName(), "Oven is of incorrect type");
    MultifunctionOven multioven = (MultifunctionOven) oven;
    Assertions.assertEquals("Westinghouse", multioven.getMake(), "Oven make is incorrect");
    Assertions.assertEquals("Economy", multioven.getModel(), "Oven model is incorrect");
    Assertions.assertEquals(true, multioven.getMicrowave(), "Oven microwave setting is incorrect");
    Assertions.assertEquals(
        "TIMER,CLOCK", multioven.getCapabilities(), "Oven capabilities is incorrect");
    getPM().currentTransaction().commit();
  }

  /** Test for querying of fields of base type of an embedded object. */
  @SuppressWarnings("unchecked")
  @Test
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

    Query<Kitchen> q =
        getPM()
            .newQuery(
                "SELECT FROM "
                    + Kitchen.class.getName()
                    + " WHERE this.oven.make == 'Westinghouse'");
    List<Kitchen> kitchens = (List<Kitchen>) q.execute();
    Assertions.assertNotNull(kitchens, "No results from query!");
    Assertions.assertEquals(1, kitchens.size(), "Number of query results was incorrect");
    Kitchen kit = kitchens.iterator().next();
    Assertions.assertEquals(id, getPM().getObjectId(kit), "Kitchen result is incorrect");

    getPM().currentTransaction().commit();
  }

  /** Test for querying of fields of subclass type of an embedded object. */
  @SuppressWarnings("unchecked")
  @Test
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

    Query<Kitchen> q1 =
        getPM()
            .newQuery(
                "SELECT FROM "
                    + Kitchen.class.getName()
                    + " WHERE this.oven instanceof org.apache.jdo.tck.pc.building.MultifunctionOven");
    List<Kitchen> kitchens1 = (List<Kitchen>) q1.execute();
    Assertions.assertNotNull(kitchens1, "No results from query!");
    Assertions.assertEquals(1, kitchens1.size(), "Number of query results was incorrect");

    // Query using cast and a field of the subclass embedded class
    Query<Kitchen> q2 =
        getPM()
            .newQuery(
                "SELECT FROM "
                    + Kitchen.class.getName()
                    + " WHERE ((org.apache.jdo.tck.pc.building.MultifunctionOven)this.oven).capabilities == 'TIMER,CLOCK'");
    List<Kitchen> kitchens2 = (List<Kitchen>) q2.execute();
    Assertions.assertNotNull(kitchens2, "No results from query!");
    Assertions.assertEquals(1, kitchens2.size(), "Number of query results was incorrect");
    Kitchen kit = kitchens2.iterator().next();
    Assertions.assertEquals(id, getPM().getObjectId(kit), "Kitchen result is incorrect");

    getPM().currentTransaction().commit();
  }
}
