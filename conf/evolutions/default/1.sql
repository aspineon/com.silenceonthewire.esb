# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  updated_at                    timestamp,
  user_id                       bigint,
  tax_number                    varchar(255),
  ssn                           varchar(255),
  address                       varchar(255),
  city                          varchar(255),
  state                         varchar(255),
  country                       varchar(255),
  postal_code                   varchar(255),
  constraint uq_account_user_id unique (user_id),
  constraint uq_account_tax_number unique (tax_number),
  constraint uq_account_ssn unique (ssn),
  constraint pk_account primary key (id)
);

create table company (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  updated_at                    timestamp,
  name                          varchar(255),
  phone                         varchar(255),
  tax_number                    varchar(255),
  email                         varchar(255),
  address                       varchar(255),
  city                          varchar(255),
  state                         varchar(255),
  country                       varchar(255),
  postal_code                   varchar(255),
  constraint uq_company_name unique (name),
  constraint uq_company_phone unique (phone),
  constraint uq_company_tax_number unique (tax_number),
  constraint uq_company_email unique (email),
  constraint pk_company primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  created_at                    timestamp,
  updated_at                    timestamp,
  first_name                    varchar(255),
  last_name                     varchar(255),
  email                         varchar(255),
  phone                         varchar(255),
  password                      varbinary(255),
  is_admin                      boolean default false not null,
  company_id                    bigint,
  constraint uq_user_email unique (email),
  constraint uq_user_phone unique (phone),
  constraint pk_user primary key (id)
);

alter table account add constraint fk_account_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user add constraint fk_user_company_id foreign key (company_id) references company (id) on delete restrict on update restrict;
create index ix_user_company_id on user (company_id);


# --- !Downs

alter table account drop constraint if exists fk_account_user_id;

alter table user drop constraint if exists fk_user_company_id;
drop index if exists ix_user_company_id;

drop table if exists account;

drop table if exists company;

drop table if exists user;

