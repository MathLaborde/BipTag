package br.com.biptag.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.biptag.screens.BindTagScreen
import br.com.biptag.screens.CreditsScreen
import br.com.biptag.screens.EditItemScreen
import br.com.biptag.screens.InitialScreen
import br.com.biptag.screens.ItemFormScreen
import br.com.biptag.screens.InventoryScreen
import br.com.biptag.screens.ItemDetailScreen
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
        composable(Destination.InitialScreen.route) {
            InitialScreen(navController)
        }
        composable(Destination.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Destination.SignUpScreen.route) {
            SignUpScreen(navController)
        }
        composable(Destination.InventoryScreen.route) {
            InventoryScreen(navController)
        }
        composable(Destination.CreditsScreen.route) {
            CreditsScreen(navController)
        }
        composable(Destination.InventoryFormScreen.route) {
            ItemFormScreen(navController)
        }
        composable(Destination.BindTagScreen.route) {
            BindTagScreen(navController)
        }
        composable(Destination.ProfileScreen.route) {
            ProfileScreen(navController)
        }
        // Telas dinâmicas com argumentos
        composable(
            route = Destination.ItemDetailScreen.route,
            arguments = listOf(navArgument("itemId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            ItemDetailScreen(navController = navController, itemId = itemId)
        }
        composable(Destination.EditItemScreen.route) {
            EditItemScreen(navController)
        }
    }
}