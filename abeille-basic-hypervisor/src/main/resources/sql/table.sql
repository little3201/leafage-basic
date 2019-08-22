drop table if exists user_info;

/*==============================================================*/
/* Table: user_info                                             */
/*==============================================================*/
create table user_info
(
    id                         bigint(11) not null auto_increment comment '主键',
    user_id                    varchar(8) comment '用户ID',
    group_id                   bigint(11) comment '组主键',
    chinese_name               varchar(64) comment '中文名',
    english_name               varchar(64) comment '英文名',
    username                   varchar(64) comment '用户名',
    password                   varchar(128) comment '密码',
    mobile                     varchar(64) comment '电话',
    email                      varchar(128) comment '邮箱',
    address                    varchar(512) comment '地址',
    is_account_non_expired     tinyint(1) default 1 comment '是否未失效',
    is_account_non_locked      tinyint(1) default 1 comment '是否未锁定',
    is_credentials_non_expired tinyint(1) default 1 comment '是否资格未失效',
    is_enabled                 tinyint(1) default 1 comment '是否激活',
    modifier                   bigint(11) not null comment '修改人',
    modify_time                timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id),
    unique key AK_username (username),
    unique key AK_user_id (user_id)
);

alter table user_info
    comment '用户信息表';



drop table if exists role_info;

/*==============================================================*/
/* Table: role_info                                             */
/*==============================================================*/
create table role_info
(
    id          bigint(11) not null auto_increment comment '主键',
    role_id     varchar(8) comment '角色ID',
    name        varchar(64) comment '名称',
    description varchar(64) comment '描述',
    remark      varchar(128) comment '备注',
    is_enabled  tinyint(1) default 1 comment '是否可用',
    modifier    bigint(11) not null comment '修改人',
    modify_time timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id),
    unique key AK_role_id (role_id)
);

alter table role_info
    comment '角色信息表';



drop table if exists source_info;

/*==============================================================*/
/* Table: source_info                                           */
/*==============================================================*/
create table source_info
(
    id           bigint(11) not null auto_increment comment '主键',
    source_id    varchar(8) comment '资源ID',
    superior     bigint(11) comment '上级',
    chinese_name varchar(64) comment '中文名',
    english_name varchar(64) comment '英文名',
    type         tinyint(4) comment '类型',
    description  varchar(64) comment '描述',
    path         varchar(128) comment '路径',
    is_enabled   tinyint(1) default 1 comment '是否可用',
    modifier     bigint(11) not null comment '修改人',
    modify_time  timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id),
    unique key AK_source_id (source_id)
);

alter table source_info
    comment '资源信息表';



drop table if exists role_source;

/*==============================================================*/
/* Table: role_source                                           */
/*==============================================================*/
create table role_source
(
    id          bigint(11) not null auto_increment comment '主键',
    role_id     bigint(11) comment '角色主键',
    source_id   bigint(11) comment '资源主键',
    is_enabled  tinyint(1) default 1 comment '是否可用',
    modifier    bigint(11) not null comment '修改人',
    modify_time timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table role_source
    comment '角色资源表';


drop table if exists user_role;

/*==============================================================*/
/* Table: user_role                                             */
/*==============================================================*/
create table user_role
(
    id          bigint(11) not null auto_increment comment '主键',
    user_id     bigint(11) comment '用户主键',
    role_id     bigint(11) comment '角色主键',
    is_enabled  tinyint(1) default 1 comment '是否可用',
    modifier    bigint(11) not null comment '修改人',
    modify_time timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table user_role
    comment '用户角色表';


drop table if exists group_info;

/*==============================================================*/
/* Table: group_info                                            */
/*==============================================================*/
create table group_info
(
    id           bigint(11) not null auto_increment comment '主键',
    group_id     varchar(8) comment '组ID',
    leader       bigint(11) comment '领导',
    superior     bigint(11) comment '上级',
    chinese_name varchar(128) comment '中文名',
    english_name varchar(128) comment '英文名',
    is_enabled   tinyint(1) not null default 1 comment '是否可用',
    modifier     bigint(11) not null comment '修改人',
    modify_time  timestamp  not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id),
    unique key AK_group_id (group_id)
);

alter table group_info
    comment '组信息表';

