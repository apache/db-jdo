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
 
package org.apache.jdo.tck.api.persistencemanager.fetchplan;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.FetchPlan;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PCRect;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * This class is an abstract superclass for the fetch plan tests.
 * It contains methods useful for testing the behavior of the
 * fetch plan.
 */

public class AbstractFetchPlanTest extends JDO_Test {

    /**
     * The <code>main</code> method is not defined in this abstract class.
     */

    /** The persistent instances used in the test.
     */
    protected PCPoint upperLeft;
    protected PCPoint lowerRight;
    protected PCRect pcrect;

    /** The oids of the persistent instances used in the test.
     */
    protected Object upperLeftoid;
    protected Object lowerRightoid;
    protected Object pcrectoid;

    /** The String arrays used for setting fetch groups.
     */
    protected String[] defaultGroup = new String[]
        {"default"};
    protected String[] upperLeftGroup = new String[]
        {"default", "PCRect.upperLeft"};
    protected String[] lowerRightGroup = new String[]
        {"default", "PCRect.lowerRight"};
    protected String[] bothGroup = new String[]{
        "default", "PCRect.upperLeft", "PCRect.lowerRight"};
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(PCRect.class);
        addTearDownClass(PCPoint.class);
        upperLeft = new PCPoint(0,10);
        lowerRight = new PCPoint(10,0);
        pcrect = new PCRect(upperLeft, lowerRight);
        getPM().currentTransaction().begin();
        pm.makePersistent(pcrect); // makes all three persistent
        upperLeftoid = pm.getObjectId(upperLeft);
        lowerRightoid = pm.getObjectId(lowerRight);
        pcrectoid = pm.getObjectId(pcrect);
        pm.currentTransaction().commit();
    }

    /** Set the default plus upper left field as the fetch group.
     */
    protected void setDefaultGroup() {
        FetchPlan fp = getPM().getFetchPlan();
        fp.setGroups(defaultGroup);
    }

    /** Set the default plus upper left field as the fetch group.
     */
    protected void setUpperLeftGroup() {
        FetchPlan fp = getPM().getFetchPlan();
        fp.setGroups(upperLeftGroup);
    }

    /** Set the default plus lower right field as the fetch group.
     */
    protected void setLowerRightGroup() {
        FetchPlan fp = getPM().getFetchPlan();
        fp.setGroups(upperLeftGroup);
    }

    /** Set the default plus both fields as the fetch group.
     */
    protected void setBothGroup() {
        FetchPlan fp = getPM().getFetchPlan();
        fp.setGroups(bothGroup);
    }

    /** */
    protected void checkBothLoaded(String location, PCRect pcrect) {
        checkUpperLeftLoaded(location, pcrect);
        checkLowerRightLoaded(location, pcrect);
    }

    /** */
    protected void checkUpperLeftLoaded(String location, PCRect pcrect) {
        if (pcrect.upperLeft == null) {
            appendMessage(location + NL + "Upper Left was null." + NL +
                "The fetch plan includes PCRect.upperLeft and this field" +
                " should have been loaded.");
        }
    }

    /** */
    protected void checkLowerRightLoaded(String location, PCRect pcrect) {
        if (pcrect.lowerRight == null) {
            appendMessage(location + NL + "Lower Right was null." + NL +
                "The fetch plan includes PCRect.lowerRight and this field" +
                " should have been loaded.");
        }
    }
    protected void checkGroups(String location, 
            FetchPlan fetchPlan, String[] groups) {
        Collection expected = new HashSet();
        Collection actual = fetchPlan.getGroups();
        for (int i = 0; i < groups.length; ++i) {
            expected.add(groups[i]);
        }
        if (!expected.equals(actual)) {
            appendMessage(location + NL + "Fetch groups differ." + NL +
                "expected: " + expected + NL +
                "actual: " + actual + NL);
        }
    }
}