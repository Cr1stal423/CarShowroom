package com.dealership.car.controller;

import com.dealership.car.model.Contact;
import com.dealership.car.service.ContactService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/contact")
public class ContactController {

    private ContactService contactService;

    @GetMapping()
    public String showContactPage(Model model){
        model.addAttribute("contact", new Contact());
        return "contact.html";
    }

    @PostMapping("/saveMsg")
    public String saveMsg(@Valid @ModelAttribute Contact contact, Errors errors){
        String returnResult = "";
        if (errors.hasErrors()){
            returnResult = "contact.html";
        }
        boolean isExist = contactService.isUserAlreadyExist(contact);
        boolean isSaved = contactService.saveMsg(contact);
        if (isSaved && isExist){
            returnResult = "redirect:/home";
        } else if (isSaved) {
            returnResult = "redirect:/public/register";

        }
        return returnResult;
    }

}
