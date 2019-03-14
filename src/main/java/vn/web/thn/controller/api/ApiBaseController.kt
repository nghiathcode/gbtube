package vn.web.thn.controller.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import vn.thn.shop.models.response.ErrorResponse
import vn.web.thn.ResponseCode
import vn.web.thn.models.ParameterSql
import vn.web.thn.models.entity.tables.DeviceApp
import vn.web.thn.models.service.VideoService
import vn.web.thn.utils.GBUtils
import java.util.*
@RestController
abstract class ApiBaseController {
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
}