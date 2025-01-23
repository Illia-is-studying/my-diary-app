package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Models.PendingDeletionModel;
import com.example.mydiaryapp.Repo.IPendingDeletionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PendingDeletionService {
    private final IPendingDeletionRepository pendingDeletionRepository;

    @Autowired
    public PendingDeletionService(IPendingDeletionRepository pendingDeletionRepository) {
        this.pendingDeletionRepository = pendingDeletionRepository;
    }

    public List<PendingDeletionModel> findAllByUserId(Long userId) {
        return pendingDeletionRepository.findAllByMyAppUserId(userId);
    }

    public PendingDeletionModel save(PendingDeletionModel pendingDeletionModel) {
        return pendingDeletionRepository.save(pendingDeletionModel);
    }

    public void delete(Long pendingDeletionId) {
        pendingDeletionRepository.deleteById(pendingDeletionId);
    }
}
