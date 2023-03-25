package com.jeremias.dev.models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
@Entity
public class Factura extends EntityBaseAudit {
	
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
