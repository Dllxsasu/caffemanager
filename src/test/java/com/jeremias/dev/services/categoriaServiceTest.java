package com.jeremias.dev.services;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jeremias.dev.dtos.CategoriaDto;
import com.jeremias.dev.exceptions.ResourceNotFoundException;
import com.jeremias.dev.mappers.DefaultMapper;
import com.jeremias.dev.models.Categoria;
import com.jeremias.dev.repositorios.CategoriaRepository;

import lombok.var;

@ExtendWith(MockitoExtension.class)
public class categoriaServiceTest {

	@Mock
	private DefaultMapper defaultMapper;

	@Mock
	private CategoriaRepository categoriaRepository;

    @InjectMocks
	private CategoriaService categoriaService;

	private Categoria categoria;

	private CategoriaDto categoriaDto;

	@BeforeEach
	void setUp() {
		categoria = new Categoria();
		categoria.setId(1L);
		categoria.setNombre("Category 1");

		categoriaDto = new CategoriaDto();
		categoriaDto.setId(1L);
		categoriaDto.setNombre("Category 1");
	}

	@Test
    @DisplayName("Test save categoria")
	public void testSaveCategoria() {
		// Given
		
		//when 
		when(defaultMapper.convertCategoriaDto( any(Categoria.class))).thenReturn(categoriaDto);
        when(defaultMapper.convertCategoria(any(CategoriaDto.class))).thenReturn(categoria);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        
        CategoriaDto result = categoriaService.save(categoriaDto);
        
        assertEquals(categoriaDto, result);
        verify(defaultMapper, times(1)).convertCategoriaDto(any());
        verify(defaultMapper, times(1)).convertCategoria(any());
        verify(categoriaRepository, times(1)).save(any());
	}
	@Test
	@DisplayName("test find id de Categoria service")
	public void testFindId() {
		when(categoriaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(categoria));
	    
		when(defaultMapper.convertCategoriaDto(any(Categoria.class))).thenReturn(categoriaDto);
	    
	    CategoriaDto result = categoriaService.findById(1L);
	    assertEquals(categoriaDto, result);
	    
	    verify(defaultMapper, times(1)).convertCategoriaDto(any());
	    verify(categoriaRepository, times(1)).findById(anyLong());
	    
	}
		
	@Test
	@DisplayName("test for fin all caategoria service")
	public void TestFindAllCategorias() {
		//given
		List<Categoria> categorias = new ArrayList<Categoria>();
		categorias.add(categoria);
		//when
		when(categoriaRepository.findAll()).thenReturn(categorias);
		when(defaultMapper.convertCategoriaDto(any(Categoria.class))).thenReturn(categoriaDto);
		//Ouput
		var result = categoriaService.findAll();
		var expected = categorias.stream().map(item -> defaultMapper.convertCategoriaDto(item)).toList();
		assertEquals(expected, result);
		assertEquals(1, result.size());
		verify(categoriaRepository, times(1)).findAll();
		verify(defaultMapper, times(2)).convertCategoriaDto(any());
	}
	
	@Test
	@DisplayName("test getCategoria from service")
	public void testGetCategoria() {
		//fiven
		//when
		when(categoriaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(categoria));
		//when(defaultMapper.convertCategoriaDto(any(Categoria.class))).thenReturn(categoriaDto);
		
		Categoria result = categoriaService.getCategoria(1L);
		assertEquals(categoria, result);
		verify(categoriaRepository, times(1)).findById(anyLong());
	}
	
	@Test
	@DisplayName("test delete categoria")
	public void testDeleteCategoria() {
		//when
		when(categoriaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(categoria));
		
		assertDoesNotThrow(() -> categoriaService.delete(1L));
		verify(categoriaRepository, times(1)).findById(anyLong());
		verify(categoriaRepository, times(1)).deleteById(anyLong());
		
	}
	
	
	@Test
	@DisplayName("test for update")
	public void testUpdateCategoria() {
		
		//When
		when(categoriaRepository.findById(anyLong())).thenReturn(Optional.ofNullable(categoria));
		when(defaultMapper.convertCategoriaDto(any())).thenReturn(categoriaDto);
		
		//assertThrows( ResourceNotFoundException.class, () -> categoriaService.update(categoriaDto) );
		var result = categoriaService.update(categoriaDto);
		
		assertEquals(categoriaDto, result);
		verify(categoriaRepository, times(1)).findById(anyLong());
		verify(categoriaRepository, times(1)).save(any());
		verify(defaultMapper, times(1)).convertCategoriaDto(any());
	}
	
	@Test
	@DisplayName("test for update with invalid  id")
	public void testUpdateWithIdInvalid() {
		//When vero
		when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());
		//then
		assertThrows(ResourceNotFoundException.class, () -> categoriaService.update(categoriaDto) );
		//recuerda anothar en cuaderno las definiciones
		verify( categoriaRepository,times(1)  ).findById(anyLong());
		verify(categoriaRepository, times(0)).save(any());
		verify(defaultMapper, times(0)).convertCategoriaDto(any()); 
	}
	
	@Test 
	@DisplayName("test for invalid id find user")
	public void testInvalidId() {
		
		when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> categoriaService.findById(3l) );
	
		verify(categoriaRepository, times(1)).findById(anyLong());
		//veify()
	}
}
