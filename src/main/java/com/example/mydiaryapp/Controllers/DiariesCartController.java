package com.example.mydiaryapp.Controllers;

import com.example.mydiaryapp.Helpers.AuthenticationHelper;
import com.example.mydiaryapp.Helpers.DateTimeHelper;
import com.example.mydiaryapp.Models.DiaryModel;
import com.example.mydiaryapp.Models.PendingDeletionModel;
import com.example.mydiaryapp.Models.ViewModels.DiaryViewModel;
import com.example.mydiaryapp.Models.ViewModels.PendingDeletionViewModel;
import com.example.mydiaryapp.Services.DiaryService;
import com.example.mydiaryapp.Services.MyAppUserService;
import com.example.mydiaryapp.Services.PendingDeletionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DiariesCartController {
    public final PendingDeletionService pendingDeletionService;
    public final MyAppUserService myAppUserService;
    public final DiaryService diaryService;

    @Autowired
    public DiariesCartController(PendingDeletionService pendingDeletionService,
                                     MyAppUserService myAppUserService, DiaryService diaryService) {
        this.pendingDeletionService = pendingDeletionService;
        this.myAppUserService = myAppUserService;
        this.diaryService = diaryService;
    }

    @GetMapping("/diaries/cart")
    public String pendingDeletion(Model model, HttpSession httpSession) {
        Long userId = AuthenticationHelper.getCurrentUserId(httpSession, myAppUserService);

        List<PendingDeletionModel> diaryModels = pendingDeletionService.findAllByUserId(userId);

        List<PendingDeletionViewModel> pendingDeletionViewModels = new ArrayList<>();

        for (PendingDeletionModel pendingDeletionModel : diaryModels) {
            DiaryModel diaryModel= pendingDeletionModel.getDiary();

            String timeUntilDeletion = DateTimeHelper
                    .getTimeDifference(LocalDateTime.now(), pendingDeletionModel.getDeletionDate(), "");

            PendingDeletionViewModel pendingDeletionViewModel = new PendingDeletionViewModel();
            pendingDeletionViewModel.setId(pendingDeletionModel.getId());
            pendingDeletionViewModel.setDiaryTitle(diaryModel.getTitle());
            pendingDeletionViewModel.setLastEditDiaryDate(diaryModel.getLastEditDate()
                    .format(DiaryViewModel.getFormatter()));
            pendingDeletionViewModel.setTimeUntilDiaryDeletion(timeUntilDeletion);
            pendingDeletionViewModels.add(pendingDeletionViewModel);
        }

        model.addAttribute("pendingDeletionViewModels", pendingDeletionViewModels);

        return "user-diaries/diaries-cart";
    }

    @GetMapping("diaries/cart/restore/{id}")
    public String pendingDeletion(@PathVariable Long id, Model model, HttpSession httpSession) {
        pendingDeletionService.delete(id);

        return "redirect:/diaries/cart";
    }
}
