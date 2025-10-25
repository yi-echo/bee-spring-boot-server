# 🐝 Bee Spring Boot Server

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-red?logo=java)](https://www.oracle.com/java/)
[![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.5+-blue?logo=java)](https://baomidou.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> 💡 一个轻量、模块化、可拓展的 Spring Boot 后端脚手架  
> 🚀 与 [bee-react-admin](https://github.com/yi-echo/bee-react-admin) 前端模板完美配合， 面向前后端学习者 / 个人开发者，帮助你快速掌握前后端分离项目的完整开发流程。

---

## ✨ 项目简介

Bee Spring Boot Server 是一个基于 **Spring Boot 3 + MyBatis-Plus + JWT + Redis** 的后端服务模板，  
内置通用权限控制、用户登录、角色管理、菜单接口等基础功能。

🧩 你可以配合前端项目：
> [🖥️ bee-react-admin](https://github.com/yi-echo/bee-react-admin)

实现完整的 **前后端分离** 管理系统架构。

---

## ⚙️ 技术栈

| 模块 | 技术栈 |
|------|--------|
| 核心框架 | Spring Boot 3.x |
| ORM 框架 | MyBatis-Plus |
| 安全认证 | JWT + Spring Security |
| 缓存中间件 | Redis |
| 数据库 | MySQL 8+ |
| 文档生成 | Swagger3 / Knife4j |
| 构建工具 | Maven |
| 部署 | Dockerfile + Docker Compose |

---

## 📦 功能特性

- 🔐 用户登录 / 角色 / 权限管理
- 🧭 菜单与资源路由控制
- 🧰 通用 CRUD 模板（基于 MyBatis-Plus）
- 🪄 统一响应结构与异常处理
- 🧩 Swagger3 接口文档自动生成
- ⚡ 支持 Docker 快速部署
- 🔄 可无缝对接 [bee-react-admin](https://github.com/yi-echo/bee-react-admin)

---

## 🧑‍💻 快速开始

### 1️⃣ 克隆项目
```bash
git clone https://github.com/yi-echo/bee-spring-boot-server.git
cd bee-spring-boot-server
