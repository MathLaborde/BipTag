package br.com.biptag.screens

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.biptag.R
import br.com.biptag.components.TopBar
import br.com.biptag.model.Inventory
import br.com.biptag.repository.RoomInventoryRepository
import br.com.biptag.ui.theme.BipTagTheme
import br.com.biptag.ui.theme.Black
import br.com.fiap.recipes.utils.convertBitmapToByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun InventoryFormScreen() {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.new_item),
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack
            )
        },
    ) { paddingValues ->
        ContentInventoryFormScreen(modifier = Modifier.padding(paddingValues))
    }
}

@Composable
fun ContentInventoryFormScreen(modifier: Modifier) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val inventoryRepository = remember { RoomInventoryRepository(context) }

    // Criar uma variável que armazena uma
    // imagem default para o perfil
    val placeholderImage = BitmapFactory
        .decodeResource(
            context.resources,
            R.drawable.no_image
        )

    // Armazenar a imagem de profile
    // em uma variável de estado do tipo Bitmap
    var itemImage by remember {
        mutableStateOf<Bitmap>(placeholderImage)
    }

    // Criar um lançador de atividade para
    // abrir a galeria de imagens
    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri ->
        if (Build.VERSION.SDK_INT < 28){
            itemImage = MediaStore
                .Images
                .Media
                .getBitmap(
                    context.contentResolver,
                    uri
                )
        } else {
            if (uri != null){
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                itemImage = ImageDecoder.decodeBitmap(source)
            } else{
                itemImage = placeholderImage
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {

            UserImage(
                profileImage = itemImage,
                launchImage = launchImage
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Inventory2,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Description,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)

            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text(stringResource(R.string.category)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Category,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.outlineVariant
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }

        Button (
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 8.dp),
            onClick = {
                // Configurar depois o userId
                val inventory = Inventory(
                    name = name,
                    description = description,
                    category = category,
                    userId = 0,
                    image = convertBitmapToByteArray(itemImage)
                )

                scope.launch(Dispatchers.IO) {
                    try {
                        inventoryRepository.save(inventory)
                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Black)
        ) {
            Text(stringResource(R.string.save))
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    launchImage.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ){
            if (profileImage != null) {
                Image(
                    bitmap = profileImage.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(360.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.AddAPhoto,
                    contentDescription = "",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
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
private fun InventoryFormScreenPreview() {
    BipTagTheme {
        InventoryFormScreen()
    }
}
