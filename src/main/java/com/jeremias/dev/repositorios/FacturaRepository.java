package com.jeremias.dev.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeremias.dev.models.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

}
