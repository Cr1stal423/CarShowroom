package com.dealership.car.controller;

import com.dealership.car.service.QueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/custom")
@AllArgsConstructor
public class CustomController {

    private final StringHttpMessageConverter stringHttpMessageConverter;
    private QueryService queryService;

    @GetMapping("/query")
    public String customQuery(Model model){
        return "customQuery.html";
    }
    @PostMapping("/processQuery")
    public String processQuery(Model model, @RequestParam(value = "query",required = true)String query){
        String result = queryService.executeQuery(query).toString();
        model.addAttribute("result",result);
        return "customQuery.html";
    }
}
