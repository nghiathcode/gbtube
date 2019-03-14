package vn.web.thn.controller.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import vn.thn.shop.models.response.ApiResponse
import vn.thn.shop.models.response.ErrorResponse
import vn.web.thn.models.entity.tables.DeviceApp
import vn.web.thn.models.service.VideoService
import vn.web.thn.utils.GBUtils
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

@RestController
@RequestMapping(value = ["/api/v1.0"])
class ApiRegisterController {
    @Autowired
    private lateinit var videoService: VideoService
    @RequestMapping(value = ["/register"], method = arrayOf(RequestMethod.POST))
    fun appRegister(@RequestBody device:DeviceApp): ResponseEntity<*> {
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH,1)
        device.endTime = GBUtils.dateFormat(calendar.time,"yyyyMMddHHmmss")
        var token = StringBuilder()
        var listFields:MutableList<String> =ArrayList<String>()
        for (indexChar in device.deviceID ){
            listFields.add(indexChar.toString())
        }
        for (indexChar in device.appID ){
            listFields.add(indexChar.toString())
        }
        for (indexChar in device.deviceName ){
            listFields.add(indexChar.toString())
        }
        for (indexChar in device.deviceType ){
            listFields.add(indexChar.toString())
        }
        for (indexChar in device.deviceVersion ){
            listFields.add(indexChar.toString())
        }
        for (indexChar in device.endTime ){
            listFields.add(indexChar.toString())
        }
        for (indexChar in device.appVersion ){
            listFields.add(indexChar.toString())
        }
        Collections.shuffle(listFields)
        for (obj in listFields){
            token.append(obj)
        }
        device.token = Base64.getEncoder().encodeToString( token.toString().toByteArray(Charsets.UTF_8))
        videoService.save(device)
        return ResponseEntity.ok<Any>(ApiResponse(ErrorResponse(),device))
    }
}