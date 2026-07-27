package br.com.biptag.screens


import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.Alert
import br.com.biptag.model.Item
import br.com.biptag.repository.AlertRepository
import br.com.biptag.repository.ItemRepository

@Composable
fun LostItemScreen(navController: NavController, alertId: Int) {
    // Instancia os repositórios
    val alertRepo = remember { AlertRepository() }
    val itemRepo = remember { ItemRepository() }

    // Estados para guardar as informações que vêm do banco
    var alert by remember { mutableStateOf<Alert?>(null) }
    var item by remember { mutableStateOf<Item?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Efeito colateral que roda ao abrir a tela: busca o alerta e depois o item
    LaunchedEffect(alertId) {
        isLoading = true
        alert = alertRepo.getAlertById(alertId)

        // Se achou o alerta, usa o itemId para buscar os dados do objeto (nome, foto, etc)
        alert?.let { currentAlert ->
            item = itemRepo.getItemById(currentAlert.itemId)
        }
        isLoading = false
    }

    Scaffold(
        topBar = { TopBar(title = "Detalhes do Alerta") }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE9F0F2)),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            } else if (alert == null || item == null) {
                Text(
                    text = "Não foi possível carregar as informações do item.",
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Ícone provisório (colocar o AsyncImage do Coil se forem usar a URL da foto do Item)
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            // Chamada do ícone corrigida aqui:
                            imageVector = Icons.Outlined.DirectionsBike,
                            contentDescription = "Foto do Item",
                            modifier = Modifier.size(60.dp),
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = item!!.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val statusColor = if (alert!!.type == "stolen") Color(0xFFE57373) else Color(0xFFFBC02D)
                            val statusText = if (alert!!.type == "stolen") "Roubado" else "Perdido"

                            Text(
                                text = "Status: $statusText",
                                color = statusColor,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Descrição:",
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = item!!.description.ifEmpty { "Nenhuma descrição informada." },
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    PrimaryButton(
                        text = "Reportar como Encontrado",
                        onClick = {
                            navController.navigate("confirmation_screen/$alertId")
                        }
                    )
                }
            }
        }
    }
}
