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

-- SchemaType: application identity

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

-- connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA applicationidentity2;
SET SCHEMA applicationidentity2;

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
DROP TABLE meetingrooms;
DROP TABLE department_rooms;

CREATE TABLE companies (
    ID INTEGER NOT NULL,
    NAME VARCHAR(32) NOT NULL,
    FOUNDEDDATE DATE NOT NULL,
    ADDRID INTEGER,
    STREET VARCHAR(64),
    CITY VARCHAR(64),
    STATE CHAR(2),
    ZIPCODE CHAR(5),
    COUNTRY VARCHAR(64),
    CONSTRAINT COMPS_PK PRIMARY KEY (ID)
);

CREATE TABLE departments (
    ID INTEGER NOT NULL,
    NAME VARCHAR(32) NOT NULL,
    EMP_OF_THE_MONTH INTEGER,
    COMPANYID INTEGER REFERENCES companies,
    CONSTRAINT DEPTS_PK PRIMARY KEY (ID)
);

CREATE TABLE department_rooms (
    DEPTID INTEGER NOT NULL,
    ROOMID INTEGER NOT NULL,
    ROOM_ORDER INTEGER NOT NULL
);

CREATE TABLE meetingrooms (
    ID INTEGER NOT NULL,
    NAME VARCHAR(32) NOT NULL,
    CONSTRAINT ROOMS_PK PRIMARY KEY (ID)
);

CREATE TABLE persons (
    PERSONID INTEGER NOT NULL,
    FIRSTNAME VARCHAR(32) NOT NULL,
    LASTNAME VARCHAR(32) NOT NULL,
    MIDDLENAME VARCHAR(32),
    BIRTHDATE DATE NOT NULL,
    ADDRID INTEGER,
    STREET VARCHAR(64),
    CITY VARCHAR(64),
    STATE CHAR(2),
    ZIPCODE CHAR(5),
    COUNTRY VARCHAR(64),
    CONSTRAINT PERS_PK PRIMARY KEY (PERSONID)
);

CREATE TABLE fulltimeemployees (
    PERSONID INTEGER NOT NULL,
    FIRSTNAME VARCHAR(32) NOT NULL,
    LASTNAME VARCHAR(32) NOT NULL,
    MIDDLENAME VARCHAR(32),
    BIRTHDATE DATE NOT NULL,
    ADDRID INTEGER,
    STREET VARCHAR(64),
    CITY VARCHAR(64),
    STATE CHAR(2),
    ZIPCODE CHAR(5),
    COUNTRY VARCHAR(64),
    HIREDATE DATE,
    WEEKLYHOURS DOUBLE,
    DEPARTMENT INTEGER REFERENCES departments,
    FUNDINGDEPT INTEGER REFERENCES departments,
    MANAGER INTEGER REFERENCES fulltimeemployees,
    MENTOR INTEGER REFERENCES fulltimeemployees,
    HRADVISOR INTEGER REFERENCES fulltimeemployees,
    SALARY DOUBLE,
    CONSTRAINT FTEMPS_PK PRIMARY KEY (PERSONID)
);

CREATE TABLE parttimeemployees (
    PERSONID INTEGER NOT NULL,
    FIRSTNAME VARCHAR(32) NOT NULL,
    LASTNAME VARCHAR(32) NOT NULL,
    MIDDLENAME VARCHAR(32),
    BIRTHDATE DATE NOT NULL,
    ADDRID INTEGER,
    STREET VARCHAR(64),
    CITY VARCHAR(64),
    STATE CHAR(2),
    ZIPCODE CHAR(5),
    COUNTRY VARCHAR(64),
    HIREDATE DATE,
    WEEKLYHOURS DOUBLE,
    DEPARTMENT INTEGER REFERENCES departments,
    FUNDINGDEPT INTEGER REFERENCES departments,
    MANAGER INTEGER REFERENCES fulltimeemployees,
    MENTOR INTEGER REFERENCES fulltimeemployees,
    HRADVISOR INTEGER REFERENCES fulltimeemployees,
    WAGE DOUBLE,
    CONSTRAINT PTEMPS_PK PRIMARY KEY (PERSONID)
);

CREATE TABLE medicalinsurance (
    INSID INTEGER NOT NULL,
    CARRIER VARCHAR(64) NOT NULL,
    EMPLOYEE INTEGER REFERENCES fulltimeemployees,
    PLANTYPE VARCHAR(8),
    CONSTRAINT MEDINS_PK PRIMARY KEY (INSID)
);

CREATE TABLE dentalinsurance (
    INSID INTEGER NOT NULL,
    CARRIER VARCHAR(64) NOT NULL,
    EMPLOYEE INTEGER REFERENCES fulltimeemployees,
    LIFETIME_ORTHO_BENEFIT DECIMAL(22,3),
    CONSTRAINT DENTINS_PK PRIMARY KEY (INSID)
);

CREATE TABLE projects (
    PROJID INTEGER NOT NULL,
    NAME VARCHAR(32) NOT NULL,
    BUDGET DECIMAL(11,2) NOT NULL,
    CONSTRAINT PROJS_PK PRIMARY KEY (PROJID)
);

CREATE TABLE project_reviewer (
    PROJID INTEGER NOT NULL,
    REVIEWER INTEGER NOT NULL
);

CREATE TABLE project_member (
    PROJID INTEGER REFERENCES projects NOT NULL,
    MEMBER INTEGER REFERENCES fulltimeemployees NOT NULL
);

CREATE TABLE employee_phoneno_type (
    EMPID INTEGER REFERENCES persons NOT NULL,
    PHONENO VARCHAR(16) NOT NULL,
    TYPE VARCHAR(16) NOT NULL
);

CREATE TABLE fulltime_employee_phoneno_type (
    EMPID INTEGER REFERENCES fulltimeemployees NOT NULL,
    PHONENO VARCHAR(16) NOT NULL,
    TYPE VARCHAR(16) NOT NULL
);

CREATE TABLE parttime_employee_phoneno_type (
    EMPID INTEGER REFERENCES parttimeemployees NOT NULL,
    PHONENO VARCHAR(16) NOT NULL,
    TYPE VARCHAR(16) NOT NULL
);

ALTER TABLE project_reviewer 
    ADD CONSTRAINT PR_PROJ_FK FOREIGN KEY
        (PROJID) REFERENCES projects(PROJID);

ALTER TABLE project_reviewer 
    ADD CONSTRAINT PR_REV_FK FOREIGN KEY
        (REVIEWER) REFERENCES fulltimeemployees(PERSONID);

ALTER TABLE departments 
    ADD CONSTRAINT EMP_MO_FK FOREIGN KEY
        (EMP_OF_THE_MONTH) REFERENCES fulltimeemployees(PERSONID) ON DELETE SET NULL;

-- disconnect;
