package com.example.mydiaryapp.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "diary")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime creationDate;
    private LocalDateTime lastEditDate;
    private boolean isCollaboration;

    @ManyToMany
    @JoinTable(
            name = "diary_tag",
            joinColumns = @JoinColumn(name = "diary_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagModel> tags = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "diary_user",
            joinColumns = @JoinColumn(name = "diary_id"),
            inverseJoinColumns = @JoinColumn(name = "my_app_user_id")
    )
    private List<MyAppUser> myAppUsers = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
    private List<DiaryFragmentModel> diaryFragmentModels = new ArrayList<>();
}
