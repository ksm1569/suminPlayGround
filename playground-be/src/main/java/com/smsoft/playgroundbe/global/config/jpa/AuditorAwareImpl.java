package com.smsoft.playgroundbe.global.config.jpa;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final HttpServletRequest httpServletRequest;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(httpServletRequest.getRequestURI());
    }
}
