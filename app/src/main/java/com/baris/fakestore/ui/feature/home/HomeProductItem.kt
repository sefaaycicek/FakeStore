package com.baris.fakestore.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baris.fakestore.common.Utils
import com.baris.fakestore.ui.theme.CloudGray
import com.baris.fakestore.ui.theme.MediumGrey
import com.baris.fakestore.ui.theme.Platinum
import com.baris.fakestore.ui.theme.interFamily

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@Composable
fun HomeProductItem(
    title: String,
    imageUrl: String,
    description: String,
    price: Double,
    oldPrice: Double,
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
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.size(70.dp),
            contentScale = ContentScale.Fit
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
                )
            )
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
        }
        Spacer(modifier = Modifier.width(32.dp))
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${Utils.formatPrice(price)} TL",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold
                )
            )
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
            }
        }
    }
}

@Preview
@Composable
fun HomeProductItemPreview() {
    Box(
        Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        val oldPrice = (100 - 12.96) * 549
        HomeProductItem(
            title = "iPhone 9",
            imageUrl = "https://cdn.dummyjson.com/product-images/1/thumbnail.jpg",
            description = "An apple mobile which is nothing like apple",
            price = 549.0,
            oldPrice = oldPrice
        ) {}
    }
}