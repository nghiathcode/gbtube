package vn.web.thn.controller.api

import com.mchange.v2.log.log4j.Log4jMLog
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.thn.shop.models.response.ApiResponse
import vn.thn.shop.models.response.ErrorResponse
import vn.web.thn.ResponseCode
import vn.web.thn.models.HeaderParam
import vn.web.thn.models.ParameterSql
import vn.web.thn.models.entity.tables.KeyWord
import vn.web.thn.models.entity.tables.Video
import vn.web.thn.models.response.VideoListResponse
import vn.web.thn.models.service.VideoService
import vn.web.thn.utils.GBUtils

@RestController
@RequestMapping(value = ["/api/v1.0"])
class ApiVideoController:ApiBaseController() {
    private val logger = Log4jMLog.getLogger(ApiVideoController::class.java)
    @RequestMapping(value = ["/{videoId}/delete"], method = arrayOf(RequestMethod.DELETE))
    fun deleteVideo(@RequestHeader headers: HttpHeaders,@PathVariable videoId: String): ResponseEntity<*> {
        var token = headers.get("token")
        var appId = headers.get("appId")
        if (token != null && appId!= null){
            val tokenCheckError = checkToken(token.get(0),appId.get(0))
            if (tokenCheckError.errorCode == ResponseCode.NO_ERROR){
                val query = " videoID = '" +videoId+"'"
                var obj =videoService.getObject<Video>(Video::class.java,query)
                if (obj!= null) {
                    obj.isDelete = 1
                    videoService.save(obj)
                }
            }
            return ResponseEntity.ok<Any>(ApiResponse(tokenCheckError,appStatus(HeaderParam(headers)),null))
        } else{
            return ResponseEntity.ok<Any>(ApiResponse(ErrorResponse(ResponseCode.TOKEN_INPUT,"not input token"),appStatus(HeaderParam(headers)),null))
        }


    }
    @RequestMapping(value = ["/new"], method = arrayOf(RequestMethod.GET))
    fun newVideoListWithCategory(@RequestHeader headers: HttpHeaders): ResponseEntity<*> {
        var token = headers.get("token")
        var appId = headers.get("appId")
        var offset = 0
        logger.info("token:"+token)
        logger.info("appId:"+appId)
        if (headers.containsKey("offset")){
            try {
                offset = headers.get("offset")!!.get(0).toInt()
            }catch (e:Exception){
                offset  = 0
            }

        }

        if (token != null && appId!= null){
            val tokenCheckError = checkToken(token.get(0),appId.get(0))
            if (tokenCheckError.errorCode == ResponseCode.NO_ERROR){
                videoService.select(Video::class.java)
                videoService.where("appID = ?0 and isDelete =?1")
                videoService.setParam(*arrayOf<ParameterSql>(ParameterSql(String::class,appId.get(0)),ParameterSql(Int::class,0)))
                videoService.limit(offset,MAX_RESULT)
                videoService.orderBy(false,"dateUpdate,videoID")
                val lst =videoService.getList<Video>()
                var result = VideoListResponse()
                result .offset = offset + lst.size
                result.videos = lst
                if (lst.size<MAX_RESULT){
                    result .offset = -1
                }

                return ResponseEntity.ok<Any>(ApiResponse(tokenCheckError,appStatus(HeaderParam(headers)),result))
            } else {
                return ResponseEntity.ok<Any>(ApiResponse(tokenCheckError,appStatus(HeaderParam(headers)),null))
            }
        } else {
            return ResponseEntity.ok<Any>(ApiResponse(ErrorResponse(ResponseCode.TOKEN_INPUT,"not input token"),appStatus(HeaderParam(headers)),null))
        }
        
    }
    @RequestMapping(value = ["/search"], method = arrayOf(RequestMethod.GET))
    fun appSearch( @RequestHeader headers: HttpHeaders,@RequestParam(value = "q", required = true) q: String?): ResponseEntity<*> {
        var token = headers.get("token")
        var appId = headers.get("appId")
        var offset = 0
        logger.info("token:"+token)
        logger.info("appId:"+appId)
        if (headers.containsKey("offset")){
            try {
                offset = headers.get("offset")!!.get(0).toInt()
            }catch (e:Exception){
                offset  = 0
            }

        }
        if (token != null && appId!= null){
            val tokenCheckError = checkToken(token.get(0),appId.get(0))
            if (tokenCheckError.errorCode == ResponseCode.NO_ERROR){
                if (!GBUtils.isEmpty(q)){
                    var keyWord = KeyWord()
                    keyWord.appID = appId.get(0)
                    keyWord.keyword = q!!
                    videoService.save(keyWord)
                }

                videoService.select(Video::class.java)
                if (GBUtils.isEmpty(q)){
                    videoService.where("appID = ?0 and isDelete =?1")
                } else {
                    videoService.where("appID = ?0 and isDelete =?1 and "+createSqlSearch(q!!))
                }

                videoService.setParam(*arrayOf<ParameterSql>(ParameterSql(String::class,appId.get(0)),ParameterSql(Int::class,0)))
                videoService.limit(offset,MAX_RESULT)
                videoService.orderBy(false,"dateUpdate,videoID")
                val lst =videoService.getList<Video>()
                var result = VideoListResponse()
                result .offset = offset + lst.size
                result.videos = lst
                if (lst.size<MAX_RESULT){
                    result .offset = -1
                }
                return ResponseEntity.ok<Any>(ApiResponse(tokenCheckError,appStatus(HeaderParam(headers)),result))
            } else {
                return ResponseEntity.ok<Any>(ApiResponse(tokenCheckError,appStatus(HeaderParam(headers)),null))
            }
        } else {
            return ResponseEntity.ok<Any>(ApiResponse(ErrorResponse(ResponseCode.TOKEN_INPUT,"not input token"),appStatus(HeaderParam(headers)),null))
        }

    }
    @RequestMapping(value = ["/next-video/{videoId}"], method = arrayOf(RequestMethod.GET))
    fun nextVideo(@PathVariable videoId: String,@RequestHeader headers: HttpHeaders): ResponseEntity<*> {
        var token = headers.get("token")
        var appId = headers.get("appId")
        var offset = 0
        logger.info("token:"+token)
        logger.info("appId:"+appId)
        if (headers.containsKey("offset")){
            try {
                offset = headers.get("offset")!!.get(0).toInt()
            }catch (e:Exception){
                offset  = 0
            }

        }
        if (token != null && appId!= null){
            val tokenCheckError = checkToken(token.get(0),appId.get(0))
            if (tokenCheckError.errorCode == ResponseCode.NO_ERROR){
                var deviceApp = appToken(token.get(0),appId.get(0))
                deviceApp!!.keyWord =videoId
                videoService.save(deviceApp)
                val query = " videoID = '" +videoId+"'"
                var obj =videoService.getObject<Video>(Video::class.java,query)
                videoService.select(Video::class.java)

                var keyword = StringBuilder()
                if (obj!= null ){
                    for (tag in obj.tags){
                        keyword.append(tag)
                        keyword.append(" ")
                    }
                    if (keyword.length == 0){
                        keyword.append(obj.title)
                    }
                }
                if (GBUtils.isEmpty(keyword.toString())){
                    videoService.where("appID = ?0 and isDelete =?1 and videoID !=:ids")
                } else {
                    videoService.where("appID = ?0 and isDelete =?1 and videoID !=:ids and ("+createSqlSearch(keyword.toString())+" )")
                }
                var listId = List<String>(1,{videoId})
                videoService.setParam(*arrayOf<ParameterSql>(ParameterSql(String::class,appId.get(0)),ParameterSql(Int::class,0)))
                videoService.setListParameter("ids",listId)
                videoService.limit(offset,MAX_RESULT)
                videoService.orderBy(false,"dateUpdate,videoID")
                val lst =videoService.getList<Video>()
                var result = VideoListResponse()
                result .offset = offset + lst.size
                result.videos = lst
                if (lst.size<MAX_RESULT){
                    result .offset = -1
                }
                var appStatus = appStatus(HeaderParam(headers))
                return ResponseEntity.ok<Any>(ApiResponse(tokenCheckError,appStatus,result))
            } else {
                return ResponseEntity.ok<Any>(ApiResponse(tokenCheckError,appStatus(HeaderParam(headers)),null))
            }
        } else {
            return ResponseEntity.ok<Any>(ApiResponse(ErrorResponse(ResponseCode.TOKEN_INPUT,"not input token"),appStatus(HeaderParam(headers)),null))
        }
    }
    private fun createSqlSearch(q:String):String{
        if (GBUtils.isEmpty(q)) return ""
        var sql = StringBuilder()
        var lstKeyWord = q.split(" ")
        sql.append(" ")
        sql.append("title like '%")
        sql.append(q)
        sql.append("%'")
        sql.append(" ")
        sql.append("OR")
        sql.append(" ")
        sql.append("description like '%")
        sql.append(q)
        sql.append("%'")
        sql.append(" ")
        sql.append("OR")
        sql.append(" ")
        sql.append("tags like '%")
        sql.append(q)
        sql.append("%'")
        sql.append(" ")
        for (keyWord in lstKeyWord){
            if (GBUtils.isEmpty(keyWord)) continue
            sql.append("OR")
            sql.append(" ")
            sql.append("title like '%")
            sql.append(keyWord)
            sql.append("%'")
            sql.append(" ")
            sql.append("OR")
            sql.append(" ")
            sql.append("description like '%")
            sql.append(keyWord)
            sql.append("%'")
            sql.append(" ")
            sql.append("OR")
            sql.append(" ")
            sql.append("tags like '%")
            sql.append(keyWord)
            sql.append("%'")
            sql.append(" ")
        }
        sql.append(" ")
        logger.info("query Search:"+sql.toString())
        return sql.toString()
    }
}