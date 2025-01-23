package com.example.mydiaryapp.Controllers.Advices;

import com.example.mydiaryapp.Helpers.AuthenticationHelper;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return new UserSettingsModel(0L, null, 3);
        }

        List<MyAppUser> myAppUsers = AuthenticationHelper
                .getCurrentUserInListByAuthentication(authentication, myAppUserService);

        return userSettingsService.getUserSettings(myAppUsers.get(0));
    }
}
