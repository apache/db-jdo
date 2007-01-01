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


package org.apache.jdo.impl.enhancer.meta.util;

import org.apache.jdo.impl.enhancer.meta.EnhancerMetaData;
import org.apache.jdo.impl.enhancer.meta.EnhancerMetaDataFatalError;
import org.apache.jdo.impl.enhancer.meta.EnhancerMetaDataUserException;
import org.apache.jdo.impl.enhancer.util.Support;




public final class EnhancerMetaDataTimer
    extends Support
    implements EnhancerMetaData
{
    // delegate
    final protected EnhancerMetaData delegate;

    /**
     * Creates an instance.
     */
    public EnhancerMetaDataTimer(EnhancerMetaData delegate)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        affirm(delegate);
        this.delegate = delegate;
    }

    public String getDeclaringClass(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.getDeclaringClass(String,String)",
                       "EnhancerMetaData.getDeclaringClass(" + classPath 
                       + ", " + fieldName + ")");
            return delegate.getDeclaringClass(classPath, fieldName);
        } finally {
            timer.pop();
        }
    }

    public void declareField(String classPath,
                             String fieldName,
                             String signature)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.declareField(String,String,String)",
                       "EnhancerMetaData.declareField(" + classPath
                       + ", " + fieldName + ", " + signature + ")");
            delegate.declareField(classPath, fieldName, signature);
        } finally {
            timer.pop();
        }
    }
    
    public boolean isPersistenceCapableClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.isPersistenceCapableClass(String)",
                       "EnhancerMetaData.isPersistenceCapableClass(" + classPath + ")");
            return delegate.isPersistenceCapableClass(classPath);
        } finally {
            timer.pop();
        }
    }

    public boolean isSerializableClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.isSerializableClass(String)",
                       "EnhancerMetaData.isSerializableClass(" + classPath + ")");
            return delegate.isSerializableClass(classPath);
        } finally {
            timer.pop();
        }
    }

    public boolean isKnownUnenhancableClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.isKnownUnenhancableClass(String)",
                       "EnhancerMetaData.isKnownUnenhancableClass(" + classPath + ")");
            return delegate.isKnownUnenhancableClass(classPath);
        } finally {
            timer.pop();
        }
    }

    public boolean isPersistenceCapableRootClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.isPersistenceCapableRootClass(String)",
                       "EnhancerMetaData.isPersistenceCapableRootClass(" + classPath + ")");
            return delegate.isPersistenceCapableRootClass(classPath);
        } finally {
            timer.pop();
        }
    }

    public String getPersistenceCapableRootClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.getPersistenceCapableRootClass(String)",
                       "EnhancerMetaData.getPersistenceCapableRootClass(" + classPath + ")");
            return delegate.getPersistenceCapableRootClass(classPath);
        } finally {
            timer.pop();
        }
    }

    public String getPersistenceCapableSuperClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.getPersistenceCapableSuperClass(String)",
                       "EnhancerMetaData.getPersistenceCapableSuperClass(" + classPath + ")");
            return delegate.getPersistenceCapableSuperClass(classPath);
        } finally {
            timer.pop();
        }
    }

    public String getKeyClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.getKeyClass(String)",
                       "EnhancerMetaData.getKeyClass(" + classPath + ")");
            return delegate.getKeyClass(classPath);
        } finally {
            timer.pop();
        }
    }

    public String getSuperKeyClass(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.getSuperKeyClass(String)",
                       "EnhancerMetaData.getSuperKeyClass(" + classPath + ")");
            return delegate.getSuperKeyClass(classPath);
        } finally {
            timer.pop();
        }
    }

    public boolean isKnownNonManagedField(String classPath,
                                          String fieldName,
                                          String fieldSig)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.isKnownNonManagedField(String,String,String)",
                       "EnhancerMetaData.isKnownNonManagedField(" + classPath
                       + ", " + fieldName + ", " + fieldSig + ")");
            return delegate.isKnownNonManagedField(classPath,
                                                   fieldName, fieldSig);
        } finally {
            timer.pop();
        }
    }

    public boolean isManagedField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.isManagedField(String,String)",
                       "EnhancerMetaData.isManagedField(" + classPath
                       + ", " + fieldName + ")");
            return delegate.isManagedField(classPath, fieldName);
        } finally {
            timer.pop();
        }
    }

    public boolean isPersistentField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.isPersistentField(String,String)",
                       "EnhancerMetaData.isPersistentField(" + classPath
                       + ", " + fieldName + ")");
            return delegate.isPersistentField(classPath, fieldName);
        } finally {
            timer.pop();
        }
    }

    public boolean isTransactionalField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.isTransactionalField(String,String)",
                       "EnhancerMetaData.isTransactionalField(" + classPath
                       + ", " + fieldName + ")");
            return delegate.isTransactionalField(classPath, fieldName);
        } finally {
            timer.pop();
        }
    }

    public boolean isKeyField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.isKeyField(String,String)",
                       "EnhancerMetaData.isKeyField(" + classPath
                       + ", " + fieldName + ")");
            return delegate.isKeyField(classPath, fieldName);
        } finally {
            timer.pop();
        }
    }

    public boolean isDefaultFetchGroupField(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.isDefaultFetchGroupField(String,fieldName)",
                       "EnhancerMetaData.isDefaultFetchGroupField(" + classPath
                       + ", " + fieldName + ")");
            return delegate.isDefaultFetchGroupField(classPath, fieldName);
        } finally {
            timer.pop();
        }
    }

    public int getFieldFlags(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.getFieldFlags(String, String)",
                       "EnhancerMetaData.getFieldFlags(" + classPath
                       + ", " + fieldName + ")");
            return delegate.getFieldFlags(classPath, fieldName);
        } finally {
            timer.pop();
        }
    }

    public int getFieldNumber(String classPath, String fieldName)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.getFieldNumber(String, String)",
                       "EnhancerMetaData.getFieldNumber(" + classPath
                       + ", " + fieldName + ")");
            return delegate.getFieldNumber(classPath, fieldName);
        } finally {
            timer.pop();
        }
    }

    public String[] getManagedFields(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.getManagedFields(String)",
                       "EnhancerMetaData.getmanagedFields(" + classPath + ")");
            return delegate.getManagedFields(classPath);
        } finally {
            timer.pop();
        }
    }

    public String[] getKeyFields(String classPath)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.getKeyFields(String)",
                       "EnhancerMetaData.getKeyFields(" + classPath + ")");
            return delegate.getKeyFields(classPath);
        } finally {
            timer.pop();
        }
    }


    public int[] getFieldFlags(String classPath, String[] fieldNames)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.getFieldFlags(String, String[])",
                       "EnhancerMetaData.getFieldFlags(" + classPath + ")");
            return delegate.getFieldFlags(classPath, fieldNames);
        } finally {
            timer.pop();
        }
    }


    public int[] getFieldNumber(String classPath, String[] fieldNames)
        throws EnhancerMetaDataUserException, EnhancerMetaDataFatalError
    {
        try {
            timer.push("EnhancerMetaData.getFieldNumber(String, String[])",
                       "EnhancerMetaData.getFieldNumber(" + classPath + ")");
            return delegate.getFieldNumber(classPath, fieldNames);
        } finally {
            timer.pop();
        }
    }
}
