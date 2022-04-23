package com.otavi.pl.Watcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WatcherApplication

fun main(args: Array<String>) {
	runApplication<WatcherApplication>(*args)
	println("sss")
}
