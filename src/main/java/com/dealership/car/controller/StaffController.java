package com.dealership.car.controller;

import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.Person;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.service.PersonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * StaffController is a Spring MVC Controller that handles HTTP requests related to the staff members of an organization.
 * This class provides endpoints to retrieve and display different categories of staff such as admins, operators, and users,
 * as well as individual staff members by their ID.
 */
@Controller
@RequestMapping("staff")
public class StaffController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonService personService;

    /**
     * Handles GET requests to the "/admins" endpoint and returns a ModelAndView object for displaying admin users.
     *
     * @param model The model object used to pass attributes to the view.
     * @param httpSession The HTTP session object used to store session-related information.
     *
     * @return A ModelAndView object configured with the "admins.html" view and populated with a map of admins and their dynamic field values.
     */
    @GetMapping("/admins")
    public ModelAndView modelAndView(Model model, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("admins.html");
        List<Person> admins = personRepository.findByRoles(Constants.ADMIN_ROLE);
        Map<Person, List<DynamicFieldValue>> adminsMap = personService.getDynamicFieldsForAllPerson(admins);
        modelAndView.addObject("adminsMap", adminsMap);
        httpSession.setAttribute("entityType", "Person");
        return modelAndView;
    }

    /**
     * Handles GET requests to retrieve and display information about operators.
     * This method fetches a list of operators from the database and retrieves
     * their dynamic fields to be displayed on the "operators.html" page.
     *
     * @param httpSession the HttpSession object associated with the current user session.
     *        This is used to store the "entityType" attribute.
     * @return a ModelAndView object that holds the view name "operators.html" and a map of
     *         operators to their dynamic field values, added as an attribute named "operatorsMap".
     */
    @GetMapping(value = "/operators")
    public ModelAndView modelAndView1(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("operators.html");
        List<Person> operators = personRepository.findByRoles(Constants.OPERATOR_ROLE);
        Map<Person, List<DynamicFieldValue>> operatorsMap = personService.getDynamicFieldsForAllPerson(operators);
        modelAndView.addObject("operatorsMap", operatorsMap);
        httpSession.setAttribute("entityType", "Person");
        return modelAndView;
    }

    /**
     * Handles the HTTP GET request for retrieving users with the role defined in Constants.USER_ROLE.
     * It returns a ModelAndView object that holds the view name "users.html" and a map of users
     * and their corresponding dynamic field values.
     *
     * @param httpSession the current HTTP session used to store attributes related to the session.
     * @return ModelAndView object containing the view name "users.html" and a map of users and their dynamic fields.
     */
    @GetMapping(value = "/users")
    public ModelAndView modelAndView2(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("users.html");
        List<Person> users = personRepository.findByRoles(Constants.USER_ROLE);
        Map<Person, List<DynamicFieldValue>> usersMap = personService.getDynamicFieldsForAllPerson(users);
        modelAndView.addObject("usersMap", usersMap);
        httpSession.setAttribute("entityType", "Person");
        return modelAndView;
    }

    /**
     * Handles GET requests to find a user by ID and return a view displaying the user's dynamic fields.
     * This method retrieves the user from the database using the provided ID, fetches the dynamic fields for the user,
     * and returns a ModelAndView object with the users' data populated.
     *
     * @param id the ID of the user to be found
     * @param httpSession the current HTTP session
     * @return a ModelAndView object containing the view name and model data for displaying the user's dynamic fields
     */
    @GetMapping(value = "/findById")
    public ModelAndView modelAndView3(@RequestParam(value = "id") Integer id, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("users.html");
        List<Person> personList = new ArrayList<>();
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            personList.add(person);
        } else {
            System.out.println("Not found user with given id");
        }
        Map<Person, List<DynamicFieldValue>> userMap = personService.getDynamicFieldsForAllPerson(personList);
        modelAndView.addObject("usersMap", userMap);
        return modelAndView;
    }
}
