package com.tuda.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    @Bean
    public Counter eventCounter(MeterRegistry prometheusMeterRegistry) {
        return Counter.builder("event.created")
            .description("Number of created events")
            .register(prometheusMeterRegistry);
    }
}
