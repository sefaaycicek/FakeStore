package com.baris.fakestore.ui.feature.favorites

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baris.fakestore.common.Constants
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.ui.components.LoadingDialog
import com.baris.fakestore.ui.components.ScaffoldWithTopBar
import com.baris.fakestore.ui.components.WarningDialog

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    onNavigateProductDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getFavorites()
    })

    if (state.isLoading)
        LoadingDialog()

    state.error?.let {
        WarningDialog(title = Constants.DEFAULT_ERROR_TITLE, text = it.message)
    }

    ScreenContent(
        products = state.data,
        onProductClick = onNavigateProductDetail,
        onIncreaseBasketQuantity = viewModel::onIncreaseBasketQuantity,
        onDecreaseBasketQuantity = viewModel::onDecreaseBasketQuantity,
        onFavoriteClick = viewModel::removeFavorite
    )
}

@Composable
private fun ScreenContent(
    products: List<Product>,
    onProductClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onIncreaseBasketQuantity: (Int, Int) -> Unit,
    onDecreaseBasketQuantity: (Int, Int) -> Unit
) {
    ScaffoldWithTopBar(title = "FAVORITE") {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            itemsIndexed(products) { index, product ->
                FavoriteProductItem(
                    modifier = Modifier.padding(
                        start = if (index % 2 == 1) 15.dp else 0.dp,
                        end = if (index % 2 == 0) 15.dp else 0.dp,
                        bottom = 20.dp
                    ),
                    image = product.thumbnail,
                    title = product.title,
                    description = product.description,
                    oldPrice = product.oldPrice,
                    newPrice = product.newPrice,
                    basketQuantity = product.basketQuantity,
                    onIncreaseQuantity = {
                        onIncreaseBasketQuantity(
                            product.id,
                            product.basketQuantity
                        )
                    },
                    onDecreaseQuantity = {
                        onDecreaseBasketQuantity(
                            product.id,
                            product.basketQuantity
                        )
                    },
                    onClick = { onProductClick(product.id) },
                    onFavoriteClick = { onFavoriteClick(product.id) }
                )
            }
        }
    }
}