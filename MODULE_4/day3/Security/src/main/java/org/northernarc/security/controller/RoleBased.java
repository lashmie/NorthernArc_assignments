package org.northernarc.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/hello1")
public class RoleBased {
    @GetMapping
    public String page(){
        return "welcome nga!!!";
    }
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String hellouser(){
        return "Hello user , you can only search product and place order!!";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String helloadmin(){
        return "Hello admin , you can add product and delete product!!";
    }
    @GetMapping("/loanunderwritter")
    @PreAuthorize("hasRole('ADMIN')")
    public String writter(){
        return "u can approve the loans..!!";
    }
}
