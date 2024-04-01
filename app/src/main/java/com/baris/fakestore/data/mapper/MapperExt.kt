package com.baris.fakestore.data.mapper

import com.baris.fakestore.data.model.ProductDto
import com.baris.fakestore.data.model.ProductsResponseDto
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.model.ProductsResponse

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

fun ProductDto.toDomain(basketQuantity: Int = 0, isFavorite: Boolean = false): Product {

    val newPrice = discountPercentage?.let { (1.0 - (it / 100.0)) * price } ?: price

    return Product(
        id = this.id,
        brand = this.brand,
        category = this.category,
        description = this.description,
        discountPercentage = this.discountPercentage,
        images = this.images,
        oldPrice = price,
        rating = this.rating,
        stock = this.stock,
        thumbnail = this.thumbnail,
        title = this.title,
        basketQuantity = basketQuantity,
        isFavorite = isFavorite,
        newPrice = newPrice
    )
}

fun ProductsResponseDto.toDomain() : ProductsResponse {
    return ProductsResponse(
        products = this.products.map { it.toDomain() },
        total = this.total,
        limit = this.limit,
        skip = this.skip
    )
}