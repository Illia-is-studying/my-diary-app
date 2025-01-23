package com.example.mydiaryapp.Controllers;

import com.example.mydiaryapp.Enums.FragmentType;
import com.example.mydiaryapp.Helpers.EditViewControllerHelper;
import com.example.mydiaryapp.Models.DiaryModel;
import com.example.mydiaryapp.Models.FragmentModel;
import com.example.mydiaryapp.Models.PendingDeletionModel;
import com.example.mydiaryapp.Models.TagModel;
import com.example.mydiaryapp.Models.ViewModels.DiaryViewModel;
import com.example.mydiaryapp.Models.ViewModels.FragmentViewModel;
import com.example.mydiaryapp.Services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/edit-view")
public class EditViewController {
    public final EditViewControllerHelper helper;

    @Autowired
    public EditViewController(DiaryService diaryService, FragmentService fragmentService,
                              MyAppUserService myAppUserService, TagService tagService,
                              PendingDeletionService pendingDeletionService, UserSettingsService userSettingsService) {
        this.helper = new EditViewControllerHelper(diaryService, fragmentService, myAppUserService, tagService,
                pendingDeletionService, userSettingsService);
    }

    @GetMapping("/new-diary")
    public String viewEditNotExistingDiary(
            @RequestParam(value = "tab", defaultValue = "edit") String tab,
            Model model) {
        DiaryViewModel diaryViewModel = new DiaryViewModel();

        model.addAttribute("activeTab", tab);
        model.addAttribute("diaryViewModel", diaryViewModel);
        model.addAttribute("fragmentModels", null);

        return "user-diaries/edit-view";
    }

    @GetMapping("/{diaryId}")
    public String viewEditExistingDiary(
            @RequestParam(value = "tab", defaultValue = "edit") String tab,
            @RequestParam(value = "removefragmentid", defaultValue = "-1") Long removeFragmentId,
            @RequestParam(value = "removetagid", defaultValue = "-1") Long removeTagId,
            @RequestParam(value = "addtag", defaultValue = "") String tagContent,
            @PathVariable(value = "diaryId") Long diaryId,
            Model model) {
        Optional<DiaryModel> diaryModelOptional = helper.diaryService.findById(diaryId);

        if (diaryModelOptional.isEmpty()) {
            return "redirect:/404";
        }

        DiaryModel diaryModel = diaryModelOptional.get();

        List<FragmentViewModel> fragmentModels = helper.getFragmentViewModels(diaryId);
        List<TagModel> tagModels = helper.tagService.getAllTagsByDiaryId(diaryId);

        if (tab.equals("edit")) {
            if (helper.fragmentService.existsById(removeFragmentId)) {
                helper.fragmentService.delete(removeFragmentId);

                fragmentModels = helper.getFragmentViewModels(diaryId);

                diaryModel.setLastEditDate(LocalDateTime.now());
                helper.diaryService.save(diaryModel);
            }

            if (helper.tagService.existsById(removeTagId)) {
                helper.tagService.deleteFromDiaries(diaryId, removeTagId);

                tagModels = helper.tagService.getAllTagsByDiaryId(diaryId);

                diaryModel.setLastEditDate(LocalDateTime.now());
                helper.diaryService.save(diaryModel);
            }

            tagContent = tagContent.toLowerCase();
            if (helper.addTagToDiary(diaryModel, tagContent, tagModels)) {
                tagModels = helper.tagService.getAllTagsByDiaryId(diaryId);

                diaryModel.setLastEditDate(LocalDateTime.now());
                helper.diaryService.save(diaryModel);
            }
        }

        DiaryViewModel diaryViewModel = new DiaryViewModel(
                diaryId,
                diaryModel.getTitle(),
                "",
                "",
                diaryModel.getLastEditDate(),
                diaryModel.getCreationDate());

        model.addAttribute("FragmentType", FragmentType.class);
        model.addAttribute("activeTab", tab);
        model.addAttribute("diaryViewModel", diaryViewModel);
        model.addAttribute("fragmentModels", fragmentModels);
        model.addAttribute("tagModels", tagModels);

        return "user-diaries/edit-view";
    }

    @PostMapping("/save")
    public String saveEditDiary(
            @RequestParam("titleInput") String title,
            @RequestParam("diaryId") Long diaryId,
            @RequestParam("buttonSubmit") String buttonValue,
            @RequestParam Map<String, String> textInputs,
            @RequestParam Map<String, MultipartFile> fileInputs,
            Model model) {
        Optional<DiaryModel> optionalDiaryModel = helper.diaryService.findById(diaryId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (optionalDiaryModel.isEmpty()) {
            optionalDiaryModel = helper.createDiary(authentication, title);
        } else {
            helper.aggregateReceivedInputsInFragments(textInputs, fileInputs);
            List<FragmentModel> fragmentModels = helper.getFragmentModels();
            helper.fragmentService.saveAll(fragmentModels);

            if (buttonValue.equals("delete")) {
                helper.savePendingDeletion(authentication, optionalDiaryModel.get());

                //helper.fragmentService.deleteAll(fragmentModels);

                return "redirect:/diaries";
            }
        }

        if (!helper.createEmptyFragmentBy(buttonValue, optionalDiaryModel.get())) {
            return "redirect:/404";
        }

        optionalDiaryModel.get().setLastEditDate(LocalDateTime.now());
        optionalDiaryModel.get().setTitle(title);
        helper.diaryService.save(optionalDiaryModel.get());

        return "redirect:/edit-view/" + optionalDiaryModel.get().getId();
    }
}
