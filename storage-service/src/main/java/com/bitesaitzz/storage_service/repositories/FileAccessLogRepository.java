package com.bitesaitzz.storage_service.repositories;

import com.bitesaitzz.storage_service.models.FileAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface FileAccessLogRepository extends JpaRepository<FileAccessLog, UUID> {
}
