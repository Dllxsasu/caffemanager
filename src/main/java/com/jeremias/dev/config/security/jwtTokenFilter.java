package com.jeremias.dev.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@Component
public class jwtTokenFilter  extends GenericFilterBean {

	private final JwtTokenService jwtService;
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		String token = jwtService.resolveToken((HttpServletRequest) request);
		if(token!=null && jwtService.validateToken(token)) {
			Authentication auth = token == null ? null :jwtService.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		
		chain.doFilter(request, response);
		
	}

}
