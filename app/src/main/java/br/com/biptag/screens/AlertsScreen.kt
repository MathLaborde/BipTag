package br.com.biptag.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.BottomBar
import br.com.biptag.components.TopBar
import br.com.biptag.model.Alert
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AlertRepository
import br.com.biptag.repository.AuthRepository
import br.com.biptag.ui.theme.BipTagTheme
import br.com.biptag.utils.formatRelativeTime
import coil.compose.AsyncImage

@Composable
fun AlertsScreen(navController: NavController) {

    // TODO, pensei em mudar as sessões "Hoje" e "Esta semana" para "Meus Alertas" "Outros Alertas". Assim a pessoas tem uma lista dos alertas dela também.

    var myAlerts by remember { mutableStateOf(listOf<Alert>()) }
    var othersAlerts by remember { mutableStateOf(listOf<Alert>()) }
    val repository = remember { AlertRepository() }
    val authRepository = remember { AuthRepository() }

    val user = authRepository.getCurrentUser()

    LaunchedEffect(Unit) {
        try {
            val result = repository.getActiveAlerts() ?: emptyList()

            myAlerts = result.filter { alert -> alert.itemData?.userId == user?.id }
            othersAlerts = result.filter { alert -> alert.itemData?.userId != user?.id }
        } catch (e: Exception) {
            Log.e("Supabase", "Erro ao carregar itens", e)
        }
    }

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
            if (myAlerts.isNotEmpty()) {
                Text(
                    text = "Meus Alertas",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                )

                myAlerts.forEach { alert ->
                    AlertCard(
                        image = alert.itemData?.image,
                        title = alert.itemData?.name,
                        subtitle = "Item perdido proximo a você!",
                        timeText = formatRelativeTime(alert.incidentDate),
                        isUnread = true,
                        onClick = {
                            navController.navigate(
                                Destination.AlertIssuedScreen.route
                            )
                        }
                    )
                }
            }

            if (othersAlerts.isNotEmpty()) {
                Text(
                    text = "Outros Alertas",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                )
                othersAlerts.forEach { alert ->
                    AlertCard(
                        image = alert.itemData?.image,
                        title = alert.itemData?.name,
                        subtitle = "Item perdido proximo a você!",
                        timeText = formatRelativeTime(alert.incidentDate),
                        isUnread = false,
                        onClick = {
                            navController.navigate(
                                Destination.LostItemScreen.createRoute(
                                    alert.id as Int
                                )
                            )
                        }
                    )
                }
            }

            if (othersAlerts.isEmpty() && myAlerts.isEmpty()) {
                Text(
                    text = "Nenhum Alerta Ativo.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                )
            }
//
//            AlertCard(
//                icon = Icons.Outlined.Notifications,
//                iconContainerColor = Color(0xFFFFEBEE),
//                iconTintColor = Color(0xFFE53935),
//                title = "Item perdido por perto",
//                subtitle = "Bicicleta Caloi vista a 300 m",
//                timeText = "12 min",
//                isUnread = true,
//                onClick = {
//                    navController.navigate(
//                        Destination.ConfirmationScreen.createRoute(
//                            1
//                        )
//                    )
//                    // TODO Colocar a tela dinamica com os dados do banco e colocar o ID do alerta passando para o alertId no navController acima.
//                }
//            )
//
//
//
//            AlertCard(
//                icon = Icons.Outlined.CheckCircle,
//                iconContainerColor = Color(0xFFE8F5E9),
//                iconTintColor = Color(0xFF4CAF50),
//                title = "Devolução concluída",
//                subtitle = "Carteira devolvida com sucesso",
//                timeText = "3 dias",
//                isUnread = false,
//                onClick = {
//                    navController.navigate(
//                        Destination.ConfirmationScreen.createRoute(
//                            1
//                        )
//                    )
//                    // TODO Colocar a tela dinamica com os dados do banco e colocar o ID do alerta passando para o alertId no navController acima.
//                }
//            )
//
//            AlertCard(
//                icon = Icons.Outlined.Search,
//                iconContainerColor = MaterialTheme.colorScheme.surfaceVariant,
//                iconTintColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                title = "Alerta encerrado",
//                subtitle = "Notebook Dell localizado",
//                timeText = "5 dias",
//                isUnread = false,
//                onClick = {
//                    navController.navigate(
//                        Destination.ConfirmationScreen.createRoute(
//                            1
//                        )
//                    )
//                    // TODO Colocar a tela dinamica com os dados do banco e colocar o ID do alerta passando para o alertId no navController acima.
//                }
//            )
        }
    }
}

@Composable
fun AlertCard(
    image: String?,
    title: String?,
    subtitle: String,
    timeText: String?,
    isUnread: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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
                    .background(color = MaterialTheme.colorScheme.outline),
                contentAlignment = Alignment.Center
            ) {
                if (image != null){
                    AsyncImage(
                        model = image,
                        contentDescription = "Imagem",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = null,
                        tint = Color(255, 0, 0, 1),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title ?: "",
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
                    text = timeText ?: "",
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