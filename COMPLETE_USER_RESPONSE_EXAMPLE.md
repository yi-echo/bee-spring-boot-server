# 完整用户响应数据结构示例

## 概述

现在您的登录接口将返回包含完整用户信息的响应，包括用户的菜单、权限、角色等详细信息。

## 响应结构

### 登录成功响应示例

```json
{
  "code": 200,
  "message": "login successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
    "user": {
      "id": "1",
      "username": "admin",
      "avatar": "https://avatars.githubusercontent.com/u/15075420",
      "email": "admin@example.com",
      "menu": [
        {
          "id": "group_dashboard",
          "name": "sys.nav.dashboard",
          "code": "dashboard",
          "parentId": "",
          "path": "/dashboard",
          "icon": "dashboard",
          "sortOrder": 1
        },
        {
          "id": "group_user",
          "name": "sys.nav.user",
          "code": "user",
          "parentId": "",
          "path": "/user",
          "icon": "user",
          "sortOrder": 2
        },
        {
          "id": "group_user_list",
          "name": "sys.nav.user.list",
          "code": "user-list",
          "parentId": "group_user",
          "path": "/user/list",
          "icon": "list",
          "sortOrder": 1
        }
      ],
      "permissions": [
        {
          "id": "permission_create",
          "name": "permission-create",
          "code": "permission:create",
          "description": "创建权限",
          "resource": "permission",
          "action": "create"
        },
        {
          "id": "permission_read",
          "name": "permission-read",
          "code": "permission:read",
          "description": "查看权限",
          "resource": "permission",
          "action": "read"
        },
        {
          "id": "permission_update",
          "name": "permission-update",
          "code": "permission:update",
          "description": "更新权限",
          "resource": "permission",
          "action": "update"
        },
        {
          "id": "permission_delete",
          "name": "permission-delete",
          "code": "permission:delete",
          "description": "删除权限",
          "resource": "permission",
          "action": "delete"
        }
      ],
      "roles": [
        {
          "id": "1",
          "name": "ROLE_ADMIN",
          "code": "SUPER_ADMIN",
          "description": "超级管理员"
        }
      ]
    }
  },
  "timestamp": 1703123456789
}
```

## 数据结构说明

### 1. 顶层响应结构
- `code`: 响应状态码
- `message`: 响应消息
- `data`: 包含登录数据的对象
- `timestamp`: 响应时间戳

### 2. data 对象结构
- `accessToken`: JWT访问令牌
- `tokenType`: 令牌类型（Bearer）
- `refreshToken`: 刷新令牌
- `user`: 用户完整信息对象

### 3. user 对象结构
- `id`: 用户ID
- `username`: 用户名
- `avatar`: 用户头像URL
- `email`: 用户邮箱
- `menu`: 用户可访问的菜单列表
- `permissions`: 用户拥有的权限列表
- `roles`: 用户拥有的角色列表

### 4. menu 对象结构
- `id`: 菜单ID
- `name`: 菜单名称（支持国际化）
- `code`: 菜单代码
- `parentId`: 父菜单ID（空字符串表示顶级菜单）
- `path`: 菜单路径
- `icon`: 菜单图标
- `sortOrder`: 排序顺序

### 5. permission 对象结构
- `id`: 权限ID
- `name`: 权限名称
- `code`: 权限代码（格式：资源:操作）
- `description`: 权限描述
- `resource`: 资源名称
- `action`: 操作类型

### 6. role 对象结构
- `id`: 角色ID
- `name`: 角色名称
- `code`: 角色代码
- `description`: 角色描述

## 数据库表结构

### 用户表 (users)
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(120) NOT NULL,
    avatar VARCHAR(255),
    nickname VARCHAR(50),
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    last_login_time TIMESTAMP
);
```

### 角色表 (roles)
```sql
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name ENUM('ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN') NOT NULL,
    code VARCHAR(50),
    description VARCHAR(255)
);
```

### 菜单表 (menus)
```sql
CREATE TABLE menus (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL,
    parent_id VARCHAR(50),
    path VARCHAR(255),
    icon VARCHAR(50),
    sort_order INT,
    is_active BOOLEAN DEFAULT TRUE
);
```

### 权限表 (permissions)
```sql
CREATE TABLE permissions (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    resource VARCHAR(50),
    action VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE
);
```

### 关联表
```sql
-- 用户角色关联表
CREATE TABLE user_roles (
    user_id BIGINT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- 角色权限关联表
CREATE TABLE role_permissions (
    role_id INT,
    permission_id VARCHAR(50),
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (permission_id) REFERENCES permissions(id)
);

-- 角色菜单关联表
CREATE TABLE role_menus (
    role_id INT,
    menu_id VARCHAR(50),
    PRIMARY KEY (role_id, menu_id),
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (menu_id) REFERENCES menus(id)
);
```

## 前端使用示例

### JavaScript/TypeScript
```javascript
// 登录后处理响应
function handleLoginResponse(response) {
  if (response.code === 200) {
    const { accessToken, refreshToken, user } = response.data;
    
    // 存储token
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    
    // 存储用户信息
    localStorage.setItem('userInfo', JSON.stringify(user));
    
    // 构建菜单
    buildMenu(user.menu);
    
    // 设置权限
    setPermissions(user.permissions);
    
    // 设置角色
    setRoles(user.roles);
  }
}

// 构建菜单
function buildMenu(menuItems) {
  const menuContainer = document.getElementById('menu-container');
  
  menuItems.forEach(item => {
    if (!item.parentId) { // 顶级菜单
      const menuItem = createMenuItem(item);
      menuContainer.appendChild(menuItem);
    }
  });
}

// 检查权限
function hasPermission(permissionCode) {
  const userInfo = JSON.parse(localStorage.getItem('userInfo'));
  return userInfo.permissions.some(p => p.code === permissionCode);
}

// 检查角色
function hasRole(roleCode) {
  const userInfo = JSON.parse(localStorage.getItem('userInfo'));
  return userInfo.roles.some(r => r.code === roleCode);
}
```

## 优势

1. **一次请求获取所有信息**: 登录后前端可以获取用户的所有必要信息
2. **权限控制**: 前端可以根据权限和角色控制界面显示
3. **菜单动态生成**: 根据用户角色动态生成菜单
4. **扩展性强**: 可以轻松添加新的用户属性、菜单、权限等
5. **国际化支持**: 菜单名称支持国际化
6. **层次化结构**: 菜单支持父子关系，权限支持资源-操作模式

现在您的系统具备了完整的企业级用户管理功能！
