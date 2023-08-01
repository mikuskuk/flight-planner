--liquibase formatted sql

--changeset mikus:1

CREATE TABLE flights
(
    id serial PRIMARY KEY,
    airport_from VARCHAR(255) NOT NULL,
    airport_to VARCHAR(255) NOT NULL,
    carrier VARCHAR(255) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    FOREIGN KEY (airport_from) REFERENCES airports(airport),
    FOREIGN KEY (airport_to) REFERENCES airports(airport)
);