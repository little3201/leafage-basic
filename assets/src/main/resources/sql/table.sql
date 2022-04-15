drop table if exists posts;

/*==============================================================*/
/* Table: posts                                                 */
/*==============================================================*/
create table posts
(
   id                   bigint unsigned not null auto_increment comment '主键',
   code                 varchar(9) not null comment '代码',
   title                varchar(32) comment '标题',
   tags                 varchar(255) comment '标签',
   cover                varchar(127) comment '封面图',
   category_id          bigint comment '分类',
   likes                int comment '点赞',
   viewed               int comment '阅读量',
   comments             int comment '评论数',
   is_enabled           tinyint(1) not null default 1 comment '是否可用',
   modifier             varchar(16) not null comment '修改人',
   modify_time          datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id),
   unique key AK_code (code)
);

alter table posts comment '帖子';



drop table if exists posts_content;

/*==============================================================*/
/* Table: posts_content                                         */
/*==============================================================*/
create table posts_content
(
   id                   bigint unsigned not null auto_increment comment '主键',
   posts_id             bigint not null comment '帖子主键',
   original             text comment '原文',
   content              text comment '内容',
   catalog              text comment '目录',
   is_enabled           tinyint(1) not null default 1 comment '是否可用',
   modifier             varchar(16) not null comment '修改人',
   modify_time          datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id),
   key AK_posts_id (posts_id)
);

alter table posts_content comment '帖子内容';


drop table if exists category;

/*==============================================================*/
/* Table: category                                              */
/*==============================================================*/
create table category
(
   id                   bigint unsigned not null auto_increment comment '主键',
   code                 varchar(8) not null comment '代码',
   name                 varchar(32) comment '名称',
   is_enabled           tinyint(1) not null default 1 comment '是否可用',
   modifier             varchar(16) not null comment '修改人',
   modify_time          datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id),
   unique key AK_code (code)
);

alter table category comment '分类';


drop table if exists comment;

/*==============================================================*/
/* Table: comment                                               */
/*==============================================================*/
create table comment
(
   id                   bigint unsigned not null auto_increment comment '主键',
   posts_id             bigint not null comment '帖子主键',
   nickname             text comment '昵称',
   email                text comment '邮箱',
   content              text comment '内容',
   is_enabled           tinyint(1) not null default 1 comment '是否可用',
   modifier             varchar(16) not null comment '修改人',
   modify_time          datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id),
   key AK_posts_id (posts_id)
);

alter table comment comment '评论';


drop table if exists resource;

/*==============================================================*/
/* Table: resource                                              */
/*==============================================================*/
create table resource
(
   id                   bigint unsigned not null auto_increment comment '主键',
   code                 varchar(9) not null comment '代码',
   title                varchar(32) comment '标题',
   cover                varchar(127) comment '封面图',
   type                 char(1) comment '类型',
   category_id          bigint comment '分类',
   viewed               int comment '浏览量',
   downloads            int comment '下载量',
   description          text comment '描述',
   is_enabled           tinyint(1) not null default 1 comment '是否可用',
   modifier             varchar(16) not null comment '修改人',
   modify_time          datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id),
   unique key AK_code (code)
);

alter table resource comment '资源';




drop table if exists statistics;

/*==============================================================*/
/* Table: statistics                                            */
/*==============================================================*/
create table statistics
(
   id                   bigint unsigned not null auto_increment comment '主键',
   date                 date not null comment '日期',
   viewed               int comment '浏览量',
   likes                int comment '点赞数',
   comments             int comment '评论数',
   over_viewed          float(7,2) comment '浏览量环比',
   over_likes           float(7,2) comment '点赞数环比',
   over_comments        float(7,2) comment '评论数环比',
   is_enabled           tinyint(1) not null default 1 comment '是否可用',
   modifier             varchar(16) not null comment '修改人',
   modify_time          datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
   primary key (id),
   unique key AK_code (date)
);

alter table statistics comment '统计';

