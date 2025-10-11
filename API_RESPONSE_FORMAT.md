# API响应格式规范

## 概述

本项目已实现统一的API响应格式，所有接口都遵循相同的响应结构，便于前端统一处理。

## 响应格式

### 统一响应结构

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1703123456789,
  "traceId": "optional-trace-id"
}
```

### 字段说明

- `code`: 响应状态码（HTTP状态码）
- `message`: 响应消息
- `data`: 响应数据（可选）
- `timestamp`: 响应时间戳
- `traceId`: 请求追踪ID（可选）

## 使用示例

### 1. 成功响应

```java
// 简单成功响应
return ResponseUtil.success();

// 带数据的成功响应
return ResponseUtil.success(data);

// 带消息和数据的成功响应
return ResponseUtil.success("操作成功", data);
```

### 2. 错误响应

```java
// 参数错误
return ResponseUtil.badRequest("参数错误");

// 未授权
return ResponseUtil.unauthorized("未授权访问");

// 禁止访问
return ResponseUtil.forbidden("没有权限");

// 资源未找到
return ResponseUtil.notFound("资源不存在");

// 服务器错误
return ResponseUtil.internalServerError("服务器内部错误");
```

### 3. 分页响应

```java
// 分页数据响应
return ResponseUtil.successPage(content, page, size, totalElements);

// 带消息的分页响应
return ResponseUtil.successPage("查询成功", content, page, size, totalElements);
```

## 分页响应格式

```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "content": [],
    "page": 0,
    "size": 10,
    "totalPages": 5,
    "totalElements": 50,
    "first": true,
    "last": false,
    "numberOfElements": 10,
    "hasNext": true,
    "hasPrevious": false
  },
  "timestamp": 1703123456789
}
```

## 异常处理

项目已配置全局异常处理器，会自动捕获并格式化以下异常：

- `BusinessException`: 业务异常
- `MethodArgumentNotValidException`: 参数校验异常
- `AuthenticationException`: 认证异常
- `AccessDeniedException`: 访问拒绝异常
- `IllegalArgumentException`: 非法参数异常
- `RuntimeException`: 运行时异常
- `Exception`: 其他异常

## 业务异常使用

```java
// 抛出业务异常
throw new BusinessException("业务处理失败");

// 带错误码的业务异常
throw new BusinessException(400, "参数错误");
```

## 响应示例

### 登录成功
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "roles": ["ROLE_ADMIN"]
  },
  "timestamp": 1703123456789
}
```

### 注册失败
```json
{
  "code": 400,
  "message": "用户名已被使用",
  "timestamp": 1703123456789
}
```

### 参数校验失败
```json
{
  "code": 400,
  "message": "参数校验失败",
  "data": {
    "username": "用户名不能为空",
    "email": "邮箱格式不正确"
  },
  "timestamp": 1703123456789
}
```

## 注意事项

1. 所有控制器方法都应返回 `ResponseEntity<ApiResponse<T>>` 类型
2. 使用 `ResponseUtil` 工具类创建响应，避免直接构造 `ApiResponse`
3. 业务异常应使用 `BusinessException` 抛出
4. 分页查询使用 `PageResponse` 包装数据
5. 响应消息应使用中文，便于前端显示
