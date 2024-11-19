package com.bitesaitzz.QuickPost.services;


import com.bitesaitzz.QuickPost.kafka.KafkaProducer;
import com.bitesaitzz.QuickPost.models.MailMessage;
import com.bitesaitzz.QuickPost.models.Person;
import com.bitesaitzz.QuickPost.repositories.jpa.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final PersonRepository personRepository;
    private final KafkaProducer kafkaProducer;
    private final PasswordEncoder passwordEncoder;





    public RegistrationService(PersonRepository personRepository, KafkaProducer kafkaProducer, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.kafkaProducer = kafkaProducer;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        person.setCreatedAt(java.time.LocalDateTime.now());
        person.setLastLogin(java.time.LocalDateTime.now());
        personRepository.save(person);
        String messageToSend = "Welcome to QuickPost, "+person.getName()+"! Your account has been created.";
        MailMessage mailMessage = MailMessage.builder().mail(person.getEmail()).subject("Welcome!").text(messageToSend).build();
        kafkaProducer.sendMessage(mailMessage);
        System.out.println("Person "+person.getName()+" added");
    }
}
