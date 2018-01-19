package cn.isaac.showlog

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class ShowlogApplication

fun main(args: Array<String>) {
    SpringApplication.run(ShowlogApplication::class.java, *args)
}
