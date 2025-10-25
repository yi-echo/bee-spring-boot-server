package com.bezkoder.springjwt.controllers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.request.TokenRefreshRequest;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.ApiResponse;
import com.bezkoder.springjwt.util.ResponseUtil;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import com.bezkoder.springjwt.security.services.RefreshTokenService;
import com.bezkoder.springjwt.security.services.UserInfoService;
import com.bezkoder.springjwt.security.services.JwtBlacklistService;
import com.bezkoder.springjwt.models.RefreshToken;
import com.bezkoder.springjwt.payload.response.UserInfo;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @Autowired
  UserInfoService userInfoService;

  @Autowired
  JwtBlacklistService jwtBlacklistService;

  @PostMapping("/signin")
  public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
    
    // 获取完整的用户信息
    UserInfo userInfo = userInfoService.getUserInfo(userDetails.getId());
    
    JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken.getToken(), userInfo);
    
    return ResponseUtil.success("login successfully", jwtResponse);
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<Object>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseUtil.badRequest("username already exists");
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseUtil.badRequest("email already exists");
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), 
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseUtil.success("user registered successfully");
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<ApiResponse<JwtResponse>> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
          RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());
          
          // 获取完整的用户信息
          UserInfo userInfo = userInfoService.getUserInfo(user.getId());
          
          JwtResponse jwtResponse = new JwtResponse(token, newRefreshToken.getToken(), userInfo);
          
          return ResponseUtil.success("Token refreshed successfully", jwtResponse);
        })
        .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<ApiResponse<Object>> logoutUser(@RequestBody(required = false) TokenRefreshRequest request) {
    try {
      // 如果提供了refresh token，则删除特定的refresh token
      if (request != null && request.getRefreshToken() != null) {
        String refreshToken = request.getRefreshToken();
        
        // 验证并删除refresh token
        Optional<RefreshToken> token = refreshTokenService.findByToken(refreshToken);
        if (token.isPresent()) {
          refreshTokenService.deleteByToken(refreshToken);
          return ResponseUtil.success("User signed out successfully");
        } else {
          return ResponseUtil.badRequest("Invalid refresh token");
        }
      } else {
        // 如果没有提供refresh token，则删除当前用户的所有refresh token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
          UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
          
          // 删除用户的所有refresh token
          refreshTokenService.deleteByUserId(userDetails.getId());
          
          // 清除安全上下文
          SecurityContextHolder.clearContext();
          
          return ResponseUtil.success("User signed out successfully");
        } else {
          return ResponseUtil.badRequest("No authenticated user found");
        }
      }
    } catch (Exception e) {
      return ResponseUtil.error("Error during logout: " + e.getMessage());
    }
  }

  /**
   * 增强版登出接口，支持将JWT token加入黑名单
   * 需要提供Authorization header中的JWT token
   */
  @PostMapping("/signout/secure")
  public ResponseEntity<ApiResponse<Object>> secureLogoutUser(
      @RequestBody(required = false) TokenRefreshRequest request,
      HttpServletRequest httpRequest) {
    try {
      // 获取当前请求的JWT token
      String jwtToken = getJwtFromRequest(httpRequest);
      
      // 如果提供了refresh token，则删除特定的refresh token
      if (request != null && request.getRefreshToken() != null) {
        String refreshToken = request.getRefreshToken();
        
        // 验证并删除refresh token
        Optional<RefreshToken> token = refreshTokenService.findByToken(refreshToken);
        if (token.isPresent()) {
          refreshTokenService.deleteByToken(refreshToken);
          
          // 将JWT token加入黑名单
          if (jwtToken != null) {
            jwtBlacklistService.blacklistToken(jwtToken);
          }
          
          return ResponseUtil.success("User signed out successfully");
        } else {
          return ResponseUtil.badRequest("Invalid refresh token");
        }
      } else {
        // 如果没有提供refresh token，则删除当前用户的所有refresh token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
          UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
          
          // 删除用户的所有refresh token
          refreshTokenService.deleteByUserId(userDetails.getId());
          
          // 将JWT token加入黑名单
          if (jwtToken != null) {
            jwtBlacklistService.blacklistToken(jwtToken);
          }
          
          // 清除安全上下文
          SecurityContextHolder.clearContext();
          
          return ResponseUtil.success("User signed out successfully");
        } else {
          return ResponseUtil.badRequest("No authenticated user found");
        }
      }
    } catch (Exception e) {
      return ResponseUtil.error("Error during logout: " + e.getMessage());
    }
  }

  /**
   * 从请求中提取JWT token
   */
  private String getJwtFromRequest(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    
    if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }
    
    return null;
  }
}
