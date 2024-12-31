package com.example.mydiaryapp.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "tag")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String content;

    @ManyToMany(mappedBy = "tags")
    private List<DiaryModel> diaries = new ArrayList<>();
}
