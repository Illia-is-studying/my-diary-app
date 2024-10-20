package com.example.mydiaryapp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DiaryController {
    @GetMapping("/diaries")
    public String diaries(){
        return "user-diaries/diaries";
    }


}
