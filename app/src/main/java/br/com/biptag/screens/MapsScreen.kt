package br.com.biptag.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PedalBike
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
import br.com.biptag.components.BipTagTextField
import br.com.biptag.components.BottomBar
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.Alert
import br.com.biptag.model.Item
import br.com.biptag.repository.AlertRepository
import br.com.biptag.repository.ItemRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import androidx.compose.material.icons.outlined.Backpack

// Estrutura auxiliar para juntar o Alerta com os dados do Item na tela
data class MapAlertData(
    val alert: Alert,
    val item: Item?
)

@Composable
fun MapsScreen(navController: NavController, onItemClick: (Int) -> Unit = {}) {
    val paulistaPosition = LatLng(-23.5611, -46.6565)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(paulistaPosition, 15f)
    }

    val coroutineScope = rememberCoroutineScope()

    // Instanciando os Repositories (padrão da equipe)
    val alertRepository = remember { AlertRepository() }
    val itemRepository = remember { ItemRepository() }

    // Estados Locais da Tela
    var mapDataList by remember { mutableStateOf<List<MapAlertData>>(emptyList()) }
    var selectedAlert by remember { mutableStateOf<MapAlertData?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    // LaunchedEffect para buscar no Supabase apenas 1 vez ao abrir a tela
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val alerts = alertRepository.getActiveAlerts()
            val items = itemRepository.getAllItems()

            // Junta as informações do alerta com as do item (nome, categoria, etc)
            mapDataList = alerts.map { alert ->
                MapAlertData(
                    alert = alert,
                    item = items.find { it.id == alert.itemId }
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = { TopBar(title = "Mapa") },
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // MAPA
// MAPA
            // MAPA
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { selectedAlert = null },
                uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
            ) {
                // 1. PINOS FICTÍCIOS (ITENS PERDIDOS - VERMELHOS)
                val mockAlerts = listOf(
                    MapAlertData(
                        alert = Alert(id = 9991, itemId = 9991, type = "lost", lastSeenLat = -23.5611, lastSeenLng = -46.6565, status = "active"),
                        item = Item(id = 9991, name = "Bicicleta Caloi", description = "Quadro azul, aro 29, adesivo na traseira.", category = 1, status = "lost")
                    ),
                    MapAlertData(
                        alert = Alert(id = 9992, itemId = 9992, type = "lost", lastSeenLat = -23.5585, lastSeenLng = -46.6580, status = "active"),
                        item = Item(id = 9992, name = "Mochila Azul", description = "Mochila escolar azul escura.", category = 2, status = "lost")
                    )
                )

                // Desenha os pinos falsos primeiro
                mockAlerts.forEach { data ->
                    val position = LatLng(data.alert.lastSeenLat ?: 0.0, data.alert.lastSeenLng ?: 0.0)
                    Marker(
                        state = MarkerState(position = position),
                        title = data.item?.name ?: "Item reportado",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                        onClick = {
                            selectedAlert = data
                            true
                        }
                    )
                }

                // 2. PINOS REAIS DO SUPABASE (ITENS - VERMELHOS)
                mapDataList.forEach { data ->
                    val position = LatLng(data.alert.lastSeenLat ?: 0.0, data.alert.lastSeenLng ?: 0.0)
                    val iconColor = if (data.alert.type == "lost") BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_AZURE
                    Marker(
                        state = MarkerState(position = position),
                        title = data.item?.name ?: "Item reportado",
                        icon = BitmapDescriptorFactory.defaultMarker(iconColor),
                        onClick = {
                            selectedAlert = data
                            true
                        }
                    )
                }

                // 3. PINOS FICTÍCIOS (PESSOAS - AZUIS)
                val mockPeople = listOf(
                    LatLng(-23.5625, -46.6540) to "João Silva",
                    LatLng(-23.5595, -46.6530) to "Marina S."
                )

                mockPeople.forEach { (position, name) ->
                    Marker(
                        state = MarkerState(position = position),
                        title = name,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                        onClick = { true }
                    )
                }
            }

            // BARRA DE PESQUISA E LEGENDA (Mantido idêntico)
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Surface(shape = RoundedCornerShape(16.dp), shadowElevation = 4.dp, color = Color.Transparent) {
                    BipTagTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar local ou item", color = Color(0xFFB6B6B6), fontSize = 15.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFB6B6B6)) }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Surface(shape = RoundedCornerShape(12.dp), shadowElevation = 4.dp, color = MaterialTheme.colorScheme.surface) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF629EB0)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Pessoas próximas", style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFD32F2F)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Itens perdidos", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            // BOTÃO DE CENTRALIZAR
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(paulistaPosition, 15f))
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = if (selectedAlert != null) 210.dp else 24.dp),
                containerColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = null, tint = Color(0xFF233540))
            }

            // CARD DO ITEM PERDIDO (Agora com dados reais)
            selectedAlert?.let { data ->
                Card(
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                // Escolhe o ícone baseado na categoria do item (1 = Bike, 2 = Mochila)
                                val itemIcon = when (data.item?.category) {
                                    2 -> Icons.Outlined.Backpack
                                    else -> Icons.Outlined.PedalBike // Padrão será a bike se for 1 ou desconhecido
                                }

                                Icon(itemIcon, contentDescription = null)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(data.item?.name ?: "Item sem nome", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (data.alert.type == "lost") "Perdido" else "Roubado",
                                        color = Color(0xFFD32F2F),
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.background(Color(0xFFFFEBEE), RoundedCornerShape(6.dp)).padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PrimaryButton(
                            text = "Ver detalhes do alerta",
                            onClick = { data.alert.id?.let { onItemClick(it) } }, // Passamos o ID real do alerta
                            containerColor = Color(0xFF233540),
                            contentColor = Color.White
                        )
                    }
                }
            }
        }
    }
}