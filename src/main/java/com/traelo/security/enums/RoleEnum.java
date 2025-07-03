package com.traelo.security.enums;

public enum RoleEnum {
	ROLE_CUSTOMER("ROLE_CUSTOMER"), ROLE_BUSINESS("ROLE_BUSINESS"), ROLE_RIDER("ROLE_RIDER"), ROLE_ADMIN("ROLE_ADMIN");

	private final String role;

	RoleEnum(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}
