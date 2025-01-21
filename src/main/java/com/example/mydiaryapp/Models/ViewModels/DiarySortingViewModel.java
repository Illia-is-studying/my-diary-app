package com.example.mydiaryapp.Models.ViewModels;

import com.example.mydiaryapp.Enums.SortingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiarySortingViewModel {
    private SortingType sortingType;
    private String titlePiece;
    private String tagsLine;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
