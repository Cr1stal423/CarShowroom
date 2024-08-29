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
public class OperatorController {

    private PersonRepository personRepository;

    private PersonService personService;

    @RequestMapping(value = "/deleteOperator")
    public String deleteOperator(@RequestParam("id")int id, Model model){
        Boolean isDeleted = personService.deleteUserById(id);
        if (isDeleted){
            model.addAttribute("message", "Operator deleted");
        }
        model.addAttribute("message", "error while deleting operator ");

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
//    @PostMapping(value = "/updateOperator")
//    public ModelAndView updateOperator(@RequestParam(value = "id", required = false)Integer id, @RequestParam("username") String username,
//                                       @RequestParam("firstName")String firstName, @RequestParam("lastName")String lastName,
//                                       @RequestParam("address") String address, @RequestParam("mobileNumber") String mobileNumber){
//        String viewName = "redirect:/staff/operators";
//        ModelAndView modelAndView = new ModelAndView(viewName);
//        boolean isUpdated = personService.updateUser(id, username, firstName, lastName, address, mobileNumber);
//        if (!isUpdated){
//            viewName = "redirect:/dashboard";
//            modelAndView.setViewName(viewName);
//        }
//        return modelAndView;
//    }
@PostMapping(value = "/updateOperator")
public String updateOperator(@RequestParam(value = "id", required = false)Integer id, @RequestParam("username")String username,
                             @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                             @RequestParam("address")String address, @RequestParam("mobileNumber") String mobileNumber) {

    boolean isUpdated = personService.updateUser(username, firstName, lastName, address, mobileNumber);
    if (isUpdated){
        return "redirect:/staff/operators";
    } else {
        return "redirect:/dashboard";
    }
}

}
