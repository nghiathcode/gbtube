package vn.web.thn.controller


import org.apache.commons.io.IOUtils
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import vn.web.thn.models.entity.tables.Video
import vn.web.thn.models.service.VideoService
import java.net.URL
import com.google.gson.Gson




@Controller
class ThumbnailsController {
    @Autowired
    private lateinit var videoService: VideoService
    @RequestMapping(value = "/thumbnail/{videoid}",method = arrayOf(RequestMethod.GET))
    fun thumbnail(@PathVariable videoid: String, request: HttpServletRequest): ResponseEntity<Any> {

        var  inputStream =  URL(loadImage(videoid)).openStream()
        val media = IOUtils.toByteArray(inputStream)
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(media)
    }
    @RequestMapping(value = "/thumbnail_high/{videoid}",method = arrayOf(RequestMethod.GET))
    fun thumbnailHigh(@PathVariable videoid: String,request: HttpServletRequest): ResponseEntity<Any> {
        var  inputStream =  URL(loadImage(videoid,false)).openStream()
        val media = IOUtils.toByteArray(inputStream)
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(media)
    }
    private fun loadImage(videoId:String,isThumbnail:Boolean = true):String{
        val query = " videoID = '" +videoId+"'"
        var obj =videoService.getObject<Video>(Video::class.java,query)
        if (obj != null){
            if (isThumbnail){
                var jsonObject = JSONObject()
                if (obj.thumbnails.containsKey("default")){
                    jsonObject = JSONObject(Gson().toJson(obj.thumbnails.get("default"), Map::class.java))

                    return jsonObject.getString("url")
                } else if (obj.thumbnails.containsKey("medium")){
                    jsonObject = JSONObject(Gson().toJson(obj.thumbnails.get("medium"), Map::class.java))

                    return jsonObject.getString("url")
                }else if (obj.thumbnails.containsKey("high")){
                    jsonObject = JSONObject(Gson().toJson(obj.thumbnails.get("high"), Map::class.java))
                    return jsonObject.getString("url")
                }else if (obj.thumbnails.containsKey("standard")){
                    jsonObject =JSONObject(Gson().toJson(obj.thumbnails.get("standard"), Map::class.java))

                    return jsonObject.getString("url")
                }else if (obj.thumbnails.containsKey("maxres")){
                    jsonObject = JSONObject(Gson().toJson(obj.thumbnails.get("maxres"), Map::class.java))

                    return jsonObject.getString("url")
                }
            } else {
                var jsonObject = JSONObject()
                if (obj.thumbnails.containsKey("standard")){
                    jsonObject = JSONObject(Gson().toJson(obj.thumbnails.get("standard"), Map::class.java))

                    return jsonObject.getString("url")
                } else if (obj.thumbnails.containsKey("high")){
                    jsonObject = JSONObject(Gson().toJson(obj.thumbnails.get("high"), Map::class.java))

                    return jsonObject.getString("url")
                }else if (obj.thumbnails.containsKey("maxres")){
                    jsonObject = JSONObject(Gson().toJson(obj.thumbnails.get("maxres"), Map::class.java))

                    return jsonObject.getString("url")
                }else if (obj.thumbnails.containsKey("medium")){
                    jsonObject = JSONObject(Gson().toJson(obj.thumbnails.get("medium"), Map::class.java))

                    return jsonObject.getString("url")
                }else if (obj.thumbnails.containsKey("default")){
                    jsonObject = JSONObject(Gson().toJson(obj.thumbnails.get("default"), Map::class.java))

                    return jsonObject.getString("url")
                }
            }
        }
        return ""
    }
}