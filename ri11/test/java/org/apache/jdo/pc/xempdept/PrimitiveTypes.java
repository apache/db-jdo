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

package org.apache.jdo.pc.xempdept;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Date;
import java.math.BigDecimal;
import java.math.BigInteger;

public class PrimitiveTypes implements Serializable, Identifiable {

    private long id;
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
        this.PrimitiveTypes = PrimitiveTypes;
    }
    
    public static class Oid implements Serializable, Comparable {

        public long id;

        public Oid() {
        }

        public boolean equals(java.lang.Object obj) {
            if( obj==null ||
                !this.getClass().equals(obj.getClass()) ) return( false );
            Oid o=(Oid) obj;
            if( this.id!=o.id ) return( false );
            return( true );
        }

        public int hashCode() {
            int hashCode=0;
            hashCode += id;
            return( hashCode );
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

