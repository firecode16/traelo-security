package com.traelo.security.model;

import java.util.Set;

import com.traelo.security.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class RegisterUser {
	private Long userId;
	private String email;
	private String username;
	private String password;
	private String fullName;
	private String phone;
	private String createdAt;
	private Set<RoleEnum> roles;
}
