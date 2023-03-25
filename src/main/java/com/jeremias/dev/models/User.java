package com.jeremias.dev.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
@Data

@Entity
@Table(name="users")
public class User  extends EntityBaseAudit{
	@NotBlank
	@Size(max=255)
	@Column(unique=true)
	private String username;
	@NotBlank
	@Size(max=255)
	private String password;
//	private String moviedbkey;
	@NotBlank
	@Size(max=255)
	private String roles;
	@Column(unique=true)
	private String email;
	private boolean locked;
	private boolean enabled;
	private String uuid;
	private LocalDate birthDate;
	
}
