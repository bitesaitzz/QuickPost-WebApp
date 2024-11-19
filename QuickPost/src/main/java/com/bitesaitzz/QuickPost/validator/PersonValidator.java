package com.bitesaitzz.QuickPost.validator;

import com.bitesaitzz.QuickPost.models.Person;
import com.bitesaitzz.QuickPost.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class PersonValidator implements Validator {



    public final PersonService personService;

    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if(personService.findByName(person.getName()).isPresent()){
            errors.rejectValue("name", "name.exists", "Username already exists");
            return;
        }
        if(personService.findByEmail(person.getEmail()).isPresent()){

            errors.rejectValue("email", "email.exists", "Mail already exists");

        }
    }
}
