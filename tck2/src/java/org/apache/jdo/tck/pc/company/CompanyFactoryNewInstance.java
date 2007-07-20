/*
 * CompanyFactoryNewInstance.java
 *
 * Created on July 19, 2007, 2:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.apache.jdo.tck.pc.company;

/**
 *
 * @author michelle
 */
public interface CompanyFactoryNewInstance {
    
    abstract public IAddress newAddress();
    abstract public ICompany newCompany();
    abstract public IDentalInsurance newDentalInsurance();
    abstract public IDepartment newDepartment();
    abstract public IFullTimeEmployee newFullTimeEmployee();
    abstract public IMedicalInsurance newMedicalInsurance();
    abstract public IPartTimeEmployee newPartTimeEmployee();
    abstract public IProject newProject();
    Class[] getTearDownClasses();    
}
