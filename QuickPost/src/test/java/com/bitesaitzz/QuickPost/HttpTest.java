package com.bitesaitzz.QuickPost;

import com.bitesaitzz.QuickPost.DTO.FilesDTO;
import com.bitesaitzz.QuickPost.services.HttpRequestService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpTest {
    @Autowired
    HttpRequestService httpRequestService;

    @Test
    public void testAddFile() throws IOException {
        File file = new File("/Users/bitesaitzzz/Desktop/all_codes/github/QuickPost/data/upload/Plan.png");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "image/png", input);
        ResponseEntity<UUID> response =  httpRequestService.sendPostRequest(multipartFile, "post", UUID.fromString("a1377839-16c2-47aa-b5e2-3fddd817e914"));
        System.out.println(response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }


    @Test
    public void testGetFile() {
        ResponseEntity<ByteArrayResource> response = httpRequestService.getFile(UUID.fromString("a1377839-16c2-47aa-b5e2-3fddd817e914"), UUID.fromString("a1377839-16c2-47aa-b5e2-3fddd817e914"));
        System.out.println(response.getBody());
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void testGetMeta() {
        ResponseEntity<FilesDTO> response = httpRequestService.getMeta(UUID.fromString("a1377839-16c2-47aa-b5e2-3fddd817e914"), UUID.fromString("a1377839-16c2-47aa-b5e2-3fddd817e914"));
        //ResponseEntity<ByteArrayResource> response = httpRequestService.getFile((UUID.fromString("a1377839-16c2-47aa-b5e2-3fddd817e914"), UUID.fromString("a1377839-16c2-47aa-b5e2-3fddd817e914"));
        FilesDTO filesDTO = response.getBody();
        System.out.println(filesDTO);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
}
