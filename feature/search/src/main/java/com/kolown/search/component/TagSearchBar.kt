package com.kolown.search.component

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kolown.designsystem.Primary
import com.kolown.designsystem.Surface2
import com.kolown.search.R

@Composable
fun TagSearchBar(
    text: String,
    modifier: Modifier = Modifier.zIndex(1f),
    onValueChange: (String) -> Unit,
    focusState: Boolean,
    onFocusChange: (Boolean) -> Unit = {},
    onClearClick: () -> Unit = {},
    onBackButtonClicked: () -> Unit = {}
) {

    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            modifier = modifier.onFocusChanged {
                onFocusChange(it.isFocused)
            },
            value = text,
            singleLine = true,
            onValueChange = onValueChange,
            shape = androidx.compose.foundation.shape.CircleShape,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Surface2,
                disabledContainerColor = Surface2,
                focusedContainerColor = Surface2,
            ),
            textStyle = TextStyle(
                color = Primary,
                fontWeight = FontWeight.SemiBold
            ),
            leadingIcon = {
                IconButton(onClick = onBackButtonClicked) {
                    if (focusState) Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.icon_arrow_back),
                        contentDescription = "search_back", tint = Primary
                    )
                    else Icon(
                        imageVector = Icons.Default.Search,
                        tint = Primary,
                        contentDescription = "search"
                    )
                }
            },
            trailingIcon = {
                if(focusState){
                    IconButton(onClick = onClearClick){
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "search_back", tint = Primary
                        )
                    }
                }
            }
        )
    }

}

@Composable
@Preview
fun PreviewSearchBar() {
//    TagSearchBar("무니") {
//
//    }
}
