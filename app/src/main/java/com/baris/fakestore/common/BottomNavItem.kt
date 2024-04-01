package com.baris.fakestore.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {

    data object Home : BottomNavItem(Route.HOME, Icons.Default.Home, "Home")

    data object Favorite : BottomNavItem(Route.FAVORITE, Icons.Default.Favorite, "Favorite")

    data object Basket : BottomNavItem(Route.BASKET, Icons.Default.ShoppingCart, "Basket")

}