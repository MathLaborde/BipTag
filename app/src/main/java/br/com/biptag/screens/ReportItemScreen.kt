package br.com.biptag.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.biptag.components.BipTagTextField
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.ui.theme.BipTagTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportItemScreen(
    onBackClick: () -> Unit = {}, onSubmitClick: () -> Unit = {}
) {
    var reportType by remember { mutableStateOf("Roubado") }
    var location by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBar(
                title = "Reportar item",
                startIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onClick = onBackClick
            )
        }, bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                HorizontalDivider(
                    thickness = 2.dp, color = MaterialTheme.colorScheme.surfaceVariant
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                ) {
                    PrimaryButton(
                        text = "Emitir alerta", onClick = onSubmitClick
                    )
                }
            }
        }, containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                val isRoubado = reportType == "Roubado"

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isRoubado) MaterialTheme.colorScheme.surface else Color.Transparent)
                        .clickable { reportType = "Roubado" }, contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Roubado",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isRoubado) FontWeight.Bold else FontWeight.Medium,
                        color = if (isRoubado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (!isRoubado) MaterialTheme.colorScheme.surface else Color.Transparent)
                        .clickable { reportType = "Perdido" }, contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Perdido",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (!isRoubado) FontWeight.Bold else FontWeight.Medium,
                        color = if (!isRoubado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            ReportCustomTextField(
                label = "Onde foi visto pela última vez",
                value = location,
                onValueChange = { location = it },
                placeholder = "Av. Paulista, 1000 - São Paulo",
                icon = Icons.Outlined.LocationOn
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Pino do mapa",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            ReportCustomTextField(
                label = "Quando aconteceu",
                value = time,
                onValueChange = { time = it },
                placeholder = "Hoje, 14:30",
                icon = Icons.Outlined.Schedule
            )

            Spacer(modifier = Modifier.height(18.dp))

            ReportCustomTextField(
                label = "Descrição",
                value = description,
                onValueChange = { description = it },
                placeholder = "Cor, marca, sinais e detalhes do item...",
                icon = Icons.AutoMirrored.Outlined.Notes,
                singleLine = false,
                modifier = Modifier.height(100.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFFFF8E1),
                border = BorderStroke(
                    1.dp, Color(0xFFE8C86A)
                )
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 16.dp, vertical = 14.dp
                    ), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = null,
                        tint = Color(0xFF9C7A00)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Informe dados verdadeiros. Alertas falsos podem gerar penalidades.",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.W500,
                        color = Color(0xFF9A7B12)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ReportCustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        BipTagTextField(
            value = value, onValueChange = onValueChange, placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }, leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = if (!singleLine) Modifier.padding(bottom = 45.dp) else Modifier
                )
            }, singleLine = singleLine, modifier = modifier
        )
    }
}

@Preview
@Composable
private fun ReportItemScreenPreview() {
    BipTagTheme {
        ReportItemScreen()
    }
}