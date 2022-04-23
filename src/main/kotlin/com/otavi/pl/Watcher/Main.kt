package com.otavi.pl.Watcher

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled



@Configuration
@EnableScheduling
@EnableAsync
class Main {
    @Scheduled(fixedRate = 1000)
    fun scheduleFixedRateTask() {
        println(
            "Fixed rate task - " + System.currentTimeMillis() / 1000
        )
    }
}