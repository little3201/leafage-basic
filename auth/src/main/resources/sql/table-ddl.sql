drop table if exists oauth_registered_client;

/*==============================================================*/
/* Table: oauth_registered_client                               */
/*==============================================================*/
create table oauth_registered_client
(
   id                   varchar(100) not null comment '主键',
   client_id            varchar(100) not null comment '客户端ID',
   client_id_issued_at  timestamp not null comment '生效时间',
   client_secret        varchar(200) not null comment '密钥',
   client_secret_expires_at timestamp comment '密钥失效时间',
   client_name          varchar(200) comment '名称',
   client_authorization_methods varchar(1000) comment '认证方法',
   authorization_grant_types varchar(1000) comment '授权方式',
   redirect_uris        varchar(1000) comment '跳转连接',
   scopes               varchar(1000) comment '作用域',
   client_settings      varchar(2000) comment '客户端设置',
   token_settings       varchar(2000) comment 'token 设置',
   primary key (id)
);

alter table oauth_registered_client comment 'client 信息';


drop table if exists oauth_authorization;

/*==============================================================*/
/* Table: oauth_authorization                                   */
/*==============================================================*/
create table oauth_authorization
(
   id                   varchar(100) not null comment '主键',
   registered_client_id varchar(100) not null comment '客户端ID',
   principal_name       timestamp not null comment '认证账号',
   authorization_grant_type varchar(1000) not null comment '授权类型',
   attributes           varchar(4000) comment '参数',
   state                varchar(500) comment '状态',
   authorization_code_value blob comment 'authorization code',
   authorization_code_issued_at timestamp comment 'authorization code生效时间',
   authorization_code_expires_at timestamp comment 'authorization code失效时间',
   authorization_code_metadata varchar(2000) comment 'authorization code 元数据',
   access_token_value   blob comment 'access token',
   access_token_issued_at timestamp comment 'access token 生效时间',
   access_token_expires_at timestamp comment 'access_token 失效时间',
   access_token_metadata varchar(2000) comment 'access token元数据',
   access_token_type    varchar(100) comment 'access token 类型',
   access_token_scopes  varchar(1000) comment 'access token 域',
   oidc_id_token_value  blob comment 'oidc token',
   oidc_id_token_issued_at timestamp comment 'oidc token 生效时间',
   oidc_id_token_expires_at timestamp comment 'oidc token 失效时间',
   oidc_id_token_metadata varchar(2000) comment 'oidc token 元数据',
   refresh_token_value  blob comment 'refresh token',
   refresh_token_issued_at timestamp comment 'refresh token 生效时间',
   refresh_token_expires_at timestamp comment 'refresh token 失效时间',
   refresh_token_metadata varchar(2000) comment 'refresh token 元数据',
   primary key (id)
);

alter table oauth_authorization comment '授权信息';


drop table if exists oauth_authorization_consent;

/*==============================================================*/
/* Table: oauth_authorization_consent                           */
/*==============================================================*/
create table oauth_authorization_consent
(
   registered_client_id varchar(100) not null comment '客户端ID',
   principal_name       varchar(200) not null comment '认证账号',
   authorities          varchar(1000) not null comment '权限',
   primary key ()
);

alter table oauth_authorization_consent comment '认证内容';

