# 数据初始化验证指南

## 验证步骤

### 1. 启动应用
```bash
mvn spring-boot:run
```

### 2. 测试登录接口

#### 管理员登录
```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "demo1234"
  }'
```

**预期响应**：
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
      "email": "admin@slash.com",
      "menu": [
        // 应该包含所有菜单项
      ],
      "permissions": [
        {
          "id": "permission_create",
          "name": "permission-create",
          "code": "permission:create"
        },
        {
          "id": "permission_read",
          "name": "permission-read",
          "code": "permission:read"
        },
        {
          "id": "permission_update",
          "name": "permission-update",
          "code": "permission:update"
        },
        {
          "id": "permission_delete",
          "name": "permission-delete",
          "code": "permission:delete"
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

#### 测试用户登录
```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "password": "demo1234"
  }'
```

**预期响应**：
- 用户信息正确
- 菜单数量较少（只有部分菜单）
- 权限只有读取和更新权限

### 3. 验证数据库数据

#### 检查用户数据
```sql
SELECT id, username, email, avatar, nickname FROM users;
```

#### 检查角色数据
```sql
SELECT id, name, code, description FROM roles;
```

#### 检查菜单数据
```sql
SELECT id, name, code, parent_id, path, icon FROM menus ORDER BY sort_order;
```

#### 检查权限数据
```sql
SELECT id, name, code, description FROM permissions;
```

#### 检查关联数据
```sql
-- 用户角色关联
SELECT u.username, r.name as role_name, r.code as role_code 
FROM users u 
JOIN user_roles ur ON u.id = ur.user_id 
JOIN roles r ON ur.role_id = r.id;

-- 角色权限关联
SELECT r.name as role_name, p.name as permission_name, p.code as permission_code
FROM roles r
JOIN role_permissions rp ON r.id = rp.role_id
JOIN permissions p ON rp.permission_id = p.id;

-- 角色菜单关联
SELECT r.name as role_name, m.name as menu_name, m.code as menu_code
FROM roles r
JOIN role_menus rm ON r.id = rm.role_id
JOIN menus m ON rm.menu_id = m.id
ORDER BY r.name, m.sort_order;
```

### 4. 验证菜单层次结构

检查菜单的父子关系是否正确：

```sql
-- 查看顶级菜单
SELECT * FROM menus WHERE parent_id = '';

-- 查看子菜单
SELECT * FROM menus WHERE parent_id = 'group_dashboard';

-- 查看三级菜单
SELECT * FROM menus WHERE parent_id LIKE 'menulevel_1b_2b%';
```

### 5. 测试权限控制

使用获取到的accessToken测试受保护的接口：

```bash
# 使用admin token测试
curl -X GET http://localhost:8080/api/test/admin \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"

# 使用test token测试
curl -X GET http://localhost:8080/api/test/user \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## 常见问题排查

### 1. 登录失败
- 检查用户名和密码是否正确
- 检查数据库连接是否正常
- 检查用户数据是否正确插入

### 2. 菜单数据为空
- 检查菜单数据是否正确插入
- 检查角色菜单关联是否正确
- 检查UserInfoService是否正确查询数据

### 3. 权限数据为空
- 检查权限数据是否正确插入
- 检查角色权限关联是否正确

### 4. 数据库连接问题
- 检查application.properties中的数据库配置
- 确保MySQL服务正在运行
- 确保数据库和表已创建

## 数据统计验证

执行以下查询来验证数据完整性：

```sql
-- 数据统计
SELECT 
  (SELECT COUNT(*) FROM users) as user_count,
  (SELECT COUNT(*) FROM roles) as role_count,
  (SELECT COUNT(*) FROM menus) as menu_count,
  (SELECT COUNT(*) FROM permissions) as permission_count,
  (SELECT COUNT(*) FROM user_roles) as user_role_count,
  (SELECT COUNT(*) FROM role_permissions) as role_permission_count,
  (SELECT COUNT(*) FROM role_menus) as role_menu_count;
```

**预期结果**：
- user_count: 3
- role_count: 2
- menu_count: 50+ (根据实际菜单数量)
- permission_count: 4
- user_role_count: 3
- role_permission_count: 6 (admin有4个权限，test有2个权限)
- role_menu_count: 50+ (根据实际菜单分配)

## 性能测试

### 登录性能测试
```bash
# 使用ab工具测试登录接口性能
ab -n 100 -c 10 -p login.json -T application/json http://localhost:8080/api/auth/signin
```

其中login.json内容：
```json
{
  "username": "admin",
  "password": "demo1234"
}
```

通过以上验证步骤，您可以确保数据初始化脚本正常工作，系统具备完整的用户管理功能。
