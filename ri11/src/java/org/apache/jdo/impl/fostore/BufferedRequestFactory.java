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
class BufferedRequestFactory implements RequestFactory {
    private static BufferedRequestFactory instance;

    private BufferedRequestFactory() { }

    static RequestFactory getInstance() {
        if (null == instance) {
            instance = new BufferedRequestFactory();
        }
        return instance;
    }
    
    /**
     * @see RequestFactory#getCreateOIDRequest
     */
    public CreateOIDRequest getCreateOIDRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf,
        OID oid, PersistenceManagerInternal pm) {

        return new CreateOIDRequest(sm, m, pmf, oid, pm);
    }

    /**
     * @see RequestFactory#getActivateClassRequest
     */
    public ActivateClassRequest getActivateClassRequest(
        Class cls, Message m, FOStorePMF pmf) {
        
        return new ActivateClassRequest(cls, m, pmf);
    }

    /**
     * @see RequestFactory#getInsertRequest
     */
    public InsertRequest getInsertRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf) {

        return new InsertRequest(sm, m, pmf);
    }

    /**
     * @see RequestFactory#getUpdateRequest
     */
    public UpdateRequest getUpdateRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf,
        BitSet loadedFields, BitSet dirtyFields, boolean optimistic) {

        return new UpdateRequest(sm, m, pmf,
                                 loadedFields, dirtyFields, optimistic);
    }

    /**
     * @see RequestFactory#getVerifyRequest
     */
    public VerifyRequest getVerifyRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf,
        boolean verifyFields, BitSet fieldsToVerify) {

        return new VerifyRequest(sm, m, pmf, verifyFields, fieldsToVerify);
    }

    /**
     * @see RequestFactory#getFetchRequest
     */
    public FetchRequest getFetchRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf) {

        return new FetchRequest(sm, m, pmf);
    }

    /**
     * @see RequestFactory#getGetExtentRequest
     */
    public GetExtentRequest getGetExtentRequest(
        FOStoreExtent extent, Class pcClass, 
        boolean subclasses, Message m,
        PersistenceManagerInternal pm) {

        return new GetExtentRequest(extent, pcClass, subclasses, m, pm);
    }

    /**
     * @see RequestFactory#getGetInstancesRequest
     */
    public GetInstancesRequest getGetInstancesRequest(
        ArrayList oids, int start, int numInstances,
        Message m, PersistenceManagerInternal pm, Class cls) {

        return new GetInstancesRequest(oids, start, numInstances, m, pm, cls);
    }

    /**
     * @see RequestFactory#getDeleteRequest
     */
    public DeleteRequest getDeleteRequest(
        StateManagerInternal sm, Message m, FOStorePMF pmf) {

        return new DeleteRequest(sm, m, pmf);
    }

    /**
     * @see RequestFactory#getGetClassRequest
     */
    public GetClassRequest getGetClassRequest(
        CLID clid, Message m, FOStorePMF pmf, PersistenceManagerInternal pm) {

        return new GetClassRequest(clid, m, pmf, pm);
    }

    /**
     * @see RequestFactory#getBeginTxRequest
     */
    public BeginTxRequest getBeginTxRequest(
        Message m, FOStorePMF pmf, boolean optimistic) {

        return new BeginTxRequest(m, pmf, optimistic);
    }

    /**
     * @see RequestFactory#getCommitRequest
     */
    public CommitRequest getCommitRequest(
        Message m, FOStorePMF pmf) {

        return new CommitRequest(m, pmf);
    }

    /**
     * @see RequestFactory#getRollbackRequest
     */
    public RollbackRequest getRollbackRequest(
        Message m, FOStorePMF pmf) {

        return new RollbackRequest(m, pmf);
    }

    /**
     * @see RequestFactory#getDumpRequest
     */
    public DumpRequest getDumpRequest(
        DumpOption option, String className, Message m,
        FOStorePMF pmf) {
        return new DumpRequest(option, className, m, pmf);
    }
}
