package com.bezkoder.springjwt.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;

public class SignupRequest {
  @NotBlank(message = "username cannot be blank")
  @Size(min = 3, max = 20, message = "username length must be between 3 and 20")
  private String username;

  @NotBlank(message = "email cannot be blank")
  @Size(max = 50, message = "email length cannot be more than 50")
  @Email(message = "email format is incorrect")
  private String email;

  private Set<String> role;

  @NotBlank(message = "password cannot be blank") 
  @Size(min = 6, max = 40, message = "password length must be between 6 and 40")
  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<String> getRole() {
    return this.role;
  }

  public void setRole(Set<String> role) {
    this.role = role;
  }
}
