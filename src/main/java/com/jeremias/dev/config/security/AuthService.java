package com.jeremias.dev.config.security;

import java.util.Map;

import com.jeremias.dev.dtos.RefreshTokenDto;
import com.jeremias.dev.dtos.UserDto;
import com.jeremias.dev.models.User;
import com.jeremias.dev.utils.TokenSubjectRole;

public interface AuthService {
	User getCurrentUser(String bearerStr);
	TokenSubjectRole getTokenRoles(Map<String, String> headers);
	Boolean signin(UserDto appUserDto);
	Boolean confirmUuid(String uuid);
	UserDto login(UserDto appUserDto);
	Boolean logout(String bearerStr);
	RefreshTokenDto refreshToken(String bearerToken);
	UserDto load(Long id);
	UserDto save(UserDto appUser);
	void updateLoggedOutUsers();
	//void sendKafkaEvent(KafkaEventDto kafkaEventDto);
}
