package vn.web.thn.models

import kotlin.reflect.KClass
import kotlin.reflect.full.cast

class ParameterSql {
    private var mClassType: KClass<*>
    private var mValue: Any

    constructor(classType: KClass<*>, value: Any) {
        mClassType = classType
        mValue = value
    }

    fun getValue(): Any {
        return mClassType.cast(mValue)
    }
}