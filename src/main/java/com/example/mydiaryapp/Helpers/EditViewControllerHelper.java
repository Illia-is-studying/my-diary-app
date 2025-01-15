package com.example.mydiaryapp.Helpers;

import com.example.mydiaryapp.Enums.FragmentType;
import com.example.mydiaryapp.Models.DiaryModel;
import com.example.mydiaryapp.Models.FragmentModel;
import com.example.mydiaryapp.Models.MyAppUser;
import com.example.mydiaryapp.Models.TagModel;
import com.example.mydiaryapp.Services.DiaryService;
import com.example.mydiaryapp.Services.FragmentService;
import com.example.mydiaryapp.Services.MyAppUserService;
import com.example.mydiaryapp.Services.TagService;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EditViewControllerHelper {
    public final DiaryService diaryService;
    public final FragmentService fragmentService;
    public final MyAppUserService myAppUserService;
    public final TagService tagService;

    private final List<FragmentModel> fragmentModelsBuffer = new ArrayList<>();

    public EditViewControllerHelper(DiaryService diaryService, FragmentService fragmentService,
                                    MyAppUserService myAppUserService, TagService tagService) {
        this.diaryService = diaryService;
        this.fragmentService = fragmentService;
        this.myAppUserService = myAppUserService;
        this.tagService = tagService;
    }

    public Optional<DiaryModel> createDiary(Authentication authentication, String title) {
        List<MyAppUser> myAppUsers = myAppUserService.getCurrentUserInListByAuthentication(authentication);

        DiaryModel diaryModel = new DiaryModel();
        diaryModel.setCollaboration(false);
        diaryModel.setTitle(title);
        diaryModel.setCreationDate(LocalDateTime.now());
        diaryModel.setLastEditDate(LocalDateTime.now());
        diaryModel.setMyAppUsers(myAppUsers);

        Long diaryId = diaryService.save(diaryModel);

        diaryModel.setId(diaryId);

        return Optional.of(diaryModel);
    }

    public void aggregateReceivedInputsInFragments(Map<String, String> textInputs,
                                                   Map<String, MultipartFile> fileInputs) {
        fragmentModelsBuffer.clear();

        textInputs.forEach((key, value) -> {
            if (key.startsWith("content")) {
                Long id = Long.parseLong(key.replace("content", ""));

                FragmentModel fragmentModel = new FragmentModel();
                fragmentModel.setId(id);
                fragmentModel.setFragmentType(FragmentType.TEXT_FRAGMENT);
                fragmentModel.setTextFragment(value);
                fragmentModelsBuffer.add(fragmentModel);
            }
        });

        fileInputs.forEach((key, file) -> {
            try {
                if (key.startsWith("image") && !file.isEmpty()) {
                    FragmentModel fragmentModel = new FragmentModel();

                    Long id = Long.parseLong(key.replace("image", ""));
                    fragmentModel.setId(id);
                    fragmentModel.setFragmentType(FragmentType.IMAGE);
                    fragmentModel.setImageFragment(file.getBytes());

                    fragmentModelsBuffer.add(fragmentModel);
                } else if (key.startsWith("media") && !file.isEmpty()) {
                    FragmentModel fragmentModel = new FragmentModel();

                    Long id = Long.parseLong(key.replace("image", ""));
                    fragmentModel.setId(id);
                    fragmentModel.setFragmentType(FragmentType.MEDIA_FILE);
                    fragmentModel.setMediaFileFragment(file.getBytes());

                    fragmentModelsBuffer.add(fragmentModel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean createEmptyFragmentBy(String buttonValue, DiaryModel diaryModel) {
        FragmentModel newFragmentModel = new FragmentModel();

        if (buttonValue.equals("newtext")) {
            newFragmentModel.setFragmentType(FragmentType.TEXT_FRAGMENT);
        } else if (buttonValue.equals("newimage")) {
            newFragmentModel.setFragmentType(FragmentType.IMAGE);
        } else if (buttonValue.equals("newmedia")) {
            newFragmentModel.setFragmentType(FragmentType.MEDIA_FILE);
        } else if (!buttonValue.equals("save")) {
            return false;
        }

        fragmentService.save(newFragmentModel, diaryModel);

        return true;
    }

    public boolean addTagToDiary(DiaryModel diaryModel, String tagContent, List<TagModel> tagModels) {
        if (!tagContent.isEmpty()) {
            TagModel tagModel = tagService.findByContent(tagContent);

            if (tagModel == null) {
                tagModel = new TagModel();
                tagModel.setContent(tagContent);
            } else {
                List<TagModel> similarTags = tagModels.stream()
                        .filter(tm -> tm.getContent().equalsIgnoreCase(tagContent))
                        .toList();

                if (!similarTags.isEmpty()) {
                    return false;
                }
            }

            tagService.save(tagModel, diaryModel.getId());

            return true;
        }

        return false;
    }

    public List<FragmentModel> getFragmentModels() {
        return fragmentModelsBuffer;
    }
}
