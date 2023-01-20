package com.prueba.nextia.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.prueba.nextia.domain.ERole;
import com.prueba.nextia.domain.Role;
import com.prueba.nextia.domain.User;
import com.prueba.nextia.exception.ExistException;
import com.prueba.nextia.exception.RequestRelatedException;
import com.prueba.nextia.repository.IRoleRepository;
import com.prueba.nextia.repository.IUserRepository;
import com.prueba.nextia.service.IUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements IUserService {
		
    String EMAIL_ALREADY_EXISTS = "El username escrito ya se encuentra registrado en el sistema.";
    String NO_USER_FOUND_BY_USERNAME = "No se ha encontrado el usuario.";

    @Autowired
    IRoleRepository roleRepository;

	private IUserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	public UserServiceImpl( 
        IUserRepository userRepository,
        BCryptPasswordEncoder passwordEncoder 
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User addNewUser(String email, String username, String password )
        throws UsernameNotFoundException, ExistException {

        validateUsername(StringUtils.EMPTY, username);

        User user = new User();

        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(encodePassword(password)); // Se guardar el password encriptado

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Error: El Rol no fue encontrado."));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        LOGGER.info("New user password: " + password);

        return user;
    }

    @Override
    public User updateUser(String username, String email )
        throws UsernameNotFoundException, ExistException {

        User currentUser = validateUsername(username, StringUtils.EMPTY);

        currentUser.setEmail(email);

        userRepository.save(currentUser);

        return currentUser;
    }

	@Override
	public List<User> getUsers() {
		return userRepository.getUsers();
	}

    @Override
    public void deleteUser(String username) throws RequestRelatedException {

        User user = userRepository.findUserByUsername(username);

        userRepository.deleteById(user.getId()); 

        LOGGER.info("El usuario con el username: " + username + " se ha eliminado exitosamente.");
    }

    @Override
	public User findUserByUsername(String username) { 
		return userRepository.findUserByUsername(username);
	}

    private User validateUsername(String currentUsername, String newUsername) throws UsernameNotFoundException {

        User userByNewUsername = findUserByUsername(newUsername);
          
        if(StringUtils.isNotBlank(currentUsername)) { 
            User currentUser = findUserByUsername(currentUsername);
            if(currentUser == null) {
                throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UsernameNotFoundException(EMAIL_ALREADY_EXISTS);
            }
            
            return currentUser;
        } else {
            
            if(userByNewUsername != null) {
                throw new UsernameNotFoundException(EMAIL_ALREADY_EXISTS);
            }
            
            return userByNewUsername;
        }  
    }

    // Metodo para encriptar el password
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


}
