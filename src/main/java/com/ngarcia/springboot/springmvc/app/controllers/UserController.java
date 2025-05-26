package com.ngarcia.springboot.springmvc.app.controllers;

import com.ngarcia.springboot.springmvc.app.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class UserController {

    @GetMapping({"/view","/"})
    public String viewMethod(Model model) {
        model.addAttribute("title","Hola mundo spring boot!!!");
        model.addAttribute("message","Probando Spring");
        model.addAttribute("user", new User("Gael","Garcia"));
        return "view"; //en templates
    }
}
