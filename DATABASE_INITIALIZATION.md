# 数据库初始化说明

## 概述

本项目使用SQL脚本进行数据初始化，所有初始数据都定义在 `src/main/resources/data.sql` 文件中。

## 初始化数据内容

### 1. 角色数据 (roles)
- **SUPER_ADMIN** - 超级管理员角色
- **TEST** - 测试用户角色

### 2. 权限数据 (permissions)
- **permission:create** - 创建权限
- **permission:read** - 查看权限
- **permission:update** - 更新权限
- **permission:delete** - 删除权限

### 3. 菜单数据 (menus)
包含完整的菜单层次结构：

#### 仪表板组 (group_dashboard)
- 工作台 (workbench)
- 分析页面 (analysis)

#### 页面组 (group_pages)
- **管理模块 (management)**
  - 用户管理 (management_user)
    - 用户资料 (management_user_profile)
    - 用户账户 (management_user_account)
  - 系统管理 (management_system)
    - 系统用户 (management_system_user)
    - 系统角色 (management_system_role)
    - 系统权限 (management_system_permission)

- **菜单层级 (menulevel)**
  - 一级菜单A (menulevel_1a)
  - 一级菜单B (menulevel_1b)
    - 二级菜单A (menulevel_1b_2a)
    - 二级菜单B (menulevel_1b_2b)
      - 三级菜单A (menulevel_1b_2b_3a)
      - 三级菜单B (menulevel_1b_2b_3b)

- **错误页面 (error)**
  - 403错误页面
  - 404错误页面
  - 500错误页面

#### UI组件组 (group_ui)
- **组件 (components)**
  - 图标组件
  - 动画组件
  - 滚动组件
  - 国际化组件
  - 上传组件
  - 图表组件
  - 提示组件

- **功能 (functions)**
  - 剪贴板功能
  - Token过期处理

#### 其他组 (group_others)
- 权限管理
- 权限页面测试
- 日历
- 看板
- 禁用页面
- 标签
- 链接
- 空白页面

### 4. 用户数据 (users)
- **admin** - 管理员用户 (admin@slash.com)
- **test** - 测试用户 (test@slash.com)
- **guest** - 访客用户 (guest@slash.com)

默认密码：`demo1234` (已加密存储)

### 5. 关联数据
- **用户角色关联** - 定义用户拥有的角色
- **角色权限关联** - 定义角色拥有的权限
- **角色菜单关联** - 定义角色可访问的菜单

## 权限分配

### 超级管理员 (SUPER_ADMIN)
- 拥有所有权限 (create, read, update, delete)
- 可以访问所有菜单

### 测试用户 (TEST)
- 拥有读取和更新权限
- 可以访问部分菜单（仪表板、部分管理功能、组件等）

## 使用方法

### 自动初始化
Spring Boot会在应用启动时自动执行 `data.sql` 脚本。

### 手动执行
如果需要手动执行初始化脚本：

```sql
-- 连接到数据库
mysql -u root -p rpworld_db

-- 执行初始化脚本
source /path/to/data.sql;
```

### 重置数据
如果需要重置所有数据：

```sql
-- 清空所有数据
DELETE FROM role_menus;
DELETE FROM role_permissions;
DELETE FROM user_roles;
DELETE FROM refresh_tokens;
DELETE FROM users;
DELETE FROM roles;
DELETE FROM permissions;
DELETE FROM menus;

-- 重置自增ID
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE roles AUTO_INCREMENT = 1;

-- 重新执行初始化脚本
source /path/to/data.sql;
```

## 测试账号

| 用户名 | 密码 | 角色 | 权限 |
|--------|------|------|------|
| admin | demo1234 | 超级管理员 | 所有权限 |
| test | demo1234 | 测试用户 | 读取、更新 |
| guest | demo1234 | 测试用户 | 读取、更新 |

## 注意事项

1. **密码加密**：所有用户密码都使用BCrypt加密存储
2. **菜单层次**：菜单支持多级层次结构，通过parent_id字段关联
3. **权限控制**：权限采用"资源:操作"的格式，如"permission:create"
4. **国际化**：菜单名称使用国际化键值，如"sys.nav.dashboard"
5. **图标支持**：菜单支持多种图标格式，包括本地图标和外部图标库

## 扩展数据

如需添加新的菜单、权限或角色，请按以下步骤操作：

1. 在相应的INSERT语句中添加新数据
2. 更新角色权限关联表 (role_permissions)
3. 更新角色菜单关联表 (role_menus)
4. 重启应用或手动执行SQL脚本

## 数据库表结构

详细的表结构请参考：
- `src/main/java/com/bezkoder/springjwt/models/` 目录下的实体类
- `COMPLETE_USER_RESPONSE_EXAMPLE.md` 文档中的表结构说明
