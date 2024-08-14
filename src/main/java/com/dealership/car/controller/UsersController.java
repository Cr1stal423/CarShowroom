package com.dealership.car.controller;

import com.dealership.car.constants.Constants;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class UsersController {

    private PersonRepository personRepository;

    private PersonService personService;

    @RequestMapping(value = "/deleteUser")
    public String deleteUser(@RequestParam("id")int id, Model model){
        try{
            personRepository.deleteById(id);
            model.addAttribute("message", "User deleted");
        } catch (Exception e){
            model.addAttribute("message", "error deleting user " + e.getMessage());
        }
        return "redirect:/staff/users";
    }
    @RequestMapping(value = "/turnUserIntoOperator")
    public String turnIntoOperator1(@RequestParam("id") int id, Model model){
        try{
            personService.changeUserRole(id, Constants.OPERATOR_ROLE);
            model.addAttribute("message", "Role changed");
        } catch (Exception e){
            model.addAttribute("message", e.getMessage());
        }
        return "redirect:/staff/users";
    }
}
