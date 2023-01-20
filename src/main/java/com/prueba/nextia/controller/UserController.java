package com.prueba.nextia.controller;

import org.springframework.web.bind.annotation.RestController;

import com.prueba.nextia.domain.HttpResponse;
import com.prueba.nextia.domain.User;
import com.prueba.nextia.exception.ExistException;
import com.prueba.nextia.exception.RequestRelatedException;
import com.prueba.nextia.service.IUserService;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(path = { "/user" })
@CrossOrigin(origins = "*")
public class UserController {
    
    private IUserService userService;

    public UserController( 
        IUserService userService
    ) {
        this.userService = userService;
    }

	// * http://localhost:8080/user/add
    @PostMapping("/add")
	public ResponseEntity<User> addUser(@Valid 
        @RequestParam("email") String email,
        @RequestParam("username") String username,
        @RequestParam("password") String password
        ) throws UsernameNotFoundException, ExistException {

		User newUser = userService.addNewUser( email, username, password );
		
        return new ResponseEntity<User>(newUser, HttpStatus.OK);
	}
    
    
	// * http://localhost:8080/user/update
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(
        @RequestParam("username") String username,
        @RequestParam("email") String email
    ) throws UsernameNotFoundException, ExistException {

        User updatedUser = userService.updateUser( username, email );

        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
    }

	// * http://localhost:8080/user/list
	@GetMapping("/list")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

    // * Eliminado de usuario (Muy drastico)
	// * http://localhost:8080/user/delete/rodrigo.gomez
	@DeleteMapping("/delete/{username}")
	public ResponseEntity<HttpResponse> deleteUser(
        @PathVariable("username") String username
    ) throws RequestRelatedException {
        String USER_DELETED_SUCCESSFULLY = "Usuario se ha eliminado exitosamente.";

		userService.deleteUser(username);

		return response(HttpStatus.OK, USER_DELETED_SUCCESSFULLY);
	}

    // * Metodo para respuestas genericas
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		return new ResponseEntity<>(
            new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message),
            httpStatus);
	}

}
