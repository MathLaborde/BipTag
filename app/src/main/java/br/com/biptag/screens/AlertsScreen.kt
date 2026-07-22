package br.com.biptag.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.BottomBar
import br.com.biptag.components.TopBar
import br.com.biptag.ui.theme.BipTagTheme

@Composable
fun AlertsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(title = "Alertas")
        },
        bottomBar = {
            BottomBar(navController)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "HOJE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
            )

            AlertCard(
                icon = Icons.Outlined.LocationOn,
                iconContainerColor = Color(0xFFE8F5E9),
                iconTintColor = Color(0xFF4CAF50),
                title = "Alguém está com seu item!",
                subtitle = "Notebook Dell · toque para ver",
                timeText = "agora",
                isUnread = true
            )

            AlertCard(
                icon = Icons.Outlined.Notifications,
                iconContainerColor = Color(0xFFFFEBEE),
                iconTintColor = Color(0xFFE53935),
                title = "Item perdido por perto",
                subtitle = "Bicicleta Caloi vista a 300 m",
                timeText = "12 min",
                isUnread = true
            )

            Text(
                text = "ESTA SEMANA",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
            )

            AlertCard(
                icon = Icons.Outlined.Sell,
                iconContainerColor = Color(0xFFE3F2FD),
                iconTintColor = Color(0xFF1E88E5),
                title = "Etiqueta vinculada",
                subtitle = "Câmera Canon EOS protegida",
                timeText = "2 dias",
                isUnread = false
            )

            AlertCard(
                icon = Icons.Outlined.CheckCircle,
                iconContainerColor = Color(0xFFE8F5E9),
                iconTintColor = Color(0xFF4CAF50),
                title = "Devolução concluída",
                subtitle = "Carteira devolvida com sucesso",
                timeText = "3 dias",
                isUnread = false
            )

            AlertCard(
                icon = Icons.Outlined.Search,
                iconContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                iconTintColor = MaterialTheme.colorScheme.onSurfaceVariant,
                title = "Alerta encerrado",
                subtitle = "Notebook Dell localizado",
                timeText = "5 dias",
                isUnread = false
            )
        }
    }
}

@Composable
fun AlertCard(
    icon: ImageVector,
    iconContainerColor: Color,
    iconTintColor: Color,
    title: String,
    subtitle: String,
    timeText: String,
    isUnread: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTintColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )

                if (isUnread) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFFE53935), CircleShape)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AlertsScreenPreview() {
    BipTagTheme {
        AlertsScreen(rememberNavController())
    }
}