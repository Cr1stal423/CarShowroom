package com.dealership.car.controller;

import com.dealership.car.dynamic.DynamicFieldValue;
import com.dealership.car.model.Product;
import com.dealership.car.model.TechnicalData;
import com.dealership.car.repository.ProductRepository;
import com.dealership.car.repository.TechnicalDataRepository;
import com.dealership.car.service.DynamicFieldValueService;
import com.dealership.car.service.ProductService;
import com.dealership.car.service.TechnicalDataService;
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
import java.util.Map;
import java.util.Optional;

/**
 * A Spring MVC controller that handles requests related to technical data of products.
 * This controller provides endpoints to view and update technical data associated with products.
 */
@Controller
@AllArgsConstructor
@RequestMapping(value = "/technicalData")
public class TechnicalController {

    private final DynamicFieldValueService dynamicFieldValueService;
    private final TechnicalDataService technicalDataService;
    private ProductService productService;

    private ProductRepository productRepository;

    private TechnicalDataRepository technicalDataRepository;

    /**
     * Retrieves the technical data for a specific product, including dynamic fields,
     * and adds it to the model and HTTP session.
     *
     * @param id The ID of the product to retrieve technical data for.
     * @param model The Spring model to which the technical data map is added.
     * @param httpSession The HTTP session in which the technical data is stored.
     * @return The name of the view to render, in this case "technicalData.html".
     */
    @GetMapping("/forProduct")
    public String technicalDataForProduct(@RequestParam int id ,
                                          Model model, HttpSession httpSession){
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()){
            TechnicalData technicalData = product.get().getTechnicalData();
            List<TechnicalData> technicalDataList =  new ArrayList<>();
            technicalDataList.add(technicalData);
            Map<TechnicalData,List<DynamicFieldValue>> technicalMap = technicalDataService.getDynamicFieldsForAllTechnicalData(technicalDataList);
            model.addAttribute("technicalDataMap", technicalMap);
            httpSession.setAttribute("technicalData", technicalData);

        } else {
            //TODO add exception handler
            System.out.println(String.format("Error: not found product with id %s", id));
        }
        return "technicalData.html";
    }
    /**
     * Displays the product by its technical data id.
     *
     * @param id The id of the technical data.
     * @param model The model to hold the technical data.
     * @return The URL to redirect to the product's detailed page.
     */
    @GetMapping("/showProduct")
    public String showProduct(@RequestParam("id") int id, Model model){
        Optional<TechnicalData> technicalData = technicalDataRepository.findById(id);
        return "redirect:/product/showProductById";
    }

    /**
     * Updates the technical data of a product based on the provided parameters.
     *
     * @param technicalId The ID of the technical data to update.
     * @param bodyType The body type of the technical data.
     * @param doors The number of doors of the technical data.
     * @param seats The number of seats of the technical data.
     * @param engineType The engine type of the technical data.
     * @param enginePlacement The placement of the engine in the technical data.
     * @param engineCapacity The capacity of the engine in the technical data.
     * @param model The Spring Model object.
     * @param httpSession The HTTP session object.
     * @return A redirect string to the updated technical data view if successful, otherwise to the dashboard.
     */
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
