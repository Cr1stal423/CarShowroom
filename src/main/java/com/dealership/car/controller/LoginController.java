package com.dealership.car.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The LoginController class handles HTTP requests for the login page.
 * It supports both GET and POST methods for displaying the login view.
 */
@Controller
public class LoginController {
    /**
     * Displays the login page and handles errors or registration success messages.
     *
     * @param error Optional request parameter indicating an error has occurred.
     * @param register Optional request parameter indicating successful registration.
     * @param model Interface to pass attributes to the view.
     * @return The login page view name.
     */
    @RequestMapping(value ="/login",method = { RequestMethod.GET, RequestMethod.POST })
    public String displayLoginPage(@RequestParam(value = "error", required = false)String error,
                                   @RequestParam(value = "register", required = false)String register,
                                   Model model){
        String errorMessage = null;
        if (error != null){
            errorMessage = "Username or Password is incorrect";
        } else if (register != null) {
            errorMessage = "You registration successful. Login with registered credentials !!";
        }

        model.addAttribute("errorMessage", errorMessage);
        return "login.html";
    }
}
