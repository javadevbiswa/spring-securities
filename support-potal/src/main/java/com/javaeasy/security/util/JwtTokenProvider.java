package com.javaeasy.security.util;

import static com.javaeasy.security.constant.JwtSecurityConstants.AUTHORITIES;
import static com.javaeasy.security.constant.JwtSecurityConstants.EXPIRATION_TIME;
import static com.javaeasy.security.constant.JwtSecurityConstants.GET_ARRAYS_ADMINISTRATION;
import static com.javaeasy.security.constant.JwtSecurityConstants.GET_ARRAYS_LLC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.javaeasy.security.constant.JwtSecurityConstants;
import com.javaeasy.security.model.UserPrincipal;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secret;

	public String generateJwtToken(UserPrincipal userPrincipal) {

		String[] claims = getClaimFromUser(userPrincipal);

		return JWT.create().withIssuer(GET_ARRAYS_LLC).withAudience(GET_ARRAYS_ADMINISTRATION).withIssuedAt(new Date())
				.withArrayClaim(AUTHORITIES, claims)
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(secret.getBytes()));

	}

	public List<GrantedAuthority> getAuthorities(String token) {
		String[] claims = getClaimsFromToken(token);

		return Arrays.stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	private String[] getClaimsFromToken(String token) {

		JWTVerifier verifier = getJWTVerifier();

		return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
	}

	private JWTVerifier getJWTVerifier() {
		JWTVerifier verifier = null;

		try {
			Algorithm algorithim = Algorithm.HMAC512(secret);
			verifier = JWT.require(algorithim).withIssuer(GET_ARRAYS_LLC).build();
			return verifier;
		} catch (JWTVerificationException e) {
			throw new JWTVerificationException(JwtSecurityConstants.TOKEN_CANNOT_BE_VERIFIED);
		}
	}

	private String[] getClaimFromUser(UserPrincipal userPrincipal) {

		List<String> authorities = new ArrayList<>();

		for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
			authorities.add(authority.getAuthority());
		}
		return authorities.toArray(new String[0]);
	}

	public Authentication getAuthentication(String userName, List<GrantedAuthority> authorities,
			HttpServletRequest request) {

		UsernamePasswordAuthenticationToken userNamePasswordToken = new UsernamePasswordAuthenticationToken(userName,
				null, authorities);

		userNamePasswordToken.setDetails(new WebAuthenticationDetails(request));

		return userNamePasswordToken;
	}

	public boolean isTokenValid(String userName, String token) {
		JWTVerifier verifier = getJWTVerifier();

		return StringUtils.isNotEmpty(token) && isTokenVerified(verifier, token);

	}

	private boolean isTokenVerified(JWTVerifier verifier, String token) {

		Date expirationDate = verifier.verify(token).getExpiresAt();

		return expirationDate.before(new Date());
	}

	public String getSubject(String token) {
		JWTVerifier verifier = getJWTVerifier();
		return verifier.verify(token).getSubject();
	}
}
