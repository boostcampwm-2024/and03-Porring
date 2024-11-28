package com.kolown.their.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kolown.their.R

@Composable
internal fun TheirAppBar(
    followerName: String = "",
    popBackStack: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White)
    ) {
        IconButton(
            onClick = { popBackStack() }, modifier = Modifier.size(48.dp).align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.string_back_button),
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            "${followerName}'s Gallery",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview
private fun TheirAppBarPreview() {
    TheirAppBar()
}
