package com.smsoft.playgroundbe.web.kakaotoken.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class KakaoTokenController {

    @Value("${kakao.client.id}")
    private String clientId;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("clientId", clientId);
        return "loginForm";
    }

    @GetMapping("/oauth/kakao/callback")
    public @ResponseBody String loginCallback(String code) {
        return code;
    }
}
