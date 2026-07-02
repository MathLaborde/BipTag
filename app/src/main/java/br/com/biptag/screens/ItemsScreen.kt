package br.com.biptag.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.R
import br.com.biptag.components.BipTagTextField
import br.com.biptag.components.BottomBar
import br.com.biptag.components.TopBar
import br.com.biptag.model.Category
import br.com.biptag.model.Item
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AuthRepository
import br.com.biptag.repository.ItemRepository
import br.com.biptag.ui.theme.BipTagTheme
import coil.compose.AsyncImage

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
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Button"
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        ContentInventoryScreen(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun ContentInventoryScreen(modifier: Modifier) {
    val isPreview = LocalInspectionMode.current
    val repository = remember { if (isPreview) null else ItemRepository() }

    var items by remember { mutableStateOf(listOf<Item>()) }

    LaunchedEffect(Unit) {
        if (isPreview) return@LaunchedEffect
        try {
            val result = repository?.getAllItems() ?: emptyList()
            Log.d("Supabase", "Itens carregados: $result")
            items = result
        } catch (e: Exception) {
            Log.e("Supabase", "Erro ao carregar itens", e)
        }
    }


    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        MySearchBar()

        Text(
            text = "${items.size} itens cadastrados",
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelSmall,
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
fun InventoryItem(item: Item) {
    // TODO Implementar tela de loading.

    val statusColor = when (item.status) {
        "Created", "Criado" -> MaterialTheme.colorScheme.surfaceVariant
        "Verified", "Verificado" -> MaterialTheme.colorScheme.secondary
        "Stolen", "Roubado" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val statusTextColor = when (item.status) {
        "Created", "Criado" -> MaterialTheme.colorScheme.onSurfaceVariant
        "Verified", "Verificado" -> MaterialTheme.colorScheme.onSecondary
        "Stolen", "Roubado" -> MaterialTheme.colorScheme.onError
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(18.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
             AsyncImage(
                model = item.image,
                contentDescription = "Imagem de ${item.name}",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(Modifier.size(14.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.categoryData?.name ?: "Sem categoria",
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = statusColor,
                                shape = CircleShape
                            ),
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                            text = item.status,
                            color = statusTextColor,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Sell,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (item.tagId !== null) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = if (item.tagId !== null) {
                                "Etiqueta vinculada"
                            } else {
                                "Sem etiqueta"
                            },
                            maxLines = 1,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (item.tagId !== null) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun InventoryItemPreview() {
    BipTagTheme() {
        val mockItem = Item(
            id = 1,
            name = "Notebook Dell",
            description = "I7 16GB RAM",
            category = 1,
            categoryData = Category(1, "Eletronico"),
            status = "Verificado"
        )

        InventoryItem(item = mockItem)
    }
}

@Composable
fun MySearchBar() {
    var searchText by remember { mutableStateOf("") }

    // TODO fazer busca dos Itens

    BipTagTextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = {
            Text(
                text = "Buscar item...",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        leadingIcon = {
            Icon(Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
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
