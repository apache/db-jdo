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

/**
 * Allows a set of threads to wait for all to reach a common barrier
 * point.
 *
 * <p>The <tt>RogueBarrier</tt> uses a rigorous breakage model:
 * No thread can leave a barrier point prematurely.  All attempts to
 * interrupt an awaiting thread via an {@link InterruptedException}
 * are being ignored.
 *
 * @author Martin Zaun
 */
public class RogueBarrier {

    /** The internal synchronization object */
    private final Object lock = new Object();
    
    /** The number of parties needed to trip the barrier */
    private final int parties;

    /** The current generation's trip condition (need a boolean holder) */
    private boolean[] tripped = new boolean[1];

    /** The number of parties still waiting for */
    private int missing;

    /**
     * Creates a new <tt>RogueBarrier</tt> that will trip when the
     * given number of parties (threads) are waiting upon it.
     *
     * @param parties the number of threads that must invoke {@link #await}
     * before the barrier is tripped.
     *
     * @throws IllegalArgumentException if <tt>parties</tt> is less than 1.
     */
    public RogueBarrier(int parties) {
        if (parties <= 0) {
            throw new IllegalArgumentException("Argument for parties must be positive.");
        }
        this.parties = parties;
        missing = parties;
    }

    /**
     * Waits until all parties have invoked <tt>await</tt> on this barrier.
     *
     * @return the arrival index of the current thread, where index
     *  <tt>parties - 1</tt> indicates the first to arrive and
     *  zero indicates the last to arrive.
     */
    public int await() {
        synchronized (lock) {
            //assert (parties > 0);
            //System.out.println("thread = " + Thread.currentThread().getId());
            
            // if tripping, reset barrier and notify waiting threads
            final int index = --missing;
            if (index == 0) {
                tripped[0] = true; // notify current generation
                lock.notifyAll();
                tripped = new boolean[1]; // start new generation
                missing = parties;
                return 0;
            }
            
            // wait until tripped
            final boolean[] myTripped = tripped; // my generation's condition
            do {
                try {
                    lock.wait();
                } catch (InterruptedException ie) { // swallow
                }
            } while (!myTripped[0]);
            return index;
        }
    }

    static public class Test extends Thread {
        static private final int parties = 1000;
        static private final RogueBarrier b = new RogueBarrier(parties);
        static private int count;

        public void run() {
            final int i0 = b.await();
            if (i0 == 0) {
                System.out.println("    incrementing count to " + parties
                                   + " ...");
            }
            synchronized (Test.class) {
                count++;
            }

            final int i1 = b.await();
            synchronized (Test.class) {
                if (i1 == 0) {
                    System.out.println("    count = " + count);
                }
                //assert (count == parties+1);
            }

            final int i2 = b.await();
            if (i2 == 0) {
                System.out.println("    decrementing count to zero ...");
            }
            synchronized (Test.class) {
                count--;
            }

            final int i3 = b.await();
            synchronized (Test.class) {
                if (i3 == 0) {
                    System.out.println("    count = " + count);
                }
                //assert (count == 0);
            }

            b.await();
        }

        static public void test() {
            System.out.println("--> RogueBarrier$Test.test()");

            // start threads
            final Thread threads[] = new Test[parties];
            for (int i = 0; i < parties; i++) {
                threads[i] = new Test();
                threads[i].start();
            }

            // wait for all threads to finish
            for (int i = 0; i < parties; i++) {
                while (true) {
                    try {
                        threads[i].join();
                        break;
                    } catch (InterruptedException e) {
                    }
                }
            }

            System.out.println("<-- RogueBarrier$Test.test()");
        }
    
        static public void main(String[] args) {
            Test.test();
        }
    }
}
