package com.jeremias.dev.config.security;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jeremias.dev.dtos.ApiResponse;
import com.jeremias.dev.dtos.RefreshTokenDto;
import com.jeremias.dev.dtos.RevokedTokenDto;
import com.jeremias.dev.dtos.UserDto;
import com.jeremias.dev.exceptions.AuthenticationException;
import com.jeremias.dev.exceptions.ResourceNotFoundException;
import com.jeremias.dev.exceptions.appException;
import com.jeremias.dev.mappers.RevokedTokenMapper;
import com.jeremias.dev.mappers.UserMapper;
import com.jeremias.dev.models.RevokedToken;
import com.jeremias.dev.models.Role;
import com.jeremias.dev.models.User;
import com.jeremias.dev.repositorios.RevokedTokenRepository;
import com.jeremias.dev.repositorios.UserRepository;
import com.jeremias.dev.response.LoginResponse;

import com.jeremias.dev.utils.TokenSubjectRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthServiceImpl {
	//private final static logger log = loggerFactory.getlogger(UserDetailServiceBase.class);
	private final static long logOUT_TIMEOUT = 185L;
	private final UserRepository userRepository;
	private final RevokedTokenRepository revokedTokenRepository;
	protected final RevokedTokenMapper revokedTokenMapper;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender javaMailSender;
	private final JwtTokenService jwtTokenService;
	protected final UserMapper userMapper;
	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
	@Value("${mail.url.uuid.confirm}")
	private String confirmUrl;
/*
	public UserDetailServiceBase(UserRepository userRepository, PasswordEncoder passwordEncoder,
			RevokedTokenRepository revokedTokenRepository, JavaMailSender javaMailSender,
			JwtTokenService jwtTokenService, UserMapper userMapper, RevokedTokenMapper revokedTokenMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.javaMailSender = javaMailSender;
		this.jwtTokenService = jwtTokenService;
		this.userMapper = userMapper;
		this.revokedTokenRepository = revokedTokenRepository;
		this.revokedTokenMapper = revokedTokenMapper;
	}
*/
	public boolean isAvalaibleUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	public boolean isAvalaibleEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public void updateloggedOutUsers() {
		this.updateloggedOutUsers(logOUT_TIMEOUT);
	}

	protected void updateloggedOutUsers(Long timeout) {
		//list from bd all the revokedTokens 
		final List<RevokedToken> revokedTokens = new ArrayList<RevokedToken>(this.revokedTokenRepository.findAll());
		//
		this.jwtTokenService.updateLoggedOutUsers(revokedTokens.stream()
				//Filterlist form revodektokens
				.filter(myRevokedToken -> myRevokedToken.getLastLogout() == null
						|| !myRevokedToken.getLastLogout().isBefore(LocalDateTime.now().minusSeconds(timeout)))
				.toList());
		//we delete all they have his lasLogout
		this.revokedTokenRepository.deleteAll(revokedTokens.stream()
				.filter(myRevokedToken -> myRevokedToken.getLastLogout() != null
						&& myRevokedToken.getLastLogout().isBefore(LocalDateTime.now().minusSeconds(timeout)))
				.toList());
	}
	//we obtain the user Brom bearStr just header
	public User getCurrentUser(String bearerStr) {
		//WE user resolveToken in this case for get the tokken, that function get "" if is not found
		//if is "" the token then we have to throw a exception saying invalid bearer string
		final String token = this.jwtTokenService.resolveToken(bearerStr)
				.orElseThrow(() -> new AuthenticationException("Invalid bearer string."));
		//then we get the username  or subject 
		final String userName = this.jwtTokenService.getUsername(token);
		//we find the username inside bd if don't existe we thrown a exception saying the user don't exist
		return this.userRepository.findByUsername(userName).orElseThrow(
				() -> new UsernameNotFoundException(String.format("The username %s doesn't exist", userName)));
	}

	public RefreshTokenDto refreshToken(String bearerToken) {
		//same before we get the token
		Optional<String> tokenOpt = this.jwtTokenService.resolveToken(bearerToken);
		//verify if the tokenOPt is present
		if (tokenOpt.isEmpty()) {
			//if is we trown a exception
			throw new AuthorizationServiceException("Invalid token");
		}
		//if not we get a new token
		String newToken = this.jwtTokenService.refreshToken(tokenOpt.get());
		log.info("Jwt Token refreshed.");
		//and setUp for dto refresh token
		return new RefreshTokenDto(newToken);
	}
	//how say the name of the method save 
	public UserDto save(UserDto appUser) {
		
		return this.userMapper.convert(Optional
				//we return the value or otherwise we return empty opional
				.ofNullable(
						//first we save 
						this.userRepository
						.save(
								//first we convert from dto to entity model
								this.userMapper.convert(appUser, this.userRepository.findById(appUser.getId())))
						
						)
				//then if is a optional empty we throw a resourceNotFoundexception
				.orElseThrow(() -> new ResourceNotFoundException("User " + appUser.getId() + " not found")), "", 10L);
	}
/*
	public ResponseEntity<?> signin(UserDto appUserDto) {
		//we verify if is signin
		if(userRepository.existsByUsername(appUserDto.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(appUserDto.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        
       
		return this.signin(appUserDto);
       
	}
*/
	public ResponseEntity<?> signin(UserDto appUserDto) {
		//verify if the id isn't null
		
	
		if(userRepository.existsByUsername(appUserDto.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(appUserDto.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }
        
		User result = 
				userRepository.save(this.checkSaveSignin(
						 this.userMapper.convert(appUserDto,
						this.userRepository.findByUsername(appUserDto.getUsername())))
				 .orElseThrow(() -> new appException("error al registrar")));
		
		//User result = userRepository.save(user);
		//result = result.stream().map(myAppUser -> persist ? this.userRepository.save(myAppUser) : myAppUser).findAny();
		log.info("user registrado");
		return new ResponseEntity(new ApiResponse(true, "Okey!"),
                 HttpStatus.OK);
		//return result;
	}

	private Optional<User> checkSaveSignin(User entity) {
		///create a emppty user
		Optional<User> result = Optional.empty();
		//verify if the id is null
		if (entity.getId() == null) {
			log.info("Password: "+entity.getPassword());
			//we encrpyted the password with passwordEnconder
			String encryptedPassword = this.passwordEncoder.encode(entity.getPassword());
			entity.setPassword(encryptedPassword);
			//we generate a randomUUID
			UUID uuid = UUID.randomUUID();
			entity.setUuid(uuid.toString());
			//we set as default false
			entity.setLocked(false);
			///we set as user
			entity.setRoles(Role.USERS.name());
			//verifiy is confirm or not
			//boolean emailConfirmEnabled = this.confirmUrl != null && !this.confirmUrl.isBlank();
			//then turnOn
			entity.setEnabled(false);
			
		//	if (emailConfirmEnabled) {
			//	they send a confirm email
				this.sendConfirmMail(entity);
	//		}
			//then set in the result
			result = Optional.of(entity);
		}
		log.warn("Username multiple signin: {}", entity.getUsername());
		return result;
	}

	public Boolean confirmUuid(String uuid) {
		return this.confirmUuid(this.userRepository.findByUuid(uuid), uuid);
	}

	private Boolean confirmUuid(Optional<User> entityOpt, final String uuid) {
		//verify if exist  set enable or othercase set false 
		return entityOpt.map(entity -> {
			entity.setEnabled(true);
			return this.userRepository.save(entity).isEnabled();
		}).orElseGet(() -> {
			log.warn("Uuid confirm failed: {}", uuid);
			return Boolean.FALSE;
		});
	}

	public LoginResponse login(UserDto appUserDto) {
		//we get userDto  parmeter(user, password)
		return this.loginHelp(this.userRepository.findByUsername(appUserDto.getUsername()), appUserDto.getPassword());
	}

	public Boolean logout(String bearerStr) {
		//we add t
		Optional<RevokedToken> revokedTokenOpt = this.logoutToken(bearerStr).stream()
				.peek(revokedToken -> this.revokedTokenRepository.save(revokedToken)).findAny();
		return revokedTokenOpt.isPresent();
	}

	public Boolean logout(RevokedTokenDto revokedTokenDto) {
		this.revokedTokenRepository.findAll().stream()
				.filter(myRevokedToken -> myRevokedToken.getUuid().equals(revokedTokenDto.getUuid())
						&& myRevokedToken.getName().equalsIgnoreCase(revokedTokenDto.getName()))
				.findAny().or(() -> Optional
						.of(this.revokedTokenRepository.save(this.revokedTokenMapper.convert(revokedTokenDto))));
		return Boolean.TRUE;
	}

	public Optional<RevokedToken> logoutToken(String bearerStr) {
		//validate the toke inside
		if (!this.jwtTokenService.validateToken(this.jwtTokenService.resolveToken(bearerStr).orElse(""))) {
			throw new AuthenticationException("Invalid token.");
		}
		//we get the username or thrwon a authentification exception
		String username = this.jwtTokenService.getUsername(this.jwtTokenService.resolveToken(bearerStr)
				.orElseThrow(() -> new AuthenticationException("Invalid bearer string.")));
		//same here
		String uuid = this.jwtTokenService.getUuid(this.jwtTokenService.resolveToken(bearerStr)
				.orElseThrow(() -> new AuthenticationException("Invalid bearer string.")));
		//we find the user by username or throen a exception resourceNotFOundException
		this.userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Username not found: " + username));
		//we verify if uuid and username are inside revokedToken, if count how much are inside
		long revokedTokensForUuid = this.revokedTokenRepository.findAll().stream()
				.filter(myRevokedToken -> myRevokedToken.getUuid().equals(uuid)
						&& myRevokedToken.getName().equalsIgnoreCase(username))
				.count();
		//we creata empty optional
		Optional<RevokedToken> result = Optional.empty();
		
		if (revokedTokensForUuid == 0) {
			//we create a revokeToken and is store in bd, for the filter the token when is loogout the user
			result = Optional
					.of(this.revokedTokenRepository.save(new RevokedToken(username, uuid, LocalDateTime.now())));
		} else {
			//in this case we don't store anything because is store and we return a empty optional
			log.warn("Duplicate logout for user {}", username);
		}
		return result;
	}

	private LoginResponse loginHelp(Optional<User> entityOpt, String passwd) {
		//We create a object from UserDto
		LoginResponse response = new LoginResponse("",false);
		//We get the a Optional<Role> we find the rol we are goin to use
		Optional<Role> myRole = entityOpt.stream().flatMap(myUser -> Arrays.stream(Role.values())
				.filter(role1 -> Role.USERS.equals(role1)).filter(role1 -> role1.name().equals(myUser.getRoles())))
				.findAny();
		//verify if we have a role, and the user is enable, and we match the password
		if (myRole.isPresent() && entityOpt.get().isEnabled()
				&& this.passwordEncoder.matches(passwd, entityOpt.get().getPassword())) {
			
			Callable<String> callableTask = () -> this.jwtTokenService.createToken(entityOpt.get().getUsername(),
					Arrays.asList(myRole.get()), Optional.empty());
			try {
				//The callable is executed with a 3 second delay on a different thread pool to limit the amount of logouts a user can do between updates of the revoked token cache.
				String jwtToken = executorService.schedule(callableTask, 3, TimeUnit.SECONDS).get();
				//if ahve more then 2 usernameLogouts return a empty user, toherwise me return a user with values
				response = new LoginResponse(jwtToken, true);
				//user = this.jwtTokenService.userNameLogouts(entityOpt.get().getUsername()) > 2 ? user
				//		: this.userMapper.convert(entityOpt.get(), jwtToken, 0L);
			} catch (InterruptedException | ExecutionException e) {
				log.error("login failed.", e);
			}
		}
		return response;
	}
	
	public List<UserDto> getAllUser(){
		return userRepository.findByEnabled(true).stream().map( item -> userMapper.convert(item)).collect(Collectors.toList());
	}
	
	private void sendConfirmMail(User entity) {
		//we send the messages 
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom("springreddit@email.com");
       // msg.setTo(notificationEmail.getRecipient());
		//email from the user
		msg.setTo(entity.getEmail());
		//Subject
		msg.setSubject("AngularPortfolioMgr Account Confirmation Mail");
		//Url
		String url = this.confirmUrl + "/" + entity.getUuid();
		//Text
		msg.setText(String
				.format("Welcome to the AngularPwaMessenger please use this link ("+url+") to confirm your account.", url));
		this.javaMailSender.send(msg);
		log.info("Confirm Mail send to: " + entity.getEmail());
	}
	//we get token
	public TokenSubjectRole getTokenRoles(Map<String, String> headers) {
		return this.jwtTokenService.getTokenUserRoles(headers);
	}
//we obtain data from user by id and return Dto 
	public UserDto load(Long id) {
		return this.userMapper.convert(this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found")), "", 10);
	}
/*
	public void sendKafkaEvent(KafkaEventDto kafkaEventDto) {
		log.info("KafkaEvent not send.");
	}
	*/
	/*
	 * public List<UserDto> loadAll() { return
	 * this.userRepository.findAll().stream() .flatMap(entity ->
	 * Stream.of(this.appUserMapper.convert(Optional.of(entity))))
	 * .collect(Collectors.toList()); }
	 */
}
