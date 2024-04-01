package com.baris.fakestore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baris.fakestore.ui.theme.MediumGrey
import com.baris.fakestore.ui.theme.RossRed
import com.baris.fakestore.ui.theme.interFamily

/**
 * Created on 28.02.2024.
 * @author saycicek
 */

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeHolder: String = "",
    isError: Boolean = false,
    isValid: Boolean = false,
    leadIcon: @Composable () -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit
) {

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontFamily = interFamily,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        ),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation
    ) { innerTextField ->
        Row(
            modifier = Modifier
                .border(1.dp, if (isError) RossRed else if (isValid) Color.Green else MediumGrey)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadIcon().also {
                Spacer(modifier = Modifier.width(6.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeHolder,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = interFamily,
                            fontWeight = FontWeight.Normal,
                            color = MediumGrey,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                innerTextField()
            }
        }
    }
}

@Preview
@Composable
private fun AppTextFieldPreview() {
    Box(
        Modifier.background(Color.White)
    ) {
        var value by remember {
            mutableStateOf("")
        }
        AppTextField(
            placeHolder = "Arama Yap",
            value = value,
            onValueChange = {
                value = it
            }
        )
    }
}