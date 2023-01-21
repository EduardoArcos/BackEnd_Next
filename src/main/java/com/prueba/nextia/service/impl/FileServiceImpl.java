package com.prueba.nextia.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.prueba.nextia.domain.FileData;
import com.prueba.nextia.exception.ExistException;
import com.prueba.nextia.repository.IFileRepository;
import com.prueba.nextia.service.IFIleService;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class FileServiceImpl implements IFIleService {

    
    @Autowired
    IFileRepository fileRepository;
    
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

    public FileServiceImpl() {}

    @Override
    public FileData addNewFile(String name, MultipartFile file) throws ExistException, IOException {

        validateName("", name);

        // Se crea la carpeta que almacenara el archivo
		Path folder = Paths.get(getFolder());
		if (!Files.exists(folder)) {
			Files.createDirectories(folder);
		}
        
        // Files
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path fileStorage = get(getFolder(), filename).toAbsolutePath().normalize();

        try {
            copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
        } catch (Exception e) {
            throw new IOException("Error al intentar crear el archivo: " + e);
        }

        FileData newFile = new FileData();

        newFile.setName(name);
        newFile.setUrl(fileStorage.toString());

        fileRepository.save(newFile);

        LOGGER.info("La ruta del archivo guardado es: " + fileStorage);

        return newFile;
    }

    @Override
    public FileData addNewMultiFile(List<String> names, List<MultipartFile> files) throws ExistException, IOException {

        // Validamos que todos los nombres pasados no existan
        for (final String _name : names) {

            validateName("", _name);
        }

        //Se crea la carpeta que almacenara el archivo
		Path folder = Paths.get(getFolder());
		if (!Files.exists(folder)) {
			Files.createDirectories(folder);
		}

        // Hacemos un for para poder crear los archivos y guardarlos en la BD como corresponde.
        for ( int i = 0; i < files.size(); i++) {

            String nombre = names.get(i).replaceAll("[^\\w+]","");

            // Proceso de creacion del archivo
            String filename = nombre + "-" + StringUtils.cleanPath(files.get(i).getOriginalFilename());
            Path fileStorage = get(getFolder(), filename).toAbsolutePath().normalize();

            try {
                copy(files.get(i).getInputStream(), fileStorage, REPLACE_EXISTING);
            } catch (Exception e) {
                throw new IOException("Error al intentar crear el archivo: " + e);
            }

            FileData newFile = new FileData();


            newFile.setName(nombre);
            newFile.setUrl(fileStorage.toString());

            fileRepository.save(newFile);
        }
        FileData newFile = new FileData();

        return newFile;
    }

    @Override
    public FileData updateFileName(String oldName, String newName) throws ExistException {
        
        FileData currentFile = validateName(oldName, newName);

        currentFile.setName(newName);

        fileRepository.save(currentFile);

        return currentFile;
    }

    @Override
    public List<FileData> getAllFiles() {
        
        return fileRepository.getAllFiles();
    }

    @Override
    public void deleteFile(String name) throws FileNotFoundException, IOException {

        FileData fileData = fileRepository.findFileDataByName(name);

		// Posteriormente creamos el path para recuperar el filename
		Path filePath = get(fileData.getUrl()).toAbsolutePath().normalize();
		System.out.println("Eliminando arhivo " + filePath);
        
		// El archivo no existe en el servidor
		if (!Files.exists(filePath)) {
			throw new FileNotFoundException("El archivo con el nombre: " + fileData.getName() + " no existe en el servidor.");
		}		

		// Nos elimina el archivo si este existe en el directorio
		Files.delete(filePath);

        fileRepository.deleteById(fileData.getId()); 

        LOGGER.info("El archivo con el nombre: " + name + " se ha eliminado exitosamente.");

    }

	public FileData findFileByName(String name) { 
		return fileRepository.findFileDataByName(name);
	}
    
    //Método para obtener la ruta de la carpeta
    public static String getFolder() {
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String stringDate= DateFor.format(date);
        String newFolder = stringDate.replace("/", "-");
        String DIRECTORY = System.getProperty("user.home") +  "/files_pv/" +  newFolder + "/";
        return DIRECTORY;
    }

    private FileData validateName(String currentUsername, String newUsername) throws ExistException {

        FileData userByNewUsername = findFileByName(newUsername);
          
        if(currentUsername.length() > 0) { 
            FileData currentUser = findFileByName(currentUsername);
            if(currentUser == null) {
                throw new ExistException("No se ha encontrado el archivo con el nombre: " + currentUsername);
            }
            
            if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new ExistException("El nombre que ya escribio ya existe.");
            }
            
            return currentUser;
        } else {
            
            if(userByNewUsername != null) {
                throw new ExistException("El nombre que ya escribio ya existe.");
            }
            
            return userByNewUsername;
        }  
    }

}
