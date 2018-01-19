package cn.isaac.showlog

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.RandomAccessFile
import javax.servlet.http.HttpServletResponse

/**
 *
 * create by isaac at 2018/1/19 8:58
 */
@RestController
class LogController {

    @RequestMapping("/log")
    fun log(response: HttpServletResponse): String {
        val s = "E:\\home\\log\\wechat\\2018-01-07.log"
        val read = RandomAccessFile(File(s), "r")
        var result = ""
        var tmp: String?
        do {
            tmp = read.readLine()
            tmp?.let {
                result += "____________$tmp<br>"
            }
        } while (tmp != null)

        return result
    }
}