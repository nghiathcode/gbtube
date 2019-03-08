package vn.web.thn.models

import com.google.gson.Gson
import org.json.JSONObject
import vn.web.thn.controller.libs.YoutubeResponse
import vn.web.thn.models.entity.tables.Video
import vn.web.thn.utils.GBUtils

class VideosResponse: YoutubeResponse() {
    var items:MutableList<Video> = ArrayList<Video>()
    override fun onJsonData(data: JSONObject?) {
        if (data != null){
            if (has(data,"items")){
                val jitems = data.getJSONArray("items")
                for (i in 0.. (jitems.length() -1)){
                    var jObj =jitems.getJSONObject(i)
                    var  obj = Video()
                    if (has(jObj,"id")){
                        obj.videoID = jObj.getString("id")
                    }
                    if (has(jObj,"snippet")){
                        var snippet = jObj.getJSONObject("snippet")
                        if (has(snippet,"publishedAt")){
                            obj.publishedAt = snippet.getString("publishedAt")
                        }
                        if (has(snippet,"channelId")){
                            obj.channelID = snippet.getString("channelId")
                        }
                        if (has(snippet,"title")){
                            obj.title = snippet.getString("title")
                        }
                        if (has(snippet,"description")){
                            obj.description = snippet.getString("description")
                        }
                        if (has(snippet,"thumbnails")){
                            obj.thumbnails = Gson().fromJson<HashMap<String,String>>(snippet.get("thumbnails").toString(),  HashMap::class.java)
                        }
                        if (has(snippet,"channelTitle")){
                            obj.channelTitle = snippet.getString("channelTitle")
                        }
                        if (has(snippet,"tags")){
                            obj.tags =Gson().fromJson<ArrayList<String>>(snippet.get("tags").toString(),  ArrayList::class.java)
                        }

                    }
                    if (has(jObj,"statistics")){
                        obj.statistics = Gson().fromJson<HashMap<String,String>>(jObj.get("statistics").toString(),  HashMap::class.java)
                    }
                    obj.dateUpdate = GBUtils.dateNow("")
                    items.add(obj)
                }
            }
        }
    }
}