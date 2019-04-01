package vn.web.thn.controller
import com.mchange.v2.log.log4j.Log4jMLog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import vn.web.thn.models.service.VideoService

@Controller
class PlayController {
    private val logger = Log4jMLog.getLogger(PlayController::class.java)
    @Autowired
    private lateinit var videoService: VideoService
    @RequestMapping(value = "/play/{videoId}",method = arrayOf(RequestMethod.GET))
    fun homePage(model: Model, @PathVariable(value ="videoId",required = true ) videoId: String):String{
        return "index"
    }
}