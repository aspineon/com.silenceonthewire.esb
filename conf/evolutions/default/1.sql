# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

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
  constraint uq_user_email unique (email),
  constraint uq_user_phone unique (phone),
  constraint pk_user primary key (id)
);


# --- !Downs

drop table if exists user;

