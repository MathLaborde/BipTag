package br.com.biptag.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.navigation.Destination
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LostItemScreen(navController: NavController, alertId: Int) {
    // Busca os dados dinâmicos com base no ID que veio da navegação
    val item = remember(alertId) { getMockItemDetails(alertId) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(item.location, 15f)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Item perdido",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                shadowElevation = 8.dp
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    PrimaryButton(
                        text = "Estou com este item",
                        onClick = {
                            navController.navigate(Destination.ConfirmationScreen.route.replace("{alertId}", alertId.toString()))
                        },
                        containerColor = Color(0xFF233540),
                        contentColor = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. Placeholder da Imagem com a Tag de tempo dinâmica
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEEF5F8))
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Image, contentDescription = "Foto do item", tint = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Foto do item", color = Color.Gray, fontSize = 14.sp)
                }

                Text(
                    text = item.timeLost, // Dinâmico
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color(0xFFD32F2F), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            // 2. Título e Categoria Dinâmicos
            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B2A36)
                )
                Text(
                    text = item.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // 3. Card do Usuário Dinâmico
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFEEF5F8), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Person, contentDescription = null, tint = Color(0xFF233540))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.reporterName, fontWeight = FontWeight.Bold, color = Color(0xFF1B2A36))
                        Text(item.reporterTime, fontSize = 12.sp, color = Color.Gray)
                    }
                    Text(
                        text = item.distance,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF629EB0),
                        modifier = Modifier
                            .background(Color(0xFFEEF5F8), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // 4. Mini Mapa Dinâmico
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Box {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        uiSettings = MapUiSettings(
                            scrollGesturesEnabled = false,
                            zoomGesturesEnabled = false,
                            rotationGesturesEnabled = false,
                            tiltGesturesEnabled = false,
                            zoomControlsEnabled = false
                        )
                    ) {
                        Marker(state = MarkerState(position = item.location))
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Outlined.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(item.addressHint, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // 5. Card de Descrição Dinâmico
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Descrição", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.description,
                        color = Color(0xFF1B2A36),
                        fontSize = 14.sp
                    )
                }
            }

            // 6. Aviso de Segurança (Fixo)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFEEF5F8),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Shield, contentDescription = null, tint = Color(0xFF629EB0))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "A entrega é combinada por ponto parceiro ou motoboy. Não compartilhe dados pessoais.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- CLASSES DE DADOS MOCK PARA TESTAR A INTERFACE ---

data class LostItemDetail(
    val title: String,
    val category: String,
    val timeLost: String,
    val distance: String,
    val reporterName: String,
    val reporterTime: String,
    val description: String,
    val addressHint: String,
    val location: LatLng
)

fun getMockItemDetails(alertId: Int): LostItemDetail {
    return when (alertId) {
        1 -> LostItemDetail(
            title = "Bicicleta Caloi",
            category = "Veículos",
            timeLost = "Perdido há 2h",
            distance = "a 300 m",
            reporterName = "Marina S.",
            reporterTime = "Reportou há 3 horas",
            description = "Quadro azul, aro 29, adesivo na traseira.",
            addressHint = "Av. Paulista, ~1000",
            location = LatLng(-23.5611, -46.6565)
        )
        2 -> LostItemDetail(
            title = "Mochila Azul",
            category = "Acessórios",
            timeLost = "Perdido há 5h",
            distance = "a 800 m",
            reporterName = "Lucas R.",
            reporterTime = "Reportou há 5 horas",
            description = "Mochila escolar azul escura com um chaveiro lateral.",
            addressHint = "R. Augusta, ~1500",
            location = LatLng(-23.5585, -46.6580)
        )
        // Fallback caso seja um ID desconhecido
        else -> LostItemDetail(
            title = "Item Desconhecido",
            category = "Outros",
            timeLost = "Desconhecido",
            distance = "-",
            reporterName = "Usuário Anônimo",
            reporterTime = "-",
            description = "Sem descrição disponível para este item.",
            addressHint = "Localização Indisponível",
            location = LatLng(-23.5611, -46.6565)
        )
    }
}