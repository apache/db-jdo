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
 
package org.apache.jdo.tck.api.persistencemanager.fetchplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.FetchPlan;

import org.apache.jdo.tck.JDO_Test;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PCRect;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Test TITLE
 *<BR>
 *<B>Keywords:</B> fetch plan
 *<BR>
 *<B>Assertion IDs:</B> 12.7.5-1
 *<BR>
 *<B>Assertion Description: </B>
public interface FetchPlan {
String DEFAULT = "default";
String ALL = "all";
int FETCH_SIZE_GREEDY = -1;
int FETCH_SIZE_OPTIMAL = 0;
int DETACH_LOAD_FIELDS = 1;
int DETACH_UNLOAD_FIELDS = 2;
A12.7.1-1 [/** Add the fetchgroup to the set of active fetch groups. Duplicate names will be removed.
FetchPlan addGroup(String fetchGroupName);
/** Remove the fetch group from the set active fetch groups. 
FetchPlan removeGroup(String fetchGroupName);
/** Remove all active groups, including the default fetch group. 
FetchPlan clearGroups();
/** Return an immutable Set of the names of all active fetch groups. 
Set getGroups();
/** Set a Collection of group names to replace the current groups. Duplicate names will be removed.
FetchPlan setGroups(Collection fetchGroupNames);
/** Set an array of group names to replace the current groups. Duplicate names will be removed.
FetchPlan setGroups(String[] fetchGroupNames);
/** Set a single group to replace the current groups. 
FetchPlan setGroup(String fetchGroupName);] 
/** Set the roots for DetachAllOnCommit 
FetchPlan setDetachmentRoots(Collection roots);
/** Get the roots for DetachAllOnCommit 
Collection getDetachmentRoots();
/** Set the roots for DetachAllOnCommit 
FetchPlan setDetachmentRootClasses(Class[] rootClasses);
/** Get the roots for DetachAllOnCommit 
Class[] getDetachmentRootClasses();
/** Set the maximum fetch depth. 
FetchPlan setMaxFetchDepth(int fetchDepth);
/** Get the maximum fetch depth. 
int getMaxFetchDepth();
A12.7.1-2 [/** Set the fetch size for large result set support. 
FetchPlan setFetchSize(int fetchSize);
/** Return the fetch size; 0 if not set; -1 for greedy fetching. 
int getFetchSize();]
A12.7.1-3 [/** Set detachment options 
FetchPlan setDetachmentOptions(int options);
/** Return the detachment options 
int getDetachmentOptions();]
 */

public class FetchPlanInterface extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion 12.7.5-1 (FetchPlanTest) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(FetchPlanInterface.class);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(PCRect.class);
        addTearDownClass(PCPoint.class);
    }

    /** */
    protected boolean setEquals
            (Collection expected, Collection actual) {
        if (expected == actual) 
            return true;
        if (expected == null || actual == null) 
            return false;
        Set expectedSet = new HashSet(expected);
        Set actualSet = new HashSet(actual);
        return expectedSet.equals(actualSet);
    }

    /** */
    protected void failCompare(String message, 
            Object expected, Object actual) {
        appendMessage(ASSERTION_FAILED + message);
        appendMessage("expected: " + expected);
        appendMessage("actual: " + actual);
    }

    /** */
    protected void failCompare(String message, 
            int expected, int actual) {
        appendMessage(ASSERTION_FAILED + message);
        appendMessage("expected: " + expected);
        appendMessage("actual: " + actual);
    }

    /** */
    public void testGroups() {
        checkDefaultGroups();
        checkClearGroups();
        checkSetGroup();
        checkAddGroup();
        checkRemoveGroup();
        checkClearGroups();
        checkSetGroupsCollection();
        checkSetGroupsArray();
        failOnError();
    }

    /** */
    public void testDetachmentRoots() {
        checkGetDetachmentRoots();
        checkSetDetachmentRoots();
        checkSetDetachmentRootClasses();
        failOnError();
    }

    /** */
    public void testDetachmentOptions() {
        int expectedOptions = 
                FetchPlan.DETACH_LOAD_FIELDS + 
                FetchPlan.DETACH_UNLOAD_FIELDS;
        FetchPlan fp = getPM().getFetchPlan();
        int initialOptions = fp.getDetachmentOptions();
        if (FetchPlan.DETACH_LOAD_FIELDS != initialOptions) {
            failCompare(
                "testDetachmentOptions(): wrong getDetachmentOptions() " + 
                    "after getPersistenceManager().",
                    FetchPlan.DETACH_LOAD_FIELDS, initialOptions);
        }
        fp.setDetachmentOptions(expectedOptions);
        int actualOptions = fp.getDetachmentOptions();
        if (expectedOptions != actualOptions) {
            failCompare(
                "testDetachmentOptions(): wrong getDetachmentOptions() " + 
                    "after setDetachmentOptions().",
                    expectedOptions, actualOptions);
        }
        cleanupPM();
        failOnError();
    }

    /** */
    public void testMaxFetchDepth() {
        int expectedMaxFetchDepth = 12;
        FetchPlan fp = getPM().getFetchPlan();
        fp.setMaxFetchDepth(expectedMaxFetchDepth);
        int actualMaxFetchDepth = fp.getMaxFetchDepth();
        if (expectedMaxFetchDepth != actualMaxFetchDepth) {
            failCompare(
                "testMaxFetchDepth(): wrong getMaxFetchDepth() " + 
                    "after setMaxFetchDepth().",
                    expectedMaxFetchDepth, actualMaxFetchDepth);
        }
        cleanupPM();
        failOnError();
    }

    /** */
    public void testFetchSize() {
        int expectedFetchSize = 12;
        FetchPlan fp = getPM().getFetchPlan();
        fp.setFetchSize(expectedFetchSize);
        int actualFetchSize = fp.getFetchSize();
        if (expectedFetchSize != actualFetchSize) {
            failCompare(
                "testFetchSize(): wrong getFetchSize() " + 
                    "after setFetchSize().",
                    expectedFetchSize, actualFetchSize);
        }
        cleanupPM();
        failOnError();
    }

    /** */
    public void checkDefaultGroups() {
        Set expectedGroups = new HashSet();
        expectedGroups.add("default");
        FetchPlan fp = getPM().getFetchPlan();
        Collection groups = fp.getGroups();
        if (!setEquals(expectedGroups, groups)) {
            failCompare(
                "checkDefaultGroups(): wrong getGroups() " + 
                    "after getPersistenceManager().",
                    expectedGroups, groups);
        }
        cleanupPM();
    }

    /** */
    public void checkClearGroups() {
        Set expectedGroups = new HashSet();
        FetchPlan fp = getPM().getFetchPlan();
        fp.clearGroups();
        Collection groups = fp.getGroups();
        if (!setEquals(expectedGroups, groups)) {
            failCompare(
                "checkClearGroups(): wrong getGroups() " + 
                    "after clearGroups.",
                    expectedGroups, groups);
        }
        cleanupPM();
    }

    /** */
    public void checkSetGroup() {
        Set expectedGroups = new HashSet();
        expectedGroups.add("group1");
        FetchPlan fp = getPM().getFetchPlan();
        fp.setGroup("group1");
        Collection groups = fp.getGroups();
        if (!setEquals(expectedGroups, groups)) {
            failCompare(
                "checkSetGroup(): wrong getGroups() " + 
                    "after setGroup.",
                    expectedGroups, groups);
        }
        cleanupPM();
    }

    /** */
    public void checkAddGroup() {
        Set expectedGroups = new HashSet();
        expectedGroups.add("default");
        expectedGroups.add("group1");
        FetchPlan fp = getPM().getFetchPlan();
        fp.addGroup("group1");
        Collection groups = fp.getGroups();
        if (!setEquals(expectedGroups, groups)) {
            failCompare(
                "checkAddGroup(): wrong getGroups() " + 
                    "after addGroup.",
                    expectedGroups, groups);
        }
        cleanupPM();
    }

    /** */
    public void checkRemoveGroup() {
        Set expectedGroups = new HashSet();
        FetchPlan fp = getPM().getFetchPlan();
        Collection groups = fp.getGroups();
        fp.removeGroup("default");
        if (!setEquals(expectedGroups, groups)) {
            failCompare(
                "checkRemoveGroup(): wrong getGroups() " + 
                    "after removeGroup.",
                    expectedGroups, groups);
        }
        cleanupPM();
    }

    /** */
    public void checkSetGroupsCollection() {
        Set expectedGroups = new HashSet();
        expectedGroups.add("default");
        expectedGroups.add("group1");
        expectedGroups.add("group2");
        FetchPlan fp = getPM().getFetchPlan();
        fp.setGroups(expectedGroups);
        Collection groups = fp.getGroups();
        if (!setEquals(expectedGroups, groups)) {
            failCompare(
                "checkSetGroupsCollection(): wrong getGroups() " + 
                    "after SetGroups(Collection).",
                    expectedGroups, groups);
        }
        cleanupPM();
    }

    /** */
    public void checkSetGroupsArray() {
        Set expectedGroups = new HashSet();
        expectedGroups.add("default");
        expectedGroups.add("group1");
        expectedGroups.add("group2");
        FetchPlan fp = getPM().getFetchPlan();
        fp.setGroups(new String[] {"default", "group1", "group2"});
        Collection groups = fp.getGroups();
        if (!setEquals(expectedGroups, groups)) {
            failCompare(
                "checkSetGroupsArray(): wrong getGroups() " + 
                    "after setGroups(String[]).",
                    expectedGroups, groups);
        }
         cleanupPM();
    }

    /** */
    protected void checkGetDetachmentRoots() {
        Set expectedRoots = new HashSet();
        FetchPlan fp = getPM().getFetchPlan();
        Collection roots = fp.getDetachmentRoots();
        if (!setEquals(expectedRoots, roots)) {
            failCompare(
                "checkGetDetachmentRoots(): wrong getDetachmentRoots() " + 
                    "after getPersistenceManager().",
                    expectedRoots, roots);
        }
         cleanupPM();
    }

    /** */
    protected void checkSetDetachmentRoots() {
        PCPoint p = new PCPoint(10, 20);
        Set expectedRoots = new HashSet();
        expectedRoots.add(p);
        FetchPlan fp = getPM().getFetchPlan();
        fp.setDetachmentRoots(expectedRoots);
        Collection roots = fp.getDetachmentRoots();
        if (!setEquals(expectedRoots, roots)) {
            failCompare(
                "checkGetDetachmentRoots(): wrong getDetachmentRoots() " + 
                    "after setDetachmentRoots().",
                    expectedRoots, roots);
        }
         cleanupPM();
    }

    /** */
    private void checkSetDetachmentRootClasses() {
        Class[] expectedRootClasses = new Class[] {PCPoint.class};
        FetchPlan fp = getPM().getFetchPlan();
        fp.setDetachmentRootClasses(
                expectedRootClasses);
        Class[] rootClasses = fp.getDetachmentRootClasses();
        if (!Arrays.equals(expectedRootClasses, rootClasses)) {
            failCompare(
                "checkGetDetachmentRootClasses(): " + 
                    "wrong getDetachmentRootClasses() " + 
                    "after setDetachmentRootClasses().",
                    expectedRootClasses, rootClasses);
        }
         cleanupPM();
    }

}