package com.dealership.car.controller;

import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.TechnicalDataRepository;
import com.dealership.car.service.ProductService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping(value = "/technicalData")
public class TechnicalController {

    private ProductService productService;

    private ProductRepository productRepository;

    private TechnicalDataRepository technicalDataRepository;

    @GetMapping("/forProduct")
    public String technicalDataForProduct(@RequestParam int id ,
                                          Model model, HttpSession httpSession){
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()){
//            List<TechnicalData> technicalData =  new ArrayList<>();
//            technicalData.add(product.get().getTechnicalData());
            TechnicalData technicalData = product.get().getTechnicalData();
            model.addAttribute("technicalData", technicalData);
            httpSession.setAttribute("technicalData", technicalData);

        } else {
            //TODO add exception handler
            System.out.println(String.format("Error: not found product with id %s", id));
        }
        return "technicalData.html";
    }
    @GetMapping("/showProduct")
    public String showProduct(@RequestParam("id") int id, Model model){
        Optional<TechnicalData> technicalData = technicalDataRepository.findById(id);
        return "redirect:/product/showProductById";
    }

    @PostMapping(value = "/updateTechnicalData")
    public String updateData(@RequestParam(value = "technicalId") Integer technicalId,
                             @RequestParam(value = "bodyType")TechnicalData.BodyType bodyType,
                             @RequestParam(value = "doors")Integer doors,
                             @RequestParam(value = "seats")Integer seats, @RequestParam(value = "engineType")TechnicalData.EngineType engineType,
                             @RequestParam(value = "enginePlacement")TechnicalData.EnginePlacement enginePlacement, @RequestParam(value = "engineCapacity")Double engineCapacity,
                             Model model, HttpSession httpSession){
        boolean isUpdated = productService.updateTechData(technicalId,bodyType,doors,seats,engineType,enginePlacement,engineCapacity);

        if (isUpdated){
            TechnicalData technicalData = (TechnicalData) httpSession.getAttribute("technicalData");
            int id = technicalData.getTechnicalId();
            return String.format("redirect:/technicalData/forProduct?id=%s",id);
        } else {
            return "redirect:/dashboard";
        }
    }

}
