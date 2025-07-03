package com.traelo.security.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.traelo.security.model.Role;
import com.traelo.security.repository.RoleRepository;

@Configuration
public class DataLoader {
	@Autowired
	private RoleRepository roleRepository;

	@Bean
	CommandLineRunner initData() {
		return args -> {
			loadRolesByDefault();
		};
	}

	private void loadRolesByDefault() {
		if (!roleRepository.existsByRoleName("ROLE_CUSTOMER")) {
			Role userRole = new Role();
			userRole.setRoleName("ROLE_CUSTOMER");
			roleRepository.save(userRole);
		}

		if (!roleRepository.existsByRoleName("ROLE_BUSINESS")) {
			Role userRole = new Role();
			userRole.setRoleName("ROLE_BUSINESS");
			roleRepository.save(userRole);
		}

		if (!roleRepository.existsByRoleName("ROLE_RIDER")) {
			Role userRole = new Role();
			userRole.setRoleName("ROLE_RIDER");
			roleRepository.save(userRole);
		}

		if (!roleRepository.existsByRoleName("ROLE_ADMIN")) {
			Role adminRole = new Role();
			adminRole.setRoleName("ROLE_ADMIN");
			roleRepository.save(adminRole);
		}
	}
}
