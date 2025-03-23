CREATE TABLE IF NOT EXISTS t_Player (
    id uuid primary key not null,
    name varchar(32) not null unique
);