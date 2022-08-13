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

/*
 * InstanceLifecycleEventTest.java
 *
 */

package javax.jdo.listener;

import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;

/**
 * Tests that instances of InstanceLifecycleEvent can be created and
 * that the source, type, and target instances are correct.
 */
public class InstanceLifecycleEventTest extends AbstractTest {
    
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
    public InstanceLifecycleEventTest() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BatchTestRunner.run(InstanceLifecycleEventTest.class);
    }
    
    public void testConstructorCreateEvent() {
        InstanceLifecycleEvent e = new InstanceLifecycleEvent 
                (created, InstanceLifecycleEvent.CREATE);
        assertSame ("Create source differs.", created, e.getSource());
        assertEquals ("Create type differs.", InstanceLifecycleEvent.CREATE, e.getEventType());
    }
    
    public void testConstructorLoadEvent() {
        InstanceLifecycleEvent e = new InstanceLifecycleEvent 
                (loaded, InstanceLifecycleEvent.LOAD);
        assertSame ("Load source differs.", loaded, e.getSource());
        assertEquals ("Load type differs.", InstanceLifecycleEvent.LOAD, e.getEventType());
    }
    
    public void testConstructorStoreEvent() {
        InstanceLifecycleEvent e = new InstanceLifecycleEvent 
                (stored, InstanceLifecycleEvent.STORE);
        assertSame ("Store source differs.", stored, e.getSource());
        assertEquals ("Store type differs.", InstanceLifecycleEvent.STORE, e.getEventType());
    }
    
    public void testConstructorClearEvent() {
        InstanceLifecycleEvent e = new InstanceLifecycleEvent 
                (cleared, InstanceLifecycleEvent.CLEAR);
        assertSame ("Clear source differs.", cleared, e.getSource());
        assertEquals ("Clear type differs.", InstanceLifecycleEvent.CLEAR, e.getEventType());
    }
    
    public void testConstructorDeleteEvent() {
        InstanceLifecycleEvent e = new InstanceLifecycleEvent 
                (deleted, InstanceLifecycleEvent.DELETE);
        assertSame ("Delete source differs.", deleted, e.getSource());
        assertEquals ("Delete type differs.", InstanceLifecycleEvent.DELETE, e.getEventType());
    }
    
    public void testConstructorDirtyEvent() {
        InstanceLifecycleEvent e = new InstanceLifecycleEvent 
                (dirtied, InstanceLifecycleEvent.DIRTY);
        assertSame ("Dirty source differs.", dirtied, e.getSource());
        assertEquals ("Dirty type differs.", InstanceLifecycleEvent.DIRTY, e.getEventType());
    }
    
    public void testConstructorDetachEvent() {
        InstanceLifecycleEvent e = new InstanceLifecycleEvent 
                (detached, InstanceLifecycleEvent.DETACH, detachTarget);
        assertSame ("Detach source differs.", detached, e.getSource());
        assertEquals ("Detach type differs.", InstanceLifecycleEvent.DETACH, e.getEventType());
        assertSame ("Detach target differs.", detachTarget, e.getTarget());
    }
    
    public void testConstructorAttachEvent() {
        InstanceLifecycleEvent e = new InstanceLifecycleEvent 
                (attached, InstanceLifecycleEvent.ATTACH, attachTarget);
        assertSame ("Attach source differs.", attached, e.getSource());
        assertEquals ("Attach type differs.", InstanceLifecycleEvent.ATTACH, e.getEventType());
        assertSame ("Attach target differs.", attachTarget, e.getTarget());
    }
    
    public void testIllegalConstructorTooSmall() {
        try {
            new InstanceLifecycleEvent (new Object(), -1);
        } catch (IllegalArgumentException e) {
            return; // good catch
        } 
        fail ("Invalid event did not throw IllegalArgumentException.");
    }
    
    public void testIllegalConstructorTooBig() {
        try {
            new InstanceLifecycleEvent (new Object(), 8);
        } catch (IllegalArgumentException e) {
            return; // good catch
        }
        fail ("Invalid event did not throw IllegalArgumentException.");
    }
}
