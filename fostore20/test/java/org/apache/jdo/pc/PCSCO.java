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

package org.apache.jdo.pc;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.BitSet;
import java.util.Calendar;
import java.util.Locale;

import org.apache.jdo.test.util.Util;

/**
* A class that represents SCO's.  For each type of SCO class, provides an
* instance with a value and an instance whose value is null.
*
* @author Dave Bristor
*/
public class PCSCO {

    public java.util.Date _date;
    public java.util.Date _nullDate;

    public java.util.Date _scoDate;
    public java.util.Date _nullSCODate;

    public java.sql.Date _sqlDate;
    public java.sql.Date _nullSqlDate;

    public BigDecimal _bigDecimal;
    public BigDecimal _nullBigDecimal;

    public BigInteger _bigInteger;
    public BigInteger _nullBigInteger;

    public BitSet _bitSet;
    public BitSet _nullBitSet;

    public Locale _locale;
    public Locale _nullLocale;

    public PCSCO() { }

    // Create a Collections with "interesting" values.
    public void init() {
        _date = Util.moonWalkDate.getTime();
        _nullDate = null;

        _scoDate = null; // No PM => Test_SCO assigns via setSCODate.
        _nullSCODate = null;

        _sqlDate = new java.sql.Date(Util.moonWalkDate.getTime().getTime());
        _nullSqlDate = null;

        _bigDecimal = new BigDecimal(3.14159);
        _nullBigDecimal = null;

        _bigInteger = new BigInteger("1234567890987654321");
        _nullBigInteger = null;

        _bitSet = new BitSet();
        _bitSet.set(0); _bitSet.set(3); _bitSet.set(5); _bitSet.set(8);
        _nullBitSet = null;

        // French spoken in Canada on a Macintosh.
        _locale = new Locale("fr", "CA", "MAC");
        _nullLocale = null;
    }

    public java.util.Date getSCODate() {
        return this._scoDate;
    }

    public void setSCODate(java.util.Date scoDate) {
        this._scoDate = scoDate;
        // Note, this changes the the specified Date to represent the same
        // Date as stored in _date 
        this._scoDate.setTime(_date.getTime());
    }

    public java.sql.Date getSCOSqlDate() {
        return this._sqlDate;
    }

    public void setSCODate(java.sql.Date sqlDate) {
        this._sqlDate = sqlDate;
        // Note, this changes the the specified Date to represent the same
        // Date as stored in _date 
        this._sqlDate.setTime(_date.getTime());
    }

    public String toString() {
        StringBuffer rc = new StringBuffer(Util.getClassName(this));
        try {
            rc.append("\n_date: ").append(Util.longFormatter.format(_date));
            rc.append(" (" + _date.getClass().getName());
            if (_date instanceof org.apache.jdo.sco.SCO) {
                org.apache.jdo.sco.SCO _sco = (org.apache.jdo.sco.SCO)_date;
                rc.append(", owning field=" + _sco.getFieldName());
            }
            rc.append(")");
            rc.append("\n_nullDate: ").append(_nullDate);
            
            rc.append("\n_scoDate: ").append(Util.longFormatter.format(_scoDate));
            rc.append(" (" + _scoDate.getClass().getName());
            if (_scoDate instanceof org.apache.jdo.sco.SCO) {
                org.apache.jdo.sco.SCO _sco = (org.apache.jdo.sco.SCO)_scoDate;
                rc.append(", owning field=" + _sco.getFieldName());
            }
            rc.append(")");
            rc.append("\n_nullSCODate: ").append(_nullSCODate);
            
            rc.append("\n_sqlDate: ").append(Util.longFormatter.format(_sqlDate));
            rc.append(" (" + _sqlDate.getClass().getName());
            if (_sqlDate instanceof org.apache.jdo.sco.SCO) {
                org.apache.jdo.sco.SCO _sql = (org.apache.jdo.sco.SCO)_sqlDate;
                rc.append(", owning field=" + _sql.getFieldName());
            }
            rc.append(")");
            rc.append("\n_nullSqlDate: ").append(_nullSqlDate);
            
            rc.append("\n_bigDecimal: ").append(_bigDecimal.toString());
            rc.append("\n_nullBigDecimal: ").append(_nullBigDecimal);
            
            rc.append("\n_bigInteger: ").append(_bigInteger.toString());
            rc.append("\n_nullBigInteger: ").append(_nullBigInteger);
            
            rc.append("\n_bitSet: ").append(_bitSet.toString());
            rc.append("\n_nullBitSet: ").append(_nullBitSet);
            
            rc.append("\n_locale: ").append(_locale.toString());
            rc.append("\n_nullLocale: ").append(_nullLocale);
            
        } catch (NullPointerException ex) {
            return "SCO has no values";
        }
        return rc.toString();
    }
}
