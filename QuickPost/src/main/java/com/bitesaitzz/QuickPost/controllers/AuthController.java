package com.bitesaitzz.QuickPost.controllers;


import com.bitesaitzz.QuickPost.models.Person;
import com.bitesaitzz.QuickPost.services.RegistrationService;
import com.bitesaitzz.QuickPost.validator.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final PersonValidator personValidator;


    public AuthController(RegistrationService registrationService, PersonValidator personValidator) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person){
        return "auth/registration";
    }
    @PostMapping("/registration")
    public String doRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            return "auth/registration";
        }
        registrationService.register(person);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout(){
        return "auth/logout";
    }

}
