package com.dealership.car.controller;

import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Person;
import com.dealership.car.repository.OrderEntityRepository;
import com.dealership.car.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/orders")
public class OrderController {

    private OrderEntityRepository orderEntityRepository;

    private PersonRepository personRepository;

    @GetMapping(value = "/showAll")
    public String showAllOrders(Model model){
        List<OrderEntity> orders = orderEntityRepository.findAll();
        model.addAttribute("orders", orders);
        return "orders.html";
    }

    @GetMapping("/ordersByUser")
    public String findOrdersByUser(@RequestParam("id")int id, Model model){
        Optional<Person> person = personRepository.findById(id);
        List<OrderEntity> orders = orderEntityRepository.findByPerson(person.get());
        model.addAttribute("orders", orders);
        return "orders.html";
    }
}
