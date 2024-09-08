package com.dealership.car.controller;

import com.dealership.car.constants.Constants;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller class for managing operator-related operations.
 */
@Controller
@AllArgsConstructor
public class OperatorController {

    private PersonRepository personRepository;

    private PersonService personService;

    /**
     * Deletes an operator based on the given ID.
     *
     * @param id the ID of the operator to be deleted.
     * @param model the model to which attributes are added to be sent to the view.
     * @return a redirect URL to the list of operators.
     */
    @RequestMapping(value = "/deleteOperator")
    public String deleteOperator(@RequestParam("id") int id, Model model) {
        Boolean isDeleted = personService.deleteUserById(id);
        if (isDeleted) {
            model.addAttribute("message", "Operator deleted");
        }
        model.addAttribute("message", "error while deleting operator ");

        return "redirect:/staff/operators";
    }

    /**
     * Changes the role of a user identified by their ID to an admin role.
     *
     * @param id the ID of the user whose role is to be changed
     * @param model the model to which success or error messages will be added
     * @return a redirection URL to the operators page
     */
    @RequestMapping(value = "/turnIntoAdmin")
    public String turnIntoAdmin(@RequestParam("id") int id, Model model) {
        try {
            personService.changeUserRole(id, Constants.ADMIN_ROLE);
            model.addAttribute("message", "Role changed");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "redirect:/staff/operators";
    }

    /**
     * Updates the details of an existing operator.
     *
     * @param id the ID of the operator to update; can be null
     * @param username the updated username of the operator
     * @param firstName the updated first name of the operator
     * @param lastName the updated last name of the operator
     * @param address the updated address of the operator
     * @param mobileNumber the updated mobile number of the operator
     * @return a redirect URL to the operators list page if the update is successful, or to the dashboard if the update fails
     */
    @PostMapping(value = "/updateOperator")
    public String updateOperator(@RequestParam(value = "id", required = false) Integer id, @RequestParam("username") String username,
                                 @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                                 @RequestParam("address") String address, @RequestParam("mobileNumber") String mobileNumber) {

        boolean isUpdated = personService.updateUser(username, firstName, lastName, address, mobileNumber);
        if (isUpdated) {
            return "redirect:/staff/operators";
        } else {
            return "redirect:/dashboard";
        }
    }

}
