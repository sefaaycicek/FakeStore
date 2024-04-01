package com.baris.fakestore.domain.model

import com.baris.fakestore.common.SortType
import com.baris.fakestore.common.SelectionSheetItem
import java.util.UUID

/**
 * Created on 28.02.2024.
 * @author saycicek
 */
data class SortModel(
    override val id: String = UUID.randomUUID().toString(),
    override val text: String,
    override val isSelected: Boolean = false,
    val sortType: SortType
): SelectionSheetItem
