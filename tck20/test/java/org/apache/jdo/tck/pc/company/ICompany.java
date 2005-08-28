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

package org.apache.jdo.tck.pc.company;

import java.util.Date;
import java.util.Set;

/**
 * This interface represents the persistent state of Company.
 * Javadoc was deliberately omitted because it would distract from
 * the purpose of the interface.
 */
public interface ICompany {
    
    IAddress getAddress();
    long getCompanyid();
    Set getDepartments();
    Date getFounded();
    String getName();
    
    void setAddress(IAddress a);
    void setCompanyid(long id);
    void setDepartments(Set depts);
    void setFounded(Date date);
    void setName(String string);
}
