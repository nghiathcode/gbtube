package vn.web.thn.models.entity

import java.io.Serializable

class InsertEntity:Serializable {
    var flag = 0//0:video,1:playList,2:channel
    var  listData:MutableList<String> = ArrayList()
}