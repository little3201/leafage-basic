# assets

### 1. 资源管理：

对通用数据资源提供管理功能，包括行政区划、站点帖子（以及评论等）；

### 2. 接口安全：

支持oauth2安全认证，接口细粒度的安全保护；

#### 核心依赖

|                     依赖                     |  说明   |
|:------------------------------------------:|:-----:|
|            Spring Boot WebFlux             | web框架 |
|   spring-cloud-starter-consul-discovery    | 服务注册  |
| spring-boot-starter-oauth2-resource-server | 接口安全  |
|        spring-boot-starter-actuator        | 健康监控  |
|       micrometer-registry-prometheus       | 监控接口  |
|             Spring Data R2DBC              | 数据访问层 |
|                  postgres                  | 数据存储  |
|                  caffeine                  | 内存缓存  |
