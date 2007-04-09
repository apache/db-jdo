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
-- Person, Employee, and Insurance have inheritance strategy "new-table". 
-- PartTimeEmployee, FullTimeEmployee, MedicalInsurance, and DentalInsurance
-- have inheritance strategy "superclass-table".
-- See tables "persons", "employees", and "insuranceplans".

connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA datastoreidentity4;
SET SCHEMA datastoreidentity4;

-------------------------
-- company
-------------------------

ALTER TABLE departments DROP CONSTRAINT EMP_MO_FK;
ALTER TABLE project_reviewer DROP CONSTRAINT PR_PROJ_FK;
ALTER TABLE project_reviewer DROP CONSTRAINT PR_REV_FK;
DROP TABLE insuranceplans;
DROP TABLE project_reviewer;
DROP TABLE project_member;
DROP TABLE employee_phoneno_type;
DROP TABLE employees;
DROP TABLE persons;
DROP TABLE projects;
DROP TABLE departments;
DROP TABLE companies;

CREATE TABLE companies (
    DATASTORE_IDENTITY INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    ID INTEGER,
    NAME VARCHAR(32) NOT NULL,
    FOUNDEDDATE TIMESTAMP NOT NULL,
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
    DATASTORE_IDENTITY INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    PERSONID INTEGER NOT NULL,
    FIRSTNAME VARCHAR(32) NOT NULL,
    LASTNAME VARCHAR(32) NOT NULL,
    MIDDLENAME VARCHAR(32),
    BIRTHDATE TIMESTAMP NOT NULL,
    ADDRID INTEGER,
    STREET VARCHAR(64),
    CITY VARCHAR(64),
    STATE CHAR(2),
    ZIPCODE CHAR(5),
    COUNTRY VARCHAR(64),
    CONSTRAINT PERS_PK PRIMARY KEY (DATASTORE_IDENTITY),
    CONSTRAINT PERS_UK UNIQUE (PERSONID)
);

CREATE TABLE employees (
    DATASTORE_IDENTITY INTEGER NOT NULL,
    HIREDATE TIMESTAMP,
    WEEKLYHOURS DOUBLE,
    DEPARTMENT INTEGER,
    FUNDINGDEPT INTEGER,
    MANAGER INTEGER,
    MENTOR INTEGER,
    HRADVISOR INTEGER,
    WAGE DOUBLE,
    SALARY DOUBLE,
    DISCRIMINATOR varchar(64) NOT NULL,
    CONSTRAINT EMPS_PK PRIMARY KEY (DATASTORE_IDENTITY),
    CONSTRAINT EMPS_FK FOREIGN KEY (DATASTORE_IDENTITY) 
        REFERENCES persons (DATASTORE_IDENTITY),
    CONSTRAINT EMPS_DEPARTMENT FOREIGN KEY (DEPARTMENT) 
        REFERENCES departments (DATASTORE_IDENTITY),
    CONSTRAINT EMPS_FUNDINGDEPT FOREIGN KEY (FUNDINGDEPT) 
        REFERENCES departments (DATASTORE_IDENTITY),
    CONSTRAINT EMPS_MANAGER FOREIGN KEY (MANAGER) 
        REFERENCES persons (DATASTORE_IDENTITY),
    CONSTRAINT EMPS_MENTOR FOREIGN KEY (MENTOR) 
        REFERENCES persons (DATASTORE_IDENTITY),
    CONSTRAINT EMPS_HRADVISOR FOREIGN KEY (HRADVISOR) 
        REFERENCES persons (DATASTORE_IDENTITY)
);

CREATE TABLE insuranceplans (
    DATASTORE_IDENTITY INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    INSID INTEGER,
    CARRIER VARCHAR(64) NOT NULL,
    EMPLOYEE INTEGER,
    PLANTYPE VARCHAR(8),
    LIFETIME_ORTHO_BENEFIT DECIMAL(22,3),
    DISCRIMINATOR varchar(64) NOT NULL,
    CONSTRAINT INS_PK PRIMARY KEY (DATASTORE_IDENTITY),
    CONSTRAINT INS_EMPLOYEE FOREIGN KEY (EMPLOYEE) 
        REFERENCES persons (DATASTORE_IDENTITY)
);

CREATE TABLE projects (
    DATASTORE_IDENTITY INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    PROJID INTEGER NOT NULL,
    NAME VARCHAR(32) NOT NULL,
    BUDGET DECIMAL(11,2) NOT NULL,
    CONSTRAINT PROJS_PK PRIMARY KEY (DATASTORE_IDENTITY),
    CONSTRAINT PROJS_UK UNIQUE (PROJID)
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
    CONSTRAINT employee_phoneno_type_EMPID FOREIGN KEY (EMPID) 
        REFERENCES persons (DATASTORE_IDENTITY)
);

ALTER TABLE project_reviewer 
    ADD CONSTRAINT PR_PROJ_FK FOREIGN KEY
        (PROJID) REFERENCES projects;

ALTER TABLE project_reviewer 
    ADD CONSTRAINT PR_REV_FK FOREIGN KEY
        (REVIEWER) REFERENCES persons;

ALTER TABLE project_member 
    ADD CONSTRAINT PM_PROJ_FK FOREIGN KEY
        (PROJID) REFERENCES projects;

ALTER TABLE project_member 
    ADD CONSTRAINT PM_MEMB_FK FOREIGN KEY
        (MEMBER) REFERENCES persons;

ALTER TABLE departments 
    ADD CONSTRAINT EMP_MO_FK FOREIGN KEY
        (EMP_OF_THE_MONTH) REFERENCES persons;

disconnect;
