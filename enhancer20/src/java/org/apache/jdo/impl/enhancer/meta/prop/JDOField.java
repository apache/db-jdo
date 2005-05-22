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

package org.apache.jdo.impl.enhancer.meta.prop;

import java.lang.reflect.Modifier;


/**
 * A class to hold the properties of a field.
 */
final class JDOField
{
    /**
     * The name of the field.
     */
    final private String name;
    
    /**
     * The type of the field.
     */
    private String type = null;
    
    /**
     * The access modifier of the field.
     */
    private int modifiers = Modifier.PRIVATE;
    
    /**
     * The JDO modifier of the field.
     */
    private String jdoModifier = null;
    
    /**
     * The annotation type.
     */
    private String annotationType = null;
    
    /**
     * Creates a new object with the given name.
     *
     * @param  name  The name of the field.
     */
    JDOField(String name)
    {
        this.name = name;
    }
    
    /**
     * Returns the name of the field.
     *
     * @return  The name of the field.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Sets the type of the field. The given classname should have a
     * natural form(with dots) and is converted to a VM-similar
     * notation(with slashes).
     *
     * @param  type  The natural classname.
     */
    public void setType(String type)
    {
        this.type = NameHelper.fromCanonicalClassName(type);
    }
    
    /**
     * Returns the type of the field.
     *
     * @return  The type of the field.
     */
    public String getType()
    {
        return type;
    }
    
    /**
     * Returns the modifiers of the field.
     *
     * @param modifiers  The modifiers of the field.
     */
    public void setModifiers(int modifiers)
    {
        this.modifiers = modifiers;
    }
    
    /**
     * Returns the modifiers of the field.
     *
     * @return  The modifiers of the field.
     */
    public int getModifiers()
    {
        return modifiers;
    }
    
    /**
     * Sets the annotation type of the field.
     *
     * @param  annotationType annotation type
     */
    public void setAnnotationType(String annotationType)
    {
        this.annotationType = annotationType;
    }
    
    /**
     * Returns whether the field is annotated.
     *
     * @return  true if annotated field
     */
    public boolean isAnnotated()
    {
        return annotationType != null;
    }
    
    /**
     * Returns whether the field is a key primary.
     *
     * @return  true if primary key.
     */
    public boolean isKey()
    {
        return (annotationType != null
                && annotationType.equals(
                    MetaDataProperties.ANNOTATION_TYPE_KEY));
    }
    
    /**
     * Is the field in the default fetch group?
     *
     * @return  Is the field in the default fetch group?
     */
    public boolean isInDefaultFetchGroup()
    {
        return (annotationType != null
                && annotationType.equals(
                    MetaDataProperties.ANNOTATION_TYPE_DFG));
    }
    
    /**
     * Sets the modifiers of the field.
     *
     * @param jdoModifier  the persistence modifier of the field
     */
    public void setJdoModifier(String jdoModifier)
    {
        this.jdoModifier = jdoModifier;
    }
    
    /**
     * Returns whether the field is declared transient.
     *
     * @return  true if declared transient field.
     * @see  #setJdoModifier
     */
    public boolean isKnownTransient()
    {
        return (jdoModifier != null
                && jdoModifier.equals(MetaDataProperties.JDO_TRANSIENT));
    }
    
    /**
     * Returns whether the field is persistent.
     *
     * @return  true if persistent field.
     * @see  #setJdoModifier
     */
    public boolean isPersistent()
    {
        return (jdoModifier != null
                && jdoModifier.equals(MetaDataProperties.JDO_PERSISTENT));
    }
    
    /**
     * Returns whether the field is transactional.
     *
     * @return  true if transactional field
     * @see  #setJdoModifier
     */
    public boolean isTransactional()
    {
        return (jdoModifier != null
                && jdoModifier.equals(MetaDataProperties.JDO_TRANSACTIONAL));
    }
    
    /**
     * Returns whether the field is managed.
     *
     * @return  true if managed field
     */
    public boolean isManaged()
    {
        return (isPersistent() || isTransactional());
    }
    
    /**
     * Creates a string-representation of the object.
     *
     * @return  The string-representation of the object.
     */
    public String toString()
    {
        return ('<' + "name:" + name
                + ',' + MetaDataProperties.PROPERTY_TYPE
                + ':' + type
                + ',' + MetaDataProperties.PROPERTY_ACCESS_MODIFIER
                + ':' + Modifier.toString(modifiers)
                + ',' + MetaDataProperties.PROPERTY_JDO_MODIFIER
                + ':' + jdoModifier
                + ',' + MetaDataProperties.PROPERTY_ANNOTATION_TYPE
                + ':' + annotationType + '>');
    }
}
