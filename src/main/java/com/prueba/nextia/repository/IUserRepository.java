package com.prueba.nextia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prueba.nextia.domain.User;

public interface IUserRepository extends JpaRepository< User, Long > {
    
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	User findUserByUsername( String username );

	@Query(value = "SELECT u.* FROM users AS u ", nativeQuery = true)
	List<User> getUsers();
}
