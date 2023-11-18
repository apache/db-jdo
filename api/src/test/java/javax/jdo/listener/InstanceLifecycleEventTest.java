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

/*
 * InstanceLifecycleEventTest.java
 *
 */

package javax.jdo.listener;

import javax.jdo.util.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests that instances of InstanceLifecycleEvent can be created and that the source, type, and
 * target instances are correct.
 */
class InstanceLifecycleEventTest extends AbstractTest {

  final Object created = new Object();
  final Object loaded = new Object();
  final Object stored = new Object();
  final Object cleared = new Object();
  final Object deleted = new Object();
  final Object dirtied = new Object();
  final Object attached = new Object();
  final Object attachTarget = new Object();
  final Object detached = new Object();
  final Object detachTarget = new Object();

  /** Creates a new instance of SingleFieldIdentityTest */
  public InstanceLifecycleEventTest() {}

  @Test
  void testConstructorCreateEvent() {
    InstanceLifecycleEvent e = new InstanceLifecycleEvent(created, InstanceLifecycleEvent.CREATE);
    Assertions.assertSame(created, e.getSource(), "Create source differs.");
    Assertions.assertEquals(
        InstanceLifecycleEvent.CREATE, e.getEventType(), "Create type differs.");
  }

  @Test
  void testConstructorLoadEvent() {
    InstanceLifecycleEvent e = new InstanceLifecycleEvent(loaded, InstanceLifecycleEvent.LOAD);
    Assertions.assertSame(loaded, e.getSource(), "Load source differs.");
    Assertions.assertEquals(InstanceLifecycleEvent.LOAD, e.getEventType(), "Load type differs.");
  }

  @Test
  void testConstructorStoreEvent() {
    InstanceLifecycleEvent e = new InstanceLifecycleEvent(stored, InstanceLifecycleEvent.STORE);
    Assertions.assertSame(stored, e.getSource(), "Store source differs.");
    Assertions.assertEquals(InstanceLifecycleEvent.STORE, e.getEventType(), "Store type differs.");
  }

  @Test
  void testConstructorClearEvent() {
    InstanceLifecycleEvent e = new InstanceLifecycleEvent(cleared, InstanceLifecycleEvent.CLEAR);
    Assertions.assertSame(cleared, e.getSource(), "Clear source differs.");
    Assertions.assertEquals(InstanceLifecycleEvent.CLEAR, e.getEventType(), "Clear type differs.");
  }

  @Test
  void testConstructorDeleteEvent() {
    InstanceLifecycleEvent e = new InstanceLifecycleEvent(deleted, InstanceLifecycleEvent.DELETE);
    Assertions.assertSame(deleted, e.getSource(), "Delete source differs.");
    Assertions.assertEquals(
        InstanceLifecycleEvent.DELETE, e.getEventType(), "Delete type differs.");
  }

  @Test
  void testConstructorDirtyEvent() {
    InstanceLifecycleEvent e = new InstanceLifecycleEvent(dirtied, InstanceLifecycleEvent.DIRTY);
    Assertions.assertSame(dirtied, e.getSource(), "Dirty source differs.");
    Assertions.assertEquals(InstanceLifecycleEvent.DIRTY, e.getEventType(), "Dirty type differs.");
  }

  @Test
  void testConstructorDetachEvent() {
    InstanceLifecycleEvent e =
        new InstanceLifecycleEvent(detached, InstanceLifecycleEvent.DETACH, detachTarget);
    Assertions.assertSame(detached, e.getSource(), "Detach source differs.");
    Assertions.assertEquals(
        InstanceLifecycleEvent.DETACH, e.getEventType(), "Detach type differs.");
    Assertions.assertSame(detachTarget, e.getTarget(), "Detach target differs.");
  }

  @Test
  void testConstructorAttachEvent() {
    InstanceLifecycleEvent e =
        new InstanceLifecycleEvent(attached, InstanceLifecycleEvent.ATTACH, attachTarget);
    Assertions.assertSame(attached, e.getSource(), "Attach source differs.");
    Assertions.assertEquals(
        InstanceLifecycleEvent.ATTACH, e.getEventType(), "Attach type differs.");
    Assertions.assertSame(attachTarget, e.getTarget(), "Attach target differs.");
  }

  @Test
  void testIllegalConstructorTooSmall() {
    Object o = new Object();
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new InstanceLifecycleEvent(o, -1),
        "Invalid event did not throw IllegalArgumentException.");
  }

  @Test
  void testIllegalConstructorTooBig() {
    Object o = new Object();
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new InstanceLifecycleEvent(o, 8),
        "Invalid event did not throw IllegalArgumentException.");
  }
}
