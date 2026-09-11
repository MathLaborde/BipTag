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
import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@Composable
fun DeliveryRequestedScreen(
    navController: NavController,
    alertId: Int
) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = {
                            // Navega para a R3 - Acompanhar motorista (já usando o seu alertId)
                            navController.navigate(Destination.TrackReturnScreen.createRoute(alertId))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF263E4D))
                    ) {
                        Text(
                            text = "Acompanhar entrega",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Banner Amarelo
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFEF8E7),
                border = BorderStroke(1.dp, Color(0xFFF7DE9B))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NotificationsActive,
                        contentDescription = "Aviso",
                        tint = Color(0xFFB27B16),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Entrega solicitada",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8A640F)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Marina S. pediu um motorista parceiro para devolver sua Bicicleta Caloi.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF8A640F),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card do Item
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF1F5F9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DirectionsBike,
                            contentDescription = null,
                            tint = Color(0xFF475569)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Bicicleta Caloi",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = "Código BIP-4827",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF8E7))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "A caminho",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB27B16)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card "O que acontece agora"
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "O que acontece agora",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    InstructionStep(active = true, title = "Motorista a caminho da coleta", subtitle = "Rafael S. busca o item com Marina S.", isLast = false)
                    InstructionStep(active = false, title = "Item coletado", subtitle = "Marina informa o código de entrega", isLast = false)
                    InstructionStep(active = false, title = "A caminho de você", subtitle = "Você acompanha pelo mapa em tempo real", isLast = false)
                    InstructionStep(active = false, title = "Entrega e validação", subtitle = "Você informa seu código e avalia", isLast = true)

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(16.dp))

                    DetailRowText("Chegada estimada", "Hoje, 15h40", isBold = true)
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailRowText("Local de entrega", "Av. Paulista, 1000", isBold = true)
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailRowText("Janela informada", "Tarde - 12h às 18h", isBold = true)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InstructionStep(active: Boolean, title: String, subtitle: String, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(24.dp)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(if (active) 0.dp else 2.dp, if (active) Color.Transparent else Color(0xFFCBD5E1), CircleShape)
                    .background(if (active) Color(0xFF263E4D) else Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (active) {
                    Box(modifier = Modifier.size(10.dp).background(Color.White, CircleShape))
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(if (active) Color(0xFF263E4D) else Color(0xFFE2E8F0))
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (active) Color(0xFF1E293B) else Color(0xFF94A3B8)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = if (active) Color(0xFF6B7280) else Color(0xFFCBD5E1)
            )
        }
    }
}

@Composable
fun DetailRowText(label: String, value: String, isBold: Boolean) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280))
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal, color = Color(0xFF1E293B))
    }
}