package com.example.mydiaryapp.Repo;

import com.example.mydiaryapp.Models.DiaryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IDiaryRepository extends JpaRepository<DiaryModel, Long> {
    List<DiaryModel> findByTitleStartingWithIgnoreCase(String title);

    List<DiaryModel> findAllByOrderByLastEditDateDesc();

    List<DiaryModel> findAllByOrderByLastEditDateAsc();

    List<DiaryModel> findAllByOrderByTitleDesc();

    List<DiaryModel> findByLastEditDateBefore(LocalDateTime startDate);

    List<DiaryModel> findByLastEditDateAfter(LocalDateTime startDate);

    List<DiaryModel> findByLastEditDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}