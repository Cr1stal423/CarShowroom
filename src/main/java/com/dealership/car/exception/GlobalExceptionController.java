package com.dealership.car.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception ex){
        ModelAndView modelAndView = new ModelAndView("error.html");
        modelAndView.addObject("exception", ex);
        return modelAndView;
    }
}