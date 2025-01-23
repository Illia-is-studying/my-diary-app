package com.example.mydiaryapp.Models.ViewModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PendingDeletionViewModel {
    private Long id;
    private String diaryTitle;
    private String lastEditDiaryDate;
    private String timeUntilDiaryDeletion;
}
