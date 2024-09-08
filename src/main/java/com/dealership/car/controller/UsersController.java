package com.dealership.car.controller;

import com.dealership.car.DTO.PersonDto;
import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.mapper.PersonMapper;
import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Person;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.security.AuthentictionProvider;
import com.dealership.car.service.OrderService;
import com.dealership.car.service.PersonService;
import jakarta.transaction.UserTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The UsersController class handles user-related operations such as deleting users,
 * updating user information, and changing user roles. It interacts with services
 * such as OrderService and PersonService to perform these operations.
 */
@Controller
@AllArgsConstructor
public class UsersController {

    private final OrderService orderService;
    private final PersonMapper personMapper;

    private PersonRepository personRepository;

    private PersonService personService;


    /**
     * Deletes a user based on the provided user ID.
     *
     * @param id The ID of the user to be deleted.
     * @param model The model to hold attributes for view rendering.
     * @return A redirect string to the users page, with a message indicating the result of the deletion.
     */
    @RequestMapping(value = "/deleteUser")
    public String deleteUser(@RequestParam("id") int id, Model model) {
        Boolean isDeleted = personService.deleteUserById(id);
        if (isDeleted) {
            model.addAttribute("message", "User deleted");
        }
        model.addAttribute("message", "error while deleting user ");

        return "redirect:/staff/users";
    }

    /**
     * Changes the role of a user to "OPERATOR" and updates the model with a message.
     *
     * @param id The ID of the user whose role is to be changed.
     * @param model The model to be updated with success or error message.
     * @return A redirect URL to the users page.
     */
    @RequestMapping(value = "/turnUserIntoOperator")
    public String turnIntoOperator1(@RequestParam("id") int id, Model model) {
        try {
            personService.changeUserRole(id, Constants.OPERATOR_ROLE);
            model.addAttribute("message", "Role changed");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "redirect:/staff/users";
    }

    /**
     * Updates the user details based on the provided parameters.
     *
     * @param username the username of the user to be updated
     * @param firstName the new first name of the user
     * @param lastName the new last name of the user
     * @param address the new address of the user
     * @param mobileNumber the new mobile number of the user
     * @param model the Model object used by the view
     * @return a redirect URL indicating whether the update was successful or not
     */
    @PostMapping(value = "updateUser")
    public String updateUser(@RequestParam("username") String username, @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName, @RequestParam("address") String address,
                             @RequestParam("mobileNumber") String mobileNumber, Model model) {
        boolean isUpdated = personService.updateUser(username, firstName, lastName, address, mobileNumber);
        if (isUpdated) {
            return "redirect:/staff/users";
        } else {
            return "redirect:/dashboard";
        }
    }
/**
 * Handles updating a user with the specified details.
 *
 * @param id the unique identifier of the user
 * @param username the username of the user
 * @param firstName the first name of the user
 * @param lastName the last name of the user
 * @param address the address of the user
 * @param mobileNumber the mobile number of the user
 * @param passportSeries the passport series of the user
 * @param passportNumber the passport number of the user
 * @return a redirect URL based on the success of the update operation
 */
//    @PostMapping(value = "updateUserA")
//    public String updateUser(@ModelAttribute("personDto") PersonDto personDto, Model model) {
//        Person person = personRepository.findByUsername(personDto.getUsername());
//        if (person != null){
//            personMapper.updatePersonFromDto(personDto, person);
//            personRepository.save(person);
//        }
//        return "redirect:/staff/users";
//    }
    @PostMapping(value = "updateUserA")
    public String updateUserA(@RequestParam("id")Integer id, @RequestParam("username")String username,
                              @RequestParam("firstName")String firstName,@RequestParam("lastName")String lastName,
                              @RequestParam("address")String address, @RequestParam("mobileNumber")String mobileNumber,
                              @RequestParam("passportSeries")String passportSeries, @RequestParam("passportNumber")String passportNumber){
        boolean isUpdated = personService.updateroleA(id,username,firstName,lastName,address,mobileNumber,passportSeries,passportNumber);
        if (isUpdated) {
            return "redirect:/analytics/users";
        } else {
            return "redirect:/analytics";
        }
    }
    /**
     * Handles the request to display the current user's orders.
     * Fetches the username of the currently authenticated user and retrieves the corresponding Person entity.
     * Redirects to the orders page for the specific user.
     *
     * @return A string that contains the redirect URL to display the orders of the current user.
     */
    @GetMapping("/user/displayOrders")
    public String userOrders(){
        String currentUser= personService.getCurrentUser();
        Person person = personRepository.findByUsername(currentUser);
        Integer personId = person.getPersonId();
        return String.format("redirect:/orders/ordersByUser?id=%s",personId);
    }
}
