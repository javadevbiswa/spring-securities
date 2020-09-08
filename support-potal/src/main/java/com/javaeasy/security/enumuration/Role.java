package com.javaeasy.security.enumuration;

import static com.javaeasy.security.constant.Authority.ADMIN_AUTHORITIES;
import static com.javaeasy.security.constant.Authority.HR_AUTHORITIES;
import static com.javaeasy.security.constant.Authority.MANAGER_AUTHORITIES;
import static com.javaeasy.security.constant.Authority.SUPER_ADMIN_AUTHORITIES;
import static com.javaeasy.security.constant.Authority.USER_AUTHORITIES;

import lombok.Getter;

@Getter
public enum Role {

	ROLE_USER(USER_AUTHORITIES), ROLE_HR(HR_AUTHORITIES), ROLE_MANAGER(MANAGER_AUTHORITIES),
	ROLE_ADMIN(ADMIN_AUTHORITIES), ROLE_SUPER_USER(SUPER_ADMIN_AUTHORITIES);

	private String[] authorities;

	Role(String... authorities) {
		this.authorities = authorities;
	}

}
