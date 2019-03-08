package vn.web.thn.controller.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import vn.web.thn.models.entity.tables.Video
import vn.web.thn.models.service.VideoService

@RestController
@RequestMapping(value = ["/api"])
class RestGetDataController {
    @Autowired
    private lateinit var videoService: VideoService
    @RequestMapping(value = ["/data"], method = arrayOf(RequestMethod.GET))
    fun homPage(): ResponseEntity<*> {
        val lst =videoService.getList<Video>(Video::class.java)
        return ResponseEntity.ok<Any>(lst)
    }
}