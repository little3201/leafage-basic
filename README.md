# Leafage

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.0-green.svg" alt="Spring Boot 4.1.0" />
  <img src="https://sonarcloud.io/api/project_badges/measure?project=little3201_leafage&metric=alert_status" alt="Quality Gate Status" />
  <img src="https://sonarcloud.io/api/project_badges/measure?project=little3201_leafage&metric=code_smells" alt="Code Smells" />
  <img src="https://sonarcloud.io/api/project_badges/measure?project=little3201_leafage&metric=bugs" alt="Bugs" />
  <img src="https://sonarcloud.io/api/project_badges/measure?project=little3201_leafage&metric=coverage" alt="Coverage" />
</p>

## 项目简介

Leafage 是一个基于 Spring Boot 的后台管理系统。当前 `develop` 分支采用 BFF（Backend for Frontend）架构：浏览器只与 Gateway 建立会话，Gateway 完成 OAuth2/OIDC 登录并将访问令牌转发给业务服务，前端不会直接持有 JWT。

```text
Browser / leafage-ui
        │
        ▼
Gateway :8760 ───────► Auth :8761
        │                 OAuth2 / OIDC 授权服务器
        │
        └──────────────► Hypervisor :8762
                          业务资源服务器
```

前端项目见 [leafage-ui](https://github.com/little3201/leafage-ui)。

## 模块说明

| 模块 | 端口 | 职责 |
| --- | ---: | --- |
| `gateway` | 8760 | BFF 网关、OAuth2 Client 登录、会话、CSRF、路由与 Token Relay |
| `auth` | 8761 | OAuth2/OIDC 授权服务器、登录页、用户与授权数据持久化 |
| `hypervisor` | 8762 | 业务资源服务器；提供用户、角色、权限、用户组、资产、文件、消息、审计和调度等 API |

## 技术栈

- Java 21+
- Spring Boot 4
- Spring Security、Spring Authorization Server
- Spring Cloud Gateway Server Web MVC
- Spring Data JPA / JDBC
- PostgreSQL
- Actuator、Micrometer Prometheus

| 认证模式 | 分支 | 主要实现 |
| --- | --- | --- |
| BFF | `develop` | JPA、Spring Cloud Gateway Server Web MVC |
| PKCE | `webflux` | R2DBC、Spring Cloud Gateway WebFlux |
| JWT | `jdbc` | Spring Data JDBC、Spring Cloud Gateway Server Web MVC |

## 本地运行

### 前置条件

- JDK 21 或更高版本
- PostgreSQL
- Maven 3.9+，或使用仓库提供的 Maven Wrapper

### 配置数据库

开发环境的 `auth` 与 `hypervisor` 都从环境变量读取数据库连接信息：

```text
DB_HOST=127.0.0.1
DB_PORT=5432
DB_USERNAME=postgres
DB_PASSWORD=your-password
```

默认连接的数据库名为 `postgres`。首次运行前，请根据部署方式初始化认证与业务数据：

- `auth/src/main/resources/schema.sql`：认证服务器的 OAuth2 客户端、授权和用户数据；该脚本含有 `DROP TABLE`，只应在初始化环境执行。
- `hypervisor/src/main/resources/schema.sql`：Hypervisor 的完整业务表结构参考脚本。

开发配置中 Hypervisor 使用 `spring.jpa.hibernate.ddl-auto=update`；生产环境应使用版本化数据库迁移，而不是依赖该配置自动建表。

### 启动服务

根工程仅用于聚合模块，不能直接执行 `mvn spring-boot:run`。请按以下顺序在仓库根目录分别启动：

```bash
mvn -pl auth spring-boot:run
mvn -pl hypervisor spring-boot:run
mvn -pl gateway spring-boot:run
```

使用 Maven Wrapper 时，将 `mvn` 替换为 Unix/macOS 下的 `./mvnw`，或 Windows 下的 `./mvnw.cmd`。三个服务默认使用 `dev` Profile；本地前端默认地址为 `http://127.0.0.1:5173`。

## 测试与打包

执行全部测试：

```bash
mvn test
```

构建所有可执行 JAR：

```bash
mvn package -DskipTests
```

## Docker 镜像

先完成 JAR 打包，再以各服务目录作为 Docker 构建上下文：

```bash
docker build -t leafage-auth ./auth
docker build -t leafage-hypervisor ./hypervisor
docker build -t leafage-gateway ./gateway
```

部署时请通过环境变量提供数据库凭据和服务地址；不要将生产密码或 OAuth2 Client Secret 写入配置文件。

## 贡献指南

欢迎参与贡献：

1. Fork 本仓库。
2. 新建分支 `feature/xxx`。
3. 运行相关测试与 Checkstyle。
4. 提交代码并发起 Pull Request。

## License

[Apache License 2.0](LICENSE)
