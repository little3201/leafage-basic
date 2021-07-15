drop table if exists posts;

/*==============================================================*/
/* Table: posts                                                 */
/*==============================================================*/
create table posts
(
    id          bigint(11)  not null auto_increment comment '主键',
    code        varchar(10) not null comment '代码',
    title       varchar(32) comment '标题',
    subtitle    varchar(216) comment '概要',
    cover       varchar(128) comment '封面图',
    is_enabled  tinyint(1)  not null default 1 comment '是否可用',
    modifier    bigint(11)  not null comment '修改人',
    modify_time datetime    not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id),
    unique key AK_code (code)
);

alter table posts
    comment '帖子';


drop table if exists posts_content;

/*==============================================================*/
/* Table: posts_content                                         */
/*==============================================================*/
create table posts_content
(
    id          bigint(11) not null auto_increment comment '主键',
    posts_id    bigint(11) not null comment '帖子主键',
    content     char comment '内容',
    is_enabled  tinyint(1) not null default 1 comment '是否可用',
    modifier    bigint(11) not null comment '修改人',
    modify_time datetime   not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id),
    unique key AK_posts_id (posts_id)
);

alter table posts_content
    comment '帖子内容';

drop table if exists category;

/*==============================================================*/
/* Table: category                                              */
/*==============================================================*/
create table category
(
    id          bigint(11)  not null auto_increment comment '主键',
    code        varchar(10) not null comment '代码',
    name        varchar(32) comment '名称',
    is_enabled  tinyint(1)  not null default 1 comment '是否可用',
    modifier    bigint(11)  not null comment '修改人',
    modify_time datetime    not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id),
    unique key AK_code (code)
);

alter table category
    comment '分类';

