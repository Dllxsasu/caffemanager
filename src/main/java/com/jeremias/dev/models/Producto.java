package com.jeremias.dev.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Producto extends EntityBaseAudit {
	private String nombre;
	private double precio;
	private String status;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="categoria_fk", nullable=false)
	private Categoria categoria;
	private String descripcion;
}
