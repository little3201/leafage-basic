drop table if exists posts;

/*==============================================================*/
/* Table: posts                                                 */
/*==============================================================*/
create table posts
(
   id                   bigserial not null primary key,
   title                varchar(32),
   tags                 varchar[],
   cover                varchar(127),
   category_id          bigint,
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists post_content;

/*==============================================================*/
/* Table: post_content                                         */
/*==============================================================*/
create table post_content
(
   id                   bigserial not null primary key,
   post_id              bigint not null,
   context              text,
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
   constraint fk_post_content_post foreign key(post_id) references posts(id)
);


drop table if exists categories;

/*==============================================================*/
/* Table: categories                                              */
/*==============================================================*/
create table categories
(
   id                   bigserial not null primary key,
   name        varchar(32),
   description          varchar(255),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
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
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
);


drop table if exists post_statistics;

/*==============================================================*/
/* Table: post_statistics                                       */
/*==============================================================*/
create table post_statistics
(
   id                   bigserial not null primary key,
   post_id              bigint not null ,
   viewed               bigint,
   likes                bigint,
   comments             bigint,
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_at           timestamp not null default CURRENT_TIMESTAMP,
   last_updated_by      varchar(16) not null ,
   last_updated_at      timestamp not null default CURRENT_TIMESTAMP
);