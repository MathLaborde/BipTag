package br.com.biptag.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.biptag.screens.CreditsScreen
import br.com.biptag.screens.InitialScreen
import br.com.biptag.screens.InventoryFormScreen
import br.com.biptag.screens.InventoryScreen
import br.com.biptag.screens.LoginScreen
import br.com.biptag.screens.ProfileScreen
import br.com.biptag.screens.SignUpScreen

@Composable
fun NavigationRoutes() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.InitialScreen.route
    ) {
        // Telas Simples
        composable(Destination.InitialScreen.route){
            InitialScreen(navController)
        }
        composable(Destination.LoginScreen.route){
            LoginScreen(navController)
        }
        composable(Destination.SignUpScreen.route){
            SignUpScreen(navController)
        }
        composable(Destination.InventoryScreen.route){
            InventoryScreen(navController)
        }
        composable(Destination.CreditsScreen.route){
            CreditsScreen(navController)
        }
        composable(Destination.InventoryFormScreen.route){
            InventoryFormScreen(navController)
        }

        // Telas Dinâmicas (com argumento)
        composable(Destination.ProfileScreen.route){
            ProfileScreen(navController)
        }
    }
}