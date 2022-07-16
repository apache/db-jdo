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

package org.apache.jdo.tck.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * A JDK1.4 logging Handler class which delegates to a file handler.
 * Configuration: By default each TCKFileHandler is initialized using 
 * the following LogManager configuration properties:
 * <ul>
 * <li>java.util.logging.TCKFileHandler.level 
 * specifies the default level for the Handler (defaults to Level.ALL).</li>
 * <li>java.util.logging.FileHandler.filter 
 * specifies the name of a Filter class to use (defaults to no Filter).</li>
 * <li>java.util.logging.FileHandler.formatter 
 * specifies the name of a Formatter class to use 
 * (defaults to java.util.logging.SimpleFormatter).</li>
 * <li>java.util.logging.FileHandler.encoding 
 * the name of the character set encoding to use 
 * (defaults to the default platform encoding).</li>
 * <li>java.util.logging.FileHandler.fileName 
 * specifies a the output file name. See below for details.</li>
 * <li>java.util.logging.FileHandler.append 
 * specifies whether the FileHandler should append onto any existing files 
 * (defaults to false).</li>
 * </ul> 
 *  For details on the construction of the file name see method
 *  {@link BatchTestRunner#changeFileName(String)}.
 */
public class TCKFileHandler extends Handler {

    private static final String defaultName = "";
    private static final boolean defaultAppend = false;
    private static final Level defaultLevel = Level.ALL;
    private static final Filter defaultFilter = null;
    private static final Formatter defaultFormatter = new SimpleFormatter();
    private static final String defaultEncoding = null;
    
    private String fileName;
    private boolean append;
    
    private Level level;
    private Filter filter;
    private Formatter formatter;
    private String encoding;
    
    private FileHandlerDelegate delegate;
    
    /**
     * @see Handler#Handler()
     * @throws IOException IO exception
     * @throws SecurityException securiyt exception
     */
    public TCKFileHandler() throws IOException, SecurityException {
        configure();
        
        this.delegate = new FileHandlerDelegate();
        
        OutputStream stream = new FileOutputStream(this.fileName, this.append);
        this.delegate.setOutputStream(stream);
        
        this.delegate.setLevel(this.level);
        this.delegate.setFilter(this.filter);
        this.delegate.setFormatter(this.formatter);
        this.delegate.setEncoding(this.encoding);
    }

    /**
     * @see Handler#publish(java.util.logging.LogRecord)
     */
    public synchronized void publish(LogRecord record) {
        this.delegate.publish(record);
    }

    /**
     * @see Handler#close()
     */
    public synchronized void close() throws SecurityException {
        this.delegate.close();
    }

    
    /**
     * @see Handler#flush()
     */
    public void flush() {
        this.delegate.flush();
    }
    
    /**
     * @see Handler#getEncoding()
     */
    @Override
    public String getEncoding() {
        return this.encoding;
    }

    /**
     * @see Handler#getErrorManager()
     */
    @Override
    public ErrorManager getErrorManager() {
        return this.delegate.getErrorManager();
    }
    
    /**
     * @see Handler#getFilter()
     */
    @Override
    public Filter getFilter() {
        return this.filter;
    }
    
    /**
     * @see Handler#getFormatter()
     */
    @Override
    public Formatter getFormatter() {
        return this.formatter;
    }
    
    /**
     * @see Handler#getLevel()
     */
    @Override
    public Level getLevel() {
        return this.level;
    }
    
    /**
     * @see Handler#reportError(java.lang.String, java.lang.Exception, int)
     */
    @Override
    protected void reportError(String msg, Exception ex, int code) {
        this.delegate.reportError(msg, ex, code);
    }
    
    /**
     * @see Handler#setErrorManager(java.util.logging.ErrorManager)
     */
    @Override
    public void setErrorManager(ErrorManager em) {
        this.delegate.setErrorManager(em);
    }

    /**
     * @see StreamHandler#isLoggable(java.util.logging.LogRecord)
     */
    @Override
    public boolean isLoggable(LogRecord record) {
        return this.delegate.isLoggable(record);
    }

    /**
     * Sets the file name fileName property.
     * @param fileName The fileName to set
     */
    protected void setPattern(String fileName) {
        this.fileName = BatchTestRunner.changeFileName(fileName);
    }

    /**
     * Sets the append property.
     * @param append The append to set.
     */
    protected void setAppend(boolean append) {
        this.append = append;
    }

    /**
     * Sets the level property.
     * @param level The level to set.
     */
    @Override
    public void setLevel(Level level) {
        this.level = level;
        if (this.delegate != null) {
            this.delegate.setLevel(level);
        }
    }

    /**
     * Sets the filter property.
     * @param filter The filter to set.
     */
    @Override
    public void setFilter(Filter filter) {
        this.filter = filter;
        if (this.delegate != null) {
            this.delegate.setFilter(filter);
        }
    }

    /**
     * Sets the formatter property.
     * @param formatter The formatter to set.
     */
    @Override
    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
        if (this.delegate != null) {
            this.delegate.setFormatter(formatter);
        }
    }

    /**
     * Sets the encoding property.
     * @param encoding The encoding to set.
     * @throws UnsupportedEncodingException encoding not supported
     * @throws SecurityException security exception
     */
    @Override
    public void setEncoding(String encoding)
        throws SecurityException, UnsupportedEncodingException {
        this.encoding = encoding;
        if (this.delegate != null) {
            this.delegate.setEncoding(encoding);
        }
    }

    /**
     * Sets the delegate.
     * @param delegate The delegate to set.
     */
    protected void setDelegate(FileHandlerDelegate delegate) {
        this.delegate = delegate;
    }
    
    private void configure() {
        LogManager manager = LogManager.getLogManager();
        String className = this.getClass().getName();
        
        setPattern(getStringProperty(manager, className + ".fileName", defaultName));
        setAppend(getBooleanProperty(manager, className + ".append", defaultAppend));

        setLevel(getLevelProperty(manager, className + ".level", defaultLevel));
        setFilter(getFilterProperty(manager, className + ".filter", defaultFilter));
        setFormatter(getFormatterProperty(manager, className + ".formatter", defaultFormatter));
        try {
            setEncoding(getStringProperty(manager, className + ".encoding", defaultEncoding));
        } catch (Exception e) {
            try {
                setEncoding(defaultEncoding);
            } catch (Exception ignored) {
            }
        }
    }
    
    private boolean getBooleanProperty(LogManager manager, String property, 
            boolean defaultValue) {
        boolean result = defaultValue;
        String value = manager.getProperty(property);
        if ( value != null) {
            try {
                result = Boolean.valueOf(value.trim()).booleanValue();
            } catch (Exception ignored) {
            }    
        }
        return result;
    }

    private String getStringProperty(LogManager manager, String property, 
            String defaultValue) {
        String result = defaultValue;
        String value = manager.getProperty(property);
        if ( value != null) {
            result = value.trim();
        }
        return result;
    }

    private Level getLevelProperty(LogManager manager, String property, 
            Level defaultValue) {
        Level result = defaultValue;
        String value = manager.getProperty(property);
        if (value != null) {
            try {
                result = Level.parse(value.trim());
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    private Filter getFilterProperty(LogManager manager, String property, 
            Filter defaultValue) {
        return (Filter) newInstanceForProperty(manager, property, defaultValue);
    }

    private Formatter getFormatterProperty(LogManager manager, String property, 
            Formatter defaultValue) {
        return (Formatter) newInstanceForProperty(manager, property, defaultValue);
    }

    private Object newInstanceForProperty(LogManager manager, String property, 
            Object defaultValue) {
        Object result = defaultValue;
        String value = manager.getProperty(property);
        if (value != null) {
            try {
                Class<?> clazz =
                    ClassLoader.getSystemClassLoader().loadClass(value);
                result = clazz.newInstance();
            } catch (Exception ignored) {
            }
        }
        return result;
    }
    
    /**
     * This class has been defined to make method 
     * {@link Handler#reportError(java.lang.String, java.lang.Exception, int)}
     * accessible in the outer class. 
     */
    private static class FileHandlerDelegate extends StreamHandler {

        @Override
        protected void setOutputStream(OutputStream out) {
            super.setOutputStream(out);
        }

        /**
         * @see Handler#reportError(java.lang.String, java.lang.Exception, int)
         */
        @Override
        protected void reportError(String msg, Exception ex, int code) {
            super.reportError(msg, ex, code);
        }
    }
}
