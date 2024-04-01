package com.baris.fakestore.data.source.api

import com.baris.fakestore.data.model.ProductDto
import com.baris.fakestore.data.model.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

interface FakeStoreApi {

    @GET("/products")
    suspend fun getAllProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductsResponseDto

    @GET("/product/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): ProductDto

    @GET("/products/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductsResponseDto

    @GET("/products/categories")
    suspend fun getCategories(): List<String>

    @GET("/products/category/{category}")
    suspend fun getProductsOfCategory(
        @Path("category") category: String
    ): ProductsResponseDto

}