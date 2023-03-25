package com.jeremias.dev.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeremias.dev.dtos.ProductoDto;
import com.jeremias.dev.services.ProductoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/producto")
public class ProductoController {
	private final ProductoService servicio;
	@PostMapping
	public ResponseEntity<Object> save(@Valid @RequestBody ProductoDto dto){
		return new ResponseEntity<>( servicio.save(dto),HttpStatus.OK);	
	}
	@PutMapping
	public ResponseEntity<Object> update(@Valid @RequestBody ProductoDto dto){
		return new ResponseEntity<>( servicio.update(dto),HttpStatus.OK);	
	}
	@PutMapping("/status")
	public ResponseEntity<Object> updateStatus( @RequestBody ProductoDto dto){
		return new ResponseEntity<>( servicio.updateStatus(dto),HttpStatus.OK);	
	}
	@GetMapping
	public ResponseEntity<Object > getALL(){
		return new ResponseEntity<>( servicio.findAll(),HttpStatus.OK);	
	}
	@GetMapping("/bycategoria/{id}")
	public ResponseEntity<Object > getALL(@PathVariable long id){
		return new ResponseEntity<>( servicio.findAllByCategoria(id),HttpStatus.OK);	
	}
	//addd filter value method with hibernate search 
	@GetMapping("/{id}")
	public ResponseEntity<Object > findId(@PathVariable long id){
		return new ResponseEntity<>( servicio.findById(id),HttpStatus.OK);	
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Object > deleteId(@PathVariable long id){
		servicio.delete(id);
		return new ResponseEntity<>( HttpStatus.OK);	
	}
}
