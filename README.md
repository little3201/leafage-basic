# Leafage Basic

<p align="center">
 <img src="https://img.shields.io/badge/Spring%20Cloud-2020.0.3-green.svg" alt="Coverage Status">
 <img src="https://img.shields.io/badge/Spring%20Boot-2.4.6-green.svg" alt="Downloads">
 <img src="https://sonarcloud.io/api/project_badges/measure?project=little3201_leafage-basic&metric=alert_status" alt="Quality Gate Status">
 <img src="https://codecov.io/gh/little3201/leafage-basic/branch/master/graph/badge.svg?token=O8PZL1HK01"/>
</p>

### 本模块依赖

> 现在leafage-starter-parent 和 leafage-common 已经可以在maven repository 中存在了（groupId 替换成了top.leafage），可以直接通过maven repository使用

#### [leafage-starter-parent](https://github.com/little3201/) —— 不包含任何代码，仅仅作为所有模块依赖的版本控制；

#### [leafage-common](https://github.com/little3201/leafage-common) —— 包含非业务代码，提供通用工具类；

### leafage-basic

<a target="_blank" href="https://console.leafage.top"> 在线体验</a>， 目前仅开放assets模块的查询

### 介绍：

**本模块分为webmvc和webflux两个版本，分别关联响应的分支，具体请查看对应分支代码；**

两个版本的主要区别：

webmvc 版本：

|               依赖               |            说明            |
|:-------------------------------:|:-------------------------:|
|         Spring Boot Web         |           web框架          |
|         Spring Data Jpa         |          数据访问层         |
|               mysql             |           数据存储          |

webflux 版本：

|               依赖               |            说明            |
|:-------------------------------:|:-------------------------:|
|       Spring Boot Webflux       |           web框架          |
|  Spring Data Mongodb Reactive   |          数据访问层         |
|              mongodb            |           数据存储          |

**leafage-basic下分两个模块「assets」和 「hypervisor」**

### 「assets」

CMS服务相关接口，包括个人网站的资料维护，大体包含：

- 帖子 posts
- 类目 category
- 作品 portfolio

#### 目标功能：

- [x] 帖子
- [ ] 作品
- [x] 类目
- [x] 浏览量统计
- [x] 喜欢
- [x] 评论
- [x] 统计分析
- [ ] 资源下载

### 「hypervisor」

一套权限管理服务相关接口，包括用户、角色、权限、组的信息维护，大体包含：

- 用户 user
- 角色 role
- 权限 authority
- 分组 group
- 账户 account

#### 目标功能：

- [x] 用户
- [x] 角色
- [x] 权限
- [x] 分组
- [ ] 账户
