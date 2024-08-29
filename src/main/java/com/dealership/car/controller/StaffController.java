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
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("staff")
public class StaffController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonService personService;

    @GetMapping("/admins")
    public ModelAndView modelAndView(Model model, HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView("admins.html");
        List<Person> admins = personRepository.findByRoles(Constants.ADMIN_ROLE);
        Map<Person,List<DynamicFieldValue>> adminsMap = personService.getDynamicFieldsForAllPerson(admins);
        modelAndView.addObject("adminsMap", adminsMap);
        return modelAndView;
    }
    @GetMapping(value = "/operators")
    public ModelAndView modelAndView1(Model model, HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView("operators.html");
        List<Person> operators = personRepository.findByRoles(Constants.OPERATOR_ROLE);
        Map<Person,List<DynamicFieldValue>> operatorsMap = personService.getDynamicFieldsForAllPerson(operators);
        modelAndView.addObject("operatorsMap",operatorsMap);
        return modelAndView;
    }
    @GetMapping(value = "/users")
    public ModelAndView modelAndView2(Model model,HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView("users.html");
        List<Person> users = personRepository.findByRoles(Constants.USER_ROLE);
        Map<Person,List<DynamicFieldValue>> usersMap = personService.getDynamicFieldsForAllPerson(users);
        modelAndView.addObject("usersMap", usersMap
        );
        return modelAndView;
    }
}
