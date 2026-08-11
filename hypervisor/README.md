# Leafage Hypervisor

`hypervisor` 是 Leafage 的业务资源服务器，默认监听 `8762` 端口。它校验由 `auth` 签发的 JWT，并以细粒度权限保护后台管理 API。在 BFF 部署中，客户端应通过 Gateway 的 `/hypervisor/**` 路由访问本服务。

## 功能模块

- 系统管理：用户、角色、权限、用户组、字典和日历事件；
- 资产管理：模板、分区、区域、文章、评论、归档和报表；
- 文件与消息：文件记录、文件上传、消息与收件箱；
- 审计与日志：审计日志、巡检、访问日志和操作日志；
- 调度：定时任务及执行日志；
- 开发配置：连接、模块、片段、样本、脚本与方案生成。

## 安全模型

本服务是 OAuth2 Resource Server。它使用授权服务器 `http://localhost:8761` 作为 JWT issuer，并将 JWT 中的 `authorities` 声明映射为 Spring Security 权限。Controller 使用 `@PreAuthorize` 对角色与权限码进行访问控制。

## 数据库配置

开发环境通过环境变量连接 PostgreSQL：

```text
DB_HOST=127.0.0.1
DB_PORT=5432
DB_USERNAME=postgres
DB_PASSWORD=your-password
```

[schema.sql](src/main/resources/schema.sql) 包含完整业务表结构参考。开发配置使用 `spring.jpa.hibernate.ddl-auto=update`；生产环境应使用版本化数据库迁移并设置 `ddl-auto=validate`。

## 运行

启动前请先启动 `auth`：

```bash
mvn -pl hypervisor spring-boot:run
```

或：

```bash
mvn -pl hypervisor package -DskipTests
java -jar hypervisor/target/hypervisor-0.0.1.jar
```

## 核心依赖

| 依赖 | 用途 |
| --- | --- |
| Spring Web MVC | REST API |
| Spring Data JPA | 业务数据持久化 |
| Spring Security OAuth2 Resource Server | JWT 校验与资源保护 |
| Spring Validation、AspectJ | 请求校验与横切处理 |
| FreeMarker、Apache POI | 模板和导入导出支持 |
| Leafage Common | JPA、日志与 POI 公共能力 |
| Actuator、Micrometer Prometheus | 健康检查与指标 |

## Docker

```bash
mvn -pl hypervisor package -DskipTests
docker build -t leafage-hypervisor ./hypervisor
```
