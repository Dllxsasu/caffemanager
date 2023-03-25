package com.jeremias.dev.mappers;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.jeremias.dev.dtos.UserDto;
import com.jeremias.dev.models.User;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
@RequiredArgsConstructor
@Component
@Log4j2
public class UserMapper {
	
	private final ModelMapper mapper;
	
	public UserDto convert(User user, String token, long untilNextLogin) {
		UserDto dto = new UserDto(user.getId(), user.getUsername(), "XXX", "YYY", user.getRoles(), token, "ZZZ", "AAA",
				LocalDate.EPOCH, untilNextLogin);
		return dto;
	}
	public User convert(UserDto dto, Optional<User> entityOpt) {
		final User myEntity = entityOpt.orElse(new User());
		myEntity.setBirthDate(dto.getBirthDate());
		myEntity.setEmail(dto.getEmail());
		log.info("Password: "+dto.getPassword());
		myEntity.setPassword(dto.getPassword());
		myEntity.setUsername(dto.getUsername());
		myEntity.setRoles(dto.getRoles());
		myEntity.setUuid(dto.getUuid());
		return myEntity;
	}
	 public UserDto convert(User obj) {
		 	UserDto dto = mapper.map(obj, UserDto.class);
	        return dto;
	    }
}
