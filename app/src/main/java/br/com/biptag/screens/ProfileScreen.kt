package br.com.biptag.screens

import android.content.res.Configuration
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.biptag.R
import br.com.biptag.components.BottomBar
import br.com.biptag.components.TopBar
import br.com.biptag.ui.theme.BipTagTheme

@Composable
fun ProfileScreen() {

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(title = stringResource(R.string.profile))
            },
            bottomBar = {
                BottomBar()
            },
        ) { paddingValues ->
            ProfileContentScreen(modifier = Modifier.padding(paddingValues))
        }
    }
}

@Composable
fun MyTopProfileBar() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.profile),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun ProfileContentScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // User information and profile picture
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Maria Silva",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "maria@email.com",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Upgradeable user data
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.personal_data),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            ProfileCardItem(
                label = stringResource(R.string.phone),
                value = "(11) 99999-9999"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileCardItem(
                label = stringResource(R.string.notifications),
                trailingContent = {
                    Switch( modifier = Modifier.padding(0.dp), checked = true, onCheckedChange = {})
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            ProfileCardItem(
                label = stringResource(R.string.app_credits),
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(128.dp))

        // Logout
        Text(
            text = stringResource(R.string.logout),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun ProfileCardItem(
    label: String,
    value: String? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceBetween) {
                if (value != null) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            trailingContent?.invoke()
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Composable
private fun ProfileScreenPreview() {
    BipTagTheme() {
        ProfileScreen()
    }
}