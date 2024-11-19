package com.kolown.upload.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun CategoryGroup(
    modifier: Modifier = Modifier,
    categoryItems: List<String> = emptyList()
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categoryItems.forEach { item ->
            CategoryChip(item = item) { }
        }
        AddChipButton {
            if (categoryItems.size < 6) {
                // todo Chip이 6개보다 적은 경우에만 Chip 추가
            }
        }
    }
}

@Composable
private fun CategoryChip(
    item: String,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.Transparent)
            .padding(start = 12.dp, end = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = item,
                onValueChange = {},
                textStyle = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.width(IntrinsicSize.Min)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Remove item",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AddChipButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.height(32.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Center),
            onClick = onClick
        ) {
            Card(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Icon(
                    modifier = Modifier.padding(3.dp),
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "trailing icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}