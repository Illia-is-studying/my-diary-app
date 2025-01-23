package com.example.mydiaryapp.Helpers;

import com.example.mydiaryapp.Models.MyAppUser;
import com.example.mydiaryapp.Services.MyAppUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthenticationHelper {
    public static List<MyAppUser> getCurrentUserInListByAuthentication(Authentication authentication,
                                                                       MyAppUserService myAppUserService) {
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String currentUsername = userDetails.getUsername();
        Optional<MyAppUser> currentUser = myAppUserService.findByUsername(currentUsername);

        List<MyAppUser> myAppUsers = new ArrayList<>();
        currentUser.ifPresent(myAppUsers::add);

        return myAppUsers;
    }

    public static Long getCurrentUserId(HttpSession httpSession, MyAppUserService myAppUserService) {
        Long userId = (Long) httpSession.getAttribute("userId");

        if (userId == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<MyAppUser> myAppUsers = getCurrentUserInListByAuthentication(authentication, myAppUserService);
            userId = myAppUsers.get(0).getId();
            httpSession.setAttribute("userId", userId);
        }

        return userId;
    }
}
