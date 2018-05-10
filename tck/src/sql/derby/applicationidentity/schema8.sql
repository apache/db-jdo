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

CREATE SCHEMA applicationidentity8;
SET SCHEMA applicationidentity8;

DROP TABLE CART_ENTRIES;
DROP TABLE CARTS;
DROP TABLE PRODUCTS;
DROP TABLE UNDETACHABLES;

CREATE TABLE CARTS (
    CART_ID BIGINT NOT NULL,
    CUSTOMER_ID VARCHAR(128) NOT NULL,
    VERSION INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT CARTS_PK PRIMARY KEY (CART_ID)
);

CREATE TABLE PRODUCTS (
    SKU VARCHAR(48) NOT NULL,
    DESCRIPTION VARCHAR(128),
    VERSION INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT PRODUCTS_PK PRIMARY KEY (SKU)
);

CREATE TABLE CART_ENTRIES (
    ID BIGINT NOT NULL,
    QUANTITY INTEGER DEFAULT 0,
    CART_ID BIGINT NOT NULL,
    SKU VARCHAR(48) NOT NULL,
    VERSION INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT CART_ENTRIES_PK  PRIMARY KEY (ID),
    CONSTRAINT CARTS_FK         FOREIGN KEY (CART_ID)   REFERENCES CARTS,
    CONSTRAINT PRODUCTS_FK      FOREIGN KEY (SKU)       REFERENCES PRODUCTS
);

CREATE TABLE UNDETACHABLES (
    ID BIGINT NOT NULL,
    FOO INTEGER,
    BAR INTEGER,
    VERSION INTEGER NOT NULL DEFAULT 0,

    CONSTRAINT UNDETACHABLES_PK PRIMARY KEY (ID)
);
