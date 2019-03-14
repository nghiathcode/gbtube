package vn.web.thn.models.entity.tables

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "app")
class App :Serializable{
    @Id
    @Column(name = "appID")
    lateinit var appID:String
    @Column(name = "appName")
    lateinit var appName:String
}