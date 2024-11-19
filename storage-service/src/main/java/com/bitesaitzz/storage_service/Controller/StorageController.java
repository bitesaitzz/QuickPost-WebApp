package com.bitesaitzz.storage_service.Controller;

import java.nio.file.Files;
import org.springframework.cache.annotation.Cacheable;
import com.bitesaitzz.storage_service.DTO.FileAccessLogDTO;
import com.bitesaitzz.storage_service.DTO.FilesDTO;

import com.bitesaitzz.storage_service.services.FilesService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/storage")
@EnableCaching
public class StorageController {

    FilesService filesService;



    public StorageController(FilesService filesService, ModelMapper modelMapper) {
        this.filesService = filesService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UUID> uploadFile(@RequestParam("tag") String tag, @RequestParam("file") MultipartFile file, @RequestParam("uuid") UUID uuid) throws IOException {
        UUID id = filesService.addFile(file, tag);
        com.bitesaitzz.storage_service.models.Files optionalFile = filesService.getFileById(id);
        filesService.registerAccessLog(optionalFile, uuid);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("id") UUID id){
        filesService.deleteFileById(id);
        return ResponseEntity.ok("File deleted");
    }

    @GetMapping("/getFile")
    public ResponseEntity<ByteArrayResource> getFile2(@RequestParam("id") UUID id) {
        com.bitesaitzz.storage_service.models.Files file = filesService.getFileById(id);
        if (file!=null) {
            Path filePath = Path.of(file.getFilePath());
            try {
                byte[] fileData = Files.readAllBytes(filePath);
                ByteArrayResource resource = new ByteArrayResource(fileData);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, file.getFileType())
                        .body(resource);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//    @GetMapping(value = "getMeta/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
//    @Cacheable(value = "filesCache", key = "#id + '-' + #userId")  // Кэширование по ID файла и userId
//    public ResponseEntity<FilesDTO> getFileInfo(@PathVariable UUID id, @RequestParam(value = "userid", required = false) String userId){
//        UUID userUuid;
//        try {
//            userUuid = UUID.fromString(userId);
//        } catch (IllegalArgumentException | NullPointerException e) {
//            return ResponseEntity.badRequest().build(); // Неверный формат UUID или null
//        }
//        com.bitesaitzz.storage_service.models.Files file = filesService.getFileById(id);
//        if(file != null){
//            FilesDTO filesDTO = convertToDTO(file);
//            filesService.registerAccessLog(file, userUuid);
//            return ResponseEntity.ok(filesDTO);
//        }
//        else{
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

    @GetMapping(value = "getMeta/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<FilesDTO> getFileInfo(@PathVariable UUID id, @RequestParam(value = "userid", required = false) String userId){
        UUID userUuid;
        try {
            userUuid = UUID.fromString(userId);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().build(); // Неверный формат UUID или null
        }
        FilesDTO filesDTO = filesService.getFileInfoDto(id, userUuid);
        if(filesDTO != null){

            return ResponseEntity.ok(filesDTO);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }






    @GetMapping("get/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable UUID id, @RequestParam(value = "userid", required = false) UUID userId) {
       com.bitesaitzz.storage_service.models.Files file = filesService.getFileById(id);
        if (file != null) {
            Path filePath = Path.of(file.getFilePath());
            try {
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(filePath));
                if(userId != null)
                    filesService.registerAccessLog(file, userId);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFileName() + "\"")
                        .header(HttpHeaders.CONTENT_TYPE, file.getFileType())
                        .body(resource);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }






}
