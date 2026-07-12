package br.com.biptag.navigation

sealed class Destination(val route: String){

    // Rotas Simples
    object InitialScreen : Destination("inital")
    object LoginScreen : Destination("login")
    object SignUpScreen : Destination("signup")

    object InventoryScreen : Destination("inventory")
    object CreditsScreen : Destination("credits")
    object InventoryFormScreen : Destination("inventory_form")
    object BindTagScreen : Destination("bind_tag_screen")
    object ItemDetailScreen : Destination("item_detail_screen/{itemId}") {
        fun createRoute(itemId: Int): String {
            return "item_detail_screen/$itemId"
        }
    }
    object EditItemScreen : Destination("edit_item_screen")
    object ProfileScreen : Destination("profile")


}