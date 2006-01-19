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
 
package org.apache.jdo.tck.pc.mylib;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class PrimitiveTypes implements Serializable {

    private static long counter = new Date().getTime();
    private static long newId() {
        synchronized (PrimitiveTypes.class) {
            return counter++;
        }
    }
    private long id = newId();
    private boolean booleanNotNull;
    private Boolean booleanNull;
    private byte byteNotNull;
    private Byte byteNull;
    private short shortNotNull;
    private Short shortNull;
    private int intNotNull;
    private Integer intNull;
    private long longNotNull;
    private Long longNull;
    private float floatNotNull;
    private Float floatNull;
    private double doubleNotNull;
    private Double doubleNull;
    private char charNotNull;
    private Character charNull;
    private Date dateNull;
    private String stringNull;
    private BigDecimal bigDecimal;
    private BigInteger bigInteger;
    private Long PrimitiveTypes;

    public Object getOid() {
        Oid oid = new Oid();
        oid.id = this.id;
        return oid;
    }

    public String toString() { 
        return "PrimitiveTypes(" + id + ")";
    }
    
    public int hashCode() {
        int hashCode = 0;
        hashCode += id;
        hashCode += booleanNotNull ? 
                Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
        hashCode += booleanNull != null ? booleanNull.hashCode() : 0;
        hashCode += byteNotNull;
        hashCode += byteNull != null ? byteNull.hashCode() : 0;
        hashCode += shortNotNull;
        hashCode += shortNull != null ? shortNull.hashCode() : 0;
        hashCode += intNotNull;
        hashCode += intNull != null ? intNull.hashCode() : 0;
        hashCode += longNotNull;
        hashCode += longNull != null ? longNull.hashCode() : 0;
        hashCode += floatNotNull;
        hashCode += floatNull != null ? floatNull.hashCode() : 0;
        hashCode += doubleNotNull;
        hashCode += doubleNull != null ? doubleNull.hashCode() : 0;
        hashCode += charNotNull;
        hashCode += charNull != null ? charNull.hashCode() : 0;
        hashCode += dateNull != null ? dateNull.hashCode() : 0;
        hashCode += stringNull != null ? stringNull.hashCode() : 0;
        hashCode += bigDecimal != null ? bigDecimal.hashCode() : 0;
        hashCode += bigInteger != null ? bigDecimal.hashCode() : 0;
        hashCode += PrimitiveTypes != null ? PrimitiveTypes.hashCode() : 0;
        return hashCode;
    }

    public boolean equals(Object o) {
        PrimitiveTypes p = (PrimitiveTypes) o;
        if (id != p.id) return false;
        if (booleanNotNull != p.booleanNotNull) return false;
        if (booleanNull != null) return booleanNull.equals(p.booleanNull);
        else if (p.booleanNull != null) return false;
        if (byteNotNull != p.byteNotNull) return false;
        if (byteNull != null) return byteNull.equals(p.byteNull);
        else if (p.byteNull != null) return false;
        if (shortNotNull != p.shortNotNull) return false;
        if (shortNull != null) return shortNull.equals(p.shortNull);
        else if (p.shortNull != null) return false;
        if (intNotNull != p.intNotNull) return false;
        if (intNull != null) return intNull.equals(p.intNull);
        else if (p.intNull != null) return false;
        if (longNotNull != p.longNotNull) return false;
        if (longNull != null) return longNull.equals(p.longNull);
        else if (p.longNull != null) return false;
        if (floatNotNull != p.floatNotNull) return false;
        if (floatNull != null) return floatNull.equals(p.floatNull);
        else if (p.floatNull != null) return false;
        if (doubleNotNull != p.doubleNotNull) return false;
        if (doubleNull != null) return doubleNull.equals(p.doubleNull);
        else if (p.doubleNull != null) return false;
        if (charNotNull != p.charNotNull) return false;
        if (charNull != null) return charNull.equals(p.charNull);
        else if (p.charNull != null) return false;
        if (dateNull != null) return dateNull.equals(p.dateNull);
        else if (p.dateNull != null) return false;
        if (stringNull != null) return stringNull.equals(p.stringNull);
        else if (p.stringNull != null) return false;
        if (bigDecimal != null) return bigDecimal.equals(p.bigDecimal);
        else if (p.bigDecimal != null) return false;
        if (bigInteger != null) return bigInteger.equals(p.bigInteger);
        else if (p.bigInteger != null) return false;
        if (PrimitiveTypes != null) return PrimitiveTypes.equals(p.PrimitiveTypes);
        else if (p.PrimitiveTypes != null) return false;
        return true;
    }

    public PrimitiveTypes() {
    }

    public PrimitiveTypes(long id,
                          boolean booleanNotNull,
                          Boolean booleanNull,
                          byte byteNotNull,
                          Byte byteNull,
                          short shortNotNull,
                          Short shortNull,
                          int intNotNull,
                          Integer intNull,
                          long longNotNull,
                          Long longNull,
                          float floatNotNull,
                          Float floatNull,
                          double doubleNotNull,
                          Double doubleNull,
                          char charNotNull,
                          Character charNull,
                          Date dateNull,
                          String stringNull,
                          BigDecimal bigDecimal,
                          BigInteger bigInteger,
                          Long PrimitiveTypes) {
        this.id = id;
        this.booleanNotNull = booleanNotNull;
        this.booleanNull = booleanNull;
        this.byteNotNull = byteNotNull;
        this.byteNull = byteNull;
        this.shortNotNull = shortNotNull;
        this.shortNull = shortNull;
        this.intNotNull = intNotNull;
        this.intNull = intNull;
        this.longNotNull = longNotNull;
        this.longNull =longNull;
        this.floatNotNull = floatNotNull;
        this.floatNull =floatNull;
        this.doubleNotNull = doubleNotNull;
        this.doubleNull = doubleNull;
        this.charNotNull = charNotNull;
        this.charNull = charNull;
        this.dateNull = dateNull;
        this.stringNull = stringNull;
        this.bigDecimal = bigDecimal; 
        this.bigInteger = bigInteger;
        this.PrimitiveTypes = PrimitiveTypes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getBooleanNotNull() {
        return booleanNotNull;
    }

    public void setBooleanNotNull(boolean booleanNotNull) {
        this.booleanNotNull = booleanNotNull;
    }

    public Boolean getBooleanNull() {
        return booleanNull;
    }

    public void setBooleanNull(Boolean booleanNull) {
        this.booleanNull = booleanNull;
    }

    public byte getByteNotNull() {
        return byteNotNull;
    }

    public void setByteNotNull(byte byteNotNull) {
        this.byteNotNull = byteNotNull;
    }

    public Byte getByteNull() {
        return byteNull;
    }

    public void setByteNull(Byte byteNull) {
        this.byteNull = byteNull;
    }

    public short getShortNotNull() {
        return shortNotNull;
    }

    public void setShortNotNull(short shortNotNull) {
        this.shortNotNull = shortNotNull;
    }

    public Short getShortNull() {
        return shortNull;
    }

    public void setShortNull(Short shortNull) {
        this.shortNull = shortNull;
    }

    public int getIntNotNull() {
        return intNotNull;
    }

    public void setIntNotNull(int intNotNull) {
        this.intNotNull = intNotNull;
    }

    public Integer getIntNull() {
        return intNull;
    }

    public void setIntNull(Integer intNull) {
        this.intNull = intNull;
    }

    public long getLongNotNull() {
        return longNotNull;
    }

    public void setLongNotNull(long longNotNull) {
        this.longNotNull = longNotNull;
    }

    public Long getLongNull() {
        return longNull;
    }

    public void setLongNull(Long longNull) {
        this.longNull = longNull;
    }

    public float getFloatNotNull() {
        return floatNotNull;
    }

    public void setFloatNotNull(float floatNotNull) {
        this.floatNotNull = floatNotNull;
    }

    public Float getFloatNull() {
        return floatNull;
    }

    public void setFloatNull(Float floatNull) {
        this.floatNull = floatNull;
    }

    public double getDoubleNotNull() {
        return doubleNotNull;
    }

    public void setDoubleNotNull(double doubleNotNull) {
        this.doubleNotNull = doubleNotNull;
    }

    public Double getDoubleNull() {
        return doubleNull;
    }

    public void setDoubleNull(Double doubleNull) {
        this.doubleNull = doubleNull;
    }

    public char getCharNotNull() {
        return charNotNull;
    }

    public void setCharNotNull(char charNotNull) {
        this.charNotNull = charNotNull;
    }

    public Character getCharNull() {
        return charNull;
    }

    public void setCharNull(Character charNull) {
        this.charNull = charNull;
    }

    public Date getDateNull() {
        return dateNull;
    }

    public void setDateNull(Date dateNull) {
        this.dateNull = dateNull;
    }

    public String getStringNull() {
        return stringNull;
    }

    public void setStringNull(String stringNull) {
        this.stringNull = stringNull;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public BigInteger getBigInteger() {
        return bigInteger;
    }

    public void setBigInteger(BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }

    public Long getPrimitiveTypes() {
        return PrimitiveTypes;
    }

    public void setPrimitiveTypes(Long primitiveTypes) {
        this.PrimitiveTypes = primitiveTypes;
    }
    
    public static class Oid implements Serializable, Comparable {

        public long id;

        public Oid() {
        }

        public Oid(String s) { id = Long.parseLong(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + id;}

        public int hashCode() { return (int)id ; }

        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid k = (Oid)other;
                return k.id == this.id;
            }
            return false;
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

        public int compareTo(Object o) {
            if (o == null)
                throw new ClassCastException();
            if (o == this)
                return 0;
            long otherId = ((Oid)o).id;
            if (id == otherId)
                return 0;
            else if (id < otherId)
                return -1;
            return 1;
        }
    }

}

