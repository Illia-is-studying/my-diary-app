package com.example.mydiaryapp.Repo;

import com.example.mydiaryapp.Models.UserSettingsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserSettingsRepository extends JpaRepository<UserSettingsModel, Long> {
    Optional<UserSettingsModel> findByMyAppUserId(Long userId);
}