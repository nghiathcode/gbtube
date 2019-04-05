package vn.web.thn.models

import vn.web.thn.utils.GBUtils


//
// Created by NghiaTH on 2/26/19.
// Copyright (c) 2019

class SearchParam {
    val part = "snippet"
    var q:String
    var key:String
    var type ="video"//channel,playlist,video

    var order ="viewCount"//rating,date,relevance,title,videoCount,viewCount
    var pageToken = ""
    var eventType ="completed"//completed,live,upcoming
    var videoEmbeddable = "true"
    var videoDefinition = "any"
    var videoDuration = "long"
    var videoType = "movie"
    var maxResults = "50"
    var channelId = "UC3KknIJZXRygH2pZ6MDtGbg"
    constructor(q:String,keyApi:String ){
        this.key = keyApi
        this.q = q
    }
    fun toParamRequest():HashMap<String, String>{
        var param = HashMap<String, String>()
        param.put("part",part)
        param.put("key",key)
        param.put("maxResults",maxResults)
//        param.put("q",q)
        param.put("channelId",channelId)
//        param.put("eventType",eventType)
        if (!GBUtils.isEmpty(type)){
            param.put("type",type)

            if (type.equals("video",true)){
//                param.put("videoDuration",videoDuration)
                param.put("videoEmbeddable",videoEmbeddable)
//                param.put("videoType",videoType)
//                param.put("eventType",eventType)
                param.put("videoDefinition",videoDefinition)
            }
        }
        if (!GBUtils.isEmpty(order)){
            param.put("order",order)
        }
        if (!GBUtils.isEmpty(pageToken)){
            param.put("pageToken",pageToken)
        }
//        param.put("videoEmbeddable",videoEmbeddable)

        return param
    }
}
