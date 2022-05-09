package com.bzavoevanyy.controllers;

import lombok.val;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorsController implements ErrorController {

    @RequestMapping("/error")
    public String renderErrorPage(HttpServletRequest httpRequest, Model model) {
        val errorCode = Integer
                .parseInt(httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());
        if (errorCode == 404) {
            model.addAttribute("errorMsg", "Http Error Code: 404. Resource not found");
        } else {
            model.addAttribute("errorMsg", "Server error. Bad request or resource not found");
        }
        return "errors/errors";
    }
}
