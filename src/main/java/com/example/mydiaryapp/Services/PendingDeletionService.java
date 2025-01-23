package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Models.PendingDeletionModel;
import com.example.mydiaryapp.Repo.IPendingDeletionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PendingDeletionService {
    private final IPendingDeletionRepository pendingDeletionRepository;
    private final DiaryService diaryService;

    @Autowired
    public PendingDeletionService(IPendingDeletionRepository pendingDeletionRepository, DiaryService diaryService) {
        this.pendingDeletionRepository = pendingDeletionRepository;
        this.diaryService = diaryService;
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

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void deleteExpiredRecords() {
        LocalDateTime now = LocalDateTime.now();

        List<PendingDeletionModel> expiredEntities = pendingDeletionRepository.findByDeletionDateBefore(now);

        for(PendingDeletionModel pendingDeletionModel : expiredEntities) {
            diaryService.deleteById(pendingDeletionModel.getDiary().getId());
        }

        pendingDeletionRepository.deleteAll(expiredEntities);
    }
}
