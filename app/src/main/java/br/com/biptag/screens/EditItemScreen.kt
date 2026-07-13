package br.com.biptag.screens

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.BipTagTextField
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.Category
import br.com.biptag.model.Item
import br.com.biptag.repository.CategoryRepository
import br.com.biptag.repository.ItemRepository
import br.com.biptag.ui.theme.BipTagTheme
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

@Composable
fun EditItemScreen(navController: NavController, itemId: Int = 0) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Editar item",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = { navController.popBackStack() }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        ContentEditItemScreen(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            itemId = itemId
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentEditItemScreen(modifier: Modifier, navController: NavController, itemId: Int) {

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var isLinked by remember { mutableStateOf(false) }
    var tagIdText by remember { mutableStateOf("") }

    var existingImageUrl by remember { mutableStateOf<String?>(null) }
    var itemImage by remember { mutableStateOf<Bitmap?>(null) }

    var originalItem by remember { mutableStateOf<Item?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var expanded by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            if (Build.VERSION.SDK_INT < 28) {
                itemImage = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                itemImage = ImageDecoder.decodeBitmap(source)
            }
        }
    }

    val scrollState = rememberScrollState()
    var categoryList by remember { mutableStateOf(listOf<Category>()) }

    val isPreview = LocalInspectionMode.current
    val categoryRepository = remember { if (isPreview) null else CategoryRepository() }
    val scope = rememberCoroutineScope()
    val itemRepository = remember { if (isPreview) null else ItemRepository() }

    LaunchedEffect(itemId) {
        if (isPreview) return@LaunchedEffect
        try {
            isLoading = true

            val categories = categoryRepository?.getAllItems() ?: emptyList()
            categoryList = categories

            if (itemId > 0) {
                val item = itemRepository?.getItemById(itemId)
                if (item != null) {
                    originalItem = item
                    name = item.name
                    description = item.description ?: ""
                    existingImageUrl = item.image
                    isLinked = item.tagId != null
                    tagIdText = item.tagId ?: ""
                    selectedCategory = categoryList.find { it.id == item.category } ?: item.categoryData
                }
            }
        } catch (e: Exception) {
            Log.e("EditItem", "Erro ao carregar dados", e)
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .verticalScroll(scrollState),
    ) {
        EditImageUploadArea(
            itemImage = itemImage,
            existingImageUrl = existingImageUrl,
            launchImage = launchImage
        )

        Spacer(Modifier.size(16.dp))

        Text(
            text = "Nome do item",
            modifier = Modifier.padding(bottom = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        BipTagTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Nome do item", color = MaterialTheme.colorScheme.onBackground) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )

        Spacer(Modifier.size(16.dp))

        Text(
            text = "Descrição",
            modifier = Modifier.padding(bottom = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        BipTagTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Descrição", color = MaterialTheme.colorScheme.onBackground) },
            leadingIcon = {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notes,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            modifier = Modifier.height(100.dp),
            singleLine = false
        )

        Spacer(Modifier.size(16.dp))

        Text(
            text = "Categoria",
            modifier = Modifier.padding(bottom = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            BipTagTextField(
                value = selectedCategory?.name ?: "",
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text("Selecione uma categoria", color = MaterialTheme.colorScheme.onBackground)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.GridView,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                categoryList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            selectedCategory = item
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.primary,
                            leadingIconColor = MaterialTheme.colorScheme.primary,
                            trailingIconColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

        Spacer(Modifier.size(16.dp))

        Text(
            text = "Etiqueta RFID",
            modifier = Modifier.padding(bottom = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        if (!isLinked) {
            val dashColor = MaterialTheme.colorScheme.onSecondary
            val stroke = remember {
                Stroke(
                    width = 7f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(22f, 16f), 0f)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .drawBehind {
                        drawRoundRect(
                            color = dashColor,
                            style = stroke,
                            cornerRadius = CornerRadius(12.dp.toPx())
                        )
                    }
                    .clickable {}
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Outlined.Sell,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Vincular etiqueta",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Nenhuma etiqueta vinculada",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = "Avançar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else {
            Column() {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Sell,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Tag #$tagIdText",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Etiqueta vinculada",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        OutlinedButton(
                            onClick = { },
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Wifi,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Trocar",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { }
                        .padding(vertical = 12.dp, horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Remover etiqueta",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(15.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Remover etiqueta",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Spacer(Modifier.size(14.dp))

        PrimaryButton(
            enabled = !isSaving,
            onClick = {
                isSaving = true
                scope.launch {
                    try {
                        var finalImageUrl = existingImageUrl

                        if (itemImage != null && originalItem != null) {

                            val stream = ByteArrayOutputStream()
                            itemImage!!.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                            val byteArray = stream.toByteArray()
                            val fileName = "item_${originalItem!!.id}_${System.currentTimeMillis()}.jpg"
                            val userId = "usuario_logado_id"
                            val novaUrl = itemRepository?.uploadImage(userId, fileName, byteArray)
                            if (novaUrl != null) {
                                finalImageUrl = novaUrl
                            }
                        }

                        val updatedItem = originalItem?.let { itemAtual ->
                            itemAtual.copy(
                                name = name,
                                description = description,
                                category = selectedCategory?.id ?: itemAtual.category,
                                image = finalImageUrl
                            )
                        }

                        if (updatedItem != null) {
                            itemRepository?.updateItem(updatedItem)
                        }

                        withContext(Dispatchers.Main) {
                            isSaving = false
                            navController.popBackStack()
                        }
                    } catch (e: Exception) {
                        Log.e("EditItem", "Erro ao atualizar item", e)
                        isSaving = false
                    }
                }
            },
            text = if (isSaving) "Salvando..." else "Salvar alterações"
        )
    }
}

@Composable
fun EditImageUploadArea(
    itemImage: Bitmap?,
    existingImageUrl: String?,
    launchImage: ManagedActivityResultLauncher<String, Uri?>
) {
    val dashColor = MaterialTheme.colorScheme.onSecondary
    val stroke = remember {
        Stroke(
            width = 6f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 16f), 0f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .drawBehind {
                drawRoundRect(
                    color = dashColor,
                    style = stroke,
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            }
            .clickable { launchImage.launch("image/*") },
        contentAlignment = Alignment.Center,
    ) {
        if (itemImage != null) {
            Image(
                bitmap = itemImage.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        else if (existingImageUrl != null){
            AsyncImage(
                model = existingImageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(52.dp),
                    shadowElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = "Trocar foto",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Trocar foto",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "Foto atual do item",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun EditItemScreenPreview() {
    BipTagTheme {
        EditItemScreen(navController = rememberNavController())
    }
}