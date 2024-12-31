package com.example.mydiaryapp.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name="fragment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FragmentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column
    private String textFragment;

    @Basic
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] imageFragment;

    @Basic
    @Column(name = "media_file", columnDefinition = "LONGBLOB")
    private byte[] mediaFileFragment;

    @OneToMany(mappedBy = "fragment", cascade = CascadeType.ALL)
    private List<DiaryFragmentModel> diaryFragmentModels = new ArrayList<>();
}
