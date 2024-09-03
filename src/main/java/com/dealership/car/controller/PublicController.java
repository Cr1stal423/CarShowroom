package com.dealership.car.controller;

import com.dealership.car.DTO.PersonDto;
import com.dealership.car.model.Person;
import com.dealership.car.service.PersonService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "public")
public class PublicController {
    @Autowired
    private PersonService personService;
    @GetMapping(value = "/register")
    public String displayRegisterPage(Model model){
        model.addAttribute("person", new PersonDto());
        return "register.html";
    }
    @PostMapping(value = "createUser")
    public String createUser(@Valid @ModelAttribute("person")PersonDto person, Errors errors){
        if (errors.hasErrors()){
            return "register.html";
        }
        boolean isSaved = personService.createNewUser(person);
        if(isSaved){
            return "redirect:/login?register=true";
        } else {
            return "register.html";
        }
    }
    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model){
        model.addAttribute("person", new Person());
        return "fpassword.html";
    }
}
