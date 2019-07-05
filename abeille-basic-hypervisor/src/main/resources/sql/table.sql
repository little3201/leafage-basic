drop table if exists user_info;

/*==============================================================*/
/* Table: user_info                                             */
/*==============================================================*/
create table user_info
(
    id                         bigint(11) not null auto_increment comment '主键',
    user_id                    varchar(32) comment '用户ID',
    group_id                   bigint(11) comment '部门ID',
    user_name_cn               varchar(64) comment '中文姓名',
    user_name_en               varchar(64) comment '英文姓名',
    username                   varchar(64) comment '用户名',
    password                   varchar(128) comment '密码',
    user_mobile                varchar(64) comment '电话',
    user_email                 varchar(128) comment '邮箱',
    user_address               varchar(512) comment '地址',
    is_account_non_expired     tinyint(1)          default 1 comment '是否未失效',
    is_account_non_locked      tinyint(1)          default 1 comment '是否未锁定',
    is_credentials_non_expired tinyint(1)          default 1 comment '是否资格未失效',
    is_enabled                 tinyint(1)          default 1 comment '是否激活',
    modifier_id                bigint(11) not null comment '修改人ID',
    modify_time                timestamp  not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table user_info
    comment '用户信息表';



drop table if exists user_role;

/*==============================================================*/
/* Table: user_role                                             */
/*==============================================================*/
create table user_role
(
    id          bigint(11) not null auto_increment comment '主键',
    user_id     bigint(11) comment '用户ID',
    role_id     bigint(11) comment '角色ID',
    is_enabled  tinyint(1) default 1 comment '是否可用',
    modifier_id bigint(11) not null comment '修改人ID',
    modify_time timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table user_role
    comment '用户角色表';


drop table if exists role_info;

/*==============================================================*/
/* Table: role_info                                             */
/*==============================================================*/
create table role_info
(
    id          bigint(11) not null auto_increment comment '主键',
    role_name   varchar(64) comment '角色名称',
    role_desc   varchar(256) comment '角色描述',
    role_remark varchar(512) comment '备注',
    is_enabled  tinyint(1) default 1 comment '是否可用',
    modifier_id bigint(11) not null comment '修改人ID',
    modify_time timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table role_info
    comment '角色信息表';


drop table if exists role_source;

/*==============================================================*/
/* Table: role_source                                           */
/*==============================================================*/
create table role_source
(
    id          bigint(11) not null auto_increment comment '主键',
    role_id     bigint(11) comment '角色ID',
    source_id   bigint(11) comment '资源ID',
    is_enabled  tinyint(1) default 1 comment '是否可用',
    modifier_id bigint(11) not null comment '修改人ID',
    modify_time timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table role_source
    comment '角色资源表';


drop table if exists source_info;

/*==============================================================*/
/* Table: source_info                                           */
/*==============================================================*/
create table source_info
(
    id                 bigint(11) not null auto_increment comment '主键',
    source_code        varchar(64) comment '资源编号',
    source_parent_code varchar(64) comment '资源父编号',
    source_name_cn     varchar(64) comment '资源中文名称',
    source_name_en     varchar(64) comment '资源英文名称',
    source_type        tinyint(4) comment '资源类型',
    source_desc        varchar(64) comment '资源描述',
    source_path        varchar(128) comment '资源路径',
    is_enabled         tinyint(1) default 1 comment '是否可用',
    modifier_id        bigint(11) not null comment '修改人ID',
    modify_time        timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table source_info
    comment '权限信息表';







