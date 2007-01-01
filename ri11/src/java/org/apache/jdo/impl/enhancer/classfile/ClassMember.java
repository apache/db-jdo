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


package org.apache.jdo.impl.enhancer.classfile;
import java.util.Stack;

/**
 * ClassMember is a common base class for ClassMethod and ClassField
 */
abstract public class ClassMember implements VMConstants {

    /* public accessors */

    /**
     * Is the member static?
     */
    final public boolean isStatic() {
        return (access() & ACCStatic) != 0;
    }

    /**
     * Is the member final?
     */
    final public boolean isFinal() {
        return (access() & ACCFinal) != 0;
    }

    /**
     * Turn on or off the final qualifier for the member.
     */
    public void setIsFinal(boolean newFinal) {
        if (newFinal)
            setAccess(access() | ACCFinal);
        else
            setAccess(access() & ~ACCFinal);
    }

    /**
     * Is the member private?
     */
    final public boolean isPrivate() {
        return (access() & ACCPrivate) != 0;
    }

    /**
     * Is the member protected?
     */
    final public boolean isProtected() {
        return (access() & ACCProtected) != 0;
    }

    /**
     * Is the member public?
     */
    final public boolean isPublic() {
        return (access() & ACCPublic) != 0;
    }

    /* These are expected to be implemented by subtypes */

    /**
     * Return the access flags for the method - see VMConstants
     */
    abstract public int access();

    /**
     * Set the access flags for the method - see VMConstants
     */
    abstract public void setAccess(int newAccess);

    /**
     * Return the name of the member
     */
    abstract public ConstUtf8 name();

    /**
     * Return the type signature of the method
     */
    abstract public ConstUtf8 signature();

    /**
     * Return the attributes associated with the member
     */
    abstract public AttributeVector attributes();

    /**
     * Compares this instance with another for structural equality.
     */
    //@olsen: added method
    public boolean isEqual(Stack msg, Object obj) {
        if (!(obj instanceof ClassMember)) {
            msg.push("obj/obj.getClass() = "
                     + (obj == null ? null : obj.getClass()));
            msg.push("this.getClass() = "
                     + this.getClass());
            return false;
        }
        ClassMember other = (ClassMember)obj;

        return true;
    }
}
