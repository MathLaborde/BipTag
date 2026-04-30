package br.com.biptag.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.R
import br.com.biptag.components.BottomBar
import br.com.biptag.components.TopBar
import br.com.biptag.model.Inventory
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.RoomInventoryRepository
import br.com.biptag.repository.SharedPreferencesUserRepository
import br.com.biptag.ui.theme.BipTagTheme
import br.com.fiap.recipes.utils.convertByteArrayToBitmap

@Composable
fun InventoryScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.inventory),
                endIcon = Icons.Default.Tune
            )
        },
        bottomBar = {
            BottomBar(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Destination.InventoryFormScreen.route) },
                shape = CircleShape,
                // O Compose já puxa as cores do BipTagTheme automaticamente aqui!
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Button"
                )
            }
        },
        // Adicionamos a cor de fundo no Scaffold para garantir consistência
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        ContentInventoryScreen(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun ContentInventoryScreen(modifier: Modifier) {
    val context = LocalContext.current
    val inventoryRepository = remember { RoomInventoryRepository(context) }
    val userShared = SharedPreferencesUserRepository(context)

    // Observa o Flow do banco de dados em tempo real
    val items by inventoryRepository.getAllByUser(userShared.getUser().id).collectAsState(initial = emptyList())

    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        MySearchBar()

        // Texto de contagem atualizado com a tipografia
        Text(
            text = "${items.size} itens cadastrados",
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(items) { item ->
                InventoryItem(item)
            }
        }
    }
}

@SuppressLint("LocalContextResourcesRead")
@Composable
fun InventoryItem(item: Inventory) {
    val context = LocalContext.current

    // Lógica de cores baseada no BipTagTheme
    val statusColor = when (item.status) {
        "Created", "Criado" -> MaterialTheme.colorScheme.tertiary // Cinza claro
        "Verified", "Verificado" -> MaterialTheme.colorScheme.primary // Preto do tema
        "Stolen", "Roubado" -> MaterialTheme.colorScheme.error // Vermelho padrão do Material
        else -> MaterialTheme.colorScheme.tertiary
    }

    val statusTextColor = when (item.status) {
        "Created", "Criado" -> MaterialTheme.colorScheme.onBackground // Preto
        "Verified", "Verificado" -> MaterialTheme.colorScheme.onPrimary // Branco
        "Stolen", "Roubado" -> MaterialTheme.colorScheme.onError // Branco
        else -> MaterialTheme.colorScheme.onBackground
    }

    val itemImage: Bitmap = remember(item.image) {
        if (item.image != null) {
            convertByteArrayToBitmap(item.image)
        } else {
            android.graphics.BitmapFactory.decodeResource(context.resources, R.drawable.no_image)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary, // Borda cinza do tema
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Imagem com shape clip atualizado
            Image(
                bitmap = itemImage.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)), // Mais limpo que usar um Card em volta
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, end = 8.dp) // Respiro melhor entre texto e tag
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium, // Fonte Inter Medium
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.description,
                    maxLines = 1, // Impede que descrições longas quebrem o card
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary // Cinza do tema
                )
            }

            // A Tag de Status
            Box(
                modifier = Modifier
                    .background(
                        color = statusColor,
                        shape = RoundedCornerShape(4.dp) // Cantos mais retos conforme o wireframe
                    ),
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                    text = item.status,
                    color = statusTextColor,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun MySearchBar() {
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = {
            Text("Buscar item...", color = MaterialTheme.colorScheme.secondary)
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = MaterialTheme.colorScheme.secondary)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.tertiary, // Bordas baseadas no tema
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            // Mantém o texto da busca na cor correta
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        singleLine = true
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun InventoryScreenPreview() {
    BipTagTheme {
        InventoryScreen(navController = rememberNavController())
    }
}
