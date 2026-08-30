package br.com.biptag.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.FoundReport
import br.com.biptag.model.Alert
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AlertRepository
import br.com.biptag.repository.FoundReportRepository
import br.com.biptag.ui.theme.BipTagTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import formatDateAgo
import formatToBRDateTime
import kotlinx.coroutines.launch

@Composable
fun ItemFoundScreen(
    navController: NavController,
    foundReportId: Int,
) {
    val scope = rememberCoroutineScope()
    val foundReportRepository = remember { FoundReportRepository() }
    val alertRepository = remember { AlertRepository() }

    var reportData by remember { mutableStateOf<FoundReport?>(null) }
    var alertData by remember { mutableStateOf<Alert?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(foundReportId) {
        try {
            isLoading = true
            errorMessage = null

            alertData = alertRepository.getAlertById(foundReportId)

            reportData = foundReportRepository.getFoundReportByAlertId(foundReportId)

            Log.d("ItemFoundScreen", "alertData: $alertData")
            Log.d("ItemFoundScreen", "reportData: $reportData")
        } catch (e: Exception) {
            Log.e("ItemFoundScreen", "Erro ao carregar dados", e)
            errorMessage = "Erro ao carregar informações. Tente novamente."
        } finally {
            isLoading = false
        }
    }

    val itemName = alertData?.itemData?.name ?: "item"
    val finderName = if (reportData?.isAnonymous == true) {
        "Anônimo"
    } else {
        // TODO: Buscar nome do usuário pelo ID (reportData?.userId)
        "Usuário"
    }

    val location = reportData?.let { report ->
        if (report.foundLat != 0.0 && report.foundLng != 0.0) {
            LatLng(report.foundLat, report.foundLng)
        } else null
    }

    val foundDateTime = reportData?.foundDate?.let { dateStr ->
        formatToBRDateTime(dateStr)
    } ?: "Data não informada"

    val foundDateAgo = reportData?.foundDate?.let { dateStr ->
        formatDateAgo(dateStr)
    } ?: "Data não informada"

    val address = reportData?.foundAddress ?: "Local não informado"

    val cameraPositionState = rememberCameraPositionState {
        position = location?.let { CameraPosition.fromLatLngZoom(it, 15f) }
            ?: CameraPosition.fromLatLngZoom(LatLng(-23.5505, -46.6333), 10f)
    }

    LaunchedEffect(location) {
        location?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Item encontrado",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            if (!isLoading && errorMessage == null) {
                PrimaryButton(
                    modifier = Modifier.padding(16.dp),
                    text = "Escolher forma de devolução",
                    onClick = {
                        navController.navigate(Destination.ReturnProcessScreen.createRoute(foundReportId))
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
                errorMessage != null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        PrimaryButton(
                            text = "Tentar novamente",
                            onClick = {
                                scope.launch {
                                    isLoading = true
                                    errorMessage = null
                                    try {
                                        alertData = alertRepository.getAlertById(foundReportId)
                                        reportData = foundReportRepository.getFoundReportByAlertId(foundReportId)
                                    } catch (e: Exception) {
                                        errorMessage = "Erro ao carregar informações. Tente novamente."
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color(0xFFE8F5E9), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = "Sucesso",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(55.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Boas notícias!",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = "Alguém está com a sua $itemName.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                        )

                        OutlinedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(MaterialTheme.colorScheme.secondary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Outlined.Person,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = finderName,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = foundDateAgo,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.outlineVariant
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Onde",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outlineVariant
                                        )
                                        Text(
                                            text = address,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = MaterialTheme.colorScheme.outline
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Outlined.AccessTime,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.outlineVariant
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Quando",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outlineVariant
                                        )
                                        Text(
                                            text = foundDateTime,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (location != null) {
                            GoogleMap(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                                cameraPositionState = cameraPositionState,
                                uiSettings = MapUiSettings(
                                    zoomControlsEnabled = false,
                                    scrollGesturesEnabled = false
                                )
                            ) {
                                Marker(
                                    state = MarkerState(position = location),
                                    title = "Local encontrado"
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Localização não disponível",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ItemFoundScreenPreview() {
    BipTagTheme {
        ItemFoundScreen(
            navController = rememberNavController(),
            foundReportId = 1
        )
    }
}