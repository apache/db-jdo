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

package org.apache.jdo.tck.pc.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javax.jdo.annotations.PersistenceCapable;
import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

/** This class represents a project, a budgeted task with one or more employees working on it. */
@PersistenceCapable
public class Project
    implements IProject, Serializable, Comparable<IProject>, Comparator<IProject>, DeepEquality {

  private static final long serialVersionUID = 1L;

  private long projid;
  private String name;
  private BigDecimal budget;
  private transient Set<IEmployee> reviewers = new HashSet<>();
  private transient Set<IEmployee> members = new HashSet<>();

  /**
   * This is the JDO-required no-args constructor. The TCK relies on this constructor for testing
   * PersistenceManager.newInstance(PCClass).
   */
  public Project() {}

  /**
   * Initialize a project.
   *
   * @param projid The project identifier.
   * @param name The name of the project.
   * @param budget The budget for the project.
   */
  public Project(long projid, String name, BigDecimal budget) {
    this.projid = projid;
    this.name = name;
    this.budget = budget;
  }

  /**
   * Set the id associated with this object.
   *
   * @param id the id.
   */
  public void setProjid(long id) {
    if (this.projid != 0) throw new IllegalStateException("Id is already set.");
    this.projid = id;
  }

  /**
   * Get the project ID.
   *
   * @return The project ID.
   */
  public long getProjid() {
    return projid;
  }

  /**
   * Get the name of the project.
   *
   * @return The name of the project.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of the project.
   *
   * @param name The name of the project.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the project's budget.
   *
   * @return The project's budget.
   */
  public BigDecimal getBudget() {
    return budget;
  }

  /**
   * Set the project's budget.
   *
   * @param budget The project's budget.
   */
  public void setBudget(BigDecimal budget) {
    this.budget = budget;
  }

  /** Get the reviewers associated with this project. */
  public Set<IEmployee> getReviewers() {
    return Collections.unmodifiableSet(reviewers);
  }

  /**
   * Add a reviewer to the project.
   *
   * @param emp The employee to add as a reviewer.
   */
  public void addReviewer(IEmployee emp) {
    reviewers.add(emp);
  }

  /**
   * Remove a reviewer from the project.
   *
   * @param emp The employee to remove as a reviewer of this project.
   */
  public void removeReviewer(IEmployee emp) {
    reviewers.remove(emp);
  }

  /**
   * Set the reviewers associated with this project.
   *
   * @param reviewers The set of reviewers to associate with this project.
   */
  public void setReviewers(Set<IEmployee> reviewers) {
    // workaround: create a new HashSet, because fostore does not
    // support LinkedHashSet
    this.reviewers = (reviewers != null) ? new HashSet<>(reviewers) : null;
  }

  /**
   * Get the project members.
   *
   * @return The members of the project is returned as an unmodifiable set of <code>Employee</code>
   *     s.
   */
  public Set<IEmployee> getMembers() {
    return Collections.unmodifiableSet(members);
  }

  /**
   * Add a new member to the project.
   *
   * @param emp The employee to add to the project.
   */
  public void addMember(IEmployee emp) {
    members.add(emp);
  }

  /**
   * Remove a member from the project.
   *
   * @param emp The employee to remove from the project.
   */
  public void removeMember(IEmployee emp) {
    members.remove(emp);
  }

  /**
   * Set the members of the project.
   *
   * @param employees The set of employees to be the members of this project.
   */
  public void setMembers(Set<IEmployee> employees) {
    // workaround: create a new HashSet, because fostore does not
    // support LinkedHashSet
    this.members = (employees != null) ? new HashSet<>(employees) : null;
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
    reviewers = new HashSet<>();
    members = new HashSet<>();
  }

  /**
   * Returns a String representation of a <code>Project</code> object.
   *
   * @return a String representation of a <code>Project</code> object.
   */
  public String toString() {
    return "Project(" + getFieldRepr() + ")";
  }

  /**
   * Returns a String representation of the non-relationship fields.
   *
   * @return a String representation of the non-relationship fields.
   */
  protected String getFieldRepr() {
    StringBuilder rc = new StringBuilder();
    rc.append(projid);
    rc.append(", name ").append(name);
    rc.append(", budget ").append(budget);
    return rc.toString();
  }

  /**
   * Returns <code>true</code> if all the fields of this instance are deep equal to the coresponding
   * fields of the specified Person.
   *
   * @param other the object with which to compare.
   * @param helper EqualityHelper to keep track of instances that have already been processed.
   * @return <code>true</code> if all the fields are deep equal; <code>false</code> otherwise.
   * @throws ClassCastException if the specified instances' type prevents it from being compared to
   *     this instance.
   */
  public boolean deepCompareFields(Object other, EqualityHelper helper) {
    IProject otherProject = (IProject) other;
    String where = "Project<" + projid + ">";
    return helper.equals(projid, otherProject.getProjid(), where + ".projid")
        & helper.equals(name, otherProject.getName(), where + ".name")
        & helper.equals(budget, otherProject.getBudget(), where + ".budget")
        & helper.deepEquals(reviewers, otherProject.getReviewers(), where + ".reviewers")
        & helper.deepEquals(members, otherProject.getMembers(), where + ".members");
  }

  /**
   * Compares this object with the specified Project object for order. Returns a negative integer,
   * zero, or a positive integer as this object is less than, equal to, or greater than the
   * specified object.
   *
   * @param other The Project object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified Project object.
   */
  public int compareTo(IProject other) {
    return compare(this, other);
  }

  /**
   * Compares its two IProject arguments for order. Returns a negative integer, zero, or a positive
   * integer as the first argument is less than, equal to, or greater than the second.
   *
   * @param o1 the first IProject object to be compared.
   * @param o2 the second IProject object to be compared.
   * @return a negative integer, zero, or a positive integer as the first object is less than, equal
   *     to, or greater than the second object.
   */
  public int compare(IProject o1, IProject o2) {
    return EqualityHelper.compare(o1.getProjid(), o2.getProjid());
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
   *     otherwise.
   */
  public boolean equals(Object obj) {
    if (obj instanceof IProject) {
      return compareTo((IProject) obj) == 0;
    }
    return false;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for this object.
   */
  public int hashCode() {
    return (int) projid;
  }

  /**
   * This class is used to represent the application identity for the <code>Project</code> class.
   */
  public static class Oid implements Serializable, Comparable<Oid> {

    private static final long serialVersionUID = 1L;

    /**
     * This field represents the identifier for the <code>Project</code> class. It must match a
     * field in the <code>Project</code> class in both name and type.
     */
    public long projid;

    /** The required public no-arg constructor. */
    public Oid() {}

    /**
     * Initialize the application identifier with a project ID.
     *
     * @param projid The id of the project.
     */
    public Oid(long projid) {
      this.projid = projid;
    }

    public Oid(String s) {
      projid = Long.parseLong(justTheId(s));
    }

    public String toString() {
      return this.getClass().getName() + ": " + projid;
    }

    /** */
    public boolean equals(java.lang.Object obj) {
      if (obj == null || !this.getClass().equals(obj.getClass())) return (false);
      Oid o = (Oid) obj;
      if (this.projid != o.projid) return (false);
      return (true);
    }

    /** */
    public int hashCode() {
      return ((int) projid);
    }

    protected static String justTheId(String str) {
      return str.substring(str.indexOf(':') + 1);
    }

    /** */
    public int compareTo(Oid obj) {
      return Long.compare(projid, obj.projid);
    }
  }
}
