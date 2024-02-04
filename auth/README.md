# Leafage Basic

<p align="center">
 <img src="https://img.shields.io/badge/Spring%20Cloud-2022.0.1-green.svg" alt="Coverage Status">
 <img src="https://img.shields.io/badge/Spring%20Boot-3.0.4-green.svg" alt="Downloads">
 <img src="https://sonarcloud.io/api/project_badges/measure?project=little3201_leafage-auth&metric=alert_status" alt="Quality Gate Status">
 <img src="https://sonarcloud.io/api/project_badges/measure?project=little3201_leafage-auth&metric=coverage"/>
</p>

### 依赖

> 现在leafage-starter-parent 和 leafage-common 已经可以在maven repository 中存在了（groupId 替换成了top.leafage），可以直接通过maven repository使用

#### [leafage-starter-parent](https://github.com/little3201/) —— 不包含任何代码，仅仅作为所有模块依赖的版本控制；

### 介绍：

<a target="_blank" href="https://console.abeille.top"> 在线体验</a>， 未登录状态仅可访问部分接口

#### 1. 代码质量：

- 完全采用 restful 风格，不做过多的封装，统一返回 ResponseEntity 对象类型，尽可能相关的 HttpStatus；
- 代码覆盖率，总体超过90%，其中controller和service接近line 100%；
- 代码全部通过 sonarlint 扫描，代码警告除了IDEA 提示的未被使用的setter, getter之外，没有任何异常提醒；

#### 2. 概况：

- 同一认证中心
