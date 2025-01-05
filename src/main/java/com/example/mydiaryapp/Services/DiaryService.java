package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Repo.IDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiaryService {
    private final IDiaryRepository diaryRepository;

    @Autowired
    public DiaryService(IDiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }


}
