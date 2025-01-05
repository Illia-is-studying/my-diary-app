package com.example.mydiaryapp.Models.ViewModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FragmentViewModel {
    private Long diaryFragmentId;

    private String fragmentType;

    private String textFragment;

    private byte[] imageFragment;

    private byte[] mediaFileFragment;
}
