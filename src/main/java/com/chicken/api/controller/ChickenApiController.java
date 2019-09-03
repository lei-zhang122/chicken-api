package com.chicken.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp")
public class ChickenApiController {
    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {

        return "123";
    }
}
