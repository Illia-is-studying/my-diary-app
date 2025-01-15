package com.example.mydiaryapp.Controllers;

import com.example.mydiaryapp.Enums.FragmentType;
import com.example.mydiaryapp.Helpers.EditViewControllerHelper;
import com.example.mydiaryapp.Models.DiaryModel;
import com.example.mydiaryapp.Models.FragmentModel;
import com.example.mydiaryapp.Models.TagModel;
import com.example.mydiaryapp.Services.DiaryService;
import com.example.mydiaryapp.Services.FragmentService;
import com.example.mydiaryapp.Services.MyAppUserService;
import com.example.mydiaryapp.Services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/edit-view")
public class EditViewController {
    public final EditViewControllerHelper helper;

    @Autowired
    public EditViewController(DiaryService diaryService, FragmentService fragmentService,
                              MyAppUserService myAppUserService, TagService tagService) {
        this.helper = new EditViewControllerHelper(diaryService, fragmentService, myAppUserService, tagService);
    }

    @GetMapping("/new-diary")
    public String viewEditNotExistingDiary(
            @RequestParam(value = "tab", defaultValue = "edit") String tab,
            Model model) {
        model.addAttribute("activeTab", tab);
        model.addAttribute("title", "");
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
        Optional<DiaryModel> diaryModel = helper.diaryService.findById(diaryId);

        if (diaryModel.isEmpty()) {
            return "redirect:/404";
        }

        List<FragmentModel> fragmentModels = helper.fragmentService.findAllByDiaryId(diaryId);
        List<TagModel> tagModels = helper.tagService.getAllTagsByDiaryId(diaryId);

        if (tab.equals("edit")) {
            if (helper.fragmentService.existsById(removeFragmentId)) {
                helper.fragmentService.delete(removeFragmentId);

                fragmentModels = helper.fragmentService.findAllByDiaryId(diaryId);

                diaryModel.get().setLastEditDate(LocalDateTime.now());
                helper.diaryService.save(diaryModel.get());

                diaryModel.get().setLastEditDate(LocalDateTime.now());
                helper.diaryService.save(diaryModel.get());
            }

            if (helper.tagService.existsById(removeTagId)) {
                helper.tagService.deleteFromDiaries(diaryId, removeTagId);

                tagModels = helper.tagService.getAllTagsByDiaryId(diaryId);

                diaryModel.get().setLastEditDate(LocalDateTime.now());
                helper.diaryService.save(diaryModel.get());
            }

            tagContent = tagContent.toLowerCase();
            if (helper.addTagToDiary(diaryModel.get(), tagContent, tagModels)) {
                tagModels = helper.tagService.getAllTagsByDiaryId(diaryId);

                diaryModel.get().setLastEditDate(LocalDateTime.now());
                helper.diaryService.save(diaryModel.get());
            }
        } else if (tab.equals("view")) {

        }

        model.addAttribute("FragmentType", FragmentType.class);
        model.addAttribute("activeTab", tab);
        model.addAttribute("diaryId", diaryId);
        model.addAttribute("title", diaryModel.get().getTitle());
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

        if (optionalDiaryModel.isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            optionalDiaryModel = helper.createDiary(authentication, title);
        } else {
            helper.aggregateReceivedInputsInFragments(textInputs, fileInputs);
            List<FragmentModel> fragmentModels = helper.getFragmentModels();

            if (buttonValue.equals("delete")) {
                helper.diaryService.delete(optionalDiaryModel.get());
                helper.fragmentService.deleteAll(fragmentModels);

                return "redirect:/diaries";
            }

            helper.fragmentService.saveAll(fragmentModels);
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
