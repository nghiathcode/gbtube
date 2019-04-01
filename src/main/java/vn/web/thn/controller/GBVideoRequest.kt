package vn.web.thn.controller

import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import vn.web.thn.controller.libs.GBRequest
import vn.web.thn.controller.libs.YoutubeRequestCallBack
import vn.web.thn.controller.libs.YoutubeResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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

    override fun execute() {
//        super.execute()
        try {
            val con: HttpURLConnection
            var urlConnect = URL(makeUrl())
            con = urlConnect.openConnection() as HttpURLConnection
            con.doOutput = true
            con.doInput = true
            con.connectTimeout = 500
            con.requestMethod = "GET"
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                val inputStream = BufferedReader(
                        InputStreamReader(con.inputStream, "UTF-8"))
                var inputLine: String?=""
                val response = StringBuffer()

                while (inputLine!= null) {
                    response.append(inputLine)
                    inputLine = inputStream.readLine()
                }
                inputStream.close()
                if (mRequestListener != null) {
                    val callBack = mRequestListener as YoutubeRequestCallBack
                    callBack.onResponse(200, YoutubeResponse(response.toString()!!,responseType), this)
                }
            }
        }catch (e:Exception){
            System.out.print("error read stream")
        }
    }
    override fun onResponse(p0: Call, response: Response) {
        val body = response.body()?.string()
        if (mRequestListener != null) {
            val callBack = mRequestListener as YoutubeRequestCallBack
            callBack.onResponse(response.code(), YoutubeResponse(body!!,responseType), this)
        }

    }
}