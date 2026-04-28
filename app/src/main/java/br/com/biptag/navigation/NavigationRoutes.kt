package br.com.biptag.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.biptag.screens.HomeScreen
import br.com.biptag.screens.InitialScreen
import br.com.biptag.screens.InventoryFormScreen
import br.com.biptag.screens.LoginScreen
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
        composable(Destination.HomeScreen.route){
            HomeScreen(navController)
        }
        composable(Destination.CreditsScreen.route){
            //CreditsScreen(navController)
        }
        composable(Destination.InventoryFormScreen.route){
            InventoryFormScreen(navController)
        }

        // Telas Dinâmicas (com argumento)
        composable(
            route = Destination.ProfileScreen.route,
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            //ProfileScreen(navController, userId)
        }
    }
}