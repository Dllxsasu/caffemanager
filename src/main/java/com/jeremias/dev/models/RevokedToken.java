package com.jeremias.dev.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class RevokedToken extends EntityBase{
	@NotBlank
	@Size(max=255)
	private String name;
	@NotBlank
	@Size(max=255)
	private String uuid;
	@NotNull	
	private LocalDateTime lastLogout;
}
