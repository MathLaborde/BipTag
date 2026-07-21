package br.com.biptag.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.ui.theme.BipTagTheme

@Composable
fun AlertIssuedScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Column {
                TopBar(
                    title = "Alerta emitido",
                    startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                    onClick = { navController.popBackStack() }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.background,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    PrimaryButton(
                        text = "Ver no mapa",
                        onClick = { /* Navegar para o mapa */ }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            // Exemplo: Voltar para a Home limpando a pilha de telas
                            navController.popBackStack(navController.graph.startDestinationId, false)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = "Voltar ao início",
                            color = MaterialTheme.colorScheme.primary, // Azul escuro do tema
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFFF7F9FA) // Fundo cinza bem claro para destacar os cards brancos
    ) { paddingValues ->
        ContentAlertIssuedScreen(
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun ContentAlertIssuedScreen(modifier: Modifier) {
    val scrollState = rememberScrollState()

    // Cores específicas da identidade visual da tela de sucesso
    val successGreen = Color(0xFF1FA363)
    val lightGreenBg = Color(0xFFE8F6F0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Ícone de Alerta Central
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(lightGreenBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Sensors, // O ícone que mais se aproxima do sinal do Figma
                contentDescription = "Alerta de sinal",
                tint = successGreen,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Títulos
        Text(
            text = "Alerta emitido!",
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Avisamos os usuários próximos sobre o seu item perdido.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Row de Estatísticas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                value = "128",
                label = "Notificados"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                value = "5 km",
                label = "Raio"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                value = "Ativo",
                label = "Status",
                valueColor = successGreen
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card da Timeline (O que acontece agora)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "O que acontece agora",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Passos da Timeline
                TimelineStep(
                    isActive = true,
                    title = "Monitorando a região",
                    description = "Buscando seu item entre usuários próximos",
                    activeColor = successGreen,
                    activeBgColor = lightGreenBg
                )

                TimelineStep(
                    isActive = false,
                    title = "Alguém encontra e avisa você",
                    description = "Você recebe uma notificação na hora"
                )

                TimelineStep(
                    isActive = false,
                    title = "Você escolhe a devolução",
                    description = "Ponto parceiro ou motoboy em casa"
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    valueColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Card(
        modifier = modifier.height(76.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = valueColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun TimelineStep(
    isActive: Boolean,
    title: String,
    description: String,
    activeColor: Color = Color.Unspecified,
    activeBgColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Container padronizado para o ícone (tamanho maior para dar o respiro do Figma)
        Box(
            modifier = Modifier
                .size(32.dp) // Aumentamos para 32dp para caber o fundo
                .clip(CircleShape)
                .background(
                    if (isActive) activeBgColor else Color(0xFFEFF3F5) // Fundo verdinho ou cinza clarinho
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isActive) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Passo ativo",
                    tint = activeColor,
                    modifier = Modifier.size(18.dp) // Ícone ligeiramente menor que o fundo
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = "Passo pendente",
                    tint = Color(0xFF90A4AE), // Cinza azulado para combinar com a borda do figma
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                // O Figma mantém o título escuro mesmo nos inativos
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AlertIssuedScreenPreview() {
    BipTagTheme {
        AlertIssuedScreen(navController = rememberNavController())
    }
}