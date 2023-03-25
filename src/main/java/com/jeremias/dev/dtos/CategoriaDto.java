package com.jeremias.dev.dtos;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaDto {
	Long id;
	@NotBlank
	String nombre;
	public CategoriaDto(){
		
	}
	public CategoriaDto(String nombre){
		this.nombre = nombre;
	}
}
