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
 
package org.apache.jdo.tck.api.fetchgroup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.jdo.FetchGroup;

import javax.jdo.JDOException;
import org.apache.jdo.tck.JDO_Test;

import org.apache.jdo.tck.pc.company.Address;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.pc.company.Insurance;
import org.apache.jdo.tck.pc.company.MedicalInsurance;
import org.apache.jdo.tck.pc.company.PIEmployee;
import org.apache.jdo.tck.pc.company.PartTimeEmployee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.Project;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * This class is an abstract superclass for the fetch plan tests.
 * It contains methods useful for testing the behavior of the
 * fetch plan.
 */

public class FetchGroupTest extends JDO_Test {

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(FetchGroupTest.class);
    }

    /** All fetch groups in this PMF. */
    protected Set allPMFFetchGroups;

    /**
     * Employee.java:
    private Date             hiredate;
    private double           weeklyhours;
    private DentalInsurance  dentalInsurance;
    private MedicalInsurance medicalInsurance;
    private Department       department;
    private Department       fundingDept;
    private Employee         manager;
    private Employee         mentor;
    private Employee         protege;
    private Employee         hradvisor;
    private transient Set reviewedProjects = new HashSet(); // element-type is Project
    private transient Set projects = new HashSet();         // element-type is Project
    private transient Set team = new HashSet();             // element-type is Employee
    private transient Set hradvisees = new HashSet();       // element-type is Employee
     * Person.java:
    private long    personid;
    private String  firstname;
    private String  lastname;
    private String  middlename;
    private Date    birthdate;
    private Address address;
    private Map phoneNumbers = new HashMap();
     */
    protected String[] basicMembers = new String[]{"hiredate", "weeklyhours", 
        "personid", "firstname", "lastname", "middlename", "birthdate"};
    /** In org/apache/jdo/tck/pc/package.jdo, middlename is not in DFG */
    protected String[] defaultMembers = new String[]{"hiredate", "weeklyhours", 
        "personid", "firstname", "lastname","birthdate"};
    protected String[] allMembers = new String[]{"hiredate", "weeklyhours", 
        "dentalInsurance", "medicalInsurance", "department", "fundingDept",
        "manager", "mentor", "protege", "hradvisor",
        "reviewedProjects", "projects", "team", "hradvisees",
        "personid", "firstname", "lastname", "middlename", "birthdate",
        "address", "phoneNumbers"};
    /** Address address is of type Address and is a relationship */
    protected String[] relationshipMembers = new String[]{
        "dentalInsurance", "medicalInsurance", "department", "fundingDept",
        "manager", "mentor", "protege", "hradvisor",
        "reviewedProjects", "projects", "team", "hradvisees", "address"};
    /** Map phoneNumbers is not a relationship but is multivalued */
    protected String[] multivaluedMembers = new String[]{
        "reviewedProjects", "projects", "team", "hradvisees", "phoneNumbers"};
    protected String[] allButMultivaluedMembers = new String[]
        {"hiredate", "weeklyhours", 
        "dentalInsurance", "medicalInsurance", "department", "fundingDept",
        "manager", "mentor", "protege", "hradvisor",
        "personid", "firstname", "lastname", "middlename", "birthdate",
        "address"};

    /** 
     * SetUp for test.
     */
    public void localSetUp() {
        getPM(); // initialize pmf and pm fields
    }

    public void testPMFGetFetchGroup() {
        allPMFFetchGroups = pmf.getFetchGroups();
        Map unscopedFetchGroupMap = new HashMap();    
        unscopedFetchGroupMap.put("Address+default", 
                pmf.getFetchGroup(Address.class, "default"));
        unscopedFetchGroupMap.put("Company+default", 
                pmf.getFetchGroup(Company.class, "default"));
        unscopedFetchGroupMap.put("DentalInsurance+default", 
                pmf.getFetchGroup(DentalInsurance.class, "default"));
        unscopedFetchGroupMap.put("Department+default", 
                pmf.getFetchGroup(Department.class, "default"));
        unscopedFetchGroupMap.put("Employee+default", 
                pmf.getFetchGroup(Employee.class, "default"));
        unscopedFetchGroupMap.put("FullTimeEmployee+default", 
                pmf.getFetchGroup(FullTimeEmployee.class, "default"));
        unscopedFetchGroupMap.put("Insurance+default", 
                pmf.getFetchGroup(Insurance.class, "default"));
        unscopedFetchGroupMap.put("MedicalInsurance+default", 
                pmf.getFetchGroup(MedicalInsurance.class, "default"));
        unscopedFetchGroupMap.put("PartTimeEmployee+default", 
                pmf.getFetchGroup(PartTimeEmployee.class, "default"));
        unscopedFetchGroupMap.put("Person+default", 
                pmf.getFetchGroup(Person.class, "default"));
        unscopedFetchGroupMap.put("Project+default", 
                pmf.getFetchGroup(Project.class, "default"));
    }

    public void testPMFGetFetchGroupHashCode() {
        FetchGroup scoped = pm.getFetchGroup(Address.class, "default");
        FetchGroup unscoped = pmf.getFetchGroup(Address.class, "default");
        int actual = scoped.hashCode();
        int expected = unscoped.hashCode();
        assertEquals("Scoped hash code does not equal unscoped hash code;"
                    + "Expected: " + expected + " actual: " + actual,
                    actual, expected);
    }

    public void testPMGetFetchGroupIdentical() {
        FetchGroup scoped = pm.getFetchGroup(Address.class, "default");
        FetchGroup identical = pm.getFetchGroup(Address.class, "default");
        assertSame("Modifiable FetchGroup is not identical to modifiable FetchGroup;"
                    + "FetchGroup: " + printFetchGroup(scoped)
                    + " identical: " + printFetchGroup(identical),
                    scoped, identical);
    }

    public void testPMGetFetchGroupUnmodifiableNotIdentical() {
        FetchGroup scoped = pm.getFetchGroup(Address.class, "default");
        scoped.setUnmodifiable();
        FetchGroup modifiable = pm.getFetchGroup(Address.class, "default");
        assertNotSame("Unmodifiable FetchGroup is identical to modifiable FetchGroup;"
                    + "\nunmodifiable: " + printFetchGroup(scoped)
                    + "\n  modifiable: " + printFetchGroup(modifiable),
                    scoped, modifiable);
    }

    public void testPMFGetFetchGroupNotIdentical() {
        FetchGroup first = pmf.getFetchGroup(Address.class, "default");
        FetchGroup second = pmf.getFetchGroup(Address.class, "default");
        assertNotSame("First FetchGroup is identical to second FetchGroup;"
                    + "\n first: " + printFetchGroup(first)
                    + "\nsecond: " + printFetchGroup(second),
                    first, second);
    }

    public void testPMGetFetchGroupEquals() {
        FetchGroup unmodifiable = pm.getFetchGroup(Address.class, "default");
        unmodifiable.setUnmodifiable();
        FetchGroup modifiable = pm.getFetchGroup(Address.class, "default");
        assertEquals("Unmodifiable FetchGroup is not equal to modifiable FetchGroup;"
                    + "\nunmodifiable: " + printFetchGroup(unmodifiable)
                    + "\n  modifiable: " + printFetchGroup(modifiable),
                    unmodifiable, modifiable);
    }

    public void testPMModifiable() {
        FetchGroup scoped = pm.getFetchGroup(Address.class, "default");
        assertFalse("Scoped FetchGroup should be modifiable initially, but is unmodifiable.",
                scoped.isUnmodifiable());
        scoped.setUnmodifiable();
        assertTrue("Scoped FetchGroup should be unmodifiable after setUnmodifiable, but is modifiable.",
                scoped.isUnmodifiable());
    }

    public void testPMFModifiable() {
        FetchGroup scoped = pmf.getFetchGroup(Address.class, "default");
        assertFalse("Unscoped FetchGroup should be modifiable initially, but is unmodifiable.",
                scoped.isUnmodifiable());
        scoped.setUnmodifiable();
        assertTrue("Unscoped FetchGroup should be unmodifiable after setUnmodifiable, but is modifiable.",
                scoped.isUnmodifiable());
    }

    public void testCategoriesClass() {
        checkAddCategory(Employee.class, FetchGroup.ALL, allMembers);
        checkAddCategory(Employee.class, FetchGroup.BASIC, basicMembers);
        checkAddCategory(Employee.class, FetchGroup.DEFAULT, defaultMembers);
        checkAddCategory(Employee.class, FetchGroup.RELATIONSHIP, relationshipMembers);
        checkAddCategory(Employee.class, FetchGroup.MULTIVALUED, multivaluedMembers);
        failOnError();
    }

    public void testCategoriesInterface() {
        checkAddCategory(PIEmployee.class, FetchGroup.ALL, allMembers);
        checkAddCategory(PIEmployee.class, FetchGroup.BASIC, basicMembers);
        checkAddCategory(PIEmployee.class, FetchGroup.DEFAULT, defaultMembers);
        checkAddCategory(PIEmployee.class, FetchGroup.RELATIONSHIP, relationshipMembers);
        checkAddCategory(PIEmployee.class, FetchGroup.MULTIVALUED, multivaluedMembers);
        failOnError();
    }

    public void testRemoveCategory() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testRemoveCategory");
        Set expectedSet = new HashSet();
        expectedSet.addAll(Arrays.asList(allButMultivaluedMembers));
        Set members = fg.getMembers();
        fg.addCategory(FetchGroup.ALL);
        fg.removeCategory(FetchGroup.MULTIVALUED);
        members = fg.getMembers();
        assertEquals("FetchGroup.addCategory(all).removeCategory(multivalued)"
                + " should contain all but multivalued members.\n",
                expectedSet, members);        
    }

    public void testAddMember() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testAddMember");
        for (int i = 0; i < allMembers.length; ++i) {
            String member = allMembers[i];
            fg.addMember(member);
            Set members = fg.getMembers();
            assertTrue("FetchGroup should contain " + member + " but does not.\n"
                    + printFetchGroup(fg),
                    members.contains(member));
            assertEquals("FetchGroup should contain " + i+1
                    + "members, but does not; ", 
                    i+1, members.size());
        }
    }

    public void testAddMembers() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testAddMembers");
        fg.addMembers(multivaluedMembers);
        fg.addMembers(allButMultivaluedMembers);
        Set members = fg.getMembers();
        Set expectedSet = new HashSet();
        expectedSet.addAll(Arrays.asList(allMembers));
        assertEquals("FetchGroup should contain all members.\n",
                expectedSet, members);
    }

    public void testRemoveMembers() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testRemoveMembers");
        fg.addMembers(allMembers);
        fg.removeMembers(relationshipMembers);
        Set members = fg.getMembers();
        Set expectedSet = new HashSet();
        expectedSet.addAll(Arrays.asList(basicMembers));
        expectedSet.add("phoneNumbers");
        assertEquals("FetchGroup should contain basic members " +
                "plus address plus phoneNumbers.\n",
                expectedSet, members);
        fg.removeMembers(basicMembers);
        members = fg.getMembers();
        expectedSet = new HashSet();
        expectedSet.add("phoneNumbers");
        assertEquals("FetchGroup should contain address plus phoneNumbers.\n",
                expectedSet, members);
    }

    public void testRemoveMember() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testRemoveMember");
        fg.addCategory(FetchGroup.ALL);
        for (int i = allMembers.length - 1; i >= 0; --i) {
            String member = allMembers[i];
            fg.removeMember(member);
            Set members = fg.getMembers();
            assertFalse("FetchGroup should not contain " + member + " but does.\n"
                    + printFetchGroup(fg),
                    members.contains(member));
            assertEquals("FetchGroup should contain " + i 
                    + "members, but does not; ", 
                    i, members.size());
        }
    }

    public void testRecursionDepth() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testRecursionDepth");
        fg.addMember("manager");
        int depth = fg.getRecursionDepth("manager");
        assertEquals("Initial recursion depth for manager should be 1." +
                printFetchGroup(fg),
                1, depth);
        fg.setRecursionDepth("manager", 64);
        assertEquals("Recursion depth for manager should be 64." +
                printFetchGroup(fg),
                64, fg.getRecursionDepth("manager"));
    }

    public void testPostLoad() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testPostLoad");
        assertFalse("New FetchGroup should have post-load false; " 
                + printFetchGroup(fg),
                fg.getPostLoad());
        fg.setPostLoad(true);
        assertTrue("After setPostLoad(true) FetchGroup should have post-load true; " 
                + printFetchGroup(fg),
                fg.getPostLoad());
        fg.setPostLoad(false);
        assertFalse("After setPostLoad, FetchGroup should have post-load false; " 
                + printFetchGroup(fg),
                fg.getPostLoad());
    }

    public void testUnmodifiableSetPostLoad() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testUnmodifiableSetPostLoad");
        fg.setUnmodifiable();
        try {
            fg.setPostLoad(true);
            fail("Unmodifiable FetchGroup should throw on setPostLoad.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testUnmodifiableAddMember() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testUnmodifiableAddMember");
        fg.setUnmodifiable();
        try {
            fg.addMember("hiredate");
            fail("Unmodifiable FetchGroup should throw on addMember.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testUnmodifiableAddMembers() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testUnmodifiableAddMembers");
        fg.setUnmodifiable();
        try {
            fg.addMembers(allMembers);
            fail("Unmodifiable FetchGroup should throw on addMembers.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testUnmodifiableRemoveMember() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testUnmodifiableRemoveMember");
        fg.addMembers(allMembers);
        fg.setUnmodifiable();
        try {
            fg.removeMember("hiredate");
            fail("Unmodifiable FetchGroup should throw on removeMember.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testUnmodifiableRemoveMembers() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testUnmodifiableRemoveMembers");
        fg.addMembers(allMembers);
        fg.setUnmodifiable();
        try {
            fg.removeMembers(allMembers);
            fail("Unmodifiable FetchGroup should throw on removeMembers.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testUnmodifiableAddCategory() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testUnmodifiableAddCategory");
        fg.setUnmodifiable();
        try {
            fg.addCategory(FetchGroup.ALL);
            fail("Unmodifiable FetchGroup should throw on addCategory.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testUnmodifiableRemoveCategory() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testUnmodifiableRemoveCategory");
        fg.addCategory(FetchGroup.ALL);
        fg.setUnmodifiable();
        try {
            fg.removeCategory(FetchGroup.ALL);
            fail("Unmodifiable FetchGroup should throw on removeCategory.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testUnmodifiableSetRecursionDepth() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testUnmodifiableSetRecursionDepth");
        fg.setUnmodifiable();
        try {
            fg.setRecursionDepth("hiredate", 64);
            fail("Unmodifiable FetchGroup should throw on setRecursionDepth.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testUnmodifiableSetUnmodifiable() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testUnmodifiableSetUnmodifiable");
        fg.setUnmodifiable();
        fg.setUnmodifiable(); // should be ok
    }

    public void testPMGetFetchGroupClassNotPersistenceCapable() {
        try {
            FetchGroup fg = pm.getFetchGroup(Integer.class, "testPMGetFetchGroupClassNotPersistenceCapable");
            fail("getFetchGroup should throw on nonPersistenceCapable class.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testPMGetFetchGroupInterfaceNotPersistenceCapable() {
        try {
            FetchGroup fg = pm.getFetchGroup(IEmployee.class, "testPMGetFetchGroupInterfaceNotPersistenceCapable");
            fail("getFetchGroup should throw on nonPersistenceCapable interface.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testPMFGetFetchGroupClassNotPersistenceCapable() {
        try {
            FetchGroup fg = pmf.getFetchGroup(Integer.class, "testtestPMFGetFetchGroupClassNotPersistenceCapableGetFetchGroupClassNotPersistenceCapable");
            fail("getFetchGroup should throw on nonPersistenceCapable class.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testPMFGetFetchGroupInterfaceNotPersistenceCapable() {
        try {
            FetchGroup fg = pmf.getFetchGroup(IEmployee.class, "testPMFGetFetchGroupInterfaceNotPersistenceCapable");
            fail("getFetchGroup should throw on nonPersistenceCapable interface.");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testAddMemberNotAMember() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testAddMemberNotAMember");
        try {
            fg.addMember("NotAMember");
            fail("FetchGroup should throw on addMember(NotAMember).");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testAddMembersNotAMember() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testAddMembersNotAMember");
        try {
            fg.addMembers(new String[]{"NotAMember"});
            fail("FetchGroup should throw on addMembers(NotAMember).");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testRemoveMemberNotAMember() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testRemoveMemberNotAMember");
        fg.addCategory(FetchGroup.ALL);
        try {
            fg.removeMember("NotAMember");
            fail("FetchGroup should throw on removeMember(NotAMember).");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testRemoveMembersNotAMember() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testRemoveMembersNotAMember");
        fg.addCategory(FetchGroup.ALL);
        try {
            fg.removeMembers(new String[]{"NotAMember"});
            fail("FetchGroup should throw on removeMembers(NotAMember).");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    public void testSetRecursionDepthNotAMember() {
        FetchGroup fg = pm.getFetchGroup(Employee.class, "testSetRecursionDepthNotAMember");
        try {
            fg.setRecursionDepth("NotAMember", 64);
            fail("FetchGroup should throw on setRecursionDepth(NotAMember).");
        } catch(JDOException ex) {
            // good catch!
        }
    }

    private void checkAddCategory(Class cls, String category, String[] expected) {
        FetchGroup fg = pm.getFetchGroup(cls, "test" + count() + category);
        Set expectedSet = new HashSet();
        expectedSet.addAll(Arrays.asList(expected));
        Set members = fg.getMembers();
        assertTrue("New FetchGroup should have no members; "
                + printFetchGroup(fg),
                members.isEmpty());
        fg.addCategory(category);
        members = fg.getMembers();
        if (!members.equals(expectedSet)) {
            appendMessage("FetchGroup(" + cls.getName() 
                    + ".addCategory(" + category + ") should contain\n"
                + expectedSet + " but contains\n" + members);
        }
    }

    private String printFetchGroup(FetchGroup fg) {
        StringBuffer sb = new StringBuffer("FetchGroup (");
        sb.append(fg.getType().isInterface()?"interface: ":"class: ");
        sb.append(fg.getType().getName());
        sb.append("; name: ");
        sb.append(fg.getName());
        sb.append(fg.isUnmodifiable()?"; unmodifiable":"; modifiable");
        Set members = fg.getMembers();
        Iterator it = members.iterator();
        if (it.hasNext()) {
            sb.append("; members: ");
            String member = (String)it.next();
            formatMember(sb, fg, member);
        }
        while (it.hasNext()) {
            sb.append(", ");
            String member = (String)it.next();
            formatMember(sb, fg, member);
        }
        sb.append(")");
        return sb.toString();
    }

    protected void formatMember(StringBuffer sb, FetchGroup fg, String member) {
        sb.append(member);
        sb.append("[");
        sb.append(fg.getRecursionDepth(member));
        sb.append("]");
    }

    /** Counter */
    protected int counter = 0;
    protected String count() {
        return String.valueOf(counter++);
    }
}
