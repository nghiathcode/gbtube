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
    override fun orderBy(isASC: Boolean,column:String): VideoService {
        var listColumns = column.split(",")
        var orderBy = StringBuilder()
        for (col in listColumns){
            if (isASC){
                orderBy.append(",")
                orderBy.append(col)
                orderBy.append(" ")
                orderBy.append("ASC")
                orderBy.append(" ")
//                ORDER_BY = " order by "+ column+ " ASC"
            } else {
                orderBy.append(",")
                orderBy.append(col)
                orderBy.append(" ")
                orderBy.append("DESC")
                orderBy.append(" ")
//                ORDER_BY = " order by "+ column + " DESC"
            }
        }
        ORDER_BY =" order by "+ orderBy.substring(1).toString()
        return this
    }
    override fun <T> getList(): MutableList<T> {
        try{
            val session = getSession()
            val strQuery = StringBuilder()
            strQuery.append("from ")
            strQuery.append(this.table.simpleName)
            if (!GBUtils.isEmpty(sqlWhere)){
                strQuery.append(" where $sqlWhere")
            }
            if (!GBUtils.isEmpty(ORDER_BY)){
                strQuery.append(ORDER_BY)
            }
            var query: Query<*> = session.createQuery(strQuery.toString())
            if (params.size > 0 && !GBUtils.isEmpty(sqlWhere)) {
                query = setParam(query, params)
            }
            if (listParameter.size > 0) {
                val paramList = listParameter
                for (paramName in paramList.keys) {
                    query.setParameterList(paramName, paramList[paramName])
                }
            }
            if (maxResults > 0) {
                query.maxResults = maxResults
                query.firstResult = firstResult
            }
            initParam()
            return query.resultList as MutableList<T>
        } catch (e:Exception){
            initParam()
            return ArrayList<T>()
        }

    }

    override fun <T> getObject(): T? {

        try {
            val session = getSession()
            val strQuery = StringBuilder()
            strQuery.append("from ")
            strQuery.append(this.table.simpleName)
            if (!GBUtils.isEmpty(sqlWhere)){
                strQuery.append(" where $sqlWhere")
            }
            var query: Query<*> = session.createQuery(strQuery.toString())
            if (params.size > 0 && !GBUtils.isEmpty(sqlWhere)) {
                query = setParam(query, params)
            }
            if (listParameter.size > 0) {
                val paramList = listParameter
                for (paramName in paramList.keys) {
                    query.setParameterList(paramName, paramList[paramName])
                }
            }
            initParam()
            return query.singleResult as T
        } catch (e:Exception){
            initParam()
            System.out.println(e.message)
            return null
        }
    }

    override fun search(q: String): VideoService {
        this.textSearch = q
        return this
    }
    override fun setParam(vararg args: ParameterSql) {
        params.clear()
        params = args.toMutableList()
    }
    private var ORDER_BY = ""
    private var textSearch = ""
    private lateinit var table:Class<*>
    private var firstResult:Int = 0
    private var maxResults:Int = 0
    private var sqlWhere = ""
    private var params:MutableList<ParameterSql> = ArrayList<ParameterSql>()
    var listParameter = HashMap<String, List<*>>()
    private fun initParam(){
        ORDER_BY = ""
        textSearch = ""
        firstResult = 0
        maxResults = 0
        sqlWhere = ""
        params = ArrayList<ParameterSql>()
        listParameter.clear()
        listParameter = HashMap<String, List<*>>()
    }
    override fun setListParameter(paramName: String, listParameter: List<*>): VideoService {
        this.listParameter[paramName] = listParameter
        return this
    }
    override fun limit(first: Int, maxResult: Int): VideoService {
        this.firstResult = first
        this.maxResults = maxResult
        return this
    }
    override fun select(table: Class<*>): VideoService {
        this.table = table
        return this
    }

    override fun where(where: String): VideoService {
        this.sqlWhere = where
        return this
    }

    @Autowired
    private lateinit var videoDAO: VideoDao
    override fun <T> getList(table:Class<*>): MutableList<T> {
        val session = getSession()
        val strQuery = StringBuilder()
        strQuery.append("from ")
        strQuery.append(table.simpleName)
        var query: Query<*> = session.createQuery(strQuery.toString())
        initParam()
        return query.resultList as MutableList<T>
    }
    override fun getVideos(): MutableList<Video> {
        return videoDAO.getVideos()
    }
    override fun getSession(): Session {
        return videoDAO.getSession()
    }

    override fun <T> save(entity: T) {
        val session = getSession()
        session.saveOrUpdate(entity)
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

        try {
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
            if (listParameter.size > 0) {
                val paramList = listParameter
                for (paramName in paramList.keys) {
                    query.setParameterList(paramName, paramList[paramName])
                }
            }
            initParam()
            return query.singleResult as T
        } catch (e:Exception){
            initParam()
            System.out.println(e.message)
            return null
        }

    }

}