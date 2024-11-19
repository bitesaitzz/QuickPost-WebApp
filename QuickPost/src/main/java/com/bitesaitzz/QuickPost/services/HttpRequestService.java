package com.bitesaitzz.QuickPost.services;

import com.bitesaitzz.QuickPost.DTO.FilesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class HttpRequestService {
    @Value("${service.storage.url}")
    String storageUrl;

    private final RestTemplate restTemplate;
    @Autowired
    public HttpRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<UUID> sendPostRequest(MultipartFile file, String tag, UUID uuid) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Формируем тело запроса
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("tag", tag);
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename(); // Возвращаем оригинальное имя файла
            }
        });
        body.add("uuid", uuid.toString());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        System.out.println("Sending POST request to: " + storageUrl + "/upload");
        ResponseEntity<UUID> response = restTemplate.exchange(storageUrl + "/upload", HttpMethod.POST, requestEntity, UUID.class);

        return response;

    }
    public ResponseEntity<ByteArrayResource> getFile(UUID fileId, UUID userId) {
        String url = storageUrl + "/getFile?id=" + fileId + "&uuid=" + userId; // Формируем URL для GET-запроса
        ResponseEntity<ByteArrayResource> response = restTemplate.getForEntity(url, ByteArrayResource.class);
        return response;
    }

    public ResponseEntity<FilesDTO> getMeta(UUID fileId, UUID userId) { // Метод для получения метаинформации о файле
        String url = storageUrl + "/getMeta/"+ fileId + "?userid=" + userId; // Формируем URL для GET-запроса
        ResponseEntity<FilesDTO> response = restTemplate.getForEntity(url, FilesDTO.class);
        return response;
    }



}
