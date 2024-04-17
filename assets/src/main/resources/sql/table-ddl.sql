-- Drop table if exists tags
DROP TABLE IF EXISTS tags;

-- Create table tags
CREATE TABLE tags (
   id                   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   name                 varchar(127) NOT NULL UNIQUE,
   enabled              boolean NOT NULL DEFAULT true,
   created_by           varchar(50),
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   last_modified_by     varchar(50),
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add comment to the table and columns
COMMENT ON TABLE tags IS '标签表';
COMMENT ON COLUMN tags.id IS '主键';
COMMENT ON COLUMN tags.name IS '名称';
COMMENT ON COLUMN tags.enabled IS '是否启用';
COMMENT ON COLUMN tags.created_by IS '创建者';
COMMENT ON COLUMN tags.created_date IS '创建时间';
COMMENT ON COLUMN tags.last_modified_by IS '最后修改者';
COMMENT ON COLUMN tags.last_modified_date IS '最后修改时间';

-- Create unique index on tags table
CREATE UNIQUE INDEX idx_unique_name ON tags (name);
COMMENT ON INDEX idx_unique_name IS '标签名称唯一索引';

-- Drop table if exists posts
DROP TABLE IF EXISTS posts;

-- Create table posts
CREATE TABLE posts (
   id                   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   title                varchar(127) NOT NULL,
   excerpt              varchar(127),
   enabled              boolean NOT NULL DEFAULT true,
   created_by           varchar(50),
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   last_modified_by     varchar(50),
   last_modified_date   timestamp
);

-- Add comment to the table and columns
COMMENT ON TABLE posts IS '帖子表';
COMMENT ON COLUMN posts.id IS '主键';
COMMENT ON COLUMN posts.title IS '标题';
COMMENT ON COLUMN posts.tags IS '标签';
COMMENT ON COLUMN posts.excerpt IS '概述';
COMMENT ON COLUMN posts.enabled IS '是否启用';
COMMENT ON COLUMN posts.created_by IS '创建者';
COMMENT ON COLUMN posts.created_date IS '创建时间';
COMMENT ON COLUMN posts.last_modified_by IS '最后修改者';
COMMENT ON COLUMN posts.last_modified_date IS '最后修改时间';

-- Create index on created_by column
CREATE INDEX idx_created_by ON posts(created_by);
COMMENT ON INDEX idx_created_by IS '帖子创建者索引';

-- Drop table if exists tags
DROP TABLE IF EXISTS tag_posts;

-- Create table tags
CREATE TABLE tag_posts (
   id                   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   tag_id               bigint NOT NULL,
   post_id              bigint NOT NULL
);

-- Add comment to the table and columns
COMMENT ON TABLE tag_posts IS '标签帖子关联表';
COMMENT ON COLUMN tag_posts.id IS '主键';
COMMENT ON COLUMN tag_posts.tag_id IS 'tag主键';
COMMENT ON COLUMN tag_posts.post_id IS 'post主键';

-- Drop table if exists post_content
DROP TABLE IF EXISTS post_content;

-- Create table post_content
CREATE TABLE post_content (
   id                   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   post_id              bigint NOT NULL UNIQUE,
   content              text,
   created_by           varchar(50),
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   last_modified_by     varchar(50),
   last_modified_date   timestamp,
   CONSTRAINT fk_post_content_post FOREIGN KEY (post_id) REFERENCES posts(id)
);

-- Add comment to the table and columns
COMMENT ON TABLE post_content IS '帖子内容表';
COMMENT ON COLUMN post_content.id IS '主键';
COMMENT ON COLUMN post_content.post_id IS '帖子ID';
COMMENT ON COLUMN post_content.content IS '内容';
COMMENT ON COLUMN post_content.created_by IS '创建者';
COMMENT ON COLUMN post_content.created_date IS '创建时间';
COMMENT ON COLUMN post_content.last_modified_by IS '最后修改者';
COMMENT ON COLUMN post_content.last_modified_date IS '最后修改时间';

-- Create unique index on post_id column
CREATE UNIQUE INDEX idx_unique_post_id ON post_content (post_id);
COMMENT ON INDEX idx_unique_post_id IS '帖子ID唯一索引';

-- Drop table if exists comments
DROP TABLE IF EXISTS comments;

-- Create table comments
CREATE TABLE comments (
   id                   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   post_id              bigint NOT NULL,
   location             varchar(255),
   content              varchar(512),
   created_by           varchar(50),
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   last_modified_by     varchar(50),
   last_modified_date   timestamp,
   CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts(id)
);

-- Add comment to the table and columns
COMMENT ON TABLE comments IS '评论表';
COMMENT ON COLUMN comments.id IS '主键';
COMMENT ON COLUMN comments.post_id IS '帖子ID';
COMMENT ON COLUMN comments.location IS '位置';
COMMENT ON COLUMN comments.content IS '内容';
COMMENT ON COLUMN comments.created_by IS '创建者';
COMMENT ON COLUMN comments.created_date IS '创建时间';
COMMENT ON COLUMN comments.last_modified_by IS '最后修改者';
COMMENT ON COLUMN comments.last_modified_date IS '最后修改时间';

-- Drop table if exists post_statistics
DROP TABLE IF EXISTS post_statistics;

-- Create table post_statistics
CREATE TABLE post_statistics (
   id                   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   post_id              bigint NOT NULL,
   viewed               bigint,
   likes                bigint,
   comments             bigint,
   created_by           varchar(50),
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   last_modified_by     varchar(50),
   last_modified_date   timestamp
);

-- Add comment to the table and columns
COMMENT ON TABLE post_statistics IS '帖子统计信息表';
COMMENT ON COLUMN post_statistics.id IS '主键';
COMMENT ON COLUMN post_statistics.post_id IS '帖子ID';
COMMENT ON COLUMN post_statistics.viewed IS '浏览次数';
COMMENT ON COLUMN post_statistics.likes IS '点赞次数';
COMMENT ON COLUMN post_statistics.comments IS '评论次数';
COMMENT ON COLUMN post_statistics.created_by IS '创建者';
COMMENT ON COLUMN post_statistics.created_date IS '创建时间';
COMMENT ON COLUMN post_statistics.last_modified_by IS '最后修改者';
COMMENT ON COLUMN post_statistics.last_modified_date IS '最后修改时间';
