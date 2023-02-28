package ru.huza.core.config

import java.time.Clock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ClockConfig {

    @Bean
    fun clock(): Clock = Clock.systemUTC()
}
