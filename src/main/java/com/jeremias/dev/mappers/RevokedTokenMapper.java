package com.jeremias.dev.mappers;

import org.springframework.stereotype.Component;

import com.jeremias.dev.dtos.RevokedTokenDto;
import com.jeremias.dev.models.RevokedToken;

@Component
public class RevokedTokenMapper {
	public RevokedToken convert(RevokedTokenDto dto) {
		return new RevokedToken(dto.getName(), dto.getUuid(), dto.getLastLogout());
	}
	
	public RevokedToken convert(RevokedTokenDto dto, Long id) {		
		RevokedToken entity = new RevokedToken(dto.getName(), dto.getUuid(), dto.getLastLogout());
		entity.setId(id != null ? id : null);
		return entity;
	}
	
	public RevokedTokenDto convert(RevokedToken entity) {
		return new RevokedTokenDto(entity.getName(), entity.getUuid(), entity.getLastLogout());
	}
}
