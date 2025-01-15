package com.example.mydiaryapp.Controllers;

import com.example.mydiaryapp.Enums.FragmentType;
import com.example.mydiaryapp.Models.DiaryModel;
import com.example.mydiaryapp.Models.FragmentModel;
import com.example.mydiaryapp.Models.MyAppUser;
import com.example.mydiaryapp.Models.ViewModels.DiaryViewModel;
import com.example.mydiaryapp.Services.DiaryService;
import com.example.mydiaryapp.Services.FragmentService;
import com.example.mydiaryapp.Services.MyAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DiariesController {
    public final DiaryService diaryService;
    public final FragmentService fragmentService;
    public final MyAppUserService myAppUserService;

    @Autowired
    public DiariesController(DiaryService diaryService, FragmentService fragmentService,
                             MyAppUserService myAppUserService) {
        this.diaryService = diaryService;
        this.fragmentService = fragmentService;
        this.myAppUserService = myAppUserService;
    }

    @GetMapping("/diaries")
    public String diaries(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<MyAppUser> myAppUsers = myAppUserService.getCurrentUserInListByAuthentication(authentication);

        List<DiaryModel> diaryModels = diaryService.findAllByMyAppUserId(myAppUsers.get(0).getId());
        List<DiaryViewModel> diaryViewModels = new ArrayList<>();

        for (DiaryModel diaryModel : diaryModels) {
            List<FragmentModel> fragmentModels = fragmentService.findAllByDiaryId(diaryModel.getId());
            List<FragmentModel> textFragments = fragmentModels.stream()
                    .filter(fm -> fm.getFragmentType() == FragmentType.TEXT_FRAGMENT
                            && fm.getTextFragment() != null && !fm.getTextFragment().isEmpty())
                    .toList();
            String shortDescription = "This diary is missing text.";
            if (!textFragments.isEmpty()) {
                shortDescription = textFragments.get(0).getTextFragment();

                if (shortDescription.length() > 50) {
                    shortDescription = shortDescription.substring(0, 50) + "...";
                }
            }

            DiaryViewModel diaryViewModel = new DiaryViewModel();

            LocalDateTime lastEditDate = diaryModel.getLastEditDate();
            long daysAgo = ChronoUnit.DAYS.between(lastEditDate, LocalDateTime.now());

            diaryViewModel.setId(diaryModel.getId());
            diaryViewModel.setTitle(diaryModel.getTitle());
            diaryViewModel.setLastEditDate(lastEditDate);
            diaryViewModel.setCountDaysAgo(daysAgo);
            diaryViewModel.setShortDescription(shortDescription);
            diaryViewModels.add(diaryViewModel);
        }

        model.addAttribute("diaryViewModels", diaryViewModels);

        return "user-diaries/diaries";
    }


}
