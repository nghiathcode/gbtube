package vn.web.thn.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMethod
import vn.web.thn.models.GBVideoDBBuilder
import vn.web.thn.models.entity.tables.Video
import vn.web.thn.models.service.VideoService


@Controller
class HomeController {
    @Autowired
    private lateinit var videoService: VideoService
    @RequestMapping(value = "/",method = arrayOf(RequestMethod.GET))
    fun homePage(model: Model):String{
//        val lst =videoService.getList<Video>(Video::class.java)
//        if (lst.size>0) {
//            System.out.println(lst.get(0).videoID)
//        }
        return "index"
    }
}