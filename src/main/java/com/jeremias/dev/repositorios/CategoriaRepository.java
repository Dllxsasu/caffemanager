package com.jeremias.dev.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeremias.dev.models.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
