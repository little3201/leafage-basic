# hypervisor

<p align="center">
 <img src="https://img.shields.io/badge/Spring%20Cloud-2020.0.2-green.svg" alt="Coverage Status">
 <img src="https://img.shields.io/badge/Spring%20Boot-2.4.4-green.svg" alt="Downloads">
</p>

一套权限管理服务相关接口，包括用户、角色、权限、组的信息维护，大体包含：

- 用户 user
- 角色 role
- 权限 authority
- 分组 group
- 账户 account

#### 目标功能：

- [x] 用户
- [ ] 角色
- [x] 权限
- [ ] 分组
- [ ] 账户
- [ ] 统计分析

<a href="#" target="_blank">部署文档</a> | <a target="_blank" href="https://console.leafage.top"> 在线体验</a>

#### 核心依赖

|               依赖               |            说明            |
|:-------------------------------:|:-------------------------:|
|       Spring Boot Webflux       |           web框架          |
|  Spring Data Mongodb Reactive   |          数据访问层         |
|              mongodb            |           数据存储          |
