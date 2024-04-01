package com.baris.fakestore.core.extensions

import com.baris.fakestore.core.IResult
import com.baris.fakestore.core.ResultError

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

inline fun <T> IResult<T>.onLoading(body: () -> Unit) = this.also {
    if (this is IResult.Loading)
        body.invoke()
}

inline fun <T> IResult<T>.onSuccess(body: (T) -> Unit) = this.also {
    if (this is IResult.Success)
        body.invoke(this.data)
}

inline fun <T> IResult<T>.onError(body: (ResultError) -> Unit) = this.also {
    if (this is IResult.Error)
        body.invoke(this.error)
}