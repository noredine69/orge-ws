drop table if exists fee_rule;
create table fee_rule
(
    id integer not null,
    name varchar(255) not null,
    rate integer not null,
    sql_restrictions varchar2(1024) not null,
    is_default varchar2(1) default '0',
    primary key(id)
);


