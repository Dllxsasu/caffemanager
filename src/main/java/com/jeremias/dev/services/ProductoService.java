package com.jeremias.dev.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jeremias.dev.dtos.ProductoDto;
import com.jeremias.dev.exceptions.ResourceNotFoundException;
import com.jeremias.dev.mappers.ProductoMapper;
import com.jeremias.dev.models.Categoria;
import com.jeremias.dev.models.Producto;
import com.jeremias.dev.repositorios.ProductoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductoService {
	private final ProductoRepository productoRepositorio;
	private final CategoriaService categoriaService;
	private final ProductoMapper mapper;
	
	public ProductoDto save(ProductoDto dto) {
		
		Producto obj = new Producto();
		
		Categoria cat = categoriaService.getCategoria(dto.getIdCategoria());
		
		obj.setCategoria(cat);
		obj.setDescripcion(dto.getDescripcion());
		obj.setNombre(dto.getNombre());
		obj.setPrecio(dto.getPrecio());
		obj.setStatus("ACTIVO");
		
		
		dto = this.mapper.convertProductoDto(Optional.ofNullable(
						this.productoRepositorio
						.save(obj))						
				.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado.")));
		return dto;
		//return new ResponseEntity<>(dto,OK);
		
	}
	
	public ProductoDto findById(Long id) {
		 return this.mapper.convertProductoDto(
						this.productoRepositorio
						.findById(id)						
						.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado.")));
		
		//return new ResponseEntity<>(dto,OK);
		
	}
	public List<ProductoDto> findAll(){
		return this.productoRepositorio.findAll().stream().map(cat -> mapper.convertProductoDto(cat)  ).collect(Collectors.toList());
	}
	public List<ProductoDto> findAllByCategoria(Long id){
		return this.productoRepositorio.findAllByCategoria(id).stream().map(mapper::convertProductoDto).collect(Collectors.toList());
	}
	private Producto getProducto(long id) {
		return this.productoRepositorio
				.findById(id)						
				.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado."));
	}
	
	public void delete(Long id) {
		
		ProductoDto dto = this.mapper.convertProductoDto(
				this.productoRepositorio
				.findById(id)						
				.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado.")));
		
		productoRepositorio.deleteById(id);
		
		//return new ResponseEntity<>(dto,OK);
	}
	public ProductoDto updateStatus(ProductoDto dto) {
		Producto obj = getProducto(dto.getId());
		
				
		obj.setStatus(dto.getStatus());
		
	return 	this.mapper.convertProductoDto(
			Optional.ofNullable(productoRepositorio.save(obj))
			.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado.")));
	}
	public ProductoDto update(ProductoDto dto) {
		Producto obj = getProducto(dto.getId());
		
		Categoria cat = categoriaService.getCategoria(dto.getIdCategoria());
		
		obj.setCategoria(cat);
		obj.setDescripcion(dto.getDescripcion());
		obj.setNombre(dto.getNombre());
		obj.setPrecio(dto.getPrecio());
		obj.setStatus("ACTIVO");
		
	return 	this.mapper.convertProductoDto(
			Optional.ofNullable(productoRepositorio.save(obj))
			.orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado.")));
	}
}
