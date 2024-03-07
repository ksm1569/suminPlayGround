package com.smsoft.playgroundbe.api.exceptiontest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BindExceptionTestDTO {

    @NotBlank(message = "해당 값은 필수입니다.")
    private String value1;

    @Max(value = 10, message = "최대 입력값은 10자리 입니다.")
    private String value2;
}
