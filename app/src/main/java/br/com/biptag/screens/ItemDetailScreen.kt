package br.com.biptag.screens

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.Category
import br.com.biptag.model.Item
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.ItemRepository
import br.com.biptag.ui.theme.BipTagTheme
import coil.compose.AsyncImage

@Composable
fun ItemDetailScreen(navController: NavController, itemId: Int) {

    val isPreview = LocalInspectionMode.current
    val repository = remember { if (isPreview) null else ItemRepository() }
    var item by remember { mutableStateOf<Item?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(itemId) {
        if (isPreview) return@LaunchedEffect
        try {
            isLoading = true
            item = repository?.getItemById(itemId)
        } catch (e: Exception) {
            Log.e("ItemDetail", "Erro ao carregar detalhes do item", e)
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Detalhes",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(color = MaterialTheme.colorScheme.surface)
                ) {
                    BottomActionButtons(navController, item?.id)
                }
            }
        },
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (item == null) {
            Box(modifier = Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Item não encontrado.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                val currentItem = item!!
                ItemHeaderSection(currentItem)
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    ItemTitleSection(currentItem)
                    Spacer(modifier = Modifier.height(16.dp))
                    RfidActionCard(navController, currentItem)
                    Spacer(modifier = Modifier.height(16.dp))
                    ItemDetailsListCard(currentItem)
                }
            }
        }
    }
}

@Composable
fun ItemHeaderSection(item: Item) {

    val statusColor = when (item.status) {
        "Verified", "Verificado" -> MaterialTheme.colorScheme.primary
        "Stolen", "Roubado" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.15f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = item.status,
                    modifier = Modifier.size(14.dp),
                    tint = statusColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = item.status,
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor
                )
            }
        }
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = item.image,
                contentDescription = "Foto do Item",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ItemTitleSection(item: Item) {
    Column {
        Text(
            text = item.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.categoryData?.name ?: "Sem categoria",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
fun RfidActionCard(navController: NavController, item: Item) {

    val isLinked = item.tagId != null

    if (!isLinked) {
        val dashColor = MaterialTheme.colorScheme.onSecondary
        val stroke = remember {
            Stroke(
                width = 5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 16f), 0f)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSecondary
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .drawBehind {
                                drawRoundRect(
                                    color = dashColor,
                                    style = stroke,
                                    cornerRadius = CornerRadius(10.dp.toPx())
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Sell,
                            contentDescription = "Sem etiqueta",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Sem etiqueta vinculada",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Vincule uma etiqueta RFID para proteger e rastrear este item",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(
                    text = "Vincular etiqueta",
                    icon = Icons.Outlined.Wifi,
                    onClick = { navController.navigate(Destination.BindTagScreen.route) }
                )
            }
        }
    } else {
        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .background(
                        color = Color(0xFFE1EFF6),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFC2DDEA),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Sell,
                        contentDescription = "Ícone de etiqueta",
                        tint = MaterialTheme.colorScheme.surface,
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Etiqueta vinculada",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tag #${item.tagId}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFFFE16D),
                            shape = CircleShape
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Protegido",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = "Reportar perda ou roubo",
                icon = Icons.Outlined.WarningAmber,
                containerColor = Color(0xFFDC4D3C),
                onClick = { }
            )
        }
    }
}

@Composable
fun ItemDetailsListCard(item: Item) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column {
            DetailRowItem(
                icon = Icons.Outlined.GridView,
                label = "Categoria",
                value = item.categoryData?.name ?: "Nenhuma"
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )
            DetailRowItem(
                icon = Icons.Outlined.Notes,
                label = "Descrição",
                value = item.description ?: "Sem descrição"
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )
            DetailRowItem(
                icon = Icons.Outlined.ReceiptLong,
                label = "Selo de procedência",
                value = "Nota fiscal validada por IA"
            )
        }
    }
}

@Composable
fun DetailRowItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun BottomActionButtons(navController: NavController, itemId: Int?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrimaryButton(
            text = "Editar Item",
            icon = Icons.Outlined.Edit,
            onClick = {
                itemId?.let { id ->
                    navController.navigate(Destination.EditItemScreen.route)
                }
            },
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        )
        Button(
            onClick = {},
            modifier = Modifier
                .width(64.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Excluir item",
                modifier = Modifier.size(24.dp)
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
private fun ItemDetailScreenPreview() {
    BipTagTheme {
        val mockItem = Item(
            id = 1,
            name = "Notebook Dell Inspiron",
            description = "i7, 16GB RAM · série 5GZ8X92",
            category = 1,
            categoryData = Category(1, "Eletrônicos"),
            status = "Verificado",
            tagId = "A4B2-99F0"
        )

        Scaffold { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                ItemHeaderSection(mockItem)
                ItemTitleSection(mockItem)
                RfidActionCard(rememberNavController(), mockItem)
                ItemDetailsListCard(mockItem)
            }
        }
    }
}