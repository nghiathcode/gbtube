package vn.web.thn.models

class VideosParam {
    var part = "snippet,contentDetails,statistics"
    var key:String
    var id= ""
    constructor(video_id:String,keyApi:String = "AIzaSyDE_FRgWApb2U9zKRSc1wriLH4w5i1clfA"){
        this.key = keyApi
        this.id = video_id
    }
    fun toParamRequest():HashMap<String, String>{
        var param = HashMap<String, String>()
        param.put("key",key)
        param.put("part",part)
        param.put("id",id)
        return param
    }
}