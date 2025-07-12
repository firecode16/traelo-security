package com.traelo.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.security.model.Role;
import com.traelo.security.model.User;
import com.traelo.security.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	int response = 0;

	@Transactional(readOnly = true)
	public User searchByUsername(String username) {
		return userRepository.findByUsernameIgnoreCase(username);
	}

	@Transactional(readOnly = true)
	public User searchByUsernameOrEmail(String username, String email) {
		return userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(username, email);
	}

	@Transactional
	public User saveUser(User user) {
		user.setUsername(user.getUsername());
		user.setRoles(user.getRoles());
		user.setPassword(user.getPassword());

		User status = userRepository.save(user);

		for (Role role : user.getRoles()) {
			role.getUsers().add(status);
		}
		return status;
	}

	@Transactional
	public int updateUser(Long userId, User user) {
		User objUser = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		objUser.setFullName(user.getFullName());
		objUser.setEmail(user.getEmail());
		objUser.setPhone(user.getPhone());

		try {
			userRepository.save(objUser);
			response = 1;
		} catch (Exception e) {
			e.getLocalizedMessage();
			response = -1;
		}

		return response;
	}
}
