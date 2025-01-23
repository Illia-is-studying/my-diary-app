package com.example.mydiaryapp.Helpers;

import com.example.mydiaryapp.Enums.FragmentType;
import com.example.mydiaryapp.Models.*;
import com.example.mydiaryapp.Models.ViewModels.FragmentViewModel;
import com.example.mydiaryapp.Services.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class EditViewControllerHelper {
    public final DiaryService diaryService;
    public final FragmentService fragmentService;
    public final MyAppUserService myAppUserService;
    public final TagService tagService;
    public final PendingDeletionService pendingDeletionService;
    public final UserSettingsService userSettingsService;

    private final List<FragmentModel> fragmentModelsBuffer = new ArrayList<>();

    public EditViewControllerHelper(DiaryService diaryService, FragmentService fragmentService,
                                    MyAppUserService myAppUserService, TagService tagService,
                                    PendingDeletionService pendingDeletionService,
                                    UserSettingsService userSettingsService) {
        this.diaryService = diaryService;
        this.fragmentService = fragmentService;
        this.myAppUserService = myAppUserService;
        this.tagService = tagService;
        this.pendingDeletionService = pendingDeletionService;
        this.userSettingsService = userSettingsService;
    }

    public Optional<DiaryModel> createDiary(Authentication authentication, String title) {
        List<MyAppUser> myAppUsers = AuthenticationHelper
                .getCurrentUserInListByAuthentication(authentication, myAppUserService);
        MyAppUser user = myAppUsers.get(0);

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

    public void savePendingDeletion(Authentication authentication, DiaryModel diaryModel) {
        List<MyAppUser> myAppUsers = AuthenticationHelper
                .getCurrentUserInListByAuthentication(authentication, myAppUserService);
        MyAppUser user = myAppUsers.get(0);

        UserSettingsModel userSettingsModel = userSettingsService.getUserSettings(user);
        LocalDateTime deletionDate = LocalDateTime.now().plusDays(userSettingsModel.getCartStorageDays());

        PendingDeletionModel pendingDeletionModel = new PendingDeletionModel();
        pendingDeletionModel.setDiary(diaryModel);
        pendingDeletionModel.setMyAppUser(user);
        pendingDeletionModel.setDeletionDate(deletionDate);
        pendingDeletionService.save(pendingDeletionModel);
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

                    ImageFragment imageFragment = new ImageFragment();
                    imageFragment.setFileName(file.getOriginalFilename());
                    imageFragment.setFileType(file.getContentType());
                    imageFragment.setImageData(file.getBytes());

                    Long id = Long.parseLong(key.replace("image", ""));
                    fragmentModel.setId(id);
                    fragmentModel.setFragmentType(FragmentType.IMAGE);
                    fragmentModel.setImageFragment(imageFragment);

                    fragmentModelsBuffer.add(fragmentModel);
                } else if (key.startsWith("media") && !file.isEmpty()) {
                    FragmentModel fragmentModel = new FragmentModel();

                    MediaFileFragment mediaFileFragment = new MediaFileFragment();
                    mediaFileFragment.setFileName(file.getOriginalFilename());
                    mediaFileFragment.setFileType(file.getContentType());
                    mediaFileFragment.setMediaData(file.getBytes());

                    Long id = Long.parseLong(key.replace("media", ""));
                    fragmentModel.setId(id);
                    fragmentModel.setFragmentType(FragmentType.MEDIA_FILE);
                    fragmentModel.setMediaFileFragment(mediaFileFragment);

                    fragmentModelsBuffer.add(fragmentModel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean createEmptyFragmentOrSaveBy(String fragmentAction, DiaryModel diaryModel) {
        FragmentModel newFragmentModel = new FragmentModel();

        if (fragmentAction.equals("newtext")) {
            newFragmentModel.setFragmentType(FragmentType.TEXT_FRAGMENT);
        } else if (fragmentAction.equals("newimage")) {
            newFragmentModel.setFragmentType(FragmentType.IMAGE);
        } else if (fragmentAction.equals("newmedia")) {
            newFragmentModel.setFragmentType(FragmentType.MEDIA_FILE);
        } else if (!fragmentAction.equals("save")) {
            return false;
        }

        fragmentService.save(newFragmentModel, diaryModel);

        return true;
    }

    public boolean addTagToDiary(DiaryModel diaryModel, String tagContent) {
        if (!tagContent.isEmpty()) {
            TagModel tagModel = tagService.findByContent(tagContent);

            if (tagModel != null) {
                TagModel checkTagModel = tagService.findByContentAndDiaryId(tagContent, diaryModel.getId());

                if (checkTagModel != null) {
                    return false;
                }
            } else {
                tagModel = new TagModel();
                tagModel.setContent(tagContent);
            }

            tagService.save(tagModel, diaryModel.getId());

            return true;
        }

        return false;
    }

    public List<FragmentViewModel> getFragmentViewModels(Long diaryId) {
        List<FragmentViewModel> fragmentViewModels = new ArrayList<>();
        List<FragmentModel> fragmentModels = fragmentService.findAllByDiaryId(diaryId);

        fragmentModels.forEach(fragmentModel -> {
            String base64Data = null;
            if (fragmentModel.getImageFragment() != null) {
                base64Data = Base64.getEncoder().encodeToString(fragmentModel.getImageFragment().getImageData());
            } else if (fragmentModel.getMediaFileFragment() != null) {
                base64Data = Base64.getEncoder().encodeToString(fragmentModel.getMediaFileFragment().getMediaData());
            }

            fragmentViewModels.add(new FragmentViewModel(
                    fragmentModel.getId(),
                    fragmentModel.getFragmentType(),
                    fragmentModel.getTextFragment(),
                    base64Data,
                    fragmentModel.getImageFragment(),
                    fragmentModel.getMediaFileFragment()));
        });

        return fragmentViewModels;
    }

    public List<FragmentModel> getFragmentModels() {
        return fragmentModelsBuffer;
    }
}
