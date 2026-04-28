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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.biptag.R
import br.com.biptag.components.BottomBar
import br.com.biptag.components.TopBar
import br.com.biptag.model.Inventory
import br.com.biptag.repository.RoomInventoryRepository
import br.com.biptag.ui.theme.BipTagTheme
import br.com.biptag.ui.theme.Black
import br.com.biptag.ui.theme.DarkGray
import br.com.biptag.ui.theme.Gray
import br.com.biptag.ui.theme.Red
import br.com.biptag.ui.theme.White
import br.com.fiap.recipes.utils.convertByteArrayToBitmap

@Composable
fun InventoryScreen() {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.inventory),
                endIcon = Icons.Default.Tune
            )
        },
        bottomBar = {
            BottomBar()
        },
    ) { paddingValues ->
        ContentInventoryScreen(modifier = Modifier.padding(paddingValues))

    }
}

@Composable
fun ContentInventoryScreen(modifier: Modifier) {
    val context = LocalContext.current
    val inventoryRepository = remember { RoomInventoryRepository(context) }
    
    // Observa o Flow do banco de dados em tempo real
    val items by inventoryRepository.getAllByUser(0).collectAsState(initial = emptyList())

    Column(
        modifier = modifier
    ) {
        MySearchBar()

        Text(
            text = "${items.size} items found",
            modifier = Modifier.padding(horizontal = 16.dp),
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

    val statusColor = when (item.status) {
        "Created" -> Gray
        "Verified" -> Black
        "Stolen" -> Red
        else -> Gray
    }

    val statusTextColor = when (item.status) {
        "Created" -> Black
        "Verified" -> White
        "Stolen" -> White
        else -> Black
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
            .padding(vertical = 4.dp, horizontal = 12.dp)
            .border(1.dp, Gray, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                modifier = Modifier
                    .size(56.dp),
                shape = RoundedCornerShape(8.dp)
            ){
                Image(
                    bitmap = itemImage.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(56.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkGray
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        color = statusColor,
                        shape = RoundedCornerShape( 8.dp)
                    ),
            ){
                Text(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 12.dp),
                    text = item.status,
                    color = statusTextColor,
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
        label = { Text(stringResource(R.string.search)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.outlineVariant
            )
        },
        shape = RoundedCornerShape(8.dp)
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
private fun InventoryScreenPreview() {
    BipTagTheme {
        InventoryScreen()
    }
}
