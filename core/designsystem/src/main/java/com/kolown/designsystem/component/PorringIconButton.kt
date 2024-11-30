package com.kolown.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kolown.designsystem.ui.theme.Primary

@Composable
fun PorringIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    color: Color = Primary,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier
            .size(48.dp)
            .background(Color.Transparent),
        onClick = onClick,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier
                .size(24.dp)
                .background(Color.Transparent),
        )
    }
}

@Composable
fun PorringIconButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier
            .size(48.dp)
            .background(Color.Transparent),
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier
                .size(24.dp)
                .background(Color.Transparent),
        )
    }
}