create table if not exists player_cash
(
    uuid VARCHAR(128) not null primary key,
    cash bigint default 0 not null
);