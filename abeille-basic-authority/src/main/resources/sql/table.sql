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
    is_account_non_expired     tinyint(1) comment '是否未失效',
    is_account_non_locked      tinyint(1) comment '是否未锁定',
    is_credentials_non_expired tinyint(1) comment '是否资格未失效',
    is_enabled                 tinyint(1) not null default 1 comment '是否激活',
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
    modifier_id bigint(11) comment '修改人ID',
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
    is_enabled  tinyint(1) not null default 1 comment '是否可用',
    modifier_id bigint(11) not null comment '修改人ID',
    modify_time timestamp  not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table role_info
    comment '角色信息表';


drop table if exists role_perm;

/*==============================================================*/
/* Table: role_perm                                             */
/*==============================================================*/
create table role_perm
(
    id          bigint(11) not null auto_increment comment '主键',
    role_id     bigint(11) comment '角色ID',
    perm_id     bigint(11) comment '权限ID',
    is_enabled  tinyint(1) not null default 1 comment '是否可用',
    modifier_id bigint(11) not null comment '修改人ID',
    modify_time timestamp  not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table role_perm
    comment '角色权限表';


drop table if exists perm_info;

/*==============================================================*/
/* Table: perm_info                                             */
/*==============================================================*/
create table perm_info
(
    id               bigint(11) not null auto_increment comment '主键',
    perm_code        varchar(64) comment '权限编号',
    perm_parent_code varchar(64) comment '权限父编号',
    perm_name_cn     varchar(64) comment '权限中文名称',
    perm_name_en     varchar(64) comment '权限英文名称',
    perm_type        tinyint(4) comment '权限类型',
    perm_desc        varchar(64) comment '权限描述',
    perm_path        varchar(128) comment '权限路径',
    is_enabled       tinyint(1) not null default 1 comment '是否可用',
    modifier_id      bigint(11) not null comment '修改人ID',
    modify_time      timestamp  not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table perm_info
    comment '权限信息表';



drop table if exists oauth_access_token;

/*==============================================================*/
/* Table: oauth_access_token                                    */
/*==============================================================*/
create table oauth_access_token
(
    id                bigint(11) not null auto_increment comment '主键',
    token_id          varchar(128) comment 'TokenID',
    token             varchar(128) comment 'Token信息',
    authentication_id varchar(128) comment 'AuthenticationID',
    user_name         varchar(128) comment '用户名',
    client_id         varchar(128) comment 'ClientID',
    authentication    varchar(256) comment '认证信息',
    refresh_token     varchar(128) comment 'RefreshToken',
    is_enabled        tinyint(1) not null default 1 comment '是否可用',
    modifier_id       bigint(11) not null comment '修改人ID',
    modify_time       timestamp  not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table oauth_access_token
    comment 'token信息表';



drop table if exists oauth_client_details;

/*==============================================================*/
/* Table: oauth_client_details                                  */
/*==============================================================*/
create table oauth_client_details
(
    id                      bigint(11) not null auto_increment comment '主键',
    client_id               varchar(128) comment '客户端ID',
    resource_ids            varchar(128) comment '资源ID',
    client_secret           varchar(128) comment '客户端密钥',
    scope                   varchar(128) comment '作用域',
    authorized_grant_types  varchar(128) comment '认证类型',
    web_server_redirect_uri varchar(128) comment '跳转url',
    authorities             varchar(128) comment '权限',
    access_token_validity   varchar(128) comment 'token验证',
    refresh_token_validity  varchar(128) comment '刷新token验证',
    additional_information  varchar(128) comment '附加信息',
    autoapprove             varchar(128) comment '自动提交',
    is_enabled              tinyint(1) default 1 comment '是否可用',
    modifier_id             bigint(11) comment '修改人ID',
    modifier_time           timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table oauth_client_details
    comment '客户端信息表';







