package vn.web.thn.models.entity.tables

import javax.persistence.*

@Entity
@Table(name = "api_key",uniqueConstraints=arrayOf(UniqueConstraint(columnNames = arrayOf("api_key"))))
class ApiKey {
    @Id
    @Column(name = "api_key",length = 255)
    var api_key = ""
    @Column(name = "api_limit_date")
    var api_limit_date = ""
    @Column(name = "emailApi")
    var emailApi=""
}