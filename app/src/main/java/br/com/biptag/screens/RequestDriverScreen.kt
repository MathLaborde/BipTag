package br.com.biptag.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.ReturnProcess
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AuthRepository
import br.com.biptag.repository.FoundReportRepository
import br.com.biptag.repository.ItemRepository
import br.com.biptag.repository.ReturnProcessRepository
import br.com.biptag.ui.theme.BipTagTheme
import br.com.biptag.ui.theme.SuccessGreen
import br.com.biptag.ui.theme.SuccessGreenBorder
import br.com.biptag.ui.theme.SuccessGreenDark
import br.com.biptag.ui.theme.SuccessGreenLight
import kotlinx.coroutines.launch

@Composable
fun RequestDriverScreen(
    navController: NavController,
    foundReportId: Int
) {
    val coroutineScope = rememberCoroutineScope()

    // Instanciando os Repositories diretamente na tela
    val foundReportRepo = remember { FoundReportRepository() }
    val itemRepo = remember { ItemRepository() }
    val returnProcessRepo = remember { ReturnProcessRepository() }
    val authRepo = remember { AuthRepository() }

    // Estados da Tela
    var selectedTime by remember { mutableStateOf("Agora") }
    var isLoading by remember { mutableStateOf(true) }

    // Dados reais que virão do banco
    var itemName by remember { mutableStateOf("") }
    var itemCode by remember { mutableStateOf("") }
    var itemAddress by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var alertId by remember { mutableStateOf(0) }

    // Dispara a busca no banco de dados assim que a tela abre
    // Dispara a busca no banco de dados assim que a tela abre
    LaunchedEffect(foundReportId) {
        isLoading = true
        try {
            // O foundReportId que chega na rota na verdade é o alertId vindo da tela anterior
            val realAlertId = foundReportId
            android.util.Log.d("RequestDriver", "Buscando pelo Alert ID: $realAlertId")

            // Agora usamos a função certa do repositório!
            val report = foundReportRepo.getFoundReportByAlertId(realAlertId)

            if (report != null) {
                android.util.Log.d("RequestDriver", "Report encontrado! itemId: ${report.itemId}")
                alertId = report.alertId
                itemAddress = report.foundAddress

                val item = itemRepo.getItemById(report.itemId)
                itemName = item?.name ?: "Item sem nome"
                itemCode = item?.tagId ?: "Sem código"

                val currentUser = authRepo.getCurrentUser()
                ownerName = currentUser?.name ?: "Usuário"
            } else {
                android.util.Log.e("RequestDriver", "Nenhum FoundReport para o Alert ID $realAlertId")
                itemName = "Erro: Objeto não encontrado"
                itemAddress = "Erro de endereço"
            }
        } catch (e: Exception) {
            android.util.Log.e("RequestDriver", "Erro ao buscar dados no Supabase", e)
            itemName = "Erro de conexão"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar(
                title = "Solicitar motorista parceiro",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(24.dp)) {
                PrimaryButton(
                    text = if (isLoading) "Aguarde..." else "Solicitar motorista parceiro",
                    icon = Icons.Outlined.DirectionsBike,
                    enabled = !isLoading,
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true

                            val newProcess = ReturnProcess(
                                alertId = alertId,
                                foundReportId = foundReportRepo.getFoundReportByAlertId(alertId)?.id ?: 0,
                                returnType = "home_delivery",
                                deliveryFee = 18.0,
                                status = "pending"
                            )

                            val created = returnProcessRepo.createReturnProcess(newProcess)

                            if (created != null && created.id != null) {
                                // Redireciona para a tela de acompanhamento se der certo
                                navController.navigate(Destination.TrackReturnScreen.createRoute(created.id!!)) {
                                    popUpTo(Destination.RequestDriverScreen.route) { inclusive = true }
                                }
                            }
                            isLoading = false
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        if (isLoading && itemName.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Um motorista parceiro busca o item com você e leva direto até a pessoa dona.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                ItemSummaryCard(itemName = itemName, itemCode = itemCode)
                Spacer(modifier = Modifier.height(16.dp))

                AddressCard(address = itemAddress, city = "São Paulo")
                Spacer(modifier = Modifier.height(16.dp))

                TimeSelectionCard(selectedTime = selectedTime, onTimeSelected = { selectedTime = it })
                Spacer(modifier = Modifier.height(16.dp))

                DetailsCard()
                Spacer(modifier = Modifier.height(16.dp))

                SuccessBanner(ownerName = ownerName)
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun ItemSummaryCard(itemName: String, itemCode: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.DirectionsBike,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = itemName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = itemCode,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Em devolução",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
fun AddressCard(address: String, city: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ENDEREÇO DE COLETA",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = address,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = city,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TimeSelectionCard(selectedTime: String, onTimeSelected: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "QUANDO O MOTORISTA DEVE PASSAR",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TimeChip("Agora", selectedTime, onTimeSelected)
                TimeChip("Hoje 14h–16h", selectedTime, onTimeSelected)
                TimeChip("Hoje 16h–18h", selectedTime, onTimeSelected)
            }
        }
    }
}

@Composable
fun TimeChip(label: String, selected: String, onSelect: (String) -> Unit) {
    val isSelected = label == selected
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelect(label) }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DetailsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DetailRow("Distância até o dono", "6,2 km", isBold = true)
            Spacer(modifier = Modifier.height(12.dp))
            DetailRow("Tempo estimado", "25 min", isBold = true)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Custo da corrida",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Grátis · plano Premium",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = SuccessGreen
                )
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SuccessBanner(ownerName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SuccessGreenLight)
            .border(1.dp, SuccessGreenBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = SuccessGreenDark,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "$ownerName informou que pode receber hoje entre 14h e 20h.",
                style = MaterialTheme.typography.bodySmall,
                color = SuccessGreenDark
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RequestDriverScreenPreview() {
    BipTagTheme {
        RequestDriverScreen(navController = rememberNavController(), foundReportId = 1)
    }
}