package com.kolown.search.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kolown.model.Tag
import com.kolown.search.R

@Composable
internal fun TagItem(tag: Tag, onClick: (Tag) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .clickable { onClick(tag) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.string_tag_name, tag.name))
    }
}
