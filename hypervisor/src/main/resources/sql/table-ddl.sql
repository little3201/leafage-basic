-- Drop table if exists groups
DROP TABLE IF EXISTS groups;

-- Create table groups
CREATE TABLE groups (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   name                 varchar(64) NOT NULL UNIQUE COMMENT '名称',
   principal            varchar(16) COMMENT '负责人',
   description          varchar(512) COMMENT '描述',
   enabled           boolean NOT NULL DEFAULT true COMMENT '是否启用',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间'
);

-- Add comment to the table
COMMENT ON TABLE groups IS '用户组表';

-- Drop table if exists users
DROP TABLE IF EXISTS users;

-- Create table users
CREATE TABLE users (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   username             varchar(16) NOT NULL UNIQUE COMMENT '用户名',
   password             varchar(255) NOT NULL COMMENT '密码',
   firstname            varchar(16) COMMENT '名字',
   lastname             varchar(16) COMMENT '姓氏',
   avatar               varchar(127) COMMENT '头像',
   account_expires_at   timestamp COMMENT '账号过期时间',
   credentials_expires_at timestamp COMMENT '凭证过期时间',
   account_non_locked   boolean NOT NULL DEFAULT true COMMENT '账号是否非锁定',
   enabled           boolean NOT NULL DEFAULT true COMMENT '是否启用',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间'
);

-- Add comment to the table
COMMENT ON TABLE users IS '用户表';

-- Drop table if exists roles
DROP TABLE IF EXISTS roles;

-- Create table roles
CREATE TABLE roles (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   name                 varchar(64) NOT NULL UNIQUE COMMENT '名称',
   description          varchar(512) COMMENT '描述',
   enabled           boolean NOT NULL DEFAULT true COMMENT '是否启用',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间'
);

-- Add comment to the table
COMMENT ON TABLE roles IS '角色表';

-- Drop table if exists group_members
DROP TABLE IF EXISTS group_members;

-- Create table group_members
CREATE TABLE group_members (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   group_id             bigint NOT NULL COMMENT '用户组ID',
   username             varchar(32) NOT NULL COMMENT '用户名',
   CONSTRAINT fk_group_members_group FOREIGN KEY (group_id) REFERENCES groups(id),
   CONSTRAINT fk_group_members_username FOREIGN KEY (username) REFERENCES users(username)
);

-- Add comment to the table
COMMENT ON TABLE group_members IS '用户组成员关系表';

-- Drop table if exists group_roles
DROP TABLE IF EXISTS group_roles;

-- Create table group_roles
CREATE TABLE group_roles (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   group_id             bigint NOT NULL COMMENT '用户组ID',
   role_id              bigint NOT NULL COMMENT '角色ID',
   CONSTRAINT fk_group_roles_group FOREIGN KEY (group_id) REFERENCES groups(id),
   CONSTRAINT fk_group_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Add comment to the table
COMMENT ON TABLE group_roles IS '用户组角色关系表';

-- Drop table if exists role_members
DROP TABLE IF EXISTS role_members;

-- Create table role_members
CREATE TABLE role_members (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   role_id              bigint NOT NULL COMMENT '角色ID',
   username             varchar(32) NOT NULL COMMENT '用户名',
   CONSTRAINT fk_role_members_role FOREIGN KEY (role_id) REFERENCES roles(id),
   CONSTRAINT fk_role_members_username FOREIGN KEY (username) REFERENCES users(username)
);

-- Add comment to the table
COMMENT ON TABLE role_members IS '角色成员关系表';

-- Drop table if exists privileges
DROP TABLE IF EXISTS privileges;

-- Create table privileges
CREATE TABLE privileges (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   superior_id          bigint COMMENT '上级ID',
   name                 varchar(64) NOT NULL COMMENT '名称',
   type                 character(1) NOT NULL COMMENT '类型',
   path                 varchar(127) COMMENT '路径',
   icon                 varchar(127) COMMENT '图标',
   description          varchar(512) COMMENT '描述',
   enabled           boolean NOT NULL DEFAULT true COMMENT '是否启用',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间'
);

-- Add comment to the table
COMMENT ON TABLE privileges IS '权限表';

-- Drop table if exists role_privileges
DROP TABLE IF EXISTS role_privileges;

-- Create table role_privileges
CREATE TABLE role_privileges (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   role_id              bigint NOT NULL COMMENT '角色ID',
   privilege_id         bigint NOT NULL COMMENT '权限ID',
   CONSTRAINT fk_role_privileges_role FOREIGN KEY (role_id) REFERENCES roles(id),
   CONSTRAINT fk_role_privileges_privilege FOREIGN KEY (privilege_id) REFERENCES privileges(id)
);

-- Add comment to the table
COMMENT ON TABLE role_privileges IS '角色权限关系表';

-- Drop table if exists dictionaries
DROP TABLE IF EXISTS dictionaries;

-- Create table dictionaries
CREATE TABLE dictionaries (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   name                 varchar(64) NOT NULL COMMENT '名称',
   superior_id          bigint COMMENT '上级ID',
   description          varchar(512) COMMENT '描述',
   enabled           boolean NOT NULL DEFAULT true COMMENT '是否启用',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间'
);

-- Add comment to the table
COMMENT ON TABLE dictionaries IS '字典表';

-- Drop table if exists messages
DROP TABLE IF EXISTS messages;

-- Create table messages
CREATE TABLE messages (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   title                varchar(255) NOT NULL COMMENT '标题',
   context              text COMMENT '内容',
   is_read              boolean NOT NULL DEFAULT false COMMENT
   -- 是否已读
   receiver             varchar(32) NOT NULL COMMENT '接收者',
   description          varchar(512) COMMENT '描述',
   enabled           boolean NOT NULL DEFAULT true COMMENT '是否启用',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间'
);

-- Add comment to the table
COMMENT ON TABLE messages IS '消息表';

-- Drop table if exists regions
DROP TABLE IF EXISTS regions;

-- Create table regions
CREATE TABLE regions (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   name                 varchar(64) NOT NULL COMMENT '名称',
   superior_id          bigint COMMENT '上级ID',
   postal_code          bigint COMMENT '邮政编码',
   area_code            bigint COMMENT '区号',
   description          varchar(512) COMMENT '描述',
   enabled           boolean NOT NULL DEFAULT true COMMENT '是否启用',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间'
);

-- Add comment to the table
COMMENT ON TABLE regions IS '地区表';

-- Drop table if exists access_logs
DROP TABLE IF EXISTS access_logs;

-- Create table access_logs
CREATE TABLE access_logs (
   id                   bigserial PRIMARY KEY NOT NULL COMMENT '主键',
   ip                   inet COMMENT 'IP地址',
   location             varchar(127) COMMENT '位置',
   context              text COMMENT '内容',
   user_agent           varchar(255) COMMENT '用户代理信息',
   http_method          varchar(10) COMMENT 'HTTP方法',
   url                  text COMMENT '请求URL',
   status_code          integer COMMENT 'HTTP状态码',
   response_time        bigint COMMENT '响应时间',
   referer              varchar(255) COMMENT '来源页面',
   session_id           varchar(50) COMMENT '会话标识符',
   device_type          varchar(20) COMMENT '设备类型',
   os                   varchar(50) COMMENT '操作系统',
   browser              varchar(50) COMMENT '浏览器',
   enabled           boolean NOT NULL DEFAULT true COMMENT '是否启用',
   created_by           varchar(32) NOT NULL COMMENT '创建者',
   created_date         timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   last_modified_by     varchar(32) NOT NULL COMMENT '最后修改者',
   last_modified_date   timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间'
);

-- Add comment to the table
COMMENT ON TABLE access_logs IS '访问日志表';
