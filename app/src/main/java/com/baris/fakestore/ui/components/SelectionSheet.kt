package com.baris.fakestore.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baris.fakestore.common.SelectionSheetItem
import java.util.UUID

/**
 * Created on 5.02.2024.
 * @author saycicek
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionSheet(
    list: List<SelectionSheetItem>,
    onDismiss: (SelectionSheetItem?) -> Unit,
    sheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss.invoke(list.find { it.isSelected })
        },
        sheetState = sheetState
    ) {
        val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = bottomPadding.plus(8.dp)
                )
        ) {
            LazyColumn {
                itemsIndexed(list) { index, item ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {
                                onDismiss.invoke(item)
                            }
                    ) {
                        Text(
                            text = item.text,
                            style = TextStyle(
                                fontSize = 17.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight(400),
                                color = Color(0xFF000000),
                            )
                        )

                        if (item.isSelected) {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }
                    }

                    if (index != list.size - 1) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SelectionSheetPreview() {
    Box {
        val list = listOf(
            SelectionSheetItemModel(
                text = "Sample"
            ),
            SelectionSheetItemModel(
                text = "Sample"
            )
        )
        val sheetState = rememberModalBottomSheetState()
        SelectionSheet(
            list = list,
            onDismiss = { },
            sheetState = sheetState
        )
    }
}

internal data class SelectionSheetItemModel(
    override val id: String = UUID.randomUUID().toString(),
    override val text: String,
    override val isSelected: Boolean = false
) : SelectionSheetItem