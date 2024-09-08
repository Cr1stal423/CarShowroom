package com.dealership.car.controller;

import com.dealership.car.DTO.PersonDto;
import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.OrderEntity;
import com.dealership.car.model.Person;
import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import com.dealership.car.repository.OrderEntityRepository;
import com.dealership.car.repository.PersonRepository;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.TechnicalDataRepository;
import com.dealership.car.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final OrderService orderService;
    private final OrderEntityRepository orderEntityRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final HttpSession httpSession;
    private final DynamicFieldValueService dynamicFieldValueService;
    private final PersonRepository personRepository;
    private final PersonService personService;
    private final TechnicalDataService technicalDataService;
    private final TechnicalDataRepository technicalDataRepository;

    public AnalyticsController(AnalyticsService analyticsService, OrderService orderService, OrderEntityRepository orderEntityRepository, ProductRepository productRepository, ProductService productService, HttpSession httpSession, DynamicFieldValueService dynamicFieldValueService, PersonRepository personRepository, PersonService personService, TechnicalDataService technicalDataService, TechnicalDataRepository technicalDataRepository) {
        this.analyticsService = analyticsService;
        this.orderService = orderService;
        this.orderEntityRepository = orderEntityRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.httpSession = httpSession;
        this.dynamicFieldValueService = dynamicFieldValueService;
        this.personRepository = personRepository;
        this.personService = personService;
        this.technicalDataService = technicalDataService;
        this.technicalDataRepository = technicalDataRepository;
    }

    @GetMapping("/mainPage")
    public ModelAndView showAnalyticsPage() {
        ModelAndView modelAndView = new ModelAndView("analytics.html");
        return modelAndView;
    }

    @GetMapping("/sales")
    public ModelAndView showSales() {
        ModelAndView modelAndView = new ModelAndView("sales.html");
        List<OrderEntity> orderEntityList = orderEntityRepository.findAll();
        List<String> brands = analyticsService.findAllUniqueBrand();
        Map<String, Long> salesCountByBrandMap = analyticsService.getSalesCountByBrand(brands);
        Map<OrderEntity, List<DynamicFieldValue>> orderMap = orderService.getDynamicFieldsForAllOrder(orderEntityList);
        Long totalSales = analyticsService.getTotalSales(salesCountByBrandMap);
        modelAndView.addObject("orderMap", orderMap);
        modelAndView.addObject("brands", brands);
        modelAndView.addObject("salesCountByBrandMap", salesCountByBrandMap);
        modelAndView.addObject("totalSales", totalSales);
        httpSession.setAttribute("entityType", "OrderEntity");
        return modelAndView;
    }

    @PostMapping("/salesByBrand")
    public String showSalesByBrand(@RequestParam("brand") String brand, Model model) {
        List<Product> products = new ArrayList<>();
        if (brand != null && !brand.isEmpty()) {
            products = productService.findProductByBrand(brand);
        }

        List<OrderEntity> orderEntityListByBrand = new ArrayList<>();
        for (Product product : products) {
            OrderEntity order = orderService.findOrderByProduct(product);
            if (order != null) {
                orderEntityListByBrand.add(order);
            }
        }

        Map<OrderEntity, List<DynamicFieldValue>> orderMap = orderService.getDynamicFieldsForAllOrder(orderEntityListByBrand);

        if (orderMap.isEmpty()) {
            model.addAttribute("noOrdersMessage", "No orders available for the selected brand.");
        }

        model.addAttribute("orderMap", orderMap);
        return "sales.html";
    }

    @GetMapping("/availableCars")
    public ModelAndView showAvailableCars() {
        ModelAndView modelAndView = new ModelAndView("availableCar.html");
        List<Product> cars = productRepository.findByAvailabilityStatusEquals(Constants.AVAILABILITY_STATUSES.get(0));
        List<String> models = analyticsService.getAllUniqueModel();
        List<String> brands = analyticsService.findAllUniqueBrand();
        Map<Product, List<DynamicFieldValue>> carsMap = productService.getDynamicFieldsForAllProduct(cars);
        modelAndView.addObject("map", carsMap);
        modelAndView.addObject("models", models);
        modelAndView.addObject("brands", brands);
        httpSession.setAttribute("entityType","Product");
        return modelAndView;
    }

    @PostMapping("/filterByBrandAndModel")
    public String filterByBrandAndModel(@RequestParam(value = "brand", required = false) String brand,
                                        @RequestParam(value = "carModel", required = false) String carModel, Model model) {
        Map<Product, List<DynamicFieldValue>> carsMap = new HashMap<>();
        List<Product> productList = new ArrayList<>();
        if (!(brand == null) && !(carModel == null)) {
            productList = productRepository.findByBrandAndModel(brand, carModel);
            carsMap = productService.getDynamicFieldsForAllProduct(productList);
        } else if ((!(brand == null) && carModel == null)) {
            productList = productRepository.findByBrand(brand);
            carsMap = productService.getDynamicFieldsForAllProduct(productList);
        } else {
            productList = productRepository.findByModel(carModel);
            carsMap = productService.getDynamicFieldsForAllProduct(productList);
        }

        model.addAttribute("map", carsMap);
        return "availableCar.html";
    }
    @GetMapping("/users")
    public String showUserAnalytics(Model model) {
        List<Person> users = personRepository.findByRoles(Constants.USER_ROLE);
        Map<Person,List<DynamicFieldValue>> userMap = personService.getDynamicFieldsForAllPerson(users);
        model.addAttribute("usersMap", userMap);
        httpSession.setAttribute("entityType", "Person");
        model.addAttribute("personDto", new PersonDto());
        return "userAnalytics.html";
    }
    @GetMapping("/technicalModel")
    public String showTechDataByModel(Model model, HttpSession http) {
        List<TechnicalData> technicalDataList = technicalDataRepository.findAll();
        Map<TechnicalData,List<DynamicFieldValue>> technicalDataMap = technicalDataService.getDynamicFieldsForAllTechnicalData(technicalDataList);
        List<String> models = analyticsService.getAllUniqueModel();
        model.addAttribute("models", models);
        http.setAttribute("entityType", "TechnicalData");
        model.addAttribute("technicalDataMap", technicalDataMap);
        return "technicalModel.html";
    }
    @PostMapping("/filterByModel")
    public String findTechDataByModel(@RequestParam("model") String carModel,Model model){
        List<TechnicalData> technicalDataList = technicalDataService.getTechnicalDataByModel(carModel);
        Map<TechnicalData,List<DynamicFieldValue>> technicalDataMap = technicalDataService.getDynamicFieldsForAllTechnicalData(technicalDataList);
            model.addAttribute("technicalDataMap", technicalDataMap);
        return "technicalModel.html";
    }

}
