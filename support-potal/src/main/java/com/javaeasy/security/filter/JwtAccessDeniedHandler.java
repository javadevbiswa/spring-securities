package com.javaeasy.security.filter;

import static com.javaeasy.security.constant.JwtSecurityConstants.ACCESS_DENIED_MESSAGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaeasy.security.model.HttpResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		HttpResponse httpResponse = new HttpResponse(new Date(), UNAUTHORIZED.value(), UNAUTHORIZED,
				UNAUTHORIZED.getReasonPhrase().toUpperCase(), ACCESS_DENIED_MESSAGE);

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(UNAUTHORIZED.value());

		OutputStream os = response.getOutputStream();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(os, httpResponse);

		os.flush();

	}

}
