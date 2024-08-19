package com.dealership.car.controller;

import com.dealership.car.DTO.ProductDto;
import com.dealership.car.constants.Constants;
import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.TechnicalDataRepository;
import com.dealership.car.service.ProductService;
import com.fasterxml.jackson.annotation.OptBoolean;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController {

    private ProductRepository productRepository;

    private TechnicalDataRepository technicalDataRepository;

    private ProductService productService;

    @GetMapping("/showAllProduct")
    public String showAllProduct(Model model){
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
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
        Optional<TechnicalData> technicalData = technicalDataRepository.findById(id);
        Product product = technicalData.get().getProduct();
        products.add(product);
        model.addAttribute("products", products);
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
    public String addProduct(@RequestParam("productDto") ProductDto productDto){
        productService.saveProduct(productDto);
        return "redirect:/product/addProduct";
    }
}
