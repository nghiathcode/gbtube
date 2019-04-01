package vn.web.thn.models.entity.tables

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "device_app",uniqueConstraints=arrayOf(UniqueConstraint(columnNames = arrayOf("deviceID", "appID"))))
class DeviceApp : Serializable {
    @Id
    @Column(name = "deviceID",length = 30)
//    @Column(name = "item_id",onUniqueConflicts = [ Column.ConflictAction.REPLACE],uniqueGroups =["item_id_location_id"])
    lateinit var deviceID:String
    @Id
    @Column(name = "appID",length = 30)
    lateinit var appID:String
    @Column(name = "deviceType")
    lateinit var deviceType:String
    @Column(name = "deviceName")
    lateinit var deviceName:String
    @Column(name = "deviceVersion")
    lateinit var deviceVersion:String
    @Lob
    @Column(name = "token")
    var token:String = ""
    @Column(name = "endTime")
    var endTime:String = ""
    @Column(name = "appVersion")
    var appVersion:String = ""
    @Column(name = "lastRequest")
    var lastRequest:String = ""
    @Lob
    @Column(name = "keyWord")
    var keyWord:String = ""
}