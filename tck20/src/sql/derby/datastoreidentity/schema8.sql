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

connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA datastoreidentity8;
SET SCHEMA datastoreidentity8;

DROP TABLE CART_ENTRIES;
DROP TABLE CARTS;
DROP TABLE PRODUCTS;
DROP TABLE UNDETACHABLES;

CREATE TABLE CARTS (

    DATASTORE_ID BIGINT NOT NULL,
    CART_ID BIGINT NOT NULL,
    CUSTOMER_ID VARCHAR(128) NOT NULL,
    VERSION INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT CARTS_PK PRIMARY KEY (DATASTORE_ID),
    CONSTRAINT CARTS_UK UNIQUE      (CART_ID)
);

CREATE TABLE PRODUCTS (

    DATASTORE_ID VARCHAR(48) NOT NULL,
    SKU VARCHAR(48) NOT NULL,
    DESCRIPTION VARCHAR(128),
    VERSION INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT PRODUCTS_PK  PRIMARY KEY  (DATASTORE_ID),
    CONSTRAINT PRODUCTS_UK  UNIQUE       (SKU)
);

CREATE TABLE CART_ENTRIES (

    DATASTORE_ID BIGINT NOT NULL,
    ID BIGINT NOT NULL,
    QUANTITY INTEGER DEFAULT 0,
    CART_ID BIGINT NOT NULL,
    SKU VARCHAR(48) NOT NULL,
    VERSION INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT CART_ENTRIES_PK  PRIMARY KEY (DATASTORE_ID),
    CONSTRAINT CART_ENTRIES_UK  UNIQUE      (ID),
    CONSTRAINT CARTS_FK         FOREIGN KEY (CART_ID)   REFERENCES CARTS,
    CONSTRAINT PRODUCTS_FK      FOREIGN KEY (SKU)       REFERENCES PRODUCTS
);

CREATE TABLE UNDETACHABLES (

    DATASTORE_ID BIGINT NOT NULL,
    ID BIGINT NOT NULL,
    FOO INTEGER,
    BAR INTEGER,
    VERSION INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT UNDETACHABLES_PK PRIMARY KEY (DATASTORE_ID),
    CONSTRAINT UNDETACHABLES_UK UNIQUE      (ID)
);
