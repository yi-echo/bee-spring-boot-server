package com.bezkoder.springjwt.payload.response;

public class RoleInfo {
    
    private String id;
    private String name;
    private String code;
    private String description;
    
    public RoleInfo() {}
    
    public RoleInfo(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
