package phattrienungdungvoij2ee.bai4_qlsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "home";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
