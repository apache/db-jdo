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
-- Each table contains columns for the declared fields.
-- See tables "persons", "employees", "parttimeemployees", 
-- "fulltimeemployees", "insuranceplans", "medicalinsurance", and "dentalinsurance".

connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA applicationidentity1;
SET SCHEMA applicationidentity1;

-------------------------
-- company
-------------------------

ALTER TABLE departments DROP CONSTRAINT EMP_MO_FK;
ALTER TABLE project_reviewer DROP CONSTRAINT PR_PROJ_FK;
ALTER TABLE project_reviewer DROP CONSTRAINT PR_REV_FK;
DROP TABLE dentalinsurance;
DROP TABLE medicalinsurance;
DROP TABLE insuranceplans;
DROP TABLE project_reviewer;
DROP TABLE project_member;
DROP TABLE employee_phoneno_type;
DROP TABLE fulltimeemployees;
DROP TABLE parttimeemployees;
DROP TABLE employees;
DROP TABLE persons;
DROP TABLE projects;
DROP TABLE departments;
DROP TABLE companies;

CREATE TABLE companies (
    ID INTEGER NOT NULL,
    NAME VARCHAR(32) NOT NULL,
    FOUNDEDDATE TIMESTAMP NOT NULL,
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

CREATE TABLE persons (
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
    CONSTRAINT PERS_PK PRIMARY KEY (PERSONID)
);

CREATE TABLE employees (
    PERSONID INTEGER NOT NULL,
    HIREDATE TIMESTAMP,
    WEEKLYHOURS DOUBLE,
    DEPARTMENT INTEGER REFERENCES departments,
    FUNDINGDEPT INTEGER REFERENCES departments,
    MANAGER INTEGER REFERENCES persons,
    MENTOR INTEGER REFERENCES persons,
    HRADVISOR INTEGER REFERENCES persons,
    CONSTRAINT EMPS_PK PRIMARY KEY (PERSONID),
    CONSTRAINT EMPS_FK FOREIGN KEY (PERSONID) REFERENCES persons (PERSONID)
);

CREATE TABLE parttimeemployees (
    PERSONID INTEGER NOT NULL,
    WAGE DOUBLE,
    CONSTRAINT PTEMPS_PK PRIMARY KEY (PERSONID),
    CONSTRAINT PTEMPS_FK FOREIGN KEY (PERSONID) REFERENCES persons (PERSONID)
);

CREATE TABLE fulltimeemployees (
    PERSONID INTEGER NOT NULL,
    SALARY DOUBLE,
    CONSTRAINT FTEMPS_PK PRIMARY KEY (PERSONID),
    CONSTRAINT FTEMPS_FK FOREIGN KEY (PERSONID) REFERENCES persons (PERSONID)
);

CREATE TABLE insuranceplans (
    INSID INTEGER NOT NULL,
    CARRIER VARCHAR(64) NOT NULL,
    EMPLOYEE INTEGER REFERENCES persons,
    CONSTRAINT INS_PK PRIMARY KEY (INSID)
);

CREATE TABLE medicalinsurance (
    INSID INTEGER NOT NULL,
    PLANTYPE VARCHAR(8),
    CONSTRAINT MEDINS_PK PRIMARY KEY (INSID),
    CONSTRAINT MEDINS_FK FOREIGN KEY (INSID) REFERENCES insuranceplans (INSID)
);

CREATE TABLE dentalinsurance (
    INSID INTEGER NOT NULL,
    LIFETIME_ORTHO_BENEFIT DECIMAL(22,3),
    CONSTRAINT DENTINS_PK PRIMARY KEY (INSID),
    CONSTRAINT DENTINS_FK FOREIGN KEY (INSID) REFERENCES insuranceplans (INSID)
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
    MEMBER INTEGER REFERENCES persons NOT NULL
);

CREATE TABLE employee_phoneno_type (
    EMPID INTEGER REFERENCES persons NOT NULL,
    PHONENO VARCHAR(16) NOT NULL,
    TYPE VARCHAR(16) NOT NULL
);

ALTER TABLE project_reviewer 
    ADD CONSTRAINT PR_PROJ_FK FOREIGN KEY
        (PROJID) REFERENCES projects(PROJID);

ALTER TABLE project_reviewer 
    ADD CONSTRAINT PR_REV_FK FOREIGN KEY
        (REVIEWER) REFERENCES persons(PERSONID);

ALTER TABLE departments 
    ADD CONSTRAINT EMP_MO_FK FOREIGN KEY
        (EMP_OF_THE_MONTH) REFERENCES persons(PERSONID) ON DELETE SET NULL;

-------------------------
-- fieldtypes (enum)
-------------------------

DROP TABLE FIELDSOFENUMORDINAL;

CREATE TABLE FIELDSOFENUMORDINAL
(
    IDENTIFIER INTEGER NOT NULL,
    ENUM0 SMALLINT,
    ENUM1 SMALLINT,
    ENUM102 SMALLINT,
    ENUM103 SMALLINT,
    ENUM104 INTEGER,
    ENUM108 INTEGER,
    ENUM109 INTEGER,
    ENUM11 INTEGER,
    ENUM110 BIGINT,
    ENUM112 BIGINT,
    ENUM113 BIGINT,
    ENUM114 BIGINT,
    ENUM119 NUMERIC(2,0),
    ENUM12 NUMERIC(2,0),
    ENUM120 NUMERIC(2,0),
    ENUM121 NUMERIC(2,0),
    ENUM124 SMALLINT,
    ENUM125 SMALLINT,
    ENUM126 SMALLINT,
    ENUM128 SMALLINT,
    ENUM129 INTEGER,
    ENUM13 INTEGER,
    ENUM130 INTEGER,
    ENUM138 INTEGER,
    ENUM139 BIGINT,
    ENUM140 BIGINT,
    ENUM16 BIGINT,
    ENUM17 BIGINT,
    ENUM18 NUMERIC(2,0),
    ENUM2 NUMERIC(2,0),
    ENUM20 NUMERIC(2,0),
    ENUM21 NUMERIC(2,0),
    ENUM22 SMALLINT,
    ENUM30 SMALLINT,
    ENUM31 SMALLINT,
    ENUM32 SMALLINT,
    ENUM36 INTEGER,
    ENUM37 INTEGER,
    ENUM38 INTEGER,
    ENUM4 INTEGER,
    ENUM40 BIGINT,
    ENUM41 BIGINT,
    ENUM42 BIGINT,
    ENUM47 BIGINT,
    ENUM48 NUMERIC(2,0),
    ENUM49 NUMERIC(2,0),
    ENUM5 NUMERIC(2,0),
    ENUM52 NUMERIC(2,0),
    ENUM53 SMALLINT,
    ENUM54 SMALLINT,
    ENUM56 SMALLINT,
    ENUM57 SMALLINT,
    ENUM58 INTEGER,
    ENUM6 INTEGER,
    ENUM66 INTEGER,
    ENUM67 INTEGER,
    ENUM68 BIGINT,
    ENUM72 BIGINT,
    ENUM73 BIGINT,
    ENUM74 BIGINT,
    ENUM76 NUMERIC(2,0),
    ENUM77 NUMERIC(2,0),
    ENUM78 NUMERIC(2,0),
    ENUM83 NUMERIC(2,0),
    ENUM84 SMALLINT,
    ENUM85 SMALLINT,
    ENUM88 SMALLINT,
    ENUM89 SMALLINT,
    ENUM90 INTEGER,
    ENUM92 INTEGER,
    ENUM93 INTEGER,
    ENUM94 INTEGER
);

ALTER TABLE FIELDSOFENUMORDINAL ADD CONSTRAINT
    FIELDSOFENUMORDINAL_PK PRIMARY KEY (IDENTIFIER);

disconnect;
