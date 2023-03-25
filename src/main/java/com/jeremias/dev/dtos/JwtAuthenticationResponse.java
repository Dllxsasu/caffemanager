package com.jeremias.dev.dtos;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
	private String accessToken;
	private String tokenType = "Bearer";
	private boolean status = false;
	public JwtAuthenticationResponse(String accessToken) {
		this.accessToken = accessToken;
	}
}
