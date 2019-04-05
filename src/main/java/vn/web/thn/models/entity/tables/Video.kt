package vn.web.thn.models.entity.tables

import vn.web.thn.models.entity.youtube.PlayerEntity
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "videos")
class Video :Serializable{
    @Id
    @Column(name = "videoID", length = 50, nullable = false,unique = true)
    lateinit var videoID:String
    @Lob
    @Column(name = "title")
    lateinit var title:String
    @Lob

    @Column(name = "description")
    lateinit var description:String
    @Column(name = "channelID", length = 50)
    lateinit var channelID:String
    @Lob
    @Column(name = "thumbnails")
    var thumbnails:HashMap<String,String> = HashMap<String,String>()
    @Lob
    @Column(name = "channelTitle")
    lateinit var channelTitle:String
    @Lob
    @Column(name = "publishedAt")
    lateinit var publishedAt:String
    @Lob
    @Column(name = "tags")
    var tags = ArrayList<String>()
    @Lob
    @Column(name = "statistics")
    var statistics:HashMap<String,String> = HashMap<String,String>()
    @Column(name = "isDelete")
    var isDelete:Int = 0
    @Column(name = "dateUpdate",length = 50)
    var dateUpdate:String = ""
    @Lob
    @Column(name = "player")
    var player:PlayerEntity = PlayerEntity()
    @Column(name = "categoryIdVideo")
    var categoryId:String = ""
    @Column(name = "appID")
    var appID:String = "vn.android.thn.gbkids"
}