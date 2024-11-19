package com.bitesaitzz.QuickPost.services;


import com.bitesaitzz.QuickPost.models.Person;
import com.bitesaitzz.QuickPost.repositories.jpa.PersonRepository;
import com.bitesaitzz.QuickPost.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    @Autowired
    private PersonRepository personRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByName(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new PersonDetails(person.get());
    }
}
