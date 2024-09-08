package com.dealership.car.controller;

import com.dealership.car.model.Person;
import com.dealership.car.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The DashboardController class handles the requests for the dashboard page.
 * It retrieves user information based on their authentication details and sets up the model and session attributes.
 */
@Controller
public class DashboardController {
    @Autowired
    private PersonRepository personRepository;

    /**
     * Displays the dashboard page for the authenticated user. Retrieves user information
     * based on their authentication details and sets up the model and session attributes.
     *
     * @param model the Model object used to pass attributes to the view
     * @param authentication the Authentication object containing the user's authentication information
     * @param httpSession the HttpSession object used to store session attributes
     * @return the name of the view to be rendered, in this case "dashboard.html"
     */
    @GetMapping(value = "/dashboard")
    public String displayDashboard(Model model, Authentication authentication, HttpSession httpSession){
        Person person = personRepository.findByUsername(authentication.getName());
        model.addAttribute("username", person.getUsername());
        model.addAttribute("roles", authentication.getAuthorities().toString());
        httpSession.setAttribute("loggedInPerson", person);
        return "dashboard.html";
    }

}
