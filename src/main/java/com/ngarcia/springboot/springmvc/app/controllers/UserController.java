package com.ngarcia.springboot.springmvc.app.controllers;

import com.ngarcia.springboot.springmvc.app.entities.User;
import com.ngarcia.springboot.springmvc.app.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/users")
@SessionAttributes({"user"})
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping({"/view"})
    public String viewMethod(Model model) {
        model.addAttribute("title","Hello world spring boot!!!");
        model.addAttribute("message","Trying Spring");
        model.addAttribute("user", new User("Gael","Garcia"));
        return "view"; //en templates
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("title","Users");
        model.addAttribute("users",service.findAll());
        return "list"; //en templates
    }

    @GetMapping({"/form"})
    public String form(Model model) {
        model.addAttribute("title","Create user");
        model.addAttribute("user", new User());
        return "form"; //en templates
    }

    @GetMapping({"/form/{id}"})
    public String form(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        model.addAttribute("title","Edit user");
        Optional<User> opt = service.findById(id);
        if(opt.isPresent()) {
            model.addAttribute("user", opt.get());
            return "form"; //en templates
        } else {
            redirect.addFlashAttribute("error", "User doesn't exist");
            return "redirect:/users"; //redirecciona a la lista de usuarios
        }
    }

    @PostMapping
    //@ModelAttribute se utiliza para indicar que clase es, si el nombre coincide con la
    //clase, no es necesario indicarlo
    //BindingResult debe ir inmediatamente después del objeto que se va a validar
    //public String form(@ModelAttribute(name="XXX") User user, Model model) {
    public String form(@Valid User user, BindingResult result, Model model, RedirectAttributes redirect, SessionStatus status) {

        if(result.hasErrors()) {
            model.addAttribute("title","Validation errors");
            return "form"; //en templates
        }

        //antes de save
        String message = (user.getId() != null && user.getId() > 0) ? "User was updated" : "User was created";
        service.save(user);
        status.setComplete(); //indica que la sesión se ha completado, se elimina el objeto de la sesión
        redirect.addFlashAttribute("success", message);
        return "redirect:/users"; //redirecciona a la lista de usuarios
    }

    @GetMapping({"/delete/{id}"})
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        Optional<User> opt = service.findById(id);
        if(opt.isPresent()) {
            redirect.addFlashAttribute("success", "User was deleted");
            service.deleteById(id);
            return "redirect:/users"; //redirecciona a la lista de usuarios
        }
        redirect.addFlashAttribute("error", "User doesn't exist");
        return "redirect:/users"; //redirecciona a la lista de usuarios
    }

}
