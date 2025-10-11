# RefreshToken 功能使用指南

## 概述

本项目已成功添加了RefreshToken功能，实现了JWT token的自动刷新机制，提高了用户体验和安全性。

## 功能特点

### 1. 双Token机制
- **Access Token**: 短期有效（24小时），用于API访问
- **Refresh Token**: 长期有效（7天），用于刷新Access Token

### 2. 安全性
- RefreshToken存储在数据库中，支持撤销
- 每次刷新都会生成新的RefreshToken
- 自动删除过期的RefreshToken

### 3. 用户体验
- 用户无需频繁登录
- 自动处理token过期
- 支持登出功能

## API接口

### 1. 登录接口
**POST** `/api/auth/signin`

**请求体:**
```json
{
  "username": "admin",
  "password": "123456"
}
```

**响应:**
```json
{
  "code": 200,
  "message": "login successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "roles": ["ROLE_ADMIN"]
  },
  "timestamp": 1703123456789
}
```

### 2. 刷新Token接口
**POST** `/api/auth/refreshtoken`

**请求体:**
```json
{
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
}
```

**响应:**
```json
{
  "code": 200,
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "refreshToken": "550e8400-e29b-41d4-a716-446655440001",
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "roles": ["ROLE_ADMIN"]
  },
  "timestamp": 1703123456789
}
```

### 3. 登出接口
**POST** `/api/auth/signout`

**响应:**
```json
{
  "code": 200,
  "message": "User signed out successfully",
  "timestamp": 1703123456789
}
```

## 数据库表结构

### refresh_tokens表
```sql
CREATE TABLE refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 配置说明

### application.properties
```properties
# JWT配置
bezkoder.app.jwtSecret= ======================BezKoder=Spring===========================
bezkoder.app.jwtExpirationMs=86400000          # Access Token过期时间（24小时）
bezkoder.app.jwtRefreshExpirationMs=604800000  # Refresh Token过期时间（7天）
```

## 前端集成示例

### JavaScript/TypeScript
```javascript
class AuthService {
  constructor() {
    this.accessToken = localStorage.getItem('accessToken');
    this.refreshToken = localStorage.getItem('refreshToken');
  }

  // 登录
  async login(username, password) {
    const response = await fetch('/api/auth/signin', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    });

    const data = await response.json();
    if (data.code === 200) {
      this.accessToken = data.data.accessToken;
      this.refreshToken = data.data.refreshToken;
      localStorage.setItem('accessToken', this.accessToken);
      localStorage.setItem('refreshToken', this.refreshToken);
    }
    return data;
  }

  // 刷新Token
  async refreshToken() {
    if (!this.refreshToken) {
      throw new Error('No refresh token available');
    }

    const response = await fetch('/api/auth/refreshtoken', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ refreshToken: this.refreshToken }),
    });

    const data = await response.json();
    if (data.code === 200) {
      this.accessToken = data.data.accessToken;
      this.refreshToken = data.data.refreshToken;
      localStorage.setItem('accessToken', this.accessToken);
      localStorage.setItem('refreshToken', this.refreshToken);
    }
    return data;
  }

  // 自动处理Token过期
  async apiCall(url, options = {}) {
    let response = await fetch(url, {
      ...options,
      headers: {
        ...options.headers,
        'Authorization': `Bearer ${this.accessToken}`,
      },
    });

    // 如果token过期，尝试刷新
    if (response.status === 401) {
      try {
        await this.refreshToken();
        // 重试请求
        response = await fetch(url, {
          ...options,
          headers: {
            ...options.headers,
            'Authorization': `Bearer ${this.accessToken}`,
          },
        });
      } catch (error) {
        // 刷新失败，跳转到登录页
        this.logout();
        throw error;
      }
    }

    return response;
  }

  // 登出
  logout() {
    this.accessToken = null;
    this.refreshToken = null;
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }
}
```

## 错误处理

### 常见错误响应

1. **RefreshToken过期**
```json
{
  "code": 401,
  "message": "Refresh token was expired. Please make a new signin request",
  "timestamp": 1703123456789
}
```

2. **RefreshToken不存在**
```json
{
  "code": 500,
  "message": "Refresh token is not in database!",
  "timestamp": 1703123456789
}
```

3. **参数校验失败**
```json
{
  "code": 400,
  "message": "pramas validation failed",
  "data": {
    "refreshToken": "refresh token cannot be blank"
  },
  "timestamp": 1703123456789
}
```

## 安全建议

1. **存储安全**
   - 前端使用httpOnly cookie存储refreshToken（推荐）
   - 或使用localStorage但要注意XSS攻击

2. **传输安全**
   - 始终使用HTTPS传输
   - 避免在URL中传递token

3. **过期策略**
   - Access Token设置较短的过期时间
   - Refresh Token设置较长的过期时间
   - 支持手动撤销refreshToken

4. **监控和日志**
   - 记录token刷新操作
   - 监控异常刷新行为
   - 定期清理过期token

## 测试建议

1. **功能测试**
   - 测试正常登录和token获取
   - 测试token刷新功能
   - 测试过期token的处理

2. **安全测试**
   - 测试无效refreshToken的处理
   - 测试过期refreshToken的处理
   - 测试并发刷新请求

3. **性能测试**
   - 测试大量用户同时刷新的性能
   - 测试数据库连接池配置

现在您的项目已经具备了完整的RefreshToken功能！
