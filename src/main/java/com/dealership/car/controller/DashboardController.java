package com.dealership.car.controller;

import com.dealership.car.model.Person;
import com.dealership.car.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @Autowired
    private PersonRepository personRepository;

    @GetMapping(value = "/dashboard")
    public String displayDashboard(Model model, Authentication authentication, HttpSession httpSession){
        Person person = personRepository.findByUsername(authentication.getName());
        model.addAttribute("username", person.getUsername());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        httpSession.setAttribute("loggedInPerson", person);
        return "dashboard.html";
    }

}
