package com.baris.fakestore.data.model

data class ProductsResponseDto(
    val limit: Int,
    val products: List<ProductDto>,
    val skip: Int,
    val total: Int
)