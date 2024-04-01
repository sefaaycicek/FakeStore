package com.baris.fakestore.domain.model

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
data class ProductsResponse(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)