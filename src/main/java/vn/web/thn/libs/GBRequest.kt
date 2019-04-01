package vn.web.thn.controller.libs

import okhttp3.*
import vn.web.thn.utils.GBUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

abstract class GBRequest : Callback{
    protected var mClient: OkHttpClient
    var mParameters: HashMap<String, String> = HashMap()
    protected var mHeader: HashMap<String, String> = HashMap()
    protected var mRequestName: String = ""
    protected var mMethod: String = "GET"
    var mRequestListener: Any? = null

    var responseType: GBResponseType = GBResponseType.JSON

    var mediaType = "application/json"
        get() = field
        set(value) {
            field = value
        }

    init {
        mClient = OkHttpClient()
        mParameters = HashMap()
        mHeader = HashMap()
    }
    constructor(apiName:String){
        this.mRequestName = apiName
    }
    /**
     * addQueryParameter
     */
    fun addQueryParameter(parameterName: String, parameterValue: String) {
        this.mParameters.put(parameterName, parameterValue);
    }

    /**
     * removeQueryParameter
     */
    fun removeQueryParameter(parameterName: String) {
        this.mParameters.remove(parameterName);
    }

    /**
     * removeHeader
     */
    fun removeHeader(headerName: String) {
        this.mHeader.remove(headerName);
    }

    /**
     * addHeader
     */
    fun addHeader(headerName: String, headerValue: String) {
        this.mHeader.put(headerName, headerValue);
    }
    /**
     * GBRequest
     */
    open fun get(): GBRequest {
        mMethod = "GET"
        return this
    }

    /**
     * GBRequest
     */
    open fun <T:GBRequest>post(): T {
        mMethod = "POST"
        return this as  T
    }
    /**
     * GBRequest
     */
    open fun <T:GBRequest>delete(): T {
        mMethod = "DELETE"
        return this as  T
    }

    /**
     * makeUrl
     */
    open protected fun makeUrl(): String {
        var urlRequest:StringBuilder = StringBuilder()
        urlRequest.append(getDomain());
        if (!GBUtils.isEmpty(getPath())){
            urlRequest.append("/")
            urlRequest.append(getPath())
        }
        if (!GBUtils.isEmpty(getVersion())){
            urlRequest.append("/")
            urlRequest.append(getVersion())
        }
        if (!GBUtils.isEmpty(mRequestName)){
            urlRequest.append("/")
            urlRequest.append(mRequestName)
        }
        try {
            val urlBuilder = HttpUrl.parse(urlRequest.toString())!!.newBuilder()
            for (key in mParameters.keys) {
                urlBuilder.addEncodedQueryParameter(key, mParameters[key])
            }
            return urlBuilder.build().toString()
        } catch (e:Exception){
            return ""
        }

    }
    /**
     * execute
     */
    open fun execute() {
        try {
            val con: HttpURLConnection
            var urlConnect = URL("")
            con = urlConnect.openConnection() as HttpURLConnection
            con.doOutput = true
            con.doInput = true
            con.connectTimeout = 500
            con.requestMethod = "GET"
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                val inputStream = BufferedReader(
                        InputStreamReader(con.inputStream, "UTF-8"))
                var inputLine: String
                val response = StringBuilder()
                inputLine = inputStream.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = inputStream.readLine()
                }
                inputStream.close()
            }

            var builder = Request.Builder()

            builder = builder.url(makeUrl())
            if (mHeader.size > 0) {
                for (key in mHeader.keys) {
                    if (!GBUtils.isEmpty(mHeader[key])){
                        builder = builder.addHeader(key, mHeader[key]!!)

                    }

                }
            }
            if (mMethod.equals("POST", true)||mMethod.equals("DELETE", true)) {
                var body: RequestBody?
                if (!GBUtils.isEmpty(getBodyPost())) {
                    body = RequestBody.create(MediaType.parse(mediaType), getBodyPost())
                } else {
                    if (mParameters.size > 0) {
                        var formBody = FormBody.Builder()
                        for (key in this.mParameters.keys) {
                            if (!GBUtils.isEmpty(mParameters[key])) {
                                formBody.add(key, mParameters[key]!!)
                            }
                        }
                        formBody.build()
                        body = formBody.build()
                    } else {
                        body = RequestBody.create(MediaType.parse(mediaType), "")
                    }
                }
                builder.method(mMethod, body)
            } else {
                builder.method("GET", null)
            }
            var request: Request = builder.build()
            mClient.newCall(request).enqueue(this)
        } catch (e:Exception){
            exceptionError()
        }

    }

    /**
     *execute
     * @param requestListener
     */
    open fun execute(requestListener: Any) {
        this.mRequestListener = requestListener
        execute()
    }
    open fun exceptionError(){}
    //abstract
    abstract fun getDomain(): String

    abstract fun getVersion(): String

    abstract fun getPath(): String?

    abstract fun getBodyPost(): String
}
