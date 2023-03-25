package com.jeremias.dev.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeremias.dev.config.security.AuthServiceImpl;
import com.jeremias.dev.dtos.AuthCheckDto;
import com.jeremias.dev.dtos.RefreshTokenDto;
import com.jeremias.dev.dtos.UserDto;
import com.jeremias.dev.models.Role;
import com.jeremias.dev.response.LoginResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("rest/auth")
@RequiredArgsConstructor
@Api(value="Language API", description="Operations pertaining to Language")
public class AuthController {
	private final AuthServiceImpl authService;
	/*
	@Value("${spring.mail.username}")
	private String mailuser;
	@Value("${spring.mail.password}")
	private String mailpwd;
	@Value("${spring.profiles.active:}")
	private String activeProfile;
*/


	@PostMapping("/authorize")
	public AuthCheckDto postAuthorize(@RequestBody AuthCheckDto authcheck, @RequestHeader Map<String, String> header) {
		String tokenRoles = this.authService.getTokenRoles(header).role();
		if (tokenRoles != null && tokenRoles.contains(Role.USERS.name()) && !tokenRoles.contains(Role.GUEST.name())) {
			return new AuthCheckDto(authcheck.getPath(), true);
		} else {
			return new AuthCheckDto(authcheck.getPath(), false);
		}
	}
	
	   @GetMapping("/checkUsernameAvailability")
	    public Boolean checkUsernameAvailability(@RequestParam(value = "username") String username) {

	        return !authService.isAvalaibleUsername(username);
	    }

	    @GetMapping("/checkEmailAvailability")
	    public boolean checkEmailAvailability(@RequestParam(value = "email") String email) {
	  
	        return !authService.isAvalaibleEmail(email);
	    }
	    
	    
	@PostMapping("/signin")
	public ResponseEntity<?> postUserSignin(@RequestBody UserDto myUser) {
		return this.authService.signin(myUser);
	}

	@GetMapping("/confirm/{uuid}")
	public Boolean getConfirmUuid(@PathVariable String uuid) {
		return this.authService.confirmUuid(uuid);
	}
	
	@PostMapping("/login")
	public LoginResponse postUserLogin(@RequestBody UserDto myUser) {
		return this.authService.login(myUser);
	}

	@PutMapping("/logout")
	public Boolean putUserLogout(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String bearerStr) {
		return this.authService.logout(bearerStr);
	}

	@GetMapping("/refreshToken")
	public RefreshTokenDto getRefreshToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String bearerStr) {
		return this.authService.refreshToken(bearerStr);
	}

	@GetMapping("/id/{id}")
	public UserDto getUser(@PathVariable Long id) {
		return this.authService.load(id);
	}
	
	@PutMapping()
	public UserDto putUser(@RequestBody UserDto appUserDto) {
		return this.authService.save(appUserDto);
	}
/*
	@PutMapping("/kafkaEvent")
	public ResponseEntity<Boolean> putKafkaEvent(@RequestBody KafkaEventDto dto) {
		ResponseEntity<Boolean> result = new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.FORBIDDEN);
		if (!this.activeProfile.toLowerCase().contains("prod")) {
			result = new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.ACCEPTED);
			try {
				this.authService.sendKafkaEvent(dto);
			} catch (Exception e) {
				result = new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.BAD_REQUEST);
			}
		}
		return result;
	}
*/
//	@RequestMapping(value="/updateDB", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Boolean> getUpdateDB() throws InterruptedException {
//		boolean result = this.service.updateDB();
//		return new ResponseEntity<Boolean>( result, result ? HttpStatus.OK : HttpStatus.NOT_IMPLEMENTED);	
//	}
}
