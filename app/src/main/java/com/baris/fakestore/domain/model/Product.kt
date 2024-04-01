package com.baris.fakestore.domain.model

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
data class Product(
    val brand: String,
    val category: String,
    val description: String,
    val discountPercentage: Double?,
    val id: Int,
    val images: List<String>,
    val oldPrice: Double,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val title: String,
    val basketQuantity: Int = 0,
    val isFavorite: Boolean = false,
    val newPrice: Double
)
