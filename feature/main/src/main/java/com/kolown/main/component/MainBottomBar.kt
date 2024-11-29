package com.kolown.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kolown.designsystem.ui.theme.Primary
import com.kolown.designsystem.ui.theme.PrimaryUnActive
import com.kolown.main.navigation.MainMenu
import com.kolown.navigation.MainMenuRoute
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainBottomBar(
    isLoggedIn: Boolean = false,
    onShowLoginSnackBar: () -> Unit = {},
    modifier: Modifier = Modifier,
    visible: Boolean,
    menus: PersistentList<MainMenu>,
    currentMenu: MainMenu?,
    onMenuSelected: (MainMenu) -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            menus.forEach { menu ->
                MainBottomBarItem(
                    menu = menu,
                    selected = menu == currentMenu,
                    onClick = {
                        if (menu.route == MainMenuRoute.Camera && isLoggedIn.not()) {
                            onShowLoginSnackBar()
                        } else {
                            onMenuSelected(menu)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.MainBottomBarItem(
    modifier: Modifier = Modifier,
    menu: MainMenu,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .selectable(
                selected = selected,
                indication = null,
                role = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(menu.iconResId),
            contentDescription = menu.contentDescription,
            tint = if (selected || menu == MainMenu.CAMERA) Primary else PrimaryUnActive,
            modifier = Modifier.size(
                if (menu == MainMenu.CAMERA) 48.dp else 24.dp
            )
        )

        if (menu != MainMenu.CAMERA) {
            Text(
                text = menu.contentDescription,
                color = if (selected) Primary else PrimaryUnActive,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainBottomBar() {
        MainBottomBar(
            visible = true,
            menus = MainMenu.entries.toPersistentList(),
            currentMenu = MainMenu.HOME,
            onMenuSelected = {}
        )
}