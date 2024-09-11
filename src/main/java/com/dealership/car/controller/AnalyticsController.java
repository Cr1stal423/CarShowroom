package com.dealership.car.controller;

import com.dealership.car.DTO.PersonDto;
import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.*;
import com.dealership.car.repository.*;
import com.dealership.car.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


/**
 * Controller for handling analytics-related requests.
 * This class contains methods for displaying analytics pages and filtering
 * various entities such as sales, cars, users, and technical data.
 */
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
    private final SupplierService supplierService;
    private final SupplierRepository supplierRepository;

    public AnalyticsController(AnalyticsService analyticsService, OrderService orderService, OrderEntityRepository orderEntityRepository, ProductRepository productRepository, ProductService productService, HttpSession httpSession, DynamicFieldValueService dynamicFieldValueService, PersonRepository personRepository, PersonService personService, TechnicalDataService technicalDataService, TechnicalDataRepository technicalDataRepository, SupplierService supplierService, SupplierRepository supplierRepository) {
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
        this.supplierService = supplierService;
        this.supplierRepository = supplierRepository;
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
        httpSession.setAttribute("entityType", "Product");
        return modelAndView;
    }

    /**
     * Handles the POST request to filter cars by their brand and model.
     * Based on the provided brand and model parameters, it retrieves the
     * relevant cars from the repository, fetches their dynamic fields,
     * and adds them to the model for rendering.
     *
     * @param brand    the brand of the car to filter by, can be null
     * @param carModel the model of the car to filter by, can be null
     * @param model    the model object used for adding attributes required for rendering the view
     * @return the name of the view template to be rendered, "availableCar.html"
     */
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

    /**
     * Handles the GET request for the user analytics page.
     * It retrieves users with the role of "USER" from the repository,
     * fetches their dynamic fields, and adds necessary attributes to the model.
     *
     * @param model the model object used for adding attributes for rendering the view
     * @return the name of the view template to be rendered, "userAnalytics.html"
     */
    @GetMapping("/users")
    public String showUserAnalytics(Model model) {
        List<Person> users = personRepository.findByRoles(Constants.USER_ROLE);
        Map<Person, List<DynamicFieldValue>> userMap = personService.getDynamicFieldsForAllPerson(users);
        model.addAttribute("usersMap", userMap);
        httpSession.setAttribute("entityType", "Person");
        model.addAttribute("personDto", new PersonDto());
        return "userAnalytics.html";
    }

    /**
     * Handles the GET request for the technical model page.
     * It retrieves all the technical data along with their dynamic field values, fetches all unique models,
     * and adds these to the provided model for rendering the view.
     *
     * @param model the model object used for adding attributes required for rendering the view
     * @param http  the HTTP session to set session attributes
     * @return the name of the view template to be rendered, "technicalModel.html"
     */
    @GetMapping("/technicalModel")
    public String showTechDataByModel(Model model, HttpSession http) {
        List<TechnicalData> technicalDataList = technicalDataRepository.findAll();
        Map<TechnicalData, List<DynamicFieldValue>> technicalDataMap = technicalDataService.getDynamicFieldsForAllTechnicalData(technicalDataList);
        List<String> models = analyticsService.getAllUniqueModel();
        model.addAttribute("models", models);
        http.setAttribute("entityType", "TechnicalData");
        model.addAttribute("technicalDataMap", technicalDataMap);
        return "technicalModel.html";
    }

    /**
     * Handles the POST request to find technical data by a specific car model.
     * It retrieves technical data for the specified model along with their dynamic field values,
     * and adds these to the provided model for rendering.
     *
     * @param carModel the name of the car model to filter and retrieve the technical data
     * @param model    the model object used for adding attributes required for rendering the view
     * @return the name of the view template to be rendered, "technicalModel.html"
     */
    @PostMapping("/filterByModel")
    public String findTechDataByModel(@RequestParam("model") String carModel, Model model) {
        List<TechnicalData> technicalDataList = technicalDataService.getTechnicalDataByModel(carModel);
        Map<TechnicalData, List<DynamicFieldValue>> technicalDataMap = technicalDataService.getDynamicFieldsForAllTechnicalData(technicalDataList);
        model.addAttribute("technicalDataMap", technicalDataMap);
        return "technicalModel.html";
    }
//    @GetMapping("/paymentAnalytics")
//    public ModelAndView showPaymentAnalytics() {
//        ModelAndView modelAndView = new ModelAndView("paymentAnalytics.html");
//        Map<Integer,Integer> personandProductMap = analyticsService.personAndProductByPaymentType(String.valueOf(Constants.PAYMENT_TYPES.get(0)));
//        List<String> paymentTypes = orderEntityRepository.findAllUniquePaymentTypes();
//        modelAndView.addObject("personAndProductMap", personandProductMap);
//        modelAndView.addObject("paymentTypes", paymentTypes);
//        return modelAndView;
//    }
//    @PostMapping("/filterByPaymentType")
//    public String findByPaymentType(@RequestParam("paymentType") String paymentType, Model model) {
//        Map<Integer,Integer> persnoAndProductMap = analyticsService.personAndProductByPaymentType(String.valueOf(OrderEntity.PaymentType.valueOf(paymentType)));
//        List<String> paymentTypes = orderEntityRepository.findAllUniquePaymentTypes();
//        model.addAttribute("personAndProductMap", persnoAndProductMap);
//        model.addAttribute("paymentTypes", paymentTypes);
//        return "paymentAnalytics.html";
//    }

    /**
     * Handles the GET request to display payment analytics.
     * It retrieves a map of persons and products categorized by payment type
     * and a list of unique payment types. These are then added to the model
     * for rendering the view.
     *
     * @return a ModelAndView object containing the view template "paymentAnalytics.html"
     *         and the necessary model attributes for rendering the payment analytics page.
     */
    @GetMapping("/paymentAnalytics")
    public ModelAndView showPaymentAnalytics() {
        ModelAndView modelAndView = new ModelAndView("paymentAnalytics.html");
        Map<Map<Person, List<DynamicFieldValue>>, Map<Product, List<DynamicFieldValue>>> paymentMap = analyticsService.personAndProductByPaymentType(String.valueOf(Constants.PAYMENT_TYPES.get(0)));
        List<String> paymentTypes = orderEntityRepository.findAllUniquePaymentTypes();
        modelAndView.addObject("paymentMap", paymentMap);
        modelAndView.addObject("paymentTypes", paymentTypes);
        return modelAndView;
    }

    /**
     * Handles the POST request to filter analytics data by the given payment type.
     * Fetches the person and product data associated with the payment type,
     * retrieves all unique payment types, and adds the results to the model.
     *
     * @param paymentType the type of payment to filter the analytics data by
     * @param model the model object used for adding attributes required for rendering the view
     * @return the name of the view template to be rendered, "paymentAnalytics.html"
     */
    @PostMapping("/filterByPaymentType")
    public String findByPaymentType(@RequestParam("paymentType") String paymentType, Model model) {
        Map<Map<Person, List<DynamicFieldValue>>, Map<Product, List<DynamicFieldValue>>> paymentMap = analyticsService.personAndProductByPaymentType(String.valueOf(OrderEntity.PaymentType.valueOf(paymentType)));
        List<String> paymentTypes = orderEntityRepository.findAllUniquePaymentTypes();
        model.addAttribute("paymentMap", paymentMap);
        model.addAttribute("paymentTypes", paymentTypes);
        return "paymentAnalytics.html";
    }

    /**
     * Handles the GET request to show awaiting customers.
     * This method retrieves the products with the "COMING_SOON" availability status,
     * fetches orders related to these products, calculates the total number of such orders,
     * and adds these details to the model.
     *
     * @return a ModelAndView object that holds the view name "awaitingCustomers.html" along with
     *         the order list and the total count of orders related to products that are "COMING_SOON".
     */
    @GetMapping("/awaitingCustomers")
    public ModelAndView showAwaitingCustomers() {
        ModelAndView modelAndView = new ModelAndView("awaitingCustomers.html");
        List<Product> products = productRepository.findByAvailabilityStatusEquals(Constants.AVAILABILITY_STATUSES.get(2));
        List<OrderEntity> orderList = analyticsService.getOrdersByProduct(products);
        Integer total = orderList.size();
        modelAndView.addObject("total", total);
        modelAndView.addObject("orderList", orderList);
        return modelAndView;
    }
    /**
     * Handles the GET request to display the low stock car page.
     * It retrieves the total availability of cars by brand, calculates which brands have low stock,
     * and adds the result to the model for rendering the view.
     *
     * @return ModelAndView object pointing to "lowStockCar.html" with an attribute "lowStockBrands" containing the list of brands with low stock.
     */
    @GetMapping("/lowStockCar")
    public ModelAndView showLowStockCar() {
        ModelAndView modelAndView = new ModelAndView("lowStockCar.html");
        Map<String,Integer> totalAvailableCarByBrand = analyticsService.totalAvailableCarByBrand();
        Map<String,Integer> lowStockBrands =  analyticsService.findLowStockCar(totalAvailableCarByBrand);
        modelAndView.addObject("lowStockBrands", lowStockBrands);
        return modelAndView;
    }
    /**
     * Handles the POST request to display cars with low stock by their models.
     * Based on the provided car model names, it retrieves the low stock information
     * and adds it to the model object for rendering the view.
     *
     * @param models a comma-separated string of car model names to check for low stock
     * @param model  the model object used to add attributes required for rendering the view
     * @return the name of the view template to be rendered, "lowStockCar.html"
     */
    @PostMapping("/lowStockCarByModel")
    public String showLowStockCarByModel(@RequestParam("models") String models, Model model) {
        List<String> carModels = Arrays.asList(models.split("\\s*,\\s*"));
        Map<String,Integer> lowStockCarByModel = analyticsService.findLowStockCarByModel(carModels);
        model.addAttribute("lowStockCarByModel", lowStockCarByModel);
        return "lowStockCar.html";
    }
    //TODO can be improved to display how long the supplier is delayed(create Map<Map<Suppliers,Delay time>,List<dynamicFieldValue>>)
    @GetMapping("/delayedSuppliers")
    public String showDelayedSuppliers(Model model) {
        List<Supplier> supplierList = supplierRepository.findByIsDelayed(true);
        Map<Supplier,List<DynamicFieldValue>> supplierMap= supplierService.getAllDynamicFieldsForSupplier(supplierList);
        model.addAttribute("supplierMap", supplierMap);
        return "delayedSuppliers.html";
    }
    @GetMapping("/contractPerDealer")
    public String showContractPerDealer(Model model) {
        List<String> roles = Arrays.asList(
                Constants.OPERATOR_ROLE,
                Constants.ADMIN_ROLE,
                Constants.OWNER_ROLE
        );
        List<Person> staff = personRepository.findByRoles(roles);
        Map<Person,List<OrderEntity>> resultMap = analyticsService.getOrdersByWorker(staff);
        Map<Person,Integer> countOrdersMap = analyticsService.countOrdersByWorker(staff);
        model.addAttribute("countOrderMap", countOrdersMap);
        return "contractPerDealer.html";
    }
}
