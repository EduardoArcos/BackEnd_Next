package com.prueba.nextia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prueba.nextia.domain.FileData;

public interface IFileRepository extends JpaRepository< FileData, Long > {
    
	FileData findFileDataByName( String name );
	
	@Query(value = "SELECT f.* FROM file_data AS f ", nativeQuery = true)
	List<FileData> getAllFiles();
}
