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
}