package br.com.biptag.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import br.com.biptag.screens.AlertIssuedScreen
import br.com.biptag.screens.AlertsScreen
import br.com.biptag.screens.BindTagScreen
import br.com.biptag.screens.CreditsScreen
import br.com.biptag.screens.EditItemScreen
import br.com.biptag.screens.InitialScreen
import br.com.biptag.screens.ItemFormScreen
import br.com.biptag.screens.InventoryScreen
import br.com.biptag.screens.ItemDetailScreen
import br.com.biptag.screens.LoginScreen
import br.com.biptag.screens.LostItemScreen
import br.com.biptag.screens.MapsScreen
import br.com.biptag.screens.ProfileScreen
import br.com.biptag.screens.ReportItemScreen
import br.com.biptag.screens.SignUpScreen
import br.com.biptag.screens.ReturnProcessScreen
import br.com.biptag.screens.CollectionPointsScreen
import br.com.biptag.screens.ConfirmationScreen
import br.com.biptag.screens.ItemFoundScreen
import br.com.biptag.screens.RatingScreen
import br.com.biptag.screens.ReturnInstructionScreen
import br.com.biptag.screens.TrackReturnScreen
import br.com.biptag.screens.RequestDriverScreen
import br.com.biptag.screens.DeliveryCodeScreen
import br.com.biptag.screens.DeliveryToOwnerScreen

@Composable
fun NavigationRoutes() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = Destination.InitialScreen.route
    ) {
        // Telas Simples
        composable(
            route = Destination.InitialScreen.route,
            deepLinks = listOf(
                navDeepLink { uriPattern = "biptag://home" }
            )
        ) {
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
        composable(Destination.ProfileScreen.route) {
            ProfileScreen(navController)
        }
        composable(Destination.AlertIssuedScreen.route) {
            AlertIssuedScreen(navController)
        }
        // Telas dinâmicas com argumentos
        composable(
            route = Destination.BindTagScreen.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0

            BindTagScreen(navController = navController, itemId = itemId)
        }
        composable(
            route = Destination.ItemDetailScreen.route, arguments = listOf(navArgument("itemId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            ItemDetailScreen(navController = navController, itemId = itemId)
        }
        composable(
            route = Destination.EditItemScreen.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->

            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0

            EditItemScreen(navController = navController, itemId = itemId)
        }
        composable(
            route = Destination.ReportItemScreen.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->

            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0

            ReportItemScreen(itemId = itemId, onBackClick = {
                navController.popBackStack()
            }, onSuccess = {
                navController.navigate(Destination.AlertIssuedScreen.route)
            })
        }
        composable(Destination.AlertsScreen.route) {
            AlertsScreen(navController = navController)
        }
        composable(Destination.MapsScreen.route) {
            MapsScreen(
                navController = navController, onItemClick = { clickedAlertId ->
                    navController.navigate(Destination.LostItemScreen.createRoute(clickedAlertId))
                })
        }
        composable(
            route = Destination.LostItemScreen.route,
            arguments = listOf(navArgument("alertId") { type = NavType.IntType })
        ) { backStackEntry ->
            val alertId = backStackEntry.arguments?.getInt("alertId") ?: 0
            LostItemScreen(navController = navController, alertId = alertId)
        }
        composable(
            route = Destination.ConfirmationScreen.route,
            arguments = listOf(navArgument("alertId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val alertId = backStackEntry.arguments?.getInt("alertId") ?: 0
            ConfirmationScreen(navController = navController, alertId = alertId)
        }
        composable(
            route = Destination.ItemFoundScreen.route,
            arguments = listOf(navArgument("foundReportId") { type = NavType.IntType }),
            deepLinks = listOf(
                navDeepLink { uriPattern = "biptag://item_found/{foundReportId}" }
            )
        ) { backStackEntry ->
            val foundReportId = backStackEntry.arguments?.getInt("foundReportId") ?: 0
            ItemFoundScreen(navController = navController, foundReportId = foundReportId)
        }
        composable(
            route = Destination.ReturnProcessScreen.route,
            arguments = listOf(navArgument("foundReportId") { type = NavType.IntType })
        ) { backStackEntry ->
            val foundReportId = backStackEntry.arguments?.getInt("foundReportId") ?: 0
            ReturnProcessScreen(navController = navController, foundReportId = foundReportId)
        }

        composable(
            route = Destination.PartnerPointsScreen.route,
            arguments = listOf(
                navArgument("foundReportId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val foundReportId = backStackEntry.arguments?.getInt("foundReportId") ?: 0
            CollectionPointsScreen(navController = navController, foundReportId = foundReportId)
        }

        composable(
            route = Destination.ReturnInstructionScreen.route,
            arguments = listOf(navArgument("returnProcessId") { type = NavType.IntType }),
            deepLinks = listOf(
                navDeepLink { uriPattern = "biptag://return_instruction/{returnProcessId}" }
            )
        ) { backStackEntry ->
            val returnProcessId = backStackEntry.arguments?.getInt("returnProcessId") ?: 0
            ReturnInstructionScreen(navController = navController, returnProcessId = returnProcessId)
        }

        composable(
            route = Destination.TrackReturnScreen.route,
            arguments = listOf(navArgument("returnProcessId") { type = NavType.IntType })
        ) { backStackEntry ->
            val returnProcessId = backStackEntry.arguments?.getInt("returnProcessId") ?: 0
            TrackReturnScreen(navController = navController, returnProcessId = returnProcessId)
        }

        composable(
            route = Destination.RatingScreen.route,
            arguments = listOf(navArgument("returnProcessId") { type = NavType.IntType })
        ) { backStackEntry ->
            val returnProcessId = backStackEntry.arguments?.getInt("returnProcessId") ?: 0
            RatingScreen(navController = navController, returnProcessId = returnProcessId)
        }

        composable(
            route = Destination.RequestDriverScreen.route,
            arguments = listOf(navArgument("foundReportId") { type = NavType.IntType })
        ) { backStackEntry ->
            val foundReportId = backStackEntry.arguments?.getInt("foundReportId") ?: 0
            RequestDriverScreen(navController = navController, foundReportId = foundReportId)
        }

        composable(
            route = Destination.DeliveryCodeScreen.route,
            arguments = listOf(navArgument("returnProcessId") { type = NavType.IntType })
        ) { backStackEntry ->
            val returnProcessId = backStackEntry.arguments?.getInt("returnProcessId") ?: 0
            DeliveryCodeScreen(navController = navController, returnProcessId = returnProcessId)
        }

        composable(
            route = Destination.DeliveryToOwnerScreen.route,
            arguments = listOf(navArgument("returnProcessId") { type = NavType.IntType })
        ) { backStackEntry ->
            val returnProcessId = backStackEntry.arguments?.getInt("returnProcessId") ?: 0
            DeliveryToOwnerScreen(navController = navController, returnProcessId = returnProcessId)
        }



    }

}
