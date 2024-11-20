package com.kolown.search.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TagSearchBar(text: String, modifier: Modifier = Modifier,onValueChange: (String) -> Unit) {
    TextField(
        modifier = modifier,
        value = text,
        onValueChange = onValueChange,
        shape = androidx.compose.foundation.shape.CircleShape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Color.Gray,
                contentDescription = null
            )
        }
    )
}

@Composable
@Preview
fun PreviewSearchBar() {
    TagSearchBar("무니"){

    }
}
