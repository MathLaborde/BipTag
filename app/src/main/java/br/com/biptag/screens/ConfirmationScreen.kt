package br.com.biptag.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import br.com.biptag.R
import br.com.biptag.components.TopBar

@Composable
fun ConfirmationScreen(navController: NavController, alertId: Int) {
    Scaffold(
        topBar = {
            TopBar(
                title = "Confirmar Item",
                startIcon = Icons.AutoMirrored.Outlined.ArrowBack,
                onClick = {
                    navController.popBackStack()
                }
            )
        },
    ) { paddingValues ->
        ContentConfirmationScreen(modifier = Modifier.padding(paddingValues), navController = navController)
    }
}

@Composable
fun ContentConfirmationScreen(navController: NavController, modifier: Modifier) {

}