package br.com.biptag.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.R
import br.com.biptag.components.BottomBar
import br.com.biptag.components.TopBar
import br.com.biptag.model.User
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AuthRepository
import br.com.biptag.ui.theme.BipTagTheme
import kotlinx.coroutines.launch

// Função para formatar o telefone
fun formatPhoneNumber(phone: String): String {
    val digits = phone.filter { it.isDigit() }
    return if (digits.length == 11) {
        "(${digits.substring(0, 2)}) ${digits.substring(2, 7)}-${digits.substring(7)}"
    } else {
        phone
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    val isPreview = LocalInspectionMode.current
    val scope = rememberCoroutineScope()

    val authRepository = remember {
        if (isPreview) null else AuthRepository()
    }

    val user = remember(authRepository) {
        authRepository?.getCurrentUser() ?: if (isPreview) {
            User(
                name = "Maria Silva",
                email = "maria@email.com",
                phoneNumber = "11999999999"
            )
        } else null
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(title = stringResource(R.string.profile))
            },
            bottomBar = {
                BottomBar(navController)
            },
        ) { paddingValues ->
            user?.let {
                ProfileContentScreen(
                    modifier = Modifier.padding(paddingValues),
                    navController = navController,
                    user = it,
                    onLogout = {
                        scope.launch {
                            try {
                                authRepository?.signOut()
                            } catch (_: Exception) {

                            }
                            navController.navigate(Destination.LoginScreen.route) {
                                popUpTo(Destination.ProfileScreen.route) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun ProfileContentScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    user: User,
    onLogout: () -> Unit
) {
    var notifications by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.personal_data).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileCardItem(
                label = stringResource(R.string.phone),
                value = formatPhoneNumber(user.phoneNumber),
                modifier = Modifier.clickable {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileCardItem(
                label = stringResource(R.string.notifications),
                value = if (notifications) "Ativadas" else "Desativadas",
                modifier = Modifier.clickable { notifications = !notifications },
                trailingContent = {
                    Switch(
                        modifier = Modifier.scale(0.85f),
                        checked = notifications,
                        onCheckedChange = { notifications = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                            uncheckedTrackColor = Color(0xFFE2E8F0),
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProfileCardItem(
                modifier = Modifier.clickable {
                    navController.navigate(Destination.CreditsScreen.route)
                },
                label = stringResource(R.string.app_credits),
                trailingContent = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier
                    )
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.logout),
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF334155),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clickable(onClick = onLogout)
        )
    }
}

@Composable
fun ProfileCardItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .defaultMinSize(minHeight = 64.dp)
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column() {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (value != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
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