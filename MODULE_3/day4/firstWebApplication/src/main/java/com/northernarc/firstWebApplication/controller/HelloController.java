package com.northernarc.firstWebApplication.controller;=

import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/jello")
    public String hello(){
        return "hello world";
    }
    @RequestMapping("")
    public String welcome(){
        return "Welcome to the java world";
    }
    @RequestMapping("/bye")
    public String busy(){
        return "I am busy right now";
    }


}
