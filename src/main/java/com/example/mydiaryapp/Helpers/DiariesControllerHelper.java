package com.example.mydiaryapp.Helpers;

import com.example.mydiaryapp.Enums.FragmentType;
import com.example.mydiaryapp.Enums.SortingType;
import com.example.mydiaryapp.Models.DiaryModel;
import com.example.mydiaryapp.Models.FragmentModel;
import com.example.mydiaryapp.Models.TagModel;
import com.example.mydiaryapp.Models.ViewModels.DiarySortingViewModel;
import com.example.mydiaryapp.Models.ViewModels.DiaryViewModel;
import com.example.mydiaryapp.Services.DiaryService;
import com.example.mydiaryapp.Services.FragmentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DiariesControllerHelper {
    public final DiaryService diaryService;
    public final FragmentService fragmentService;

    public DiariesControllerHelper(DiaryService diaryService, FragmentService fragmentService) {
        this.diaryService = diaryService;
        this.fragmentService = fragmentService;
    }

    public List<DiaryViewModel> convertModelsToViewModels(List<DiaryModel> diaryModels) {
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
            String timeAgo = DateTimeHelper.getTimeDifference(lastEditDate, LocalDateTime.now(), " ago");

            diaryViewModel.setId(diaryModel.getId());
            diaryViewModel.setTitle(diaryModel.getTitle());
            diaryViewModel.setLastEditDate(lastEditDate);
            diaryViewModel.setCountTimeAgo(timeAgo);
            diaryViewModel.setShortDescription(shortDescription);
            diaryViewModels.add(diaryViewModel);
        }

        return diaryViewModels;
    }

    public List<DiaryModel> getSortingDiaries(DiarySortingViewModel diarySortingViewModel,
                                              Long userId) {
        List<DiaryModel> diaryModels = null;

        if (diarySortingViewModel.getSortingType().equals(SortingType.LATEST)) {
            diaryModels = diaryService.findByUserIdOrderByLastEditDateDesc(userId);
        } else if (diarySortingViewModel.getSortingType().equals(SortingType.OLDEST)) {
            diaryModels = diaryService.findByUserIdOrderByLastEditDateAsc(userId);
        } else if (diarySortingViewModel.getSortingType().equals(SortingType.ABC)) {
            diaryModels = diaryService.findByUserIdOrderByTitleAsc(userId);
        }

        diaryModels = diaryModels.stream()
                .filter(dm -> dm.getPendingDeletionModels().isEmpty())
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

        return diaryModels;
    }
}
