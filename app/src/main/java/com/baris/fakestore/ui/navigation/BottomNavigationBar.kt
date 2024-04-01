package com.baris.fakestore.ui.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.baris.fakestore.common.BottomNavItem
import com.baris.fakestore.ui.theme.CloudGray
import com.baris.fakestore.ui.theme.Platinum

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Composable
fun BottomNavigationBar(
    navController: NavController
) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorite,
        BottomNavItem.Basket
    )

    BottomNavigation(
        backgroundColor = Platinum,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            BottomNavigationItem(
                selected = currentDestination?.route == item.route,
                selectedContentColor = Color.Black,
                unselectedContentColor = CloudGray,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(item.label)
                }
            )
        }

    }
}