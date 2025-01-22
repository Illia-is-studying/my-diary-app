package com.example.mydiaryapp.Controllers.Advices;

import com.example.mydiaryapp.Models.MyAppUser;
import com.example.mydiaryapp.Models.UserSettingsModel;
import com.example.mydiaryapp.Services.MyAppUserService;
import com.example.mydiaryapp.Services.UserSettingsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class UserSettingsAdvice {
    private final UserSettingsService userSettingsService;
    private final MyAppUserService myAppUserService;

    @Autowired
    public UserSettingsAdvice(UserSettingsService userSettingsService, MyAppUserService myAppUserService) {
        this.userSettingsService = userSettingsService;
        this.myAppUserService = myAppUserService;
    }

    @ModelAttribute("userSettings")
    public UserSettingsModel addUserSettingsToModel(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<MyAppUser> myAppUsers = myAppUserService.getCurrentUserInListByAuthentication(authentication);

            return userSettingsService.getUserSettings(myAppUsers.get(0));
        }

        UserSettingsModel defaultSettings = new UserSettingsModel();
        defaultSettings.setCartStorageDays(3);
        return defaultSettings;
    }
}
