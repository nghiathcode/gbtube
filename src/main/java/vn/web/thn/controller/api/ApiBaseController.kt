package vn.web.thn.controller.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import vn.thn.shop.models.response.ErrorResponse
import vn.web.thn.ResponseCode
import vn.web.thn.controller.GBVideoRequest
import vn.web.thn.controller.libs.YoutubeRequestCallBack
import vn.web.thn.controller.libs.YoutubeResponse
import vn.web.thn.models.ParameterSql
import vn.web.thn.models.VideosParam
import vn.web.thn.models.VideosResponse
import vn.web.thn.models.entity.tables.ApiKey
import vn.web.thn.models.entity.tables.DeviceApp
import vn.web.thn.models.entity.tables.Video
import vn.web.thn.models.service.VideoService
import vn.web.thn.utils.GBUtils
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import vn.web.thn.models.HeaderParam
import vn.web.thn.models.entity.tables.App
import java.io.InputStreamReader
import java.io.BufferedReader



@RestController
abstract class ApiBaseController {
    val MAX_RESULT = 50
    @Autowired
    protected lateinit var videoService: VideoService
    fun checkToken(token:String,appId:String): ErrorResponse {
        var calendar = Calendar.getInstance()
        var currentTime = GBUtils.dateFormat(calendar.time,"yyyyMMddHHmmss")
        var obj = videoService.getObject<DeviceApp>(DeviceApp::class.java,"token = ?0 and appID=?1", *arrayOf<ParameterSql>(ParameterSql(String::class,token),ParameterSql(String::class,appId)))
        if (obj == null){
            return ErrorResponse(ResponseCode.TOKEN_REGISTER,"app not register")
        } else if (obj.endTime < currentTime){
            return ErrorResponse(ResponseCode.TOKEN_EXPIRED)
        } else {
            obj.lastRequest = currentTime
            videoService.save(obj)
            return ErrorResponse(ResponseCode.NO_ERROR)
        }
    }
    protected fun appToken(token:String,appId:String):DeviceApp?{
        return videoService.getObject<DeviceApp>(DeviceApp::class.java,"token = ?0 and appID=?1", *arrayOf<ParameterSql>(ParameterSql(String::class,token),ParameterSql(String::class,appId)))
    }
    protected fun getKeyApi():ApiKey?{
        var calendar = Calendar.getInstance()
        var dateNow = GBUtils.dateFormat(calendar.time,"yyyyMMdd")
        videoService.select(ApiKey::class.java)
        videoService.where("api_limit_date < ?0")
        videoService.setParam(*arrayOf<ParameterSql>(ParameterSql(String::class,dateNow)))
        val lstKey = videoService.getList<ApiKey>()
        if (lstKey.size>0){
            return lstKey.get(0)
        } else {
            return null
        }
    }
    protected fun insertVideo(videoIDList:String,apiKey:String,email:String,appId:String):MutableList<Video>{
        var resultData:MutableList<Video> = ArrayList<Video>()
        var api = GBVideoRequest("videos")
        var param= VideosParam(videoIDList,apiKey)
        api.mParameters = param.toParamRequest()
        api.get().execute(object : YoutubeRequestCallBack {
            override fun onResponse(httpCode: Int, response: YoutubeResponse, request: Any) {
                var result = response.toResponse(VideosResponse::class,videoService)!!
                if (!result.apiLimit) {
                    val list = result.items
                    for (item in list){
                        item.appID = appId
                        resultData.add(item)
                        videoService.save(item)
                    }

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
                    val lstKey = getKeyApi()
                    if (lstKey!= null){
                        resultData= insertVideo(videoIDList,lstKey.api_key,lstKey.emailApi,appId)
                    }

                }
            }
            override fun onRequestError(errorRequest: Any, request: GBVideoRequest) {

            }

        })


        return resultData
    }
    protected fun appStatus(headerParam: HeaderParam): App? {
        videoService.select(App::class.java)
        videoService.where("appId = ?0 and osType=?1 and deviceType=?2")
        videoService.setParam(*arrayOf<ParameterSql>(ParameterSql(String::class,headerParam.appId),ParameterSql(Int::class,headerParam.osType.toInt()),ParameterSql(Int::class,headerParam.deviceType.toInt())))
        var result = videoService.getObject<App>()
        return result
    }
}