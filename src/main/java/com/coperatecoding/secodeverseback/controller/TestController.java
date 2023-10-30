package com.coperatecoding.secodeverseback.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @RequestMapping(value = "/test/hello")
    @ResponseBody
    public String helloRuckus(Model model) {
        return "Hello Ruckus";
    }

}