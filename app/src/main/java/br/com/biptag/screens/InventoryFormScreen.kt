package br.com.biptag.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.biptag.R
import br.com.biptag.model.Inventory
import br.com.biptag.repository.RoomInventoryRepository
import br.com.biptag.ui.theme.BipTagTheme
import br.com.biptag.ui.theme.Black
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun InventoryFormScreen() {

    Scaffold(
        topBar = {
            MyTopInventoryFormBar()
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Card (
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(8.dp)
            ) {
            }

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
                shape = RoundedCornerShape(16.dp),
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
                shape = RoundedCornerShape(16.dp),
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
                shape = RoundedCornerShape(16.dp),
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
                val inventory = Inventory(name = name, description = description, category = category, userId = 0)

                scope.launch(Dispatchers.IO) {
                    try {
                        inventoryRepository.save(inventory)
                    } catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Black)
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
fun MyTopInventoryFormBar () {
    Column(){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(R.string.new_item),
                style = MaterialTheme.typography.titleMedium,
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
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
