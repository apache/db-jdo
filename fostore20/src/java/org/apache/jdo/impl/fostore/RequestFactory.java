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

package org.apache.jdo.impl.fostore;

import java.util.ArrayList;
import java.util.BitSet;

import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.state.StateManagerInternal;


/**
 * Creates different kinds of requests.
 *
 * @author Dave Bristor
 */
interface RequestFactory {
    /**
     * Creates a request object that will get a datastore OID for a
     * provisional OID.
     */
    public CreateOIDRequest getCreateOIDRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf,
        OID oid, PersistenceManagerInternal pm);

    /**
     * Creates a request object to activate the class corresponding to the
     * given oid.
     * @param cls Class to be activated.
     * @param m Message by which the request is to be sent to the store.
     * @param pmf FOStorePMF in which the request is taking place.
     */
    public ActivateClassRequest getActivateClassRequest(
        Class cls, Message m, FOStorePMF pmf);

    /**
     * Creates a request object to cause a persistent object to be inserted
     * into the datastore.
     * @param sm StateManagerInternal of the object to be stored in the
     * datastore. 
     * @param m Message by which the request is to be sent to the store.
     * @param pmf FOStorePMF in which the request is taking place.
     */
    public InsertRequest getInsertRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf);

    /**
     * Creates a request object to cause one or more fields of a persistent
     * object to be updated in the store.
     * @param sm StateManagerInternal of the object to be updated.
     * @param m Message by which the request is to be sent to the store.
     * @param pmf FOStorePMF in which the request is taking place.
     * @param loadedFields Set of fields loaded from the database.
     * @param dirtyFields Set of fields that are to be flushed and
     * verified against the those in the database, if this
     * <code>update</code> is within the context of an optimistic
     * transaction.
     * @param optimistic If true, then update is happening in context of
     * optimistic transaction, otherwise datastore transaction.
     */
    public UpdateRequest getUpdateRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf,
        BitSet loadedFields, BitSet dirtyFields, boolean optimistic);

    /**
     * Creates a request object to verify that in-memory data is the same as
     * that in the database.
     * @param sm StateManagerInternal of the object to be verified.
     * @param m Message by which the request is to be sent to the store.
     * @param pmf FOStorePMF in which the request is taking place.
     * @param verifyFields If true, verify values of object, otherwise verify
     * only existence (and ignore remaining parameters).
     * @param loadedFields Set of fields to be verified against those in the
     * database.
     */
    public VerifyRequest getVerifyRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf,
        boolean verifyFields, BitSet loadedFields);

    /**
     * Creates a request object to cause one or more fields of a persistent
     * object to be read from the store.
     * @param sm StateManagerInternal of the object whose field(s) are to be
     * read. 
     * @param m Message by which the request is to be sent to the store.
     * @param pmf FOStorePMF in which the request is taking place.
     */
    public FetchRequest getFetchRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf);

    /**
     * Creates a request object to cause a particular class's extent to be
     * retrieved.
     * @param extent FOStoreExtent for which the request is being created.
     * @param pcClass Class of the objects whose extent is sought.  It is
     * <em>required</em> that the caller ensure that the given pcClass
     * implement javax.jdo.PersistenceCapable.
     * @param subclasses If false, retrieve instances of pcClass only; if true
     * retrieve those plus all instances of subclasses of pcClass.
     * @param m Message by which the request is to be sent to the store.
     * @param pm PersistenceManager on whose behalf the request is taking
     * place.
     */
    public GetExtentRequest getGetExtentRequest(
        FOStoreExtent extent, Class pcClass, 
        boolean subclasses, Message m,
        PersistenceManagerInternal pm);

    /**
     * Creates a request to get instances for some oids.
     * @param oids List of oids for which instances are needed.
     * @param start Starting index in oids for which instances are needed.
     * @param numInstances Number of instances which are needed.
     * @param pm PersistenceManager on whose behalf the request is taking
     * place.
     * @param cls Candidate Class for which instances are being obtained.
     */
    public GetInstancesRequest getGetInstancesRequest(
        ArrayList oids, int start, int numInstances,
        Message m, PersistenceManagerInternal pm, Class cls);

    /**
     * Creates a request object to cause a persistent object in the store to
     * be deleted.
     * @param sm StateManagerInternal of the object to delete in the store.
     * @param m Message by which the request is to be sent to the store.
     * @param pmf FOStorePMF in which the request is taking place.
     */
    public DeleteRequest getDeleteRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf);

    /**
     * Creates a request object to cause the java.lang.Class associated with
     * the given CLID to be provided.
     * @param clid CLID of the class that is needed.
     * @param m Message by which the request is to be sent to the store.
     * @param pmf FOStorePMF in which the request is taking place.
     * @param pm PersistenceManager used to load the class.
     * place.
 */
    public GetClassRequest getGetClassRequest(
        CLID clid, Message m, FOStorePMF pmf, PersistenceManagerInternal pm);

    /**
     * Creates a request object which notifies the store of the kind of
     * transaction that is starting.
     * @param m Message by which the request is to be sent to the store.
     * @param pmf FOStorePMF in which the request is taking place.
     * @param optimistic Indicates whether an optimistic or datastore
     * transaction is beginning.
     */
    public BeginTxRequest getBeginTxRequest(
        Message m, FOStorePMF pmf, boolean optimistic);

    /**
     * Creates a request object which causes previous operations to commit.
     * @param m Message by which the request is to be sent to the store.
     * @param pmf FOStorePMF in which the request is taking place.
     */
    public CommitRequest getCommitRequest(
        Message m, FOStorePMF pmf);

    /**
     * Creates a request object which causes previous operations to rollback.
     * @param m Message by which the request is to be sent to the store.
     * @param pmf FOStorePMF in which the request is taking place.
     */
    public RollbackRequest getRollbackRequest(
        Message m, FOStorePMF pmf);

    /**
     * Creates a request object to get information from the store.
     * @param option Diagnostic parameter code.
     * @param className Optional class name.
     * @param m Message by which the request is to be sent to the store.
     * @param pmf FOStorePMF in which the request is taking place.
     */
    public DumpRequest getDumpRequest(
        DumpOption option, String className, Message m,
        FOStorePMF pmf);
}
