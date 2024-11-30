package com.kolown.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.kolown.designsystem.component.PorringIconButton
import com.kolown.designsystem.component.PorringTopAppBar
import com.kolown.setting.component.MenuDivider
import com.kolown.setting.component.TextLabel
import com.kolown.setting.component.TextMenu

@Composable
internal fun SettingRoute(
    popBackStack: () -> Unit,
    updateLoginState: () -> Unit,
    settingViewModel: SettingViewModel = hiltViewModel(),
    padding: PaddingValues,
) {
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(true) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            settingViewModel.logoutEnd.collect { logoutComplete ->
                if (logoutComplete) {
                    updateLoginState()
                    popBackStack()
                }
            }
        }
    }

    SettingScreen(
        clickLogout = settingViewModel::logout,
        popBackStack = popBackStack, padding = padding
    )
}

@Composable
private fun SettingScreen(
    clickLogout: () -> Unit = {},
    popBackStack: () -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
    ) {
        PorringTopAppBar(
            title = "설정",
            navigationIcon = {
                PorringIconButton(
                    icon = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                    onClick = popBackStack,
                    contentDescription = stringResource(R.string.string_back_button)
                )
            }
        )

        TextLabel(stringResource(R.string.string_label_use_porring))

        TextMenu(stringResource(R.string.string_menu_show_my_reaction))
        TextMenu(stringResource(R.string.string_menu_notify))

        MenuDivider()

        TextLabel(stringResource(R.string.string_label_manage_account))
        TextMenu(stringResource(R.string.string_menu_user_info))

        MenuDivider()

        TextMenu(
            title = stringResource(R.string.string_menu_logout),
            color = Color.Red,
            onClick = clickLogout
        )
        TextMenu(
            title = stringResource(R.string.string_menu_dropout_user), color = Color.Red
        )
    }
}