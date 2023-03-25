package com.jeremias.dev.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jeremias.dev.repositorios.CategoriaRepository;
import com.jeremias.dev.repositorios.FacturaRepository;
import com.jeremias.dev.repositorios.ProductoRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class DashboardService {
	private final CategoriaRepository categoriaRepository;
	private final ProductoRepository productoRepository;
	private final FacturaRepository facturaRepository;
	
	public Map<String, Object> getDetails(){
		Map<String, Object> mp = new HashMap<>();
		mp.put("categoria" , categoriaRepository.count());
        mp.put("producto" , productoRepository.count());
        mp.put("factura" , facturaRepository.count());
		return mp;
		
	}
}
