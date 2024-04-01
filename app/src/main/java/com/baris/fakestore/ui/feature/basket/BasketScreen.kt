package com.baris.fakestore.ui.feature.basket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baris.fakestore.common.Constants
import com.baris.fakestore.common.Utils
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.ui.components.LoadingDialog
import com.baris.fakestore.ui.components.ScaffoldWithTopBar
import com.baris.fakestore.ui.components.WarningDialog
import com.baris.fakestore.ui.theme.CloudGray
import com.baris.fakestore.ui.theme.interFamily

/**
 * Created on 28.02.2024.
 * @author saycicek
 */

@Composable
fun BasketScreen(
    viewModel: BasketViewModel = hiltViewModel(),
    onProductClick: (Int) -> Unit,
    onCheckoutClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(
        key1 = Unit,
        block = {
            viewModel.getBasketProducts()
        }
    )

    if (state.isLoading) {
        LoadingDialog()
    }

    state.error?.let {
        WarningDialog(title = Constants.DEFAULT_ERROR_TITLE, text = it.message)
    }

    ScreenContent(
        products = state.data,
        onProductClick = onProductClick,
        onCheckoutClick = onCheckoutClick,
        onDeleteProduct = viewModel::deleteBasketProduct,
        onIncreaseBasketQuantity = viewModel::onIncreaseBasketQuantity,
        onDecreaseBasketQuantity = viewModel::onDecreaseBasketQuantity
    )

}

@Composable
private fun ScreenContent(
    products: List<Product>,
    onProductClick: (Int) -> Unit,
    onCheckoutClick: () -> Unit,
    onDeleteProduct: (Int) -> Unit,
    onIncreaseBasketQuantity: (Int, Int) -> Unit,
    onDecreaseBasketQuantity: (Int, Int) -> Unit
) {
    ScaffoldWithTopBar(title = "BASKET") {
        Column {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp).weight(1f)
            ) {
                items(products) {
                    BasketItem(
                        title = it.title,
                        image = it.thumbnail,
                        newPrice = it.newPrice,
                        oldPrice = it.oldPrice,
                        basketQuantity = it.basketQuantity,
                        onIncreaseQuantity = { onIncreaseBasketQuantity(it.id, it.basketQuantity) },
                        onDecreaseQuantity = { onDecreaseBasketQuantity(it.id, it.basketQuantity) },
                        onDeleteProduct = { onDeleteProduct(it.id) },
                        onClick = { onProductClick(it.id) }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            
            TotalPrices(
                totalPrice = products.sumOf { it.oldPrice * it.basketQuantity },
                discountTotal = products.sumOf { (it.oldPrice - it.newPrice) * it.basketQuantity },
                totalEndPrice = products.sumOf { it.newPrice  * it.basketQuantity }
            )

            if(products.isNotEmpty()) {
                CheckoutButton(onCheckoutClick)
            }
        }
    }
}

@Composable
private fun TotalPrices(
    totalPrice: Double,
    discountTotal: Double,
    totalEndPrice: Double
) {
    Column(
        Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Row {
            Text(
                text = "Price:",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${Utils.formatPrice(totalPrice)} TL",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row {
            Text(
                text = "Discount:",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${Utils.formatPrice(discountTotal)} TL",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row {
            Text(
                text = "Total:",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${Utils.formatPrice(totalEndPrice)} TL",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
private fun CheckoutButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = CloudGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "CHECKOUT",
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.Bold
            )
        )
    }
}