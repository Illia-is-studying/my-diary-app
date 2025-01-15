package com.example.mydiaryapp.Models.ViewModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DiaryViewModel {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");

    private Long countDaysAgo;
    private Long id;
    private String title;
    private String shortDescription;
    private LocalDateTime lastEditDate;

    public String getLastEditDateString() {
        return lastEditDate.format(formatter);
    }
}
