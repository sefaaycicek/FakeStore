package com.baris.fakestore.ui.feature.basket

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baris.fakestore.common.Utils
import com.baris.fakestore.ui.theme.CarbonGrey
import com.baris.fakestore.ui.theme.MediumGrey
import com.baris.fakestore.ui.theme.Platinum
import com.baris.fakestore.ui.theme.RossRed
import com.baris.fakestore.ui.theme.interFamily

/**
 * Created on 28.02.2024.
 * @author saycicek
 */

@Composable
fun BasketItem(
    title: String,
    image: String,
    newPrice: Double,
    oldPrice: Double,
    basketQuantity: Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onDeleteProduct: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxSize()
            .background(
                color = Platinum,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier.size(70.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row {
                Text(
                    text = "${Utils.formatPrice(newPrice)} TL",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontFamily = interFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
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
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        BasketOperation(
            quantity = basketQuantity,
            onIncreaseQuantity = onIncreaseQuantity,
            onDecreaseQuantity = onDecreaseQuantity,
            onDeleteProduct = onDeleteProduct
        )
    }
}

@Composable
private fun BasketOperation(
    modifier: Modifier = Modifier,
    quantity: Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onDeleteProduct: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    MediumGrey.copy(
                        alpha = if (quantity == 1)
                            0.5f
                        else
                            1f
                    )
                )
                .size(20.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onDecreaseQuantity,
                    enabled = quantity > 1
                ),
            contentAlignment = Alignment.Center
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
            modifier = Modifier.size(20.dp),
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
        Box(
            modifier = Modifier
                .background(MediumGrey)
                .size(20.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onIncreaseQuantity
                ),
            contentAlignment = Alignment.Center
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
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(20.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onDeleteProduct
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = RossRed)
        }
    }
}