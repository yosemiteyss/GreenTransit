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

/**
 * Created by kevin on 1/4/21
 */

internal class SuccessResponseConverter(
    private val delegate: Converter<ResponseBody, SuccessResponse<Any>>
) : Converter<ResponseBody, Any> {

    override fun convert(value: ResponseBody): Any? = delegate.convert(value)?.data
}

class SuccessResponseConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val dataType = TypeToken.getParameterized(SuccessResponse::class.java, type).type
        val converter: Converter<ResponseBody, SuccessResponse<Any>> = retrofit
            .nextResponseBodyConverter(
                this,
                dataType,
                annotations
            )

        return SuccessResponseConverter(converter)
    }
}

internal data class SuccessResponse<T>(
    @SerializedName(GMB_RESPONSE_TYPE)
    val type: String,

    @SerializedName(GMB_RESPONSE_VERSION)
    val version: String,

    @SerializedName(GMB_RESPONSE_GENERATED_TIMESTAMP)
    val generatedTimestamp: String,

    @SerializedName(GMB_RESPONSE_DATA)
    val data: T
)