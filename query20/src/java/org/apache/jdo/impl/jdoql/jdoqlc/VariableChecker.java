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

/*
 * VariableChecker.java
 *
 * Created on September 12, 2001
 */

package org.apache.jdo.impl.jdoql.jdoqlc;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.JDOFatalInternalException;

import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.util.I18NHelper;


/**
 * Checks variable declarations/application.
 *
 * @author  Michael Bouschen
 * @version 0.1
 */
public class VariableChecker
{
    /** I18N support */
    protected final static I18NHelper msg = I18NHelper.getInstance(
        "org.apache.jdo.impl.jdoql.Bundle", VariableChecker.class.getClassLoader()); //NOI18N

    /**
     * A VarInfo consists of two info fields:
     * - constraint: the variable is constraint with the specified expr
     * - used: the variable is used
     */
    static class VarInfo
    {
        /**
         * The constraint expression.
         */
        JDOQLAST constraint;

        /**
         * Set of JDOQLAST nodes denoting an access of this variable.
         */
        Set used;

        /**
         * Dependency for this variable. 
         * The constraint for this variable may use another variable. 
         */
        String dependsOn;

        /**
         * Flag whether this varInfo is checked already (see checkConstraints)
         */
        int status;

        static final int UNCHECKED = 0;
        static final int IN_PROGRESS = 1;
        static final int CHECKED = 2;

        VarInfo()
        {
            this.constraint = null;
            this.used = new HashSet();
            this.dependsOn = null;
            this.status = UNCHECKED;
        }

        VarInfo(VarInfo other)
        {
            this.constraint = other.constraint;
            this.used = new HashSet(other.used);
            this.dependsOn = other.dependsOn;
            this.status = other.status;
        }
    }

    /**
     * Map of variable infos
     */
    protected Map varInfos;

    /**
     * Create an empty variable table
     */
    public VariableChecker()
    {
        varInfos = new HashMap();
    }

    /**
     * Create a variable table initialized with the entries of the other variable table.
     * The constructor creates copies of the values stored in the map (instances of class VarInfo).
     */
    public VariableChecker(VariableChecker other)
    {
        varInfos = new HashMap();
        for (Iterator i = other.varInfos.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry)i.next();
            varInfos.put(entry.getKey(), new VarInfo((VarInfo)entry.getValue()));
        }
    }

    /**
     * Creates a new entry in the variable table with the specified name as key and 
     * an empty value.
     */
    public void add(String name)
    {
        // init var entry as not constraint and unused
        varInfos.put(name, new VarInfo());
    }

    /**
     * Mark the specified variable as used. 
     * The method sets the info field of the VarInfo object to true.
     */
    public void markUsed(JDOQLAST variable, String dependendVar)
    {
        String name = variable.getText();
        VarInfo entry = (VarInfo)varInfos.get(name);
        if (entry == null) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_VariableCheckerUndefinedVariable", //NOI18N
                        "markUsed", name)); //NOI18N
        }
        entry.used.add(variable);
        if (dependendVar != null) {
            VarInfo dependendVarInfo = (VarInfo)varInfos.get(dependendVar);
            if (dependendVarInfo.dependsOn != null) {
                throw new JDOFatalInternalException(
                    msg.msg("ERR_VariableCheckerMultipleDependencies", //NOI18N    
                            dependendVar, dependendVarInfo.dependsOn, name));
            }
            dependendVarInfo.dependsOn = name;
        }
    }

    /**
     * Mark the specified variable as constaint with the specified expr.
     * The method sets the constraint field of the VarInfo object to true.
     */
    public void markConstraint(JDOQLAST variable, JDOQLAST expr)
    {
        String name = variable.getText();
        VarInfo entry = (VarInfo)varInfos.get(name);
        if (entry == null) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_VariableCheckerUndefinedVariable", //NOI18N
                        "markConstraint", name)); //NOI18N
        }
        String old = (entry.constraint==null ? null : entry.constraint.getText());
        if ((old != null) && !old.equals(expr.getText())) {
            throw new JDOUnsupportedOptionException(
                msg.msg("EXC_UnsupportedMultipleConstraints", name)); //NOI18N
        }
        entry.constraint = expr;
    }

    /**
     * Merges the specified variable table (other) into this variable table.
     */
    public void merge(VariableChecker other)
    {
        for (Iterator i = varInfos.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry)i.next();
            String name = (String)entry.getKey();
            VarInfo info = (VarInfo)entry.getValue();
            VarInfo otherInfo = (VarInfo)other.varInfos.get(name);
            
            // copy other info if this info is empty
            if ((info.constraint == null) && (info.used.size() == 0)) {
                info.constraint = otherInfo.constraint;
                info.used = otherInfo.used;
                info.dependsOn = otherInfo.dependsOn;
                info.status = otherInfo.status;
                continue;
            }

            // do nothing if otherInfo is empty
            if ((otherInfo.constraint == null) && (otherInfo.used.size() == 0)) {
                continue;
            }
            
            // constraint check
            // If both variables tables include constraints they have to be the same
            if ((info.constraint != null) && (otherInfo.constraint != null)) {
                if (!otherInfo.constraint.getText().equals(info.constraint.getText())) {
                    throw new JDOUnsupportedOptionException(
                        msg.msg("EXC_DifferentConstraints", name)); //NOI18N
                }
            }
            // If at least one variable table does not define constraint, 
            // nullify the constaint in this variable table
            else {
                info.constraint = null;
                info.dependsOn = null;
                info.status = VarInfo.UNCHECKED;
            }
            
            // copy otherInfo.used to this used list
            info.used.addAll(otherInfo.used);
        }
    }

    /**
     *
     */
    public void checkConstraints()
    {
        for (Iterator i = varInfos.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry)i.next();
            VarInfo info = (VarInfo)entry.getValue();
            checkConstraint((String)entry.getKey(), (VarInfo)entry.getValue());
        }
    }

    protected void checkConstraint(String variable, VarInfo info)
    {
        switch (info.status) {
        case VarInfo.UNCHECKED:
            // if unchecked, start checking
            info.status = VarInfo.IN_PROGRESS;
            break;
        case VarInfo.IN_PROGRESS:
            // if this VarInfo is currently processed we have a cyclic dependency
            throw new JDOUnsupportedOptionException(
                msg.msg("EXC_UnsupportedCyclicConstaint", variable)); // NOI18N
        case VarInfo.CHECKED:
            // if alreday checked just return
            return;
        }
        
        if (info.dependsOn != null) {
            VarInfo dependendVarInfo = (VarInfo)varInfos.get(info.dependsOn);
            checkConstraint(info.dependsOn, dependendVarInfo);
        }
        
        if (info.constraint == null) {
            throw new JDOUnsupportedOptionException(
                msg.msg("EXC_UnconstraintVariable", variable)); //NOI18N
        }
        
        if (info.used.size() == 0) {
            throw new JDOUnsupportedOptionException(
                msg.msg("EXC_UnusedVariable", variable)); //NOI18N
        }
        
        // Next line in comment, because the node visitor for queries in memory
        // does not like VARIABLE_ACCESS child nodes. 
        // This needs to be investigated for the SQL generation.
        //attachConstraintToUsedAST(info);
        info.status = VarInfo.CHECKED;
    }
    
    /**
     *
     */
    protected void attachConstraintToUsedAST(VarInfo info)
    {
        for (Iterator i = info.used.iterator(); i.hasNext();) {
            JDOQLAST varNode = (JDOQLAST)i.next();
            if (varNode.getFirstChild() == null) 
                varNode.setFirstChild(JDOQLASTFactory.getInstance().dupTree(info.constraint));
        }
    }

}

