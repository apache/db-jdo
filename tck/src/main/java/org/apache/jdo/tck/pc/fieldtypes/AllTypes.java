/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.pc.fieldtypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

public class AllTypes {
  private int id;
  private boolean fld_boolean;
  private byte fld_byte;
  private char fld_char;
  private double fld_double;
  private float fld_float;
  private int fld_int;
  private long fld_long;
  private short fld_short;

  private Boolean fld_Boolean;
  private Byte fld_Byte;
  private Character fld_Character;
  private Double fld_Double;
  private Float fld_Float;
  private Integer fld_Integer;
  private Long fld_Long;
  private Short fld_Short;

  private String fld_String;
  private Locale fld_Locale;
  private Date fld_Date;
  private BigDecimal fld_BigDecimal;
  private BigInteger fld_BigInteger;

  public static final int VERY_LARGE_POSITIVE_INT = Integer.MAX_VALUE - 511;
  public static final int VERY_LARGE_NEGATIVE_INT = Integer.MIN_VALUE + 512;

  public static final int NUM_VALUES =
      10; // should equal number of elements in the following arrays
  // DO NOT CHANGE THE FOLLOWING VALUES, OR MANY LINES OF CODE IN TESTS MUST CHANGE!!!
  public static final boolean[] boolean_values = {
    false, true, true, false, true, false, false, false, true, false
  };
  public static final byte[] byte_values = {
    Byte.MIN_VALUE, Byte.MAX_VALUE, 0, -100, 100, -10, 10, 50, 50, 75
  };
  public static final char[] char_values = {
    Character.MIN_VALUE, Character.MAX_VALUE, ' ', 'A', 'z', 'B', 'F', 'z', 'M', 'M'
  };
  public static final double DOUBLE_SMALLEST = -9999999999999.9;
  public static final double DOUBLE_LARGEST = 9999999999999.9;
  public static final double[] double_values = {
    DOUBLE_SMALLEST,
    DOUBLE_LARGEST,
    0.0,
    100.0,
    100.0,
    50000000.0,
    -234234.234,
    1000000000.0,
    350.5,
    -25.5
  };
  public static final float FLOAT_SMALLEST = -999999999999.9f;
  public static final float FLOAT_LARGEST = 999999999999.9f;
  public static final float[] float_values = {
    FLOAT_SMALLEST,
    FLOAT_LARGEST,
    0.0f,
    100.0f,
    100.0f,
    50000000.0f,
    -234.23f,
    1000000000.0f,
    350.5f,
    -25.5f
  };
  public static final int[] int_values = {
    VERY_LARGE_NEGATIVE_INT,
    VERY_LARGE_POSITIVE_INT,
    0,
    100,
    100,
    1000,
    -1000,
    1000000,
    -1000000,
    10000
  };
  public static final long[] long_values = {
    Long.MIN_VALUE, Long.MAX_VALUE, 0, 100, 100, 1000, -1000, 1000000, -1000, -1000000
  };
  public static final short[] short_values = {
    Short.MIN_VALUE, Short.MAX_VALUE, 0, 100, 100, 1000, -1000, 10000, -10000, -500
  };

  public static final Boolean[] Boolean_values = {
    false, true, true, false, true,
    false, false, false, true, false
  };
  public static final Byte[] Byte_values = {
    Byte.MIN_VALUE,
    Byte.MAX_VALUE,
    (byte) 0,
    (byte) -100,
    (byte) 100,
    (byte) -10,
    (byte) 10,
    (byte) 50,
    (byte) 50,
    (byte) 75
  };
  public static final Character[] Character_values = {
    Character.MIN_VALUE, Character.MAX_VALUE, ' ', 'A', 'z', 'B', 'F', 'z', 'M', 'M'
  };
  public static final Double[] Double_values = {
    DOUBLE_SMALLEST,
    DOUBLE_LARGEST,
    0.0,
    100.0,
    100.0,
    50000000.0,
    -234234.234,
    1000000000.0,
    350.5,
    -25.5
  };
  public static final Float[] Float_values = {
    FLOAT_SMALLEST,
    FLOAT_LARGEST,
    0.0f,
    100.0f,
    100.0f,
    50000000.0f,
    234234.234f,
    1000000000.0f,
    350.5f,
    -25.5f
  };
  public static final Integer[] Integer_values = {
    VERY_LARGE_NEGATIVE_INT,
    VERY_LARGE_POSITIVE_INT,
    0,
    10000,
    100,
    100,
    1000000,
    -1000000,
    -1000,
    1000
  };
  public static final Long[] Long_values = {
    Long.MIN_VALUE, Long.MAX_VALUE, 0L, 100L, -1000L, 1000L, -1000L, 1000000L, 100L, -1000000L
  };
  public static final Short[] Short_values = {
    Short.MIN_VALUE,
    Short.MAX_VALUE,
    (short) 0,
    (short) 100,
    (short) 100,
    (short) 1000,
    (short) -1000,
    (short) 10000,
    (short) -10000,
    (short) -500
  };

  public static final String[] String_values = {
    "", "hello world",
    "JDO has a very nice persistence API", "JDO",
    "Java", "abcde",
    "abcdef", "JDO is a breeze to use",
    "Java", "Long-live JDO"
  };
  public static final Locale[] Locale_values = {
    Locale.US, Locale.UK, Locale.FRANCE, Locale.GERMANY, Locale.CANADA,
    Locale.JAPAN, Locale.ITALY, Locale.CHINA, Locale.KOREA, Locale.TAIWAN
  };

  public static final BigDecimal[] BigDecimal_values = {
    new BigDecimal("24323423423.234234"), new BigDecimal("-1123123.22"),
    new BigDecimal("100.0"), new BigDecimal("100.0"),
    new BigDecimal("0"), new BigDecimal("123232.22"),
    new BigDecimal("-234234.23333"), new BigDecimal("98345983475.23"),
    new BigDecimal("-23.000034"), new BigDecimal("989899.98889")
  };
  public static final BigInteger[] BigInteger_values = {
    new BigInteger("-999999999999999999"), new BigInteger("987034534985043985"),
    new BigInteger("0"), new BigInteger("39582"),
    new BigInteger("39582"), new BigInteger("1000000000"),
    new BigInteger("-1000000000"), new BigInteger("153"),
    new BigInteger("-27345"), new BigInteger("1333330")
  };

  public static final Date[] Date_values = new Date[10];

  static {
    GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.UK);
    gc.clear();
    gc.set(1999, Calendar.DECEMBER, 31, 9, 0, 0);
    Date_values[0] = gc.getTime();
    gc.set(1957, Calendar.FEBRUARY, 1, 9, 0, 0);
    Date_values[1] = gc.getTime();
    gc.set(2032, Calendar.MARCH, 15, 9, 0, 0);
    Date_values[2] = gc.getTime();
    gc.set(1957, Calendar.FEBRUARY, 1, 9, 0, 0);
    Date_values[3] = gc.getTime();
    gc.set(1995, Calendar.JUNE, 14, 9, 0, 0);
    Date_values[4] = gc.getTime();
    gc.set(1992, Calendar.NOVEMBER, 22, 9, 0, 0);
    Date_values[5] = gc.getTime();
    gc.set(1900, Calendar.JANUARY, 1, 9, 0, 0);
    Date_values[6] = gc.getTime();
    gc.set(2015, Calendar.SEPTEMBER, 15, 9, 0, 0);
    Date_values[7] = gc.getTime();
    gc.set(1979, Calendar.AUGUST, 12, 9, 0, 0);
    Date_values[8] = gc.getTime();
    gc.set(1979, Calendar.AUGUST, 13, 9, 0, 0);
    Date_values[9] = gc.getTime();
  }

  public AllTypes() {
    id = 0;
  }

  public AllTypes(int id) {
    this.id = id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean getboolean() {
    return fld_boolean;
  }

  public void setboolean(boolean b) {
    fld_boolean = b;
  }

  public byte getbyte() {
    return fld_byte;
  }

  public void setbyte(byte b) {
    fld_byte = b;
  }

  public char getchar() {
    return fld_char;
  }

  public void setchar(char c) {
    fld_char = c;
  }

  public double getdouble() {
    return fld_double;
  }

  public void setdouble(double d) {
    fld_double = d;
  }

  public float getfloat() {
    return fld_float;
  }

  public void setfloat(float f) {
    fld_float = f;
  }

  public int getint() {
    return fld_int;
  }

  public void setint(int i) {
    fld_int = i;
  }

  public long getlong() {
    return fld_long;
  }

  public void setlong(long l) {
    fld_long = l;
  }

  public short getshort() {
    return fld_short;
  }

  public void setshort(short s) {
    fld_short = s;
  }

  public Boolean getBoolean() {
    return fld_Boolean;
  }

  public void setBoolean(Boolean b) {
    fld_Boolean = b;
  }

  public Byte getByte() {
    return fld_Byte;
  }

  public void setByte(Byte b) {
    fld_Byte = b;
  }

  public Character getCharacter() {
    return fld_Character;
  }

  public void setCharacter(Character c) {
    fld_Character = c;
  }

  public Double getDouble() {
    return fld_Double;
  }

  public void setDouble(Double d) {
    fld_Double = d;
  }

  public Float getFloat() {
    return fld_Float;
  }

  public void setFloat(Float f) {
    fld_Float = f;
  }

  public Integer getInteger() {
    return fld_Integer;
  }

  public void setInteger(Integer i) {
    fld_Integer = i;
  }

  public Long getLong() {
    return fld_Long;
  }

  public void setLong(Long l) {
    fld_Long = l;
  }

  public Short getShort() {
    return fld_Short;
  }

  public void setShort(Short s) {
    fld_Short = s;
  }

  public String getString() {
    return fld_String;
  }

  public void setString(String s) {
    fld_String = s;
  }

  public Locale getLocale() {
    return fld_Locale;
  }

  public void setLocale(Locale l) {
    fld_Locale = l;
  }

  public Date getDate() {
    return fld_Date;
  }

  public void setDate(Date d) {
    fld_Date = d;
  }

  public BigDecimal getBigDecimal() {
    return fld_BigDecimal;
  }

  public void setBigDecimal(BigDecimal bd) {
    fld_BigDecimal = bd;
  }

  public BigInteger getBigInteger() {
    return fld_BigInteger;
  }

  public void setBigInteger(BigInteger bi) {
    fld_BigInteger = bi;
  }

  public static void load(PersistenceManager pm) {
    Transaction t = pm.currentTransaction();
    t.begin();
    for (int i = 0; i < NUM_VALUES; ++i) {
      AllTypes o = new AllTypes(i);
      pm.makePersistent(o);
      o.setboolean(boolean_values[i]);
      o.setBoolean(Boolean_values[i]);
      o.setbyte(byte_values[i]);
      o.setByte(Byte_values[i]);
      o.setchar(char_values[i]);
      o.setCharacter(Character_values[i]);
      o.setdouble(double_values[i]);
      o.setDouble(Double_values[i]);
      o.setfloat(float_values[i]);
      o.setFloat(Float_values[i]);
      o.setint(int_values[i]);
      o.setInteger(Integer_values[i]);
      o.setlong(long_values[i]);
      o.setLong(Long_values[i]);
      o.setshort(short_values[i]);
      o.setShort(Short_values[i]);
      o.setString(String_values[i]);
      o.setLocale(Locale_values[i]);
      o.setDate(Date_values[i]);
      o.setBigDecimal(BigDecimal_values[i]);
      o.setBigInteger(BigInteger_values[i]);
    }
    t.commit();
  }

  public static class Oid implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id;

    public Oid() {}

    public Oid(String s) {
      id = Integer.parseInt(justTheId(s));
    }

    public String toString() {
      return this.getClass().getName() + ": " + id;
    }

    public int hashCode() {
      return id;
    }

    public boolean equals(Object other) {
      if (other != null && (other instanceof Oid)) {
        Oid k = (Oid) other;
        return k.id == this.id;
      }
      return false;
    }

    protected static String justTheId(String str) {
      return str.substring(str.indexOf(':') + 1);
    }
  }
}
