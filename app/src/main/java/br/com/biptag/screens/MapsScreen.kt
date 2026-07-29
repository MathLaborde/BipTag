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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState

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

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry) {
        isLoading = true
        try {
            val alerts = alertRepository.getActiveAlerts()
            val items = itemRepository.getAllItems()

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
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { selectedAlert = null },
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false
                )
            ) {
                // 2. PINOS REAIS DO SUPABASE (ITENS - VERMELHOS)
                mapDataList.forEach { data ->
                    val position = LatLng(data.alert.lastSeenLat ?: 0.0, data.alert.lastSeenLng ?: 0.0)
                    val isItem = data.item != null
                    val iconHue = if (isItem) {
                        BitmapDescriptorFactory.HUE_RED
                    } else {
                        BitmapDescriptorFactory.HUE_AZURE
                    }

                    Marker(
                        state = MarkerState(position = position),
                        title = data.item?.name ?: "Pessoa",
                        icon = BitmapDescriptorFactory.defaultMarker(iconHue),
                        onClick = {
                            selectedAlert = data
                            true
                        }
                    )
                }
            }

            // BARRA DE PESQUISA E LEGENDA (Mantido idêntico)
            SearchAndLegendOverlay(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )

            // BOTÃO DE CENTRALIZAR
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(
                                paulistaPosition,
                                15f
                            )
                        )
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
                AlertDetailsCard(
                    data = data,
                    onItemClick = onItemClick,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
fun SearchAndLegendOverlay(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 4.dp,
            color = Color.Transparent
        ) {
            BipTagTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        "Buscar local ou item",
                        color = Color(0xFFB6B6B6),
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFFB6B6B6)
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 4.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF629EB0))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pessoas próximas", style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD32F2F))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Itens perdidos", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
@Composable
fun AlertDetailsCard(
    data: MapAlertData,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    val itemIcon = when (data.item?.category) {
                        2 -> Icons.Outlined.Backpack
                        else -> Icons.Outlined.PedalBike
                    }
                    Icon(itemIcon, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        data.item?.name ?: "Item sem nome",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (data.alert.type == "lost") "Perdido" else "Roubado",
                            color = Color(0xFFD32F2F),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier
                                .background(
                                    Color(0xFFFFEBEE),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = "Ver item perdido",
                onClick = { data.alert.id?.let { onItemClick(it) } },
                containerColor = Color(0xFF233540),
                contentColor = Color.White
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MapsScreenPreview() {
    val navController = rememberNavController()

    MapsScreen(
        navController = navController,
        onItemClick = {}
    )
}