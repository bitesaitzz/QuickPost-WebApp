package com.bitesaitzz.QuickPost.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
//@Data
//@RedisHash("Message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Size(max = 1024, message = "Message length should be maximum 1024 characters")
    private String text;

    @Size(min = 1, max = 16, message = "Tag length should be between 1 and 16 characters")
    @NotEmpty
    private String tag;

    private UUID fileId;

    private String fileName;


    @Transient
    private boolean photo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Person person;


    public boolean getPhoto(){
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        String[] photoExtensions = {"jpg", "jpeg", "png", "gif", "bmp", "tiff"};
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        for (String extension : photoExtensions) {
            if (extension.equals(fileExtension)) {
                return true;
            }
        }
        return false;
    }
}
