package com.ngarcia.springboot.springmvc.app.controllers;

import com.ngarcia.springboot.springmvc.app.entities.User;
import com.ngarcia.springboot.springmvc.app.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping({"/view"})
    public String viewMethod(Model model) {
        model.addAttribute("title","Hola mundo spring boot!!!");
        model.addAttribute("message","Probando Spring");
        model.addAttribute("user", new User("Gael","Garcia"));
        return "view"; //en templates
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("title","Listado de usuarios");
        model.addAttribute("users",service.findAll());
        return "list"; //en templates
    }

    @GetMapping({"/form"})
    public String form(Model model) {
        model.addAttribute("title","Crear usuario");
        model.addAttribute("user", new User());
        return "form"; //en templates
    }

    @GetMapping({"/form/{id}"})
    public String form(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        model.addAttribute("title","Editar usuario");
        Optional<User> opt = service.findById(id);
        if(opt.isPresent()) {
            model.addAttribute("user", opt.get());
            return "form"; //en templates
        } else {
            redirect.addFlashAttribute("error", "El usuario no existe");
            return "redirect:/users"; //redirecciona a la lista de usuarios
        }
    }

    @PostMapping
    //@ModelAttribute se utiliza para indicar que clase es, si el nombre coincide con la
    //clase, no es necesario indicarlo
    //public String form(@ModelAttribute(name="XXX") User user, Model model) {
    public String form(User user, Model model, RedirectAttributes redirect) {
        //antes de save
        String message = (user.getId() != null) ? "Usuario actualizado correctamente" : "Usuario creado correctamente";
        service.save(user);
        redirect.addFlashAttribute("success", message);
        return "redirect:/users"; //redirecciona a la lista de usuarios
    }

    @GetMapping({"/delete/{id}"})
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        Optional<User> opt = service.findById(id);
        if(opt.isPresent()) {
            redirect.addFlashAttribute("success", "Usuario eliminado correctamente");
            service.deleteById(id);
            return "redirect:/users"; //redirecciona a la lista de usuarios
        }
        redirect.addFlashAttribute("error", "El usuario no existe");
        return "redirect:/users"; //redirecciona a la lista de usuarios
    }

}
