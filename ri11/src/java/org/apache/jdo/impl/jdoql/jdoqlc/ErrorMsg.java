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
 * ErrorMsg.java
 *
 * Created on August 28, 2001
 */

package org.apache.jdo.impl.jdoql.jdoqlc;

import java.util.ResourceBundle;

import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOUnsupportedOptionException;

import org.apache.jdo.jdoql.JDOQueryException;
import org.apache.jdo.util.I18NHelper;


/** 
 * 
 * @author  Michael Bouschen
 */
public class ErrorMsg
{
    /** 
     * The context is included in an error message. It usually defines the query 
     * method with the invalid definition (such as declareParameters, setFilter, 
     * etc.).
     */
    protected String context = null;
    
    /** I18N support */
    protected final static I18NHelper msg = I18NHelper.getInstance(
        "org.apache.jdo.impl.jdoql.Bundle", ErrorMsg.class.getClassLoader()); //NOI18N
    
    /** Return the current context string */
    public String getContext()
    {
        return context;
    }
    
    /** Set the specified string as the current context string. */
    public void setContext(String name)
    {
        context = name;
    }

    /**
     * Indicates an error situation. 
     * @param line line number
     * @param col column number
     * @param text error message
     */
    public void error(int line, int col, String text)
        throws JDOQueryException
    {
        if (line > 1) {
            // include line and column info
            Object args[] = {context, new Integer(line), new Integer(col), text};
            throw new JDOQueryException(
                msg.msg("EXC_PositionInfoMsgLineColumn", args)); //NOI18N
        }
        else if (col > 0)
        {
            // include column info
            Object args[] = {context, new Integer(col), text};
            throw new JDOQueryException(
                msg.msg("EXC_PositionInfoMsgColumn", args)); //NOI18N
        }
        else 
        {
            Object args[] = {context, text};
            throw new JDOQueryException(
                msg.msg("EXC_PositionInfoMsg", args)); //NOI18N
        }
    }
    
    /**
     * Indicates that a feature is not supported by the current release. 
     * @param line line number
     * @param col column number
     * @param text message
     */
    public void unsupported(int line, int col, String text)
        throws JDOUnsupportedOptionException
    {
        if (line > 1)
        {
            // include line and column info
            Object args[] = {context, new Integer(line), new Integer(col), text};
            throw new JDOUnsupportedOptionException(
                msg.msg("EXC_PositionInfoMsgLineColumn", args)); //NOI18N
        }
        else if (col > 0)
        {
            // include column info
            Object args[] = {context, new Integer(col), text};
            throw new JDOUnsupportedOptionException(
                msg.msg("EXC_PositionInfoMsgColumn", args)); //NOI18N
        }
        else 
        {
            Object args[] = {context, text};
            throw new JDOUnsupportedOptionException(
                msg.msg("EXC_PositionInfoMsg", args)); //NOI18N
        }   
    }
    
    /**
     * Indicates a fatal situation (implementation error).
     * @param text error message
     */
    public void fatal(String text)
        throws JDOFatalInternalException
    {
        throw new JDOFatalInternalException(text);
    }

    /**
     * Indicates a fatal situation (implementation error).
     * @param text error message
     */
    public void fatal(String text, Exception nested)
        throws JDOFatalInternalException
    {
        throw new JDOFatalInternalException(text, nested);
    }
}
