DROP TABLE FIXCANCELREJECT;
CREATE TABLE FIXCANCELREJECT (
	DBCANCELREJECTID	INTEGER	NOT NULL,
	ACCOUNT	VARCHAR(12)	,
	CLORDERID	VARCHAR(32)	,
	EXECID	VARCHAR(40)	,
	ORDERID	VARCHAR(17)	,
	ORIGCLORDERID	VARCHAR(32)	,
	TEXT	VARCHAR(18)	,
	TRANSACTTIME	TIMESTAMP	,
	CXLREJREASON	SMALLINT	,
	CXLREJRESPONSETO	CHAR(1)	,
	CORRELATIONCLORDID	VARCHAR(32)	,
	CREATEDBY VARCHAR(32),
	CREATIONDATE TIMESTAMP,
	LASTMODIFIEDBY VARCHAR(32),
	LASTMODIFIEDDATE TIMESTAMP
)