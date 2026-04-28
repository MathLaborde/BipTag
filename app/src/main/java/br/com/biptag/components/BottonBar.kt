package br.com.biptag.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.biptag.R
import br.com.biptag.screens.BottomNavigationItem
import br.com.biptag.ui.theme.Gray

@Composable
fun BottomBar() {
    val items = listOf(
        BottomNavigationItem(title = stringResource(R.string.inventory), icon = Icons.Outlined.Inventory2),
        BottomNavigationItem(title = stringResource(R.string.profile), Icons.Outlined.Person)
    )
    NavigationBar(
        modifier = Modifier.drawBehind {
            // Desenha uma linha no topo (y = 0)
            drawLine(
                color = Gray,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = 2.dp.toPx()
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
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

@Preview
@Composable
private fun BottomBarPreview() {
    BottomBar()
}