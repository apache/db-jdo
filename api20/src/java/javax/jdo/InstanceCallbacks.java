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
 * InstanceCallbacks.java
 *
 */
 
package javax.jdo;

/** A <code>PersistenceCapable</code> class that provides callback methods for life
 * cycle events implements this interface.
 *
 * <P>For JDO 2.0, <code>InstanceCallbacks</code> has been refactored to extend 
 * four other interfaces, without changing any of the methods or semantics. 
 * This allows fine-grained control over callbacks, for
 * example to allow a class to implement the load callback without
 * implementing any of the other callbacks. For backward compatibility
 * with JDO 1.0, the <code>InstanceCallbacks</code> interface is preserved.
 *
 * <P>Classes which include non-persistent fields whose values depend
 * on the values of persistent fields require callbacks on specific
 * JDO instance life cycle events in order to correctly populate the
 * values in these fields.
 *
 * <P>The callbacks might also be used if the persistent instances
 * need to be put into the runtime infrastructure of the application.
 * For example, a persistent instance might notify other instances
 * on changes to state.  The persistent instance might be in a list of
 * managed instances. When the persistent instance is made hollow,
 * it can no longer generate change events, and the persistent
 * instance should be removed from the list of managed instances.
 *
 * <P>To implement this, the application programmer would implement
 * <code>jdoPostLoad</code> to put itself into the list of managed
 * instances, and implement <code>jdoPreClear</code> to remove itself from
 * the list. With JDO 1.0, the domain class would be declared to implement
 * <code>InstanceCallbacks</code>. With JDO 2.0, the domain class 
 * would be declared to implement
 * <code>javax.jdo.listener.LoadCallback</code> and 
 * <code>javax.jdo.listener.ClearCallback</code>.
 *
 * <P>Note that JDO does not manage the state of non-persistent
 * fields, and when a JDO instance transitions to hollow, JDO clears
 * the persistent fields.  It is the programmer's responsibility to
 * clear non-persistent fields so that garbage collection of
 * referred instances can occur.
 *
 * @since 1.0
 * @version 2.0
 */
public interface InstanceCallbacks 
    extends javax.jdo.listener.ClearCallback, 
        javax.jdo.listener.DeleteCallback,
        javax.jdo.listener.LoadCallback,
        javax.jdo.listener.StoreCallback {
}
