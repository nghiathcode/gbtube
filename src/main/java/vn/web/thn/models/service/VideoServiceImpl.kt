package vn.web.thn.models.service

import org.hibernate.Session
import org.springframework.stereotype.Service
import vn.web.thn.models.entity.tables.Video
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import vn.web.thn.models.dao.VideoDao
import org.hibernate.query.Query
import vn.web.thn.models.ParameterSql
import vn.web.thn.utils.GBUtils


@Service
@Transactional
open class VideoServiceImpl:VideoService {

    @Autowired
    private lateinit var videoDAO: VideoDao
    override fun <T> getList(table:Class<*>): MutableList<T> {
        val session = getSession()
        val strQuery = StringBuilder()
        strQuery.append("from ")
        strQuery.append(table.simpleName)
        var query: Query<*> = session.createQuery(strQuery.toString())
        return query.resultList as MutableList<T>
    }
    override fun getVideos(): MutableList<Video> {
        return videoDAO.getVideos()
    }
    override fun getSession(): Session {
        return videoDAO.getSession()
    }

    override fun <T> save(entity: T) {
        videoDAO.save(entity)
    }

    override fun <T> save(lst: MutableList<T>) {
        val session = getSession()
//        session.beginTransaction()
        for (obj in lst){
            session.saveOrUpdate(obj)
        }
//        session.transaction.commit()
//        session.close()
    }
    fun <T>setParam(query: Query<T>, args:MutableList<ParameterSql>): Query<T> {
        if (args.size > 0) {
            var paramIndex = 0
            for (obj in args) {
                query.setParameter(paramIndex, obj.getValue())
                paramIndex++
            }
        }
        return query
    }
    override fun <T> getObject(table:Class<*>,clause: String?,vararg args: ParameterSql ): T? {
        val session = getSession()
        val strQuery = StringBuilder()
        strQuery.append("from ")
        strQuery.append(table.simpleName)

        if (!GBUtils.isEmpty(clause)){
            strQuery.append(" where $clause")
        }
        var query: Query<*> = session.createQuery(strQuery.toString())
        if (args.size > 0 && !GBUtils.isEmpty(clause)) {
            query = setParam(query, args.toMutableList())
        }
        try {
            return query.singleResult as T
        } catch (e:Exception){
            System.out.println(e.message)
            return null
        }

    }
}