package com.bitesaitzz.QuickPost.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter

public class FilesDTO {
    private String fileName;
    private long fileSize;
    private String fileType;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    private String tag;
    private List<FileAccessLogDTO> fileAccessLog;

    @Override
    public String toString() {
        return "FilesDTO{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", fileType='" + fileType + '\'' +
                ", createdAt=" + createdAt +
                ", tag='" + tag + '\'' +
                ", fileAccessLog=" + fileAccessLog +
                '}';
    }
}
