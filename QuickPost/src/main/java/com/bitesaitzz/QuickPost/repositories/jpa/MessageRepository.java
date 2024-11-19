package com.bitesaitzz.QuickPost.repositories.jpa;

import com.bitesaitzz.QuickPost.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByTag(String tag);

    Page<Message> findByTag(String tag, Pageable pageable);
}
