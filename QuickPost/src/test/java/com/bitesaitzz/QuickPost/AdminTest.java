package com.bitesaitzz.QuickPost;

import com.bitesaitzz.QuickPost.models.Person;
import com.bitesaitzz.QuickPost.services.PersonService;
import com.bitesaitzz.QuickPost.services.RegistrationService;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@NoArgsConstructor(force = true)
public class AdminTest {
    @Autowired
    PersonService personService;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    private final MockMvc mockMvc;


    @Test
    public void deletePerson() {
        Person person = new Person();
        person.setName("test");
        person.setPassword("test");
        person.setEmail("test@gmail.com");
        registrationService.register(person);
        Optional<Person> registeredPerson = personService.findByName("test");
        assertTrue(registeredPerson.isPresent());
        personService.deletePerson(registeredPerson.get().getId());
        Optional<Person> deletedPerson = personService.findByName("test");
        assertFalse(deletedPerson.isPresent());
    }

    @Test
    public void authorizePerson() throws Exception {
        mockMvc.perform(formLogin().loginProcessingUrl("/auth/login").user("test").password("test"))
                .andDo(print());
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());

}

    @Test
    public void authorizePerson2() throws Exception {
        Person person = personService.findByName("test").get();
        personService.updateRole(person.getId(), "ROLE_ADMIN");
        mockMvc.perform(formLogin().loginProcessingUrl("/auth/login").user("test").password("test"));
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
        personService.updateRole(person.getId(), "ROLE_USER");

    }


}
