package com.prueba.nextia.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.prueba.nextia.domain.User;
import com.prueba.nextia.exception.ExistException;
import com.prueba.nextia.exception.RequestRelatedException;

@Component
public interface IUserService {

	User findUserByUsername( String username );

	User addNewUser( String email, String username, String password ) throws UsernameNotFoundException, ExistException;
	
    User updateUser( String username, String email ) throws UsernameNotFoundException, ExistException;

    List<User> getUsers();
        
    void deleteUser(String username) throws IOException, RequestRelatedException;
    
}
