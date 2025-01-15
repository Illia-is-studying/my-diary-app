package com.example.mydiaryapp.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="diary_fragment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryFragmentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "diary_id")
    private DiaryModel diary;

    @ManyToOne
    @JoinColumn(name="fragment_id")
    private FragmentModel fragment;

//    @Column(nullable = false)
//    private Integer queuePosition;
}
