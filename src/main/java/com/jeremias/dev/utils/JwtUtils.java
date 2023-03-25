package com.jeremias.dev.utils;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.jeremias.dev.models.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class JwtUtils {
	public static final String AUTHORIZATION = HttpHeaders.AUTHORIZATION.toLowerCase();
	public static final String TOKENAUTHKEY = "auth";
	public static final String TOKENLASTMSGKEY = "lastmsg";
	public static final String UUID = "uuid";
	public static final String BEARER = "Bearer ";
	public static final String AUTHORITY = "authority";
	//We get the token from the headers, inside of the aplication
	public static Optional<String> extractToken(Map<String,String> headers) {
		String authStr = headers.get(AUTHORIZATION);
		return extractToken(Optional.ofNullable(authStr));
	}
	//Extract the token from the bearer 
	private static Optional<String> extractToken(Optional<String> authStr) {
		if (authStr.isPresent()) {
			authStr = Optional.ofNullable(authStr.get().startsWith(BEARER) ? authStr.get().substring(7) : null);
		}
		return authStr;
	}
//Claims are the information about the subjecty(token
	public static Optional<Jws<Claims>> getClaims(Optional<String> token, Key jwtTokenKey) {
		//Check is empty
		if (!token.isPresent()) {
			return Optional.empty();
		}
		//otherwise they send a ooptional claims, first use JWTS.parsebuilder() set secretke, build, apar, after we have to use parseClaimsjws whick have inside a exceptions like a exprire or etcc
		return Optional.of(Jwts.parserBuilder().setSigningKey(jwtTokenKey).build().parseClaimsJws(token.get()));
	}
	//whith this methoh we get all the roles inside 
	public static String getTokenRoles(Map<String,String> headers, Key jwtTokenKey) {
		//first we get the token from the header
		Optional<String> tokenStr = extractToken(headers);
		//we get all the claims
		Optional<Jws<Claims>> claims = JwtUtils.getClaims(tokenStr, jwtTokenKey);
		//verify is present and the expiration
		if (claims.isPresent() && new Date().before(claims.get().getBody().getExpiration())) {
			//If everything is okey we retorn the tokenauthkey
			//WE get the body and after the we get the auth into a string
			return claims.get().getBody().get(TOKENAUTHKEY).toString();
		}
		//otherwise we get empty string
		return "";
	}
//from the tokenn we get all the userroles
	public static TokenSubjectRole getTokenUserRoles(Map<String,String> headers, Key jwtTokenKey) {
		//ge the token
		Optional<String> tokenStr = extractToken(headers);
		//get claims
		Optional<Jws<Claims>> claims = JwtUtils.getClaims(tokenStr, jwtTokenKey);
		//Verify if exist and don't expire
		if (claims.isPresent() && new Date().before(claims.get().getBody().getExpiration())) {
			//we use record for setup set the subject from the claims and roles from that
			return new TokenSubjectRole(claims.get().getBody().getSubject(),
					claims.get().getBody().get(TOKENAUTHKEY).toString());
		}
		//otherwise we return a a object with subject and role nulls
		return new TokenSubjectRole(null, null);
	}
	//with this function we check if the token is valid
	public static boolean checkToken(HttpServletRequest request, Key jwtTokenKey) {
		//WE get the tokenStr
		Optional<String> tokenStr = JwtUtils
				.extractToken(Optional.ofNullable(request.getHeader(JwtUtils.AUTHORIZATION)));
		//we get the claims
		Optional<Jws<Claims>> claims = JwtUtils.getClaims(tokenStr, jwtTokenKey);
		//verify if is present, expiration, roles they have
		if (claims.isPresent() && new Date().before(claims.get().getBody().getExpiration())
				&& claims.get().getBody().get(TOKENAUTHKEY).toString().contains(Role.USERS.name())
				&& !claims.get().getBody().get(TOKENAUTHKEY).toString().contains(Role.GUEST.name())) {
			return true;
		}
	//	otherwise they return false
		return false;
	}
}
