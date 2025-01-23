package com.example.mydiaryapp.Controllers;

import com.example.mydiaryapp.Helpers.AuthenticationHelper;
import com.example.mydiaryapp.Models.MyAppUser;
import com.example.mydiaryapp.Models.UserSettingsModel;
import com.example.mydiaryapp.Services.MyAppUserService;
import com.example.mydiaryapp.Services.UserSettingsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/settings")
public class UserSettingsController {
    private final UserSettingsService userSettingsService;
    private final MyAppUserService myAppUserService;

    @Autowired
    public UserSettingsController(UserSettingsService userSettingsService, MyAppUserService myAppUserService) {
        this.userSettingsService = userSettingsService;
        this.myAppUserService = myAppUserService;
    }

    @PostMapping("/save")
    public String saveSettings(
            @ModelAttribute("userSettings") UserSettingsModel userSettings,
            HttpSession session,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null && userId.equals(userSettings.getMyAppUser().getId())) {
            userSettingsService.saveUserSettings(userSettings);
            redirectAttributes.addFlashAttribute("message", "Settings saved successfully!");
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @GetMapping("/reset")
    public String resetSettings(
            HttpSession session,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<MyAppUser> myAppUsers = AuthenticationHelper
                    .getCurrentUserInListByAuthentication(authentication, myAppUserService);

            UserSettingsModel userSettings = userSettingsService.getUserSettings(myAppUsers.get(0));
            userSettingsService.resetToDefaults(userSettings);

            redirectAttributes.addFlashAttribute("message", "Settings reset to default!");
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
