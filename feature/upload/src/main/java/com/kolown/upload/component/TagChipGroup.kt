package com.kolown.upload.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.kolown.designsystem.component.PorringIconButton
import com.kolown.designsystem.component.PorringInputChip
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.upload.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CategoryGroup(
    modifier: Modifier = Modifier,
    categoryItems: List<String> = emptyList(),
    addCategory: () -> Unit,
    removeCategory: (String) -> Unit,
    changeCategoryName: (Int, String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val previousSize = remember { mutableIntStateOf(categoryItems.size) }

    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        categoryItems.forEachIndexed { idx, item ->
            PorringInputChip(
                value = item,
                onValueChange = {
                    if (it.length <= 10) {
                        changeCategoryName(idx, it.filter { char -> char.isLetterOrDigit() })
                    }
                },
                hint = stringResource(R.string.string_input_tag),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.size(18.dp),
                        onClick = { removeCategory(item) }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_cancel_circle),
                            contentDescription = stringResource(R.string.string_delete_tag),
                            tint = Primary
                        )
                    }
                },
                modifier = if (idx == categoryItems.lastIndex) Modifier.focusRequester(
                    focusRequester
                ) else Modifier
            )
        }
        if (categoryItems.size < 6) {
            PorringIconButton(
                icon = ImageVector.vectorResource(R.drawable.ic_add_circle),
                onClick = { addCategory() },
                modifier = Modifier.size(32.dp)
            )
        }
    }

    LaunchedEffect(categoryItems.size) {
        if (categoryItems.size > previousSize.intValue) {
            focusRequester.requestFocus()
        }
        previousSize.intValue = categoryItems.size
    }
}