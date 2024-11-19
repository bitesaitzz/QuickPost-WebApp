package com.bitesaitzz.QuickPost.controllers;


import com.bitesaitzz.QuickPost.DTO.FileAccessLogDTO;
import com.bitesaitzz.QuickPost.DTO.FilesDTO;
import com.bitesaitzz.QuickPost.models.Message;
import com.bitesaitzz.QuickPost.models.Person;
import com.bitesaitzz.QuickPost.services.HttpRequestService;
import com.bitesaitzz.QuickPost.services.PersonDetailsService;
import com.bitesaitzz.QuickPost.services.PersonService;
import com.bitesaitzz.QuickPost.validator.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.bitesaitzz.QuickPost.services.MessageService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class MessagesController {

    @Value("${upload.path}")
    private String uploadPath;
    private final MessageService messageService;


    private final HttpRequestService httpRequestService;
    private final PersonValidator personValidator;

    private final PersonService personService;

    private final int MESS_PER_PAGE = 5;


    @Value("${service.storage.url}")
    private String storageUrl;

    public MessagesController(MessageService messageService, HttpRequestService httpRequestService, PersonValidator personValidator, PersonService personService) {
        this.messageService = messageService;
        this.httpRequestService = httpRequestService;
        this.personValidator = personValidator;
        this.personService = personService;
    }



    @GetMapping("/messages")
    public String messages(@ModelAttribute("message") Message message, Model model, @RequestParam(required = false) String tag, @RequestParam(required = false) Integer page, @AuthenticationPrincipal UserDetails userDetails){
        Person person = personService.findByName(userDetails.getUsername()).get();
        personService.updateLastLogin(person.getId());
        if(page == null)
            page = 0;
        if(tag != null && !tag.isEmpty())
            model.addAttribute("messages", messageService.findByTag(tag, page, MESS_PER_PAGE));
        else
            model.addAttribute("messages", messageService.getAllMessages(page, MESS_PER_PAGE));
        model.addAttribute("person", person);
        model.addAttribute("storageUrl", storageUrl);
        model.addAttribute("message", new Message());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", messageService.getTotalPages(MESS_PER_PAGE));
        model.addAttribute("tag", "");
        return "messages/all_messages";
    }
    @GetMapping("/{id}/message")
    public String message(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserDetails userDetails){
        Person person = personService.findByName(userDetails.getUsername()).get();
        personService.updateLastLogin(person.getId());
        UUID fileId = messageService.findById(id).getFileId();
        if(fileId != null){
            FilesDTO filesDTO = httpRequestService.getMeta(fileId, person.getId()).getBody();
            List<FileAccessLogDTO> fileAccessLogDTOS = filesDTO.getFileAccessLog();
            fileAccessLogDTOS.forEach(fileAccessLogDTO -> {
                Person messagePerson = personService.findById(fileAccessLogDTO.getAccessedByID()).orElse(null);
                if (messagePerson != null) {
                    fileAccessLogDTO.setPerson(messagePerson);
                } else {
                    Person unknownPerson = new Person();
                    unknownPerson.setName("unknown");
                    fileAccessLogDTO.setPerson(unknownPerson);
                }
            });
            model.addAttribute("filesDTO", filesDTO);
        }
        model.addAttribute("storageUrl", storageUrl);
        model.addAttribute("person", person);
        model.addAttribute("message", messageService.findById(id));
        return "messages/message";
    }



    @PostMapping("/add")
    public String addMessage(@ModelAttribute("message") @Valid Message message, BindingResult bindingResult, @RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if(bindingResult.hasErrors()){
            return "redirect:/messages";
        }
        Optional<Person> person = personService.findByName(userDetails.getUsername());
        if(person.isEmpty()){
            System.out.println("Error: person not found");
            return "redirect:/messages";
        }


        if(!file.isEmpty()){
            ResponseEntity<UUID> response = httpRequestService.sendPostRequest( file, "post", person.get().getId());
            if(response.getStatusCode().is2xxSuccessful()){
                message.setFileId(response.getBody());
                message.setFileName(file.getOriginalFilename());
            }
            else
                System.out.println("Error: file not uploaded");
//            File uploadDir = new File(uploadPath);
//            if(!uploadDir.exists()){
//                uploadDir.mkdir();
//            }
//            String uuidFile = java.util.UUID.randomUUID().toString();
//            String resultFilename = uuidFile + "." + file.getOriginalFilename() ;
//            file.transferTo(new File(uploadPath + "/" + resultFilename));
//            message.setFileName(resultFilename);
        }
        messageService.addMessage(message, person.get());
        return "redirect:/messages";
    }





    @PostMapping("/filter")
    public String filter(@RequestParam("tag") String tag, Model model) {
        System.out.println(tag);
        if (tag.isEmpty()) return "redirect:/messages";
        return "redirect:/messages?tag=" + tag;
    }


    @GetMapping("/random")
    public String randomMessage(Model model, @AuthenticationPrincipal UserDetails userDetails){
        Person person = personService.findByName(userDetails.getUsername()).get();
        personService.updateLastLogin(person.getId());
        long id = (long) (Math.random() * 7) + 18;
        Message message = messageService.findById(id);
        UUID fileId = message.getFileId();
        if(fileId != null){
            FilesDTO filesDTO = httpRequestService.getMeta(fileId, person.getId()).getBody();
            List<FileAccessLogDTO> fileAccessLogDTOS = filesDTO.getFileAccessLog();
            fileAccessLogDTOS.forEach(fileAccessLogDTO -> {
                Person messagePerson = personService.findById(fileAccessLogDTO.getAccessedByID()).orElse(null);
                if (messagePerson != null) {
                    fileAccessLogDTO.setPerson(messagePerson);
                } else {
                    Person unknownPerson = new Person();
                    unknownPerson.setName("unknown");
                    fileAccessLogDTO.setPerson(unknownPerson);
                }
            });
            model.addAttribute("filesDTO", filesDTO);
        }
        model.addAttribute("storageUrl", storageUrl);
        model.addAttribute("person", person);
        model.addAttribute("message", message);
        return "messages/message";
    }


}
