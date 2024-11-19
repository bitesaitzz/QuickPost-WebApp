package com.bitesaitzz.QuickPost.services;

import com.bitesaitzz.QuickPost.models.Person;
import com.bitesaitzz.QuickPost.repositories.jpa.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonDetailsService personDetailsService;

    public PersonService(PersonRepository personRepository, PersonDetailsService personDetailsService) {
        this.personRepository = personRepository;
        this.personDetailsService = personDetailsService;
    }

    @Transactional
    //@Cacheable(value = "person", key = "#id")
    public Optional<Person> findById(UUID id) {
        return personRepository.findById(id);
    }
    @Transactional
    public Optional<Person> findByName(String name) {
        return personRepository.findByName(name);
    }
    @Transactional
    public List<Person> getAllUsers(){
        return personRepository.findAll();
    }

    public Optional<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }


    @Transactional
    public void deletePerson(UUID userId) {
        personRepository.deleteById(userId);
    }

    @Transactional
    public void updateRole(UUID userId, String role) {
        Optional<Person> person = personRepository.findById(userId);
        if(person.isEmpty()){
            System.out.println("Error: person not found");
            return;
        }
        person.get().setRole(role);
        personRepository.save(person.get());
        UserDetails updatedUserDetails = personDetailsService.loadUserByUsername(person.get().getName());
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                updatedUserDetails, updatedUserDetails.getPassword(), updatedUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Transactional
    public void updateLastLogin(UUID userId){
        Optional<Person> person = personRepository.findById(userId);
        if(person.isEmpty()){
            System.out.println("Error: person not found");
            return;
        }
        person.get().setLastLogin(java.time.LocalDateTime.now());
        personRepository.save(person.get());
    }
}
