package com.yosemiteyss.greentransit.retrofit

import com.yosemiteyss.greentransit.states.Resource
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by kevin on 12/18/20
 */

internal class ResourceCall<T>(private val delegate: Call<T>) : Call<Resource<T>> {

    override fun clone(): Call<Resource<T>> = ResourceCall(delegate.clone())

    override fun execute(): Response<Resource<T>> {
        throw UnsupportedOperationException("ResourceCall doesn't support execute")
    }

    override fun enqueue(callback: Callback<Resource<T>>) {
        return delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                //val code = response.code()

                if (response.isSuccessful) {
                    if (body != null) {
                        // Success response
                        callback.onResponse(
                            this@ResourceCall,
                            Response.success(Resource.Success(body))
                        )
                    } else {
                        // Empty response
                        callback.onResponse(
                            this@ResourceCall,
                            Response.success(Resource.Error("Empty response."))
                        )
                    }
                } else {
                    val errorBody = response.errorBody()
                    val error = when {
                        errorBody == null -> null
                        errorBody.contentLength() == 0L -> null
                        else -> convertErrorBody(errorBody)
                    }

                    callback.onResponse(
                        this@ResourceCall,
                        Response.success(Resource.Error(error))
                    )
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onResponse(
                    this@ResourceCall,
                    Response.success(Resource.Error(t.message))
                )
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled() = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

    private fun convertErrorBody(errorBody: ResponseBody): String {
        val errorResponse = errorBody.string()
        return errorResponse
    }
}

class ResourceCallAdapter<T>(
    private val successType: Type
) : CallAdapter<T, Call<Resource<T>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<T>): Call<Resource<T>> {
        return ResourceCall(call)
    }
}

class ResourceCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        check(returnType is ParameterizedType) {
            "Return type must be parameterized"
        }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != Resource::class.java) {
            return null
        }

        check(responseType is ParameterizedType) {
            "Response type must be parameterized"
        }

        val successBodyType = getParameterUpperBound(0, responseType)

        return ResourceCallAdapter<Any>(successBodyType)
    }
}