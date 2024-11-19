create database app_service;
create database storage_service;

\c app_service;

CREATE TABLE persistent_logins (
                                   username VARCHAR(64) NOT NULL,
                                   series VARCHAR(64) PRIMARY KEY,
                                   token VARCHAR(64) NOT NULL,
                                   last_used TIMESTAMP NOT NULL
);