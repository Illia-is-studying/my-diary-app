package com.example.mydiaryapp.Repo;

import com.example.mydiaryapp.Models.PendingDeletionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IPendingDeletionRepository extends JpaRepository<PendingDeletionModel, Long> {
    List<PendingDeletionModel> findAllByMyAppUserId(Long userId);

    List<PendingDeletionModel> findByDeletionDateBefore(LocalDateTime dateTime);
}
