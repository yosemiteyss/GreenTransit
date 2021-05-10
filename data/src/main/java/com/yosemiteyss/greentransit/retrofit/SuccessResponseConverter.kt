package com.yosemiteyss.greentransit.retrofit

import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.yosemiteyss.greentransit.constants.Constants.GMB_DATA
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
    @SerializedName("type")
    val type: String,

    @SerializedName("version")
    val version: String,

    @SerializedName("generated_timestamp")
    val generatedTimestamp: String,

    @SerializedName(GMB_DATA)
    val data: T
)