package com.jeremias.dev.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeremias.dev.config.security.AuthServiceImpl;
import com.jeremias.dev.dtos.UserDto;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@RestController
@RequestMapping("rest/user")
public class UserController {
	
	private final AuthServiceImpl authService;
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping()
	public List<UserDto> getAllUser(){
		return authService.getAllUser();
	}
}
