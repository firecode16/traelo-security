package com.traelo.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.traelo.security.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);

	User findByUsernameIgnoreCase(String username);

	User findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

	Boolean existsByUserId(Long userId);

	Optional<User> findByUserId(Long userId);

	@Query("SELECT u.backdropProfile FROM User u WHERE u.userId = :userId")
	byte[] findBackdropProfileByUserId(@Param("userId") Long userId);

	@Query("SELECT u.avatarProfile FROM User u WHERE u.userId = :userId")
	byte[] findAvatarProfileByUserId(@Param("userId") Long userId);
}
