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
 * StateInterrogation.java
 *
 */

package javax.jdo.spi;

import javax.jdo.PersistenceManager;

/**
 * This interface is implemented by a non-binary-compatible JDO implementation to provide state
 * interrogation for non-enhanced persistent classes.
 *
 * <p>A call to JDOHelper to get the status of an instance is handled internally if the parameter
 * instance implements PersistenceCapable. For non-binary-compatible implementations, there is no
 * requirement that persistent instances implement PersistenceCapable. Therefore, if the parameter
 * does not implement PersistenceCapable, JDOHelper delegates to all registered instances of
 * StateInterrogation until an instance can handle the request.
 *
 * <p>For JDOHelper isXXX methods, which return boolean, the corresponding method in
 * StateInterrogation returns Boolean. If the return value is <code>null</code> then the
 * StateInterrogation does not recognize the parameter as being handled by it. A non-null return
 * value indicates that the implementation has determined the answer.
 *
 * <p>For JDOHelper getXXX methods, which return an Object, each registered StateInterrogation is
 * given the parameter until one of them returns a non-null value, which is passed to the caller.
 *
 * <p>For JDOHelper makeDirty, each registered StateInterrogation is given the parameter until one
 * of them returns true, indicating that it has handled the call. An instance that implements this
 * interface must be registered with the {@link JDOImplHelper}.
 *
 * @version 2.0
 * @since 2.0
 */
public interface StateInterrogation {

  /**
   * Tests whether the parameter instance is persistent.
   *
   * <p>Instances that represent persistent objects in the data store return <code>Boolean.TRUE
   * </code>.
   *
   * <p>Instances known by the implementation to be non-persistent return <code>Boolean.FALSE</code>
   * .
   *
   * <p>Instances not recognized by the implementation return <code>null</code>.
   *
   * @see PersistenceManager#makePersistent(Object pc)
   * @see PersistenceCapable#jdoIsPersistent()
   * @param pc the instance.
   * @return <code>Boolean.TRUE</code> if the parameter instance is persistent.
   */
  Boolean isPersistent(Object pc);

  /**
   * Tests whether the parameter instance is transactional.
   *
   * <p>Instances whose state is associated with the current transaction return <code>Boolean.TRUE
   * </code>.
   *
   * <p>Instances known by the implementation to be non-transactional return <code>Boolean.FALSE
   * </code>.
   *
   * <p>Instances not recognized by the implementation return <code>null</code>.
   *
   * @see PersistenceCapable#jdoIsTransactional()
   * @param pc the instance.
   * @return <code>Boolean.TRUE</code> if the parameter instance is transactional.
   */
  Boolean isTransactional(Object pc);

  /**
   * Tests whether the parameter instance is dirty.
   *
   * <p>Instances that have been modified, deleted, newly made persistent in the current
   * transaction, or modified while detached return <code>Boolean.TRUE</code>.
   *
   * <p>Instances known by the implementation to be non-dirty return <code>Boolean.FALSE</code>.
   *
   * <p>Instances not recognized by the implementation return <code>null</code>.
   *
   * @see StateManager#makeDirty(PersistenceCapable pc, String fieldName)
   * @see PersistenceCapable#jdoIsDirty()
   * @param pc the instance.
   * @return <code>Boolean.TRUE</code> if the parameter instance has been modified in the current
   *     transaction, or while detached.
   */
  Boolean isDirty(Object pc);

  /**
   * Tests whether the parameter instance has been newly made persistent.
   *
   * <p>Instances that have been made persistent in the current transaction return <code>
   * Boolean.TRUE</code>.
   *
   * <p>Instances known by the implementation to be non-new return <code>Boolean.FALSE</code>.
   *
   * <p>Instances not recognized by the implementation return <code>null</code>.
   *
   * @see PersistenceManager#makePersistent(Object pc)
   * @see PersistenceCapable#jdoIsNew()
   * @param pc the instance.
   * @return <code>Boolean.TRUE</code> if the parameter instance was made persistent in the current
   *     transaction.
   */
  Boolean isNew(Object pc);

  /**
   * Tests whether the parameter instance has been deleted.
   *
   * <p>Instances that have been deleted in the current transaction return <code>Boolean.TRUE</code>
   * .
   *
   * <p>Instances known by the implementation to be non-deleted return <code>Boolean.FALSE</code>.
   *
   * <p>Instances not recognized by the implementation return <code>null</code>.
   *
   * @see PersistenceManager#deletePersistent(Object pc)
   * @see PersistenceCapable#jdoIsDeleted()
   * @param pc the instance.
   * @return <code>Boolean.TRUE</code> if the parameter instance was deleted in the current
   *     transaction.
   */
  Boolean isDeleted(Object pc);

  /**
   * Tests whether the parameter instance is detached.
   *
   * <p>Instances that are detached return <code>Boolean.TRUE</code>.
   *
   * <p>Instances known by the implementation to be non-detached return <code>Boolean.FALSE</code>.
   *
   * <p>Instances not recognized by the implementation return <code>null</code>.
   *
   * @see PersistenceManager#detachCopy(Object pc)
   * @see PersistenceCapable#jdoIsDeleted()
   * @param pc the instance.
   * @return <code>Boolean.TRUE</code> if the parameter instance is detached.
   */
  Boolean isDetached(Object pc);

  /**
   * Return the associated <code>PersistenceManager</code> if there is one. Transactional and
   * persistent instances return the associated <code>PersistenceManager</code>.
   *
   * <p>Transient non-transactional instances return <code>null</code>.
   *
   * <p>Instances unknown by the implementation return <code>null</code>.
   *
   * @see PersistenceCapable#jdoGetPersistenceManager()
   * @param pc the instance.
   * @return the <code>PersistenceManager</code> associated with the parameter instance.
   */
  PersistenceManager getPersistenceManager(Object pc);

  /**
   * Return a copy of the JDO identity associated with the parameter instance.
   *
   * <p>Persistent instances of <code>PersistenceCapable</code> classes have a JDO identity managed
   * by the <code>PersistenceManager</code>. This method returns a copy of the ObjectId that
   * represents the JDO identity.
   *
   * <p>Instances unknown by the implementation return <code>null</code>.
   *
   * <p>The ObjectId may be serialized and later restored, and used with a <code>PersistenceManager
   * </code> from the same JDO implementation to locate a persistent instance with the same data
   * store identity.
   *
   * <p>If the JDO identity is managed by the application, then the ObjectId may be used with a
   * <code>PersistenceManager</code> from any JDO implementation that supports the <code>
   * PersistenceCapable</code> class.
   *
   * <p>If the JDO identity is not managed by the application or the data store, then the ObjectId
   * returned is only valid within the current transaction.
   *
   * <p>
   *
   * @see PersistenceManager#getObjectId(Object pc)
   * @see PersistenceCapable#jdoGetObjectId()
   * @see PersistenceManager#getObjectById(Object oid, boolean validate)
   * @param pc the instance.
   * @return a copy of the ObjectId of the parameter instance as of the beginning of the
   *     transaction.
   */
  Object getObjectId(Object pc);

  /**
   * Return a copy of the JDO identity associated with the parameter instance.
   *
   * <p>Instances unknown by the implementation return <code>null</code>.
   *
   * @see PersistenceCapable#jdoGetTransactionalObjectId()
   * @see PersistenceManager#getObjectById(Object oid, boolean validate)
   * @param pc the instance.
   * @return a copy of the ObjectId of the parameter instance as modified in this transaction.
   */
  Object getTransactionalObjectId(Object pc);

  /**
   * Return the version of the parameter instance.
   *
   * <p>Instances unknown by the implementation return <code>null</code>.
   *
   * @see PersistenceCapable#jdoGetVersion()
   * @param pc the instance.
   * @return a copy of the ObjectId of the parameter instance as modified in this transaction.
   */
  Object getVersion(Object pc);

  /**
   * Explicitly mark the parameter instance and field dirty. Normally, <code>PersistenceCapable
   * </code> classes are able to detect changes made to their fields. However, if a reference to an
   * array is given to a method outside the class, and the array is modified, then the persistent
   * instance is not aware of the change. This API allows the application to notify the instance
   * that a change was made to a field.
   *
   * <p>Instances unknown by the implementation are unaffected.
   *
   * @see PersistenceCapable#jdoMakeDirty(String fieldName)
   * @param pc the instance.
   * @param fieldName the name of the field to be marked dirty.
   * @return Whether it was made dirty
   */
  boolean makeDirty(Object pc, String fieldName);
}
