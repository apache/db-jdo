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

-- connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA applicationidentity10;
SET SCHEMA applicationidentity10;

-------------------------
-- company
-------------------------

ALTER TABLE departments DROP CONSTRAINT EMP_MO_FK;
ALTER TABLE departments DROP CONSTRAINT DEPTS_COMP_FK;
ALTER TABLE persons DROP CONSTRAINT PERS_DEPT_FK;
ALTER TABLE persons DROP CONSTRAINT PERS_FUNDDEPT_FK;
ALTER TABLE persons DROP CONSTRAINT PERS_MANAGER_FK;
ALTER TABLE persons DROP CONSTRAINT PERS_MENTOR_FK;
ALTER TABLE persons DROP CONSTRAINT PERS_HRADVISOR_FK;
DROP TABLE persons;
DROP TABLE departments;
DROP TABLE companies;

CREATE TABLE companies (
    ID INTEGER NOT NULL,
    NAME VARCHAR(32) NOT NULL,
    FOUNDEDDATE DATE NOT NULL,
    CONSTRAINT COMPS_PK PRIMARY KEY (ID)
);

CREATE TABLE departments (
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
    BIRTHDATE DATE NOT NULL,
    DISCRIMINATOR varchar(64) NOT NULL,
    ORDER_COL INTEGER NOT NULL,
    HIREDATE DATE,
    WEEKLYHOURS REAL,
    DEPARTMENT INTEGER,
    FUNDINGDEPT INTEGER,
    MANAGER INTEGER,
    MENTOR INTEGER,
    HRADVISOR INTEGER,
    SALARY REAL,
    WAGE REAL,
    CONSTRAINT PERS_DEPT_FK FOREIGN KEY (DEPARTMENT) REFERENCES departments,
    CONSTRAINT PERS_FUNDDEPT_FK FOREIGN KEY (FUNDINGDEPT)
        REFERENCES departments,
    CONSTRAINT PERS_MANAGER_FK FOREIGN KEY (MANAGER) REFERENCES persons,
    CONSTRAINT PERS_MENTOR_FK FOREIGN KEY (MENTOR) REFERENCES persons,
    CONSTRAINT PERS_HRADVISOR_FK FOREIGN KEY (HRADVISOR) REFERENCES persons,
    CONSTRAINT PERS_PK PRIMARY KEY (PERSONID)
);

ALTER TABLE departments 
    ADD CONSTRAINT EMP_MO_FK FOREIGN KEY
        (EMP_OF_THE_MONTH) REFERENCES persons(PERSONID) ON DELETE SET NULL;

