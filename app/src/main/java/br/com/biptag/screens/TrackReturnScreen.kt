package br.com.biptag.screens

import androidx.compose.animation.core.copy
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.Alert
import br.com.biptag.model.PartnerPoint
import br.com.biptag.model.ReturnProcess
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AlertRepository
import br.com.biptag.repository.PartnerPointRepository
import br.com.biptag.repository.ReturnProcessRepository
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TrackReturnScreen(
    navController: NavController,
    returnProcessId: Int
) {
    val returnProcessRepository = remember { ReturnProcessRepository() }
    val partnerRepository = remember { PartnerPointRepository() }
    val alertRepository = remember { AlertRepository() }

    var returnProcess by remember { mutableStateOf<ReturnProcess?>(null) }
    var partnerPoint by remember { mutableStateOf<PartnerPoint?>(null) }
    var alert by remember { mutableStateOf<Alert?>(null) }

    var refreshing by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(returnProcessId, refreshing) {
        if (refreshing || returnProcess == null) {
            returnProcess = returnProcessRepository.getReturnProcessById(returnProcessId)
            partnerPoint = partnerRepository.getPartnerPointById(returnProcess?.partnerPointId)
            alert = alertRepository.getAlertById(returnProcess?.alertId ?: return@LaunchedEffect)
            refreshing = false
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = "Instruções",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
            PrimaryButton(
                text = "Confirmar entrega",
                modifier = Modifier.padding(16.dp),
                onClick = {
                    scope.launch {
                        withContext(Dispatchers.Main) {
                            navController.navigate(Destination.RatingScreen.createRoute(returnProcessId))
                        }
                    }
                },
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = refreshing,
            onRefresh = { refreshing = true },
            modifier = Modifier.padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(18.dp)
                                ),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (alert?.itemData?.image != null) {
                                    AsyncImage(
                                        model = alert?.itemData?.image,
                                        contentDescription = "Imagem de ${alert?.itemData?.name}",
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop,
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Sell,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                Spacer(Modifier.size(14.dp))

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = alert?.itemData?.name ?: "Item não encontrado.",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = alert?.itemData?.tagId ?: "Sem Tag.",
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        ReturnTimeLine(
                            status = returnProcess?.status ?: "pending",
                            pointName = partnerPoint?.name ?: "Ponto Parceiro"
                        )

                        // TODO deslisar para baixo para atualizar a tela.
                    }
        }
    }
}

@Composable
fun ReturnTimeLine(status: String, pointName: String) {
    val statusOrder = listOf("pending", "with_finder", "in_transit", "ready_for_pickup", "completed")
    val currentIndex = statusOrder.indexOf(status)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Status da devolução", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

            val steps = listOf(
                "Item com quem encontrou" to "Confirmado pelo localizador",
                "A caminho do ponto" to "Indo para $pointName",
                "Disponível para retirada" to "Pode buscar no local",
                "Entregue ao dono" to "Processo finalizado"
            )

            steps.forEachIndexed { index, step ->
                val stepIndex = index + 1

                TimelineItem(
                    title = step.first,
                    subtitle = step.second,
                    isDone = currentIndex > stepIndex,
                    isCurrent = currentIndex == stepIndex,
                    isLast = index == steps.lastIndex
                )
            }
        }
    }
}

@Composable
fun TimelineItem(
    title: String,
    subtitle: String,
    isDone: Boolean,
    isCurrent: Boolean,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (isDone) Color(0xFF2E7D32) else if (isCurrent) Color(
                            0xFF263238
                        ) else Color.Transparent
                    )
                    .border(
                        1.dp,
                        if (isDone || isCurrent) Color.Transparent else Color.LightGray,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) Icon(Icons.Default.Check, null, modifier = Modifier.size(14.dp), tint = Color.White)
                if (isCurrent) Box(Modifier
                    .size(6.dp)
                    .background(Color.White, CircleShape))
            }
            // Linha
            if (!isLast) {
                Box(Modifier
                    .width(2.dp)
                    .height(40.dp)
                    .background(
                        if (isDone) Color(0xFF2E7D32) else Color.LightGray.copy(
                            0.5f
                        )
                    ))
            }
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, color = if (isDone || isCurrent) Color.Unspecified else Color.Gray)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}