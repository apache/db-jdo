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

package org.apache.jdo.tck.pc.companyAnnotatedPC;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import org.apache.jdo.tck.pc.company.ICompany;
import org.apache.jdo.tck.pc.company.IDepartment;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.pc.company.IMeetingRoom;
import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

/** This class represents a department within a company. */
@PersistenceCapable(table = "departments")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME, column = "DISCRIMINATOR")
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "DATASTORE_IDENTITY")
public class PCDSDepartment
    implements IDepartment,
        Serializable,
        Comparable<IDepartment>,
        Comparator<IDepartment>,
        DeepEquality {

  private static final long serialVersionUID = 1L;

  public static final int RECOMMENDED_NO_OF_EMPS = 2;

  @NotPersistent() private long _deptid;
  @NotPersistent() private String _name;
  @NotPersistent() private PCDSCompany _company;
  @NotPersistent() private PCDSEmployee _employeeOfTheMonth;
  @NotPersistent() private transient Set<IEmployee> _employees = new HashSet<>();
  @NotPersistent() private transient Set<IEmployee> _fundedEmps = new HashSet<>();

  /**
   * This is the JDO-required no-args constructor. The TCK relies on this constructor for testing
   * PersistenceManager.newInstance(PCClass).
   */
  public PCDSDepartment() {}

  /**
   * Construct a <code>Department</code> instance.
   *
   * @param deptid The department id.
   * @param name The name of the department.
   */
  public PCDSDepartment(long deptid, String name) {
    this._deptid = deptid;
    this._name = name;
  }

  /**
   * Construct a <code>Department</code> instance.
   *
   * @param deptid The department id.
   * @param name The name of the department.
   * @param company The company that the department is associated with.
   */
  public PCDSDepartment(long deptid, String name, ICompany company) {
    this._deptid = deptid;
    this._name = name;
    this._company = (PCDSCompany) company;
  }

  /**
   * Construct a <code>Department</code> instance.
   *
   * @param deptid The department id.
   * @param name The name of the department.
   * @param company The company that the department is associated with.
   * @param employeeOfTheMonth The employee of the month the department is associated with.
   */
  public PCDSDepartment(long deptid, String name, ICompany company, IEmployee employeeOfTheMonth) {
    this._deptid = deptid;
    this._name = name;
    this._company = (PCDSCompany) company;
    this._employeeOfTheMonth = (PCDSEmployee) employeeOfTheMonth;
  }

  /**
   * Set the id associated with this object.
   *
   * @param id the id.
   */
  public void setDeptid(long id) {
    this._deptid = id;
  }

  /**
   * Get the department id.
   *
   * @return The department id.
   */
  @Column(name = "ID")
  public long getDeptid() {
    return _deptid;
  }

  /**
   * Get the name of the department.
   *
   * @return The name of the department.
   */
  @Column(name = "NAME")
  public String getName() {
    return _name;
  }

  /**
   * Set the name of the department.
   *
   * @param name The name to set for the department.
   */
  public void setName(String name) {
    this._name = name;
  }

  /**
   * Get the company associated with the department.
   *
   * @return The company.
   */
  @Persistent(types = org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSCompany.class)
  @Column(name = "COMPANYID")
  public ICompany getCompany() {
    return _company;
  }

  /**
   * Set the company for the department.
   *
   * @param company The company to associate with the department.
   */
  public void setCompany(ICompany company) {
    this._company = (PCDSCompany) company;
  }

  /**
   * Get the employee of the month associated with the department.
   *
   * @return The employee of the month.
   */
  @Persistent(types = org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSEmployee.class)
  @Column(name = "EMP_OF_THE_MONTH")
  public IEmployee getEmployeeOfTheMonth() {
    return _employeeOfTheMonth;
  }

  /**
   * Set the employee of the month for the department.
   *
   * @param employeeOfTheMonth The employee of the month to associate with the department.
   */
  public void setEmployeeOfTheMonth(IEmployee employeeOfTheMonth) {
    this._employeeOfTheMonth = (PCDSEmployee) employeeOfTheMonth;
  }

  /**
   * Get the employees in the department.
   *
   * @return The set of employees in the department.
   */
  @Persistent(mappedBy = "department")
  @Element(types = org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSEmployee.class)
  public Set<IEmployee> getEmployees() {
    return _employees;
  }

  /**
   * Add an employee to the department.
   *
   * @param emp The employee to add to the department.
   */
  public void addEmployee(PCDSEmployee emp) {
    _employees.add(emp);
  }

  /**
   * Remove an employee from the department.
   *
   * @param emp The employee to remove from the department.
   */
  public void removeEmployee(PCDSEmployee emp) {
    _employees.remove(emp);
  }

  /**
   * Set the employees to be in this department.
   *
   * @param employees The set of employees for this department.
   */
  public void setEmployees(Set<IEmployee> employees) {
    // workaround: create a new HashSet, because fostore does not
    // support LinkedHashSet
    this._employees = (employees != null) ? new HashSet<>(employees) : null;
  }

  /**
   * Get the funded employees in the department.
   *
   * @return The set of funded employees in the department.
   */
  @Element(types = org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSEmployee.class)
  @Persistent(mappedBy = "fundingDept")
  public Set<IEmployee> getFundedEmps() {
    return _fundedEmps;
  }

  /**
   * Add an employee to the collection of funded employees of this department.
   *
   * @param emp The employee to add to the department.
   */
  public void addFundedEmp(PCDSEmployee emp) {
    _fundedEmps.add(emp);
  }

  /**
   * Remove an employee from collection of funded employees of this department.
   *
   * @param emp The employee to remove from the department.
   */
  public void removeFundedEmp(PCDSEmployee emp) {
    _fundedEmps.remove(emp);
  }

  /**
   * Set the funded employees to be in this department.
   *
   * @param employees The set of funded employees for this department.
   */
  public void setFundedEmps(Set<IEmployee> employees) {
    // workaround: create a new HashSet, because fostore does not
    // support LinkedHashSet
    this._fundedEmps = (employees != null) ? new HashSet<>(employees) : null;
  }

  @Override
  public List<IMeetingRoom> getMeetingRooms() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setMeetingRooms(List<IMeetingRoom> rooms) {
    // TODO Auto-generated method stub

  }

  /**
   * Serialization support: initialize transient fields.
   *
   * @param in stream
   * @throws IOException error during reading
   * @throws ClassNotFoundException class could not be found
   */
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    _employees = new HashSet<>();
    _fundedEmps = new HashSet<>();
  }

  /**
   * Returns <code>true</code> if all the fields of this instance are deep equal to the coresponding
   * fields of the other PCDSDepartment.
   *
   * @param other the object with which to compare.
   * @param helper EqualityHelper to keep track of instances that have already been processed.
   * @return <code>true</code> if all the fields are deep equal; <code>false</code> otherwise.
   * @throws ClassCastException if the specified instances' type prevents it from being compared to
   *     this instance.
   */
  public boolean deepCompareFields(Object other, EqualityHelper helper) {
    PCDSDepartment otherDept = (PCDSDepartment) other;
    String where = "FCDepartment<" + _deptid + ">";
    return helper.equals(_deptid, otherDept.getDeptid(), where + ".deptid")
        & helper.equals(_name, otherDept.getName(), where + ".name")
        & helper.deepEquals(_company, otherDept.getCompany(), where + ".company")
        & helper.deepEquals(
            _employeeOfTheMonth, otherDept.getEmployeeOfTheMonth(), where + ".employeeOfTheMonth")
        & helper.deepEquals(_employees, otherDept.getEmployees(), where + ".employees")
        & helper.deepEquals(_fundedEmps, otherDept.getFundedEmps(), where + ".fundedEmps");
  }

  /**
   * Returns a String representation of a <code>PCDSDepartment</code> object.
   *
   * @return a String representation of a <code>PCDSDepartment</code> object.
   */
  public String toString() {
    return "FCDepartment(" + getFieldRepr() + ")";
  }

  /**
   * Returns a String representation of the non-relationship fields.
   *
   * @return a String representation of the non-relationship fields.
   */
  protected String getFieldRepr() {
    StringBuilder rc = new StringBuilder();
    rc.append(_deptid);
    rc.append(", name ").append(_name);
    return rc.toString();
  }

  /**
   * Compares this object with the specified Department object for order. Returns a negative
   * integer, zero, or a positive integer as this object is less than, equal to, or greater than the
   * specified object.
   *
   * @param other The Department object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified Department object.
   */
  public int compareTo(IDepartment other) {
    return compare(this, other);
  }

  /**
   * Compares its two IDepartment arguments for order. Returns a negative integer, zero, or a
   * positive integer as the first argument is less than, equal to, or greater than the second.
   *
   * @param o1 the first IDepartment object to be compared.
   * @param o2 the second IDepartment object to be compared.
   * @return a negative integer, zero, or a positive integer as the first object is less than, equal
   *     to, or greater than the second object.
   */
  public int compare(IDepartment o1, IDepartment o2) {
    return EqualityHelper.compare(o1.getDeptid(), o2.getDeptid());
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
   *     otherwise.
   */
  public boolean equals(Object obj) {
    if (obj instanceof PCDSDepartment) {
      return compareTo((PCDSDepartment) obj) == 0;
    }
    return false;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for this object.
   */
  public int hashCode() {
    return (int) _deptid;
  }

  /** The application identity class associated with the <code>Department</code> class. */
  public static class Oid implements Serializable, Comparable<Oid> {

    private static final long serialVersionUID = 1L;

    /**
     * This field represents the application identifier field for the <code>Department</code> class.
     * It must match in name and type with the field in the <code>Department</code> class.
     */
    public long deptid;

    /** The required public, no-arg constructor. */
    public Oid() {}

    /**
     * A constructor to initialize the identifier field.
     *
     * @param deptid the deptid of the Department.
     */
    public Oid(long deptid) {
      this.deptid = deptid;
    }

    public Oid(String s) {
      deptid = Long.parseLong(justTheId(s));
    }

    public String toString() {
      return this.getClass().getName() + ": " + deptid;
    }

    /** */
    public boolean equals(java.lang.Object obj) {
      if (obj == null || !this.getClass().equals(obj.getClass())) return (false);
      Oid o = (Oid) obj;
      if (this.deptid != o.deptid) return (false);
      return (true);
    }

    /** */
    public int hashCode() {
      return ((int) deptid);
    }

    protected static String justTheId(String str) {
      return str.substring(str.indexOf(':') + 1);
    }

    /** */
    public int compareTo(Oid obj) {
      return Long.compare(deptid, obj.deptid);
    }
  }
}
