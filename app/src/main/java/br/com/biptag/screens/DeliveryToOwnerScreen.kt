package br.com.biptag.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.biptag.components.TopBar
import br.com.biptag.model.ReturnProcess
import br.com.biptag.network.RetrofitClient
import br.com.biptag.R
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import io.github.jan.supabase.auth.auth

@Composable
fun DeliveryToOwnerScreen(
    navController: NavController,
    returnProcessId: Int
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var returnProcess by remember { mutableStateOf<ReturnProcess?>(null) }
    var alertData by remember { mutableStateOf<br.com.biptag.model.Alert?>(null) }

    // Dispara a busca na API via Retrofit assim que a tela abre
    LaunchedEffect(returnProcessId) {
        isLoading = true
        try {
            val accessToken = br.com.biptag.network.SupabaseClient.client.auth.currentAccessTokenOrNull()
            val token = "Bearer $accessToken"

            // 1ª Chamada: Pega o pacote (Response) do Processo de Devolução
            val processResponse = RetrofitClient.returnProcessService.getReturnProcessById(token, returnProcessId)

            // Verifica se a chamada deu certo (código 200)
            if (processResponse.isSuccessful) {
                // "Abre" o pacote usando o .body()
                val processBody = processResponse.body()
                returnProcess = processBody

                // 2ª Chamada: Usa o alertId para puxar os dados do Dono/Item, de forma segura
                processBody?.alertId?.let { idDoAlerta ->
                    // OBS: Ajuste "getAlertById" para o nome exato da função que estiver na sua AlertService
                    val alertResponse = RetrofitClient.alertApiService.getAlertById(token, idDoAlerta)
                    val alert = RetrofitClient.alertApiService.getAlertById(token, idDoAlerta)
                    alertData = alert
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("DeliveryToOwner", "Erro ao buscar dados", e)
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopBar(
                title = "A caminho do dono",
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = { },
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Color(0xFFEEF2F6),
                            disabledContentColor = Color(0xFF94A3B8)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Aguardando confirmação do dono",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // MAPA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    val currentPosition = LatLng(-23.5611, -46.6565)
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(currentPosition, 14f)
                    }
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = false,
                            myLocationButtonEnabled = false,
                            scrollGesturesEnabled = false,
                            tiltGesturesEnabled = false
                        )
                    ) {
                        // Trocado para o ícone da Moto!
                        Marker(
                            state = MarkerState(position = currentPosition),
                            title = "Motorista a caminho",
                            icon = resizeMapIcon(context, R.drawable.ic_moto_marker, 34, 34)
                        )
                    }
                }

                // CONTEÚDO
                Column(modifier = Modifier.padding(20.dp)) {
                    // Header (Item Coletado + Tempo)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Item coletado",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF16A34A)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Entrega em 18 min",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Card da Timeline
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Status da corrida",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            TimelineStep(
                                state = StepState.COMPLETED,
                                title = "Motorista a caminho da coleta",
                                subtitle = "Rafael S. saiu às 14h02",
                                isLast = false
                            )
                            TimelineStep(
                                state = StepState.COMPLETED,
                                title = "Item coletado",
                                subtitle = "Código validado com sucesso",
                                isLast = false
                            )
                            TimelineStep(
                                state = StepState.CURRENT,
                                title = "A caminho do dono",
                                subtitle = "2,8 km restantes · atualizado agora",
                                isLast = false
                            )
                            TimelineStep(
                                state = StepState.FUTURE,
                                title = "Entregue e confirmado",
                                subtitle = "Aguardando validação do recebimento",
                                isLast = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Card do Dono (AGORA DINÂMICO)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFEAF2F6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF263E4D)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                // AQUI VOCÊ AJUSTA A VARIÁVEL QUE VEM DA SUA API (ex: returnProcess?.user?.name)
                                Text(
                                    text = "${returnProcess?.status ?: "Nome do Dono"} · dono do item",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Endereço em processamento", // Ajuste para puxar o endereço real depois
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF94A3B8)
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

// --- COMPONENTES AUXILIARES ---

enum class StepState { COMPLETED, CURRENT, FUTURE }

@Composable
fun TimelineStep(state: StepState, title: String, subtitle: String, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            when (state) {
                StepState.COMPLETED -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFF10B981), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
                StepState.CURRENT -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(3.dp, Color(0xFF263E4D), CircleShape)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(modifier = Modifier.size(10.dp).background(Color(0xFF263E4D), CircleShape))
                    }
                }
                StepState.FUTURE -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(2.dp, Color(0xFFCBD5E1), CircleShape)
                            .background(Color.White, CircleShape)
                    )
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(if (state == StepState.COMPLETED) Color(0xFF10B981) else Color(0xFFE2E8F0))
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (state == StepState.FUTURE) Color(0xFF94A3B8) else Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = if (state == StepState.FUTURE) Color(0xFFCBD5E1) else Color(0xFF94A3B8)
            )
        }
    }
}

// Função para redimensionar o ícone do mapa
private fun resizeMapIcon(
    context: android.content.Context,
    resId: Int,
    widthDp: Int,
    heightDp: Int
): com.google.android.gms.maps.model.BitmapDescriptor? {
    val drawable = androidx.core.content.ContextCompat.getDrawable(context, resId) ?: return null
    val density = context.resources.displayMetrics.density
    val widthPx = (widthDp * density).toInt()
    val heightPx = (heightDp * density).toInt()

    val bitmap = android.graphics.Bitmap.createBitmap(widthPx, heightPx, android.graphics.Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(bitmap)
}