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

/**
 * CustomController handles HTTP requests for custom queries.
 *
 * This controller provides endpoints for displaying a custom query page
 * and processing custom queries. It relies on the QueryService to handle
 * the execution and processing of SQL queries.
 */
@Controller
@RequestMapping("/custom")
@AllArgsConstructor
public class CustomController {

    private QueryService queryService;

    /**
     * Handles GET requests to display the custom query page.
     *
     * @param model the Model object used to pass attributes to the view
     * @return the name of the view template to be rendered, "customQuery.html"
     */
    @GetMapping("/query")
    public String customQuery(Model model){
        return "customQuery.html";
    }
    /**
     * Processes a custom query submitted via HTTP POST request.
     *
     * This method executes the provided SQL query using the QueryService,
     * adds the result to the model, and returns the name of the view to be rendered.
     *
     * @param model the model to which the result will be added
     * @param query the SQL query string to be executed; must be present
     * @return the name of the view to be rendered, in this case "customQuery.html"
     */
    @PostMapping("/processQuery")
    public String processQuery(Model model, @RequestParam(value = "query",required = true)String query){
        String result = queryService.executeQuery(query).toString();
        model.addAttribute("result",result);
        return "customQuery.html";
    }
}
