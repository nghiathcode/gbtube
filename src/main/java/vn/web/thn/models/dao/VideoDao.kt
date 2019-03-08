package vn.web.thn.models.dao

import org.hibernate.Session
import vn.web.thn.models.entity.tables.Video

interface VideoDao {
    fun getSession(): Session
    fun getVideos():MutableList<Video>
    fun <T> save(entity: T)

}