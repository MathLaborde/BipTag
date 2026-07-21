package br.com.biptag.screens

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.ItemRepository
import br.com.biptag.ui.theme.BipTagTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun BindTagScreen(navController: NavController, itemId: Int) {

    val scope = rememberCoroutineScope()
    val repository = remember { ItemRepository() }

    Scaffold(
        topBar = {
            TopBar(
                title = "Vincular etiqueta",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = { navController.popBackStack() }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Passo 2 de 2",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSecondary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Nfc,
                        contentDescription = "Ícone RFID",
                        modifier = Modifier.size(54.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Aproxime seu celular da etiqueta RFID",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mantenha o aparelho próximo à etiqueta até ouvir o sinal de confirmação.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Aguardando leitura...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = "Confirmar vínculo",
                onClick = {
                    scope.launch {
                        try {
                            val itemAtual = repository.getItemById(itemId)

                            if (itemAtual != null) {
                                val itemAtualizado = itemAtual.copy(tagId = "Tag #A4B2-99F0")
                                repository.updateItem(itemAtualizado)
                            }

                            withContext(Dispatchers.Main) {
                                navController.previousBackStackEntry?.savedStateHandle?.set("deve_atualizar", true)
                                navController.popBackStack()
                            }

                        } catch (e: Exception) {
                            Log.e("BindTag", "Erro ao vincular etiqueta", e)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pular esta etapa",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Destination.InventoryScreen.route)
                    }
                    .padding(vertical = 8.dp)
            )

        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light Mode"
)
@Composable
private fun BindTagScreenPreview() {
    BipTagTheme {
        BindTagScreen(navController = rememberNavController(), itemId = 0)
    }
}