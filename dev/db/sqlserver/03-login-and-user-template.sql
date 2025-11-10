-- Thanks, Gemini & ChatGPT!
-- DISCLAIMER: not fully verified, AI-generated

START TRANSACTION;

USE master;
CREATE LOGIN [YourLoginName] WITH PASSWORD = 'YourStrongPassword', CHECK_POLICY = ON, CHECK_EXPIRATION = ON;

USE [YourDatabaseName];
CREATE USER [YourUserName] FOR LOGIN [YourLoginName];

-- 1. for dev

-- Grant DML Permissions
-- Too powerful for production, but easier for devs to work
GRANT CONTROL ON SCHEMA::dbo TO [YourUserName];
-- ALL DDL rights except for create (alter/drop table, view, functions, constraints, types, FKs, etc.)
-- ALL DML rights (select, insert, update, delete)
GRANT CREATE TABLE TO [YourUserName];


-- 2. for production setup (safe & least privilege)


-- DML only
GRANT SELECT, INSERT, UPDATE, DELETE ON SCHEMA::dbo TO [YourUserName];

-- (Optional) allow FK checks (read but not modify)
GRANT REFERENCES ON SCHEMA::dbo TO [YourUserName];
-- Production user CANNOT: Create tables, alter tables, drop tables, create foreign keys, create types, alter schema


ROLLBACK;