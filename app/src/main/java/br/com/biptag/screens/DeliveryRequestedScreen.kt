package br.com.biptag.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.biptag.components.TopBar
import br.com.biptag.navigation.Destination
import br.com.biptag.model.Alert
import br.com.biptag.repository.AlertRepository

@Composable
fun DeliveryRequestedScreen(
    navController: NavController,
    alertId: Int
) {
    val alertRepository = remember { AlertRepository() }
    var alertData by remember { mutableStateOf<Alert?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Busca os dados reais do Alerta e do Item usando o seu Repository
    LaunchedEffect(alertId) {
        try {
            alertData = alertRepository.getAlertById(alertId)
        } catch (e: Exception) {
            android.util.Log.e("DeliveryReq", "Erro", e)
        } finally {
            isLoading = false
        }
    }

    val itemName = alertData?.itemData?.name ?: "Buscando item..."
    val itemTag = alertData?.itemData?.tagId ?: "Sem tag"
    val deliveryAddress = alertData?.lastSeenAddress ?: "Endereço não informado"
    val ownerFirstName = alertData?.itemData?.userData?.name?.split(" ")?.firstOrNull() ?: "Você"

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopBar(
                title = "Entrega solicitada",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                tonalElevation = 2.dp,
                shadowElevation = 8.dp,
                border = BorderStroke(0.5.dp, Color(0xFFE5E7EB))
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate(Destination.OwnerTrackScreen.createRoute(alertId))
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF263E4D))
                    ) {
                        Text("Acompanhar entrega", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState()).padding(20.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFEF8E7),
                    border = BorderStroke(1.dp, Color(0xFFF7DE9B))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        Icon(Icons.Outlined.NotificationsActive, contentDescription = null, tint = Color(0xFFB27B16), modifier = Modifier.size(20.dp).padding(top = 2.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Entrega solicitada", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF8A640F))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$ownerFirstName pediu um motorista parceiro para devolver o seu $itemName.", style = MaterialTheme.typography.bodySmall, color = Color(0xFF8A640F), lineHeight = 18.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF1F5F9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Devices, contentDescription = null, tint = Color(0xFF475569))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(itemName, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Tag $itemTag", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                        }
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFFEF8E7)).padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("A caminho", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB27B16))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("O que acontece agora", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        Spacer(modifier = Modifier.height(24.dp))

                        TimelineOwnerStep(active = true, title = "Motorista a caminho da coleta", subtitle = "Motorista busca o item.", isLast = false)
                        TimelineOwnerStep(active = false, title = "Item coletado", subtitle = "Motorista valida o código de entrega", isLast = false)
                        TimelineOwnerStep(active = false, title = "A caminho de você", subtitle = "Você acompanha pelo mapa", isLast = false)
                        TimelineOwnerStep(active = false, title = "Entrega e validação", subtitle = "Você informa seu código e avalia", isLast = true)

                        Spacer(modifier = Modifier.height(24.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(20.dp))

                        DetailOwnerRow("Local de entrega", deliveryAddress, isBold = true)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun TimelineOwnerStep(active: Boolean, title: String, subtitle: String, isLast: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top // O erro estava aqui!
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(20.dp)) {
            if (active) {
                Box(modifier = Modifier.size(20.dp).background(Color(0xFF263E4D), CircleShape), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                }
            } else {
                Box(modifier = Modifier.size(20.dp).border(2.dp, Color(0xFFCBD5E1), CircleShape).background(Color.White, CircleShape))
            }
            if (!isLast) Box(modifier = Modifier.width(2.dp).height(36.dp).background(Color(0xFFE2E8F0)))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 24.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = if (active) Color(0xFF1E293B) else Color(0xFF94A3B8))
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = if (active) Color(0xFF6B7280) else Color(0xFFCBD5E1))
        }
    }
}

@Composable
fun DetailOwnerRow(label: String, value: String, isBold: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = Color(0xFF1E293B),
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}