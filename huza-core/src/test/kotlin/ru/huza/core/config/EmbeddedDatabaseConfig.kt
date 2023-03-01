package ru.huza.core.config

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = ["ru.huza.data.entity"])
@EnableJpaRepositories(basePackages = ["ru.huza.data.dao"])
class EmbeddedDatabaseConfig {

    @Bean
    fun embeddedPostgres(): EmbeddedPostgres =
        EmbeddedPostgres.builder()
            .setPort(15432)
            .start()

    @Bean
    fun embeddedDataSource(@Autowired pg: EmbeddedPostgres): DataSource =
        pg.postgresDatabase
}
