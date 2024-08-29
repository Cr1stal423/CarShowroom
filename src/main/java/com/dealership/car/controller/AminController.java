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

@Controller
@AllArgsConstructor
public class AminController {

    private PersonRepository personRepository;

    private PersonService personService;

    @RequestMapping(value = "/deleteAdmin")
    public String deleteAdmin(@RequestParam("id") int id, Model model){
        Boolean isDeleted = personService.deleteUserById(id);
        if (isDeleted){
            model.addAttribute("message", "Admin deleted");
        }
        model.addAttribute("message", "error while deleting admin ");

        return "redirect:/staff/admins";
    }
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
