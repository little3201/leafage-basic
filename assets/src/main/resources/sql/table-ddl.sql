drop table if exists categories;

/*==============================================================*/
/* Table: categories                                              */
/*==============================================================*/
create table categories
(
   id                   bigserial not null primary key,
   name                 varchar(32) UNIQUE,
   description          varchar(255),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_date         timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by     varchar(16) not null ,
   last_modified_date   timestamp not null default CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX idx_unique_name ON categories (name);


drop table if exists posts;

/*==============================================================*/
/* Table: posts                                                 */
/*==============================================================*/
CREATE TABLE posts
(
   id                   bigserial not null primary key,
   title                varchar(32),
   tags                 varchar[],
   cover                varchar(127),
   category_id          bigint,
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null ,
   created_date         timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by     varchar(16) not null ,
   last_modified_date   timestamp not null default CURRENT_TIMESTAMP,
   CONSTRAINT fk_category
      FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE INDEX idx_category_id ON posts(category_id);
CREATE INDEX idx_created_by ON posts(created_by);


drop table if exists post_content;

/*==============================================================*/
/* Table: post_content                                         */
/*==============================================================*/
CREATE TABLE post_content
(
   id                   bigserial not null primary key,
   post_id              bigint not null UNIQUE,
   context              text,
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null,
   created_date         timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by     varchar(16) not null,
   last_modified_date   timestamp not null default CURRENT_TIMESTAMP,
   CONSTRAINT fk_post_content_post
      FOREIGN KEY (post_id) REFERENCES posts(id)
);

CREATE UNIQUE INDEX idx_unique_post_id ON post_content (post_id);


drop table if exists comments;

/*==============================================================*/
/* Table: comments                                              */
/*==============================================================*/
CREATE TABLE comments
(
   id                   bigserial not null primary key,
   post_id              bigint not null,
   country              varchar(255),
   location             varchar(255),
   context              varchar(512),
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null,
   created_date         timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by     varchar(16) not null,
   last_modified_date   timestamp not null default CURRENT_TIMESTAMP,
   CONSTRAINT fk_comments_post
      FOREIGN KEY (post_id) REFERENCES posts(id)
);


drop table if exists post_statistics;

/*==============================================================*/
/* Table: post_statistics                                       */
/*==============================================================*/
CREATE TABLE post_statistics
(
   id                   bigserial not null primary key,
   post_id              bigint not null,
   viewed               bigint,
   likes                bigint,
   comments             bigint,
   is_enabled           boolean not null default true,
   created_by           varchar(16) not null,
   created_date         timestamp not null default CURRENT_TIMESTAMP,
   last_modified_by     varchar(16) not null,
   last_modified_date   timestamp not null default CURRENT_TIMESTAMP
);