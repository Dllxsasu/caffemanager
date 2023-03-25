package com.jeremias.dev.models;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
public class Categoria extends EntityBaseAudit{
	private String nombre;
}
