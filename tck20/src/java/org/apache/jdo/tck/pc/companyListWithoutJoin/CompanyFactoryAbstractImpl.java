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

package org.apache.jdo.tck.pc.companyListWithoutJoin;

import javax.jdo.PersistenceManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * This class provides an implementation of CompanyFactory that sets all
 * of the properties of the instance and defines abstract methods to
 * construct the instance itself. It is intended to be subclassed by
 * classes that implement only the methods to construct the instance.
 */
public abstract class CompanyFactoryAbstractImpl implements CompanyFactory {
    
    protected PersistenceManager pm;
    
    /** Logger */
    protected Log logger = 
        LogFactory.getFactory().getInstance("org.apache.jdo.tck");

    /** true if debug logging is enabled. */
    protected boolean debug = logger.isDebugEnabled();
    
    /** Creates a new instance of CompanyFactoryAbstractImpl */
    public CompanyFactoryAbstractImpl(PersistenceManager pm) {
        this.pm = pm;
    }

    abstract ICompany newCompany();
    abstract IDepartment newDepartment();
    abstract IFullTimeEmployee newFullTimeEmployee();
    abstract IPartTimeEmployee newPartTimeEmployee();
    
    public ICompany newCompany(long companyid, String name,
            java.util.Date founded) {
        ICompany result = newCompany();
        if (debug) logger.debug("newCompany returned" + result);
        result.setCompanyid(companyid);
        result.setName(name);
        result.setFounded(founded);
        return result;
    }

    public IDepartment newDepartment(long deptid, String name) {
        IDepartment result = newDepartment();
        if (debug) logger.debug("newDepartment returned" + result);
        result.setDeptid(deptid);
        result.setName(name);
        return result;
    }


    public IDepartment newDepartment(long deptid, String name, ICompany company) {
        IDepartment result = newDepartment();
        if (debug) logger.debug("newDepartment returned" + result);
        result.setDeptid(deptid);
        result.setName(name);
        result.setCompany(company);
        return result;
    }

    public IFullTimeEmployee newFullTimeEmployee(long personid, String first,
            String last, String middle, java.util.Date born,
                    java.util.Date hired, double sal) {
        IFullTimeEmployee result = newFullTimeEmployee();
        if (debug) logger.debug("newFullTimeEmployee returned" + result);
        result.setPersonid(personid);
        result.setFirstname(first);
        result.setLastname(last);
        result.setMiddlename(middle);
        result.setBirthdate(born);
        result.setHiredate(hired);
        result.setSalary(sal);
        return result;
    }

    public IPartTimeEmployee newPartTimeEmployee(long personid, String first, 
            String last, String middle, java.util.Date born,
                    java.util.Date hired, double wage) {
        IPartTimeEmployee result = newPartTimeEmployee();
        if (debug) logger.debug("newPartTimeEmployee returned" + result);
        result.setPersonid(personid);
        result.setFirstname(first);
        result.setLastname(last);
        result.setMiddlename(middle);
        result.setBirthdate(born);
        result.setHiredate(hired);
        result.setWage(wage);
        return result;
    }

}
