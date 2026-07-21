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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.R
import br.com.biptag.components.BipTagTextField
import br.com.biptag.components.PrimaryButton
import br.com.biptag.components.TopBar
import br.com.biptag.model.Category
import br.com.biptag.model.Item
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.AuthRepository
import br.com.biptag.repository.CategoryRepository
import br.com.biptag.repository.ItemRepository
import br.com.biptag.ui.theme.BipTagTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ItemFormScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.new_item),
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = {
                    navController.popBackStack()
                }
            )
        },
    ) { paddingValues ->
        ContentItemFormScreen(modifier = Modifier.padding(paddingValues), navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentItemFormScreen(modifier: Modifier, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var expanded by remember { mutableStateOf(false) }

    var isSaving by remember { mutableStateOf(false) }

    val context = LocalContext.current

    var itemImage by remember {
        mutableStateOf<Bitmap?>(null)
    }

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
    val repository = remember { if (isPreview) null else CategoryRepository() }

    val scope = rememberCoroutineScope()
    val itemRepository = remember { if (isPreview) null else ItemRepository() }
    val authRepository = remember { if (isPreview) null else AuthRepository() }

    LaunchedEffect(Unit) {
        if (isPreview) return@LaunchedEffect
        try {
            val result = repository?.getAllItems() ?: emptyList()
            Log.d("Supabase", "Categorias carregados: $result")
            categoryList = result
        } catch (e: Exception) {
            Log.e("Supabase", "Erro ao carregar categorias", e)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        UserImage(
            profileImage = itemImage,
            launchImage = launchImage
        )

        Spacer(Modifier.size(16.dp))

        Text(
            text = "Nome do item",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.labelMedium
        )
        BipTagTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = {
                Text(
                    text = "Ex.: Notebook Dell...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector =  Icons.Outlined.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
        )
        Text(
            text = "Descrição",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.labelMedium
        )
        BipTagTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = {
                Text(
                    "Marca, modelo, número de série...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Category,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            modifier = Modifier
                .height(100.dp),
            singleLine = false
        )

        Text(
            text = "Categoria",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.labelMedium
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
                    Text(
                        text = "Selecione uma categoria",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Category,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                categoryList.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(text = item.name)
                        },
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

        PrimaryButton(
            enabled = !isSaving,
            onClick = {
                isSaving = true

                scope.launch {
                    try {
                        val user = authRepository?.getCurrentUser() ?: run {
                            Log.e("ItemForm", "Usuário não autenticado")
                            isSaving = false
                            return@launch
                        }

                        var imageUrl: String? = null

                        itemImage?.let { bitmap ->
                            val stream = java.io.ByteArrayOutputStream()
                            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, stream)
                            val byteArray = stream.toByteArray()
                            val fileName = "item_${System.currentTimeMillis()}.jpg"
                            imageUrl = itemRepository?.uploadImage(user.id, fileName, byteArray)
                        }

                        val newItem = Item(
                            name = name,
                            description = description,
                            category = selectedCategory?.id ?: 0,
                            userId = user.id,
                            image = imageUrl
                        )

                        val itemSalvo = itemRepository?.saveItem(newItem)

                        withContext(Dispatchers.Main) {
                            isSaving = false

                            navController.previousBackStackEntry?.savedStateHandle?.set("deve_atualizar", true)

                            itemSalvo?.id?.let { idGerado ->
                                navController.navigate(Destination.BindTagScreen.createRoute(idGerado)) {
                                    popUpTo(Destination.InventoryFormScreen.route) { inclusive = true }
                                }
                            } ?: run {
                                navController.popBackStack()
                            }
                        }

                    } catch (e: Exception) {
                        Log.e("ItemForm", "Erro ao salvar item", e)
                        withContext(Dispatchers.Main) {
                            isSaving = false
                        }
                    }
                }
            },
            text = if (isSaving) "Salvando..." else stringResource(R.string.save)
        )
    }
}

@Composable
fun UserImage(
    profileImage: Bitmap?,
    launchImage: ManagedActivityResultLauncher<String, Uri?>
) {
    val dashColor = Color(0xFF9FC6DA)
    val stroke = remember {
        Stroke(
            width = 16f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(144.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .drawBehind {
                drawRoundRect(
                    color = dashColor,
                    style = stroke,
                    cornerRadius = CornerRadius(16.dp.toPx())
                )
            }
            .clickable {
                launchImage.launch("image/*")
            },
        contentAlignment = Alignment.Center,
    ) {
        if (profileImage != null) {
            Image(
                bitmap = profileImage.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(52.dp),
                    shadowElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = "Camera Icon",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Adicionar foto do item",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Text(
                    text = "Toque para tirar ou enviar uma foto",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun ContentItemFormScreenPreview() {
    BipTagTheme {
        ContentItemFormScreen(modifier = Modifier, rememberNavController())
    }
}
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun ItemFormScreenPreview() {

    val context = LocalContext.current

    var itemImage by remember {
        mutableStateOf<Bitmap?>(null)
    }

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

    BipTagTheme {
        UserImage(
            profileImage = itemImage,
            launchImage = launchImage
        )
    }
}
