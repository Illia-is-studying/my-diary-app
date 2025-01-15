package com.example.mydiaryapp.Repo;

import com.example.mydiaryapp.Models.DiaryFragmentModel;
import com.example.mydiaryapp.Models.FragmentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDiaryFragmentRepository extends JpaRepository<DiaryFragmentModel, Long> {
    List<DiaryFragmentModel> findAllByDiaryId(Long diaryId);
}
