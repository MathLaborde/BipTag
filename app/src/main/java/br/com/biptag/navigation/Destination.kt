package br.com.biptag.navigation

sealed class Destination(val route: String) {

    object InitialScreen : Destination("inital")
    object LoginScreen : Destination("login")
    object SignUpScreen : Destination("signup")
    object InventoryScreen : Destination("inventory")
    object CreditsScreen : Destination("credits")
    object InventoryFormScreen : Destination("inventory_form")
    object BindTagScreen : Destination("bind_tag_screen/{itemId}") {
        fun createRoute(itemId: Int): String {
            return "bind_tag_screen/$itemId"
        }
    }

    object ItemDetailScreen : Destination("item_detail_screen/{itemId}") {
        fun createRoute(itemId: Int): String {
            return "item_detail_screen/$itemId"
        }
    }

    object EditItemScreen : Destination("edit_item_screen/{itemId}") {
        fun createRoute(itemId: Int): String {
            return "edit_item_screen/$itemId"
        }
    }

    object ReportItemScreen : Destination("report_item/{itemId}") {
        fun createRoute(itemId: Int): String {
            return "report_item/$itemId"
        }
    }

    object AlertIssuedScreen : Destination("alert_issued_screen")
    object ProfileScreen : Destination("profile")
    object AlertsScreen : Destination("alerts")
    object MapsScreen : Destination("map_screen")
    object LostItemScreen : Destination("lost_item_screen/{alertId}") {
        fun createRoute(alertId: Int): String {
            return "lost_item_screen/$alertId"
        }
    }

    object ConfirmationScreen : Destination("confirmation_screen/{alertId}") {
        fun createRoute(alertId: Int): String {
            return "confirmation_screen/$alertId"
        }
    }

    object ItemFoundScreen : Destination("item_found_screen/{alertId}") {
        fun createRoute(alertId: Int): String {
            return "item_found_screen/$alertId"
        }
    }

    object ReturnProcessScreen : Destination("return_process_screen/{itemId}") {
        fun createRoute(itemId: Int): String {
            return "return_process_screen/$itemId"
        }
    }

    object PartnerPointsScreen : Destination("partner_points_screen/{itemId}") {
        fun createRoute(itemId: Int): String {
            return "partner_points_screen/$itemId"
        }
    }
}


