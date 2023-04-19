drop table if exists groups;

/*==============================================================*/
/* Table: groups                                                */
/*==============================================================*/
create table groups
(
   id                   bigserial not null primary key,
   group_name           varchar(64),
   principal            varchar(16) ,
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists group_members;

/*==============================================================*/
/* Table: group_members                                         */
/*==============================================================*/
create table group_members
(
   id                   bigserial not null primary key,
   group_id             bigint not null,
   username             varchar(16) not null,
   modify_time          timestamp not null default CURRENT_TIMESTAMP,
   constraint fk_group_members_group foreign key(group_id) references groups(id),
   constraint fk_group_members_username foreign key(username) references users(username)
);


drop table if exists users;

/*==============================================================*/
/* Table: users                                                 */
/*==============================================================*/
create table users
(
   id                   bigserial not null primary key,
   username             varchar(16) not null,
   password             varchar(255) not null ,
   nickname             varchar(16) not null,
   avatar               varchar(127) not null ,
   enabled              boolean not null default true,
   account_expires_at   timestamp,
   credentials_expires_at timestamp,
   account_non_locked   boolean not null default true
);


drop table if exists authorities;

/*==============================================================*/
/* Table: authorities                                           */
/*==============================================================*/
create table authorities (
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username)
);
create unique index ix_auth_username on authorities (username,authority);


drop table if exists group_authorities;

/*==============================================================*/
/* Table: group_authorities                                     */
/*==============================================================*/
create table group_authorities (
	group_id bigint not null,
	authority varchar(50) not null,
	constraint fk_group_authorities_group foreign key(group_id) references groups(id)
);


drop table if exists roles;

/*==============================================================*/
/* Table: roles                                                 */
/*==============================================================*/
create table roles
(
   id                   bigserial not null primary key,
   role_name            varchar(64),
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists role_members;

/*==============================================================*/
/* Table: role_members                                             */
/*==============================================================*/
create table role_members
(
   id                   bigserial not null primary key,
   role_id              bigint not null,
   username             varchar(16) not null,
   modify_time          timestamp not null default CURRENT_TIMESTAMP,
   constraint fk_role_members_role foreign key(role_id) references roles(id),
   constraint fk_role_members_username foreign key(username) references users(username)
);


drop table if exists components;

/*==============================================================*/
/* Table: components                                             */
/*==============================================================*/
create table components
(
   id                   bigserial not null primary key,
   superior_id          bigint,
   component_name       varchar(64),
   type                 character(1),
   path                 varchar(127),
   icon                 varchar(127),
   enabled              boolean not null default true,
   owner                varchar(16) not null,
   modify_time          timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists role_components;

/*==============================================================*/
/* Table: role_components                                       */
/*==============================================================*/
create table role_components
(
   id                   bigserial not null primary key,
   role_id              bigint not null,
   component_id         bigint not null,
   modify_time          timestamp not null default CURRENT_TIMESTAMP,
   constraint fk_role_components_role foreign key(role_id) references roles(id),
   constraint fk_role_components_comment foreign key(component_id) references components(id)
);


drop table if exists dictionaries;

/*==============================================================*/
/* Table: dictionaries                                          */
/*==============================================================*/
create table dictionaries
(
   id                   bigserial not null primary key,
   dictionary_name      varchar(64),
   superior_id          bigint,
   description          varchar(127),
   enabled              boolean not null default true,
   modify_time          timestamp not null default CURRENT_TIMESTAMP
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
   read                 boolean not null default false,
   enabled              boolean not null default true,
   receiver             varchar(16) not null,
   owner                varchar(16) not null,
   modify_time          timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists regions;

/*==============================================================*/
/* Table: regions                                               */
/*==============================================================*/
create table regions
(
   id                   bigserial not null primary key,
   region_name          varchar(64),
   superior_id          bigint,
   postal_code          bigint,
   area_code            bigint,
   enabled              boolean not null default true,
   modify_time          timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists access_logs;

/*==============================================================*/
/* Table: access_logs                                           */
/*==============================================================*/
create table access_logs
(
   id                   bigserial not null primary key,
   ip                   inet,
   location          varchar(127),
   postal_code          bigint,
   context            text,
   owner                varchar(16) not null,
   modify_time          timestamp not null default CURRENT_TIMESTAMP
);