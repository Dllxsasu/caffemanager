package com.jeremias.dev.models;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode
@MappedSuperclass
public abstract class EntityBase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//We have to change to uuid for now is okey is only the v1
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	
}
