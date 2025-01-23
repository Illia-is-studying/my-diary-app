package com.example.mydiaryapp.Controllers;

import com.example.mydiaryapp.Enums.SortingType;
import com.example.mydiaryapp.Helpers.AuthenticationHelper;
import com.example.mydiaryapp.Helpers.DiariesControllerHelper;
import com.example.mydiaryapp.Models.DiaryModel;
import com.example.mydiaryapp.Models.ViewModels.DiarySortingViewModel;
import com.example.mydiaryapp.Models.ViewModels.DiaryViewModel;
import com.example.mydiaryapp.Services.DiaryService;
import com.example.mydiaryapp.Services.FragmentService;
import com.example.mydiaryapp.Services.MyAppUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DiariesController {
    public final MyAppUserService myAppUserService;
    public final DiariesControllerHelper helper;

    @Autowired
    public DiariesController(DiaryService diaryService, FragmentService fragmentService,
                             MyAppUserService myAppUserService) {
        this.myAppUserService = myAppUserService;

        helper = new DiariesControllerHelper(diaryService, fragmentService);
    }

    @GetMapping("/diaries")
    public String diaries(HttpSession httpSession, Model model) {
        Long userId = AuthenticationHelper.getCurrentUserId(httpSession, myAppUserService);

        DiarySortingViewModel diarySortingViewModel =
                (DiarySortingViewModel) httpSession.getAttribute("diarySortingViewModel");

        if (diarySortingViewModel == null) {
            diarySortingViewModel =
                    new DiarySortingViewModel(SortingType.LATEST, "", "", null, null);

            httpSession.setAttribute("diarySortingViewModel", diarySortingViewModel);
        }

        List<DiaryModel> diaryModels = helper.getSortingDiaries(diarySortingViewModel, userId);
        List<DiaryViewModel> diaryViewModels = helper.convertModelsToViewModels(diaryModels);

        model.addAttribute("SortingType", SortingType.class);
        model.addAttribute("diaryViewModels", diaryViewModels);
        model.addAttribute("diarySortingViewModel", diarySortingViewModel);

        return "user-diaries/diaries";
    }

    @PostMapping("/diaries/search")
    public String searchDiaries(
            @RequestParam(name = "radio-sorting-type", required = false) SortingType sortingTypeValue,
            @RequestParam(name = "from-date", required = false) LocalDateTime fromDate,
            @RequestParam(name = "to-date", required = false) LocalDateTime toDate,
            @RequestParam(name = "title-piece", required = false) String titlePiece,
            @RequestParam(name = "tags-line", required = false) String tagsLine,
            HttpSession httpSession,
            Model model) {
        if (sortingTypeValue == null) {
            sortingTypeValue = SortingType.LATEST;
        }

        DiarySortingViewModel diarySortingViewModel = new DiarySortingViewModel(
                sortingTypeValue, titlePiece, tagsLine, fromDate, toDate);

        httpSession.setAttribute("diarySortingViewModel", diarySortingViewModel);

        return "redirect:/diaries";
    }
}
