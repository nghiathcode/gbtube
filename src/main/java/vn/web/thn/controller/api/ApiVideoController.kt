package vn.web.thn.controller.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import vn.thn.shop.models.response.ApiResponse
import vn.thn.shop.models.response.ErrorResponse
import vn.web.thn.ResponseCode
import vn.web.thn.models.entity.tables.Video
import vn.web.thn.models.service.VideoService

@RestController
@RequestMapping(value = ["/api/v1.0"])
class ApiVideoController:ApiBaseController() {
    @RequestMapping(value = ["/new"], method = arrayOf(RequestMethod.GET))
    fun newVideoListWithCategory(@RequestHeader headers: HttpHeaders): ResponseEntity<*> {
        var token = headers.get("token")
        var appId = headers.get("appId")
        if (token != null && appId!= null){
            val tokenCheckError = checkToken(token.get(0),appId.get(0))
            if (tokenCheckError.errorCode == ResponseCode.NO_ERROR){
                val lst =videoService.getList<Video>(Video::class.java)
                return ResponseEntity.ok<Any>(ApiResponse(tokenCheckError,lst))
            } else {
                return ResponseEntity.ok<Any>(ApiResponse(tokenCheckError,null))
            }
        } else {
            return ResponseEntity.ok<Any>(ApiResponse(ErrorResponse(ResponseCode.TOKEN_INPUT,"not input token"),null))
        }


    }
}