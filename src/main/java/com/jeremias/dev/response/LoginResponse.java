package com.jeremias.dev.response;

import lombok.Data;

@Data
public class LoginResponse {
	private String accessToken;
	private String tokenType = "Bearer";
	private boolean status;
	public LoginResponse(String accessToken, boolean status) {
		this.accessToken = accessToken;
		this.status = status;
	}
}
