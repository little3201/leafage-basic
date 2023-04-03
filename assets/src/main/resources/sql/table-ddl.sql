drop table if exists posts;

/*==============================================================*/
/* Table: posts                                                 */
/*==============================================================*/
create table posts
(
   id                   bigint unsigned not null auto_increment,
   title                varchar(32),
   tags                 varchar(255),
   cover                varchar(127),
   category_id          bigint,
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);


drop table if exists post_content;

/*==============================================================*/
/* Table: post_content                                         */
/*==============================================================*/
create table post_content
(
   id                   bigint unsigned not null auto_increment,
   post_id             bigint not null comment '帖子主键',
   context              text comment '内容',
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);


drop table if exists categories;

/*==============================================================*/
/* Table: categories                                              */
/*==============================================================*/
create table categories
(
   id                   bigint unsigned not null auto_increment,
   category_name        varchar(32),
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);


drop table if exists comments;

/*==============================================================*/
/* Table: comments                                              */
/*==============================================================*/
create table comments
(
   id                   bigint unsigned not null auto_increment,
   post_id              bigint not null,
   nickname             text,
   email                text,
   context              text,
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);


drop table if exists statistics;

/*==============================================================*/
/* Table: post_statistics                                            */
/*==============================================================*/
create table post_statistics
(
   id                   bigint unsigned not null auto_increment ,
   post_id              bigint not null ,
   viewed               bigint,
   likes                bigint,
   comments             bigint,
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   primary key (id)
);