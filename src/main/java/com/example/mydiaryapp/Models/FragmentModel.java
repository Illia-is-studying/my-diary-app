package com.example.mydiaryapp.Models;

import com.example.mydiaryapp.Enums.FragmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity(name="fragment")
@AllArgsConstructor
@NoArgsConstructor
public class FragmentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(columnDefinition = "TEXT")
    private String textFragment;

    @Basic
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] imageFragment;

    @Basic
    @Column(name = "media_file", columnDefinition = "LONGBLOB")
    private byte[] mediaFileFragment;

    @Enumerated(EnumType.ORDINAL)
    private FragmentType fragmentType;

    @OneToMany(mappedBy = "fragment", cascade = CascadeType.ALL)
    private List<DiaryFragmentModel> diaryFragmentModels = new ArrayList<>();

}
