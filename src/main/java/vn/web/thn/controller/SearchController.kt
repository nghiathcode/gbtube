package vn.web.thn.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import vn.web.thn.controller.libs.YoutubeRequestCallBack
import vn.web.thn.controller.libs.YoutubeResponse
import vn.web.thn.models.SearchParam
import vn.web.thn.models.SearchResponse
import vn.web.thn.models.VideosParam
import vn.web.thn.models.VideosResponse
import vn.web.thn.models.service.VideoService


@Controller
class SearchController {
    @Autowired
    private lateinit var videoService: VideoService
    @RequestMapping(value = "/search",method = arrayOf(RequestMethod.GET))
    fun homePage(model: Model):String{
        var api = GBVideoRequest("search")
        val param = SearchParam("kids song")
        api.mParameters = param.toParamRequest()
        api.get().execute(object :YoutubeRequestCallBack{
            override fun onResponse(httpCode: Int, response: YoutubeResponse, request: GBVideoRequest) {
                val list = response.toResponse(SearchResponse::class)!!.listId
                loadDetailVideo(list.substring(1))
                System.out.println(list.toString())
            }

            override fun onRequestError(errorRequest: Any, request: GBVideoRequest) {

            }

        })
        return "index"
    }
    private fun loadDetailVideo(videoID:String){
        var api = GBVideoRequest("videos")
        var param=VideosParam(videoID)
        api.mParameters = param.toParamRequest()
        api.get().execute(object :YoutubeRequestCallBack{
            override fun onResponse(httpCode: Int, response: YoutubeResponse, request: GBVideoRequest) {
                val list = response.toResponse(VideosResponse::class)!!.items
                videoService.save(list)
            }

            override fun onRequestError(errorRequest: Any, request: GBVideoRequest) {

            }

        })

    }
}