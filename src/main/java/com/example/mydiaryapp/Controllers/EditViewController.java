package com.example.mydiaryapp.Controllers;

import com.example.mydiaryapp.Models.ViewModels.FragmentViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
public class EditViewController {
    private final ArrayList<FragmentViewModel> fragmentViews;

    public EditViewController() {
        fragmentViews = new ArrayList<>();
    }

    @GetMapping("/edit-view")
    public String viewEditNotExistingDiary(
            @RequestParam(value = "tab", defaultValue = "edit") String tab,
            @RequestParam(value = "fragmenttype", defaultValue = "none") String fragmentType,
            @RequestParam(value = "removefragment", defaultValue = "-1") int removeFragmentIndex,
            @RequestParam(value = "title", defaultValue = "") String title,
            Model model) {
        if (tab.equals("edit")) {
            if (fragmentType.equals("text") || fragmentType.equals("image") || fragmentType.equals("media")) {
                FragmentViewModel newFragment = new FragmentViewModel();
                newFragment.setFragmentType(fragmentType);

                fragmentViews.add(newFragment);
            }

            if (removeFragmentIndex > -1 && fragmentViews.size() > removeFragmentIndex) {
                fragmentViews.remove(removeFragmentIndex);
            }
        }

        model.addAttribute("fragmentViews", fragmentViews);
        model.addAttribute("title", title);
        model.addAttribute("activeTab", tab);
        return "user-diaries/edit-view";
    }

    @GetMapping("/edit-view/{id}")
    public String viewEditExistingDiary(
            @RequestParam(value = "tab", defaultValue = "edit") String tab,
            Model model) {
        if (tab.equals("edit")) {

        } else if (tab.equals("view")) {

        }

        return "user-diaries/edit-view";
    }
}
