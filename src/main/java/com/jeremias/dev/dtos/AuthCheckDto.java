package com.jeremias.dev.dtos;

import lombok.Data;

@Data
public class AuthCheckDto {
	private final String path;
	private final boolean authorized;
}
