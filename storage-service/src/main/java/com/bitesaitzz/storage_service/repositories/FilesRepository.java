package com.bitesaitzz.storage_service.repositories;

import com.bitesaitzz.storage_service.models.Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface FilesRepository extends JpaRepository<Files, UUID> {

}
