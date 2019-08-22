drop table if exists article_info;

/*==============================================================*/
/* Table: article_info                                          */
/*==============================================================*/
create table article_info
(
    id          bigint(11) not null auto_increment comment '主键',
    article_id  varchar(8) comment '文章ID',
    title       varchar(32) comment '标题',
    description varchar(256) comment '描述',
    summary     varchar(512) comment '概要',
    url         varchar(128) comment '文件url',
    is_enabled  tinyint(1) not null default 1 comment '是否可用',
    modifier    bigint(11) not null comment '修改人',
    modify_time timestamp  not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table article_info
    comment '文章信息表';



drop table if exists account_info;

/*==============================================================*/
/* Table: account_info                                          */
/*==============================================================*/
create table account_info
(
    id          bigint(11) not null auto_increment comment '主键',
    user_id     bigint(11) not null comment '用户主键',
    account_id  varchar(8) comment '账户ID',
    type        char comment '账户类型',
    balance     decimal(11, 3) comment '账户余额',
    is_enabled  tinyint(1) not null default 1 comment '是否可用',
    modifier    bigint(11) not null comment '修改人',
    modify_time timestamp  not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);

alter table account_info
    comment '账户信息表';
