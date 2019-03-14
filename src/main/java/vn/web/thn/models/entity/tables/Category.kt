package vn.web.thn.models.entity.tables

import javax.persistence.*

@Entity
@Table(name = "categories")
class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
    var name: String = ""
    var root = 0
    var path: String = ""
    var isDelete = 0
    var dateUpdate: String = ""
}