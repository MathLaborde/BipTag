package br.com.biptag.screens
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.ui.theme.BipTagTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import br.com.biptag.model.PartnerPoint
import br.com.biptag.model.ReturnProcess
import br.com.biptag.repository.PartnerPointRepository
import br.com.biptag.repository.ReturnProcessRepository

@Composable
fun CollectionPointsScreen(
    navController: NavController,
    alertId: Int
) {
    val coroutineScope = rememberCoroutineScope()
    val partnerRepository = remember { PartnerPointRepository() }
    val returnRepository = remember { ReturnProcessRepository() }

    var partnerPoints by remember { mutableStateOf<List<PartnerPoint>>(emptyList()) }
    var selectedPointId by remember { mutableIntStateOf(0) }
    var isSubmitting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        partnerPoints = partnerRepository.getAllPartnerPoints()

        if (partnerPoints.isNotEmpty()) {
            selectedPointId = partnerPoints.first().id ?: 0
        }
    }

    CollectionPointsContent(
        partnerPoints = partnerPoints,
        selectedPointId = selectedPointId,
        isSubmitting = isSubmitting,
        onPointSelected = { id -> selectedPointId = id },
        onBackClick = { navController.popBackStack() },
        onConfirmClick = {
            if (selectedPointId != 0) {
                isSubmitting = true
                coroutineScope.launch {
                    val process = ReturnProcess(
                        alertId = alertId,
                        returnType = "partner_point",
                        partnerPointId = selectedPointId
                    )
                    val result = returnRepository.createReturnProcess(process)
                    isSubmitting = false
                    if (result != null) {
                        navController.popBackStack()
                    }
                }
            }
        }
    )
}

@Composable
fun CollectionPointsContent(
    partnerPoints: List<PartnerPoint>,
    selectedPointId: Int,
    isSubmitting: Boolean,
    onPointSelected: (Int) -> Unit,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit
) {
    val initialPos = if (partnerPoints.isNotEmpty()) {
        LatLng(partnerPoints[0].latitude, partnerPoints[0].longitude)
    } else {
        LatLng(-23.5505, -46.6333)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPos, 14f)
    }

    LaunchedEffect(partnerPoints) {
        if (partnerPoints.isNotEmpty()) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(partnerPoints[0].latitude, partnerPoints[0].longitude), 14f
            )
        }
    }

    LaunchedEffect(selectedPointId) {
        if (selectedPointId > 0) {
            val partner = partnerPoints.first { partnerPoint -> partnerPoint.id == selectedPointId }

            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(partner.latitude, partner.longitude), 14f
            )
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Pontos Parceiros",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = onBackClick
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(16.dp)) {
                PrimaryButton(
                    text = if (isSubmitting) "Confirmando..." else "Confirmar ponto",
                    enabled = selectedPointId != 0 && !isSubmitting,
                    onClick = onConfirmClick
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Escolha um ponto de coleta para retirar o item.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(zoomControlsEnabled = false, scrollGesturesEnabled = false)
                ) {
                    partnerPoints.forEach { point ->
                        Marker(
                            state = MarkerState(position = LatLng(point.latitude, point.longitude)),
                            title = point.name
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (partnerPoints.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(partnerPoints) { point ->
                        PartnerPointCard(
                            point = point,
                            isSelected = selectedPointId == point.id,
                            onClick = { onPointSelected(point.id ?: 0) }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun PartnerPointCard(
    point: PartnerPoint,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(borderWidth, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Storefront,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = point.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = point.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = point.workingHours,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outlineVariant,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CollectionPointsScreenPreview() {
    BipTagTheme {
        CollectionPointsContent(
            partnerPoints = listOf(
                PartnerPoint(
                    id = 1,
                    name = "FIAP Paulista",
                    address = "Av. Paulista, 1106",
                    latitude = -23.5645,
                    longitude = -46.6525,
                    workingHours = "Seg a Sex, 8h às 22h"
                ),
                PartnerPoint(
                    id = 2,
                    name = "Laboratório 201",
                    address = "Prédio Principal, 2º Andar",
                    latitude = -23.5650,
                    longitude = -46.6530,
                    workingHours = "Seg a Sex, 9h às 18h"
                )
            ),
            selectedPointId = 1,
            isSubmitting = false,
            onPointSelected = {},
            onBackClick = {},
            onConfirmClick = {}
        )
    }
}