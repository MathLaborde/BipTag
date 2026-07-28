package br.com.biptag.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PedalBike
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

// 1. Criamos um Enum para diferenciar se é Pessoa ou Item Perdido
enum class MarkerType { PERSON, LOST_ITEM }

data class ItemMock(
    val id: Int,
    val title: String,
    val timeLost: String,
    val distance: String,
    val location: LatLng,
    val type: MarkerType
)

@Composable
fun MapsScreen(navController: NavController, onItemClick: (Int) -> Unit = {}) {
    val paulistaPosition = LatLng(-23.5611, -46.6565)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(paulistaPosition, 15f)
    }

    // Escopo de corrotina necessário para animar o mapa no clique do botão
    val coroutineScope = rememberCoroutineScope()

    // 2. Lista de itens espalhados pelo mapa
    val mockItems = listOf(
        ItemMock(1, "Bicicleta Caloi", "Perdida há 2h", "a 300 m", paulistaPosition, MarkerType.LOST_ITEM),
        ItemMock(2, "Mochila Azul", "Perdida há 5h", "a 800 m", LatLng(-23.5585, -46.6580), MarkerType.LOST_ITEM),
        ItemMock(3, "João Silva", "Visto agora", "a 150 m", LatLng(-23.5625, -46.6540), MarkerType.PERSON),
        ItemMock(4, "Marina S.", "Visto agora", "a 500 m", LatLng(-23.5595, -46.6530), MarkerType.PERSON)
    )

    var selectedItem by remember { mutableStateOf<ItemMock?>(null) }
    var searchQuery by remember { mutableStateOf("") }

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
                onMapClick = { selectedItem = null },
                uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
            ) {
                // 3. Desenhando os múltiplos pinos
                mockItems.forEach { item ->
                    // Define a cor baseada no tipo
                    val iconColor = if (item.type == MarkerType.LOST_ITEM) {
                        BitmapDescriptorFactory.HUE_RED
                    } else {
                        BitmapDescriptorFactory.HUE_AZURE // Azul claro
                    }

                    Marker(
                        state = MarkerState(position = item.location),
                        title = item.title,
                        icon = BitmapDescriptorFactory.defaultMarker(iconColor),
                        onClick = {
                            // Para manter o foco no seu design, abrimos o card apenas para itens perdidos
                            if (item.type == MarkerType.LOST_ITEM) {
                                selectedItem = item
                            }
                            true
                        }
                    )
                }
            }

            // BARRA DE PESQUISA E LEGENDA
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
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(text = "Buscar local ou item", color = Color(0xFFB6B6B6), fontSize = 15.sp)
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Ícone de Busca", tint = Color(0xFFB6B6B6))
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
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF629EB0)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Pessoas próximas", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFD32F2F)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Itens perdidos", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }

            // 4. BOTÃO DE CENTRALIZAR (ALVO)
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        // Anima a câmera de volta para a posição inicial (Paulista) com zoom 15
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(paulistaPosition, 15f)
                        )
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    // Se o card estiver aberto, o botão sobe para não ficar escondido atrás dele
                    .padding(end = 16.dp, bottom = if (selectedItem != null) 210.dp else 24.dp),
                containerColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Centralizar mapa",
                    tint = Color(0xFF233540)
                )
            }

            // CARD DO ITEM PERDIDO
            selectedItem?.let { item ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
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
                                Icon(Icons.Outlined.PedalBike, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item.timeLost,
                                        color = Color(0xFFD32F2F),
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier
                                            .background(Color(0xFFFFEBEE), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(item.distance, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        PrimaryButton(
                            text = "Ver item perdido",
                            onClick = { onItemClick(item.id) },
                            containerColor = Color(0xFF233540),
                            contentColor = Color.White
                        )
                    }
                }
            }
        }
    }
}