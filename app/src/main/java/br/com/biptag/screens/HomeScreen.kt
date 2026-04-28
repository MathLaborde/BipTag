package br.com.biptag.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.biptag.components.MyBottomBar
import br.com.biptag.components.TopBar
import br.com.biptag.navigation.Destination
import br.com.biptag.ui.theme.BipTagTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopBar(title = "Meus itens") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController
                        .navigate(
                            Destination.InventoryFormScreen.route
                        )
                },
                containerColor = Color(0xFF222222),
                contentColor = Color.White,
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar item")
            }
        },
        bottomBar = { MyBottomBar() },
        containerColor = Color(0xFFFAFAFA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            var searchQuery by remember { mutableStateOf("") }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar item...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = Color(0xFF333333),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                singleLine = true
            )
            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = "0 itens cadastrados",
                color = Color.Gray,
                fontSize = 14.sp
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 80.dp), // Espaço para o FAB
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Usar o "items()" quando tiver os dados.
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    BipTagTheme() {
        HomeScreen(rememberNavController())
    }
}

@Composable
fun ItemCard(
    title: String,
    category: String,
    statusText: String,
    isVerified: Boolean
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = category,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Surface(
                color = if (isVerified) Color(0xFF333333) else Color(0xFFEAEAEA),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = statusText,
                    color = if (isVerified) Color.White else Color(0xFF666666),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ItemCardPreview() {
    BipTagTheme() {
        ItemCard(
            "Notebook",
            "Eletrônicos",
            "Verificado",
            true
        )
    }
}

