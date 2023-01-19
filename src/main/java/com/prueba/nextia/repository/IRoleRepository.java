package com.prueba.nextia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prueba.nextia.domain.ERole;
import com.prueba.nextia.domain.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}