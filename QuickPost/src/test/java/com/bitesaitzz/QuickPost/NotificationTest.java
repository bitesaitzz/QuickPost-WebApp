package com.bitesaitzz.QuickPost;


import com.bitesaitzz.QuickPost.kafka.KafkaProducer;
import com.bitesaitzz.QuickPost.models.MailMessage;
import com.bitesaitzz.QuickPost.services.MessageService;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@NoArgsConstructor(force = true)
public class NotificationTest {
    @Autowired
    private final MessageService messageService;
    @Autowired
    private final KafkaProducer kafkaProducer;

    @Test
    public void testMailMessage() {
        MailMessage mailMessage = MailMessage.builder().mail("shidloalex@gmail.com").subject("test").text("test").build();
        kafkaProducer.sendMessage(mailMessage);
    }
}
