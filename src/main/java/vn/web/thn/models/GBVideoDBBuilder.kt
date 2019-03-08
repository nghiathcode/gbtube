package vn.web.thn.models

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import vn.web.thn.models.entity.tables.Video

import vn.web.thn.models.service.VideoService

open class GBVideoDBBuilder {
    lateinit var table: Class<*>
    lateinit var videoService: VideoService
    companion object {
        fun newInstance(videoService: VideoService, table: Class<*>):GBVideoDBBuilder{
            return GBVideoDBBuilder(videoService,table)
        }
    }
    constructor(videoService: VideoService, table: Class<*>){
        this.table = table
        this.videoService = videoService
    }
    @Transactional
    open fun getList():MutableList<Video>{
        var session = videoService.getSession()
        val cb = session.criteriaBuilder
        val cq = cb.createQuery(Video::class.java)
        val root = cq.from(Video::class.java)
        cq.select(root)
        val query = session.createQuery<Video>(cq)
        return query.getResultList()
    }
}