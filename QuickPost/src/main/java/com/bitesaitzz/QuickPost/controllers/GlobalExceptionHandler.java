package com.bitesaitzz.QuickPost.controllers;


import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public String handleMissingParams(MissingServletRequestParameterException ex, Model model) {
//        model.addAttribute("error", "Required parameter is missing: " + ex.getParameterName());
//        return "error";
//    }
}
