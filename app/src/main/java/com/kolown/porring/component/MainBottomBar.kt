package com.kolown.porring.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kolown.designsystem.Primary
import com.kolown.designsystem.PrimaryUnActive
import com.kolown.porring.MainMenu
import com.kolown.porring.ui.theme.PorringTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainBottomBar(
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
                .height(80.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            menus.forEach { menu ->
                MainBottomBarItem(
                    menu = menu,
                    selected = menu == currentMenu,
                    onClick = { onMenuSelected(menu) }
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
    PorringTheme {
        MainBottomBar(
            visible = true,
            menus = MainMenu.entries.toPersistentList(),
            currentMenu = MainMenu.HOME,
            onMenuSelected = {}
        )
    }
}