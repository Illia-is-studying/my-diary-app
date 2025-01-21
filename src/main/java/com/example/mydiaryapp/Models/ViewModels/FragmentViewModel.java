package com.example.mydiaryapp.Models.ViewModels;

import com.example.mydiaryapp.Enums.FragmentType;
import com.example.mydiaryapp.Models.ImageFragment;
import com.example.mydiaryapp.Models.MediaFileFragment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FragmentViewModel {
    private Long id;

    private FragmentType fragmentType;

    private String textFragment;

    private String base64FileData;

    private ImageFragment imageFragment;

    private MediaFileFragment mediaFileFragment;
}
