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

    private Long id;
    private String title;
    private String shortDescription;
    private String countTimeAgo;
    private LocalDateTime lastEditDate;
    private LocalDateTime creationDate;

    public String getLastEditDateString() {
        return lastEditDate.format(formatter);
    }

    public String getCreationDateString() {
        return creationDate.format(formatter);
    }
}
