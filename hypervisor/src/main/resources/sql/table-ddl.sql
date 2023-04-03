drop table if exists groups;

/*==============================================================*/
/* Table: groups                                                */
/*==============================================================*/
create table groups
(
   id                   bigint unsigned not null auto_increment,
   group_name           varchar(64),
   principal            bigint ,
   superior_id          bigint,
   description          varchar(127),
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);


drop table if exists group_members;

/*==============================================================*/
/* Table: group_members                                         */
/*==============================================================*/
create table account_group
(
   id                   bigint unsigned not null auto_increment,
   group_id             bigint not null,
   username             varchar(16) not null,
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);

drop table if exists users;

/*==============================================================*/
/* Table: users                                                  */
/*==============================================================*/
create table users
(
   id                   bigint unsigned not null auto_increment comment '主键',
   username             varchar(16) not null unique,
   password             varchar(255) not null comment '电话',
   nickname             varchar(16) not null comment '账号',
   avatar               varchar(16) not null comment '账号',
   enabled              boolean not null default true,
   modify_time          timestamp,
   primary key (id),
   unique key AK_username (username),
   unique key AK_mobile (mobile),
   unique key AK_email (email)
);

drop table if exists role_members;

/*==============================================================*/
/* Table: role_members                                             */
/*==============================================================*/
create table role_members
(
   id                   bigint unsigned not null auto_increment comment '主键',
   role_id              bigint not null comment '角色主键',
   username             bigint not null comment '帐号主键',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);

alter table account_role comment '帐号-角色';


drop table if exists role;

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   id                   bigint unsigned not null auto_increment comment '主键',
   code                 varchar(9) comment '代码',
   name                 varchar(64) comment '名称',
   description          varchar(64) comment '描述',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id),
   key AK_code (code)
);

alter table role comment '角色';


drop table if exists role_authority;

/*==============================================================*/
/* Table: role_authority                                        */
/*==============================================================*/
create table role_authority
(
   id                   bigint unsigned not null auto_increment comment '主键',
   role_id              bigint not null comment '角色主键',
   authority_id         bigint not null comment '资源主键',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);

alter table role_authority comment '角色-权限';


drop table if exists authority;

/*==============================================================*/
/* Table: authority                                             */
/*==============================================================*/
create table authority
(
   id                   bigint unsigned not null auto_increment comment '主键',
   code                 varchar(9) not null comment '代码',
   superior             bigint comment '上级',
   name                 varchar(64) comment '名称',
   type                 tinyint(4) comment '类型',
   description          varchar(64) comment '描述',
   path                 varchar(127) comment '路径',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id),
   unique key AK_code (code)
);

alter table authority comment '权限';


drop table if exists group_role;

/*==============================================================*/
/* Table: group_role                                            */
/*==============================================================*/
create table group_role
(
   id                   bigint unsigned not null comment '主键',
   dept_id              bigint comment '分组主键',
   role_id              bigint comment '角色主键',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);

alter table group_role comment '分组-角色';


drop table if exists position_role;

/*==============================================================*/
/* Table: position_role                                         */
/*==============================================================*/
create table position_role
(
   id                   bigint unsigned not null auto_increment comment '主键',
   pos_id               bigint comment '职位主键',
   role_id              bigint comment '角色主键',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);

alter table position_role comment '职位-角色';


drop table if exists position;

/*==============================================================*/
/* Table: position                                              */
/*==============================================================*/
create table position
(
   id                   bigint unsigned not null auto_increment comment '主键',
   code                 varchar(9) not null comment '代码',
   name                 varchar(32) comment '名称',
   description          varchar(127) comment '描述',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);

alter table position comment '职位';


drop table if exists group_position;

/*==============================================================*/
/* Table: group_position                                        */
/*==============================================================*/
create table group_position
(
   id                   bigint unsigned not null auto_increment comment '主键',
   group_id             bigint comment '分组主键',
   pos_id               bigint comment '职位主键',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);

alter table group_position comment '分组-职位';


drop table if exists share_row;

/*==============================================================*/
/* Table: share_row                                             */
/*==============================================================*/
create table share_row
(
   id                   bigint unsigned not null auto_increment comment '主键',
   region               bigint comment '共享数据来源',
   region_type          tinyint comment '来源类型',
   data                 bigint comment '共享数据',
   target               bigint comment '共享数据目标',
   target_type          bigint comment '目标类型',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);

alter table share_row comment '行共享';


drop table if exists share_cell;

/*==============================================================*/
/* Table: share_cell                                            */
/*==============================================================*/
create table share_cell
(
   id                   bigint unsigned not null auto_increment comment '主键',
   table                varchar(32) comment '所属表',
   field                varchar(32) comment '字段名',
   alias                varchar(32) comment '别名',
   share_id             bigint comment '共享行主键',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);

alter table share_cell comment '列共享';


drop table if exists share;

/*==============================================================*/
/* Table: share                                                 */
/*==============================================================*/
create table share
(
   id                   bigint unsigned not null auto_increment comment '主键',
   share_row_id         bigint comment '共享行外键',
   p_id                 bigint comment '所属主键',
   type                 tinyint(1) comment '所属类型',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);

alter table share comment '共享';

drop table if exists dictionary;

/*==============================================================*/
/* Table: dictionary                                            */
/*==============================================================*/
create table dictionary
(
   id                   bigint unsigned not null auto_increment comment '主键',
   code                 varchar(9) not null comment '代码',
   name                 varchar(64) comment '名称',
   alias                varchar(64) comment '别名',
   superior             bigint comment '上级',
   description          varchar(127) comment '描述',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id),
   unique key AK_code (code)
);

alter table dictionary comment '字典';


drop table if exists notification;

/*==============================================================*/
/* Table: notification                                          */
/*==============================================================*/
create table notification
(
   id                   bigint unsigned not null auto_increment comment '主键',
   code                 varchar(9) not null comment '代码',
   title                varchar(255) comment '标题',
   content              longtext comment '内容',
   is_read              tinyint(1) comment '是否已读',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          timestamp with out timezone not null default CURRENT_TIMESTAMP,
   primary key (id)
);


drop table if exists regions;

/*==============================================================*/
/* Table: regions                                               */
/*==============================================================*/
create table regions
(
   id                   bigint unsigned not null auto_increment comment '主键',
   region_name          varchar(64) comment '名称',
   superior             bigint comment '上级',
   postal_code          int comment '邮编',
   area_code            int comment '区号',
   description          varchar(127) comment '描述',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);