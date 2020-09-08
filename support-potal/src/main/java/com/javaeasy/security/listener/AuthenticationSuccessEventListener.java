package com.javaeasy.security.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import com.javaeasy.security.model.UserPrincipal;
import com.javaeasy.security.service.LoginAttemptService;

@Component
public class AuthenticationSuccessEventListener {

	@Autowired
	private LoginAttemptService loginAttemptService;

	@EventListener
	public void onSuccessLoginAttemptEvent(AuthenticationSuccessEvent event) {

		Object principal = event.getAuthentication().getPrincipal();

		if (principal instanceof UserPrincipal) {

			UserPrincipal userPrincipal = (UserPrincipal) principal;

			String userName = userPrincipal.getUsername();

			loginAttemptService.evictUserfromLoginAttemptCache(userName);

		}

	}
}
