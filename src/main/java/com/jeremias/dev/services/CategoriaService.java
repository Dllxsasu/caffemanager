package com.jeremias.dev.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jeremias.dev.dtos.CategoriaDto;
import com.jeremias.dev.exceptions.ResourceNotFoundException;
import com.jeremias.dev.mappers.DefaultMapper;
import com.jeremias.dev.models.Categoria;
import com.jeremias.dev.repositorios.CategoriaRepository;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class CategoriaService {
	
	private final CategoriaRepository categoriaRepository;
	private final DefaultMapper defaultMapper;
	
	public CategoriaDto save(CategoriaDto dto) {
		
		dto = this.defaultMapper.convertCategoriaDto(Optional.ofNullable(
						this.categoriaRepository
						.save(this.defaultMapper.convertCategoria(dto)))						
				.orElseThrow(() -> new ResourceNotFoundException("User  not found")));
		return dto;
		//return new ResponseEntity<>(dto,OK);
		
	}
	
	public CategoriaDto findById(Long id) {
		 return this.defaultMapper.convertCategoriaDto(
						this.categoriaRepository
						.findById(id)						
						.orElseThrow(() -> new ResourceNotFoundException("User  not found")));
		
		//return new ResponseEntity<>(dto,OK);
		
	}
	public List<CategoriaDto> findAll(){
		return this.categoriaRepository.findAll().stream().map(cat -> defaultMapper.convertCategoriaDto(cat)  ).collect(Collectors.toList());
	}
	public Categoria getCategoria(long id) {
		return this.categoriaRepository
				.findById(id)						
				.orElseThrow(() -> new ResourceNotFoundException("User  not found"));
	}
	
	public void delete(Long id) {
		
		Categoria obj = getCategoria(id);
		
		categoriaRepository.deleteById(id);
		
		//return new ResponseEntity<>(dto,OK);
	}

	public CategoriaDto update(CategoriaDto dto) {
		Categoria obj = getCategoria(dto.getId());
		obj.setNombre(dto.getNombre());
	return 	this.defaultMapper.convertCategoriaDto(
			Optional.ofNullable(categoriaRepository.save(obj))
			.orElseThrow(() -> new ResourceNotFoundException("User  not found")));
	}
	
}
