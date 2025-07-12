package com.traelo.security.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.traelo.security.config.JwtConfig;
import com.traelo.security.enums.RoleEnum;
import com.traelo.security.model.RegisterUser;
import com.traelo.security.model.Role;
import com.traelo.security.model.User;
import com.traelo.security.repository.RoleRepository;
import com.traelo.security.response.AuthRequest;
import com.traelo.security.response.AuthResponse;
import com.traelo.security.response.RefreshTokenRequest;
import com.traelo.security.response.RefreshTokenResponse;
import com.traelo.security.service.UserDetailService;
import com.traelo.security.service.UserService;

@RestController
@CrossOrigin("*")
public class AuthenticationController {
	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/auth/login")
	public ResponseEntity<?> sessionLogin(@RequestBody AuthRequest authRequest) {
		UserDetails userDetails = userDetailService.loadUserByUsername(authRequest.getUsername());

		// check if user exists and password is OK
		if (userDetails != null && passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
			String jwt = getToken(userDetails.getUsername());
			return ResponseEntity.ok(new AuthResponse(jwt));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password is bad");
		}
	}

	@PostMapping("/auth/signup")
	public ResponseEntity<?> register(@RequestBody RegisterUser registerUser) {
		if (userService.searchByUsernameOrEmail(registerUser.getUsername(), registerUser.getEmail()) != null) {
			return ResponseEntity.badRequest().body("Username or email is currently in Use");
		}

		User user = new User();
		user.setUserId(registerUser.getUserId());
		user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
		user.setUsername(registerUser.getUsername());
		user.setEmail(registerUser.getEmail());
		user.setFullName(registerUser.getFullName());
		user.setPhone(registerUser.getPhone());
		user.setCreatedAt(registerUser.getCreatedAt());

		Set<Role> roles = new HashSet<>();

		if (registerUser.getRoles() != null) {
			for (RoleEnum roleEnum : registerUser.getRoles()) {
				Role roleObj = roleRepository.findByRoleName(roleEnum.name());
				if (roleObj != null) {
					roles.add(roleObj);
				}
			}
			user.setRoles(roles);
		}

		// if not assigned valid roles, so assigned default role to user
		if (roles.isEmpty()) {
			Role userRole = roleRepository.findByRoleName(RoleEnum.ROLE_CUSTOMER.getRole());
			roles.add(userRole);
			user.setRoles(roles);
		}

		userService.saveUser(user);
		String jwt = getToken(user.getUsername());

		return ResponseEntity.ok(new AuthResponse(jwt));
	}

	@PostMapping("/auth/refreshToken")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
		String token = refreshToken.getToken();
		String username = jwtConfig.extractUsername(token);
		String refreshNewToken = getToken(username);

		return ResponseEntity.ok(new RefreshTokenResponse(refreshNewToken));
	}

	@GetMapping("/auth/userInfo")
	private ResponseEntity<?> getUserInfo(Authentication authentication) {
		String username = authentication.getName();
		User user = userService.searchByUsername(username);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("username", user.getUsername());
		userInfo.put("email", user.getEmail());
		userInfo.put("fullName", user.getFullName());
		userInfo.put("phone", user.getPhone());
		userInfo.put("roles", user.getRoles().stream().map(Role::getRoleName).toList());

		return ResponseEntity.ok(userInfo);
	}

	@PutMapping("/auth/update/{userId}")
	private ResponseEntity<?> update(@PathVariable Long userId, @RequestBody User user) {
		int response = userService.updateUser(userId, user);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	private String getToken(String username) {
		return jwtConfig.generateToken(username);
	}
}
