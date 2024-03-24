package com.smsoft.playgroundbe.web.kakaotoken.controller;

import com.smsoft.playgroundbe.web.kakaotoken.client.KakaoTokenClient;
import com.smsoft.playgroundbe.web.kakaotoken.controller.dto.KakaoTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoTokenController {
    private final KakaoTokenClient kakaoTokenClient;


    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("clientId", clientId);
        return "loginForm";
    }

    @GetMapping("/oauth/kakao/callback")
    public @ResponseBody String loginCallback(String code) {
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";
        KakaoTokenDTO.Request kakaoTokenRequestDTO = KakaoTokenDTO.Request.builder()
                .client_id(clientId)
                .client_sercret(clientSecret)
                .grant_type("authorization_code")
                .code(code)
                .redirect_uri("http://localhost:8084/oauth/kakao/callback")
                .build();

        KakaoTokenDTO.Response kakaoToken = kakaoTokenClient.requestKakaoToken(contentType, kakaoTokenRequestDTO);

        return "kakao token : " + kakaoToken;
    }
}
