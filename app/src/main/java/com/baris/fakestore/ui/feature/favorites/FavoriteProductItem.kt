package com.baris.fakestore.ui.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baris.fakestore.common.Utils
import com.baris.fakestore.ui.theme.MediumGrey
import com.baris.fakestore.ui.theme.Platinum
import com.baris.fakestore.ui.theme.RossRed
import com.baris.fakestore.ui.theme.interFamily

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Composable
fun FavoriteProductItem(
    modifier: Modifier = Modifier,
    image: String,
    title: String,
    description: String,
    oldPrice: Double,
    newPrice: Double,
    basketQuantity: Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Platinum,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onClick
            )
    ) {
        Box {
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                contentScale = ContentScale.Fit
            )
            Card(
                shape = RoundedCornerShape(8.dp), modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.TopEnd)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = onFavoriteClick
                    ),
                colors = CardColors(
                    containerColor = Color.White,
                    contentColor = Color.Transparent,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            ) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "", tint = RossRed, modifier = Modifier.fillMaxSize())
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = description,
            style = TextStyle(
                color = MediumGrey,
                fontSize = 10.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.Normal
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(6.dp))
        if (oldPrice != newPrice) {
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
            text = "${Utils.formatPrice(newPrice)} TL",
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        BasketOperation(
            modifier = Modifier.align(Alignment.End),
            quantity = basketQuantity,
            onIncreaseQuantity = onIncreaseQuantity,
            onDecreaseQuantity = onDecreaseQuantity
        )

    }
}

@Composable
private fun BasketOperation(
    modifier: Modifier = Modifier,
    quantity: Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit
) {
    Card(
        modifier = modifier
            .height(30.dp),
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier.background(color = Color.White),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (quantity > 0) {
                IconButton(
                    modifier = Modifier
                        .background(Color.White)
                        .size(30.dp),
                    onClick = onDecreaseQuantity,
                ) {
                    Text(
                        text = "-",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontFamily = interFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Box(
                    Modifier.size(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = quantity.toString(),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontFamily = interFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            IconButton(
                modifier = Modifier
                    .background(Color.White)
                    .size(30.dp),
                onClick = onIncreaseQuantity,
            ) {
                Text(
                    text = "+",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontFamily = interFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}