package com.bezkoder.springjwt.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "menus")
public class Menu {
    
    @Id
    @Column(name = "id")
    private String id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "code", nullable = false)
    private String code;
    
    @Column(name = "parent_id")
    private String parentId;
    
    @Column(name = "path")
    private String path;
    
    @Column(name = "icon")
    private String icon;
    
    @Column(name = "sort_order")
    private Integer sortOrder;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "type")
    private Integer type;
    
    @Column(name = "component")
    private String component;
    
    @Column(name = "caption")
    private String caption;
    
    @Column(name = "info")
    private String info;
    
    @Column(name = "auth")
    private String auth;
    
    @Column(name = "hidden")
    private Boolean hidden = false;
    
    @Column(name = "disabled")
    private Boolean disabled = false;
    
    @Column(name = "external_link")
    private String externalLink;
    
    @ManyToMany(mappedBy = "menus")
    private Set<Role> roles;
    
    public Menu() {}
    
    public Menu(String id, String name, String code, String parentId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.parentId = parentId;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    
    public String getComponent() {
        return component;
    }
    
    public void setComponent(String component) {
        this.component = component;
    }
    
    public String getCaption() {
        return caption;
    }
    
    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public String getInfo() {
        return info;
    }
    
    public void setInfo(String info) {
        this.info = info;
    }
    
    public String getAuth() {
        return auth;
    }
    
    public void setAuth(String auth) {
        this.auth = auth;
    }
    
    public Boolean getHidden() {
        return hidden;
    }
    
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }
    
    public Boolean getDisabled() {
        return disabled;
    }
    
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
    
    public String getExternalLink() {
        return externalLink;
    }
    
    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }
}
