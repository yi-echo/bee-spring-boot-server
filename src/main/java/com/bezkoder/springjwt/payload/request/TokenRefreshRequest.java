package com.bezkoder.springjwt.payload.request;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequest {
    
    @NotBlank(message = "refresh token cannot be blank")
    private String refreshToken;
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
