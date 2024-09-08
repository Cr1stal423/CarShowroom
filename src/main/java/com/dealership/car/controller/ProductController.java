package com.dealership.car.controller;

import com.dealership.car.DTO.ProductDto;
import com.dealership.car.constants.Constants;
import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.dynamic.FieldsMetadata;
import com.dealership.car.model.Person;
import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import com.dealership.car.repository.DynamicFieldValueRepository;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.TechnicalDataRepository;
import com.dealership.car.service.DynamicFieldValueService;
import com.dealership.car.service.ProductService;
import com.fasterxml.jackson.annotation.OptBoolean;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The ProductController class is responsible for handling HTTP requests related to products.
 * It manages product-related operations such as displaying, adding, updating, and deleting products,
 * as well as showing technical data associated with products.
 */
@Controller
@Slf4j
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private ProductRepository productRepository;

    private TechnicalDataRepository technicalDataRepository;

    private ProductService productService;

    private DynamicFieldValueService dynamicFieldValueService;

    /**
     * Handles the request to display all products.
     *
     * @param model the model that holds attributes for the view
     * @param httpSession the HTTP session to store session attributes
     * @return the name of the view to be rendered
     */
    @GetMapping("/showAllProduct")
    public String showAllProduct(Model model, HttpSession httpSession) {
        List<Product> products = productRepository.findAll();
        Map<Product, List<DynamicFieldValue>> personAndDynamicFields = productService.getDynamicFieldsForAllProduct(products);
        httpSession.setAttribute("entityType", "Product");
        model.addAttribute("map", personAndDynamicFields);
        model.addAttribute("products");
        return "product.html";
    }

    /**
     * Handles HTTP GET requests for retrieving technical data associated with a specific product.
     *
     * @param model the model to be used in the view
     * @param id the ID of the product for which technical data is requested
     * @param httpSession the HTTP session for storing attributes
     * @return a string representing the redirection URL to the technical data page with the product ID as a query parameter
     */
    @GetMapping(value = "/technicalData/forProduct")
    public String showTechData(Model model, @RequestParam("id")
    int id, HttpSession httpSession) {
        httpSession.setAttribute("entityType", "TechnicalData");
        return "redirect:/technicalData/forProduct?id=" + id;
    }

    /**
     * Controller method to show a product by its ID.
     *
     * @param model the Model object to which attributes are added
     * @param id the ID of the product to be displayed
     * @param httpSession the HttpSession object for the current session
     * @return the name of the view to render the product details
     */
    @GetMapping("/showProductById")
    public String showProduct(Model model, @RequestParam("id") int id, HttpSession httpSession) {
        List<Product> products = new ArrayList<>();
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            products.add(product.get());
        }
        Map<Product, List<DynamicFieldValue>> personAndDynamicFields = productService.getDynamicFieldsForAllProduct(products);
        model.addAttribute("map", personAndDynamicFields);
        model.addAttribute("products");

        return "product.html";
    }

    /**
     * Handles the GET request for displaying the add product form.
     *
     * @param model the model to which form attributes are added
     * @return the name of the view to be rendered, which is "addProduct.html"
     */
    @GetMapping(value = "/addProduct")
    public String showForm(Model model) {
        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("availabilityStatuses", Constants.AVAILABILITY_STATUSES);
        model.addAttribute("bodyTypes", Constants.BODY_TYPES);
        model.addAttribute("engineTypes", Constants.ENGINE_TYPES);
        model.addAttribute("enginePlacements", Constants.ENGINE_PLACEMENTS);
        return "addProduct.html";
    }

    /**
     * Handles the addition of a new product to the system.
     *
     * @param productDto Data transfer object containing details of the product to be added.
     * @param errors Object holding validation errors.
     * @return A string indicating the view to be rendered. Returns "addProduct.html" if there are validation errors,
     *         otherwise redirects to the product addition page.
     */
    @PostMapping(value = "/addProduct")
    public String addProduct(@Valid @ModelAttribute("productDto") ProductDto productDto, Errors errors) {
        if (errors.hasErrors()) {
            return "addProduct.html";
        }
        productService.saveProduct(productDto);
        return "redirect:/product/addProduct";
    }

    /**
     * Handles the updating of a product's details.
     *
     * @param productId The unique identifier of the product to be updated.
     * @param originCountry The country of origin of the product.
     * @param brand The brand name of the product.
     * @param model The model designation of the product.
     * @param color The color specification of the product.
     * @param availabilityStatus The availability status of the product.
     * @param price The price of the product.
     * @return A redirection URL based on the update operation's success.
     */
    @PostMapping(value = "/updateProduct")
    public String updateProduct(@RequestParam(value = "productId") Integer productId, @RequestParam(value = "originCountry") String originCountry,
                                @RequestParam(value = "brand") String brand,
                                @RequestParam(value = "model") String model, @RequestParam(value = "color") String color,
                                @RequestParam(value = "availabilityStatus") Product.AvailabilityStatus availabilityStatus,
                                @RequestParam(value = "price") Long price) {
        boolean isUpdated = productService.updateProduct(productId, originCountry, brand, model, color, availabilityStatus, price);
        if (isUpdated) {
            return "redirect:/product/showAllProduct";
        } else {
            return "redirect:/dashboard";
        }
    }

    /**
     * Handles the deletion of a product based on its ID.
     *
     * @param id the ID of the product to be deleted
     * @param model the Model object used in the view
     * @return a redirect URL to either the all products page or the dashboard based on the success of the deletion
     */
    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("id") Integer id, Model model) {
        Boolean isDeleted = productService.deleteProductById(id);
        String redirect = "redirect:/product/showAllProduct";
        if (!isDeleted) {
            redirect = "redirect:/dashboard";
        }
        return redirect;
    }
    /**
     * Displays the product information for a user and shows dynamic fields associated with the product.
     *
     * @param model the model to hold attributes for the view
     * @param id the identifier of the product to be displayed
     * @param httpSession the current HTTP session
     * @return the name of the view to be rendered, "product.html"
     */
    @GetMapping("showProductById/forUser")
    public String showProductForUser(Model model, @RequestParam("id") int id, HttpSession httpSession) {
        List<Product> products = new ArrayList<>();
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            products.add(product.get());
        }
        Map<Product, List<DynamicFieldValue>> personAndDynamicFields = productService.getDynamicFieldsForAllProduct(products);
        model.addAttribute("map", personAndDynamicFields);
        model.addAttribute("products");

        return "product.html";
    }
}
