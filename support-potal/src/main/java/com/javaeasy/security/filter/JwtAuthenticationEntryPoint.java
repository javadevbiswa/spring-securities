package com.javaeasy.security.filter;

import static com.javaeasy.security.constant.JwtSecurityConstants.FORBIDDEN_MESSAGE;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaeasy.security.model.HttpResponse;

@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authenticationException) throws IOException {
		HttpResponse httpResponse = new HttpResponse(new Date(), FORBIDDEN.value(), FORBIDDEN,
				FORBIDDEN.getReasonPhrase().toUpperCase(), FORBIDDEN_MESSAGE);

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(FORBIDDEN.value());

		OutputStream os = response.getOutputStream();
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.writeValue(os, httpResponse);

		os.flush();
	}

}
