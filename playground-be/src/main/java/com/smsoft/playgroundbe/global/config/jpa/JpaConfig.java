package com.smsoft.playgroundbe.global.config.jpa;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl(httpServletRequest);
    }
}
