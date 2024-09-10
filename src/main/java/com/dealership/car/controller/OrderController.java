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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * OrderController is a Spring MVC controller that handles HTTP requests related to orders.
 * It provides endpoints to view, create, search, and delete orders.
 */
@Controller
@AllArgsConstructor
@RequestMapping(value = "orders")
public class OrderController {

    private final OrderService orderService;
    private OrderEntityRepository orderEntityRepository;

    private PersonRepository personRepository;

    private ProductRepository productRepository;

    private ProductService productService;

    private PersonService personService;

    /**
     * Handles requests to show all orders.
     *
     * @param model       the model to hold order data attributes
     * @param httpSession the HTTP session to set session attributes
     * @return the name of the view to be rendered, "orders.html"
     */
    @GetMapping(value = "/showAll")
    public String showAllOrders(Model model, HttpSession httpSession) {
        List<OrderEntity> orders = orderEntityRepository.findAll();
        Map<OrderEntity, List<DynamicFieldValue>> orderMap = orderService.getDynamicFieldsForAllOrder(orders);
        model.addAttribute("orderMap", orderMap);
        httpSession.setAttribute("entityType", "OrderEntity");
        return "orders.html";
    }

    /**
     * Retrieves the orders for a specific user by their ID and adds the orders along with their dynamic fields to the model.
     *
     * @param id    the ID of the user whose orders are to be retrieved
     * @param model the model to which the order data will be added
     * @return the view name "orders.html"
     */
    @GetMapping("/ordersByUser")
    public String findOrdersByUser(@RequestParam("id") int id, Model model) {
        Optional<Person> person = personRepository.findById(id);
        List<OrderEntity> orders = orderEntityRepository.findByPerson(person.get());
        Map<OrderEntity, List<DynamicFieldValue>> orderMap = orderService.getDynamicFieldsForAllOrder(orders);
        model.addAttribute("orderMap", orderMap);
        return "orders.html";
    }

    /**
     * Displays the form for adding a new order.
     *
     * @param model the model to which attributes for the form are added
     * @return the name of the view to be rendered
     */
    @GetMapping(value = "/addOrder")
    public String showForm(Model model) {
        model.addAttribute("orderDto", new OrderDto());
        model.addAttribute("persons", personService.findAll());
        model.addAttribute("products", productRepository.findByAvailabilityStatusEqualsOrAvailabilityStatus(Constants.AVAILABILITY_STATUSES.get(0), Constants.AVAILABILITY_STATUSES.get(2)));
        model.addAttribute("paymentTypes", Constants.PAYMENT_TYPES);
        model.addAttribute("paymentMethods", Constants.PAYMENT_METHODS);
        return "order-form.html";
    }

    /**
     * Processes an order request to create and persist an OrderEntity.
     * If there are validation errors, the user is redirected back to the order form.
     *
     * @param orderDto The data transfer object containing order details.
     * @param model    The model object for passing data to the view.
     * @param errors   The Errors object containing any validation errors.
     * @return A string representing the view name to be returned.
     */
    @PostMapping(value = "/makeOrder")
    public String makeOrder(@Valid @ModelAttribute("orderDto") OrderDto orderDto, Model model, Errors errors) {
        if (errors.hasErrors()) {
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
                if (product.getAvailabilityStatus().equals(Constants.AVAILABILITY_STATUSES.get(0))) {
                    product.setAvailabilityStatus(Constants.AVAILABILITY_STATUSES.get(1));
                }

                orderEntityRepository.save(order);
                productRepository.save(product);
            }

            return "redirect:/orders/addOrder";
        }
    }

    /**
     * Displays order details by order ID.
     *
     * @param id    the identifier of the order to be displayed
     * @param model the model object to pass attributes to the view
     * @return the name of the view to render, in this case "orders.html"
     */
    @GetMapping("/searchById")
    public String showOrderById(@RequestParam("id") Integer id, Model model) {
        List<OrderEntity> orderEntityList = new ArrayList<>();
        Optional<OrderEntity> optionalOrder = orderEntityRepository.findById(id);
        if (optionalOrder.isPresent()) {
            orderEntityList.add(optionalOrder.get());
        }
        Map<OrderEntity, List<DynamicFieldValue>> orderMap = orderService.getDynamicFieldsForAllOrder(orderEntityList);
        model.addAttribute("orderMap", orderMap);
        return "orders.html";
    }

    /**
     * Deletes an order by its ID and redirects to the appropriate page based on the outcome.
     *
     * @param id the ID of the order to delete
     * @return a redirection string to the appropriate view
     */
    @GetMapping("/deleteOrder")
    public String deleteOrder(@RequestParam("id") Integer id) {
        Boolean isDeleted = orderService.deleteOrder(id);
        if (isDeleted) {
            return "redirect:/orders/showAll";
        }
        return "dashboard.html";
    }
}
