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

package javax.jdo.pc;

import java.io.*;
import java.util.*;
import javax.jdo.PersistenceManager;
import javax.jdo.spi.*;

import javax.jdo.spi.PersistenceCapable;

/**
 * This is a hand-enhanced version of a simple class with two fields. The
 * enhanced code assumes datastore identity.
 *
 * @version 2.0
 */
public class PCPoint 
    implements PersistenceCapable
{
    public int x;
    public Integer y;

    // JDO generated fields
    protected transient StateManager jdoStateManager;
    protected transient byte jdoFlags;
    private static final int jdoInheritedFieldCount = 0;
    private static final String jdoFieldNames[] = { "x", "y" };
    private static final Class jdoFieldTypes[]; 
    private static final byte jdoFieldFlags[] = { 
        (byte)(PersistenceCapable.CHECK_READ + PersistenceCapable.CHECK_WRITE + 
               PersistenceCapable.SERIALIZABLE), 
        (byte)(PersistenceCapable.CHECK_READ + PersistenceCapable.CHECK_WRITE + 
               PersistenceCapable.SERIALIZABLE), 
    }; 
    private static final Class jdoPersistenceCapableSuperclass; 

    static 
    {
        jdoFieldTypes = (new Class[] {
            Integer.TYPE, sunjdo$classForName$("java.lang.Integer")
        });
        jdoPersistenceCapableSuperclass = null;
        JDOImplHelper.registerClass(
            sunjdo$classForName$("javax.jdo.pc.PCPoint"), 
            jdoFieldNames, jdoFieldTypes, jdoFieldFlags, 
            jdoPersistenceCapableSuperclass, new PCPoint());
    }

    /** JDO required no-args constructor. */
    public PCPoint() { }

    /** Constructor. */
    public PCPoint(int x, Integer y) {
        jdoSetx(this, x);
        jdoSety(this, y);
    }

    /** */
    public void setX(int x) {
        jdoSetx(this, x);
    }

    /** */
    public int getX() {
        return jdoGetx(this);
    }

    /** */
    public void setY(Integer y) {
        jdoSety(this, y);
    }

    /** */
    public Integer getY() {
        return jdoGety(this);
    }

    /** */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof PCPoint))
            return false;
        PCPoint other = (PCPoint)o;
        if (jdoGetx(this) != jdoGetx(other))
            return false;
        if (jdoGety(this) == null)
            return jdoGety(other) == null;
        if (jdoGety(other) == null)
            return jdoGety(this) == null;
        else
            return jdoGety(this).intValue() == jdoGety(other).intValue();
    }

    /** */
    public int hashCode() {
        int code = getX();
        if (getY() != null) {
            code += getY().intValue();
        }
        return code;
    }
    
    /** */
    public String toString() {
        return "PCPoint(x: " + getX() + ", y: " + getY() + ")";
    }
    

    // Generated methods in least-derived PersistenceCapable class

    public final boolean jdoIsPersistent() {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            return statemanager.isPersistent(this);
        else
            return false;
    }

    public final boolean jdoIsTransactional() {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            return statemanager.isTransactional(this);
        else
            return false;
    }

    public final boolean jdoIsNew() {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            return statemanager.isNew(this);
        else
            return false;
    }

    public final boolean jdoIsDirty() {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            return statemanager.isDirty(this);
        else
            return false;
    }

    public final boolean jdoIsDeleted() {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            return statemanager.isDeleted(this);
        else
            return false;
    }

    public final boolean jdoIsDetached() {
        return false;
    }

    public final void jdoMakeDirty(String s) {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            statemanager.makeDirty(this, s);
    }

    public final PersistenceManager jdoGetPersistenceManager() {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            return statemanager.getPersistenceManager(this);
        else
            return null;
    }

    public final Object jdoGetObjectId() {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            return statemanager.getObjectId(this);
        else
            return null;
    }

    public final Object jdoGetTransactionalObjectId() {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            return statemanager.getTransactionalObjectId(this);
        else
            return null;
    }

    public final Object jdoGetVersion() {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            return statemanager.getVersion(this);
        else
            return null;
    }

    public final synchronized void jdoReplaceStateManager(
        StateManager statemanager) {
        StateManager statemanager1 = jdoStateManager;
        if (statemanager1 != null) {
            jdoStateManager = statemanager1.replacingStateManager(
                this, statemanager);
            return;
        } 
        else {
            JDOImplHelper.checkAuthorizedStateManager(statemanager);
            jdoStateManager = statemanager;
            jdoFlags = PersistenceCapable.LOAD_REQUIRED;
            return;
        }
    }
    
    public final void jdoReplaceFlags() {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            jdoFlags = statemanager.replacingFlags(this);
    }

    public final void jdoReplaceFields(int fields[]) {
        if (fields == null)
            throw new IllegalArgumentException("fields is null");
        int i = fields.length;
        for(int j = 0; j < i; j++)
            jdoReplaceField(fields[j]);

    }

    public final void jdoProvideFields(int fields[]) {
        if (fields == null)
            throw new IllegalArgumentException("fields is null");
        int i = fields.length;
        for(int j = 0; j < i; j++)
            jdoProvideField(fields[j]);

    }

    protected final void jdoPreSerialize() {
        StateManager statemanager = jdoStateManager;
        if (statemanager != null)
            statemanager.preSerialize(this);
    }

    // Generated methods in PersistenceCapable root classes and all classes
    // that declare objectid-class in xml metadata:

    public void jdoCopyKeyFieldsToObjectId(
        PersistenceCapable.ObjectIdFieldSupplier objectidfieldsupplier, 
        Object obj) { }

    public void jdoCopyKeyFieldsToObjectId(Object obj) {
    }
    
    public void jdoCopyKeyFieldsFromObjectId(
        PersistenceCapable.ObjectIdFieldConsumer objectidfieldconsumer, 
        Object obj) { }

    protected void jdoCopyKeyFieldsFromObjectId(Object obj) {
    }

    public Object jdoNewObjectIdInstance() {
        return null;
    }

    public Object jdoNewObjectIdInstance(Object o) {
        return null;
    }
    
    // Generated methods in all PersistenceCapable classes

    public PersistenceCapable jdoNewInstance(StateManager statemanager) {
        PCPoint pcpoint = new PCPoint();
        pcpoint.jdoFlags = PersistenceCapable.LOAD_REQUIRED;
        pcpoint.jdoStateManager = statemanager;
        return pcpoint;
    }

    public PersistenceCapable jdoNewInstance(
        StateManager statemanager, Object obj) {
        PCPoint pcpoint = new PCPoint();
        pcpoint.jdoCopyKeyFieldsFromObjectId(obj);
        pcpoint.jdoFlags = PersistenceCapable.LOAD_REQUIRED;
        pcpoint.jdoStateManager = statemanager;
        return pcpoint;
    }

    protected static int jdoGetManagedFieldCount() {
        return 2;
    }

     public static final int jdoGetx(PCPoint pcpoint) {
        if (pcpoint.jdoFlags <= PersistenceCapable.READ_WRITE_OK)
            return pcpoint.x;
        StateManager statemanager = pcpoint.jdoStateManager;
        if (statemanager == null)
            return pcpoint.x;
        if (statemanager.isLoaded(pcpoint, jdoInheritedFieldCount + 0))
            return pcpoint.x;
        else
            return statemanager.getIntField(
                pcpoint, jdoInheritedFieldCount + 0, pcpoint.x);
    }

    public static final Integer jdoGety(PCPoint pcpoint) {
        if (pcpoint.jdoFlags <= PersistenceCapable.READ_WRITE_OK)
            return pcpoint.y;
        StateManager statemanager = pcpoint.jdoStateManager;
        if (statemanager == null)
            return pcpoint.y;
        if (statemanager.isLoaded(pcpoint, jdoInheritedFieldCount + 1))
            return pcpoint.y;
        else
            return (Integer)statemanager.getObjectField(
                pcpoint, jdoInheritedFieldCount + 1, pcpoint.y);
    }

    public static final void jdoSetx(PCPoint pcpoint, int i) {
        if (pcpoint.jdoFlags == PersistenceCapable.READ_WRITE_OK) {
            pcpoint.x = i;
            return;
        }
        StateManager statemanager = pcpoint.jdoStateManager;
        if (statemanager == null) {
            pcpoint.x = i;
            return;
        } 
        else {
            statemanager.setIntField(
                pcpoint, jdoInheritedFieldCount + 0, pcpoint.x, i);
            return;
        }
    }

    public static final void jdoSety(PCPoint pcpoint, Integer integer) {
        if (pcpoint.jdoFlags == PersistenceCapable.READ_WRITE_OK) {
            pcpoint.y = integer;
            return;
        }
        StateManager statemanager = pcpoint.jdoStateManager;
        if (statemanager == null) {
            pcpoint.y = integer;
            return;
        } 
        else {
            statemanager.setObjectField(pcpoint, jdoInheritedFieldCount + 1, pcpoint.y, integer);
            return;
        }
    }

    public void jdoReplaceField(int field) {
        StateManager statemanager = jdoStateManager;
        switch(field - jdoInheritedFieldCount) {
        case 0: 
            if (statemanager == null) {
                throw new IllegalStateException("jdoStateManager is null");
            } 
            else {
                x = statemanager.replacingIntField(this, field);
                return;
            }
        case 1:
            if (statemanager == null) {
                throw new IllegalStateException("jdoStateManager is null");
            } 
            else {
                y = (Integer)statemanager.replacingObjectField(this, field);
                return;
            }
        }
        throw new IllegalArgumentException("field number out of range");
    }

    public void jdoProvideField(int field) {
        StateManager statemanager = jdoStateManager;
        switch(field - jdoInheritedFieldCount) {
        case 0:
            if (statemanager == null) {
                throw new IllegalStateException("jdoStateManager is null");
            } 
            else {
                statemanager.providedIntField(this, field, x);
                return;
            }
        case 1: 
            if (statemanager == null) {
                throw new IllegalStateException("jdoStateManager is null");
            } 
            else {
                statemanager.providedObjectField(this, field, y);
                return;
            }
        }
        throw new IllegalArgumentException("field number out of range");
    }

    public void jdoCopyFields(Object obj, int fieldNumbers[]) {
        if (jdoStateManager == null)
            throw new IllegalStateException("jdoStateManager is null");
        if (!(obj instanceof PCPoint))
            throw new ClassCastException(obj.getClass().getName());
        if (fieldNumbers == null)
            throw new IllegalArgumentException("fieldNumber is null");
        PCPoint pcpoint = (PCPoint)obj;
        if (pcpoint.jdoStateManager != jdoStateManager)
            throw new IllegalArgumentException("wrong jdoStateManager");
        int i = fieldNumbers.length;
        for(int j = 0; j < i; j++)
            jdoCopyField(pcpoint, fieldNumbers[j]);
    }

    protected final void jdoCopyField(PCPoint pcpoint, int fieldNumber)
    {
        switch(fieldNumber - jdoInheritedFieldCount) {
        case 0: 
            if (pcpoint == null) {
                throw new IllegalArgumentException("pcpoint is null");
            } 
            else {
                x = pcpoint.x;
                return;
            }
        case 1: 
            if (pcpoint == null) {
                throw new IllegalArgumentException("pcpoint is null");
            } 
            else {
                y = pcpoint.y;
                return;
            }
        }
        throw new IllegalArgumentException("field number out of range");
    }

    private void writeObject(java.io.ObjectOutputStream out)
        throws java.io.IOException {
        jdoPreSerialize();
        out.defaultWriteObject();
    }
    
    protected static final Class sunjdo$classForName$(String s) {
        try {
            return Class.forName(s);
        }
        catch(ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
    
    

}
