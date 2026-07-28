package br.com.biptag.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
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
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.Alert
import br.com.biptag.model.Item
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AlertRepository
import br.com.biptag.repository.ItemRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun LostItemScreen(navController: NavController, alertId: Int) {
    val alertRepository = remember { AlertRepository() }
    val itemRepository = remember { ItemRepository() }

    var alert by remember { mutableStateOf<Alert?>(null) }
    var item by remember { mutableStateOf<Item?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-23.5611, -46.6565), 15f)
    }

    LaunchedEffect(alertId) {
        isLoading = true

        // Verifica se é um dos nossos itens fictícios de apresentação
        if (alertId == 9991 || alertId == 9992) {
            if (alertId == 9991) {
                alert = Alert(id = 9991, itemId = 9991, type = "lost", lastSeenLat = -23.5611, lastSeenLng = -46.6565, status = "active")
                item = Item(id = 9991, name = "Bicicleta Caloi", description = "Quadro azul, aro 29, adesivo na traseira.", category = 1, status = "lost")
            } else {
                alert = Alert(id = 9992, itemId = 9992, type = "lost", lastSeenLat = -23.5585, lastSeenLng = -46.6580, status = "active")
                item = Item(id = 9992, name = "Mochila Azul", description = "Mochila escolar azul escura.", category = 2, status = "lost")
            }

            val position = LatLng(alert?.lastSeenLat ?: 0.0, alert?.lastSeenLng ?: 0.0)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(position, 15f)
        } else {
            // Se não for fictício, busca do banco de dados real (Supabase)
            alert = alertRepository.getAlertById(alertId)
            alert?.let {
                val position = LatLng(it.lastSeenLat ?: 0.0, it.lastSeenLng ?: 0.0)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(position, 15f)
                item = itemRepository.getItemById(it.itemId)
            }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Detalhes do Alerta",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.background, shadowElevation = 8.dp) {
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
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF629EB0))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // 1. Placeholder da Imagem
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFEEF5F8))
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(56.dp).background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Image, contentDescription = "Foto do item", tint = Color.Gray)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Foto do item", color = Color.Gray, fontSize = 14.sp)
                    }

                    Text(
                        text = if (alert?.type == "lost") "Perdido" else "Roubado",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).background(Color(0xFFD32F2F), RoundedCornerShape(8.dp)).padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                // 2. Título Dinâmico
                Column {
                    Text(
                        text = item?.name ?: "Item Desconhecido",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B2A36)
                    )
                }

                // 3. Card do Usuário (Para o futuro pode ser puxado do Auth/User)
                Surface(
                    shape = RoundedCornerShape(16.dp), color = Color.White, border = BorderStroke(1.dp, Color(0xFFE0E0E0)), modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(48.dp).background(Color(0xFFEEF5F8), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Outlined.Person, contentDescription = null, tint = Color(0xFF233540))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Dono do item", fontWeight = FontWeight.Bold, color = Color(0xFF1B2A36))
                            Text("Informação protegida", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }

                // 4. Mini Mapa Dinâmico
                Surface(
                    shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.fillMaxWidth().height(140.dp)
                ) {
                    Box {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            uiSettings = MapUiSettings(scrollGesturesEnabled = false, zoomGesturesEnabled = false, zoomControlsEnabled = false)
                        ) {
                            alert?.let {
                                val position = LatLng(it.lastSeenLat ?: 0.0, it.lastSeenLng ?: 0.0)
                                Marker(state = MarkerState(position = position))
                            }
                        }
                    }
                }

                // 5. Card de Descrição Dinâmico
                Surface(
                    shape = RoundedCornerShape(16.dp), color = Color.White, border = BorderStroke(1.dp, Color(0xFFE0E0E0)), modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Descrição", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item?.description ?: "Nenhuma descrição fornecida.",
                            color = Color(0xFF1B2A36),
                            fontSize = 14.sp
                        )
                    }
                }

                // 6. Aviso de Segurança (Fixo)
                Surface(
                    shape = RoundedCornerShape(12.dp), color = Color(0xFFEEF5F8), modifier = Modifier.fillMaxWidth()
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
}