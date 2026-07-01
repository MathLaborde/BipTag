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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.R
import br.com.biptag.components.BipTagTextField
import br.com.biptag.components.TopBar
import br.com.biptag.model.Category
import br.com.biptag.model.Item
import br.com.biptag.repository.CategoryRepository
import br.com.biptag.ui.theme.BipTagTheme

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

    val repository = CategoryRepository()

    LaunchedEffect(Unit) {
        try {
            val result = repository.getAllItems()
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
    ){
        Column(
            modifier = Modifier
                .padding(vertical = 6.dp)
        ) {
            UserImage(
                profileImage = itemImage,
                launchImage = launchImage
            )
            Text(
                text = "Nome do item",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.labelMedium
            )
            BipTagTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
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
                modifier = Modifier.fillMaxWidth(),
            )
            Column(
                modifier = Modifier.padding(vertical = 18.dp)
            ) {
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
            }
            Column() {
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
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
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
            }
        }

        Spacer(modifier = Modifier.weight(1f, fill = true))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isSaving,
            onClick = {
                isSaving = true

                val Item = Item(
                    name = name,
                    description = description,
                    category = selectedCategory ?: Category(0, "Outros"),
                    userId = "00000000-0000-0000-0000-000000000000",
                    image = null
                )

                // TODO Salvar Item
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(if (isSaving) "Salvando..." else stringResource(R.string.save), color = Color.White)
        }
    }
}

@Composable
fun UserImage(
    profileImage: Bitmap?,
    launchImage: ManagedActivityResultLauncher<String, Uri?>
) {
    Card (
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults
            .cardColors(
                containerColor = MaterialTheme.colorScheme.outlineVariant
            ),
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    launchImage.launch("image/*")
                },
            contentAlignment = Alignment.Center,
        ){
            if (profileImage != null) {
                Image(
                    bitmap = profileImage.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PhotoCamera,
                        contentDescription = "Adicionar foto",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Adicionar foto do item",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun ItemFormScreenPreview() {
    BipTagTheme {
        ItemFormScreen(rememberNavController())
    }
}
