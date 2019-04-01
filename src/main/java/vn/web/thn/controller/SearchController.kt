package vn.web.thn.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import vn.web.thn.controller.libs.YoutubeRequestCallBack
import vn.web.thn.controller.libs.YoutubeResponse
import vn.web.thn.models.*
import vn.web.thn.models.entity.tables.ApiKey
import vn.web.thn.models.service.VideoService
import vn.web.thn.utils.GBUtils
import java.util.*


@Controller
class SearchController {
    @Autowired
    private lateinit var videoService: VideoService
    @RequestMapping(value = "/search",method = arrayOf(RequestMethod.GET))
    fun homePage(model: Model,@RequestParam(value = "q", required = true) q: String?):String{
        if (!GBUtils.isEmpty(q)) {
            getSearch(q!!)
        }
        return "index"
    }
    private fun getSearch(q:String,pageToken:String = ""){

        var api = GBVideoRequest("search")
        var calendar = Calendar.getInstance()
        var dateNow = GBUtils.dateFormat(calendar.time,"yyyyMMdd")
        videoService.select(ApiKey::class.java)
        videoService.where("api_limit_date < ?0")
        videoService.setParam(*arrayOf<ParameterSql>(ParameterSql(String::class,dateNow)))
        val lstKey = videoService.getList<ApiKey>()
        if (lstKey.size>0){
//            val param = SearchParam(q,lstKey.get(0).api_key)
            val param = SearchParam(q,lstKey.get(0).api_key)
            var keyApi = lstKey.get(0).api_key
            param.pageToken = pageToken
            api.mParameters = param.toParamRequest()

            api.get().execute(object :YoutubeRequestCallBack{
                override fun onResponse(httpCode: Int, response: YoutubeResponse, request: GBVideoRequest) {
                    var result = response.toResponse(SearchResponse::class,videoService)!!
                    if (!result.apiLimit) {
                        if (result.listId.length > 0) {
                            val list = result.listId
                            var pageToken = result.pageToken
                            System.out.println("pageToken:" + pageToken)
                            loadDetailVideo(list.substring(1), keyApi,lstKey.get(0).emailApi)
                            if (!GBUtils.isEmpty(pageToken)) {
                                getSearch(pageToken)
                            }
                        }

                    } else {
                        var api = lstKey.get(0)
                        api.api_limit_date = dateNow
                        videoService.save(api)
                        getSearch(pageToken)
                    }
                }

                override fun onRequestError(errorRequest: Any, request: GBVideoRequest) {

                }

            })
        }

    }
    private fun loadDetailVideo(videoID:String,apiKey:String,email:String){
        var api = GBVideoRequest("videos")
        var param=VideosParam(videoID,apiKey)
        api.mParameters = param.toParamRequest()
        api.get().execute(object :YoutubeRequestCallBack{
            override fun onResponse(httpCode: Int, response: YoutubeResponse, request: GBVideoRequest) {
                var result = response.toResponse(VideosResponse::class,videoService)!!
                if (!result.apiLimit) {
                    val list = result.items
                    videoService.save(list)
                } else {
                    var calendar = Calendar.getInstance()
                    var dateNow = GBUtils.dateFormat(calendar.time,"yyyyMMdd")
                    var apiKeyupdate = ApiKey()
                    apiKeyupdate.api_limit_date =dateNow
                    apiKeyupdate.emailApi = email
                    apiKeyupdate.api_key = apiKey
                    videoService.save(apiKeyupdate)
                    videoService.select(ApiKey::class.java)
                    videoService.where("api_limit_date < ?0")
                    videoService.setParam(*arrayOf<ParameterSql>(ParameterSql(String::class,dateNow)))
                    val lstKey = videoService.getList<ApiKey>()

                    if (lstKey.size>0){
                        loadDetailVideo(videoID,lstKey.get(0).api_key,email)
                    }

                }
            }
            override fun onRequestError(errorRequest: Any, request: GBVideoRequest) {

            }

        })

    }
}