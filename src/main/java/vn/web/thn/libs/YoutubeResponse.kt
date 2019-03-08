package vn.web.thn.controller.libs

import vn.web.thn.utils.GBUtils
import kotlin.reflect.KClass
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.reflect.full.createInstance


//
// Created by NghiaTH on 2/26/19.
// Copyright (c) 2019

open class YoutubeResponse() {
    val TAG = "YoutubeResponse"
    lateinit var responseType: GBResponseType
    lateinit var dataResponse: String

    init {

    }
    constructor(response: String, responseType: GBResponseType):this() {
        this.dataResponse = response
        this.responseType = responseType
    }

    open fun <T:YoutubeResponse>toResponse(clazz: KClass<out T>, callBackError:Any? = null): T? {
        val response: YoutubeResponse
        try {
            response = clazz.createInstance()
            response.dataResponse = dataResponse
            response.responseType = responseType
            response.parser()
            return response
        } catch (e: InstantiationException) {
//            if (e.message!=null){
//                GBLog.error(TAG,e.message!!,app.isDebugMode())
//            }else {
//                GBLog.error(TAG,"toResponse InstantiationException")
//            }

            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
//            if (e.message!=null){
//                GBLog.error(TAG,e.message!!)
//            }else {
//                GBLog.error(TAG,"toResponse IllegalAccessException")
//            }
            return null
        }

    }
    fun has(jsonObject: JSONObject, key:String):Boolean{
        if (jsonObject.has(key)){
            return !jsonObject.isNull(key)
        }
        return false;
    }
    fun parser() {
        if (responseType == GBResponseType.JSON){
            if (!GBUtils.isEmpty(dataResponse)){
                val dataJson = JSONTokener(dataResponse).nextValue()
                if (dataJson is JSONObject){
                    onJsonData(dataJson)
                } else if (dataJson is JSONArray){
                    onJsonArrayData(dataJson)
                }
            }
        } else {
            onTextData(dataResponse)
        }
    }
    open fun onJsonData(data:JSONObject?){}
    open  fun onJsonArrayData(data:JSONArray?){}
    open  fun onTextData(data:String?){}
}
