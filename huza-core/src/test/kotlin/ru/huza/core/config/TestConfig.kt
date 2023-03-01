package ru.huza.core.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.TestPropertySource

@Configuration
@ComponentScan(basePackages = ["ru.huza"])
//@ConfigurationProperties(prefix="test", locations = "classpath:SettingsTest.properties")
//@TestPropertySource(
//    properties = [
//        "huza.api-url=http://localhost:4242/api/",
//        "huza.file-storage-path=.temp/huza-files",
//    ],
//)
class TestConfig
