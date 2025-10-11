package com.bezkoder.springjwt.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
	@NotBlank(message = "username cannot be blank")
	@Size(min = 3, max = 20, message = "username length must be between 3 and 20")
  private String username;

	@NotBlank(message = "password cannot be blank")
	@Size(min = 6, max = 40, message = "password length must be between 6 and 40")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
