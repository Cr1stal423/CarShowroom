package com.dealership.car.controller;

import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.TechnicalDataRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/technicalData")
public class TechnicalController {

    private ProductRepository productRepository;

    private TechnicalDataRepository technicalDataRepository;

    @GetMapping("/forProduct")
    public String technicalDataForProduct(@RequestParam int id ,
                                          Model model){
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()){
            List<TechnicalData> technicalData =  new ArrayList<>();
            technicalData.add(product.get().getTechnicalData());
            model.addAttribute("technicalData", technicalData);
        } else {
            //TODO add exception handler
            System.out.println(String.format("Error: not found product with id %s", id));
        }
        return "technicalData.html";
    }
    @GetMapping("/showProduct")
    public String showProduct(@RequestParam("id") int id, Model model,
                              HttpSession httpSession){
        Optional<TechnicalData> technicalData = technicalDataRepository.findById(id);
        httpSession.setAttribute("tectechnicalData", technicalData.get());
        return "redirect:/product/showProductById";
    }

}
