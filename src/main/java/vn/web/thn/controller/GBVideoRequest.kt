package vn.web.thn.controller

import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import vn.web.thn.controller.libs.GBRequest
import vn.web.thn.controller.libs.YoutubeRequestCallBack
import vn.web.thn.controller.libs.YoutubeResponse
import java.io.IOException

open class GBVideoRequest:GBRequest {

    var dataBody: Any? = null
    constructor(apiName:String) : super(apiName) {

    }
    override fun getDomain(): String {
        return "https://www.googleapis.com"
    }

    override fun getVersion(): String {
        return "v3"
    }

    override fun getPath(): String? {
        return "youtube"
    }

    override fun getBodyPost(): String {
        if (dataBody == null) {
            return ""
        } else{
            return Gson().toJson(dataBody)
        }
    }

    override fun onFailure(p0: Call, p1: IOException) {

    }

    override fun onResponse(p0: Call, response: Response) {
        val body = response!!.body()?.string()
        if (mRequestListener != null) {
            val callBack = mRequestListener as YoutubeRequestCallBack
            callBack!!.onResponse(response.code(), YoutubeResponse(body!!,responseType), this)
        }

    }
}