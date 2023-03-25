package com.jeremias.dev.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jeremias.dev.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
	@Query("select u from Producto u where categoria.id =?1 and enabled=true ")
	List<Producto> findAllByCategoria(Long idCat);
}
