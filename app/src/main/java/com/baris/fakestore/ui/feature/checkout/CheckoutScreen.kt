package com.baris.fakestore.ui.feature.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baris.fakestore.common.Constants
import com.baris.fakestore.common.Utils
import com.baris.fakestore.ui.components.AppTextField
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
fun CheckoutScreen(
    viewModel: CheckoutViewModel = hiltViewModel(),
    onBack: () -> Unit,
    finish: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.event.collectAsStateWithLifecycle(initialValue = CheckoutViewModel.UiEvent.Init)

    if (state.isLoading)
        LoadingDialog()

    state.error?.let {
        WarningDialog(title = Constants.DEFAULT_ERROR_TITLE, text = it.message)
    }

    when (event) {
        CheckoutViewModel.UiEvent.Done -> finish()
        CheckoutViewModel.UiEvent.Init -> {}
    }

    ScreenContent(
        validateName = viewModel::validateName,
        validateEmail = viewModel::validateEmail,
        validateCreditCard = viewModel::validateCreditCard,
        onCompletePayment = viewModel::completePayment,
        onBack = onBack
    )
}

@Composable
private fun ScreenContent(
    validateName: (String) -> Boolean,
    validateEmail: (String) -> Boolean,
    validateCreditCard: (String) -> Boolean,
    onCompletePayment: () -> Unit,
    onBack: () -> Unit
) {

    var nameValue by remember {
        mutableStateOf("")
    }
    var isNameValid by remember {
        mutableStateOf(false)
    }

    var emailValue by remember {
        mutableStateOf("")
    }
    var isEmailValid by remember {
        mutableStateOf(false)
    }

    var creditCardValue by remember {
        mutableStateOf("")
    }
    var isCreditCardValid by remember {
        mutableStateOf(false)
    }

    ScaffoldWithTopBar(
        title = "CHECKOUT",
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    ) {
        Column {
            AppTextField(
                modifier = Modifier.padding(horizontal = 20.dp),
                value = nameValue,
                placeHolder = "NAME",
                onValueChange = {
                    nameValue = it
                    isNameValid = validateName(it)
                },
                isError = nameValue.isNotEmpty() && !isNameValid,
                isValid = isNameValid
            )

            Spacer(modifier = Modifier.height(20.dp))

            AppTextField(
                modifier = Modifier.padding(horizontal = 20.dp),
                value = emailValue,
                placeHolder = "EMAIL",
                onValueChange = {
                    emailValue = it
                    isEmailValid = validateEmail(emailValue)
                },
                isError = emailValue.isNotEmpty() && !isEmailValid,
                isValid = isEmailValid,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            AppTextField(
                modifier = Modifier.padding(horizontal = 20.dp),
                value = creditCardValue,
                placeHolder = "CREDIT CARD",
                onValueChange = {
                    creditCardValue = it
                    isCreditCardValid = validateCreditCard(creditCardValue)
                },
                isError = creditCardValue.isNotEmpty() && !isCreditCardValid,
                isValid = isCreditCardValid,
                visualTransformation = {
                    Utils.formatCreditCard(creditCardValue)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            val isInputsValid = isCreditCardValid && isEmailValid && isNameValid

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (isInputsValid)
                        onCompletePayment()
                },
                colors = ButtonDefaults.buttonColors(containerColor = CloudGray.copy(alpha = if(isInputsValid) 1f else 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "CHECKOUT",
                    style = TextStyle(
                        color = Color.Black.copy(alpha = if(isInputsValid) 1f else 0.5f),
                        fontSize = 14.sp,
                        fontFamily = interFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}