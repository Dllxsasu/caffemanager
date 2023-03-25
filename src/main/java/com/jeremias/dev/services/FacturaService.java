package com.jeremias.dev.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jeremias.dev.dtos.FacturaDto;
import com.jeremias.dev.exceptions.ResourceNotFoundException;
import com.jeremias.dev.mappers.FacturaMapper;
import com.jeremias.dev.models.Factura;
import com.jeremias.dev.repositorios.FacturaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacturaService {
	private final FacturaRepository facturaRepository;
	

	private final FacturaMapper facturaMapper;
	
	public List<FacturaDto> findAll() {
		return facturaRepository.findAll().stream().map(facturaMapper::toDto).collect(Collectors.toList());
		/*return facturas.stream()
				.map(facturaMapper::toDto)
				.collect(Collectors.toList());*/
	}
	
	public FacturaDto findBy(long id) {
		return facturaMapper.toDto(  findFacturaById(id));
		
	}
	public Factura findFacturaById(long id) {
		return facturaRepository.findById(id).orElseThrow( ()-> new ResourceNotFoundException("no se encontro la factura"));
		
	}
	
	
	public FacturaDto save(FacturaDto facturaDto) {
		facturaDto.setUuid(UUID.randomUUID());
		facturaDto.setFecha(new Date(System.currentTimeMillis()));
		return facturaMapper.toDto(Optional.ofNullable(facturaRepository.save(facturaMapper.toEntity(facturaDto)))
								.orElseThrow(()-> new ResourceNotFoundException("Error al registrar la factura")));
		
	}
	
	public FacturaDto update( FacturaDto facturaDto) {
		Factura factura = findFacturaById(facturaDto.getId());
		factura.setNombres(facturaDto.getNombres());
		factura.setEmail(facturaDto.getEmail());
		factura.setNumeroCelular(facturaDto.getNumeroCelular());
		factura.setMetodoPago(facturaDto.getMetodoPago());
		factura.setTotal(facturaDto.getTotal());
		factura.setProducDetail(facturaDto.getProducDetail());
		factura.setFecha(facturaDto.getFecha());
		factura.setCreador(facturaDto.getCreador());
		
		Factura updatedFactura = facturaRepository.save(factura);
		return facturaMapper.toDto(updatedFactura);
	}
	public void delete(long id) {
		if (!facturaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Factura no encontrada");
        }
		facturaRepository.deleteById(id);
	}
}
