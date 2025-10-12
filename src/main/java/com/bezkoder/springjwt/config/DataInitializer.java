package com.bezkoder.springjwt.config;

import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.MenuRepository;
import com.bezkoder.springjwt.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 数据初始化器
 * 数据初始化通过 data.sql 脚本完成，这个类用于验证初始化结果
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private MenuRepository menuRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // 验证数据初始化结果
        long userCount = userRepository.count();
        long roleCount = roleRepository.count();
        long menuCount = menuRepository.count();
        long permissionCount = permissionRepository.count();
        
        System.out.println("=== database initialized ===");  
        System.out.println("user count: " + userCount);
        System.out.println("role count: " + roleCount);
        System.out.println("menu count: " + menuCount);
        System.out.println("permission count: " + permissionCount);
        
        if (userCount > 0 && roleCount > 0 && menuCount > 0 && permissionCount > 0) {
            System.out.println("✅ data initialization successful!");
        } else {
            System.out.println("❌ data initialization may have problems, please check the data.sql script");
        }
        System.out.println("========================");
    }
}
