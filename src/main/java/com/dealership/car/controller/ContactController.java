package com.dealership.car.controller;

import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.Contact;
import com.dealership.car.repository.ContactRepository;
import com.dealership.car.service.ContactService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/contact")
public class ContactController {

    private ContactService contactService;

    private ContactRepository contactRepository;

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
    @GetMapping("/showMessages")
    public ModelAndView showMsg(HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView("messages.html");
        List<Contact> messages = contactRepository.findByStatusEqualsOrStatus(Constants.NEW_USER,Constants.USER_EXIST);
        Map<Contact,List<DynamicFieldValue>> messagesMap = contactService.getAllDynamicFieldsForContact(messages);
        modelAndView.addObject("messagesMap", messagesMap);
        httpSession.setAttribute("entityType", "Contact");
        return modelAndView;
    }
    @GetMapping("/closeMsg")
    public String closeMsg(@RequestParam("id") int id){
        contactService.closeMsg(id);
        return "redirect:/contact/showMessages";
    }
}
