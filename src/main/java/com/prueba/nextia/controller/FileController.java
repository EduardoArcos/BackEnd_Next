package com.prueba.nextia.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.nextia.domain.FileData;
import com.prueba.nextia.domain.HttpResponse;
import com.prueba.nextia.exception.ExistException;
import com.prueba.nextia.exception.RequestRelatedException;
import com.prueba.nextia.service.IFIleService;

@RestController
@RequestMapping(path = { "/file" })
@CrossOrigin(origins = "*")
public class FileController {
    
    @Autowired
    private IFIleService fileService;

    public FileController() {}

    // * http://localhost:8080/file/add
    @PostMapping("/add")
    public ResponseEntity<FileData> addFile(@Valid 
        @RequestParam("name") String name,
        @RequestParam("file") MultipartFile file
        ) throws ExistException, IOException {

        FileData newUser = fileService.addNewFile( name, file );
        
        return new ResponseEntity<FileData>(newUser, HttpStatus.OK);
    }

    // * http://localhost:8080/file/update
    @PutMapping("/update")
    public ResponseEntity<FileData> updateFile(
        @RequestParam("oldName") String oldName,
        @RequestParam("newName") String newName
    ) throws ExistException {

        FileData updatedFileName = fileService.updateFileName( oldName, newName );

        return new ResponseEntity<FileData>(updatedFileName, HttpStatus.OK);
    }

	// * http://localhost:8080/file/list
	@GetMapping("/list")
	public ResponseEntity<List<FileData>> getAllUsers() {
		
        List<FileData> files = fileService.getAllFiles();
		
        return new ResponseEntity<>(files, HttpStatus.OK);
	}

    // * Eliminado el archivo (Muy drastico)
	// * http://localhost:8080/file/delete/rodrigo.gomez
	@DeleteMapping("/delete/{name}")
	public ResponseEntity<HttpResponse> deleteUser(
        @PathVariable("name") String name
    ) throws RequestRelatedException, FileNotFoundException, IOException {
        String USER_DELETED_SUCCESSFULLY = "El Archivo se ha eliminado exitosamente.";

		fileService.deleteFile(name);

		return response(HttpStatus.OK, USER_DELETED_SUCCESSFULLY);
	}

    // * Metodo para respuestas genericas
	private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
		return new ResponseEntity<>(
            new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message),
            httpStatus);
	}
}
