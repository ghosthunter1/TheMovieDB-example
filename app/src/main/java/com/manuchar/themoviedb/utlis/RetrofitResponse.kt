package com.manuchar.themoviedb.utlis

sealed interface ApiResult<T> {

    fun onSuccess(consumer: (T) -> Unit) {

    }

    fun onError(consumer: (Throwable) -> Unit) {

    }

    fun onLoading(consumer: () -> Unit) {

    }

    class ApiSuccess<T>(val data: T) : ApiResult<T> {
        override fun onSuccess(consumer: (T) -> Unit) {
            consumer.invoke(data)
        }
    }

    class ApiLoading<T> : ApiResult<T> {
        override fun onLoading(consumer: () -> Unit) {
            consumer.invoke()
        }
    }

    class ApiException<T>(private val e: Throwable) : ApiResult<T> {
        override fun onError(consumer: (Throwable) -> Unit) {
            consumer.invoke(e)
        }
    }
}