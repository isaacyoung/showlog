package cn.isaac.showlog

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

/**
 *
 * create by isaac at 2018/1/19 22:00
 */
@Controller
class IndexController {

    @RequestMapping("/")
    fun index(model: Model): String {
        model.addAttribute("serverList",config.serverList)
        return "index"
    }
}