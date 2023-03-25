package com.jeremias.dev.dtos;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
	private Long id;
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	private String moviedbkey;
	private String roles;
	private String token;
	@NotBlank
	private String email;
	private String uuid;
	private LocalDate birthDate;
	private Long secUntilNexLogin;
}
