package com.bitesaitzz.QuickPost.controllers;


import com.bitesaitzz.QuickPost.services.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequestMapping("/admin")
@Controller
public class AdminController {
    private final PersonService personService;

    public AdminController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public String admin(Model model){
        List<String> roles = new ArrayList<>(Arrays.asList("ROLE_USER", "ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_BANNED"));
        model.addAttribute("users", personService.getAllUsers());
        model.addAttribute("roles", roles);
        model.addAttribute("user_id", 0);
        model.addAttribute("user_role", "");
        return "admin/index";
    }

    @PostMapping("/{id}/updateRole")
    public String updateRole(@PathVariable("id") UUID userId, @RequestParam("role") String role) {
        personService.updateRole(userId, role);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") UUID userId) {
        personService.deletePerson(userId);
        return "redirect:/admin";
    }

}
