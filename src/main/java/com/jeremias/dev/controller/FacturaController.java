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
import org.springframework.web.bind.annotation.RestController;

import com.jeremias.dev.dtos.CategoriaDto;
import com.jeremias.dev.dtos.FacturaDto;
import com.jeremias.dev.services.FacturaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/factura")
public class FacturaController {
	
	private final FacturaService servicio;
	
	@PostMapping
	public ResponseEntity<Object> save(@Valid @RequestBody FacturaDto dto){
		return new ResponseEntity<>( servicio.save(dto),HttpStatus.OK);	
	}
	@PutMapping
	public ResponseEntity<Object> update(@Valid @RequestBody FacturaDto dto){
		return new ResponseEntity<>( servicio.update(dto),HttpStatus.OK);	
	}
	@GetMapping
	public ResponseEntity<Object > getALL(){
		return new ResponseEntity<>( servicio.findAll(),HttpStatus.OK);	
	}
	//addd filter value method with hibernate search 
	@GetMapping("/{id}")
	public ResponseEntity<Object > findId(@PathVariable long id){
		return new ResponseEntity<>( servicio.findBy(id),HttpStatus.OK);	
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Object > deleteId(@PathVariable long id){
		servicio.delete(id);
		return new ResponseEntity<>( HttpStatus.OK);	
	}
}
