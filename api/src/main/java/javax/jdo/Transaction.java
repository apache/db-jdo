/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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

/*
 * Transaction.java
 *
 */

package javax.jdo;

import javax.transaction.Synchronization;

/**
 * The JDO <code>Transaction</code> interface provides for initiation and completion of transactions
 * under user control. It is a sub-interface of the {@link PersistenceManager} that deals with
 * options and transaction demarcation.
 *
 * <p>Transaction options include whether optimistic concurrency control should be used for the
 * current transaction, whether instances may hold values in the cache outside transactions, and
 * whether values should be retained in the cache after transaction completion. These options are
 * valid for both managed and non-managed transactions.
 *
 * <p>Transaction initiation and completion methods have similar semantics to <code>
 * javax.transaction.UserTransaction</code> when used outside a managed environment. When used in a
 * managed environment, transaction initiation and completion methods may only be used with
 * bean-managed transaction semantics.
 *
 * @version 3.0
 */
public interface Transaction {
  /**
   * Begin a transaction. The type of transaction is determined by the setting of the Optimistic
   * flag.
   *
   * @see #setOptimistic
   * @see #getOptimistic
   * @throws JDOUserException if transactions are managed by a container in the managed environment,
   *     or if the transaction is already active.
   */
  void begin();

  /**
   * Commit the current transaction.
   *
   * @throws JDOUserException if transactions are managed by a container in the managed environment,
   *     or if the transaction is not active.
   */
  void commit();

  /**
   * Roll back the current transaction.
   *
   * @throws JDOUserException if transactions are managed by a container in the managed environment,
   *     or if the transaction is not active.
   */
  void rollback();

  /**
   * Returns whether there is a transaction currently active.
   *
   * @return <code>true</code> if the transaction is active.
   */
  boolean isActive();

  /**
   * Returns the rollback-only status of the transaction. When begun, the rollback-only status is
   * false. Either the application or the JDO implementation may set this flag using
   * setRollbackOnly.
   *
   * @return <code>true</code> if the transaction has been marked for rollback.
   * @since 2.0
   */
  boolean getRollbackOnly();

  /**
   * Sets the rollback-only status of the transaction to <code>true</code>. After this flag is set
   * to <code>true</code>, the transaction can no longer be committed, and any attempt to commit the
   * transaction will throw <code>JDOFatalDataStoreException</code>.
   *
   * @since 2.0
   */
  void setRollbackOnly();

  /**
   * If <code>true</code>, allow persistent instances to be read without a transaction active. If an
   * implementation does not support this option, a <code>JDOUnsupportedOptionException</code> is
   * thrown.
   *
   * @param nontransactionalRead the value of the nontransactionalRead property
   */
  void setNontransactionalRead(boolean nontransactionalRead);

  /**
   * If <code>true</code>, allows persistent instances to be read without a transaction active.
   *
   * @return the value of the nontransactionalRead property
   */
  boolean getNontransactionalRead();

  /**
   * If <code>true</code>, allow persistent instances to be written without a transaction active. If
   * an implementation does not support this option, a <code>JDOUnsupportedOptionException</code> is
   * thrown.
   *
   * @param nontransactionalWrite the value of the nontransactionalRead property
   */
  void setNontransactionalWrite(boolean nontransactionalWrite);

  /**
   * If <code>true</code>, allows persistent instances to be written without a transaction active.
   *
   * @return the value of the nontransactionalWrite property
   */
  boolean getNontransactionalWrite();

  /**
   * If <code>true</code>, at commit instances retain their values and the instances transition to
   * persistent-nontransactional. If an implementation does not support this option, a <code>
   * JDOUnsupportedOptionException</code> is thrown.
   *
   * @param retainValues the value of the retainValues property
   */
  void setRetainValues(boolean retainValues);

  /**
   * If <code>true</code>, at commit time instances retain their field values.
   *
   * @return the value of the retainValues property
   */
  boolean getRetainValues();

  /**
   * If <code>true</code>, at rollback, fields of newly persistent instances are restored to their
   * values as of the beginning of the transaction, and the instances revert to transient.
   * Additionally, fields of modified instances of primitive types and immutable reference types are
   * restored to their values as of the beginning of the transaction.
   *
   * <p>If <code>false</code>, at rollback, the values of fields of newly persistent instances are
   * unchanged and the instances revert to transient. Additionally, dirty instances transition to
   * hollow. If an implementation does not support this option, a <code>
   * JDOUnsupportedOptionException</code> is thrown.
   *
   * @param restoreValues the value of the restoreValues property
   */
  void setRestoreValues(boolean restoreValues);

  /**
   * Return the current value of the restoreValues property.
   *
   * @return the value of the restoreValues property
   */
  boolean getRestoreValues();

  /**
   * Optimistic transactions do not hold data store locks until commit time. If an implementation
   * does not support this option, a <code>JDOUnsupportedOptionException</code> is thrown.
   *
   * @param optimistic the value of the Optimistic flag.
   */
  void setOptimistic(boolean optimistic);

  /**
   * Optimistic transactions do not hold data store locks until commit time.
   *
   * @return the value of the Optimistic property.
   */
  boolean getOptimistic();

  /**
   * Get the value for transaction isolation level for this transaction.
   *
   * @return the transaction isolation level
   * @see #setIsolationLevel(String)
   * @since 2.2
   */
  String getIsolationLevel();

  /**
   * Set the value for transaction isolation level for this transaction. Transaction isolation
   * levels are defined in javax.jdo.Constants. If the requested level is not available, but a
   * higher level is available, the higher level is silently used. If the requested level is not
   * available, and no higher level is available, then JDOUnsupportedOptionException is thrown. Five
   * standard isolation levels are defined. Other isolation levels might be supported by an
   * implementation but are not standard.
   *
   * <p>Standard values in order of low to high are:
   *
   * <ul>
   *   <li>read-uncommitted
   *   <li>read-committed
   *   <li>repeatable-read
   *   <li>snapshot
   *   <li>serializable
   * </ul>
   *
   * @param level the transaction isolation level
   * @see #getIsolationLevel()
   * @see Constants#TX_READ_UNCOMMITTED
   * @see Constants#TX_READ_COMMITTED
   * @see Constants#TX_REPEATABLE_READ
   * @see Constants#TX_SNAPSHOT
   * @see Constants#TX_SERIALIZABLE
   * @since 2.2
   */
  void setIsolationLevel(String level);

  /**
   * The user can specify a <code>Synchronization</code> instance to be notified on transaction
   * completions. The <code>beforeCompletion</code> method is called prior to flushing instances to
   * the data store.
   *
   * <p>The <code>afterCompletion</code> method is called after performing state transitions of
   * persistent and transactional instances, following the data store commit or rollback operation.
   *
   * <p>Only one <code>Synchronization</code> instance can be registered with the <code>Transaction
   * </code>. If the application requires more than one instance to receive synchronization
   * callbacks, then the single application instance is responsible for managing them, and
   * forwarding callbacks to them.
   *
   * @param sync the <code>Synchronization</code> instance to be notified; <code>null</code> for
   *     none
   */
  void setSynchronization(Synchronization sync);

  /**
   * The user-specified <code>Synchronization</code> instance for this <code>Transaction</code>
   * instance.
   *
   * @return the user-specified <code>Synchronization</code> instance.
   */
  Synchronization getSynchronization();

  /**
   * The <code>Transaction</code> instance is always associated with exactly one <code>
   * PersistenceManager</code>.
   *
   * @return the <code>PersistenceManager</code> for this <code>Transaction</code> instance
   */
  PersistenceManager getPersistenceManager();

  /**
   * If <code>true</code>, a lock will be applied to all objects read in this transaction.
   *
   * <p>If <code>false</code> then retrieved objects will not be locked. If null will fallback to
   * the value for metadata for the class in question.
   *
   * @param serialize the value of the serializeRead property
   * @since 3.0
   */
  void setSerializeRead(Boolean serialize);

  /**
   * Return the current value of the serializeRead property.
   *
   * @return the value of the serializeRead property
   * @since 3.0
   */
  Boolean getSerializeRead();
}
