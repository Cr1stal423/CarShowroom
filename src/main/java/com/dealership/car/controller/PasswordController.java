package com.dealership.car.controller;

import com.dealership.car.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling password-related operations.
 * This class handles requests related to password management such as showing a forgotten password.
 */
@Controller
public class PasswordController {
    @Autowired
    private PersonService personService;

    @PostMapping("/showPwd")
    public String showPwd(@RequestParam(value = "username",required = true) String username,
            Model model){
        String errorMessage = null;
        String password = personService.forgotPassword(username);
        if (password == null){
            errorMessage = "Not found user with given username";
        }
        model.addAttribute("password", password);
        model.addAttribute("errorMessage", errorMessage);
        return "fpassword.html";
    }
}
