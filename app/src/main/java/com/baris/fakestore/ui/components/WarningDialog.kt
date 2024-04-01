package com.baris.fakestore.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/**
 * Created on 31.12.2023.
 * @author saycicek
 */

@Composable
fun WarningDialog(
    title: String,
    text: String
) {
    var dismissState by remember {
        mutableStateOf(true)
    }
    if (dismissState) {
        AlertDialog(
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    tint = Color.Red,
                    contentDescription = "Warning"
                )
            },
            onDismissRequest = {
                dismissState = false
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = text)
            },
            confirmButton = {
                Button(onClick = {
                    dismissState = false
                }) {
                    Text(text = "Tamam")
                }
            }
        )
    }
}