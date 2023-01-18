package ru.huza

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
class HuzaApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<HuzaApplication>(*args)
}
