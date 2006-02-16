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

/*
 * FetchPlan.java
 *
 */
 
package javax.jdo;

import java.util.Collection;

/**
 * Fetch groups are activated using methods on this interface. An
 * instance of this interface can be obtained from {@link
 * PersistenceManager#getFetchPlan}, {@link Extent#getFetchPlan}, and
 * {@link Query#getFetchPlan}. When a <code>Query</code> or
 * <code>Extent</code> is retrieved from a
 * <code>PersistenceManager</code>, its <code>FetchPlan</code> is
 * initialized to the same settings as that of the
 * <code>PersistenceManager</code>. Subsequent modifications of the
 * <code>Query</code> or <code>Extent</code>'s <code>FetchPlan</code>
 * are not reflected in the <code>FetchPlan</code> of the
 * <code>PersistenceManager</code>.
 * @version 2.0
 * @since 2.0
 */
public interface FetchPlan {

    /**
     * For use with {@link #addGroup}, {@link #removeGroup}, and the
     * various {@link #setGroups} calls. Value: <code>default</code>.
     * @since 2.0
     */
    public static final String DEFAULT = "default";

    /**
     * For use with {@link #addGroup}, {@link #removeGroup}, and the
     * various {@link #setGroups} calls. Value: <code>all</code>.
     * @since 2.0
     */
    public static final String ALL = "all";

    /**
     * For use with {@link PersistenceManager#detachCopy} and
     * {@link #setDetachmentOptions}. Specifies that
     * fields that are loaded but not in the current fetch plan should
     * be unloaded prior to detachment.
     * @since 2.0
     */
    public static final int DETACH_UNLOAD_FIELDS = 2;

    /**
     * For use with {@link PersistenceManager#detachCopy} and
     * {@link #setDetachmentOptions}. Specifies that
     * fields that are not loaded but are in the current fetch plan should
     * be loaded prior to detachment.
     * @since 2.0
     */
    public static final int DETACH_LOAD_FIELDS = 1;

    /**
     * For use with {@link #setFetchSize}. Value: -1.
     * @since 2.0
     */
    public static final int FETCH_SIZE_GREEDY = -1;

    /**
     * For use with {@link #setFetchSize}. Value: 0.
     * @since 2.0
     */
    public static final int FETCH_SIZE_OPTIMAL = 0;

    /** 
     * Add the fetch group to the set of active fetch groups.
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan addGroup(String fetchGroupName);

    /** 
     * Remove the fetch group from the set active fetch groups. 
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan removeGroup(String fetchGroupName);

    /** 
     * Remove all active groups leaving no active fetch group.
     * @return the FetchPlan
     * @since 2.0
     */ 
    FetchPlan clearGroups();

    /** 
     * Return an immutable collection containing the names 
     * of all active fetch groups.
     * @return an immutable collection containing the names 
     * of all active fetch groups
     * @since 2.0
     */
    Collection getGroups();

    /** 
     * Set a collection of groups.
     * @param fetchGroupNames a collection of names of fetch groups
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan setGroups(Collection fetchGroupNames);

    /** 
     * Set a collection of groups.
     * @param fetchGroupNames a String array of names of fetch groups
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan setGroups(String[]fetchGroupNames);

    /** 
     * Set the active fetch groups to the single named fetch group.
     * @param fetchGroupName the single fetch group
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan setGroup(String fetchGroupName);

    /**
     * Set the maximum fetch depth when fetching. 
     * 0 has no meaning and will throw a JDOUserException.
     * -1 means that no limit is placed on fetching.
     * A positive integer will result in that number of references from the
     * initial object to be fetched.
     * @param fetchDepth the depth
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan setMaxFetchDepth(int fetchDepth);

    /**
     * Return the maximum fetch depth used when fetching instances.
     * @return the maximum fetch depth
     * @since 2.0
     */
    int getMaxFetchDepth(); 

    /**
     * Set the roots for DetachAllOnCommit.
     * @param roots Collection of the detachment roots.
     * @since 2.0
     */
    FetchPlan setDetachmentRoots(Collection roots);

    /**
     * Get the roots for DetachAllOnCommit.
     * @return Collection of detachment roots.
     * @since 2.0
     */
    Collection getDetachmentRoots();

    /**
     * Set the root classes for DetachAllOnCommit.
     * @param rootClasses The root classes.
     * @since 2.0
     */
    FetchPlan setDetachmentRootClasses(Class[] rootClasses);

    /**
     * Get the root classes for DetachAllOnCommit.
     * @return The detachment root classes
     * @since 2.0
     */
    Class[] getDetachmentRootClasses();

    /**
     * Set the fetch size for large result set support. Use
     * {@link #FETCH_SIZE_OPTIMAL} to unset, and {@link #FETCH_SIZE_GREEDY}
     * to force loading of everything.
     * @param fetchSize the fetch size
     * @return the FetchPlan
     * @since 2.0
     */
    FetchPlan setFetchSize(int fetchSize);

    /**
     * Return the fetch size, or {@link #FETCH_SIZE_OPTIMAL} if not set,
     * or {@link #FETCH_SIZE_GREEDY} to fetch all.
     * @return the fetch size
     * @since 2.0
     */
    int getFetchSize(); 

    /**
     * Set options to be used during detachment. Options are {@link
     * #DETACH_LOAD_FIELDS} and {@link #DETACH_UNLOAD_FIELDS}.
     */
    FetchPlan setDetachmentOptions(int options);
 
    /**
     * Get options used during detachment.
     */
    int getDetachmentOptions();
  
}

