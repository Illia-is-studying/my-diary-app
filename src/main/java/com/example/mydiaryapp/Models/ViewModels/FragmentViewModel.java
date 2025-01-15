package com.example.mydiaryapp.Models.ViewModels;

import com.example.mydiaryapp.Enums.FragmentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FragmentViewModel {
    private Integer queuePosition;

    private Long fragmentId;

    private FragmentType fragmentType;

    private String textFragment;

    private byte[] imageFragment;

    private byte[] mediaFileFragment;
}
