package vn.web.thn.models

import org.springframework.http.HttpHeaders

class HeaderParam (headers: HttpHeaders){
    var appId:String = ""
    var token:String = ""
    var osType:String = "0"
    var deviceType:String = "0"
    init {
         token = headers.get("token")!!.get(0)
         appId = headers.get("appId")!!.get(0)
        osType = headers.get("osType")!!.get(0)
        deviceType = headers.get("deviceType")!!.get(0)
    }
}