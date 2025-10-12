package com.bezkoder.springjwt.security.services;

import com.bezkoder.springjwt.models.Menu;
import com.bezkoder.springjwt.models.Permission;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.response.MenuInfo;
import com.bezkoder.springjwt.payload.response.PermissionInfo;
import com.bezkoder.springjwt.payload.response.RoleInfo;
import com.bezkoder.springjwt.payload.response.UserInfo;
import com.bezkoder.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserInfoService {
    
    @Autowired
    private UserRepository userRepository;
    
    public UserInfo getUserInfo(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId().toString());
            userInfo.setUsername(user.getUsername());
            userInfo.setAvatar(user.getAvatar());
            userInfo.setEmail(user.getEmail());
            
            // 获取用户的所有角色
            Set<Role> userRoles = user.getRoles();
            
            // 获取所有菜单
            Set<Menu> allMenus = new HashSet<>();
            Set<Permission> allPermissions = new HashSet<>();
            
            for (Role role : userRoles) {
                if (role.getMenus() != null) {
                    allMenus.addAll(role.getMenus());
                }
                if (role.getPermissions() != null) {
                    allPermissions.addAll(role.getPermissions());
                }
            }
            
            // 转换为响应对象并构建树形结构
            List<MenuInfo> menuInfos = buildMenuTree(allMenus);
            
            List<PermissionInfo> permissionInfos = allPermissions.stream()
                    .map(this::convertToPermissionInfo)
                    .collect(Collectors.toList());
            
            List<RoleInfo> roleInfos = userRoles.stream()
                    .map(this::convertToRoleInfo)
                    .collect(Collectors.toList());
            
            userInfo.setMenu(menuInfos);
            userInfo.setPermissions(permissionInfos);
            userInfo.setRoles(roleInfos);
            
            return userInfo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting user info: " + e.getMessage(), e);
        }
    }
    
    private List<MenuInfo> buildMenuTree(Set<Menu> menus) {
        // 转换为MenuInfo列表
        List<MenuInfo> menuInfos = menus.stream()
                .map(this::convertToMenuInfo)
                .collect(Collectors.toList());
        
        // 构建树形结构
        Map<String, MenuInfo> menuMap = menuInfos.stream()
                .collect(Collectors.toMap(MenuInfo::getId, menu -> menu));
        
        List<MenuInfo> rootMenus = new ArrayList<>();
        
        for (MenuInfo menu : menuInfos) {
            if (menu.getParentId() == null || menu.getParentId().isEmpty()) {
                // 根菜单
                rootMenus.add(menu);
            } else {
                // 子菜单
                MenuInfo parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        }
        
        // 排序
        sortMenus(rootMenus);
        
        return rootMenus;
    }
    
    private void sortMenus(List<MenuInfo> menus) {
        if (menus == null) return;
        
        menus.sort((a, b) -> {
            Integer sortA = a.getSortOrder() != null ? a.getSortOrder() : 0;
            Integer sortB = b.getSortOrder() != null ? b.getSortOrder() : 0;
            return sortA.compareTo(sortB);
        });
        
        for (MenuInfo menu : menus) {
            sortMenus(menu.getChildren());
        }
    }
    
    private MenuInfo convertToMenuInfo(Menu menu) {
        MenuInfo menuInfo = new MenuInfo();
        menuInfo.setId(menu.getId());
        menuInfo.setName(menu.getName());
        menuInfo.setCode(menu.getCode());
        menuInfo.setParentId(menu.getParentId());
        menuInfo.setPath(menu.getPath());
        menuInfo.setIcon(menu.getIcon());
        menuInfo.setSortOrder(menu.getSortOrder());
        menuInfo.setType(menu.getType());
        menuInfo.setComponent(menu.getComponent());
        menuInfo.setCaption(menu.getCaption());
        menuInfo.setInfo(menu.getInfo());
        menuInfo.setAuth(menu.getAuth());
        menuInfo.setHidden(menu.getHidden());
        menuInfo.setDisabled(menu.getDisabled());
        menuInfo.setExternalLink(menu.getExternalLink());
        menuInfo.setChildren(new ArrayList<>());
        return menuInfo;
    }
    
    private PermissionInfo convertToPermissionInfo(Permission permission) {
        PermissionInfo permissionInfo = new PermissionInfo();
        permissionInfo.setId(permission.getId());
        permissionInfo.setName(permission.getName());
        permissionInfo.setCode(permission.getCode());
        permissionInfo.setDescription(permission.getDescription());
        permissionInfo.setResource(permission.getResource());
        permissionInfo.setAction(permission.getAction());
        return permissionInfo;
    }
    
    private RoleInfo convertToRoleInfo(Role role) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setId(role.getId().toString());
        roleInfo.setName(role.getName().name());
        roleInfo.setCode(role.getCode());
        roleInfo.setDescription(role.getDescription());
        return roleInfo;
    }
}
