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
 * InstanceLifecycleEvent.java
 *
 */

package javax.jdo.listener;

import javax.jdo.spi.I18NHelper;

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

    private static final int FIRST_EVENT_TYPE = 0;
    public static final int CREATE = 0;
    public static final int LOAD = 1;
    public static final int STORE = 2;
    public static final int CLEAR = 3;
    public static final int DELETE = 4;
    public static final int DIRTY = 5;
    public static final int DETACH = 6;
    public static final int ATTACH = 7;
    private static final int LAST_EVENT_TYPE = 7;

    /** The Internationalization message helper.
     */
    private final static I18NHelper msg = I18NHelper.getInstance ("javax.jdo.Bundle"); //NOI18N

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
        this(source, type, null);
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
        if (type < FIRST_EVENT_TYPE || type > LAST_EVENT_TYPE) {
            throw new IllegalArgumentException(msg.msg("EXC_IllegalEventType"));
        }
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
     * The source object of the Event.  Although not deprecated,
     * it is recommended that the the methods
     * <code>getPersistentInstance()</code> and
     * <code>getDetachedInstance()</code> be used instead.
     *
     * @return   The persistent instance on any pre- callback except preAttach,
     * or the detached instance for a postDetach or preAttach callback.
     *
     * @see #getPersistentInstance()
     * @see #getDetachedInstance()
     * @see "Section 12.15, Java Data Objects 2.0 Specification"
     */
    public Object getSource() {
        return super.getSource();
    }

    /**
     * The target object of the Event.  Although not deprecated,
     * it is recommended that the the methods
     * <code>getPersistentInstance()</code> and
     * <code>getDetachedInstance()</code> be used instead.
     *
     * @return The detached instance for preDetach and postAttach, 
     * the persistent instance otherwise.
     *
     * @since 2.0
     * @see #getPersistentInstance()
     * @see #getDetachedInstance()
     * @see "Section 12.15, Java Data Objects 2.0 Specification"
     */
    public Object getTarget () {
        return target;
    }

    /**
     * Returns the persistent instance involved in the event.
     *
     * @return The persistent instance involved in the event, or null if there 
     * was none.
     *
     * @see "Section 12.15, Java Data Objects 2.0 Specification"
     */
    public Object getPersistentInstance() {
        switch (getEventType()) {
            case DETACH:
                return target == null
                        ? getSource()   // preDetach:  source is persistent instance
                        : getTarget();  // postDetach:  target is persistent instance
            case ATTACH:
                return target == null
                        ? null          // preAttach:  no persistent instance yet
                        : getSource();  // postAttach:  source is persistent instance
        }

        // for all other events, source is persistent instance
        return getSource();
    }

    /**
     * Returns the detached instance involved in the event.
     *
     * @return The detached instance involved in the event, or null if there was none.
     *
     * @see "Section 12.15, Java Data Objects 2.0 Specification"
     */
    public Object getDetachedInstance() {
        switch (getEventType()) {
            case DETACH:
                return target == null
                        ? null          // preDetach:  no detached instance yet
                        : getSource();  // postDetach:  source is detached instance
            case ATTACH:
                return target == null
                        ? getSource()   // preAttach:  source is detached instance
                        : getTarget();  // postAttach:  target is detached instance
        }

        // for all other events, there is no detached instance
        return null;
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
