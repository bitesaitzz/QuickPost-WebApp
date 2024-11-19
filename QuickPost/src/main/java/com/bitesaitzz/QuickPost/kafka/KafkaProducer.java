package com.bitesaitzz.QuickPost.kafka;

import com.bitesaitzz.QuickPost.models.MailMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String > kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }


    public void sendMessage(MailMessage mailMessage) {
        try {
            kafkaTemplate.send("mail", objectMapper.writeValueAsString(mailMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();

    }
    }
    }
