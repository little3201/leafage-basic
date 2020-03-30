drop table if exists group_info;

/*==============================================================*/
/* Table: group_info                                            */
/*==============================================================*/
create table group_info
(
    id          bigint(11) not null auto_increment comment '主键',
    group_id    bigint(13) not null comment '组ID',
    name        varchar(64) comment '名称',
    principal   bigint(11) comment '负责人',
    superior    bigint(11) comment '上级',
    remark      varchar(128) comment '备注',
    is_enabled  tinyint(1) not null default 1 comment '是否可用',
    modifier    bigint(11) not null comment '修改人',
    modify_time timestamp  not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id),
    unique key AK_group_id (group_id)
);

alter table group_info
    comment '组信息表';


drop table if exists user_group;

/*==============================================================*/
/* Table: user_group                                            */
/*==============================================================*/
create table user_group
(
    id          bigint(11) not null auto_increment comment '主键',
    user_id     bigint(11) not null comment '用户主键',
    group_id    bigint(11) not null comment '组主键',
    is_enabled  tinyint(1) not null default 1 comment '是否可用',
    modifier    bigint(11) not null comment '修改人',
    modify_time timestamp  not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table user_group
    comment '用户组表';


drop table if exists user_info;

/*==============================================================*/
/* Table: user_info                                             */
/*==============================================================*/
create table user_info
(
    id                         bigint(11) not null auto_increment comment '主键',
    user_id                    bigint(13) not null comment '用户ID',
    role_id                    bigint(11) not null comment '角色主键',
    nickname                   varchar(64) comment '昵称',
    avatar                     varchar(128) comment '头像',
    username                   varchar(64) comment '用户名',
    password                   varchar(128) comment '密码',
    mobile                     varchar(64) comment '电话',
    email                      varchar(128) comment '邮箱',
    address                    varchar(512) comment '地址',
    is_account_non_expired     tinyint(1) default 1 comment '是否有效',
    is_account_non_locked      tinyint(1) default 1 comment '是否未锁定',
    is_credentials_non_expired tinyint(1) default 1 comment '是否密码有效',
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
    role_id     bigint(13) not null comment '角色ID',
    name        varchar(64) comment '名称',
    description varchar(64) comment '描述',
    is_enabled  tinyint(1) default 1 comment '是否可用',
    modifier    bigint(11) not null comment '修改人',
    modify_time timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id),
    unique key AK_role_id (role_id)
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
    role_id     bigint(11) not null comment '角色主键',
    source_id   bigint(11) not null comment '资源主键',
    is_enabled  tinyint(1) not null default 1 comment '是否可用',
    modifier    bigint(11) not null comment '修改人',
    modify_time timestamp  not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
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
    id          bigint(11) not null auto_increment comment '主键',
    source_id   bigint(13) not null comment '资源ID',
    superior    bigint(11) comment '上级',
    name        varchar(64) comment '名称',
    type        tinyint(4) comment '类型',
    description varchar(64) comment '描述',
    path        varchar(128) comment '路径',
    is_enabled  tinyint(1) default 1 comment '是否可用',
    modifier    bigint(11) not null comment '修改人',
    modify_time timestamp  default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id),
    unique key AK_source_id (source_id)
);

alter table source_info
    comment '资源信息表';


drop table if exists oauth_client_details;

/*==============================================================*/
/* Table: oauth_client_details                                  */
/*==============================================================*/
create table oauth_client_details
(
    id                   bigint(11) not null auto_increment comment '主键',
    client_id            varchar(128) comment '客户端ID',
    resource_ids         varchar(128) comment '客户端所能访问的资源id集合,多个资源时用逗号(,)分隔,如: “unity-resource,mobile-resource”. 该字段的值必须来源于与security.xml中标签?oauth2:resource-server的属性resource-id值一致',
    client_secret        varchar(128) comment '客户端密钥',
    scope                varchar(128) comment '作用域',
    authorized_grant_types varchar(128) comment '认证类型',
    web_server_redirect_uri varchar(128) comment '跳转url',
    authorities          varchar(128) comment '指定客户端所拥有的Spring Security的权限值,可选, 若有多个权限值,用逗号(,)分隔, 如: "ROLE_',
    access_token_validity varchar(128) comment '设定客户端的access_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时)',
    refresh_token_validity varchar(128) comment '设定客户端的refresh_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 24 * 30, 30天). ',
    additional_information varchar(128) comment '预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据,如:{“country”:“CN”,“country_code”:“086”}',
    autoapprove          varchar(128) comment '设置用户是否自动Approval操作, 默认值为 ''false'', 可选值包括 ''true'',''false'', ''read'',''write''.
            该字段只适用于grant_type="authorization_code"的情况,当用户登录成功后,若该值为''true''或支持的scope值,则会跳过用户Approve的页面, 直接授权. ',
    is_enabled           tinyint(1) default 1 comment '是否可用',
    modifier             bigint(11) not null comment '修改人',
    modifier_time        timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table oauth_client_details comment '客户端信息表';
