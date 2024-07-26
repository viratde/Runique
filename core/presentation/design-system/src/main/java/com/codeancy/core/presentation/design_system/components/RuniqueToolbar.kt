package com.codeancy.core.presentation.design_system.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codeancy.core.presentation.design_system.AnalyticsIcon
import com.codeancy.core.presentation.design_system.ArrowLeftIcon
import com.codeancy.core.presentation.design_system.LogoIcon
import com.codeancy.core.presentation.design_system.LogoutIcon
import com.codeancy.core.presentation.design_system.Poppins
import com.codeancy.core.presentation.design_system.R
import com.codeancy.core.presentation.design_system.RuniqueTheme
import com.codeancy.core.presentation.design_system.components.util.DropDownItem


@OptIn(ExperimentalMaterial3Api::class)
@Stable
@Composable
fun RuniqueToolbar(
    modifier: Modifier = Modifier,
    showBackButton: Boolean,
    title: String,
    menu: List<DropDownItem> = listOf(),
    onMenuItemClick: (Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    startContent: (@Composable () -> Unit)? = null
) {

    var isDropDownOpen by rememberSaveable {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                startContent?.invoke()
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = Poppins
                )

            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = ArrowLeftIcon,
                        contentDescription = stringResource(id = R.string.go_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        actions = {
            if (menu.isNotEmpty()) {

                Box {

                    DropdownMenu(
                        expanded = isDropDownOpen,
                        onDismissRequest = { isDropDownOpen = false }
                    ) {

                        menu.forEachIndexed { index, item ->

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable {
                                        onMenuItemClick(index)
                                    }
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {

                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )


                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = item.title
                                )


                            }

                        }

                    }

                    IconButton(onClick = { isDropDownOpen = !isDropDownOpen }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(id = R.string.open_menu),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                }

            }
        },
        scrollBehavior = scrollBehavior
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun RuniqueToolbarPreview() {
    RuniqueTheme {
        RuniqueToolbar(
            showBackButton = true,
            title = "Runique",
            menu = listOf(
                DropDownItem(title = "Analytics", icon = AnalyticsIcon),
                DropDownItem(title = "Logout", icon = LogoutIcon)
            ),
            onMenuItemClick = {

            },
            onBackClick = {

            },
            startContent = {
                Icon(
                    imageVector = LogoIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(35.dp)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}