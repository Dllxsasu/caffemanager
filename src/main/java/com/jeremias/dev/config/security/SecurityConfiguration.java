package com.jeremias.dev.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity

@EnableGlobalMethodSecurity(
		  prePostEnabled = true, 
		  securedEnabled = true, 
		  jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
	  private final jwtTokenFilter jwtAuthFilter;
	//  private final AuthenticationProvider authenticationProvider;


	  
	  
	  @Bean	
	  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		  http.cors().and()
	        .csrf()
	        .disable()
	        .headers().frameOptions().sameOrigin()
	        .and()
	        .authorizeHttpRequests()
	        .antMatchers("/rest/auth/**").permitAll()
			.antMatchers("/rest/**").permitAll()
			.antMatchers("/rest/dashboard").permitAll()
			.antMatchers("/**").permitAll()
	        .anyRequest()
	        .authenticated()
	        .and()
	        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .and()
	       // .authenticationProvider(authenticationProvider)
	        
	        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	  }
	  @Bean
		public PasswordEncoder passwordEncoder() {
		    return new BCryptPasswordEncoder();
		}
	  
}
