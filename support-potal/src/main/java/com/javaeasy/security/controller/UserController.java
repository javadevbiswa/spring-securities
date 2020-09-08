package com.javaeasy.security.controller;

import static com.javaeasy.security.constant.JwtSecurityConstants.JWT_TOKEN_HEADER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaeasy.security.model.User;
import com.javaeasy.security.model.UserPrincipal;
import com.javaeasy.security.service.UserService;
import com.javaeasy.security.util.JwtTokenProvider;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User newUser) {

		User user = userService.registreUser(newUser.getFirstName(), newUser.getLastName(), newUser.getUserName(),
				newUser.getEmail());

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@PostMapping(value = "/login")
	public ResponseEntity<User> loginUser(@RequestBody User user) {

		authenticateUser(user.getUserName(), user.getPassword());
		User userByUserName = userService.findByUserName(user.getUserName());
		UserPrincipal principal = new UserPrincipal(userByUserName);
		HttpHeaders header = getJwtHeader(principal);
		return new ResponseEntity<User>(userByUserName, header, HttpStatus.OK);
	}

	private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
		String token = tokenProvider.generateJwtToken(userPrincipal);
		HttpHeaders headers = new HttpHeaders();
		headers.add(JWT_TOKEN_HEADER, token);
		return headers;
	}

	private void authenticateUser(String userName, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
	}
}