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

/*
 * FetchGroup.java
 *
 */
 
package javax.jdo;

import java.util.Set;

/**
 * FetchGroup represents a named fetch group for a specific class or 
 * interface. A fetch group instance identifies the name of the class or 
 * interface, the list of members (fields or properties) to be fetched when 
 * the fetch group is active, and the recursion depth for each member.
 * <p> 
 * Fetch groups are updated using methods on this interface. An instance of
 * a class implementing this interface can be obtained from
 * {@link PersistenceManager#getFetchGroup} or
 * {@link PersistenceManagerFactory#getFetchGroup}.
 * <p> 
 * A FetchGroup can be unscoped or can be in one of two scopes (the 
 * {@link PersistenceManager} or the {@link PersistenceManagerFactory} scope). 
 * Unscoped FetchGroups do not affect any behavior.
 * A FetchGroup in PersistenceManager scope hides the corresponding 
 * FetchGroup in the PersistenceManagerFactory scope.
 * <ul><li>When a FetchGroup is obtained via 
 * {@link PersistenceManager#getFetchGroup},
 * it is immediately in scope of its <code>PersistenceManager</code>.
 * Subsequent modifications of the FetchGroup
 * immediately affect <code>FetchPlan</code>s that contain the
 * <code>FetchGroup</code>.
 * </li><li>When a FetchGroup is obtained via 
 * {@link PersistenceManagerFactory#getFetchGroup}, it is unscoped.
 * </li><li>When a FetchGroup is added to the set of active FetchGroups via
 * {@link PersistenceManagerFactory#addFetchGroups}, it is put in scope of the
 * <code>PersistenceManagerFactory</code>. 
 * </li><li>When a FetchGroup is removed from the set of active FetchGroups via
 * {@link PersistenceManagerFactory#removeFetchGroups}, 
 * {@link PersistenceManagerFactory#removeAllFetchGroups}, or replaced via
 * {@link PersistenceManagerFactory#addFetchGroups}, it is unscoped.
 * </li></ul>
 * @version 2.2
 * @since 2.2
 */
public interface FetchGroup {

    /**
     * For use with {@link #addCategory} and {@link #removeCategory} calls.
     * This category includes members defined in the default fetch group
     * in xml or annotations. Redefining the default fetch group via the API
     * does not affect the members defined by this category.
     * <p>
     * Using this category also sets the fetch-depth for the members in the
     * default fetch group.</p>
     * @since 2.2
     */
    public static final String DEFAULT = "default";

    /**
     * For use with {@link #addCategory} and {@link #removeCategory} calls.
     * This category includes members of all relationship types.
     * @since 2.2
     */
    public static final String RELATIONSHIP = "relationship";

    /**
     * For use with {@link #addCategory} and {@link #removeCategory} calls.
     * This category includes members of all multi-valued types, including
     * Collection, array, and Map types of basic and relationship types.
     * @since 2.2
     */
    public static final String MULTIVALUED = "multivalued";

    /**
     * For use with {@link #addCategory} and {@link #removeCategory} calls.
     * This category includes members of all primitive and immutable
     * object class types as defined in section 6.4 of the specification,
     * including String, Locale, Currency, BigDecimal, and BigInteger; 
     * as well as Date and its jdbc subtypes and Enum types.
     * @since 2.2
     */
    public static final String BASIC = "basic";

    /**
     * For use with {@link #addCategory} and {@link #removeCategory} calls.
     * This category includes all members in the persistent type.
     * <p>
     * Using this category also sets the fetch-depth for the members in the
     * default fetch group.</p>
     * @since 2.2
     */
    public static final String ALL = "all";

    /** 
     * Return the hashCode for this instance. The hash code should combine both
     * the class and fetch group name. The hash codes for two equal instances
     * must be identical.
     * @return the hash code
     * @since 2.2
     */
    int hashCode();

    /**
     * Return whether this instance is equal to the other. The equals method
     * must compare the class for identity and the fetch group name for
     * equality.
     * @return whether this instance is equal to the other
     * @since 2.2
     */
    boolean equals(Object other);

    /** 
     * Get the name of this FetchGroup. The name is set only in the
     * factory method.
     * @return the name
     * @since 2.2
     */
    String getName();

    /** 
     * Get the persistent type (class or interface) of this FetchGroup.
     * The persistent type is set only in the factory method(s).
     * @return the persistent type
     * @since 2.2
     */
    Class getType();

    /** 
     * Get the post-load property of this FetchGroup.
     * @return the post-load property
     * @since 2.2
     */
    boolean getPostLoad();

    /** 
     * Set the post-load property of this FetchGroup.
     * @return the FetchGroup
     * @throws JDOUserException if the FetchGroup is unmodifiable
     * @since 2.2
     */
    FetchGroup setPostLoad(boolean postLoad);

    /** 
     * Add the member (field or property) to the set of members in this
     * FetchGroup.
     * @param memberName the name of a member to add to the FetchGroup
     * @return the FetchGroup
     * @throws JDOUserException if the parameter is not a member of the
     * persistent type
     * @throws JDOUserException if the FetchGroup is unmodifiable
     * @since 2.2
     */
    FetchGroup addMember(String memberName);

    /** 
     * Add the member (field or property) to the set of members in this
     * FetchGroup. Duplicates are ignored.
     * @param memberNames the names of members to add to the FetchGroup
     * @return the FetchGroup
     * @throws JDOUserException if any parameter is not a member of the
     * persistent type
     * @throws JDOUserException if the FetchGroup is unmodifiable
     * @since 2.2
     */
    FetchGroup addMembers(String[] memberNames);

    /**
     * Remove the member (field or property) from the set of members in this
     * FetchGroup.
     * @return the FetchGroup
     * @throws JDOUserException if the parameter is not a member of the
     * persistent type
     * @throws JDOUserException if the FetchGroup is unmodifiable
     * @since 2.2
     */
    FetchGroup removeMember(String memberName);

    /**
     * Remove the member (field or property) from the set of members in this
     * FetchGroup. Duplicates in the parameter list are eliminated before
     * removing them from the membership.
     * @return the FetchGroup
     * @throws JDOUserException if any parameter is not a member of the
     * persistent type
     * @throws JDOUserException if the FetchGroup is unmodifiable
     * @since 2.2
     */
    FetchGroup removeMembers(String[] memberNames);

    /**
     * Add the members (fields or properties) of the named category
     * to the set of members in this FetchGroup. This method first 
     * resolves the category name to a set of members and then adds
     * the members as if {@link #addMembers} was called. After this
     * method executes, the category is not remembered.
     * @return the FetchGroup
     * @throws JDOUserException if the FetchGroup is unmodifiable
     * @since 2.2
     */
    FetchGroup addCategory(String categoryName);

    /**
     * Remove the members (fields or properties) of the named category
     * from the set of members in this FetchGroup. This method first 
     * resolves the category name to a set of members and then removes
     * the members as if {@link #removeMembers} was called. After this
     * method executes, the category is not remembered.
     * @return the FetchGroup
     * @throws JDOUserException if the FetchGroup is unmodifiable
     * @since 2.2
     */
    FetchGroup removeCategory(String categoryName);

    /**
     * Set the recursion-depth for this member. The default is 1. A value of 0
     * means don't fetch the member (as if the member were omitted entirely).
     * A value of -1 means fetch all instances reachable via this member.
     * @return the FetchGroup
     * @param memberName the name of the field or property
     * @param recursionDepth the value for the recursion-depth property
     * @throws JDOUserException if the member does not exist
     * @throws JDOUserException if the FetchGroup is unmodifiable
     * @since 2.2
     */
    FetchGroup setRecursionDepth(String memberName, int recursionDepth);

    /**
     * Get the recursion-depth for this member.
     * @param memberName the name of the field or property
     * @return the recursion-depth for this member
     * @throws JDOUserException if the member is not in the FetchGroup
     * @since 2.2
     */
    int getRecursionDepth(String memberName);

    /**
     * Return an immutable Set of String containing the names of all members.
     * The Set is a copy of the currently defined members and will not change
     * based on subsequent changes to the membership in the FetchGroup.
     * @return an immutable Set containing the names of all members
     * in the FetchGroup
     * @since 2.2
     */
    Set getMembers();

    /**
     * Make this FetchGroup unmodifiable. If already unmodifiable, this method
     * has no effect.
     * @return the FetchGroup
     * @since 2.2
     */
    FetchGroup setUnmodifiable();

    /**
     * Return whether this FetchGroup is unmodifiable. If so, methods
     * {@link #setPostLoad}, {@link #addMember}, {@link #removeMember}, 
     * {@link #addMembers}, {@link #removeMembers}, 
     * {@link #addCategory}, and {@link #removeCategory}
     * will throw {@link JDOUserException}.
     * @return whether the FetchGroup is unmodifiable
     * @since 2.2
     */
    boolean isUnmodifiable();
}
