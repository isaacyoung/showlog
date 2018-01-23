package cn.isaac.showlog

import java.util.*

/**
 *
 * create by isaac at 2018/1/23 10:41
 */

@DslMarker
annotation class TagMarker

@TagMarker
abstract class Tag(val name: String)

fun configBuilder(init: Config.() -> Unit): Config {
    val config = Config()
    config.init()
    return config
}

class Config : Tag("cn.isaac.config.getConfig") {

    val serverList = arrayListOf<Server>()

    fun server(init: Server.() -> Unit): Server {
        val server = Server()
        server.init()
        server.id = UUID.randomUUID().toString()
        serverList.add(server)
        return server
    }
}

class Server : Tag("server") {
    lateinit var id: String
    lateinit var title: String
    var host: String = ""
    var userName: String = ""
    var password: String = ""
    lateinit var logFile: String
    var isRemote = true
}