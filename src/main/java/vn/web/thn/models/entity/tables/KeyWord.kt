package vn.web.thn.models.entity.tables

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "key_word",uniqueConstraints=arrayOf(UniqueConstraint(columnNames = arrayOf("keyword", "appID"))))
class KeyWord: Serializable {
    @Id
    @Column(name = "keyword",length = 255)
    var keyword = ""
    @Id
    @Column(name = "appID",length = 30)
    var appID = ""
}