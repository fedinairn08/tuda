package com.tuda.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration implements WebMvcConfigurer {
    @Bean
    public Counter eventCounter(MeterRegistry prometheusMeterRegistry) {
        return Counter.builder("event.created")
            .description("Number of created events")
            .register(prometheusMeterRegistry);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
