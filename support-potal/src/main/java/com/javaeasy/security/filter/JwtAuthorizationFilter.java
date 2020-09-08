package com.javaeasy.security.filter;

import static com.javaeasy.security.constant.JwtSecurityConstants.JWT_TOKEN_HEADER;
import static com.javaeasy.security.constant.JwtSecurityConstants.OPTIONS_HTTP_METHOD;
import static com.javaeasy.security.constant.JwtSecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.javaeasy.security.util.JwtTokenProvider;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
			response.setStatus(HttpStatus.OK.value());

		} else {
			String authorizationHeader = request.getHeader(JWT_TOKEN_HEADER);
			if (authorizationHeader == null || authorizationHeader.startsWith(TOKEN_PREFIX)) {
				filterChain.doFilter(request, response);
				return;
			}

			String token = authorizationHeader.substring(TOKEN_PREFIX.length());
			String userName = jwtTokenProvider.getSubject(token);

			if (jwtTokenProvider.isTokenValid(userName, token)
					&& SecurityContextHolder.getContext().getAuthentication() == null) {
				List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
				Authentication authentication = jwtTokenProvider.getAuthentication(userName, authorities, request);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				SecurityContextHolder.clearContext();
			}
		}
		filterChain.doFilter(request, response);
	}

}
