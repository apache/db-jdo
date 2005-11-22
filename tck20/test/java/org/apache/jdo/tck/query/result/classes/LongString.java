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
 
package org.apache.jdo.tck.query.result.classes;

import org.apache.jdo.tck.util.EqualityHelper;

/**
 * JDOQL result class having bean properties <code>s</code> and <code>l</code>.
 * These properties are used by JDO query tests in result clauses.
 */
public class LongString {

    private long l;
    private String s;
    
    public LongString() {}
    
    public LongString(long l, String s) {
        this.l = l;
        this.s = s;
    }
    
    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = 0;
        result += this.s != null ? this.s.hashCode() : 0;
        result += (int) (this.l % Integer.MAX_VALUE);
        return result;
    }
    
    /**
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (!(o instanceof LongString))
            return false;
        LongString other = (LongString) o;
        return this.l == other.l &&
               EqualityHelper.equals(this.s, other.s);
    }
    
    /**
     * @see Object#toString()
     */
    public String toString() {
        return getClass().getName() + '(' +
        this.l + ", " + this.s + ')';
    }

    /**
     * @return Returns the l.
     */
    public long getL() {
        return l;
    }

    /**
     * @param l The l to set.
     */
    public void setL(long l) {
        this.l = l;
    }

    /**
     * @return Returns the s.
     */
    public String getS() {
        return s;
    }

    /**
     * @param s The s to set.
     */
    public void setS(String s) {
        this.s = s;
    }
    
}
