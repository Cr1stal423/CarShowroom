package com.dealership.car.controller;

import com.dealership.car.constants.Constants;
import com.dealership.car.model.Person;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class OperatorController {

    private PersonRepository personRepository;

    private PersonService personService;

    @RequestMapping(value = "/deleteOperator")
    public String deleteOperator(@RequestParam("id")int id, Model model){
        try {
            personRepository.deleteById(id);
            model.addAttribute("message", "Operator deleted");
        } catch (Exception e){
            model.addAttribute("message", "Error deleting operator" + e.getMessage());
        }
        return "redirect:/staff/operators";
    }
    @RequestMapping(value = "/turnIntoAdmin")
    public String turnIntoAdmin(@RequestParam("id")int id, Model model){
        try{
            personService.changeUserRole(id, Constants.ADMIN_ROLE);
            model.addAttribute("message", "Role changed");
        } catch (Exception e){
            model.addAttribute("message",e.getMessage());
        }
        return "redirect:/staff/operators";
    }
}
