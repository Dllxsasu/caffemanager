package com.jeremias.dev.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeremias.dev.dtos.CategoriaDto;
import com.jeremias.dev.models.Categoria;
import com.jeremias.dev.services.CategoriaService;

@ExtendWith(MockitoExtension.class)
public class CategoriaControllerTest {
	@Mock
	CategoriaService categoriaServicio;
	@InjectMocks
	CategoriaController categoriaController;
	 private MockMvc mockMvc;
	 
	CategoriaDto categoriaDto;
	
	Categoria categoria;
	
	@BeforeEach
	void setup() {
		categoriaDto = new CategoriaDto();
		categoriaDto.setNombre("cate1");
		//categoriaDto.setId(1L);	
		
		categoria = new Categoria();
		categoria.setNombre("cate1");
		categoria.setId(1L);	
	    mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
	}
	
	@Test
	@DisplayName("text save categoriaController Controller")
	public void testSave() throws Exception{
		//given
		//when
		
		when(categoriaServicio.save(any())).thenReturn(categoriaDto);
		
		//them
		ResponseEntity<Object> result = categoriaController.save(categoriaDto);
		
		//acert
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(categoriaDto, result.getBody());
		
		
		  // Act
        mockMvc.perform(post("/rest/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(categoriaDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test Categoria"));
        
		//verify 
		verify(categoriaServicio, times(1)).save(any());
	}
	
	 public static String asJsonString(final Object obj) {
		    try {
		      return new ObjectMapper().writeValueAsString(obj);
		    } catch (Exception e) {
		      throw new RuntimeException(e);
		    }
		  }
	 
	@Test
	@DisplayName("text for find all categoriaController")
	public void testFindAll() throws Exception{
		//given
		List<CategoriaDto> lista= new ArrayList<>();
		lista.add( new CategoriaDto( 1l, "cate 1"));
		lista.add( new CategoriaDto(1l,"cate 2"));
		lista.add( new CategoriaDto(1l,"cate 3"));
		//when
		when(categoriaServicio.findAll()).thenReturn(lista);
		
	
		
		//them
		ResponseEntity<Object> result = categoriaController.getALL();
		 // Act
        mockMvc.perform(get("/rest/categoria"))
                .andExpect(status().isOk())
            //    .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Test Categoria 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nombre").value("Test Categoria 2"));
		
		//acert
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(lista, result.getBody());
		//verify 
		verify(categoriaServicio, times(1)).findAll();
	}
	@Test 
	@DisplayName("text for find all categoriaController")
	public void testDelete() {
		
	
		
		//them
		ResponseEntity<Object> result = categoriaController.deleteId(1L);
		
		//acert
		assertEquals(HttpStatus.OK, result.getStatusCode());
	
		//verify 
		verify(categoriaServicio, times(1)).delete(any());
	}
	
	@Test
	public void testUpdate() {
	    // Arrange
	    CategoriaDto categoriaDto = new CategoriaDto(1L, "Categoria Actualizada");
	    when(categoriaServicio.update(categoriaDto)).thenReturn(categoriaDto);

	    // Act
	    ResponseEntity<Object> response = categoriaController.update(categoriaDto);

	    // Assert
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(categoriaDto, response.getBody());
	}

	@Test
	public void testFindById() throws Exception{
	    // Arrange
	    CategoriaDto categoriaDto = new CategoriaDto(1L, "Categoria");
	    when(categoriaServicio.findById(1L)).thenReturn(categoriaDto);

	    // Act
	    ResponseEntity<Object> response = categoriaController.findId(1L);
	    // Act
        mockMvc.perform(get("/rest/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test Categoria"));
        
	    // Assert
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(categoriaDto, response.getBody());
	}

	
}
