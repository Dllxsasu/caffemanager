package com.jeremias.dev.dtos;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FacturaDto {
	private long id;
	private UUID uuid;
	private String nombres;
	private String email;
	private String numeroCelular;
	private String metodoPago;
	private double total;
	
	private String producDetail;
	private Date fecha;
	private String creador;
}
