package vn.web.thn.models.response

import vn.web.thn.models.entity.tables.Video

class VideoListResponse {
    var offset = 0
    var videos:MutableList<Video> = ArrayList<Video>()
}