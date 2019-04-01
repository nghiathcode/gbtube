package vn.web.thn.models.service

import org.hibernate.Session
import vn.web.thn.models.ParameterSql
import vn.web.thn.models.entity.tables.Video

interface VideoService {
    fun getSession(): Session
    fun getVideos():MutableList<Video>
    fun <T>getList(table:Class<*>):MutableList<T>
    fun <T> save(entity: T)
    fun <T> save(lst: MutableList<T>)
    fun <T>getObject(table:Class<*>,clause: String? = null,vararg args: ParameterSql = arrayOf()):T?
    fun select(table:Class<*>):VideoService
    fun where(where:String):VideoService
    fun limit(first:Int = 0,maxResult:Int = 0):VideoService
    fun <T>getList():MutableList<T>
    fun <T>getObject():T?
    fun setParam(vararg args: ParameterSql = arrayOf())
    fun orderBy(isASC:Boolean,column:String):VideoService
    fun search(q:String):VideoService
    fun setListParameter(paramName: String, listParameter: List<*>): VideoService
}