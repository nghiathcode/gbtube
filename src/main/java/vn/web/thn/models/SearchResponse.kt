package vn.web.thn.models

import com.google.gson.Gson
import org.json.JSONObject
import vn.web.thn.controller.libs.YoutubeResponse
import vn.web.thn.models.entity.tables.Video
import vn.web.thn.utils.GBUtils

class SearchResponse:YoutubeResponse() {
//    var items:MutableList<Video> = ArrayList<Video>()
    var listId = StringBuilder()

    override fun onJsonData(data: JSONObject?) {
        if (data != null){
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
//                    if (has(jObj,"snippet")){
//                        var snippet = jObj.getJSONObject("snippet")
//                        if (has(snippet,"publishedAt")){
//                            obj.publishedAt = snippet.getString("publishedAt")
//                        }
//                        if (has(snippet,"channelId")){
//                            obj.channelID = snippet.getString("channelId")
//                        }
//                        if (has(snippet,"title")){
//                            obj.title = snippet.getString("title")
//                        }
//                        if (has(snippet,"description")){
//                            obj.description = snippet.getString("description")
//                        }
//                        if (has(snippet,"thumbnails")){
//                            obj.thumbnails = Gson().fromJson<HashMap<String,String>>(snippet.get("thumbnails").toString(),  HashMap::class.java)
//                        }
//                        if (has(snippet,"channelTitle")){
//                            obj.channelTitle = snippet.getString("channelTitle")
//                        }
//                    }
//                    obj.dateUpdate = GBUtils.dateNow("")+i.toString()
//                    items.add(obj)

                }
            }
        }
    }
}