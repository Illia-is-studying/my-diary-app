package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Models.DiaryFragmentModel;
import com.example.mydiaryapp.Models.DiaryModel;
import com.example.mydiaryapp.Models.FragmentModel;
import com.example.mydiaryapp.Repo.IFragmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FragmentService {
    private final IFragmentRepository fragmentRepository;
    private final DiaryFragmentService diaryFragmentService;

    @Autowired
    public FragmentService(IFragmentRepository fragmentRepository, DiaryFragmentService diaryFragmentService) {
        this.fragmentRepository = fragmentRepository;
        this.diaryFragmentService = diaryFragmentService;
    }

    public void save(FragmentModel fragmentModel, DiaryModel diary) {
        FragmentModel savedFragment = this.fragmentRepository.save(fragmentModel);

        DiaryFragmentModel diaryFragmentModel = new DiaryFragmentModel();
        diaryFragmentModel.setFragment(savedFragment);
        diaryFragmentModel.setDiary(diary);

        diaryFragmentService.save(diaryFragmentModel);
    }

    public void saveAll(List<FragmentModel> fragmentModelList) {
        fragmentRepository.saveAll(fragmentModelList);
    }

    public List<FragmentModel> findAllByDiaryId(Long diaryId) {
        List<DiaryFragmentModel> diaryFragmentModels = diaryFragmentService.findAllByDiaryId(diaryId);

        return diaryFragmentModels
                .stream().map(DiaryFragmentModel::getFragment).toList();
    }

    public void delete(Long id) {
        fragmentRepository.deleteById(id);
    }

    public void deleteAll(List<FragmentModel> fragmentModels) {
        fragmentRepository.deleteAll(fragmentModels);
    }

    public boolean existsById(Long id) {
        return fragmentRepository.existsById(id);
    }
}
