package com.example.mydiaryapp.Controllers;

import com.example.mydiaryapp.Models.DTOs.UserDto;
import com.example.mydiaryapp.Models.MyAppUser;
import com.example.mydiaryapp.Services.MyAppUserService;
import com.example.mydiaryapp.Services.ValidationErrorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {
    private final MyAppUserService myAppUserService;
    private final ValidationErrorsService validationErrorsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(MyAppUserService myAppUserService,
                                  ValidationErrorsService validationErrorsService,
                                  PasswordEncoder passwordEncoder) {
        this.myAppUserService = myAppUserService;
        this.validationErrorsService = validationErrorsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registration(Model model) {
        validationErrorsService.setValidationErrors("username", "email", "password", "matchingPassword");
        model.addAttribute("validErrors", validationErrorsService.getValidationErrors());
        model.addAttribute("userDto", new UserDto());
        return "main/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") UserDto userDto, Model model) {
        if (validationErrorsService.validateField(
                userDto.getUsername().length() < 3,
                "username",
                "Username must be at least 3 characters long...")) {
            validationErrorsService.validateField(
                    myAppUserService.existsByUsername(userDto.getUsername()),
                    "username",
                    "This username is already taken..."
            );
        }

        validationErrorsService.validateField(
                myAppUserService.existsByEmail(userDto.getEmail()),
                "email",
                "This email is already taken..."
        );

        if (validationErrorsService.validateField(
                userDto.getPassword().length() < 8,
                "password",
                "Password must be at least 8 characters long...")) {
            validationErrorsService.validateField(
                    !userDto.getPassword().equals(userDto.getMatchingPassword()),
                    "matchingPassword",
                    "Passwords do not match...");
        }

        if (!validationErrorsService.isErrors()) {
            MyAppUser user = new MyAppUser();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setEmail(userDto.getEmail());

            myAppUserService.save(user);

            return "redirect:main/login";
        }

        validationErrorsService.setIsErrors(false);
        model.addAttribute("userDto", userDto);
        model.addAttribute("validErrors", validationErrorsService.getValidationErrors());

        return "main/register";
    }
}
