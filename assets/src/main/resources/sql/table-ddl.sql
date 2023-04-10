drop table if exists posts;

/*==============================================================*/
/* Table: posts                                                 */
/*==============================================================*/
create table posts
(
   id                   bigserial not null primary key,
   title                varchar(32),
   tags                 varchar(255),
   cover                varchar(127),
   category_id          bigint,
   context              text,
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          timestamp not null default CURRENT_TIMESTAMP,
   constraint fk_posts_category foreign key(category_id) references categories(id)
);


drop table if exists post_content;

/*==============================================================*/
/* Table: post_content                                         */
/*==============================================================*/
create table post_content
(
   id                   bigserial not null primary key,
   post_id              bigint not null comment '帖子主键',
   context              text comment '内容',
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   constraint fk_post_content_post foreign key(post_id) references posts(id)
);


drop table if exists categories;

/*==============================================================*/
/* Table: categories                                              */
/*==============================================================*/
create table categories
(
   id                   bigserial not null primary key,
   category_name        varchar(32),
   description          varchar(255),
   enabled              boolean not null default true,
   owner                varchar(16) not null ,
   modify_time          datetime not null default CURRENT_TIMESTAMP
);


drop table if exists comments;

/*==============================================================*/
/* Table: comments                                              */
/*==============================================================*/
create table comments
(
   id                   bigserial not null primary key,
   post_id              bigint not null,
   country              varchar(255),
   location             varchar(255),
   context              text,
   enabled              boolean not null default true,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   constraint fk_comments_post foreign key(post_id) references posts(id)
);


drop table if exists statistics;

/*==============================================================*/
/* Table: post_statistics                                            */
/*==============================================================*/
create table post_statistics
(
   id                   bigserial not null primary key,
   post_id              bigint not null ,
   viewed               bigint,
   likes                bigint,
   comments             bigint,
   modify_time          datetime not null default CURRENT_TIMESTAMP,
   constraint fk_post_statistics_post foreign key(post_id) references posts(id)
);