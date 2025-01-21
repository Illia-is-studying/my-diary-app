package com.example.mydiaryapp.Controllers;

import com.example.mydiaryapp.Enums.FragmentType;
import com.example.mydiaryapp.Enums.SortingType;
import com.example.mydiaryapp.Helpers.DateTimeHelper;
import com.example.mydiaryapp.Models.DiaryModel;
import com.example.mydiaryapp.Models.FragmentModel;
import com.example.mydiaryapp.Models.MyAppUser;
import com.example.mydiaryapp.Models.TagModel;
import com.example.mydiaryapp.Models.ViewModels.DiarySortingViewModel;
import com.example.mydiaryapp.Models.ViewModels.DiaryViewModel;
import com.example.mydiaryapp.Services.DiaryService;
import com.example.mydiaryapp.Services.FragmentService;
import com.example.mydiaryapp.Services.MyAppUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
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
    public String diaries(HttpSession httpSession, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<MyAppUser> myAppUsers = myAppUserService.getCurrentUserInListByAuthentication(authentication);
        Long userId = myAppUsers.get(0).getId();

        List<DiaryModel> diaryModels = null;

        DiarySortingViewModel diarySortingViewModel =
                (DiarySortingViewModel) httpSession.getAttribute("diarySortingViewModel");

        if (diarySortingViewModel == null) {
            DiarySortingViewModel newDiarySortingViewModel =
                    new DiarySortingViewModel(SortingType.LATEST, "", "", null, null);

            httpSession.setAttribute("diarySortingViewModel", newDiarySortingViewModel);

            diaryModels = diaryService.findByUserIdOrderByLastEditDateDesc(userId);

            model.addAttribute("diarySortingViewModel", newDiarySortingViewModel);
        } else {
            if (diarySortingViewModel.getSortingType().equals(SortingType.LATEST)) {
                diaryModels = diaryService.findByUserIdOrderByLastEditDateDesc(userId);
            } else if (diarySortingViewModel.getSortingType().equals(SortingType.OLDEST)) {
                diaryModels = diaryService.findByUserIdOrderByLastEditDateAsc(userId);
            } else if (diarySortingViewModel.getSortingType().equals(SortingType.ABC)) {
                diaryModels = diaryService.findByUserIdOrderByTitleAsc(userId);
            }

            diaryModels = diaryModels.stream()
                    .filter(dm -> DateTimeHelper.isDateBetween(dm.getLastEditDate(),
                            diarySortingViewModel.getFromDate(),
                            diarySortingViewModel.getToDate()))
                    .filter(dm -> diarySortingViewModel.getTitlePiece().isEmpty() ||
                            dm.getTitle().toLowerCase()
                                    .contains(diarySortingViewModel.getTitlePiece().toLowerCase()))
                    .toList();
            String tagsLine = diarySortingViewModel.getTagsLine();

            if (tagsLine != null && !tagsLine.isEmpty()) {
                List<String> tagList = List.of(tagsLine.split(" "));

                diaryModels = diaryModels.stream()
                        .filter(dm -> tagList.stream()
                                .allMatch(tagName -> dm.getTags().stream()
                                        .map(TagModel::getContent)
                                        .anyMatch(tagName::equals)
                                )
                        )
                        .toList();
            }

            model.addAttribute("diarySortingViewModel", diarySortingViewModel);
        }

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
            String timeAgo = DateTimeHelper.getTimeDifference(lastEditDate);

            diaryViewModel.setId(diaryModel.getId());
            diaryViewModel.setTitle(diaryModel.getTitle());
            diaryViewModel.setLastEditDate(lastEditDate);
            diaryViewModel.setCountTimeAgo(timeAgo);
            diaryViewModel.setShortDescription(shortDescription);
            diaryViewModels.add(diaryViewModel);
        }

        model.addAttribute("SortingType", SortingType.class);
        model.addAttribute("diaryViewModels", diaryViewModels);

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
