package com.baris.fakestore.ui.feature.productDetail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.baris.fakestore.common.Constants
import com.baris.fakestore.common.Utils
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.ui.components.LoadingDialog
import com.baris.fakestore.ui.components.ScaffoldWithTopBar
import com.baris.fakestore.ui.components.WarningDialog
import com.baris.fakestore.ui.theme.CloudGray
import com.baris.fakestore.ui.theme.MediumGrey
import com.baris.fakestore.ui.theme.RossRed
import com.baris.fakestore.ui.theme.interFamily
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

/**
 * Created on 27.02.2024.
 * @author saycicek
 */

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    productId: Int?,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.event.collectAsStateWithLifecycle(initialValue = ProductDetailViewModel.UiEvent.Init)

    LaunchedEffect(key1 = Unit, block = {
        productId?.let {
            viewModel.getProductById(productId)
        }
    })

    when (event) {
        ProductDetailViewModel.UiEvent.Init -> {}
        is ProductDetailViewModel.UiEvent.ShowToast -> {
            val context = LocalContext.current
            Toast.makeText(context, (event as ProductDetailViewModel.UiEvent.ShowToast).message, Toast.LENGTH_SHORT).show()
        }
    }

    if (state.isLoading)
        LoadingDialog()

    state.error?.let {
        WarningDialog(title = Constants.DEFAULT_ERROR_TITLE, text = it.message)
    }

    state.data?.let {
        ScreenContent(
            product = it,
            addToCart = viewModel::addToCart,
            onBack = onBack,
            onFavorite = viewModel::onFavoriteClick
        )
    }
}

@Composable
private fun ScreenContent(
    product: Product,
    onBack: () -> Unit,
    onFavorite: () -> Unit,
    addToCart: () -> Unit
) {

    ScaffoldWithTopBar(
        title = product.title,
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onFavorite) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Add Favorite",
                    tint = if (product.isFavorite) RossRed else MediumGrey
                )
            }
        }
    ) {
        Column {
            ImageSlider(
                images = product.images,
                discount = product.discountPercentage
            )

            TitleAndPrice(
                title = product.title,
                price = product.newPrice,
                oldPrice = product.oldPrice
            )

            Description(description = product.description)

            Spacer(modifier = Modifier.weight(1f))

            CartButton(addToCart)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ImageSlider(
    images: List<String>,
    discount: Double?
) {
    val pagerState = rememberPagerState(initialPage = 0)
    Column {
        Box {
            HorizontalPager(
                state = pagerState,
                count = images.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                AsyncImage(
                    model = images[it],
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
            }
            discount?.let {
                Box(
                    Modifier
                        .padding(12.dp)
                        .wrapContentSize()
                        .background(
                            color = RossRed,
                            shape = CircleShape
                        )
                        .padding(6.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "%$it",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontFamily = interFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }
}

@Composable
private fun TitleAndPrice(
    title: String,
    price: Double,
    oldPrice: Double
) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.Black,
                fontFamily = interFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(12.dp))

        if (oldPrice != price) {
            Text(
                text = "${Utils.formatPrice(oldPrice)} TL",
                style = TextStyle(
                    color = MediumGrey,
                    fontSize = 12.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.LineThrough
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        Text(
            text = "${Utils.formatPrice(price)} TL",
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun Description(
    description: String
) {
    Text(
        text = description,
        style = TextStyle(
            color = MediumGrey,
            fontSize = 14.sp,
            fontFamily = interFamily,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
    )
}

@Composable
private fun CartButton(
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
            text = "ADD TO CART",
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.Bold
            )
        )
    }
}