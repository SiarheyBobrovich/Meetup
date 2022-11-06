CREATE DATABASE IF NOT EXISTS modsen
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

\c modsen

CREATE SCHEMA IF NOT EXISTS example
    AUTHORIZATION postgres;

CREATE SEQUENCE IF NOT EXISTS example.meetup_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS example.meetups
(
    id bigint NOT NULL DEFAULT  nextval('example.meetup_id_seq'),
    topic character varying(100) NOT NULL,
    description text,
    dt_meetup timestamp without time zone NOT NULL,
    dt_update timestamp(3) without time zone NOT NULL,
    organization character varying(100) NOT NULL,
    place character varying(150) NOT NULL,
    CONSTRAINT meetups_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS example.meetups
        OWNER to postgres;

ALTER SEQUENCE example.meetup_id_seq
    OWNED BY example.meetups.id;
