package com.bitesaitzz.notifications_service.kafka;


import com.bitesaitzz.notifications_service.Service.MailSender;
import com.bitesaitzz.notifications_service.models.MailMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    private final MailSender mailSender;

    private final ObjectMapper objectMapper;

    public KafkaConsumer(MailSender mailSender, ObjectMapper objectMapper) {
        this.mailSender = mailSender;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "mail", groupId = "1", containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message) {
        log.info("Received Messasge in group mailToSend: " + message);
        try {
            MailMessage mailMessage = objectMapper.readValue(message, MailMessage.class);
            mailSender.sendEmail(mailMessage.getMail(), mailMessage.getSubject(), mailMessage.getText());
        } catch (Exception e) {
            log.error("Error while sending mail: " + e.getMessage());
        }
    }


}
