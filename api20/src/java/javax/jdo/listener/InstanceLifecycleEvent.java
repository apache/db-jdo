/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
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
 * InstanceLifecycleEvent.java
 *
 */

package javax.jdo.listener;

/**
 * This is the event class used in life cycle event notifications.
 * <P>Note that although InstanceLifecycleEvent inherits Serializable interface 
 * from EventObject, it is not intended to be Serializable. Appropriate 
 * serialization methods are implemented to throw NotSerializableException.
 * @version 2.0
 * @since 2.0
 */
public class InstanceLifecycleEvent
    extends java.util.EventObject {

    public static final int CREATE = 0;
    public static final int LOAD = 1;
    public static final int STORE = 2;
    public static final int CLEAR = 3;
    public static final int DELETE = 4;
    public static final int DIRTY = 5;
    public static final int DETACH = 6;
    public static final int ATTACH = 7;

    /**
     * The event type that triggered the construction of this event object.
     */
    private final int eventType;
    
    /** 
     * The "other" object associated with the event.
     */
    private final Object target;

    /**
     *	Creates a new event object with the specified
     *	<code>source</code> and <code>type</code>.
     * @param source the instance that triggered the event
     * @param type the event type
     * @since 2.0
     */
    public InstanceLifecycleEvent (Object source, int type) {
        super (source);
        eventType = type;
        target = null;
    }

    /**
     *	Creates a new event object with the specified
     *	<code>source</code>, <code>type</code>, and <code>target</code>.
     * @param source the instance that triggered the event
     * @param type the event type
     * @param target the "other" instance
     * @since 2.0
     */
    public InstanceLifecycleEvent (Object source, int type, Object target) {
        super (source);
        eventType = type;
        this.target = target;
    }

    /**
     *	Returns the event type that triggered this event.
     * @return the event type
     * @since 2.0
     */
    public int getEventType () {
        return eventType;
    }

    /**
     *	Returns the "other" object.
     * @return the "other" object
     * @since 2.0
     */
    public Object getTarget () {
        return target;
    }
    
    /**
     * Serialization is not supported for InstanceLifecycleEvents.
     * param out the output stream
     * @since 2.0
     */
    private void writeObject(java.io.ObjectOutputStream out) 
        throws java.io.IOException {
        throw new java.io.NotSerializableException();
    }
}