package com.jeremias.dev.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.jeremias.dev.dtos.CategoriaDto;
import com.jeremias.dev.models.Categoria;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class DefaultMapper {
	private final ModelMapper mapper;
	
	public Categoria convertCategoria(CategoriaDto dto) {
		Categoria obj = mapper.map(dto, Categoria.class);
        return obj;
	}

	public CategoriaDto convertCategoriaDto(Categoria obj) {
		CategoriaDto dto = mapper.map(obj, CategoriaDto.class);
        return dto;
	}
	
	
}
