-- SchemaType: application identity
connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';
CREATE TABLE PCPoint (
    ID BIGINT NOT NULL,
    X INTEGER NOT NULL,
    Y INTEGER NOT NULL,
    CONSTRAINT PCPNT_CONST PRIMARY KEY (ID)
);

CREATE TABLE PCPoint2 (
    ID BIGINT NOT NULL,
    X INTEGER NOT NULL,
    Y INTEGER NOT NULL,
    CONSTRAINT PCPNT2_CONST PRIMARY KEY (ID)
);

CREATE TABLE PCRect (
    ID BIGINT NOT NULL,
    UPPER_LEFT INTEGER NOT NULL,
    LOWER_RIGHT INTEGER NOT NULL,
    CONSTRAINT PCRCT_CONST PRIMARY KEY (ID)
);

CREATE TABLE Point (
    X INTEGER NOT NULL,
    Y INTEGER NOT NULL
);

CREATE TABLE PrimitiveTypes (
    ID BIGINT NOT NULL,
    booleanNotNull CHAR FOR BIT DATA NOT NULL,
    booleanNull CHAR FOR BIT DATA NOT NULL,
    byteNotNull SMALLINT NOT NULL,
    byteNull SMALLINT NOT NULL,
    shortNotNull SMALLINT NOT NULL,
    shortNull SMALLINT NOT NULL,
    intNotNull INTEGER NOT NULL,
    intNull INTEGER NOT NULL,
    longNotNull INTEGER NOT NULL,
    longNull INTEGER NOT NULL,
    floatNotNull FLOAT NOT NULL,
    floatNull FLOAT NOT NULL,
    doubleNotNull FLOAT NOT NULL,
    doubleNull FLOAT NOT NULL,
    charNotNull CHAR NOT NULL,
    charNull CHAR NOT NULL,
    dateNull DATE NOT NULL,
    stringNull VARCHAR (256) NOT NULL,
    bigDecimal DECIMAL NOT NULL,
    bigInteger INTEGER NOT NULL,
    PrimitiveTypes INTEGER NOT NULL,
    CONSTRAINT PCPNT_PT PRIMARY KEY (ID)
);

disconnect;
