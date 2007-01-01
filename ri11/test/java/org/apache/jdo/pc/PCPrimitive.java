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

package org.apache.jdo.pc;

import org.apache.jdo.test.util.Util;

/**
* A class that has an instance of each primitive, and of each of Java's
* basic/simple immutable types.  For the latter, we also have a null
* equivalent, to be certain that we can store nulls.
*
* @author Dave Bristor
*/
public class PCPrimitive {
    public boolean _boolean;
    public char _char;
    public byte _byte;
    public short _short;
    public int _int;
    public long _long;
    public float _float;
    public double _double;
    public String _String;
    public String _nullString;
    public Boolean _Boolean;
    public Boolean _nullBoolean;
    public Character _Char;
    public Character _nullChar;
    public Byte _Byte;
    public Byte _nullByte;
    public Short _Short;
    public Short _nullShort;
    public Integer _Int;
    public Integer _nullInt;
    public Long _Long;
    public Long _nullLong;
    public Float _Float;
    public Float _nullFloat;
    public Double _Double;
    public Double _nullDouble;
    public Number _Number;
    public Number _nullNumber;
    
    public PCPrimitive() { }

    // Create a PCPrimitive with "interesting" values.
    public void init() {
        _boolean = true;
        _char = 'z';
        _byte = 0xf;
        _short = 10;
        _int = 42;
        _long = Long.MAX_VALUE;
        _float = 6.02e+23f;
        _double = Double.MAX_VALUE;
        _String = "hello, world";
        _nullString = null;
        _Boolean = new Boolean(_boolean);
        _nullBoolean = null;
        _Char = new Character(_char);
        _nullChar = null;
        _Byte = new Byte(_byte);
        _nullByte = null;
        _Short = new Short(_short);
        _nullShort = null;
        _Int = new Integer(_int);
        _nullInt = null;
        _Long = new Long(_long);
        _nullLong = null;
        _Float = new Float(_float);
        _nullFloat = null;
        _Double = new Double(_double);
        _nullDouble = null;
        _Number = new Double(_double);
        _nullNumber = null;
    }

    public boolean equals(Object o) {
        boolean rc = false;
        if (null != o && o instanceof PCPrimitive) {
            PCPrimitive other = (PCPrimitive)o;
            rc = 
                (_boolean == other._boolean) &&
                (_char == other._char) &&
                (_short == other._short) &&
                (_int == other._int) &&
                (_long == other._long) &&
                (_float == other._float) &&
                (_double == other._double) &&
                _String.equals(other._String) &&
                (_nullString == other._nullString) &&
                _Boolean.equals(other._Boolean) &&
                (_nullBoolean == other._nullBoolean) &&
                _Char.equals(other._Char) &&
                (_nullChar == other._nullChar) &&
                _Byte.equals(other._Byte) &&
                (_nullByte == other._nullByte) &&
                _Short.equals(other._Short) &&
                (_nullShort == other._nullShort) &&
                _Int.equals(other._Int) &&
                (_nullInt == other._nullInt) &&
                _Long.equals(other._Long) &&
                (_nullLong == other._nullLong) &&
                _Float.equals(other._Float) &&
                (_nullFloat == other._nullFloat) &&
                _Double.equals(other._Double) &&
                (_nullDouble == other._nullDouble) &&
                _Number.equals(other._Number) &&
                (_nullNumber == other._nullNumber);
        }
        return rc;
    }

    public String toString() {
        String rc = null;
        try {
            return Util.getClassName(this) +
                "\n_boolean: " + _boolean +
                "\n_char: " + _char +
                "\n_byte: " + _byte +
                "\n_short: " + _short +
                "\n_int: " + _int +
                "\n_long: " + _long +
                "\n_float: " + _float +
                "\n_double: " + _double +
                
                "\n_String: " + _String +
                "\n_nullString: " + _nullString +
                
                "\n_Boolean: " + _Boolean +
                "\n_nullBoolean: " + _nullBoolean +
                
                "\n_Char: " + _Char +
                "\n_nullChar: " + _nullChar +
                
                "\n_Byte: " + _Byte +
                "\n_nullByte: " + _nullByte +
                
                "\n_Short: " + _Short +
                "\n_nullShort: " + _nullShort +
                
                "\n_Int: " + _Int +
                "\n_nullInt: " + _nullInt +
                
                "\n_Long: " + _Long +
                "\n_nullLong: " + _nullLong +
                
                "\n_Float: " + _Float +
                "\n_nullFloat: " + _nullFloat +
                
                "\n_Double: " + _Double +
                "\n_nullDouble: " + _nullDouble +
                
                "\n_Number: " + _Number +
                "\n_nullNumber: " + _nullNumber;
        } catch (NullPointerException ex) {
            return "PCPrimitive has no values";
        }
    }
}
