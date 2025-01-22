package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Models.MyAppUser;
import com.example.mydiaryapp.Models.UserSettingsModel;
import com.example.mydiaryapp.Repo.IUserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSettingsService {
    private static final int DAYS_BY_DEFAULT = 3;
    private final IUserSettingsRepository userSettingsRepository;

    @Autowired
    public UserSettingsService(IUserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    public UserSettingsModel saveUserSettings(UserSettingsModel settings) {
        return userSettingsRepository.save(settings);
    }

    public UserSettingsModel getUserSettings(MyAppUser user) {
        Optional<UserSettingsModel> userSettingsModel = userSettingsRepository.findByMyAppUserId(user.getId());

        return userSettingsModel.orElseGet(() -> addNewUserSettings(user));
    }

    public UserSettingsModel resetToDefaults(UserSettingsModel model) {
        model.setCartStorageDays(DAYS_BY_DEFAULT);
        return saveUserSettings(model);
    }

    private UserSettingsModel addNewUserSettings(MyAppUser user) {
        UserSettingsModel defaultSettings = new UserSettingsModel();
        defaultSettings.setMyAppUser(user);
        defaultSettings.setCartStorageDays(DAYS_BY_DEFAULT);
        return userSettingsRepository.save(defaultSettings);
    }
}
