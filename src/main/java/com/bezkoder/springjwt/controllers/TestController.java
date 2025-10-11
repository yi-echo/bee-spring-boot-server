package com.bezkoder.springjwt.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.payload.response.ApiResponse;
import com.bezkoder.springjwt.util.ResponseUtil;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
  @GetMapping("/all")
  public ResponseEntity<ApiResponse<String>> allAccess() {
    return ResponseUtil.success("公开内容", "Public Content.");
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<String>> userAccess() {
    return ResponseUtil.success("用户内容", "User Content.");
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<ApiResponse<String>> moderatorAccess() {
    return ResponseUtil.success("管理员内容", "Moderator Board.");
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<String>> adminAccess() {
    return ResponseUtil.success("超级管理员内容", "Admin Board.");
  }
}
