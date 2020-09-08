package com.javaeasy.security.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import com.javaeasy.security.service.LoginAttemptService;

@Component
public class AuthenticationFailureEventListener {

	@Autowired
	private LoginAttemptService loginAttemptService;

	@EventListener
	public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {

		Object principal = event.getAuthentication().getPrincipal();

		if (principal instanceof String) {

			String userName = (String) principal;

			loginAttemptService.addUserIntoLoginAttemptCache(userName);
		}

	}
}
