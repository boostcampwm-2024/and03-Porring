package com.kolown.login.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kolown.login.R

@Composable
internal fun LogoItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier.fillMaxWidth(0.6f),
            painter = painterResource(R.drawable.logo_porring),
            contentDescription = null,
            tint = Color.Unspecified
        )

        Text(text = stringResource(R.string.app_sub_title), fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLogoItem() {
    LogoItem()
}