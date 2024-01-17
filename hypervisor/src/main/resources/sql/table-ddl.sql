drop table if exists groups;

/*==============================================================*/
/* Table: groups                                                */
/*==============================================================*/
create table groups
(
   id                   bigserial not null primary key,
   name                 varchar(64),
   principal            varchar(16),
   description          varchar(512),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists group_members;

/*==============================================================*/
/* Table: group_members                                         */
/*==============================================================*/
create table group_members
(
   id                   bigserial not null primary key,
   group_id             bigint not null,
   username             varchar(16) not null
);

drop table if exists group_roles;

/*==============================================================*/
/* Table: group_roles                                         */
/*==============================================================*/
create table group_roles
(
   id                   bigserial not null primary key,
   group_id             bigint not null,
   role_id              bigint not null
);


drop table if exists users;

/*==============================================================*/
/* Table: users                                                 */
/*==============================================================*/
create table users
(
   username             varchar(16) not null primary key,
   password             varchar(255) not null ,
   firstname             varchar(16) not null,
   lastname             varchar(16) not null,
   avatar               varchar(127) not null ,
   account_expires_at   timestamp,
   credentials_expires_at timestamp,
   account_non_locked   boolean not null default true,
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists roles;

/*==============================================================*/
/* Table: roles                                                 */
/*==============================================================*/
create table roles
(
   id                   bigserial not null primary key,
   name                 varchar(64),
   description          varchar(512),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists role_members;

/*==============================================================*/
/* Table: role_members                                             */
/*==============================================================*/
create table role_members
(
   id                   bigserial not null primary key,
   role_id              bigint not null,
   username             varchar(16) not null
);


drop table if exists privileges;

/*==============================================================*/
/* Table: privileges                                             */
/*==============================================================*/
create table privileges
(
   id                   bigserial not null primary key,
   superior_id          bigint,
   name                 varchar(64),
   type                 character(1),
   path                 varchar(127),
   icon                 varchar(127),
   description          varchar(512),
   is_enabled              boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists role_privileges;

/*==============================================================*/
/* Table: role_privileges                                       */
/*==============================================================*/
create table role_privileges
(
   id                   bigserial not null primary key,
   role_id              bigint not null,
   privilege_id         bigint not null
);


drop table if exists dictionaries;

/*==============================================================*/
/* Table: dictionaries                                          */
/*==============================================================*/
create table dictionaries
(
   id                   bigserial not null primary key,
   name      varchar(64),
   superior_id          bigint,
   description          varchar(512),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists messages;

/*==============================================================*/
/* Table: messages                                              */
/*==============================================================*/
create table messages
(
   id                   bigserial not null primary key,
   title                varchar(255),
   context              text,
   is_read              boolean not null default false,
   receiver             varchar(16) not null,
   description          varchar(512),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists regions;

/*==============================================================*/
/* Table: regions                                               */
/*==============================================================*/
create table regions
(
   id                   bigserial not null primary key,
   name                 varchar(64),
   superior_id          bigint,
   postal_code          bigint,
   area_code            bigint,
   description          varchar(512),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists access_logs;

/*==============================================================*/
/* Table: access_logs                                           */
/*==============================================================*/
create table access_logs
(
   id                   bigserial not null primary key,
   ip                   inet,
   location             varchar(127),
   postal_code          bigint,
   context              text,
   description          varchar(512),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
);