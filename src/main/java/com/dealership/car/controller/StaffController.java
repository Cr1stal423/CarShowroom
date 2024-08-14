package com.dealership.car.controller;

import com.dealership.car.constants.Constants;
import com.dealership.car.model.Person;
import com.dealership.car.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("staff")
public class StaffController {
    @Autowired
    private PersonRepository personRepository;
    @GetMapping("/admins")
    public ModelAndView modelAndView(Model model, HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView("admins.html");
        List<Person> admins = personRepository.findByRoles(Constants.ADMIN_ROLE);
        modelAndView.addObject("admins", admins);
        return modelAndView;
    }
    @GetMapping(value = "/operators")
    public ModelAndView modelAndView1(Model model, HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView("operators.html");
        List<Person> operators = personRepository.findByRoles(Constants.OPERATOR_ROLE);
        modelAndView.addObject("operators",operators);
        return modelAndView;
    }
    @GetMapping(value = "/users")
    public ModelAndView modelAndView2(Model model,HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView("users.html");
        List<Person> users = personRepository.findByRoles(Constants.USER_ROLE);
        modelAndView.addObject("users", users);
        return modelAndView;
    }
}
