package com.smsoft.playgroundbe.api.exceptiontest.controller;

import com.smsoft.playgroundbe.api.exceptiontest.dto.BindExceptionTestDTO;
import com.smsoft.playgroundbe.api.exceptiontest.dto.TestEnum;
import com.smsoft.playgroundbe.global.error.ErrorCode;
import com.smsoft.playgroundbe.global.error.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exception")
public class ExceptionTestController {

    // 파라미터 벨리데이션 바인딩 에러 테스트 (GET)
    @GetMapping("/bind-exception-test")
    public String bindExceptionTest(@Valid BindExceptionTestDTO bindExceptionTestDto){
        return "OK";
    }

    // 파라미터 벨리데이션 바인딩 에러 테스트 (POST)
    @PostMapping("/bind-exception-test")
    public String postBindExceptionTest(@Validated @RequestBody BindExceptionTestDTO bindExceptionTestDto){
        return "OK";
    }

    // 파라미터 타입이 디스매치
    @GetMapping("/type-exception-test")
    public String typeMismatchException(TestEnum testEnum){
        return "OK";
    }

    // 비지니스 로직 에러 테스트
    @GetMapping("/business-exception-test")
    public String businessExceptionTest(String isError){
        if("true".equals(isError)){
            throw new BusinessException(ErrorCode.TEST);
        }
        return "OK";
    }

    // 나머지 예외 에러 테스트
    @GetMapping("/exception-test")
    public String exceptionTest(String isError){
        if("true".equals(isError)){
            throw new IllegalArgumentException("예외 테스트");
        }
        return "OK";
    }

}