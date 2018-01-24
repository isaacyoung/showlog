package cn.isaac.showlog

import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelSftp.RESUME
import com.jcraft.jsch.JSch
import java.io.ByteArrayOutputStream


/**
 * 链接服务器
 * create by isaac at 2018/1/21 1:44
 */

data class Result(val size: Long, val str: String)

fun connect(id: String, begin: Long = 0): Result {
    val server = getServer(id) ?: return Result(begin,"")

    val jsch = JSch()
    val session = jsch.getSession(server.userName,server.host)
    val config = java.util.Properties()
    config.put("StrictHostKeyChecking", "no")
    session.setConfig(config)
    session.setPassword(server.password)
    session.connect()

    val channel=session.openChannel("sftp") as ChannelSftp
    channel.connect()

    val output = ByteArrayOutputStream()
    val file = server.logFile
    val attr = channel.stat(file)
    if (attr.size == begin) {
        return Result(begin,"")
    }
    var skip = begin
    if (begin == 0L && attr.size > 20000) {
        skip = attr.size - 20000
    }
    channel.get(file,output,null, RESUME,skip)
    var result = output.toString()
    result = result.replace("<","&lt;")
    result = result.replace(">","&gt;")

    channel.disconnect()

    session.disconnect()
    return Result(attr.size,result)
}

fun getServer(id: String): Server? {
    config.serverList.forEach {
        if (it.id == id) {
            return it
        }
    }
    return null
}