package com.prueba.nextia.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.nextia.domain.FileData;
import com.prueba.nextia.exception.ExistException;

@Component
public interface IFIleService {
    
    FileData addNewFile( String name, MultipartFile file) throws ExistException, IOException;

    FileData updateFileName ( String oldName, String newName) throws ExistException;

    List<FileData> getAllFiles();

    void deleteFile(String name) throws FileNotFoundException, IOException;

}
