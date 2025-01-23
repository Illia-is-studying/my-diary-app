package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Models.DiaryFragmentModel;
import com.example.mydiaryapp.Repo.IDiaryFragmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaryFragmentService {
    private final IDiaryFragmentRepository fragmentDiaryRepository;

    @Autowired
    public DiaryFragmentService(IDiaryFragmentRepository fragmentDiaryRepository) {
        this.fragmentDiaryRepository = fragmentDiaryRepository;
    }

    public void save(DiaryFragmentModel diaryFragmentModel) {
        this.fragmentDiaryRepository.save(diaryFragmentModel);
    }

    public List<DiaryFragmentModel> findAllByDiaryId(Long diaryId) {
        return this.fragmentDiaryRepository.findAllByDiaryId(diaryId);
    }
}
