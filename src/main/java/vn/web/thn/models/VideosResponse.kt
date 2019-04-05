package vn.web.thn.models

import com.google.gson.Gson
import org.json.JSONObject
import vn.web.thn.controller.libs.YoutubeResponse
import vn.web.thn.models.entity.tables.Video
import vn.web.thn.models.entity.youtube.PlayerEntity
import vn.web.thn.models.entity.youtube.StatusEntity
import vn.web.thn.utils.GBUtils

class VideosResponse: YoutubeResponse() {
    var items:MutableList<Video> = ArrayList<Video>()
    override fun onJsonData(data: JSONObject?) {
        if (data != null){

            var status:StatusEntity = StatusEntity()
            var player:PlayerEntity = PlayerEntity()
            var liveBroadcastContent = ""
            if (has(data,"items")){
                val jitems = data.getJSONArray("items")
                for (i in 0.. (jitems.length() -1)){
                    var jObj =jitems.getJSONObject(i)
                    var  obj = Video()
                    if (has(jObj,"id")){
                        obj.videoID = jObj.getString("id")
                    }
//                    if (has(jObj,"contentDetails")){
//                        var contentDetails = jObj.getJSONObject("contentDetails")
//                        if (has(contentDetails,"regionRestriction")){
//                            val regionRestriction = contentDetails.getJSONObject("regionRestriction")
//                            if (has(regionRestriction,"blocked")){
//                                val lst = Gson().fromJson<ArrayList<String>>(regionRestriction.get("blocked").toString(),ArrayList::class.java)
//                                if (lst.size>0){
//                                    continue
//                                }
//                            }
//                        }
//                    }
                    if (has(jObj,"snippet")){

                        var snippet = jObj.getJSONObject("snippet")
                        if (has(snippet,"liveBroadcastContent")){
                            liveBroadcastContent = snippet.getString("liveBroadcastContent")
                            if (liveBroadcastContent.equals("live",true)){
                                continue
                            }
                        }
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
                        if (has(snippet,"categoryId")){
                            obj.categoryId = snippet.getString("categoryId")
                        }

                    }

                    if (has(jObj,"statistics")){
                        obj.statistics = Gson().fromJson<HashMap<String,String>>(jObj.get("statistics").toString(),  HashMap::class.java)
                    }
                    if (has(jObj,"status")){
                        status =Gson().fromJson<StatusEntity>(jObj.get("status").toString(),StatusEntity::class.java)

                    }
                    if (has(jObj,"player")){
                        obj.player = Gson().fromJson<PlayerEntity>(jObj.get("player").toString(),PlayerEntity::class.java)
                    }
//                    if (status.privacyStatus.equals("public",true) && status.publicStatsViewable){
                    if (status.privacyStatus.equals("public",true) ){
                        obj.dateUpdate = GBUtils.dateNow("")
                        if (videoService!= null) {
                            videoService!!.select(Video::class.java)
                            videoService!!.where("videoID = ?0")
                            videoService!!.setParam(*arrayOf<ParameterSql>(ParameterSql(String::class,obj.videoID)))

                            if (videoService!!.getObject<Video>()== null){
                                items.add(obj)
                            }
                        } else {
                            items.add(obj)
                        }
                    }

                }
            }
        }
    }
}