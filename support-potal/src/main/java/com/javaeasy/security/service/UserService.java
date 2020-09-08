package com.javaeasy.security.service;

import static com.javaeasy.security.constant.UserServiceConstants.DEFAULT_PROFILE_IMAGE_PATH;
import static com.javaeasy.security.constant.UserServiceConstants.EMAIL_ALREADY_EXIST;
import static com.javaeasy.security.constant.UserServiceConstants.NO_USERNAME_FOUND_BY_USERNAME;
import static com.javaeasy.security.constant.UserServiceConstants.NO_USER_FOUND_BY_EMAIL;
import static com.javaeasy.security.constant.UserServiceConstants.NO_USER_FOUND_BY_USER_NAME;
import static com.javaeasy.security.constant.UserServiceConstants.USER_NAME_ALREADY_EXIST;
import static com.javaeasy.security.enumuration.Role.ROLE_USER;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.javaeasy.security.constant.UserServiceConstants;
import com.javaeasy.security.enumuration.Role;
import com.javaeasy.security.exception.EmailExistException;
import com.javaeasy.security.exception.EmailNotFoundException;
import com.javaeasy.security.exception.UserNameExistException;
import com.javaeasy.security.exception.UserNotFoundException;
import com.javaeasy.security.model.User;
import com.javaeasy.security.model.UserPrincipal;
import com.javaeasy.security.repository.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private LoginAttemptService loginAttemptService;

	@Autowired
	private EmailService emailService;

	public User registreUser(String firstName, String lastName, String userName, String email) {

		validateNewUserNameAndEmail(EMPTY, userName, email);
		User user = new User();
		user.setUserId(generateUserId());
		String password = generatePassword();
		user.setPassword(encodePassword(password));
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUserName(userName);
		user.setEmail(email);
		user.setActive(true);
		user.setRole(ROLE_USER.name());
		user.setAuthorities(ROLE_USER.getAuthorities());
		user.setJoinDate(new Date());
		user.setNotLocked(true);
		user.setProfileImageUrl(getTemporaryProfileImageUrl());

		User registerdUser = userRepository.save(user);
		emailService.sendEmail(firstName, password, email);
		return registerdUser;
	}

	public User addNewUser(String firstName, String lastName, String role, String userName, String email,
			boolean isNotLocked, boolean isActive, MultipartFile file) {

		validateNewUserNameAndEmail(EMPTY, userName, email);

		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setNotLocked(isNotLocked);
		user.setActive(isActive);
		user.setJoinDate(new Date());
		String password = generatePassword();
		;
		user.setPassword(encodePassword(password));
		String userId = generateUserId();
		user.setUserId(userId);
		user.setRole(role);
		user.setAuthorities(getRoleEnumValue(role).getAuthorities());
		user.setUserName(userName);
		user.setProfileImageUrl(getProfileImageUrl());

		User addedUser = userRepository.save(user);

		return addedUser;
	}

	private String getProfileImageUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	private Role getRoleEnumValue(String role) {
		return Role.valueOf(role);
	}

	public User updateUser(String currentUserName, String firstName, String lastName, String newRole,
			String newUserName, String newEmail, boolean isNotLocked, boolean isActive, MultipartFile file) {

		User currentUser = validateNewUserNameAndEmail(currentUserName, newUserName, newEmail);
		currentUser.setFirstName(firstName);
		currentUser.setLastName(lastName);
		currentUser.setEmail(newEmail);
		currentUser.setUserName(newUserName);
		currentUser.setRole(newRole);
		currentUser.setActive(isActive);
		currentUser.setNotLocked(isNotLocked);
		currentUser.setAuthorities(getRoleEnumValue(newRole).getAuthorities());
		currentUser.setProfileImageUrl(getProfileImageUrl(currentUser, file));
		currentUser.setUserName(newUserName);

		userRepository.saveAndFlush(currentUser);
		return currentUser;
	}

	private String getProfileImageUrl(User currentUser, MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteUser(long id) {
		userRepository.deleteById(id);
	}

	public void resetPassword(String email) {

		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new EmailNotFoundException(NO_USER_FOUND_BY_EMAIL);
		}

		String password = generatePassword();
		user.setPassword(encodePassword(password));

		userRepository.saveAndFlush(user);

		emailService.sendEmail(user.getFirstName(), password, email);

	}

	public User updateProfileImage(String userName, MultipartFile profileImage) {

		return null;
	}

	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public List<User> getAllUser() {
		return userRepository.findAll();
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		User user = userRepository.findByUserName(userName);

		if (user == null) {
			throw new UsernameNotFoundException(NO_USERNAME_FOUND_BY_USERNAME);
		} else {
			validateLoginAttempt(user);
			user.setLastLoginDateDisplay(user.getLastLoginDate());
			user.setLastLoginDate(new Date());
			userRepository.save(user);
			UserPrincipal userPrincipal = new UserPrincipal(user);
			return userPrincipal;
		}
	}

	private void validateLoginAttempt(User user) {

		String userName = user.getUserName();

		if (user.isNotLocked()) {
			if (loginAttemptService.hasExceededMaximiumLoginAttempt(userName)) {
				user.setNotLocked(false);
			} else {
				user.setNotLocked(true);
			}
		} else {
			loginAttemptService.evictUserfromLoginAttemptCache(userName);
		}
	}

	private String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(10);
	}

	private String getTemporaryProfileImageUrl() {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_PROFILE_IMAGE_PATH).toUriString();
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	private String generateUserId() {

		String userId = RandomStringUtils.randomNumeric(10);
		return userId;
	}

	private User validateNewUserNameAndEmail(String currentUserName, String newUserName, String newEmail) {

		User newUserByUserName = findByUserName(newUserName);
		User newUserByEmail = findByEmail(newEmail);
		if (StringUtils.isNotBlank(currentUserName)) {

			User currentUser = findByUserName(currentUserName);

			if (currentUser == null) {
				throw new UserNotFoundException(NO_USER_FOUND_BY_USER_NAME + currentUserName);
			}

			if (newUserByUserName != null && !currentUser.getId().equals(newUserByUserName.getId())) {
				throw new UserNameExistException(USER_NAME_ALREADY_EXIST);
			}

			if (newUserByEmail != null && !currentUser.getId().equals(newUserByEmail.getId())) {
				throw new EmailExistException(EMAIL_ALREADY_EXIST);
			}
			return currentUser;
		} else {
			if (newUserByUserName != null) {
				throw new UserNameExistException(USER_NAME_ALREADY_EXIST);
			}
			if (newUserByEmail != null) {
				throw new EmailExistException(EMAIL_ALREADY_EXIST);
			}
			return null;
		}
	}
}