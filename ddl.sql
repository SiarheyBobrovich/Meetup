CREATE SEQUENCE IF NOT EXISTS modsen.meetup_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS modsen.meetups
(
    id bigint NOT NULL DEFAULT  nextval('modsen.meetup_id_seq'),
    topic character varying(100) NOT NULL,
    description text,
    dt_meetup timestamp without time zone NOT NULL,
    dt_update timestamp(3) without time zone NOT NULL,
    organization character varying(100) NOT NULL,
    place character varying(150) NOT NULL,
    CONSTRAINT meetups_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS modsen.meetups
        OWNER to postgres;

ALTER SEQUENCE modsen.meetup_id_seq
    OWNED BY modsen.meetups.id;
