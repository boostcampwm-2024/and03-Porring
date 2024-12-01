package com.kolown.common.component

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kolown.common.R

@Composable
internal fun FollowDialog(
    modifier: Modifier = Modifier,
    onClickCancel: () -> Unit = {},
    onClickConfirm: (String) -> Unit = {}
) {
    val textValue = remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = { onClickCancel() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            modifier = modifier.size(
                width = 380.dp,
                height = 248.dp
            ),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "팔로우 추가하기", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "원하는 이름을 입력해 주세요.", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    value = textValue.value,
                    onValueChange = { textValue.value = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFEAEEFF),
                        unfocusedContainerColor = Color(0xFFEAEEFF),
                        focusedTextColor = Color(0xFF598AFF),
                        unfocusedTextColor = Color(0xFF598AFF)
                    ),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.icon_delete),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                textValue.value = ""
                            }
                        )
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    TextButton(
                        onClick = onClickCancel,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = "취소하기", color = Color(0xFFFF568A))
                    }
                    TextButton(
                        onClick = {
                            onClickConfirm(textValue.value)
                            onClickCancel()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = "팔로우 추가", color = Color(0xFF598AFF))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDialog(){
    FollowDialog() { }
}