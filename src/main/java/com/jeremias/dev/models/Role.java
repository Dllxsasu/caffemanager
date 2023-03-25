package com.jeremias.dev.models;

import org.springframework.security.core.GrantedAuthority;

public enum  Role implements GrantedAuthority{
	USERS, GUEST, ADMI;

	@Override
	public String getAuthority() {		
		return this.name();
	}
}
