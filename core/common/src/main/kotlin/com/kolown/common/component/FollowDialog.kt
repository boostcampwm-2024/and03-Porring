package com.kolown.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kolown.common.R
import com.kolown.designsystem.ui.theme.Error
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.designsystem.ui.theme.Surface2

@Composable
fun FollowDialog(
    modifier: Modifier = Modifier,
    onClickCancel: () -> Unit = {},
    onClickConfirm: (String) -> Unit = {}
) {
    val textValue = remember { mutableStateOf("") }
    val isFollowerNameEmpty = remember { mutableStateOf(false) }
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
                OutlinedTextField(
                    value = textValue.value,
                    onValueChange = {
                        textValue.value = it.take(10)
                        if (it.isNotEmpty()) isFollowerNameEmpty.value = false
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Primary,
                        unfocusedIndicatorColor = Surface2,
                        focusedContainerColor = Surface2,
                        unfocusedContainerColor = Surface2,
                        focusedTextColor = Primary,
                        unfocusedTextColor = Primary
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = Primary,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                textValue.value = ""
                            }
                        )
                    },
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(5.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (isFollowerNameEmpty.value) Text(
                        text = "이름을 입력해주세요.",
                        fontSize = 10.sp,
                        modifier = Modifier.align(Alignment.CenterStart),
                        color = Error
                    )
                    Text(
                        text = "(${textValue.value.length}/10)",
                        modifier = Modifier.align(Alignment.CenterEnd),
                        color = Primary
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    TextButton(
                        onClick = onClickCancel,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = stringResource(R.string.string_follow_cancel), color = Error)
                    }
                    TextButton(
                        onClick = {
                            if (textValue.value.isNotEmpty()) {
                                onClickConfirm(textValue.value)
                                onClickCancel()
                            } else isFollowerNameEmpty.value = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(text = stringResource(R.string.string_add_follow), color = Primary)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewDialog() {
    FollowDialog() { }
}
