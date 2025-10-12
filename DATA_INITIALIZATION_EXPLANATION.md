# 数据初始化自动执行说明

## 概述

是的，`data.sql`脚本会自动执行！Spring Boot提供了多种数据初始化机制，我们使用的是SQL脚本自动执行方式。

## 自动执行机制

### 1. Spring Boot SQL初始化

Spring Boot会在应用启动时自动执行以下位置的SQL脚本：
- `src/main/resources/data.sql` - 数据初始化脚本
- `src/main/resources/schema.sql` - 表结构脚本（可选）

### 2. 配置说明

在`application.properties`中的关键配置：

```properties
# 数据初始化配置
spring.sql.init.mode=always          # 总是执行初始化脚本
spring.sql.init.continue-on-error=true  # 遇到错误时继续执行
spring.sql.init.encoding=UTF-8       # 脚本编码
```

### 3. 执行时机

SQL脚本的执行时机：
1. **应用启动时** - Spring Boot启动过程中
2. **数据库连接建立后** - 在DataSource初始化之后
3. **JPA实体创建前** - 在Hibernate创建表结构之前

## 执行流程

```
应用启动
    ↓
DataSource初始化
    ↓
执行 data.sql 脚本
    ↓
JPA/Hibernate初始化
    ↓
DataInitializer.run() 执行（验证数据）
    ↓
应用启动完成
```

## 验证机制

### 1. DataInitializer类

`DataInitializer`类实现了`CommandLineRunner`接口，会在应用启动完成后执行：

```java
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        // 验证数据初始化结果
        long userCount = userRepository.count();
        long roleCount = roleRepository.count();
        long menuCount = menuRepository.count();
        long permissionCount = permissionRepository.count();
        
        System.out.println("=== 数据初始化验证 ===");
        System.out.println("用户数量: " + userCount);
        System.out.println("角色数量: " + roleCount);
        System.out.println("菜单数量: " + menuCount);
        System.out.println("权限数量: " + permissionCount);
        
        if (userCount > 0 && roleCount > 0 && menuCount > 0 && permissionCount > 0) {
            System.out.println("✅ 数据初始化成功！");
        } else {
            System.out.println("❌ 数据初始化可能存在问题，请检查 data.sql 脚本");
        }
    }
}
```

### 2. 启动日志

应用启动时，您会看到类似以下的日志输出：

```
=== 数据初始化验证 ===
用户数量: 3
角色数量: 2
菜单数量: 50
权限数量: 4
✅ 数据初始化成功！
========================
```

## 配置选项详解

### spring.sql.init.mode
- `always` - 总是执行初始化脚本
- `embedded` - 仅在嵌入式数据库时执行
- `never` - 从不执行初始化脚本

### spring.sql.init.continue-on-error
- `true` - 遇到错误时继续执行后续语句
- `false` - 遇到错误时停止执行

### spring.sql.init.encoding
- 指定SQL脚本的字符编码，通常设置为UTF-8

## 其他初始化方式

### 1. 程序化初始化
```java
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        // 使用Java代码初始化数据
        if (userRepository.count() == 0) {
            // 创建用户
            User admin = new User("admin", "admin@example.com", encoder.encode("123456"));
            userRepository.save(admin);
        }
    }
}
```

### 2. @PostConstruct初始化
```java
@Component
public class DataInitializer {
    
    @PostConstruct
    public void init() {
        // 在Bean初始化后执行
    }
}
```

### 3. ApplicationReadyEvent监听
```java
@Component
public class DataInitializer {
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        // 应用完全启动后执行
    }
}
```

## 最佳实践

### 1. 使用SQL脚本的优势
- **性能更好** - 批量插入比逐条插入快
- **事务性** - 整个脚本在一个事务中执行
- **可维护性** - SQL脚本易于版本控制和维护
- **数据库无关** - 可以轻松适配不同数据库

### 2. 注意事项
- 确保SQL脚本语法正确
- 注意外键约束和依赖关系
- 使用`continue-on-error=true`避免因重复数据导致启动失败
- 定期检查初始化日志

### 3. 开发环境vs生产环境
```properties
# 开发环境
spring.sql.init.mode=always

# 生产环境
spring.sql.init.mode=never
```

## 故障排查

### 1. 脚本未执行
- 检查`spring.sql.init.mode`配置
- 确认`data.sql`文件位置正确
- 查看启动日志中的错误信息

### 2. 数据重复插入
- 使用`INSERT IGNORE`或`ON DUPLICATE KEY UPDATE`
- 在脚本开头添加`DELETE`语句清空数据
- 设置`continue-on-error=true`

### 3. 外键约束错误
- 确保插入顺序正确（先插入被引用表）
- 检查外键约束设置
- 使用`SET FOREIGN_KEY_CHECKS=0`临时禁用检查

## 总结

`data.sql`脚本确实会自动执行，这是Spring Boot提供的一个非常方便的数据初始化机制。通过合理的配置和验证机制，可以确保数据正确初始化，为应用提供完整的测试数据。
