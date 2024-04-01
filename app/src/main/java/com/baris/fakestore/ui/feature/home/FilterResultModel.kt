package com.baris.fakestore.ui.feature.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created on 29.02.2024.
 * @author saycicek
 */

@Parcelize
data class FilterResultModel(
    val minPrice: Double?,
    val maxPrice: Double?,
    val categories: List<String>
): Parcelable
