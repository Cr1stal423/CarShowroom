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
 * AminController is a Spring MVC controller for managing admin users.
 * It provides endpoints to delete an admin, change an admin's role to operator,
 * and update admin details.
 */
@Controller
@AllArgsConstructor
public class AminController {

    private PersonRepository personRepository;

    private PersonService personService;

    /**
     * Deletes an admin user by their ID.
     *
     * @param id The ID of the admin to be deleted.
     * @param model The model to store attributes.
     * @return A redirect URL to the list of all admins.
     */
    @RequestMapping(value = "/deleteAdmin")
    public String deleteAdmin(@RequestParam("id") int id, Model model){
        Boolean isDeleted = personService.deleteUserById(id);
        if (isDeleted){
            model.addAttribute("message", "Admin deleted");
        }
        model.addAttribute("message", "error while deleting admin ");

        return "redirect:/staff/admins";
    }
    /**
     * Changes the role of a user to 'Operator' based on the provided user ID.
     *
     * @param id the ID of the user whose role is to be changed to 'Operator'
     * @param model the Model object used to add attributes to be accessed in the view
     * @return a redirect string to the admin staff page
     */
    @RequestMapping(value = "/turnIntoOperator")
    public String turnIntoOperator(@RequestParam("id") int id, Model model){
        try{
            personService.changeUserRole(id, Constants.OPERATOR_ROLE);
            model.addAttribute("message", "Role changed");
        } catch (Exception e){
            model.addAttribute("message", e.getMessage());
            System.out.println(e.getMessage());
            System.out.println("error");
        }
        return "redirect:/staff/admins";
    }
    /**
     * Updates the details of an admin and redirects based on the success of the update.
     *
     * @param id          the ID of the admin to update (optional)
     * @param username    the new username of the admin
     * @param firstName   the new first name of the admin
     * @param lastName    the new last name of the admin
     * @param address     the new address of the admin
     * @param mobileNumber the new mobile number of the admin
     * @return a redirect URL string to 'staff/admins' if the update is successful, or 'dashboard' otherwise
     */
    //TODO maybe add dto to update form
    //TODO add more scenarios if isUpdated = false
    @PostMapping("/updateAdmin")
    public String updateAdmin(@RequestParam(value = "id", required = false)Integer id, @RequestParam("username")String username,
                              @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                              @RequestParam("address")String address, @RequestParam("mobileNumber") String mobileNumber){
        boolean isUpdated = personService.updateUser(username, firstName, lastName, address, mobileNumber);
        if (isUpdated){
            return "redirect:/staff/admins";
        } else {
            return "redirect:/dashboard";
        }
    }
}
