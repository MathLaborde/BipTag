package br.com.biptag.screens

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.R
import br.com.biptag.components.TopBar
import br.com.biptag.factory.RetrofitClient
import br.com.biptag.model.Inventory
import br.com.biptag.navigation.Destination
import br.com.biptag.repository.SharedPreferencesUserRepository
import br.com.biptag.ui.theme.BipTagTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun InventoryFormScreen(navController: NavController) {
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
        ContentInventoryFormScreen(modifier = Modifier.padding(paddingValues), navController = navController)
    }
}

@Composable
fun ContentInventoryFormScreen(modifier: Modifier, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) } // Controle para não clicar 2x no botão

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            UserImage(
                profileImage = itemImage,
                launchImage = launchImage
            )
            Text(
                text = "Nome do item",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                placeholder = { Text("Ex.: Notebook Dell...", color = Color.LightGray) },
                leadingIcon = {
                    Icon(Icons.Outlined.Inventory2, contentDescription = null, tint = Color.Gray)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray
                )
            )
            Column(
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                Text(
                    text = "Descrição",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = {
                        Text(
                            "Marca, modelo, número de série...",
                            color = Color.LightGray
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray)
                )
            }
            Column(
                modifier = Modifier.padding(vertical = 6.dp)
            ) {
                Text(
                    text = "Categoria",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    singleLine = true,
                    placeholder = { Text("Ex.: Eletrônicos...", color = Color.LightGray) },
                    leadingIcon = {
                        Icon(Icons.Outlined.Category, contentDescription = null, tint = Color.Gray)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray)
                )
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !isSaving, // Desabilita enquanto salva
            onClick = {
                isSaving = true
                val userShared = SharedPreferencesUserRepository(context)

                // Cria o objeto sem passar o 'id' (a API cria automático) e usando 'image = null'
                val inventory = Inventory(
                    name = name,
                    description = description,
                    category = category,
                    userId = "00000000-0000-0000-0000-000000000000",
                    image = null // Supabase exige URL em String. Faremos upload da imagem em outro passo.
                )

                val apiKey = "sb_publishable_-uStjsesRzkPg7vwDmha9g_F17ohYyw"
                val token = "Bearer ${apiKey}"

                // Chamada da API com Retrofit
                RetrofitClient.getInventoryService().insertInventory(apiKey, token, inventory)
                    .enqueue(object : Callback<List<Inventory>> {
                        override fun onResponse(
                            call: Call<List<Inventory>>,
                            response: Response<List<Inventory>>
                        ) {
                            isSaving = false
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Item criado com sucesso!", Toast.LENGTH_SHORT).show()
                                navController.navigate(Destination.InventoryScreen.route) {
                                    popUpTo(Destination.InventoryFormScreen.route) { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "Erro: ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<List<Inventory>>, t: Throwable) {
                            isSaving = false
                            t.printStackTrace()
                            Toast.makeText(context, "Falha de conexão", Toast.LENGTH_SHORT).show()
                        }
                    })
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
            .padding(8.dp)
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
private fun InventoryFormScreenPreview() {
    BipTagTheme {
        InventoryFormScreen(rememberNavController())
    }
}
