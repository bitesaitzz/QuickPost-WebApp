package com.bitesaitzz.QuickPost.services;


import com.bitesaitzz.QuickPost.models.Message;
import com.bitesaitzz.QuickPost.models.Person;
import com.bitesaitzz.QuickPost.repositories.jpa.MessageRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public List<Message> getAllMessages(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messagePage = messageRepository.findAll(pageable);
        if (page > messagePage.getTotalPages()) {
            pageable = PageRequest.of(messagePage.getTotalPages() - 1, size);
            messagePage = messageRepository.findAll(pageable);
        }
        return messagePage.getContent();
    }

    public void addMessage(Message message, Person person){
        message.setPerson(person);
        person.getMessages().add(message);
        messageRepository.save(message);

    }


    //@Cacheable(value = "message", key = "#id")
    public Message findById(Long id){
        System.out.println("Querying ID: " + id);
        return messageRepository.findById(id).orElse(null);
    }

    public List<Message> findByTag(String tag){
        return messageRepository.findByTag(tag);
    }
    public List<Message> findByTag(String tag, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messagePage = messageRepository.findByTag(tag, pageable);
        return messagePage.getContent();
    }

    public int getTotalPages(int size) {
        long count = messageRepository.count();
        return (int) Math.ceil((double) count / size);
    }
}
