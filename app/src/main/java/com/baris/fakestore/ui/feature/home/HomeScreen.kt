package com.baris.fakestore.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.baris.fakestore.R
import com.baris.fakestore.common.Constants
import com.baris.fakestore.common.Params
import com.baris.fakestore.domain.model.Product
import com.baris.fakestore.domain.model.SortModel
import com.baris.fakestore.ui.components.AppTextField
import com.baris.fakestore.ui.components.LoadingDialog
import com.baris.fakestore.ui.components.SelectionSheet
import com.baris.fakestore.ui.components.WarningDialog
import com.baris.fakestore.ui.theme.MediumGrey
import com.baris.fakestore.ui.theme.interFamily

/**
 * Created on 26.02.2024.
 * @author saycicek
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    backStackEntry: NavBackStackEntry,
    navigateProductDetail: (Int) -> Unit,
    navigateFilterScreen: (FilterResultModel) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val sortSheetState = rememberModalBottomSheetState()
    var showSortSheet by remember {
        mutableStateOf(false)
    }

    backStackEntry.savedStateHandle.getLiveData<FilterResultModel>(Params.FILTER_RESULT)
        .observeAsState().value?.let { result ->
        LaunchedEffect(key1 = 1, block = {
            viewModel.filterResult = result
            viewModel.updateProductsByFilter()
        })
    }

    LaunchedEffect(
        key1 = Unit,
        block = {
            if (state.data.products.isEmpty()) {
                viewModel.getProducts()
            }
        }
    )

    if (state.isLoading) {
        LoadingDialog()
    }

    state.error?.let {
        WarningDialog(title = Constants.DEFAULT_ERROR_TITLE, text = it.message)
    }

    if (showSortSheet) {
        SelectionSheet(
            list = viewModel.sortModels,
            onDismiss = { selectedSort ->
                showSortSheet = false
                (selectedSort as? SortModel)?.let {
                    viewModel.sortBy(it.sortType)
                }
            },
            sheetState = sortSheetState
        )
    }

    ScreenContent(
        productList = state.data.products,
        totalProduct = state.data.total,
        search = viewModel::search,
        query = viewModel.lastSearchQuery,
        isLastPage = viewModel::isLastPage,
        nextPage = viewModel::nextPage,
        navigateProductDetail = navigateProductDetail,
        navigateFilterScreen = {
            navigateFilterScreen(viewModel.filterResult)
        },
        onSortClick = {
            showSortSheet = true
        }
    )

}

@Composable
private fun ScreenContent(
    productList: List<Product>,
    query: String,
    totalProduct: Int?,
    search: (String) -> Unit,
    onSortClick: () -> Unit,
    isLastPage: () -> Boolean,
    nextPage: () -> Unit,
    navigateProductDetail: (Int) -> Unit,
    navigateFilterScreen: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        SearchAndFilter(
            totalProduct = totalProduct,
            search = search,
            onSortClick = onSortClick,
            query = query,
            navigateFilterScreen = navigateFilterScreen
        )

        Spacer(modifier = Modifier.height(20.dp))

        ProductList(
            productList = productList,
            isLastPage = isLastPage,
            nextPage = nextPage,
            navigateProductDetail = navigateProductDetail
        )
    }
}

@Composable
fun SearchAndFilter(
    totalProduct: Int?,
    query: String,
    search: (String) -> Unit,
    onSortClick: () -> Unit,
    navigateFilterScreen: () -> Unit
) {

    var searchValue by remember {
        mutableStateOf(query)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Ürünler",
            style = TextStyle(
                color = Color.Black,
                fontFamily = interFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        totalProduct?.let {
            Text(
                text = "(Toplam $it adet)",
                style = TextStyle(
                    color = MediumGrey,
                    fontFamily = interFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }

    Spacer(modifier = Modifier.height(6.dp))

    Row(
        Modifier.fillMaxWidth()
    ) {
        AppTextField(
            modifier = Modifier.weight(1f),
            value = searchValue,
            placeHolder = "Search Product",
            leadIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            )
        ) {
            searchValue = it
            search(it)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            Modifier
                .size(40.dp)
                .background(color = Color.Transparent)
                .border(1.dp, MediumGrey)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = navigateFilterScreen
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter),
                contentDescription = null,
                tint = MediumGrey
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Box(
            Modifier
                .size(40.dp)
                .background(color = Color.Transparent)
                .border(1.dp, MediumGrey)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onSortClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_sort),
                contentDescription = null,
                tint = MediumGrey
            )
        }

    }
}

@Composable
private fun ProductList(
    productList: List<Product>,
    isLastPage: () -> Boolean,
    nextPage: () -> Unit,
    navigateProductDetail: (Int) -> Unit
) {
    val lazyColumnListState = rememberLazyListState()
    val shouldStartPaginate = remember {
        derivedStateOf {
            (lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (lazyColumnListState.layoutInfo.totalItemsCount - 6)
        }
    }

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value && !isLastPage())
            nextPage()
    }

    LazyColumn(state = lazyColumnListState) {
        items(productList) { item ->
            HomeProductItem(
                title = item.title,
                imageUrl = item.thumbnail,
                description = item.description,
                price = item.newPrice,
                oldPrice = item.oldPrice
            ) {
                navigateProductDetail(item.id)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

}