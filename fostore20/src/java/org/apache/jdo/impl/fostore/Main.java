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
 * Main.java
 *
 * Created on June 4, 2001, 9:59 AM
 */

package org.apache.jdo.impl.fostore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.jdo.JDOFatalInternalException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.util.I18NHelper;

/**
 * Standalone server for FOStore databases.
 * <p>
 * This class is <code>public</code> because it has a <code>main</code> entry
 * point for running as a standalone program.
 *
 * @author  Craig Russell
 * @version 1.0
 */
public class Main {
   
    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N
    
    /** The port number to use for incoming connections.
     */    
    int port;

    /** The backlog for Socket.listen.
     */    
    int backlog;

    /** Default backlog.
     */
    private static final int DEFAULT_BACKLOG = 5;
    
    /** The number of seconds with no activity before shutting down.
     */    
    int timeout; 
    
    /** Default timeout.
     */
    private static final int DEFAULT_TIMEOUT = 60;
    
    /** The root of the file system for database path names.
     * Currently this property is ignored.
     */    
    String root = null;

    /** The time this server started running.
     */    
    final Date startTime = new Date();
    
    /** A flag indicating that the server is shutting down.
     */    
    static boolean shutdown = false;
    
    /** The synchronizing Object for the shutdown flag.
     */    
    static Object shutdownSynchronizer = new Object();
    
    /** The Thread responsible for detecting inactivity.
     */    
    static Thread timeoutThread = null;
    
    /** The Thread responsible for listening for incoming connection requests.
     */    
    Thread listenerThread = null;
    
    /** The set of Threads servicing incoming connections.
     */    
    HashSet serviceThreads = new HashSet ();
    
    /** Creates new Main */
    public Main () {
    }

    /** The main routine.
     * @param args the command line arguments
     */
    public static void main (String args[]) {
        if (args.length == 0) {
            usage();
        }
        Main main = new Main();
        main.run (args);
    }
    
    /** Print the usage message on standard output.
     */    
    static void usage () {
        // Turn int into a String to avoid having the formatter localize it by
        // (e.g., with EN_US) putting in a comma (i.e., print 9919, not 9,919).
        print(msg.msg("MSG_MainUsage1", // NOI18N
                      new Integer(FOStoreRemoteConnection.DEFAULT_PORT).toString()));
        print(msg.msg("MSG_MainUsage2", DEFAULT_BACKLOG)); // NOI18N
        print(msg.msg("MSG_MainUsage3", DEFAULT_TIMEOUT)); // NOI18N
    }
    
    /** Run the main program.
     * @param args the command line arguments
     */    
    void run (String args[]) {
        boolean debug = logger.isDebugEnabled();
        if (debug) {
            logger.debug("FOStore Main started: " + startTime); // NOI18N
        }
        timeout = Integer.getInteger(
            "timeout", DEFAULT_TIMEOUT).intValue(); // NOI18N
        port = Integer.getInteger(
            "port", FOStoreRemoteConnection.DEFAULT_PORT).intValue(); // NOI18N
        backlog = Integer.getInteger(
            "backlog", DEFAULT_BACKLOG).intValue(); // NOI18N
        root = System.getProperty("root"); // NOI18N
        if ((root == null) || root.equals("")) { // NOI18N
            root = System.getProperty("user.dir"); // NOI18N
        }
        
        if (debug) {
            logger.debug("\ttimeout = " + timeout); // NOI18N
            logger.debug("\tport = " + port); // NOI18N
            logger.debug("\tbacklog = " + backlog); // NOI18N
            logger.debug("\troot = " + root); // NOI18N
        }
        
        startTimeoutThread();
        startListenerThread();
        try {
            timeoutThread.join();
            setShutdown();
            listenerThread.interrupt();
            listenerThread.join();
        } catch (InterruptedException ie) {
            // do nothing
            if (debug) {
                logger.debug("Main: timeoutThread.join() caught InterruptedException."); // NOI18N
            }
        } finally {
            if (debug) {
                logger.debug("Main: FOStore timeout thread ended: " + 
                               new Date().toString()); // NOI18N
            }
            setShutdown();
            for (Iterator serviceThreadIterator = serviceThreads.iterator();
                 serviceThreadIterator.hasNext();) {
                try {
                    Thread serviceThread = (Thread) serviceThreadIterator.next();
                    serviceThread.join();
                } catch (InterruptedException ie) {
                    if (debug) {
                        logger.debug("Main: serviceThread.join() caught InterruptedException."); // NOI18N
                        }
                }
            }
        }
        if (debug) {
            logger.debug("Main: FOStore shutdown."); // NOI18N
        }
    }
    
    /** Start the TimeoutThread.
     */    
    void startTimeoutThread() {
        Runnable timeoutRunnable = new TimeoutRunnable (timeout); 
        timeoutThread = new Thread (timeoutRunnable, "TimeoutThread"); // NOI18N
        timeoutThread.start();
        if (logger.isDebugEnabled()) {
            logger.debug("Main: TimeoutThread started."); // NOI18N
        }
    }
    
    static void resetTimeout() {
        timeoutThread.interrupt();
    }
    
    /** The Timeout Runnable class.  This class watches a timer,
     * and whent the timer expires, the thread terminates.
     * This causes the Main thread to fall through its join on
     * the timeout thread and completes the shutdown process.
     */    
    class TimeoutRunnable implements Runnable {
        /** The number of milliseconds to sleep before terminating this thread.
         * Another thread wishing to reset the timeout will
         * interrupt this thread.
         */        
        int timeoutMillis = timeout * 1000;
        /** Construct an instance of the TimeoutRunnable with the specified
         * number of seconds to sleep before terminating.
         * @param timeout the number of seconds before timeout.
         */        
        TimeoutRunnable (int timeout) {
            timeoutMillis = timeout * 1000;
        }
        
        /** Run the timeout thread.
         */        
        public void run() {
            boolean debug = logger.isDebugEnabled();
            
            boolean awake = false;
            if (debug) {
                logger.debug("TimeoutThread using: " + 
                               timeoutMillis + " milliseconds"); // NOI18N
            }
                while (!awake) {
                    try {
                        Thread.sleep (timeoutMillis);
                        awake = true;
                    } catch (InterruptedException ie) {
                        if (debug) {
                            logger.debug("TimeoutThread caught InterruptedException; continuing to sleep"); // NOI18N
                        }
                    }
                }
                if (debug) {
                    logger.debug("TimeoutThread ending."); // NOI18N
                }
        }
    }

    /** Start the Listener Thread.
     */    
    void startListenerThread() {
        Runnable listenerRunnable = new ListenerRunnable (port); 
        listenerThread = new Thread (listenerRunnable, "ListenerThread"); // NOI18N
        listenerThread.start();
            if (logger.isDebugEnabled()) {
                logger.debug("Main: ListenerThread started."); // NOI18N
            }
    }
    
    /** The Listener Thread class.  This class creates an
     * incoming Socket and listens on it.  When a connection
     * comes in, create a service thread using the new Socket
     * and run it.
     */    
    class ListenerRunnable implements Runnable {
        /** The port number to listen on.
         */        
        int port;
        /** The Runnable class for the Listener Thread.
         * @param port the port number to listen on.
         */        
        ListenerRunnable (int port) {
            this.port = port;
        }
        /** Run the listener thread.  Create a ServerSocket using the port
         * and backlog parameters and listen on it.  For each incoming
         * request, create a ConnectionRunnable and start a thread to
         * service the request.  
         * This thread continues to accept incoming connections until the
         * shutdown flag is set, at which point it terminates.
         */        
        public void run() {
            boolean debug = logger.isDebugEnabled();
            try {
                if (debug) {
                    logger.debug("ListenerThread using port: " + port); // NOI18N
                }
                ServerSocket listener = new ServerSocket (port, backlog);
                if (debug) {
                    logger.debug("ListenerThread using ServerSocket: " + 
                                   listener); // NOI18N
                }
                while (true) {
                    if (getShutdown()) break;
                    if (debug) {
                        logger.debug("ListenerThread accepting new connections."); // NOI18N
                    }
                    final Socket connection = listener.accept();
                    if (debug) {
                        logger.debug("ListenerThread accepted " + connection); // NOI18N
                    }
                    if (connection.getLocalPort() == 0 &
                        connection.getPort() == 0) {
                            // must be a bogus shutdown connection
                    if (debug) {
                        logger.debug("Bugus connection ignored: " + connection); // NOI18N
                    }
                        continue;
                    }
                    Runnable connectionRunnable = 
                        new ConnectionRunnable (connection);
                    Thread connectionThread = 
                        new Thread (connectionRunnable, "Connection"); // NOI18N
                    serviceThreads.add(connectionThread);
                    connectionThread.start();
                }
            } catch (java.net.UnknownHostException uhe) {
                if (debug) {
                    logger.debug("ListenerThread caught UnknownHostException"); // NOI18N
                }
            } catch (java.net.BindException ioe) {
                if (debug) {
                    logger.debug("ListenerThread caught BindException"); // NOI18N
                }
                ioe.printStackTrace();
            } catch (java.io.IOException ioe) {
                if (debug) {
                    logger.debug("ListenerThread caught IOException"); // NOI18N
                }
                ioe.printStackTrace();
            } finally {
                if (debug) {
                    logger.debug("ListenerThread ending."); // NOI18N
                }
            }
        }
    }
        
    /** The Runnable class for incoming connections.
     */    
    class ConnectionRunnable implements Runnable {
        /** The Socket that received an incoming request.
         */        
        Socket socket;
        /** The Runnable class for incoming connections.
         * @param conn the socket which received a connection request.
         */        
        ConnectionRunnable (Socket conn) {
            socket = conn;
        }
        /** Run the Connection Thread.  This handles the incoming
         * connection in the handleConnection method.  
         */        
        public void run () {
            if (logger.isDebugEnabled()) {
                logger.debug("ConnectionRunnable started."); // NOI18N
            }
            handleConnection (socket);
            serviceThreads.remove(this);
            if (logger.isDebugEnabled()) {
                logger.debug("ConnectionRunnable ending."); // NOI18N
            }
        }
    }
    
    /** Handle the incoming connection.  This method should create a new
     * handler instance to read the messages from the connection, parse
     * the message, determine which database is being used, and handle
     * the requests.
     * @param socket the socket connected by the listener
     */    
    void handleConnection (Socket socket) {
        boolean info = logger.isInfoEnabled();
        FOStoreServerConnectionImpl server =
            new FOStoreServerConnectionImpl(socket, root);
        boolean connected = true;
        while (connected) {

            if (info) {
                logger.info("Main.handleConnection"); // NOI18N
            }
            
            try {
                resetTimeout(); // reset the timeout thread on each message received
                server.readInputFromClient();
            } catch (EOFException ioe) {
                connected = false; // normal case of EOF indicating remote side closed
                break;
            } catch (IOException ioe) {
                connected = false;
                throw new JDOFatalInternalException (
                    msg.msg("ERR_HandleConnectionReadIOException"), ioe); // NOI18N
            }

            if (info) {
                logger.info("Main.handleConnection: processRequests"); // NOI18N
            }
            server.processRequests();

            try {
                if (info) {
                    logger.info("Main.handleConnection: release & write"); // NOI18N
                }
                server.releaseDatabase();
                server.writeOutputToClient();
            } catch (IOException ioe) {
                connected = false;
                ioe.printStackTrace(); // should not occur
                throw new JDOFatalInternalException (
                    msg.msg("ERR_HandleConnectionWriteIOException"), ioe); // NOI18N
            } catch (InterruptedException ioe) {
                connected = false;
                ioe.printStackTrace(); // should not occur
                throw new JDOFatalInternalException (
                    msg.msg("ERR_HandleConnectionWriteInterruptedException"), 
                    ioe); // NOI18N
            }
        }
        try {          
            if (info) {
                logger.info("Main.handleConnection: close server, socket"); // NOI18N
            }
            server.close();
            socket.close();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
        }
    }

    /** Test if the server is shutting down.
     * @return if the server is shutting down.
     */    
    static boolean getShutdown() {
        synchronized (shutdownSynchronizer) {
        return shutdown;
        }
    }
    
    /** Set the shutdown flag.
     */    
    static void setShutdown() {
        synchronized (shutdownSynchronizer) {
            shutdown = true;
        }
    }

    /** Print a message on the standard output.
     * @param s the message to print.
     */    
    static void print (String s) {
        System.out.println (s);
    }
    
    /** Flush the standard output.
     */    
    static void flush() {
        System.out.flush();
    }

}
