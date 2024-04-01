package com.baris.fakestore.ui.feature.filter

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baris.fakestore.ui.theme.interFamily

/**
 * Created on 29.02.2024.
 * @author saycicek
 */

@Composable
fun CategoryItem(
    title: String,
    isSelected: Boolean,
    onSelected: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = isSelected, onCheckedChange = onSelected)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.Medium
            )
        )
    }
}