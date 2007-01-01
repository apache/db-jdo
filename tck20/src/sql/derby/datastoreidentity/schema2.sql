-- Licensed to the Apache Software Foundation (ASF) under one or more
-- contributor license agreements.  See the NOTICE file distributed with
-- this work for additional information regarding copyright ownership.
-- The ASF licenses this file to You under the Apache License, Version 2.0
-- (the "License"); you may not use this file except in compliance with
-- the License.  You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

-- SchemaType: datastore identity

-- Inheritance mapping: 
-- Separate table for each class in the inheritance hierarchy.
-- No tables for abstract classes.
-- Each table contains columns for all fields.
-- Managers, mentors, hradvisors, and employees of the month 
-- are fulltime employees.
-- Only fulltime employees can have insurances, can be project members,
-- and can be project reviewers.
-- Separate phone number type tables for persons, fulltime employees,
-- and parttime employees.
-- See tables "persons", "parttimeemployees", 
-- "fulltimeemployees", "medicalinsurance", "dentalinsurance",
-- "fulltime_employee_phoneno_type", and "parttime_employee_phoneno_type".

connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA datastoreidentity2;
SET SCHEMA datastoreidentity2;

-------------------------
-- company
-------------------------

ALTER TABLE departments DROP CONSTRAINT EMP_MO_FK;
ALTER TABLE project_reviewer DROP CONSTRAINT PR_PROJ_FK;
ALTER TABLE project_reviewer DROP CONSTRAINT PR_REV_FK;
DROP TABLE dentalinsurance;
DROP TABLE medicalinsurance;
DROP TABLE project_reviewer;
DROP TABLE project_member;
DROP TABLE employee_phoneno_type;
DROP TABLE fulltime_employee_phoneno_type;
DROP TABLE parttime_employee_phoneno_type;
DROP TABLE parttimeemployees;
DROP TABLE fulltimeemployees;
DROP TABLE persons;
DROP TABLE projects;
DROP TABLE departments;
DROP TABLE companies;

CREATE TABLE companies (
    DATASTORE_IDENTITY INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    ID INTEGER,
    NAME VARCHAR(32) NOT NULL,
    FOUNDEDDATE VARCHAR(32) NOT NULL,
    ADDRID INTEGER,
    STREET VARCHAR(64),
    CITY VARCHAR(64),
    STATE CHAR(2),
    ZIPCODE CHAR(5),
    COUNTRY VARCHAR(64),
    CONSTRAINT COMPS_PK PRIMARY KEY (DATASTORE_IDENTITY)
);

CREATE TABLE departments (
    DATASTORE_IDENTITY INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    ID INTEGER,
    NAME VARCHAR(32) NOT NULL,
    EMP_OF_THE_MONTH INTEGER,
    COMPANYID INTEGER,
    CONSTRAINT DEPTS_PK PRIMARY KEY (DATASTORE_IDENTITY),
    CONSTRAINT DEPTS_COMPANYID FOREIGN KEY (COMPANYID) 
        REFERENCES companies (DATASTORE_IDENTITY)
);

CREATE TABLE persons (
    DATASTORE_IDENTITY INTEGER NOT NULL,
    PERSONID INTEGER NOT NULL,
    FIRSTNAME VARCHAR(32) NOT NULL,
    LASTNAME VARCHAR(32) NOT NULL,
    MIDDLENAME VARCHAR(32),
    BIRTHDATE VARCHAR(32) NOT NULL,
    ADDRID INTEGER,
    STREET VARCHAR(64),
    CITY VARCHAR(64),
    STATE CHAR(2),
    ZIPCODE CHAR(5),
    COUNTRY VARCHAR(64),
    CONSTRAINT PERS_UK UNIQUE (PERSONID),
    CONSTRAINT PERS_PK PRIMARY KEY (DATASTORE_IDENTITY)
);

CREATE TABLE fulltimeemployees (
    DATASTORE_IDENTITY INTEGER NOT NULL,
    PERSONID INTEGER NOT NULL,
    FIRSTNAME VARCHAR(32) NOT NULL,
    LASTNAME VARCHAR(32) NOT NULL,
    MIDDLENAME VARCHAR(32),
    BIRTHDATE VARCHAR(32) NOT NULL,
    ADDRID INTEGER,
    STREET VARCHAR(64),
    CITY VARCHAR(64),
    STATE CHAR(2),
    ZIPCODE CHAR(5),
    COUNTRY VARCHAR(64),
    HIREDATE VARCHAR(32),
    WEEKLYHOURS DOUBLE,
    DEPARTMENT INTEGER,
    FUNDINGDEPT INTEGER,
    MANAGER INTEGER,
    MENTOR INTEGER,
    HRADVISOR INTEGER,
    SALARY DOUBLE,
    CONSTRAINT FTEMPS_UK UNIQUE (PERSONID),
    CONSTRAINT FTEMPS_PK PRIMARY KEY (DATASTORE_IDENTITY),
    CONSTRAINT FTEMPS_DEPARTMENT FOREIGN KEY (DEPARTMENT) 
        REFERENCES departments (DATASTORE_IDENTITY),
    CONSTRAINT FTEMPS_FUNDINGDEPT FOREIGN KEY (FUNDINGDEPT) 
        REFERENCES departments (DATASTORE_IDENTITY),
    CONSTRAINT FTEMPS_MANAGER FOREIGN KEY (MANAGER) 
        REFERENCES fulltimeemployees (DATASTORE_IDENTITY),
    CONSTRAINT FTEMPS_MENTOR FOREIGN KEY (MENTOR) 
        REFERENCES fulltimeemployees (DATASTORE_IDENTITY),
    CONSTRAINT FTEMPS_HRADVISOR FOREIGN KEY (HRADVISOR) 
        REFERENCES fulltimeemployees (DATASTORE_IDENTITY)
);

CREATE TABLE parttimeemployees (
    DATASTORE_IDENTITY INTEGER NOT NULL,
    PERSONID INTEGER NOT NULL,
    FIRSTNAME VARCHAR(32) NOT NULL,
    LASTNAME VARCHAR(32) NOT NULL,
    MIDDLENAME VARCHAR(32),
    BIRTHDATE VARCHAR(32) NOT NULL,
    ADDRID INTEGER,
    STREET VARCHAR(64),
    CITY VARCHAR(64),
    STATE CHAR(2),
    ZIPCODE CHAR(5),
    COUNTRY VARCHAR(64),
    HIREDATE VARCHAR(32),
    WEEKLYHOURS DOUBLE,
    DEPARTMENT INTEGER,
    FUNDINGDEPT INTEGER,
    MANAGER INTEGER,
    MENTOR INTEGER,
    HRADVISOR INTEGER,
    WAGE DOUBLE,
    CONSTRAINT PTEMPS_UK UNIQUE (PERSONID),
    CONSTRAINT PTEMPS_PK PRIMARY KEY (DATASTORE_IDENTITY),
    CONSTRAINT PTEMPS_DEPARTMENT FOREIGN KEY (DEPARTMENT) 
        REFERENCES departments (DATASTORE_IDENTITY),
    CONSTRAINT PTEMPS_FUNDINGDEPT FOREIGN KEY (FUNDINGDEPT) 
        REFERENCES departments (DATASTORE_IDENTITY),
    CONSTRAINT PTEMPS_MANAGER FOREIGN KEY (MANAGER) 
        REFERENCES parttimeemployees (DATASTORE_IDENTITY),
    CONSTRAINT PTEMPS_MENTOR FOREIGN KEY (MENTOR) 
        REFERENCES parttimeemployees (DATASTORE_IDENTITY),
    CONSTRAINT PTEMPS_HRADVISOR FOREIGN KEY (HRADVISOR) 
        REFERENCES parttimeemployees (DATASTORE_IDENTITY)
);

CREATE TABLE medicalinsurance (
    DATASTORE_IDENTITY INTEGER NOT NULL,
    INSID INTEGER,
    CARRIER VARCHAR(64) NOT NULL,
    EMPLOYEE INTEGER,
    PLANTYPE VARCHAR(8),
    CONSTRAINT MEDINS_PK PRIMARY KEY (DATASTORE_IDENTITY),
    CONSTRAINT MEDINS_EMPLOYEE FOREIGN KEY (EMPLOYEE) 
        REFERENCES fulltimeemployees (DATASTORE_IDENTITY)
);

CREATE TABLE dentalinsurance (
    DATASTORE_IDENTITY INTEGER NOT NULL,
    INSID INTEGER,
    CARRIER VARCHAR(64) NOT NULL,
    EMPLOYEE INTEGER,
    LIFETIME_ORTHO_BENEFIT DECIMAL(22,3),
    CONSTRAINT DENTINS_PK PRIMARY KEY (DATASTORE_IDENTITY),
    CONSTRAINT DENTINS_EMPLOYEE FOREIGN KEY (EMPLOYEE) 
        REFERENCES fulltimeemployees (DATASTORE_IDENTITY)
);

CREATE TABLE projects (
    DATASTORE_IDENTITY INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    PROJID INTEGER NOT NULL,
    NAME VARCHAR(32) NOT NULL,
    BUDGET DECIMAL(11,2) NOT NULL,
    CONSTRAINT PROJS_UK UNIQUE (PROJID),
    CONSTRAINT PROJS_PK PRIMARY KEY (DATASTORE_IDENTITY)
);

CREATE TABLE project_reviewer (
    PROJID INTEGER NOT NULL,
    REVIEWER INTEGER NOT NULL
);

CREATE TABLE project_member (
    PROJID INTEGER NOT NULL,
    MEMBER INTEGER NOT NULL
);

CREATE TABLE employee_phoneno_type (
    EMPID INTEGER NOT NULL,
    PHONENO VARCHAR(16) NOT NULL,
    TYPE VARCHAR(16) NOT NULL,
    CONSTRAINT EMP_PHNO_PERSONS FOREIGN KEY (EMPID) 
        REFERENCES persons (DATASTORE_IDENTITY)
);

CREATE TABLE fulltime_employee_phoneno_type (
    EMPID INTEGER NOT NULL,
    PHONENO VARCHAR(16) NOT NULL,
    TYPE VARCHAR(16) NOT NULL,
    CONSTRAINT FTEMP_PHNO_FTEMP FOREIGN KEY (EMPID) 
        REFERENCES fulltimeemployees (DATASTORE_IDENTITY)
);

CREATE TABLE parttime_employee_phoneno_type (
    EMPID INTEGER REFERENCES parttimeemployees NOT NULL,
    PHONENO VARCHAR(16) NOT NULL,
    TYPE VARCHAR(16) NOT NULL,
    CONSTRAINT PTEMP_PHNO_PTEMP FOREIGN KEY (EMPID) 
        REFERENCES parttimeemployees (DATASTORE_IDENTITY)
);

ALTER TABLE project_reviewer 
    ADD CONSTRAINT PR_PROJ_FK FOREIGN KEY
        (PROJID) REFERENCES projects;

ALTER TABLE project_reviewer 
    ADD CONSTRAINT PR_REV_FK FOREIGN KEY
        (REVIEWER) REFERENCES fulltimeemployees;

ALTER TABLE project_member 
    ADD CONSTRAINT PM_PROJ_FK FOREIGN KEY
        (PROJID) REFERENCES projects;

ALTER TABLE project_member 
    ADD CONSTRAINT PM_MEMB_FK FOREIGN KEY
        (MEMBER) REFERENCES fulltimeemployees;

ALTER TABLE departments 
    ADD CONSTRAINT EMP_MO_FK FOREIGN KEY
        (EMP_OF_THE_MONTH) REFERENCES fulltimeemployees;

disconnect;
