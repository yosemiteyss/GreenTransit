//  COMP4521    HON KIN TAT     20514332        kthon@connect.ust.hk
//  COMP4521    LAI CHEUK HEI   20464044        chlaiak@connect.ust.hk
//  COMP4521    CHAN HOK HIM    20435392        hhchanal@connect.ust.hk


package com.yosemiteyss.greentransit.domain.states

import androidx.annotation.FloatRange

sealed class Resource<out T> {

    data class Success<out T>(val data: T) : Resource<T>()

    data class Error(val message: String?) : Resource<Nothing>()

    data class Loading(
        @FloatRange(from = 0.0, to = 100.0) val progress: Double? = null
    ) : Resource<Nothing>()
}

fun<T> Resource<T>.getSuccessDataOr(fallback: T): T {
    return (this as? Resource.Success<T>)?.data ?: fallback
}
