package cn.isaac.showlog

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest

/**
 * 服务器日志
 * create by isaac at 2018/1/19 8:58
 */
@Controller
class LogController {

    @RequestMapping("/log/{id}")
    fun log(@PathVariable id: String, request: HttpServletRequest, model: Model): String {
        val server = getServer(id) ?: return "log"
        val (size,content) = connect(id)
        if (content == "") {
            return "log"
        }

        val result = toHtml(content)

        request.session.setAttribute("logPoint$id", size)
        model.addAttribute("result",result)
        model.addAttribute("id", id)
        model.addAttribute("isRemote", server.isRemote)
        return "log"
    }

    @RequestMapping("/getlog/{id}")
    @ResponseBody
    fun more(@PathVariable id: String, request: HttpServletRequest): String {
        var point = request.session.getAttribute("logPoint$id")
        point?:let { point = 0L }
        val (size,content) = connect(id, point as Long)
        if (content == "") {
            return content
        }

        val result = toHtml(content)

        request.session.setAttribute("logPoint$id", size)

        return result
    }

    fun isContainChinese(str: String): Boolean {
        val p = Pattern.compile("[\u4e00-\u9fa5]")
        val m = p.matcher(str)
        return m.find()
    }

    fun toHtml(content: String): String {
        if (content == "") {
            return content
        }

        var result = ""
        var temp = ""
        content.forEach {
            when (it) {
                '\n' -> {
                    temp += "<br>"
                    result += when {
                        temp.contains("Exception:") -> "<p style='color:#CC3399'>$temp</p>"
                        temp.trim().startsWith("at ") -> "<p style='color:#CC3399'>$temp</p>"
                        isContainChinese(temp) -> "<p style='color:#0099CC'>$temp</p>"
                        else -> "<p>$temp</p>"
                    }
                    temp = ""
                }
                else -> temp += it
            }
        }
        return result
    }
}