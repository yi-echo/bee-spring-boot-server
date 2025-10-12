package com.bezkoder.springjwt.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private ERole name;
  
  @Column(name = "code")
  private String code;
  
  @Column(name = "description")
  private String description;
  
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "role_permissions", 
             joinColumns = @JoinColumn(name = "role_id"), 
             inverseJoinColumns = @JoinColumn(name = "permission_id"))
  private Set<Permission> permissions;
  
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "role_menus", 
             joinColumns = @JoinColumn(name = "role_id"), 
             inverseJoinColumns = @JoinColumn(name = "menu_id"))
  private Set<Menu> menus;

  public Role() {

  }

  public Role(ERole name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public ERole getName() {
    return name;
  }

  public void setName(ERole name) {
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
  
  public Set<Permission> getPermissions() {
    return permissions;
  }
  
  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
  }
  
  public Set<Menu> getMenus() {
    return menus;
  }
  
  public void setMenus(Set<Menu> menus) {
    this.menus = menus;
  }
}