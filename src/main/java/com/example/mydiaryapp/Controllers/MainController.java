package com.example.mydiaryapp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
    @GetMapping("/")
    public String home(Model model) {
        return "main/home";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "main/login";
    }
}