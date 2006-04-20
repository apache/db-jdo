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

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.jdo.tck.util.ConversionHelper;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;

/**
 * Utility class to create a graph of company model instances from an xml
 * representation. 
 *
 * @author Michael Bouschen
 */
public class CompanyModelReader extends XmlBeanFactory {

    /** The format of date values in the xml representation */
    public static final String DATE_PATTERN = "d/MMM/yyyy";

    /** The name of the root list bean. */
    public static final String ROOT_LIST_NAME = "root";

    /** The bean-factory name in the xml input files.
     */
    public static final String BEAN_FACTORY_NAME = "companyFactory";

    /** The company factory instance. */
    private CompanyFactory companyFactory;

    /** 
     * Create a CompanyModelReader for the specified resourceName. 
     * @param resourceName the name of the resource
     */
    public CompanyModelReader(String resourceName) {
        // Use the class loader of the Company class to find the resource
        this(resourceName, Company.class.getClassLoader());
    }

    /** 
     * Create a CompanyModelReader for the specified resourceName. 
     * @param resourceName the name of the resource
     */
    public CompanyModelReader(String resourceName, ClassLoader classLoader) {
        super(new ClassPathResource(resourceName, classLoader));
        configureFactory();
    }

    /**
     * Create a CompanyModelReader for the specified InputStream.
     * @param stream the input stream
     */
    public CompanyModelReader(InputStream stream) {
        super(new InputStreamResource(stream));
        configureFactory();
    }

    /** 
     * Returns a list of root objects. The method expects to find a bean
     * called "root" of type list in the xml and returns it.
     * @return a list of root instances
     */
    public List getRootList() {
        return (List)getBean(ROOT_LIST_NAME);
    }
    
    /** 
     * Configure the CompanyModelReader, e.g. register CustomEditor classes
     * to convert the string representation of a property into an instance
     * of the right type.
     */
    private void configureFactory() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN, Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        CustomDateEditor dateEditor = 
            new CustomDateEditor(formatter, true);
        registerCustomEditor(Date.class, dateEditor);
        companyFactory = CompanyFactoryRegistry.getInstance();
        addSingleton(BEAN_FACTORY_NAME, companyFactory);
    }
    
    // Convenience methods

    /** 
     * Convenience method returning an Address instance for the specified
     * name. The method returns <code>null</code> if there is no Address
     * bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * Address bean.
     */
    public Address getAddress(String name) {
        return (Address)getBean(name, Address.class);
    }

    /** 
     * Convenience method returning a Company instance for the specified 
     * name. The method returns <code>null</code> if there is no Company
     * bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * Company bean.
     */
    public Company getCompany(String name) {
        return (Company)getBean(name, Company.class);
    }

    /** 
     * Convenience method returning a DentalInsurance instance for the
     * specified name. The method returns <code>null</code> if there is no
     * DentalInsurance bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * DentalInsurance bean.
     */
    public DentalInsurance getDentalInsurance(String name) {
        return (DentalInsurance)getBean(name, DentalInsurance.class);
    }

    /** 
     * Convenience method returning a Department instance for the specified
     * name. The method returns <code>null</code> if there is no Department
     * bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * Department bean.
     */
    public Department getDepartment(String name) {
        return (Department)getBean(name, Department.class);
    }

    /** 
     * Convenience method returning an Employee instance for the specified
     * name. The method returns <code>null</code> if there is no Employee
     * bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * Employee bean.
     */
    public Employee getEmployee(String name) {
        return (Employee)getBean(name, Employee.class);
    }

    /** 
     * Convenience method returning a FullTimeEmployee instance for the
     * specified name. The method returns <code>null</code> if there is no
     * FullTimeEmployee bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * FullTimeEmployee bean.
     */
    public FullTimeEmployee getFullTimeEmployee(String name) {
        return (FullTimeEmployee)getBean(name, FullTimeEmployee.class);
    }

    /** 
     * Convenience method returning an Insurance instance for the specified
     * name. The method returns <code>null</code> if there is no Insurance
     * bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * Insurance bean.
     */
    public Insurance getInsurance(String name) {
        return (Insurance)getBean(name, Insurance.class);
    }

    /** 
     * Convenience method returning a MedicalInsurance instance for the
     * specified name. The method returns <code>null</code> if there is no
     * MedicalInsurance bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * MedicalInsurance bean.
     */
    public MedicalInsurance getMedicalInsurance(String name) {
        return (MedicalInsurance)getBean(name, MedicalInsurance.class);
    }

    /** 
     * Convenience method returning a PartTimeEmployee instance for the
     * specified name. The method returns <code>null</code> if there is no
     * PartTimeEmployee bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * PartTimeEmployee bean.
     */
    public PartTimeEmployee getPartTimeEmployee(String name) {
        return (PartTimeEmployee)getBean(name, PartTimeEmployee.class);
    }

    /** 
     * Convenience method returning a Person instance for the specified
     * name. The method returns <code>null</code> if there is no Person
     * bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * Person bean.
     */
    public Person getPerson(String name) {
        return (Person)getBean(name, Person.class);
    }

    /** 
     * Convenience method returning a Project instance for the specified
     * name. The method returns <code>null</code> if there is no Project
     * bean with the specified name. 
     * @param name the name of the bean to return.
     * @return the instance of the bean or <code>null</code> if there no
     * Project bean.
     */
    public Project getProject(String name) {
        return (Project)getBean(name, Project.class);
    }
    
    /**
     * @return Returns the tearDownClasses.
     */
    public Class[] getTearDownClassesFromFactory() {
        return companyFactory.getTearDownClasses();
    }
    
    /**
     * @return Returns the tearDownClasses.
     */
    public static Class[] getTearDownClasses() {
        return CompanyFactoryConcreteClass.tearDownClasses;
    }

    public static Date stringToUtilDate(String value) {
        return ConversionHelper.toUtilDate(DATE_PATTERN, "America/New_York", Locale.US, value);
    }
}

