package com.example.mydiaryapp.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidErrorDto {
    private String errorMessage;
    private String errorCssClass;
}
