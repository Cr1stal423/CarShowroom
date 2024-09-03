package com.dealership.car.controller;

import com.dealership.car.DTO.OrderDto;
import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Person;
import com.dealership.car.model.Product;
import com.dealership.car.repository.OrderEntityRepository;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.service.OrderService;
import com.dealership.car.service.PersonService;
import com.dealership.car.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderService orderService;
    private OrderEntityRepository orderEntityRepository;

    private PersonRepository personRepository;

    private ProductRepository productRepository;

    private ProductService productService;

    private PersonService personService;

    @GetMapping(value = "/showAll")
    public String showAllOrders(Model model, HttpSession httpSession){
        List<OrderEntity> orders = orderEntityRepository.findAll();
        Map<OrderEntity,List<DynamicFieldValue>> orderMap = orderService.getDynamicFieldsForAllOrder(orders);
        model.addAttribute("orderMap", orderMap);
        httpSession.setAttribute("entityType", "OrderEntity");
        return "orders.html";
    }

    @GetMapping("/ordersByUser")
    public String findOrdersByUser(@RequestParam("id")int id, Model model){
        Optional<Person> person = personRepository.findById(id);
        List<OrderEntity> orders = orderEntityRepository.findByPerson(person.get());
        model.addAttribute("orders", orders);
        return "orders.html";
    }
    @GetMapping(value = "/addOrder")
    public String showForm(Model model){
        model.addAttribute("orderDto", new OrderDto());
        model.addAttribute("persons", personService.findAll());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("paymentTypes", Constants.PAYMENT_TYPES);
        model.addAttribute("paymentMethods", Constants.PAYMENT_METHODS);
        return "order-form.html";
    }

    @PostMapping(value = "/makeOrder")
    public String makeOrder(@Valid@ModelAttribute("orderDto")OrderDto orderDto, Model model, Errors errors){
        if (errors.hasErrors()){
            return "order-form.html";
        } else {
            Optional<Product> optionalProduct = productRepository.findById(orderDto.getProductId());
            Optional<Person> optionalPerson = personRepository.findById(orderDto.getPersonId());
            if (optionalProduct.isPresent() && optionalPerson.isPresent()) {
                Product product = optionalProduct.get();
                Person person = optionalPerson.get();

                OrderEntity order = new OrderEntity();
                order.setPerson(person);
                order.setProduct(product);
                order.setDelivery(orderDto.getDelivery());
                order.setPaymentMethod(orderDto.getPaymentMethod());
                order.setPaymentType(orderDto.getPaymentType());
                product.setAvailabilityStatus(Constants.AVAILABILITY_STATUSES.get(1));

                orderEntityRepository.save(order);
                productRepository.save(product);
            }

            return "redirect:/orders/addOrder";
        }
    }

}
