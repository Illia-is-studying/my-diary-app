package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Models.DiaryModel;
import com.example.mydiaryapp.Repo.IDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {
    private final IDiaryRepository diaryRepository;

    @Autowired
    public DiaryService(IDiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public List<DiaryModel> findByUserIdOrderByLastEditDateDesc(Long userId) {
        return diaryRepository.findByMyAppUsersIdOrderByLastEditDateDesc(userId);
    }

    public List<DiaryModel> findByUserIdOrderByLastEditDateAsc(Long userId) {
        return diaryRepository.findByMyAppUsersIdOrderByLastEditDateAsc(userId);
    }

    public List<DiaryModel> findByUserIdOrderByTitleAsc(Long userId) {
        return diaryRepository.findByMyAppUsersIdOrderByTitleAsc(userId);
    }

    public Optional<DiaryModel> findById(Long id) {
       return this.diaryRepository.findById(id);
    }

    public Long save(DiaryModel diaryModel) {
        return this.diaryRepository.save(diaryModel).getId();
    }

    public void deleteById(Long id) {
        this.diaryRepository.deleteById(id);
    }
}
