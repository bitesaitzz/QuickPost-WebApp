package com.bitesaitzz.storage_service.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "file_access_log")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class FileAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @ManyToOne()
    @JoinColumn(name = "file_id", referencedColumnName = "id", updatable = false)
    @JsonBackReference
    private Files fileId;

    @Column(name = "accessed_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime accessedAt;

    @Column(name = "accessed_by_id")
    private UUID accessedByID;



}
