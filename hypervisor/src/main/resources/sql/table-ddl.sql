drop table if exists groups;

/*==============================================================*/
/* Table: groups                                                */
/*==============================================================*/
create table groups
(
   id                   bigserial not null primary key,
   name                 varchar(64) UNIQUE,
   principal            varchar(16),
   description          varchar(512),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_date           timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by      varchar(16) not null ,
   last_modified_date      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists users;

/*==============================================================*/
/* Table: users                                                 */
/*==============================================================*/
create table users
(
   id                   bigserial not null primary key,
   username             varchar(16) not null UNIQUE,
   password             varchar(255) not null ,
   firstname             varchar(16),
   lastname             varchar(16),
   avatar               varchar(127) not null ,
   account_expires_at   timestamp,
   credentials_expires_at timestamp,
   account_non_locked   boolean not null default true,
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_date           timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by      varchar(16) not null ,
   last_modified_date      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists roles;

/*==============================================================*/
/* Table: roles                                                 */
/*==============================================================*/
create table roles
(
   id                   bigserial not null primary key,
   name                 varchar(64) UNIQUE,
   description          varchar(512),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_date           timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by      varchar(16) not null ,
   last_modified_date      timestamp not null default CURRENT_TIMESTAMP
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
   CONSTRAINT fk_group_members_group
         FOREIGN KEY (group_id) REFERENCES groups(id),
   CONSTRAINT fk_group_members_username
         FOREIGN KEY (username) REFERENCES users(username)
);


drop table if exists group_roles;

/*==============================================================*/
/* Table: group_roles                                         */
/*==============================================================*/
create table group_roles
(
   id                   bigserial not null primary key,
   group_id             bigint not null,
   role_id              bigint not null,
   CONSTRAINT fk_group_roles_group
        FOREIGN KEY (group_id) REFERENCES groups(id),
   CONSTRAINT fk_group_roles_role
        FOREIGN KEY (role_id) REFERENCES roles(id)
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
   CONSTRAINT fk_role_members_role
        FOREIGN KEY (role_id) REFERENCES roles(id),
   CONSTRAINT fk_role_members_username
        FOREIGN KEY (username) REFERENCES users(username)
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
   created_date           timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by      varchar(16) not null ,
   last_modified_date      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists role_privileges;

/*==============================================================*/
/* Table: role_privileges                                       */
/*==============================================================*/
create table role_privileges
(
   id                   bigserial not null primary key,
   role_id              bigint not null,
   privilege_id         bigint not null,
   CONSTRAINT fk_role_privileges_role
       FOREIGN KEY (role_id) REFERENCES roles(id),
   CONSTRAINT fk_role_privileges_privilege
       FOREIGN KEY (privilege_id) REFERENCES privileges(id)
);


drop table if exists dictionaries;

/*==============================================================*/
/* Table: dictionaries                                          */
/*==============================================================*/
create table dictionaries
(
   id                   bigserial not null primary key,
   name                 varchar(64),
   superior_id          bigint,
   description          varchar(512),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_date           timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by      varchar(16) not null ,
   last_modified_date      timestamp not null default CURRENT_TIMESTAMP
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
   created_date           timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by      varchar(16) not null ,
   last_modified_date      timestamp not null default CURRENT_TIMESTAMP
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
   created_date           timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by      varchar(16) not null ,
   last_modified_date      timestamp not null default CURRENT_TIMESTAMP
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
   created_date           timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by      varchar(16) not null ,
   last_modified_date      timestamp not null default CURRENT_TIMESTAMP
);