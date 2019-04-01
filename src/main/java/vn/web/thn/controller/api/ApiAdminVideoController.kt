package vn.web.thn.controller.api

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.thn.shop.models.response.ApiResponse
import vn.thn.shop.models.response.ErrorResponse
import vn.web.thn.ResponseCode
import vn.web.thn.models.HeaderParam
import vn.web.thn.models.entity.InsertEntity

@RestController
@RequestMapping(value = ["/admin/api/v1.0"])
class ApiAdminVideoController :ApiBaseController() {
    @RequestMapping(value = ["/insert"], method = arrayOf(RequestMethod.POST))
    fun addData(@RequestHeader headers: HttpHeaders,@RequestBody data: InsertEntity): ResponseEntity<*> {
        var appId = headers.get("appId")
        if (appId!= null){
            if (data.flag == 0 && data.listData.size>0){
                var listVideo = StringBuilder()
                for (videoID in data.listData){
                    listVideo.append(",")
                    listVideo.append(videoID)
                }
                var keyApi = getKeyApi()
                if(keyApi!= null) {
                    var data = insertVideo(listVideo.toString().substring(1),keyApi.api_key,keyApi.emailApi,appId.get(0))
                    return ResponseEntity.ok<Any>(ApiResponse(ErrorResponse(ResponseCode.NO_ERROR),appStatus(HeaderParam(headers)),data))
                }
                return ResponseEntity.ok<Any>(ApiResponse(ErrorResponse(ResponseCode.ERROR_INSERT,"error_insert"),appStatus(HeaderParam(headers)),null))

            }
            if (data.flag == 1){

            }
            if (data.flag == 2){

            }
        }
        return ResponseEntity.ok<Any>(ApiResponse(ErrorResponse(ResponseCode.NO_ERROR),appStatus(HeaderParam(headers)),null))
    }
}