package com.javaeasy.security.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_user")
public class User implements Serializable {

	@Transient
	private static final long serialVersionUID = 732259795699210871L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, updatable = false)
	private Long id;
	private String userId;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private String email;
	private String profileImageUrl;
	private Date lastLoginDate;
	private Date lastLoginDateDisplay;
	private Date joinDate;
	private String role;
	private String[] authorities;
	private boolean isActive;
	private boolean isNotLocked;
}
