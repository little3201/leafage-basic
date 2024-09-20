# auth

### 1. 鉴权：

通用鉴权：包括账号密码登录、oauth2；

### 2. 授权：

接口授权检查，授权配置；

#### 核心依赖

|                     依赖                      |  说明   |
|:-------------------------------------------:|:-----:|
|               Spring Boot Web               | web框架 |
|    spring-cloud-starter-consul-discovery    | 服务注册  |
| spring-security-oauth2-authorization-server | 安全框架  |
|        spring-boot-starter-actuator         | 健康监控  |
|       micrometer-registry-prometheus        | 监控接口  |
|        spring-boot-starter-data-jdbc        | 数据访问  |
|                  postgres                   | 数据存储  |
|                  caffeine                   | 内存缓存  |