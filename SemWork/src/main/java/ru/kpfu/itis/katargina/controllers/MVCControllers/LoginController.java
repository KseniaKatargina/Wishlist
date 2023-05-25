package ru.kpfu.itis.katargina.controllers.MVCControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @PostMapping("/login")
    public String processLoginForm() {
        return "loginForm";
    }

    @GetMapping({"/login"})
    public String login(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверный email или пароль");
        }
        return "loginForm";
    }
}
