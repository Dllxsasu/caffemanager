package com.jeremias.dev.config.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.jeremias.dev.dtos.UserDto;
import com.jeremias.dev.exceptions.AuthenticationException;
import com.jeremias.dev.models.RevokedToken;
import com.jeremias.dev.models.Role;
import com.jeremias.dev.utils.JwtUtils;
import com.jeremias.dev.utils.TokenSubjectRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class JwtTokenService {
	//rivate static final Logger LOG = LoggerFactory.getLogger(JwtTokenService.class);
	//Create a class usernameUUID which have field username and uuid both are strings
	public record UserNameUuid(String userName, String uuid) {}
	//List of loogeOutUser, we use copyOnWrite beacuause we lloogedoutUser only for iterations(read)
	private final List<UserNameUuid> loggedOutUsers = new CopyOnWriteArrayList<>();
	//we obteain the value from properties
	@Value("${security.jwt.token.secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length}")
	private long validityInMilliseconds; // 1 min
	//create a local variable 
	private SecretKey jwtTokenKey;

	@PostConstruct
	public void init() {
		//here we generate the secret key we firste decode the secretKey into byte[] after the we chante secrekey using keys.hmacShakeyFor
		this.jwtTokenKey = Keys.hmacShaKeyFor(Base64.getUrlDecoder().decode(secretKey.getBytes(StandardCharsets.ISO_8859_1)));
	}
	//We remove the tokens from loggedoutUser, this function is call every 90 seconds
	public void updateLoggedOutUsers(List<RevokedToken> revokedTokens) {
		//we remove all the items frorm loogetOutuser
		this.loggedOutUsers.clear();
		//We add all the values from revokedToken list tranform into a record 
		this.loggedOutUsers.addAll(revokedTokens.stream()
			.map(myRevokedToken -> new UserNameUuid(myRevokedToken.getName(), 
					myRevokedToken.getUuid())).toList());
	}
	////We obteain theTokenRoles from here
	public TokenSubjectRole getTokenUserRoles(Map<String,String> headers) {
		return JwtUtils.getTokenUserRoles(headers, this.jwtTokenKey);
	}
	//We create a toekn 
	public String createToken(String username, List<Role> roles, Optional<Date> issuedAtOpt) {
		//We create claims 
		Claims claims = Jwts.claims();
		//Set the common claim which is subject which is the username
		claims.setSubject(username);
		//From here we add all the roles with toeknAuthKey ,
		//First Parameter name of claim, second value
		claims.put(JwtUtils.TOKENAUTHKEY, 
				roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority()))				
				.filter(Objects::nonNull).collect(Collectors.toList()));
		//set the time from the last sms to the server
		claims.put(JwtUtils.TOKENLASTMSGKEY, new Date().getTime());		
		//we create a ramddom uui
		claims.put(JwtUtils.UUID, UUID.randomUUID().toString());
		//set date from now
		Date issuedAt = issuedAtOpt.orElse(new Date());
		//set in claims
		claims.setIssuedAt(issuedAt);
		Date validity = new Date(issuedAt.getTime() + validityInMilliseconds);
		//we set the expiration time
		claims.setExpiration(validity);
		//then we build the token we Use the Jwts.we tell we are use the builder then call bbuilder set Claims,signKet,compact
		return Jwts.builder().setClaims(claims)
				.signWith(this.jwtTokenKey, SignatureAlgorithm.HS256).compact();
	}
	//the refreshToken
	public String refreshToken(String token) {
		//Validate the token is valid
		this.validateToken(token);
		//we get the calims
		Optional<Jws<Claims>> claimsOpt = JwtUtils.getClaims(Optional.of(token), this.jwtTokenKey);
		if(claimsOpt.isEmpty()) {
			//in case is empty we trown a invalid token claims
			throw new AuthorizationServiceException("Invalid token claims");
		}
		//otherwise we get the claims
		Claims claims = claimsOpt.get().getBody();
		claims.setIssuedAt(new Date());
		claims.setExpiration(new Date(Instant.now().toEpochMilli() + validityInMilliseconds));
		//we get the new token
		String newToken = Jwts.builder().setClaims(claims).signWith(this.jwtTokenKey, SignatureAlgorithm.HS256).compact();
		return newToken;
	}
	
	public Authentication getAuthentication(String token) {		
		//Validate the token
		this.validateToken(token);
		//we filter the authoriest if they have guest role in case thety have that we send null in password
		if(this.getAuthorities(token).stream().filter(role -> role.equals(Role.GUEST)).count() > 0) {
			//we pass in password null that way we obtain a exception in uysernamePassword
			return new UsernamePasswordAuthenticationToken(this.getUsername(token), null);
		}
		//otherwise we get the Authentication object
		return new UsernamePasswordAuthenticationToken(this.getUsername(token), "", this.getAuthorities(token));
	}
	//we obtain the name
	public String getUsername(String token) {
		this.validateToken(token);
		return extractClaim(token, Claims::getSubject);
		
	//	return Jwts.parserBuilder().setSigningKey(this.jwtTokenKey).build().parseClaimsJws(token).getBody().getSubject();
	}
	 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		 //oBTEMOS EL CLAIMS DEL TOKEN  
		 final Claims claims = extractAllClaims(token);
		    return claimsResolver.apply(claims);
		  }
	
	public Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(this.jwtTokenKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
				
	}
	
	public String getUuid(String token) {
		this.validateToken(token);
		//we obtain the uuid from the token
	return	extractAllClaims(token).get(JwtUtils.UUID, String.class);
	//	return Jwts.parserBuilder().setSigningKey(this.jwtTokenKey).build().parseClaimsJws(token).getBody().get(JwtUtils.UUID, String.class);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Role> getAuthorities(String token) {
		//varrify ty
		this.validateToken(token);
		//Coletions of roles
		Collection<Role> roles = new LinkedList<>();
		//itearions
		for(Role role :Role.values()) {
			roles.add(role);
		}
		//Colection rolesStr, we set the values from the roles we put inside of 
		Collection<Map<String,String>> rolestrs = (Collection<Map<String,String>>) 
				extractAllClaims(token).get(JwtUtils.TOKENAUTHKEY);
		
		return rolestrs.stream()
				//filter the value exist otherwise we set roleGuest bydefault
				.map(str -> roles.stream().filter(r -> r.name().equals(str.getOrDefault(JwtUtils.AUTHORITY, "")))
						.findFirst().orElse(Role.GUEST))
				.collect(Collectors.toList());
	}

	public String resolveToken(HttpServletRequest req) {
		//From the header we get the tokenb
		String bearerToken = req.getHeader(JwtUtils.AUTHORIZATION);
		//we call a function which get a Optional<String>
		Optional<String> tokenOpt = resolveToken(bearerToken);
		//Verify if the token is empty otherwhise we pass null 
		return tokenOpt.isEmpty() ? null : tokenOpt.get();
	}

	public Optional<String> resolveToken(String bearerToken) {
		//Usually way to get tokenn from header 
		if (bearerToken != null && bearerToken.startsWith(JwtUtils.BEARER)) {
			return Optional.of(bearerToken.substring(7, bearerToken.length()));
		}
		return Optional.empty();
	}
	
	
	public int userNameLogouts(String userName) {
		//we count the size of they logout by name
		return this.loggedOutUsers.stream().filter(myUserName -> myUserName.userName.equalsIgnoreCase(userName)).toList().size();
	}
	
	public boolean validateToken(String token) {
		try {
			//we call parseBuilder for create a object from the token and get claims
			//call parseBuilder, setSigniKet,buildang parseClaims from the token, and we obtain the Claims
			Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(this.jwtTokenKey).build().parseClaimsJws(token);
			//we get the subject and or throw a authentificationException
			String subject = Optional.ofNullable(claimsJws.getBody().getSubject()).orElseThrow(() -> new AuthenticationException("Invalid JWT token"));
			String uuid = Optional.ofNullable(claimsJws.getBody().get(JwtUtils.UUID, String.class)).orElseThrow(() -> new AuthenticationException("Invalid JWT token"));
			// log.info("Subject: {}, Uuid: {}, LoggedOutUsers: {}", subject, uuid, JwtTokenService.loggedOutUsers.size());
			//in here we verify the token dont' be in the list loggedOutUsers
			return this.loggedOutUsers.stream().noneMatch(myUserName -> subject.equalsIgnoreCase(myUserName.userName) && uuid.equals(myUserName.uuid));
		} catch (JwtException | IllegalArgumentException e) {
			throw new AuthenticationException("Expired or invalid JWT token",e);
		}
	}
}
