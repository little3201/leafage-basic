-- Drop table if exists categories
DROP TABLE IF EXISTS categories;

-- Create table categories
CREATE TABLE categories (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   name                 varchar(127) NOT NULL UNIQUE COMMENT '名称',
   description          varchar(255) COMMENT '描述',
   enabled           boolean NOT NULL DEFAULT true COMMENT '是否启用',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间'
);

-- Add comment to the table
COMMENT ON TABLE categories IS '类别表';

-- Create unique index on categories table
CREATE UNIQUE INDEX idx_unique_name ON categories (name);
COMMENT ON INDEX idx_unique_name IS '类别名称唯一索引';

-- Drop table if exists posts
DROP TABLE IF EXISTS posts;

-- Create table posts
CREATE TABLE posts (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   title                varchar(127) NOT NULL COMMENT '标题',
   tags                 varchar[] COMMENT '标签',
   cover                varchar(127) COMMENT '封面',
   category_id          bigint NOT NULL COMMENT '类别ID',
   enabled           boolean NOT NULL DEFAULT true COMMENT '是否启用',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
   CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Add comment to the table
COMMENT ON TABLE posts IS '帖子表';

-- Create index on category_id column
CREATE INDEX idx_category_id ON posts(category_id);
COMMENT ON INDEX idx_category_id IS '帖子类别ID索引';

-- Create index on created_by column
CREATE INDEX idx_created_by ON posts(created_by);
COMMENT ON INDEX idx_created_by IS '帖子创建者索引';

-- Drop table if exists post_content
DROP TABLE IF EXISTS post_content;

-- Create table post_content
CREATE TABLE post_content (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   post_id              bigint NOT NULL UNIQUE COMMENT '帖子ID',
   content              text COMMENT '内容',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
   CONSTRAINT fk_post_content_post FOREIGN KEY (post_id) REFERENCES posts(id)
);

-- Add comment to the table
COMMENT ON TABLE post_content IS '帖子内容表';

-- Create unique index on post_id column
CREATE UNIQUE INDEX idx_unique_post_id ON post_content (post_id);
COMMENT ON INDEX idx_unique_post_id IS '帖子ID唯一索引';

-- Drop table if exists comments
DROP TABLE IF EXISTS comments;

-- Create table comments
CREATE TABLE comments (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   post_id              bigint NOT NULL COMMENT '帖子ID',
   country              varchar(255) COMMENT '国家',
   location             varchar(255) COMMENT '位置',
   context              varchar(512) COMMENT '内容',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
   CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts(id)
);

-- Add comment to the table
COMMENT ON TABLE comments IS '评论表';

-- Drop table if exists post_statistics
DROP TABLE IF EXISTS post_statistics;

-- Create table post_statistics
CREATE TABLE post_statistics (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   post_id              bigint NOT NULL COMMENT '帖子ID',
   viewed               bigint COMMENT '浏览次数',
   likes                bigint COMMENT '点赞次数',
   comments             bigint COMMENT '评论次数',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间'
);

-- Add comment to the table
COMMENT ON TABLE post_statistics IS '帖子统计信息表';
