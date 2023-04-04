drop table if exists groups;

/*==============================================================*/
/* Table: groups                                                */
/*==============================================================*/
create table groups
(
   id                   bigserial not null primary key,
   group_name           varchar(64),
   principal            bigint ,
   superior_id          bigint,
   description          varchar(127),
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
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          timestamp not null default CURRENT_TIMESTAMP,
   primary key (id),
   constraint fk_group_members_group foreign key(group_id) references groups(id)
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
   avatar               varchar(16) not null ,
   enabled              boolean not null default true,
   account_expires_at   timestamp,
   credential_expires_at timestamp,
   account_non_locked   boolean not null default true,
   constraint unique_uk_username unique(username)
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
   superior_id          bigint,
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
   username             bigint not null,
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          timestamp not null default CURRENT_TIMESTAMP,
   constraint fk_role_members_role foreign key(role_id) references roles(id)
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
   type                 tinyint(4),
   path                 varchar(127),
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
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
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          timestamp not null default CURRENT_TIMESTAMP,
   constraint fk_role_components_role foreign key(role_id) references roles(id),
   constraint fk_role_components_comment foreign key(comment_id) references components(id)
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
   owner                varchar(16) not null ,
   modify_time          timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists messages;

/*==============================================================*/
/* Table: messages                                              */
/*==============================================================*/
create table messages
(
   id                   bigserial not null primary key,
   title                varchar(255) comment '标题',
   content              longtext comment '内容',
   is_read              tinyint(1) comment '是否已读',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
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
   superior             bigint,
   postal_code          bigint,
   area_code            bigint,
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP
);