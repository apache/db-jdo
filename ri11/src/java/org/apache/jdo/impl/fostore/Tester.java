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

package org.apache.jdo.impl.fostore;

import java.text.NumberFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
* Provides a relatively convient way to print debug messages.
*
* @author Dave Bristor
*/
class Tester {
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    private static boolean threadPrinting = 
        Boolean.getBoolean("org.apache.jdo.impl.fostore.threadprinting");
   
    private static final NumberFormat nf;
    static { 
        nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(4);
        nf.setMaximumIntegerDigits(4);
        nf.setMaximumFractionDigits(0);
        nf.setGroupingUsed(false);
    }

    // This method checks that TIME is enabled, unlike the other methods.
    // Reason: it doesn't take an arguments, and therefore the runtime of
    // calling this is very small (no String args to construct as w/ print
    // methods).
    static Object startTime() {
        Timer timer = new Timer();
        timer.start();
        return timer;
    }

    static void printTime(Object o, String msg) {
        if (null != o && o instanceof Timer) {
            Timer timer = (Timer)o;
            timer.println(msg);
        }
    }

    static String toHex(long n, int len) {
        String rc = Long.toHexString(n);
        StringBuffer zeroes = new StringBuffer("0000000000000000"); // NOI18N
        int length = rc.length();
        if (length > len) {
            rc = rc.substring(length - len);
        } else if (len > length) {
            rc = zeroes.substring(0, len - length) + rc;
        }
        return rc;
    }

    static void dump(String label, byte data[], int length) {
        dump(label, data, 0, length);
        
    }
    
    static void dump(String label, byte data[], int offset, int length) {
        // Determine number of lines to print
        final int bytesPerLine = 16;
        int lines = length / bytesPerLine;
        if (0 != length % bytesPerLine) {
            lines++;
        }
        int line = 0; // Address at start of line

        int addr = offset;
        int max = offset + length;

        logger.trace("dumping " + length + " bytes"); // NOI18N
        
        for (int i = 0; i < lines; i++) {
            StringBuffer buf = new StringBuffer();
            
            if (threadPrinting) {
                buf.append(Thread.currentThread().toString() + ": "); // NOI18N
            }
            buf.append(label + " " + nf.format((long)line) + ": "); // NOI18N
            line += bytesPerLine;
            for (int j = 0; j < bytesPerLine; j++) {
                if (addr >= max) {
                    break;
                } else {
                    buf.append(toHex((long)data[addr], 2) + " "); // NOI18N
                }
                addr++;
            }
            logger.trace(buf.toString());
        }
    }

    static void dump(String label, FOStoreOutput out, int offset, int length) {
        byte data[] = out.getBuf();
        dump(label, data, offset, length);
    }

    static class Timer {
        long start;
    
        public void start() {
            start = System.currentTimeMillis();
        }
    
        public void println(String msg) {
            long end;
            end = System.currentTimeMillis();
            System.out.println(msg + ": " + (end - start)); // NOI18N
            start = end;
        }
    }
}
