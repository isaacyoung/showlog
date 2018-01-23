package cn.isaac.showlog

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File
import java.io.RandomAccessFile
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest

/**
 * 本地日志
 * create by isaac at 2018/1/19 8:58
 */
@Controller
@RequestMapping("/local")
class LocalLogController {

    @RequestMapping("/log/{id}")
    fun log(@PathVariable id: String, request: HttpServletRequest, model: Model): String {
        val server = getServer(id) ?: return "log"
        val s = server.logFile
        val file = File(s)
        val size = file.length()
        var point = size - 5000
        if (point < 0) {
            point = 0
        }

        val read = RandomAccessFile(file, "r")
        read.seek(point)
        var result = ""
        var tmp: String?
        do {
            tmp = read.readLine()
            tmp?.let {
                var str = "${String(tmp.toByteArray(Charsets.ISO_8859_1),Charsets.UTF_8)}"
                result += when {
                    str.contains("Exception:") -> "<p style='color:#CC3399'>$str</p>"
                    str.trim().startsWith("at ") -> "<p style='color:#CC3399'>$str</p>"
                    isContainChinese(str) -> "<p style='color:#0099CC'>$str</p>"
                    else -> "$str<br>"
                }
            }
        } while (tmp != null)
        request.session.setAttribute("logPoint$id", size)
        model.addAttribute("result",result)
        return "log"
    }

    @RequestMapping("/getlog/{id}")
    @ResponseBody
    fun more(@PathVariable id: String, request: HttpServletRequest): String {
        val server = getServer(id) ?: return "log"
        val s = server.logFile
        val file = File(s)
        val size = file.length()
        var point = request.session.getAttribute("logPoint$id")

        val read = RandomAccessFile(file, "r")
        read.seek(point as Long)
        var result = ""
        var tmp: String?
        do {
            tmp = read.readLine()
            tmp?.let {
                var str = "${String(tmp.toByteArray(Charsets.ISO_8859_1),Charsets.UTF_8)}"
                result += when {
                    str.contains("Exception:") -> "<p style='color:#CC3399'>$str</p>"
                    str.trim().startsWith("at ") -> "<p style='color:#CC3399'>$str</p>"
                    isContainChinese(str) -> "<p style='color:#0099CC'>$str</p>"
                    else -> "$str<br>"
                }
            }
        } while (tmp != null)
        request.session.setAttribute("logPoint$id", size)

        return result
    }

    fun isContainChinese(str: String): Boolean {
        val p = Pattern.compile("[\u4e00-\u9fa5]")
        val m = p.matcher(str)
        return m.find()
    }

}