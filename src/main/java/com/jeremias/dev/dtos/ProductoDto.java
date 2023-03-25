package com.jeremias.dev.dtos;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.jeremias.dev.models.Categoria;

import lombok.Data;
@Data
public class ProductoDto {
	 Long id;
	 String nombre;
	 
	 double precio;
	 String status;

	 
	 Long idCategoria;
	 
	 String descripcion;
}
