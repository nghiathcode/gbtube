package vn.web.thn.models.entity.tables

import java.io.Serializable
import javax.persistence.*

@Entity

@Table(name = "app",uniqueConstraints=arrayOf(UniqueConstraint(columnNames = arrayOf("appID", "osType","deviceType"))))
class App :Serializable{
    @Id
    @Column(name = "appID",length = 100)
    lateinit var appID:String//bunder ID
    @Column(name = "appName")
    lateinit var appName:String
    @Id
    @Column(name = "osType",length = 50)
    var osType:Int = 0//1:IOS,0:Android
    @Column(name = "OsName")
    lateinit var OsName:String//IOS,Android
    @Id
    @Column(name = "deviceType",length = 2)
    var deviceType:Int = 0//0:mobile,table,1:tv
    @Column(name = "deviceTypeName")
    var deviceTypeName:String = "mobile,table"//0:mobile,table,1:tv
    @Column(name = "status")
    var status = 0//0:pendding,1:apply
    //vn.android.thn.gbkids
}