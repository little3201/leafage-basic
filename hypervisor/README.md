# hypervisor

### 1. 基础管理：

提供基础的数据管理、包括人员、角色、权限、分组（组织）、字典；

### 2. 接口安全：

支持oauth2安全认证，接口细粒度的安全保护；

#### 核心依赖

|                     依赖                     |  说明   |
|:------------------------------------------:|:-----:|
|              Spring Boot Web               | web框架 |
|   spring-cloud-starter-consul-discovery    | 服务注册  |
| spring-boot-starter-oauth2-resource-server | 接口安全  |
|        spring-boot-starter-actuator        | 健康监控  |
|       micrometer-registry-prometheus       | 监控接口  |
|              Spring Data Jpa               | 数据访问  |
|                  postgres                  | 数据存储  |
|                  caffeine                  | 内存缓存  |
