drop table if exists group_;

/*==============================================================*/
/* Table: group_                                                */
/*==============================================================*/
create table group_
(
   id                   bigint unsigned not null auto_increment comment '主键',
   code                 varchar(9) not null comment '代码',
   name                 varchar(64) comment '名称',
   principal            bigint comment '负责人',
   superior             bigint comment '上级',
   description          varchar(127) comment '描述',
   is_enabled           tinyint(1) not null default 1 comment '是否可用',
   modifier             varchar(16) not null comment '修改人',
   modify_time          datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id),
   unique key AK_code (code)
);

alter table group_ comment '分组';


drop table if exists account_group;

/*==============================================================*/
/* Table: account_group                                            */
/*==============================================================*/
create table account_group
(
   id                   bigint unsigned not null auto_increment comment '主键',
   group_id             bigint not null comment '分组主键',
   account_id           bigint not null comment '账号主键',
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16) comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id)
);

alter table account_group comment '账号-分组';


drop table if exists user;

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   id                   bigint unsigned not null auto_increment comment '主键',
   username             varchar(16) not null comment '账号',
   mobile               varchar(16) not null comment '电话',
   email                varchar(64) not null comment '邮箱',
   firstname            varchar(64) comment '姓',
   lastname             varchar(64) comment '名',
   gender               tinyint(1) comment '性别',
   birthday             date comment '生日',
   country              varchar(64) comment '国籍',
   province             varchar(64) comment '省/直辖市/州',
   city                 varchar(64) comment '市',
   region               varchar(64) comment '区/县',
   street               varchar(64) comment '乡/镇/街道',
   address              varchar(64) comment '地址',
   ethnicity            varchar(64) comment '民族',
   blood_type           varchar(64) comment '血型',
   education            varchar(64) comment '学历',
   id_card              varchar(18) comment '身份证号',
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16) comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id),
   unique key AK_username (username),
   unique key AK_mobile (mobile),
   unique key AK_email (email)
);

alter table user comment '用户';


drop table if exists account_role;

/*==============================================================*/
/* Table: account_role                                             */
/*==============================================================*/
create table account_role
(
   id                   bigint unsigned not null auto_increment comment '主键',
   account_id           bigint not null comment '帐号主键',
   role_id              bigint not null comment '角色主键',
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16) comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
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
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16) not null comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
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
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16),
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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
   is_enabled           tinyint(1) default 1 comment '是否启用',
   modifier             varchar(16) comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
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
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16) comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
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
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16) comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
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
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16) comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
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
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16) comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
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
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16) comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
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
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16) comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
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
   is_enabled           tinyint(1) default 1 comment '是否可用',
   modifier             varchar(16) comment '修改人',
   modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id)
);

alter table share comment '共享';