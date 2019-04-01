package vn.web.thn.models

import com.google.gson.Gson
import org.json.JSONObject
import vn.web.thn.controller.libs.YoutubeResponse
import vn.web.thn.models.entity.tables.Video
import vn.web.thn.utils.GBUtils

class SearchResponse:YoutubeResponse() {
//    var items:MutableList<Video> = ArrayList<Video>()
    var listId = StringBuilder()
    var pageToken = ""

    override fun onJsonData(data: JSONObject?) {
        if (data != null){
            if (has(data,"nextPageToken")){
                pageToken = data.getString("nextPageToken")
            }
            if (has(data,"items")){
                val jitems = data.getJSONArray("items")
                for (i in 0.. (jitems.length() -1)){
                    var jObj =jitems.getJSONObject(i)
//                    var  obj = Video()
                    if (has(jObj,"id")){
                        var id_obj = Gson().fromJson<HashMap<String,String>>(jObj.get("id").toString(),  HashMap::class.java)
                        if (id_obj.containsKey("videoId")){
                            listId.append(",")
                            listId.append(id_obj.get("videoId")!!)
//                            obj.videoID = id_obj.get("videoId")!!
                        }
                    }
                }
                if (listId.length == 0){
                    pageToken = ""
                }
            }
        }
    }
}