package com.bezkoder.springjwt.payload.response;

import java.util.List;

public class UserInfo {
    
    private String id;
    private String username;
    private String avatar;
    private String email;
    private List<MenuInfo> menu;
    private List<PermissionInfo> permissions;
    private List<RoleInfo> roles;
    
    public UserInfo() {}
    
    public UserInfo(String id, String username, String avatar, String email) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.email = email;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public List<MenuInfo> getMenu() {
        return menu;
    }
    
    public void setMenu(List<MenuInfo> menu) {
        this.menu = menu;
    }
    
    public List<PermissionInfo> getPermissions() {
        return permissions;
    }
    
    public void setPermissions(List<PermissionInfo> permissions) {
        this.permissions = permissions;
    }
    
    public List<RoleInfo> getRoles() {
        return roles;
    }
    
    public void setRoles(List<RoleInfo> roles) {
        this.roles = roles;
    }
}
