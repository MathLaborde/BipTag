package br.com.biptag.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.biptag.R
import br.com.biptag.screens.BottomNavigationItem

@Composable
fun MyBottomBar() {
    val items = listOf(
        BottomNavigationItem(title = stringResource(R.string.inventory), icon = Icons.Outlined.Inventory2),
        BottomNavigationItem(title = stringResource(R.string.profile), Icons.Outlined.Person)
    )

    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    NavigationBar() {
        items.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = {},
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
            )
        }
    }
}