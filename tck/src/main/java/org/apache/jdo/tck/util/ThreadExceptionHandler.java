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

package org.apache.jdo.tck.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class extends java.lang.ThreadGroup and stores any uncaught
 * exception of threads from this ThreadGroup for later retrieval.
 *
 * @author Michael Bouschen
 */
public class ThreadExceptionHandler extends ThreadGroup
{
    /** 
     * Map of uncaught exceptions. The thread is the key and the uncaught
     * Throwable is the value in the map. 
     */
    private final Map<Thread, Throwable> uncaughtExceptions = new HashMap<>();
    
    /** Constructor. */
    public ThreadExceptionHandler() {
        super("ThreadExceptionHandler");
    }
    
    /** Called by the Java Virtual Machine when a thread in this thread
     * group stops because of an uncaught exception. This implementation
     * stores the uncaught exception in a map for later retrieval.
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        uncaughtExceptions.put(t, e);
    }

    /**
     * Returns an uncaught exception for the specified thread.
     * @param t the thread
     * @return uncaught exception for the specified thread
     */
    public Throwable getUncaughtException(Thread t) {
        return (Throwable)uncaughtExceptions.get(t);
    }

    /** 
     * Returns all uncaught exceptions stored in this ThreadGroup. 
     * Each element in the returned set is a Map.Entry with the 
     * thread as the key and the uncaught Throwable is the value.
     * @return Set of uncaught exceptions
     */
    public Set<Map.Entry<Thread, Throwable>> getAllUncaughtExceptions() {
        return uncaughtExceptions.entrySet();
    }

    /** 
     * Clears all exceptions in this ThreadGroup. 
     */
    public void clear() {
        uncaughtExceptions.clear();
    }
}
