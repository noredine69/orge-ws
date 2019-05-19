create sequence if not exists seq_fee_rule;

drop table if exists fee_rule;
create table fee_rule
(
    id integer not null default nextval('seq_fee_rule'),
    name varchar(255) not null,
    rate integer not null,
    freelancer_location integer,
    client_location integer,
    sql_restrictions varchar2(1024) not null,
    is_default varchar2(1) default '0',
    primary key(id)
);

drop table if exists country;
create table country
(
  name varchar(255) not null,
  iso2 varchar(2) not null,
  iso3 varchar(3) not null,
  code integer not null,
  primary key(code)
);
