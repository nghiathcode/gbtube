package vn.web.thn.models

import org.springframework.http.HttpHeaders

class HeaderParam (headers: HttpHeaders){
    var appId:String = ""
    var token:String = ""
    var osType:String = "0"
    var deviceType:String = "0"
    init {
        if (headers.get("token")!= null) {
            token = headers.get("token")!!.get(0)
        }
        if (headers.get("appId")!= null) {
            appId = headers.get("appId")!!.get(0)
        }

        if (headers.get("osType")!= null) {
            osType = headers.get("osType")!!.get(0)
        }

        if (headers.get("deviceType")!= null) {
            deviceType = headers.get("deviceType")!!.get(0)
        }

    }
}