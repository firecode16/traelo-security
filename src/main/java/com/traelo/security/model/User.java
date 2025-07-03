package com.traelo.security.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long userId;

	private String password;
	private String username;
	private String email;
	private String fullName;
	private String phone;
	@Lob
	@Column(name = "avatarProfile", columnDefinition = "LONGBLOB")
	private byte[] avatarProfile;
	@Lob
	@Column(name = "backdropProfile", columnDefinition = "LONGBLOB")
	private byte[] backdropProfile;

	@Temporal(TemporalType.TIMESTAMP)
	private String createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	private String updatedAt;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@Enumerated(EnumType.STRING)
	private Set<Role> roles = new HashSet<>();

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 47 * hash + Objects.hashCode(this.id);
		hash = 47 * hash + Objects.hashCode(this.password);
		hash = 47 * hash + Objects.hashCode(this.username);
		hash = 47 * hash + Objects.hashCode(this.email);
		hash = 47 * hash + Objects.hashCode(this.fullName);
		hash = 47 * hash + Objects.hashCode(this.phone);
		hash = 47 * hash + Objects.hashCode(this.avatarProfile);
		hash = 47 * hash + Objects.hashCode(this.backdropProfile);
		hash = 47 * hash + Objects.hashCode(this.createdAt);
		hash = 47 * hash + Objects.hashCode(this.updatedAt);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;

		if (!Objects.equals(this.password, other.password)) {
			return false;
		}
		if (!Objects.equals(this.username, other.username)) {
			return false;
		}
		if (!Objects.equals(this.email, other.email)) {
			return false;
		}
		if (!Objects.equals(this.fullName, other.fullName)) {
			return false;
		}
		if (!Objects.equals(this.phone, other.phone)) {
			return false;
		}
		if (!Objects.equals(this.avatarProfile, other.avatarProfile)) {
			return false;
		}
		if (!Objects.equals(this.backdropProfile, other.backdropProfile)) {
			return false;
		}
		if (!Objects.equals(this.createdAt, other.createdAt)) {
			return false;
		}
		if (!Objects.equals(this.updatedAt, other.updatedAt)) {
			return false;
		}
		return Objects.equals(this.id, other.id);
	}

}
