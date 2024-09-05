package com.dealership.car.controller;

import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class UsersController {

    private final OrderService orderService;

    private PersonRepository personRepository;

    private PersonService personService;


    @RequestMapping(value = "/deleteUser")
    public String deleteUser(@RequestParam("id") int id, Model model) {
        Boolean isDeleted = personService.deleteUserById(id);
        if (isDeleted) {
            model.addAttribute("message", "User deleted");
        }
        model.addAttribute("message", "error while deleting user ");

        return "redirect:/staff/users";
    }

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

    @GetMapping("/user/displayOrders")
    public String userOrders(){
        String currentUser= personService.getCurrentUser();
        Person person = personRepository.findByUsername(currentUser);
        Integer personId = person.getPersonId();
        return String.format("redirect:/orders/ordersByUser?id=%s",personId);
    }
}
