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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.Alert
import br.com.biptag.model.Category
import br.com.biptag.model.Item
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AlertRepository
import br.com.biptag.repository.AuthRepository
import br.com.biptag.repository.ItemRepository
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

//(Responsável por Navegação e Banco de Dados)
@Composable
fun LostItemScreen(navController: NavController, alertId: Int) {
    val alertRepository = remember { AlertRepository() }
    val itemRepository = remember { ItemRepository() }

    var alert by remember { mutableStateOf<Alert?>(null) }
    var item by remember { mutableStateOf<Item?>(null) }
    var ownerName by remember { mutableStateOf("Buscando...") }
    var isLoading by remember { mutableStateOf(true) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-23.5611, -46.6565), 15f)
    }

    LaunchedEffect(alertId) {
        isLoading = true
        try {
            val fetchedAlert = alertRepository.getAlertById(alertId)
            alert = fetchedAlert

            fetchedAlert?.let {
                val position = LatLng(it.lastSeenLat ?: 0.0, it.lastSeenLng ?: 0.0)
                cameraPositionState.position = CameraPosition.fromLatLngZoom(position, 15f)

                val fetchedItem = itemRepository.getItemById(it.itemId)
                item = fetchedItem

                ownerName = "João da Silva"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    LostItemContent(
        alert = alert,
        item = item,
        ownerName = ownerName,
        isLoading = isLoading,
        cameraPositionState = cameraPositionState,
        onBackClick = { navController.popBackStack() },
        onConfirmClick = {
            navController.navigate(
                Destination.ConfirmationScreen.route.replace(
                    "{alertId}", alertId.toString()
                )
            )
        })
}

//(Responsável apenas por desenhar a tela)
@Composable
fun LostItemContent(
    alert: Alert?,
    item: Item?,
    ownerName: String,
    isLoading: Boolean,
    cameraPositionState: CameraPositionState,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    Scaffold(topBar = {
        TopBar(
            title = "Detalhes do Alerta",
            startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
            onClick = onBackClick
        )
    }, bottomBar = {
        Surface(color = MaterialTheme.colorScheme.background, shadowElevation = 8.dp) {
            Box(modifier = Modifier.padding(16.dp)) {
                PrimaryButton(
                    text = "Estou com este item",
                    onClick = onConfirmClick,
                    containerColor = Color(0xFF233540),
                    contentColor = Color.White
                )
            }
        }
    }) { paddingValues ->
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEEF5F8))
                ) {
                    if (!item?.image.isNullOrEmpty()) {
                        AsyncImage(
                            model = item?.image,
                            contentDescription = "Foto de ${item?.name}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
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
                                Icon(
                                    Icons.Outlined.Image,
                                    contentDescription = "Foto do item",
                                    tint = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Sem foto", color = Color.Gray, fontSize = 14.sp)
                        }
                    }

                    Text(
                        text = if (alert?.type == "lost") "Perdido" else "Roubado",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(Color(0xFFD32F2F), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Column {
                    Text(
                        text = item?.name ?: "Item Desconhecido",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B2A36)
                    )

                    val categoryText = item?.categoryData?.name ?: "Categoria desconhecida"

                    Text(
                        text = categoryText,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFEEF5F8), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = null,
                                tint = Color(0xFF233540)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column() {
                            Text(
                                text = ownerName,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Informação protegida",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }

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
                                zoomControlsEnabled = false
                            )
                        ) {
                            alert?.let {
                                val position = LatLng(it.lastSeenLat ?: 0.0, it.lastSeenLng ?: 0.0)
                                Marker(state = MarkerState(position = position))
                            }
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {
                        Text(
                            "Descrição",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        Text(
                            text = item?.description
                                ?: "O dono não forneceu detalhes adicionais sobre este item.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Shield,
                            contentDescription = null,
                            tint = Color(0xFF629EB0)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "A entrega é combinada por ponto parceiro ou motoboy. Não compartilhe dados pessoais.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}