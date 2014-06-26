CREATE TABLE IF NOT EXISTS ID_SEQ_TABLE (
	SEQ_NAME VARCHAR(20) NOT NULL,
	SEQ_VALUE BIGINT NOT NULL,
	PRIMARY KEY (SEQ_NAME)
);

CREATE TABLE IF NOT EXISTS MIGRATED_CERTIFICATE (
	ID BIGINT IDENTITY NOT NULL,  
	CERTIFICATE_ID VARCHAR(255) NOT NULL,
	DOCUMENT BLOB NOT NULL,
	QUESTIONS INT DEFAULT 0,
	ANSWERED_QUESTIONS INT DEFAULT 0,
	HAS_LEGACY_CERTIFICATE BOOLEAN,
	PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS MIGRATION_MANIFEST (
	MANIFEST_ID BIGINT IDENTITY NOT NULL,  
	EXPORTER VARCHAR(255) NOT NULL,
	EXPORT_DATETIME DATETIME NOT NULL,
	CERTIFICATES_FULL BIGINT,
	CERTIFICATES_EMPTY BIGINT, 
	QUESTIONS BIGINT,
	ANSWERS BIGINT,
	PRIMARY KEY (MANIFEST_ID)
);
