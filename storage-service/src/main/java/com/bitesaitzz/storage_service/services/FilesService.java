package com.bitesaitzz.storage_service.services;


import com.bitesaitzz.storage_service.DTO.FileAccessLogDTO;
import com.bitesaitzz.storage_service.DTO.FilesDTO;
import com.bitesaitzz.storage_service.models.Files;
import com.bitesaitzz.storage_service.models.FileAccessLog;
import com.bitesaitzz.storage_service.repositories.FileAccessLogRepository;
import com.bitesaitzz.storage_service.repositories.FilesRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FilesService {

    private final FilesRepository filesRepository;

    private final ModelMapper modelMapper;
    private final FileAccessLogRepository fileAccessLogRepository;

    @Value("${upload.dir}")
    private String uploadPath;

    public FilesService(FilesRepository filesRepository, ModelMapper modelMapper, FileAccessLogRepository fileAccessLogRepository) {
        this.filesRepository = filesRepository;
        this.modelMapper = modelMapper;
        this.fileAccessLogRepository = fileAccessLogRepository;
    }

    public void deleteFileById(UUID id){
        filesRepository.deleteById(id);
    }


    @Transactional
    public UUID addFile(MultipartFile mf, String tag) throws IOException {

        Files file = enrichFile(mf);
        filesRepository.save(file);
        if(tag.equals("profile")){
            file.setTag("profile");
            file.setFilePath(System.getProperty("user.dir") + uploadPath + "/profile/" + file.getId() + file.getFileName());
        }
        else{
            file.setTag("posts");
            file.setFilePath(System.getProperty("user.dir") + uploadPath + "/posts/" + file.getId() + file.getFileName());
        }


        File uploadDir = new File(System.getProperty("user.dir") + uploadPath);

        if(!uploadDir.exists()){
            uploadDir.mkdir();
        }

        File destinationFile = new File(file.getFilePath());
        if (destinationFile.exists()) {
            throw new IOException("File already exists: " + destinationFile.getPath());
        }

        mf.transferTo(destinationFile);
        filesRepository.save(file);
        return file.getId();
    }


    @Transactional
    public Files getFileById(UUID id){
        Files file = filesRepository.findById(id).orElse(null);
        if (file != null) {
            Hibernate.initialize(file.getFileAccessLog());
        }
        return file;
    }

    public Files enrichFile(MultipartFile mf){
        Files file = new Files();
        file.setFileName(mf.getOriginalFilename());
        file.setFileSize(mf.getSize());
        file.setCreatedAt(LocalDateTime.now());
        file.setFileType(mf.getContentType());
        return file;
    }
    @Cacheable(value = "filesCache", key = "#id + '-' + #userId")  // Кэширование по ID файла и userId
    public FilesDTO getFileInfoDto(UUID id, UUID userId){
        System.out.println("caching file info");
        com.bitesaitzz.storage_service.models.Files file = getFileById(id);
        if(file != null){
            FilesDTO filesDTO = convertToDTO(file);
            registerAccessLog(file, userId);
            return filesDTO;
        }
        else{
            return null;
        }
    }

    @Transactional
    public void registerAccessLog(Files files, UUID accessedByID){
        FileAccessLog fileAccessLog = new FileAccessLog();
        fileAccessLog.setFileId(files);
        fileAccessLog.setAccessedAt(LocalDateTime.now());
        fileAccessLog.setAccessedByID(accessedByID);
        fileAccessLogRepository.save(fileAccessLog);
    }

    public FilesDTO convertToDTO(com.bitesaitzz.storage_service.models.Files files){

        FilesDTO filesDTO = modelMapper.map(files, FilesDTO.class);
        List<FileAccessLogDTO> fileAccessLogDTOs = files.getFileAccessLog().stream()
                .map(log -> modelMapper.map(log, FileAccessLogDTO.class))
                .collect(Collectors.toList());
        filesDTO.setFileAccessLog(fileAccessLogDTOs);
        return filesDTO;
    }
}
