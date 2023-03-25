package com.jeremias.dev.mappers;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.jeremias.dev.dtos.ProductoDto;
import com.jeremias.dev.models.Producto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductoMapper {
	
	private final ModelMapper mapper;
	
	public ProductoDto convertProductoDto(Producto obj) {		
		return mapper.map(obj, ProductoDto.class);
	}
	public Producto convertProducto(ProductoDto obj) {		
		return mapper.map(obj, Producto.class);
	}
}
