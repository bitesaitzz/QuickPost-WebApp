package com.bitesaitzz.QuickPost.DTO;

import com.bitesaitzz.QuickPost.models.Person;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;



@Getter
@Setter

public class FileAccessLogDTO {
    private LocalDateTime accessedAt;
    private UUID accessedByID;
    private Person person;


    @Override
    public String toString() {
        return "FileAccessLogDTO{" +
                "accessedAt=" + accessedAt +
                ", accessedByID=" + accessedByID +
                '}';
    }
}
