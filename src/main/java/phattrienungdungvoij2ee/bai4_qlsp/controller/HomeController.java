package phattrienungdungvoij2ee.bai4_qlsp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import phattrienungdungvoij2ee.bai4_qlsp.model.Account;
import phattrienungdungvoij2ee.bai4_qlsp.model.Role;
import phattrienungdungvoij2ee.bai4_qlsp.repository.AccountRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MongoTemplate mongoTemplate;

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

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {
        // Validate
        if (username == null || username.trim().length() < 3) {
            model.addAttribute("error", "Username phải có ít nhất 3 ký tự!");
            return "register";
        }
        if (password == null || password.length() < 6) {
            model.addAttribute("error", "Password phải có ít nhất 6 ký tự!");
            return "register";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "register";
        }

        // Check if username exists
        if (accountRepository.findByLoginName(username.trim()).isPresent()) {
            model.addAttribute("error", "Username đã tồn tại!");
            return "register";
        }

        // Get or create ROLE_USER
        Role userRole = mongoTemplate.findOne(
                Query.query(Criteria.where("name").is("ROLE_USER")),
                Role.class);
        if (userRole == null) {
            userRole = new Role();
            userRole.setId("1");
            userRole.setName("ROLE_USER");
            mongoTemplate.save(userRole);
        }

        // Create account
        Account newAccount = new Account();
        newAccount.setLogin_name(username.trim());
        newAccount.setPassword(passwordEncoder.encode(password));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        newAccount.setRoles(roles);
        accountRepository.save(newAccount);

        model.addAttribute("success", "Đăng ký thành công! Bạn có thể đăng nhập.");
        return "register";
    }
}
