package vn.web.thn.models.dao

import org.hibernate.Session
import org.springframework.stereotype.Repository
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import vn.web.thn.models.entity.tables.Video

@Repository
open class VideoDaoImpl:VideoDao {
    @Transactional
    override fun <T> save(entity: T) {
        var session = getSession()
        session.saveOrUpdate(entity)
//        try {
////            session.beginTransaction()
//            session.saveOrUpdate(entity)
////            session.transaction.commit()
//        } catch (e: Exception) {
//            throw e
//        } finally {
//            session.close()
//        }
    }

    @Autowired
    lateinit var sessionFactory: SessionFactory

    override fun getSession(): Session {
        return sessionFactory.getCurrentSession()
    }
    @Transactional
    override fun getVideos(): MutableList<Video> {
        val session = getSession()
        val cb = session.criteriaBuilder
        val cq = cb.createQuery(Video::class.java)
        val root = cq.from(Video::class.java)
        cq.select(root)
        val query = session.createQuery<Video>(cq)
        return query.getResultList()
    }

}