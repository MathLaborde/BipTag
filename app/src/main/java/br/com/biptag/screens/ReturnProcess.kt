package br.com.biptag.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Storefront
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import br.com.biptag.navigation.Destination
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.ui.theme.BipTagTheme

// 1. TELA INTELIGENTE (Pensa, tem o navController e o itemId)
@Composable
fun ReturnProcessScreen(
    navController: NavController,
    foundReportId: Int
) {
    var selectedOption by remember { mutableIntStateOf(1) }

    ReturnProcessContent(
        selectedOption = selectedOption,
        onOptionSelected = { selectedOption = it },
        onBackClick = { navController.popBackStack() },
        onContinueClick = {
            if (selectedOption == 1) {
                navController.navigate(Destination.PartnerPointsScreen.createRoute(foundReportId))
            } else {
                // Fluxo de entregador (Receber em casa)
            }
        }
    )
}

// 2. TELA VISUAL (Só desenha, não sabe navegar sozinha)
@Composable
fun ReturnProcessContent(
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Forma de devolução",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = onBackClick
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(16.dp)) {
                PrimaryButton(
                    text = "Continuar",
                    onClick = onContinueClick
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Como você quer receber o item de volta?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))

            ReturnOptionCard(
                title = "Retirar em ponto parceiro",
                description = "Você busca o item em um ponto de coleta próximo.",
                tagText = "Grátis",
                tagColor = MaterialTheme.colorScheme.tertiaryContainer,
                tagTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
                icon = {
                    Icon(
                        Icons.Outlined.Storefront,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                isSelected = selectedOption == 1,
                onClick = { onOptionSelected(1) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ReturnOptionCard(
                title = "Receber em casa",
                description = "Um entregador busca com quem encontrou e entrega na sua porta.",
                tagText = "Com taxa - R$ 18",
                tagColor = MaterialTheme.colorScheme.surfaceVariant,
                tagTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                icon = {
                    Icon(
                        Icons.Outlined.Home,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                isSelected = selectedOption == 2,
                onClick = { onOptionSelected(2) }
            )
        }
    }
}

@Composable
fun ReturnOptionCard(
    title: String,
    description: String,
    tagText: String,
    tagColor: Color,
    tagTextColor: Color,
    icon: @Composable () -> Unit,
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
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(tagColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = tagText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = tagTextColor
                    )
                }
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
fun ReturnProcessScreenPreview() {
    BipTagTheme {
        ReturnProcessContent(
            selectedOption = 1,
            onOptionSelected = {},
            onBackClick = {},
            onContinueClick = {}
        )
    }
}