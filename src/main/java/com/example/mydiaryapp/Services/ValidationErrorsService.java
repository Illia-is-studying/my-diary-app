package com.example.mydiaryapp.Services;

import com.example.mydiaryapp.Models.DTOs.ValidErrorDto;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class ValidationErrorsService {
    private final Map<String, ValidErrorDto> validationErrors;
    private boolean isErrors;

    public ValidationErrorsService() {
        validationErrors = new HashMap<>();
        isErrors = false;
    }

    public boolean validateField(boolean badOption, String fieldName, String errorMessage) {
        ValidErrorDto validErrorDto = new ValidErrorDto("", "is-valid");
        boolean isValid = true;

        if (badOption) {
            validErrorDto.setErrorMessage(errorMessage);
            validErrorDto.setErrorCssClass("is-invalid");
            isErrors = true;
            isValid = false;
        }

        validationErrors.put(fieldName, validErrorDto);
        return isValid;
    }

    public void setValidationErrors(String ...fieldNames) {
        for (String fieldName : fieldNames) {
            validationErrors.put(fieldName, new ValidErrorDto(fieldName, ""));
        }
    }

    public void setIsErrors(boolean isErrors) {
        this.isErrors = isErrors;
    }
}
