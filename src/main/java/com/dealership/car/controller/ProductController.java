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

@Controller
@Slf4j
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private ProductRepository productRepository;

    private TechnicalDataRepository technicalDataRepository;

    private ProductService productService;

    private DynamicFieldValueService dynamicFieldValueService;

    @GetMapping("/showAllProduct")
    public String showAllProduct(Model model, HttpSession httpSession){
        List<Product> products = productRepository.findAll();
        Map<Product,List<DynamicFieldValue>> personAndDynamicFields = productService.getDynamicFieldsForAllProduct(products);
        httpSession.setAttribute("entityType", "Product");
        model.addAttribute("map", personAndDynamicFields);
        model.addAttribute("products");
        return "product.html";
    }
    @GetMapping(value = "/technicalData/forProduct")
    public String showTechData(Model model, @RequestParam("id")
    int id){
        return "redirect:/technicalData/forProduct?id=" + id;
    }
    @GetMapping("/showProductById")
    public String showProduct(Model model,@RequestParam("id") int id, HttpSession httpSession){
        List<Product> products = new ArrayList<>();
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()){
            products.add(product.get());
        }
        Map<Product,List<DynamicFieldValue>> personAndDynamicFields = productService.getDynamicFieldsForAllProduct(products);
        model.addAttribute("map", personAndDynamicFields);
        model.addAttribute("products");

        return "product.html";
    }
    @GetMapping(value = "/addProduct")
    public String showForm(Model model){
        model.addAttribute("productDto",new ProductDto());
        model.addAttribute("availabilityStatuses", Constants.AVAILABILITY_STATUSES);
        model.addAttribute("bodyTypes", Constants.BODY_TYPES);
        model.addAttribute("engineTypes", Constants.ENGINE_TYPES);
        model.addAttribute("enginePlacements", Constants.ENGINE_PLACEMENTS);
        return "addProduct.html";
    }
    @PostMapping(value = "/addProduct")
    public String addProduct(@Valid @ModelAttribute("productDto") ProductDto productDto, Errors errors){
        if (errors.hasErrors()){
            return "addProduct.html";
        }
        productService.saveProduct(productDto);
        return "redirect:/product/addProduct";
    }
    @PostMapping(value = "/updateProduct")
    public String updateProduct(@RequestParam(value = "productId") Integer productId,@RequestParam(value = "originCountry")String originCountry,
                                @RequestParam(value = "brand")String brand,
                                @RequestParam(value = "model")String model,@RequestParam(value = "color")String color,
                                @RequestParam(value = "availabilityStatus")Product.AvailabilityStatus availabilityStatus,
                                @RequestParam(value = "price")Long price){
        boolean isUpdated = productService.updateProduct(productId,originCountry,brand,model,color, availabilityStatus,price);
        if (isUpdated){
            return "redirect:/product/showAllProduct";
        } else {
            return "redirect:/dashboard";
        }
    }
    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("id") Integer id, Model model){
        Boolean isDeleted = productService.deleteProductById(id);
        String redirect = "redirect:/product/showAllProduct";
        if (!isDeleted){
            redirect = "redirect:/dashboard";
        }
        return redirect;
    }
}
