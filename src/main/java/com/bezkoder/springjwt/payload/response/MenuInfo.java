package com.bezkoder.springjwt.payload.response;

import java.util.List;

public class MenuInfo {
    
    private String id;
    private String name;
    private String code;
    private String parentId;
    private String path;
    private String icon;
    private Integer sortOrder;
    private Integer type;
    private String component;
    private String caption;
    private String info;
    private String auth;
    private Boolean hidden;
    private Boolean disabled;
    private String externalLink;
    private List<MenuInfo> children;
    
    public MenuInfo() {}
    
    public MenuInfo(String id, String name, String code, String parentId) {
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
    
    public List<MenuInfo> getChildren() {
        return children;
    }
    
    public void setChildren(List<MenuInfo> children) {
        this.children = children;
    }
}
