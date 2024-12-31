package com.example.mydiaryapp.Repo;

import com.example.mydiaryapp.Models.PendingDeletionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPendingDeletionRepository extends JpaRepository<PendingDeletionModel, Long> {
}
