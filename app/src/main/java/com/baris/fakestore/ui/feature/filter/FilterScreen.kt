package com.baris.fakestore.ui.feature.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.navigation.NavBackStackEntry
import com.baris.fakestore.common.Constants
import com.baris.fakestore.common.Params
import com.baris.fakestore.domain.model.CategoryUiModel
import com.baris.fakestore.ui.components.AppTextField
import com.baris.fakestore.ui.components.LoadingDialog
import com.baris.fakestore.ui.components.ScaffoldWithTopBar
import com.baris.fakestore.ui.components.WarningDialog
import com.baris.fakestore.ui.feature.home.FilterResultModel
import com.baris.fakestore.ui.theme.CloudGray
import com.baris.fakestore.ui.theme.interFamily

/**
 * Created on 29.02.2024.
 * @author saycicek
 */

@Composable
fun FilterScreen(
    viewModel: FilterViewModel = hiltViewModel(),
    backStackEntry: NavBackStackEntry,
    onBack: () -> Unit,
    onFilterApplied: (filterResult: FilterResultModel) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var minPriceValue by remember {
        mutableStateOf("")
    }

    var maxPriceValue by remember {
        mutableStateOf( "")
    }

    LaunchedEffect(key1 = 1, block = {
        viewModel.getCategories()
    })

    backStackEntry.savedStateHandle.getLiveData<FilterResultModel>(Params.FILTER_RESULT).observeAsState().value?.let {
        LaunchedEffect(key1 = 2, block = {
            minPriceValue = it.minPrice?.toString() ?: ""
            maxPriceValue = it.maxPrice?.toString() ?: ""
            viewModel.selectedCategoriesResult = it.categories
        })
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    state.error?.let {
        WarningDialog(title = Constants.DEFAULT_ERROR_TITLE, text = it.message)
    }

    ScreenContent(
        categories = state.data,
        minPrice = minPriceValue,
        minPriceChanged = {
            val dotCount = it.count { c ->
                c == '.'
            }
            val isStartWithDot = it.getOrNull(0) == '.'
            if (dotCount <= 1 && !isStartWithDot)
                minPriceValue = it.replace(",", "").replace("-", "").trim()
        },
        maxPrice = maxPriceValue,
        maxPriceChanged = {
            val dotCount = it.count { c ->
                c == '.'
            }
            val isStartWithDot = it.getOrNull(0) == '.'
            if (dotCount <= 1 && !isStartWithDot)
                maxPriceValue = it.replace(",", "").replace("-", "").trim()
        },
        onBack = onBack,
        onSelectCategory = viewModel::selectCategory,
        onFilterApplied = {
            val resultMinPrice = if(minPriceValue.isNotEmpty()) minPriceValue.toDouble() else null
            val resultMaxPrice = if(maxPriceValue.isNotEmpty()) maxPriceValue.toDouble() else null
            val selectedCategories = state.data.filter { it.isSelected }.map { it.category }

            onFilterApplied(
                FilterResultModel(
                    minPrice = resultMinPrice,
                    maxPrice = resultMaxPrice,
                    categories = selectedCategories
                )
            )
        }
    )

}

@Composable
fun ScreenContent(
    categories: List<CategoryUiModel>,
    minPrice: String,
    minPriceChanged: (String) -> Unit,
    maxPrice: String,
    maxPriceChanged: (String) -> Unit,
    onBack: () -> Unit,
    onSelectCategory: (category: String, Boolean) -> Unit,
    onFilterApplied: () -> Unit
) {

    ScaffoldWithTopBar(
        title = "FILTER",
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    ) {
        Column(
            Modifier.padding(horizontal = 20.dp)
        ) {
            Row {
                AppTextField(
                    modifier = Modifier.weight(1f),
                    value = minPrice,
                    placeHolder = "MIN PRICE",
                    onValueChange = minPriceChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                AppTextField(
                    modifier = Modifier.weight(1f),
                    value = maxPrice,
                    placeHolder = "MAX PRICE",
                    onValueChange = maxPriceChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Categories",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 12.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(categories) { item ->
                    CategoryItem(
                        title = item.category,
                        isSelected = item.isSelected,
                        onSelected = {
                            onSelectCategory(item.category, it)
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            Button(
                onClick = onFilterApplied,
                colors = ButtonDefaults.buttonColors(containerColor = CloudGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Text(
                    text = "APPLY",
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