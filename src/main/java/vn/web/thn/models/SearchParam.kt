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
    constructor(q:String,keyApi:String ){
        this.key = keyApi
        this.q = q
    }
    fun toParamRequest():HashMap<String, String>{
        var param = HashMap<String, String>()
        param.put("part",part)
        param.put("key",key)
        param.put("maxResults","50")
        param.put("q",q)
//        param.put("eventType",eventType)
        if (!GBUtils.isEmpty(type)){
            param.put("type",type)

//            if (type.equals("video",true)){
//                param.put("videoEmbeddable","true")
//                param.put("videoType","movie")
//                param.put("eventType","upcoming")
//            }
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
