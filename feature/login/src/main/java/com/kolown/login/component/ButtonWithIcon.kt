package com.kolown.login.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kolown.designsystem.Primary
import com.kolown.designsystem.Surface

@Composable
fun ButtonWithIcon(
    icon: Int,
    text: String,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(width = 252.dp),
        shape = RoundedCornerShape(2.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White, contentColor = Color.Gray
        )
    ) {
        Icon(painter = painterResource(icon), contentDescription = null, tint = Color.Unspecified)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp)
    }
}

@Composable
fun ButtonWithIcon(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = Modifier.width(width = 252.dp),
        shape = RoundedCornerShape(2.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Surface, contentColor = Color.Gray
        )
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp)
    }
}