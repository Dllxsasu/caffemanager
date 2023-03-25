package com.jeremias.dev.services;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.jeremias.dev.dtos.UserDto;
import com.jeremias.dev.exceptions.ResourceNotFoundException;
import com.jeremias.dev.models.User;
import com.jeremias.dev.repositorios.UserRepository;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	public ResponseEntity<User> changePassword(UserDto userdto) {
		User principal = (User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
		
		return Optional.ofNullable(userRepo.findByUsername(userdto.getUsername()).get())
		        .map( user -> ResponseEntity.ok().body(user))
		        .orElse(ResponseEntity.notFound().build());
		
	/*	Optional<User> userOpt = Optional.ofNullable(userRepo.findByUsername(userdto.getUsername()))
			 			.		map(ResponseEntity::ok)
			 					.orElse(ResponseEntity.notFound().build());//.orElseThrow(  () -> new ResourceNotFoundException("User  not found") ));
	if(!userOpt.isPresent()){
		new ResponseEntity<>("",HttpStatus.BAD_REQUEST);
	}
	
	User user = userOpt.get();
	user.setPassword(passwordEncoder.encode( userdto.getPassword()));
	userRepo.save(user);
	new ResponseEntity<>("asd",HttpStatus.ACCEPTED);
	*/
	} 
	
	public void forgotPassord(String email) {
		User currentUser =  userRepo.findByEmail(email)
                					.orElseThrow(() -> new UsernameNotFoundException("User name not found with the " + email));
		//new User
		//generate a token for the password saying they have 5 minutos for change the password
		//sendMail("resetPassowrd")
	}
	
	public ResponseEntity<?> resetPassword(String uuid, UserDto dto) {
		User principal=this.userRepo.findByUuid(uuid).orElseThrow( () -> new UsernameNotFoundException("El token no es valido o expiro"));
		
		
		//User Prinici
		
		/*User principal = (User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
		
		*/
		//we obtain thhe user from the token
		principal.setPassword(passwordEncoder.encode(dto.getPassword()));
		userRepo.save(principal);
		
		return new ResponseEntity<>(true,HttpStatus.OK);
	}
	
}
