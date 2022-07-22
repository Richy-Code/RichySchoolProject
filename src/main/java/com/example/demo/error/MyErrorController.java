package com.example.demo.error;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
@ControllerAdvice
public class MyErrorController{

    @ExceptionHandler(value = AppExceptions.class)
    private String errorHandler(Model model, AppExceptions exceptions){
        model.addAttribute("message",exceptions.getErrorMessage());
        return "error_page";
    }
}
