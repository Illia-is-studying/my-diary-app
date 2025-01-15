package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Models.MyAppUser;
import com.example.mydiaryapp.Repo.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyAppUserService implements UserDetailsService {
    private final MyAppUserRepository myAppUserRepository;

    @Autowired
    public MyAppUserService(MyAppUserRepository myAppUserRepository) {
        this.myAppUserRepository = myAppUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyAppUser> user = myAppUserRepository.findByUsername(username);

        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public Optional<MyAppUser> findByUsername(String username) {
        return myAppUserRepository.findByUsername(username);
    }

    public MyAppUser save(MyAppUser myAppUser) {
        return myAppUserRepository.save(myAppUser);
    }

    public boolean existsByUsername(String username) {
        return myAppUserRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return myAppUserRepository.existsByEmail(email);
    }

    public List<MyAppUser> getCurrentUserInListByAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String currentUsername = userDetails.getUsername();
        Optional<MyAppUser> currentUser = findByUsername(currentUsername);

        List<MyAppUser> myAppUsers = new ArrayList<>();
        currentUser.ifPresent(myAppUsers::add);

        return myAppUsers;
    }
}
