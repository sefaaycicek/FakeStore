package com.baris.fakestore.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
abstract class UseCase {

    protected fun <T> execute(
        block: suspend () -> T
    ): Flow<IResult<T>> = flow {
        emit(IResult.Loading)
        val result = block()
        emit(IResult.Success(result))
    }.catch {
        emit(IResult.Error(ResultError(it.localizedMessage ?: "")))
    }

}