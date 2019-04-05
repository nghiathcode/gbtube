package vn.web.thn.controller.libs
import vn.web.thn.controller.GBVideoRequest

interface YoutubeRequestCallBack {
    /**
     *
     * @param response
     * @param objRequest
     */
    fun  onResponse(httpCode: Int,response: YoutubeResponse, request: Any)

    /**
     *
     * @param errorRequest
     * @param objRequest
     */
    fun  onRequestError(errorRequest: Any, request: GBVideoRequest)
}
