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

package org.apache.jdo.impl.enhancer.core;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;

import org.apache.jdo.impl.enhancer.classfile.ClassAttribute;
import org.apache.jdo.impl.enhancer.classfile.ClassFile;
import org.apache.jdo.impl.enhancer.classfile.GenericAttribute;
import org.apache.jdo.impl.enhancer.util.Support;




/**
 * Controls the enhancement of a class.
 */
public final class Controller
    extends Support
    implements EnhancerConstants
{
    /**
     * Repository for enhancer options.
     */
    private final Environment env;

    /**
     * The classfile to be enhanced.
     */
    private final ClassFile classFile;

    /**
     * The class name in user ('.' delimited) form.
     */
    private final String userClassName;

    /**
     * The analyzer for this class.
     */
    private final Analyzer analyzer;

    /**
     * The augmentation controller for this class.
     */
    private final Augmenter augmenter;

    /**
     * The method annotation controller for this class.
     */
    private final Annotater annotater;

    /**
     * If true, this class is believed to have been modified in some way.
     */
    private boolean classUpdated = false;

    /**
     * Constructor.
     */
    public Controller(ClassFile classFile,
                      Environment env)
    {
        affirm(classFile != null);
        affirm(env != null);

        this.classFile = classFile;
        this.userClassName = classFile.userClassName();
        this.env = env;
        this.analyzer = new Analyzer(this, env);
        this.augmenter = new Augmenter(this, analyzer, env);
        this.annotater = new Annotater(this, analyzer, env);

        affirm(userClassName != null);
        affirm(analyzer != null);
        affirm(augmenter != null);
        affirm(annotater != null);
    }

    // ------------------------------------------------------------

    /**
     * Returns the class file which we are operating on.
     */
    public ClassFile getClassFile()
    {
        return classFile;
    }

    /**
     * Returns true if the classfile has been updated.
     */
    public boolean updated()
    {
        return classUpdated;
    }

    /**
     * Records a modification of the class.
     */
    void noteUpdate()
    {
        classUpdated = true;
    }

    // ------------------------------------------------------------

    /**
     * Determines what modifications are needed and perform them.
     */
    public void enhanceClass()
    {
        try{
            if (env.doTimingStatistics()) {
                Support.timer.push("Controller.enhanceClass()");
            }

            // examine classes
            scan();

            if (env.errorCount() > 0)
                return;

            // augment class
            augment();

            if (env.errorCount() > 0)
                return;

            // annotate class
            annotate();                

            if (env.errorCount() > 0)
                return;

            update();
        } finally {
            if (env.doTimingStatistics()) {
                Support.timer.pop();
            }
        }
    }

    // ------------------------------------------------------------

    /**
     * Notes the class characteristics.
     */
    private void scan()
    {
        if (analyzer.isAnalyzed()) {
            return;
        }

        try {
            if (env.doTimingStatistics()) {
                Support.timer.push("Controller.scan()");
            }

            if (env.dumpClass()) {
                dumpClass();
            }

            analyzer.scan();
        } finally {
            if (env.doTimingStatistics()) {
                Support.timer.pop();
            }
        }
    }

    /**
     * Performs necessary augmentation actions on the class.
     */
    private void augment()
    {
        if (!analyzer.isAugmentable() || env.noAugment()) {
            return;
        }

        try{
            if (env.doTimingStatistics()) {
                Support.timer.push("Controller.augment()");
            }
            augmenter.augment();

            if (env.dumpClass()) {
                dumpClass();
            }
        } finally {
            if (env.doTimingStatistics()) {
                Support.timer.pop();
            }
        }
    }

    /**
     * Performs necessary annotation actions on the class.
     */
    private void annotate()
    {
        if (!analyzer.isAnnotateable() || env.noAnnotate()) {
            return;
        }

        try{
            if (env.doTimingStatistics()) {
                Support.timer.push("Controller.annotate()");
            }
            annotater.annotate();

            if (env.dumpClass()) {
                dumpClass();
            }
        } finally {
            if (env.doTimingStatistics()) {
                Support.timer.pop();
            }
        }
    }

    /**
     * Marks the class being enhanced.
     */
    private void update()
    {
        if (!classUpdated) {
            return;
        }
    
        affirm((analyzer.isAugmentable() && !env.noAugment())
               || (analyzer.isAnnotateable() && !env.noAnnotate()));

        //^olsen: move non-modifying code to Analyzer
        final byte[] data = new byte[2];
        data[0] = (byte)(SUNJDO_PC_EnhancedVersion >>> 8);
        data[1] = (byte)(SUNJDO_PC_EnhancedVersion & 0xff);
        final ClassAttribute annotatedAttr
            = new GenericAttribute(
                classFile.pool().addUtf8(SUNJDO_PC_EnhancedAttribute),
                data);
        classFile.attributes().addElement(annotatedAttr);
    }
    
    /**
     * Dumps a class' signature and byte-code (for debugging).
     */
    private void dumpClass()
    {
        final ByteArrayOutputStream bs = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(bs);
        env.messageNL("dumping class " + userClassName + " {");
        classFile.print(ps);
        env.getOutputWriter().println(bs.toString());
        env.messageNL("} // end of class " + userClassName);
    }
}
