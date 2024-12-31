package com.example.mydiaryapp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EditViewController {
    @GetMapping("/edit-view")
    public String viewEdit(@RequestParam(value = "tab", defaultValue = "edit") String tab, Model model) {
        model.addAttribute("activeTab", tab);
        return "user-diaries/edit-view";
    }
}
