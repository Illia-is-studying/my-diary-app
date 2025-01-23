package com.example.mydiaryapp.Repo;

import com.example.mydiaryapp.Models.DiaryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDiaryRepository extends JpaRepository<DiaryModel, Long> {
    List<DiaryModel> findByMyAppUsersIdOrderByLastEditDateDesc(Long userId);

    List<DiaryModel> findByMyAppUsersIdOrderByLastEditDateAsc(Long userId);

    List<DiaryModel> findByMyAppUsersIdOrderByTitleAsc(Long userId);
}