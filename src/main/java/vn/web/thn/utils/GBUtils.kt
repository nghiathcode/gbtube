package vn.web.thn.utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.full.cast
object GBUtils {
    val DATE_FILE = "yyyyMMddHHmmss.SSS"
    fun isEmpty(objects: Any?): Boolean {
        if (objects == null){
            return true
        } else {
            if(objects is String){
                return String::class.cast(objects).isEmpty()
            }
        }
        return false
    }
    fun dateNow(format: String): String {
        var formatResult = format
        if (isEmpty(format)) {
            formatResult = DATE_FILE
        }
        val fm = SimpleDateFormat(formatResult)
        val calendar = Calendar.getInstance()
        return fm.format(calendar.time)
    }
}