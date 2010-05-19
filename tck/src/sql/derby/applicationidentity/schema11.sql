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

connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA applicationidentity11;
SET SCHEMA applicationidentity11;

-------------------------
-- company
-------------------------

ALTER TABLE comp_depts DROP CONSTRAINT EMP_MO_FK;
ALTER TABLE comp_depts DROP CONSTRAINT DEPTS_COMP_FK;
ALTER TABLE project_reviewer DROP CONSTRAINT PR_PROJ_FK;
ALTER TABLE project_reviewer DROP CONSTRAINT PR_REV_FK;
ALTER TABLE persons DROP CONSTRAINT PERS_DEPT_FK;
ALTER TABLE persons DROP CONSTRAINT PERS_FUNDDEPT_FK;
ALTER TABLE persons DROP CONSTRAINT PERS_MANAGER_FK;
ALTER TABLE persons DROP CONSTRAINT PERS_MENTOR_FK;
ALTER TABLE persons DROP CONSTRAINT PERS_HRADVISOR_FK;
ALTER TABLE insuranceplans DROP CONSTRAINT INS_EMP_FK;
DROP TABLE insuranceplans;
DROP TABLE project_reviewer;
DROP TABLE project_member;
DROP TABLE employee_phoneno_type;
DROP TABLE persons;
DROP TABLE projects;
DROP TABLE comp_depts;
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
    DISCRIMINATOR VARCHAR(255),
    CONSTRAINT COMPS_PK PRIMARY KEY (ID)
);

CREATE TABLE comp_depts (
    ID INTEGER NOT NULL,
    NAME VARCHAR(32) NOT NULL,
    EMP_OF_THE_MONTH INTEGER,
    COMPANYID INTEGER,
    DISCRIMINATOR VARCHAR(255),
    CONSTRAINT DEPTS_COMP_FK FOREIGN KEY (COMPANYID) REFERENCES companies,
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
    HIREDATE TIMESTAMP,
    WEEKLYHOURS REAL,
    DEPARTMENT INTEGER,
    FUNDINGDEPT INTEGER,
    MANAGER INTEGER,
    MENTOR INTEGER,
    HRADVISOR INTEGER,
    SALARY REAL,
    WAGE REAL,
    DISCRIMINATOR varchar(255) NOT NULL,
    CONSTRAINT PERS_DEPT_FK FOREIGN KEY (DEPARTMENT) REFERENCES comp_depts,
    CONSTRAINT PERS_FUNDDEPT_FK FOREIGN KEY (FUNDINGDEPT) REFERENCES comp_depts,
    CONSTRAINT PERS_MANAGER_FK FOREIGN KEY (MANAGER) REFERENCES persons,
    CONSTRAINT PERS_MENTOR_FK FOREIGN KEY (MENTOR) REFERENCES persons,
    CONSTRAINT PERS_HRADVISOR_FK FOREIGN KEY (HRADVISOR) REFERENCES persons,
    CONSTRAINT EMPS_PK PRIMARY KEY (PERSONID)
);

CREATE TABLE insuranceplans (
    INSID INTEGER NOT NULL,
    CARRIER VARCHAR(64) NOT NULL,
    LIFETIME_ORTHO_BENEFIT DECIMAL(22,3),
    PLANTYPE VARCHAR(8),
    DISCRIMINATOR varchar(255) NOT NULL,
    EMPLOYEE INTEGER,
    CONSTRAINT INS_EMP_FK FOREIGN KEY (EMPLOYEE) REFERENCES persons,
    CONSTRAINT INS_PK PRIMARY KEY (INSID)
);

CREATE TABLE projects (
    PROJID INTEGER NOT NULL,
    NAME VARCHAR(32) NOT NULL,
    PROJECT_BUDGET DECIMAL(11,2) NOT NULL,
    CLASS VARCHAR(255),
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

ALTER TABLE project_member 
    ADD CONSTRAINT PM_PROJ_FK FOREIGN KEY
        (PROJID) REFERENCES projects;

ALTER TABLE project_member 
    ADD CONSTRAINT PM_MEMB_FK FOREIGN KEY
        (MEMBER) REFERENCES persons;


ALTER TABLE comp_depts 
    ADD CONSTRAINT EMP_MO_FK FOREIGN KEY
        (EMP_OF_THE_MONTH) REFERENCES persons(PERSONID) ON DELETE SET NULL;

disconnect;
