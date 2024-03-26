-- Drop table if exists oauth_registered_client;
DROP TABLE IF EXISTS oauth_registered_client;

-- Create table oauth_registered_client
CREATE TABLE oauth_registered_client (
   id                         VARCHAR(100) NOT NULL,
   client_id                  VARCHAR(100) NOT NULL,
   client_id_issued_at        TIMESTAMP NOT NULL,
   client_secret              VARCHAR(200) NOT NULL,
   client_secret_expires_at   TIMESTAMP,
   client_name                VARCHAR(200),
   client_authorization_methods VARCHAR(1000),
   authorization_grant_types  VARCHAR(1000),
   redirect_uris              VARCHAR(1000),
   scopes                     VARCHAR(1000),
   client_settings            VARCHAR(2000),
   token_settings             VARCHAR(2000),
   PRIMARY KEY (id)
);

-- Add comment to the table and columns
COMMENT ON TABLE oauth_registered_client IS 'oauth_registered_client表';
COMMENT ON COLUMN oauth_registered_client.id IS '主键';
COMMENT ON COLUMN oauth_registered_client.client_id IS '客户端ID';
COMMENT ON COLUMN oauth_registered_client.client_id_issued_at IS '生效时间';
COMMENT ON COLUMN oauth_registered_client.client_secret IS '密钥';
COMMENT ON COLUMN oauth_registered_client.client_secret_expires_at IS '密钥失效时间';
COMMENT ON COLUMN oauth_registered_client.client_name IS '名称';
COMMENT ON COLUMN oauth_registered_client.client_authorization_methods IS '认证方法';
COMMENT ON COLUMN oauth_registered_client.authorization_grant_types IS '授权方式';
COMMENT ON COLUMN oauth_registered_client.redirect_uris IS '跳转连接';
COMMENT ON COLUMN oauth_registered_client.scopes IS '作用域';
COMMENT ON COLUMN oauth_registered_client.client_settings IS '客户端设置';
COMMENT ON COLUMN oauth_registered_client.token_settings IS 'token 设置';

-- Drop table if exists oauth_authorization
DROP TABLE IF EXISTS oauth_authorization;

-- Create table oauth_authorization
CREATE TABLE oauth_authorization (
   id                            VARCHAR(100) NOT NULL,
   registered_client_id          VARCHAR(100) NOT NULL,
   principal_name                TIMESTAMP NOT NULL,
   authorization_grant_type      VARCHAR(1000) NOT NULL,
   attributes                    VARCHAR(4000),
   state                         VARCHAR(500),
   authorization_code_value      BYTEA,
   authorization_code_issued_at  TIMESTAMP,
   authorization_code_expires_at TIMESTAMP,
   authorization_code_metadata   VARCHAR(2000),
   access_token_value            BYTEA,
   access_token_issued_at        TIMESTAMP,
   access_token_expires_at       TIMESTAMP,
   access_token_metadata         VARCHAR(2000),
   access_token_type             VARCHAR(100),
   access_token_scopes           VARCHAR(1000),
   oidc_id_token_value           BYTEA,
   oidc_id_token_issued_at       TIMESTAMP,
   oidc_id_token_expires_at      TIMESTAMP,
   oidc_id_token_metadata        VARCHAR(2000),
   refresh_token_value           BYTEA,
   refresh_token_issued_at       TIMESTAMP,
   refresh_token_expires_at      TIMESTAMP,
   refresh_token_metadata        VARCHAR(2000),
   PRIMARY KEY (id)
);

-- Add comment to the table and columns
COMMENT ON TABLE oauth_authorization IS 'oauth_authorization表';
COMMENT ON COLUMN oauth_authorization.id IS '主键';
COMMENT ON COLUMN oauth_authorization.registered_client_id IS '客户端ID';
COMMENT ON COLUMN oauth_authorization.principal_name IS '认证账号';
COMMENT ON COLUMN oauth_authorization.authorization_grant_type IS '授权类型';
COMMENT ON COLUMN oauth_authorization.attributes IS '参数';
COMMENT ON COLUMN oauth_authorization.state IS '状态';
COMMENT ON COLUMN oauth_authorization.authorization_code_value IS 'authorization code';
COMMENT ON COLUMN oauth_authorization.authorization_code_issued_at IS 'authorization code生效时间';
COMMENT ON COLUMN oauth_authorization.authorization_code_expires_at IS 'authorization code失效时间';
COMMENT ON COLUMN oauth_authorization.authorization_code_metadata IS 'authorization code 元数据';
COMMENT ON COLUMN oauth_authorization.access_token_value IS 'access token';
COMMENT ON COLUMN oauth_authorization.access_token_issued_at IS 'access token 生效时间';
COMMENT ON COLUMN oauth_authorization.access_token_expires_at IS 'access_token 失效时间';
COMMENT ON COLUMN oauth_authorization.access_token_metadata IS 'access token元数据';
COMMENT ON COLUMN oauth_authorization.access_token_type IS 'access token 类型';
COMMENT ON COLUMN oauth_authorization.access_token_scopes IS 'access token 域';
COMMENT ON COLUMN oauth_authorization.oidc_id_token_value IS 'oidc token';
COMMENT ON COLUMN oauth_authorization.oidc_id_token_issued_at IS 'oidc token 生效时间';
COMMENT ON COLUMN oauth_authorization.oidc_id_token_expires_at IS 'oidc token 失效时间';
COMMENT ON COLUMN oauth_authorization.oidc_id_token_metadata IS 'oidc token 元数据';
COMMENT ON COLUMN oauth_authorization.refresh_token_value IS 'refresh token';
COMMENT ON COLUMN oauth_authorization.refresh_token_issued_at IS 'refresh token 生效时间';
COMMENT ON COLUMN oauth_authorization.refresh_token_expires_at IS 'refresh token 失效时间';
COMMENT ON COLUMN oauth_authorization.refresh_token_metadata IS 'refresh token 元数据';

-- Drop table if exists oauth_authorization_consent
DROP TABLE IF EXISTS oauth_authorization_consent;

-- Create table oauth_authorization_consent
CREATE TABLE oauth_authorization_consent (
   registered_client_id VARCHAR(100) NOT NULL,
   principal_name       VARCHAR(200) NOT NULL,
   authorities          VARCHAR(1000) NOT NULL,
   PRIMARY KEY ()
);

-- Add comment to the table and columns
COMMENT ON TABLE oauth_authorization_consent IS 'oauth_authorization_consent表';
COMMENT ON COLUMN oauth_authorization_consent.registered_client_id IS '客户端ID';
COMMENT ON COLUMN oauth_authorization_consent.principal_name IS '认证账号';
COMMENT ON COLUMN oauth_authorization_consent.authorities IS '权限';
