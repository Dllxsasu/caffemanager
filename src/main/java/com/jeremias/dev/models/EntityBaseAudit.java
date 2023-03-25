package com.jeremias.dev.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)

public abstract  class EntityBaseAudit  extends EntityBase implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@CreatedDate
	  @Column(nullable = false, updatable = false)
	  @Temporal(TemporalType.TIMESTAMP)
	  private Date createdDate;

	  @CreatedBy
	  private String createdBy;

	  @LastModifiedDate
	    @Column(nullable = false)
	  @Temporal(TemporalType.TIMESTAMP)
	  private Date lastModifiedDate;

	  @LastModifiedBy
	  private String lastModifiedBy;

	  @Column(name = "deleted")
	  private boolean deleted = false;
}
