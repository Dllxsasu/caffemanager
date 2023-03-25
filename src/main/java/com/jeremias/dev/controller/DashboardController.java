package com.jeremias.dev.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeremias.dev.repositorios.CategoriaRepository;
import com.jeremias.dev.repositorios.FacturaRepository;
import com.jeremias.dev.repositorios.ProductoRepository;
import com.jeremias.dev.services.DashboardService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/dashboard")
public class DashboardController {
	private final DashboardService dashboardService;
	
	@GetMapping
	public ResponseEntity<?> getDetails(){
		return new ResponseEntity( dashboardService.getDetails(), HttpStatus.OK);
	}
}
