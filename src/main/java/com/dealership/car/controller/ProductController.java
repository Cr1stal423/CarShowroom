package com.dealership.car.controller;

import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.TechnicalDataRepository;
import com.fasterxml.jackson.annotation.OptBoolean;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ModelAndView showForm(){
        ModelAndView modelAndView = new ModelAndView("addProduct.html");
        modelAndView.addObject("product", new Product());
        modelAndView.addObject("technicalData" , new TechnicalData());
        return modelAndView;
    }
}
