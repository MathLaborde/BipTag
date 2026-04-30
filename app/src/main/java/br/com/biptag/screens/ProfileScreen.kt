package br.com.biptag.screens

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.R
import br.com.biptag.components.BottomBar
import br.com.biptag.components.TopBar
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.SharedPreferencesUserRepository
import br.com.biptag.ui.theme.BipTagTheme

@Composable
fun ProfileScreen(navController: NavController) {

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(title = stringResource(R.string.profile))
            },
            bottomBar = {
                BottomBar(navController)
            },
        ) { paddingValues ->
            ProfileContentScreen(modifier = Modifier.padding(paddingValues), navController)
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun ProfileContentScreen(modifier: Modifier = Modifier, navController: NavController) {

    val userShared = SharedPreferencesUserRepository(LocalContext.current)

    val user = userShared.getUser()

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
            text = user.name,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = user.email,
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
                value = user.phoneNumber,
                modifier = Modifier.clickable{}
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileCardItem(
                label = stringResource(R.string.notifications),
                value = "Ativadas",
                modifier = Modifier.clickable{},
                trailingContent = {
                    Switch( modifier = Modifier.padding(0.dp), checked = user.notifications, onCheckedChange = {})
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            ProfileCardItem(
                modifier = Modifier.clickable{
                    navController.navigate(
                        Destination.CreditsScreen.route
                    )
                },
                label = stringResource(R.string.app_credits),
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                    )
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Logout
        Text(
            text = stringResource(R.string.logout),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clickable{
                    navController
                        .navigate(
                            Destination.LoginScreen.route
                        )
                }
        )
    }
}

@Composable
fun ProfileCardItem(
    modifier: Modifier,
    label: String,
    value: String? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (value != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            if (trailingContent != null) {
                trailingContent()
            }
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
        ProfileScreen(navController = rememberNavController())
    }
}