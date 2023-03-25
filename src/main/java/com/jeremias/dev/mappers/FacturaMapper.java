package com.jeremias.dev.mappers;

import org.mapstruct.Mapper;

import com.jeremias.dev.dtos.FacturaDto;
import com.jeremias.dev.models.Factura;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface FacturaMapper {
	FacturaDto toDto(Factura factura);
	
    @Mapping(target = "id", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "deleted", ignore = true)
	@Mapping(target = "lastModifiedBy", ignore = true)
	@Mapping(target = "lastModifiedDate", ignore = true)
	Factura toEntity(FacturaDto facturaDto);
}
