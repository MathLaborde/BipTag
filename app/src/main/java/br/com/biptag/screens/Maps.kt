package br.com.biptag.screens

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsBike
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.BipTagTextField
import br.com.biptag.components.BottomBar
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.navigation.Destination
import br.com.biptag.ui.theme.BipTagTheme

@Composable
fun MapsScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBar(
                title = "Mapa"
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Centralizar localização */ },
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.onBackground,
                shape = CircleShape,
                modifier = Modifier
                    .size(56.dp)
                    .offset(y = (-110).dp) // Ajuste para ficar acima do card
                    .border(1.dp, Color.Black.copy(alpha = 0.05f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Minha localização",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE9F0F2)) // Cor de fundo simulando o mapa
        ) {
            // Placeholder do Mapa (Simulação de ruas)
            MapPlaceholder()

            // Barra de Busca
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                BipTagTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Buscar local ou item",
                            color = Color(0xFF90A4AE),
                            fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Busca",
                            tint = Color(0xFF90A4AE)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Legenda do Mapa
                MapLegend()
            }

            // Marcadores de Exemplo
            MarkerItem(
                modifier = Modifier.align(Alignment.TopCenter).offset(y = 200.dp, x = (-40).dp),
                color = Color(0xFFE57373) // Vermelho (Item perdido)
            )
            MarkerItem(
                modifier = Modifier.align(Alignment.CenterEnd).offset(y = (-50).dp, x = (-60).dp),
                color = Color(0xFF64B5F6) // Azul (Pessoa próxima)
            )
            MarkerItem(
                modifier = Modifier.align(Alignment.BottomStart).offset(y = (-200.dp), x = 80.dp),
                color = Color(0xFF64B5F6) // Azul
            )
            MarkerItem(
                modifier = Modifier.align(Alignment.BottomEnd).offset(y = (-240.dp), x = (-80.dp)),
                color = Color(0xFFE57373) // Vermelho
            )

            // Card de Item Selecionado (Inferior)
            SelectedItemCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                onViewDetailClick = {
                    navController.navigate(Destination.LostItemScreen.route)
                }
            )
        }
    }
}

@Composable
fun MapPlaceholder() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Simulação de ruas (linhas brancas)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val strokeWidth = 15.dp.toPx()
                    // Vertical
                    drawLine(Color.White, Offset(size.width * 0.35f, 0f), Offset(size.width * 0.35f, size.height), strokeWidth)
                    drawLine(Color.White, Offset(size.width * 0.85f, 0f), Offset(size.width * 0.85f, size.height), strokeWidth)
                    // Horizontal
                    drawLine(Color.White, Offset(0f, size.height * 0.3f), Offset(size.width, size.height * 0.3f), strokeWidth)
                    drawLine(Color.White, Offset(0f, size.height * 0.6f), Offset(size.width, size.height * 0.6f), strokeWidth)
                    drawLine(Color.White, Offset(0f, size.height * 0.85f), Offset(size.width, size.height * 0.85f), strokeWidth)
                }
        )
        
        // Simulação de blocos (áreas coloridas)
        Box(
            modifier = Modifier
                .size(100.dp, 120.dp)
                .offset(x = 40.dp, y = 140.dp)
                .background(Color(0xFFDDE6E9), RoundedCornerShape(8.dp))
        )
        Box(
            modifier = Modifier
                .size(80.dp, 100.dp)
                .offset(x = 180.dp, y = 300.dp)
                .background(Color(0xFFDDE6E9), RoundedCornerShape(8.dp))
        )
        Box(
            modifier = Modifier
                .size(120.dp, 90.dp)
                .offset(x = 40.dp, y = 420.dp)
                .background(Color(0xFFE2EFED), RoundedCornerShape(8.dp)) // Área verde clara
        )
    }
}

@Composable
fun MapLegend() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.width(180.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LegendItem(color = Color(0xFF64B5F6), text = "Pessoas próximas")
            LegendItem(color = Color(0xFFE57373), text = "Itens perdidos")
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MarkerItem(modifier: Modifier = Modifier, color: Color) {
    Icon(
        imageVector = Icons.Outlined.LocationOn,
        contentDescription = "Marcador",
        tint = color,
        modifier = modifier.size(36.dp)
    )
}

@Composable
fun SelectedItemCard(
    modifier: Modifier = Modifier,
    onViewDetailClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Ícone do Item
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE9F0F2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.DirectionsBike,
                        contentDescription = "Bicicleta",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Bicicleta Caloi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFFEBEE)
                        ) {
                            Text(
                                text = "Perdida há 2h",
                                color = Color(0xFFE57373),
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Text(
                            text = " • a 300 m",
                            color = Color(0xFF90A4AE),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            PrimaryButton(
                text = "Ver item perdido",
                onClick = onViewDetailClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapsScreenPreview() {
    BipTagTheme {
        MapsScreen(navController = rememberNavController())
    }
}

@Composable
fun SelectedItemCardPreview() {
    BipTagTheme {
        SelectedItemCard(onViewDetailClick = {})
    }
}