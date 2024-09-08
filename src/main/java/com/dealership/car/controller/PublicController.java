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

/**
 * PublicController handles the endpoints related to public access functionalities such as user registration,
 * and password recovery.
 */
@Controller
@RequestMapping(value = "public")
public class PublicController {
    @Autowired
    private PersonService personService;
    /**
     * Displays the user registration page.
     *
     * @param model The model to which a new PersonDto object will be added as an attribute.
     * @return The name of the registration view template.
     */
    @GetMapping(value = "/register")
    public String displayRegisterPage(Model model){
        model.addAttribute("person", new PersonDto());
        return "register.html";
    }
    /**
     * Creates a new user based on the provided PersonDto object. Validates the input data and, if valid, attempts to save
     * the new user. Depending on the outcome, it navigates to different views.
     *
     * @param person A PersonDto object containing user details to be validated and saved.
     * @param errors An Errors object to hold validation errors, if any.
     * @return A String indicating the next view to navigate to ("register.html" if there are validation errors or the user
     *         creation fails, or a redirect to the login page with a registration success parameter if user creation is successful).
     */
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
    /**
     * Handles the request to display the forgot password page.
     * Adds a new Person object to the model.
     *
     * @param model The model to which a new Person object is added.
     * @return The name of the forgot password view template.
     */
    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model){
        model.addAttribute("person", new Person());
        return "fpassword.html";
    }
}
