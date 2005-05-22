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

package org.apache.jdo.test.util;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Locale;
import java.text.SimpleDateFormat;

/**
* Provides for converting some collection-y objets to strings for testing
* output purposes.
*
* @author Dave Bristor
*/
public class Util {
    /** */
    public static SimpleDateFormat longFormatter;
    /** */
    public static SimpleDateFormat shortFormatter;
    /** Calendar representing the date Neil Armstrong walks on the moon */
    public static GregorianCalendar moonWalkDate;

    static {
        TimeZone timeZone = TimeZone.getTimeZone("America/New_York");

        longFormatter = new SimpleDateFormat("EEE/d/MMM/yyyy-HH:mm:ss", Locale.US);
        longFormatter.setTimeZone(timeZone);

        shortFormatter = new SimpleDateFormat("d/MMM/yyyy", Locale.US);
        shortFormatter.setTimeZone(timeZone);

        moonWalkDate = new GregorianCalendar(timeZone);
        // Neil Armstrong walks on the moon
        moonWalkDate.set(1969, GregorianCalendar.JULY, 20, 22, 56, 00);
        // we need to set the millis
        moonWalkDate.set(GregorianCalendar.MILLISECOND, 0);
    }

    public static String stringifyArrayObj(Object val) {
        StringBuffer rc = new StringBuffer("[ ");
        try {
            int length = Array.getLength(val);
            for (int i = 0; i < length; i++) {
                Object obj = Array.get(val, i);
                if (obj.getClass().isArray()) {
                    rc.append(stringifyArrayObj(obj));
                } else if (obj instanceof Date) {
                    rc.append(longFormatter.format((Date)obj));
                } else {
                    rc.append(obj);
                }
                if (i < length - 1) {
                    rc.append(", ");
                }
            }
        } catch (IllegalArgumentException ex) {
            System.err.println("stringifyArrayVal: " + ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("stringifyArrayVal: " + ex);
        }
        return rc.append(" ]").toString();
    }

    public static String stringifyList(List val, String name) {
        StringBuffer rc = new StringBuffer("\n");
        rc.append(name).append(": ");
        if (null == val) {
            rc.append("__null__");
        } else if (val.isEmpty()) {
            rc.append(" __empty__");
        } else {
            int size = val.size();
            for (int i = 0; i < size; i++) {
                rc.append("\n " + i + ": ");
                Object obj = val.get(i);
                if (obj.getClass().isArray()) {
                    rc.append(stringifyArrayObj(obj));
                } else if (obj instanceof Date) {
                    rc.append(longFormatter.format((Date)obj));
                } else if (obj instanceof Map) {
                    rc.append(sortMap((Map)obj));
                } else {
                    rc.append(obj);
                }
            }
        }
        return rc.toString();
    }

    /**
     * Returns a StringBuffer that contains the entries of the given map,
     * sorted.
     */
    public static String stringifyMap(Map val, String name) {
        StringBuffer rc = new StringBuffer("\n");
        rc.append(name).append(": ");
        if (null == val) {
            rc.append("__null__");
        } else if (val.isEmpty()) {
            rc.append(" __empty__");
        } else {
            TreeSet ts = new TreeSet();
            
            Set entries = val.entrySet();
            for (Iterator i = entries.iterator(); i.hasNext();) {
                StringBuffer entryString = new StringBuffer();
                Map.Entry entry = (Map.Entry)i.next();
                entryString.append("\n  key: " + entry.getKey() + ", value: ");
                Object obj = entry.getValue();
                if (obj.getClass().isArray()) {
                    entryString.append(stringifyArrayObj(obj));
                } else if (obj instanceof Date) {
                    entryString.append(longFormatter.format((Date)obj));
                } else if (obj instanceof Map) {
                    entryString.append(sortMap((Map)obj));
                } else {
                    entryString.append(obj);
                }
                ts.add(entryString.toString());
            }

            for (Iterator i = ts.iterator(); i.hasNext();) {
                rc.append((String)i.next());
            }
        }
        return rc.toString();
    }

    /**
     * Returns a StringBuffer that contains the entries of the given set,
     * sorted.
     */
    public static String stringifySet(Set val, String name) {
        StringBuffer rc = new StringBuffer("\n");
        rc.append(name).append(": ");
        if (null == val) {
            rc.append("__null__");
        } else if (val.isEmpty()) {
            rc.append(" __empty__");
        } else {
            TreeSet ts = new TreeSet();
            
            for (Iterator i = val.iterator(); i.hasNext();) {
                StringBuffer entryString = new StringBuffer();
                entryString.append("\n  elem: ");
                Object obj = i.next();
                if (obj instanceof Date) {
                    entryString.append(longFormatter.format((Date)obj));
                } else if (obj instanceof Map) {
                    entryString.append(sortMap((Map)obj));
                } else {
                    entryString.append(obj);
                }
                ts.add(entryString.toString());
            }

            for (Iterator i = ts.iterator(); i.hasNext();) {
                rc.append((String)i.next());
            }
        }
        return rc.toString();
    }

    /** Returns the elements of Maps in order based on
     * the toString() of the keys.
     */
    public static String sortMap(Map map) {
        if (map.size() == 0) {
            return "{ }";
        }
        StringBuffer rc = new StringBuffer();
        TreeSet sorted =
            new TreeSet(new Comparator () {
                    public int compare(Object o1, Object o2) {
                        return o1.toString().compareTo(o2.toString());
                    }
                    public boolean equals() {
                        return true;
                    }
                });
        rc.append("{");
        sorted.addAll(map.keySet());
        
        Iterator j = sorted.iterator();
        while (true) {
            Object o = j.next();
            rc.append(o.toString() + "="
                      + map.get(o).toString());
            if (j.hasNext()) {
                rc.append(", ");
            } else {
                break;
            }
        }
        rc.append("}");
        return rc.toString();
    }

    public static String getInfo(String name, Object o) {
        StringBuffer rc = new StringBuffer(name);
        rc.append(" (");
        if (o==null) {
            rc.append("nullObject");
        } else {
            rc.append(o.getClass().getName());
        }
        if (o instanceof org.apache.jdo.sco.SCO) {
            org.apache.jdo.sco.SCO _sco = (org.apache.jdo.sco.SCO)o;
            rc.append(", owning field=" + _sco.getFieldName());
        }
        rc.append(")");
        return rc.toString();
    }        

    /** @return Name of the class of the given object
    */
    public static String getClassName(Object obj) {
        return obj.getClass().getName();
    }
    
    /** 
     * @return Name of the class of the given object without package prefix
     */
    public static String getClassBaseName(Object obj) {
     	String className = getClassName(obj);
        int index = className.lastIndexOf('.');
        if (index != -1) {
            className = className.substring(index + 1);
        }
        return className;
    }
}
