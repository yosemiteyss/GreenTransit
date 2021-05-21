//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk

package com.yosemiteyss.greentransit.data.retrofit

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.yosemiteyss.greentransit.data.constants.Constants.GMB_RESPONSE_DATA
import com.yosemiteyss.greentransit.data.constants.Constants.GMB_RESPONSE_GENERATED_TIMESTAMP
import com.yosemiteyss.greentransit.data.constants.Constants.GMB_RESPONSE_TYPE
import com.yosemiteyss.greentransit.data.constants.Constants.GMB_RESPONSE_VERSION
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

internal class GMBResponseConverter(
    private val delegate: Converter<ResponseBody, GMBResponse<Any>>
) : Converter<ResponseBody, Any> {

    override fun convert(value: ResponseBody): Any? = delegate.convert(value)?.data
}

internal class GMBResponseConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val dataType = TypeToken.getParameterized(GMBResponse::class.java, type).type
        val converter: Converter<ResponseBody, GMBResponse<Any>> = retrofit
            .nextResponseBodyConverter(
                this,
                dataType,
                annotations
            )

        return GMBResponseConverter(converter)
    }
}

internal data class GMBResponse<T>(
    @SerializedName(GMB_RESPONSE_TYPE)
    val type: String,

    @SerializedName(GMB_RESPONSE_VERSION)
    val version: String,

    @SerializedName(GMB_RESPONSE_GENERATED_TIMESTAMP)
    val generatedTimestamp: String,

    @SerializedName(GMB_RESPONSE_DATA)
    val data: T
)