package com.baris.fakestore.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.baris.fakestore.common.Params
import com.baris.fakestore.common.Route
import com.baris.fakestore.ui.Greeting
import com.baris.fakestore.ui.feature.basket.BasketScreen
import com.baris.fakestore.ui.feature.checkout.CheckoutScreen
import com.baris.fakestore.ui.feature.favorites.FavoriteScreen
import com.baris.fakestore.ui.feature.filter.FilterScreen
import com.baris.fakestore.ui.feature.home.HomeScreen
import com.baris.fakestore.ui.feature.productDetail.ProductDetailScreen
import com.baris.fakestore.ui.theme.FakeStoreTheme

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.HOME) {
                HomeScreen(
                    backStackEntry = it,
                    navigateProductDetail = {
                        //productdetail/100
                        navController.navigate(Route.PRODUCT_DETAIL + "/$it")
                    },
                    navigateFilterScreen = {
                        navController.navigate(Route.FILTER)
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(Params.FILTER_RESULT, it)
                    }
                )
            }
            composable(
                route = Route.PRODUCT_DETAIL + "/{${Params.PRODUCT_ID}}",
                arguments = listOf(
                    navArgument(Params.PRODUCT_ID) { type = NavType.IntType }
                )
            ) {
                ProductDetailScreen(
                    productId = it.arguments?.getInt(Params.PRODUCT_ID)
                ) {
                    navController.popBackStack()
                }
            }
            composable(Route.FAVORITE) {
                FavoriteScreen(
                    onNavigateProductDetail = {
                        navController.navigate(Route.PRODUCT_DETAIL + "/$it")
                    }
                )
            }
            composable(Route.BASKET) {
                BasketScreen(
                    onProductClick = {
                        navController.navigate(Route.PRODUCT_DETAIL + "/$it")
                    },
                    onCheckoutClick = {
                        navController.navigate(Route.CHECKOUT)
                    }
                )
            }
            composable(Route.CHECKOUT) {
                CheckoutScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    finish = {
                        navController.popBackStack(Route.HOME, false)
                    }
                )
            }
            composable(Route.FILTER) {
                FilterScreen(
                    backStackEntry = it,
                    onBack = {
                        navController.popBackStack()
                    },
                    onFilterApplied = {
                        navController.popBackStack()
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set(Params.FILTER_RESULT, it)
                    }
                )
            }
        }
    }
}
