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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.StarBorder
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
import br.com.biptag.components.TopBar
import br.com.biptag.navigation.Destination

@Composable
fun OwnerReviewScreen(navController: NavController, alertId: Int) {
    var isMyItem by remember { mutableStateOf<Boolean?>(true) }
    var conditionGood by remember { mutableStateOf(true) }
    var accessoriesPresent by remember { mutableStateOf(true) }
    var rfidIntact by remember { mutableStateOf(false) }
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopBar(
                title = "Avaliar recebimento",
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
                Box(modifier = Modifier.fillMaxWidth().padding(20.dp, 16.dp)) {
                    Button(
                        onClick = {
                            // Conclui o fluxo e volta para a Home
                            navController.navigate(Destination.InitialScreen.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF263E4D))
                    ) {
                        Text("Enviar avaliação", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Header do Item Recebido
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp).background(Color(0xFFE2F4EB), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF16A34A))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Item recebido!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Text("Bicicleta Caloi · entregue às 15h36", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card: É o seu item?
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Este é realmente o seu item?", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SelectableButton(
                            text = "Sim, é o meu item",
                            isSelected = isMyItem == true,
                            onClick = { isMyItem = true },
                            modifier = Modifier.weight(1f)
                        )
                        SelectableButton(
                            text = "Não é o meu",
                            isSelected = isMyItem == false,
                            onClick = { isMyItem = false },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card: Estado do item
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Como o item chegou?", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomCheckbox("Em bom estado, sem danos", conditionGood) { conditionGood = it }
                    CustomCheckbox("Todos os acessórios presentes", accessoriesPresent) { accessoriesPresent = it }
                    CustomCheckbox("Etiqueta RFID intacta", rfidIntact) { rfidIntact = it }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card: Avaliação do Motorista
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Avalie o motorista parceiro", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = Icons.Outlined.StarBorder,
                                contentDescription = null,
                                tint = if (i <= rating) Color(0xFFFBBF24) else Color(0xFFCBD5E1),
                                modifier = Modifier.size(32.dp).clickable { rating = i }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        placeholder = { Text("Escreva um comentário (opcional)", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            focusedBorderColor = Color(0xFF263E4D)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SelectableButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val bgColor = if (isSelected) Color(0xFFE2F4EB) else Color.White
    val borderColor = if (isSelected) Color(0xFF16A34A) else Color(0xFFE5E7EB)
    val textColor = if (isSelected) Color(0xFF16A34A) else Color(0xFF6B7280)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isSelected && text.contains("Sim")) {
                Icon(Icons.Default.Check, contentDescription = null, tint = textColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textColor)
        }
    }
}

@Composable
fun CustomCheckbox(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!checked) }.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(if (checked) Color(0xFF10B981) else Color.White)
                .border(1.dp, if (checked) Color.Transparent else Color(0xFFCBD5E1), RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (checked) Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 14.sp, color = Color(0xFF475569))
    }
}