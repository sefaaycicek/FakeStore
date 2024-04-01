package com.baris.fakestore.core

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
sealed interface IResult<out T> {
    data object Loading : IResult<Nothing>
    data class Success<T>(val data: T) : IResult<T>
    data class Error(val error: ResultError) : IResult<Nothing>
}