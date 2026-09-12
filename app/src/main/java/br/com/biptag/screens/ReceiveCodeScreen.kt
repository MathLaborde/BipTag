package br.com.biptag.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.WarningAmber
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
fun ReceiveCodeScreen(
    navController: NavController,
    alertId: Int
) {
    val alertRepository = remember { AlertRepository() }
    var alertData by remember { mutableStateOf<Alert?>(null) }

    // Busca os dados reais para exibir o item correto
    LaunchedEffect(alertId) {
        try {
            alertData = alertRepository.getAlertById(alertId)
        } catch (e: Exception) {
            android.util.Log.e("ReceiveCode", "Erro ao carregar item", e)
        }
    }

    val itemName = alertData?.itemData?.name ?: "Buscando item..."
    val itemTag = alertData?.itemData?.tagId ?: "Sem tag"
    val receiveCode = "8153" // Código de segurança

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopBar(
                title = "Código de recebimento",
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
                            navController.navigate(Destination.OwnerReviewScreen.createRoute(alertId))
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF263E4D))
                    ) {
                        Text("Confirmar recebimento", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text("Confira o item antes de informar o código ao motorista parceiro. O código libera a baixa da entrega.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF6B7280), lineHeight = 20.sp)

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SEU CÓDIGO DE RECEBIMENTO", style = MaterialTheme.typography.labelSmall, color = Color(0xFF9CA3AF), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        receiveCode.forEach { digit ->
                            Box(
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp).height(68.dp).background(Color(0xFFDCFCE7), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(digit.toString(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    QrCodePlaceholderVisual()
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
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFF1F5F9)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.Devices, contentDescription = null, tint = Color(0xFF475569))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(itemName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        Text("Tag $itemTag", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFE0F2FE)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text("Em entrega", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = Color(0xFFFEF8E7), border = BorderStroke(1.dp, Color(0xFFF7DE9B))) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.WarningAmber, contentDescription = "Aviso", tint = Color(0xFFB27B16), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Confira número de série e etiqueta RFID antes de liberar o código.", style = MaterialTheme.typography.bodySmall, color = Color(0xFF8A640F), lineHeight = 18.sp)
                }
            }
        }
    }
}

@Composable
private fun QrCodePlaceholderVisual() {
    val matrix = listOf(
        listOf(1, 1, 0, 1, 1),
        listOf(1, 0, 1, 0, 1),
        listOf(0, 1, 0, 1, 0),
        listOf(1, 0, 1, 0, 1),
        listOf(1, 1, 0, 1, 1)
    )
    Column(modifier = Modifier.size(80.dp), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {
        matrix.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { active ->
                    Box(modifier = Modifier.size(10.dp).background(if (active == 1) Color(0xFF1E293B) else Color.Transparent, RoundedCornerShape(2.dp)))
                }
            }
        }
    }
}